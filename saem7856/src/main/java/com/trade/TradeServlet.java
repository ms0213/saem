package com.trade;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.member.SessionInfo;

import saem.util.FileManager;
import saem.util.MyUploadServlet;
import saem.util.MyUtil;

@MultipartConfig
@WebServlet("/trade/*")
public class TradeServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		// 세션 정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// 로그인된 정보가 없으면

		if(info==null) {
			forward(req,resp,"/WEB-INF/saem/member/login.jsp");
			return;
		}
	
		
		// 서버에 파일이 저장되는 위치
		String root = session.getServletContext().getRealPath("/");
					// MyUploadServlet에서 파일을 만듦
		pathname = root + "uploads" + File.separator + "trade";
		
		// uri 구분
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if (uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if (uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("deleteFile.do") != -1) {
			deleteFile(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if(uri.indexOf("insertReply.do") != -1) {
			insertReply(req, resp);
		} else if(uri.indexOf("listReply.do") != -1) {
			listReply(req, resp);
		} else if(uri.indexOf("deleteReply.do") != -1) {
			deleteReply(req, resp);
		} else if(uri.indexOf("insertReplyAnswer.do") != -1) {
			insertReplyAnswer(req, resp);
		} else if(uri.indexOf("listReplyAnswer.do") != -1) {
			listReplyAnswer(req, resp);
		} else if(uri.indexOf("deleteReplyAnswer.do") != -1) {
			deleteReplyAnswer(req, resp);
		} else if(uri.indexOf("countReplyAnswer.do") != -1) {
			countReplyAnswer(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		
		TradeDAO dao = new TradeDAO();
		MyUtil util = new MyUtil(); // 페이지 수
		
		// cp : 최상위 경로 = 프로젝트 명
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page= 1;
			if(page!=null) {
				current_page= Integer.parseInt(page);
			}
			
			// 검색
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition ==null) {
				condition="all";
				keyword="";
			}
			
			// GET 방식인 경우 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}
			
			// 전체 데이터 개수
			int dataCount;
			if(keyword.length()==0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(condition, keyword);
			}
			
			// 전체 페이지 수
			int rows= 10;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page>total_page) current_page = total_page;
			
			int start = (current_page-1)*rows+1;
			int end = current_page*rows;
			
			// 게시물 가져오기
			List<TradeDTO> list = null;
			if(keyword.length()==0) list = dao.listBoard(start, end);
			else list = dao.listBoard(start,end,condition,keyword);
			
			// 리스트 글번호 
			int listNum, n =0;
			for(TradeDTO dto : list) {
				listNum = dataCount - (start+n-1);
				dto.setListNum(listNum);
				n++;
			}
			
			// 공지글
			/*
			List<TradeDTO> listTrade = null;
			listNotice = dao.listTrade();
			for (TradeDTO dto : listTrade) {
				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			*/
			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			
			String query="";
			if(keyword.length()!=0) {
				query = "condition="+condition + "&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
		    // 페이징 처리
			String listUrl = cp +"/trade/list.do";
			String articleUrl = cp +"/trade/article.do?page="+current_page;
			if(query.length()!=0) {
				listUrl += "?" + query;
				articleUrl += "&" +query;
			}
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP로 넘길 속성
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward(req, resp, "/WEB-INF/saem/trade/list.jsp");
	}	
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/saem/trade/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		TradeDAO dao = new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/trade/list.do");
			return;
		}
		
		try {
			TradeDTO dto = new TradeDTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setType(req.getParameter("tradeType"));
			dto.setPay(Integer.parseInt(req.getParameter("pay")));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map!=null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			
			dao.insertTrade(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/trade/list.do");
	}	
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 상세보기
		TradeDAO dao = new TradeDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"UTF-8");
			}
			
			dao.updateHitCount(num);
			
			//게시물 가져오기
			TradeDTO dto = dao.readTrade(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/trade/list.do?"+query);
				return;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			

			List<TradeDTO> listFile = dao.listPhotoFile(num);
			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);
			req.setAttribute("query", query);
			
			// 포워딩
			forward(req, resp, "/WEB-INF/saem/trade/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/trade/list.do?"+query);
	}	
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정 폼
		TradeDAO dao = new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			TradeDTO dto = dao.readTrade(num);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/trade/list.do?page="+page);
				return;
			}
			
			// 게시물을 올린 사용자가 아닐 때
			if( !dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/trade/list.do?page="+page);
				return;
			}
		
			List<TradeDTO> listFile	= dao.listPhotoFile(num);
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);

			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/saem/trade/write.jsp");
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		//resp.sendRedirect(cp+"/trade/list.do?page="+page);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정 저장
		TradeDAO dao = new TradeDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/trade/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		
		try {
			TradeDTO dto = new TradeDTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setType(req.getParameter("tradeType"));
			dto.setContent(req.getParameter("content"));
			dto.setPay(Integer.parseInt(req.getParameter("pay")));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map!=null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			dao.updateTrade(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/trade/list.do?page="+page);
	}
	
	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 안에서 파일만 삭제
		TradeDAO dao = new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			int fileNum = Integer.parseInt(req.getParameter("fileNum"));
			
			TradeDTO dto = dao.readTrade(num);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/trade/list.do?page="+page);
				return;
			}
			
			if(!info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp+"/trade/list.do?page="+page);
				return;
			}
			
			List<TradeDTO> listFile = dao.listPhotoFile(num);
			
			for(TradeDTO vo : listFile) {
				if(vo.getFileNum()==fileNum) {
					FileManager.doFiledelete(pathname, vo.getImageFilename());
					dao.deletePhotoFile("one", fileNum);
					listFile.remove(vo);
					break;
				}
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);

			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/saem/trade/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/trade/list.do?page="+page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 삭제
		TradeDAO dao = new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");

			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			}

			TradeDTO dto = dao.readTrade(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/trade/list.do?"+query);
				return;
			}
			

			// 글을 등록한 사람, admin 만 삭제 가능

			if (!info.getUserId().equals(dto.getUserId()) && !info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/trade/list.do?" + query);
				return;
			}

			dao.deleteTrade(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/trade/list.do?"+query);
	}
	
	// 댓글 추가 (AJAX : JSON)
	protected void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TradeDAO dao = new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		
		try {
			ReplyDTO dto = new ReplyDTO();
			
			int num = Integer.parseInt(req.getParameter("num"));
			dto.setNum(num);
			dto.setUserId(info.getUserId());
			dto.setContent(req.getParameter("content"));
			
			String answer = req.getParameter("answer");
			
			if(answer!=null) dto.setAnswer(Integer.parseInt(answer));
			
			dao.insertReply(dto);
			
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

	// 댓글 리스트
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TradeDAO dao = new TradeDAO();
		MyUtil util = new MyUtil();
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if(pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			
			int rows = 5;
			int total_page = 0;
			int replyCount = 0;
			
			replyCount = dao.dataCountReply(num);
			total_page = util.pageCount(rows, replyCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			
			List<ReplyDTO> listReply = dao.listReply(num, start, end);
			
			for(ReplyDTO dto : listReply) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			req.setAttribute("listReply", listReply);
			req.setAttribute("pageNo", current_page);
			req.setAttribute("replyCount", replyCount);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			
			forward(req, resp, "/WEB-INF/saem/trade/listReply.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendError(405);

	}

	// 댓글 삭제
	protected void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TradeDAO dao= new TradeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String state = "false";

		try {
			int replyNum = Integer.parseInt(req.getParameter("replyNum"));
			dao.deleteReply(replyNum, info.getUserId());
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());	
		
	}

	// 답글 추가
	protected void insertReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		insertReply(req, resp);
	}

	// 답글 리스트
	protected void listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TradeDAO dao = new TradeDAO();
		
		try {
			int answer = Integer.parseInt(req.getParameter("answer"));
			List<ReplyDTO> listReplyAnswer= dao.listReplyAnswer(answer);
			for(ReplyDTO dto : listReplyAnswer) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			
			req.setAttribute("listReplyAnswer", listReplyAnswer);
			forward(req, resp, "/WEB-INF/saem/trade/listReplyAnswer.jsp");
			
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendError(405);
	}

	// 답글 삭제
	protected void deleteReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		deleteReply(req, resp);
	}

	// 답글 개수
	protected void countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TradeDAO dao = new TradeDAO();
		int count = 0;
		
		try {
			int answer= Integer.parseInt(req.getParameter("answer"));
			count = dao.dataCountReplyAnswer(answer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject job = new JSONObject();
		job.put("count", count);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());			
		
	}

}
