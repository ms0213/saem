package com.bbs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.member.SessionInfo;


import saem.util.MyServlet;
import saem.util.MyUtil;

@WebServlet("/bbs/*")
public class BoardServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();

		// 세션 정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			 forward(req, resp, "/WEB-INF/saem/member/login.jsp");
			 return;
		}

		//		 uri에 따른 작업 구분
		if (uri.indexOf("list.do") != -1) {
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
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("notice.do") != -1) {
			noticeForm(req, resp);
		} else if (uri.indexOf("notice_ok.do") != -1) {
			noticeSubmit(req, resp);
		} else if (uri.indexOf("insertBoardLike.do") != -1) {
			// 게시물 공감 저장
			insertBoardLike(req, resp);
		} else if (uri.indexOf("insertReply.do") != -1) {
			// 댓글 추가
			insertReply(req, resp);
		} else if (uri.indexOf("listReply.do") != -1) {
			// 댓글 리스트
			listReply(req, resp);
		} else if (uri.indexOf("deleteReply.do") != -1) {
			// 댓글 삭제
			deleteReply(req, resp);
		} else if (uri.indexOf("insertReplyLike.do") != -1) {
			// 댓글 좋아요/싫어요 추가
			insertReplyLike(req, resp);
		} else if (uri.indexOf("countReplyLike.do") != -1) {
			// 댓글 좋아요/싫어요 개수
			countReplyLike(req, resp);
		} else if (uri.indexOf("insertReplyAnswer.do") != -1) {
			// 댓글의 답글 추가
			insertReplyAnswer(req, resp);
		} else if (uri.indexOf("listReplyAnswer.do") != -1) {
			// 댓글의 답글 리스트
			listReplyAnswer(req, resp);
		} else if (uri.indexOf("deleteReplyAnswer.do") != -1) {
			// 댓글의 답글 삭제
			deleteReplyAnswer(req, resp);
		} else if (uri.indexOf("countReplyAnswer.do") != -1) {
			// 댓글의 답글 개수
			countReplyAnswer(req, resp);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			if (req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}

			// 한페이지 표시할 데이터 개수
			String numPerPage = req.getParameter("rows");
			int rows = numPerPage == null ? 10 : Integer.parseInt(numPerPage);

			int dataCount, total_page;

			if (keyword.length() != 0) {
				dataCount = dao.dataCount(condition, keyword);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(rows, dataCount);

			if (current_page > total_page) {
				current_page = total_page;
			}

			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;

			List<BoardDTO> list;
			if (keyword.length() != 0) {
				list = dao.listNotice(start, end, condition, keyword);
			} else {
				list = dao.listNotice(start, end);
			}

			// 공지글
			List<BoardDTO> listNotice = null;
			listNotice = dao.listNotice();
			for (BoardDTO dto : listNotice) {
				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}

			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// 리스트 글번호 만들기
			int listNum, n = 0;
			for (BoardDTO dto : list) {
				listNum = dataCount - (start + n - 1);
				dto.setListNum(listNum);

				Date date = sdf.parse(dto.getReg_date());
				// gap = (curDate.getTime() - date.getTime()) / (1000*60*60*24); // 일자
				gap = (curDate.getTime() - date.getTime()) / (1000 * 60 * 60); // 시간
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
				n++;
			}

			String query = "";
			String listUrl;
			String articleUrl;

			listUrl = cp + "/bbs/list.do?rows=" + rows;
			articleUrl = cp + "/bbs/article.do?page=" + current_page + "&rows=" + rows;
			if (keyword.length() != 0) {
				query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");

				listUrl += "&" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩 jsp에 전달할 데이터
			req.setAttribute("list", list);
			req.setAttribute("listNotice", listNotice);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			req.setAttribute("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JSP로 포워딩
		forward(req, resp, "/WEB-INF/saem/bbs/list.jsp");
	}

	private void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/saem/bbs/write.jsp");
	}
	
	private void noticeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 공지사항 글쓰기 폼
		req.setAttribute("mode", "notice");
		forward(req, resp, "/WEB-INF/saem/bbs/writeAdmin.jsp");
	}

	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/bbs/list.do");
			return;
		}
		
		try {
			BoardDTO dto = new BoardDTO();

			// userId는 세션에 저장된 정보
			dto.setUserId(info.getUserId());

			// 파라미터
			if (req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do");
	}
	
	private void noticeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/bbs/list.do");
			return;
		}
		
		try {
			BoardDTO dto = new BoardDTO();

			// userId는 세션에 저장된 정보
			dto.setUserId(info.getUserId());

			// 파라미터
			if (req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

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

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			BoardDTO dto = dao.readBoard(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				resp.sendRedirect(cp + "/bbs/list.do?" + query);
				return;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			// 로그인 유저의 게시글 공감 유무
			boolean isUserLike = dao.isUserBoardLike(num, info.getUserId()); 

			// 이전글 다음글
			BoardDTO preReadDto = dao.preReadBoard(dto.getNum(), condition, keyword);
			BoardDTO nextReadDto = dao.nextReadBoard(dto.getNum(), condition, keyword);

			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			
			req.setAttribute("isUserLike", isUserLike);

			// 포워딩
			forward(req, resp, "/WEB-INF/saem/bbs/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do?" + query);
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();

		String page = req.getParameter("page");

		try {
			int num = Integer.parseInt(req.getParameter("num"));
			BoardDTO dto = dao.readBoard(num);

			if (dto == null) {
				resp.sendRedirect(cp + "/bbs/list.do?page=" + page);
				return;
			}

			// 게시물을 올린 사용자가 아니면
			if (! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/bbs/list.do?page=" + page);
				return;
			}

			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/saem/bbs/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do?page=" + page);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/bbs/list.do");
			return;
		}

		String page = req.getParameter("page");
		try {
			BoardDTO dto = new BoardDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dto.setUserId(info.getUserId());

			dao.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do?page=" + page);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

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

			dao.deleteBoard(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/bbs/list.do?" + query);
	}
	
	// 게시물 공감 저장 - AJAX:JSON
		private void insertBoardLike(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			String state = "false";
			int boardLikeCount = 0;

			try {
				int num = Integer.parseInt(req.getParameter("num"));
				String isNoLike = req.getParameter("isNoLike");
				
				if(isNoLike.equals("true")) {
					dao.insertBoardLike(num, info.getUserId()); // 공감
				} else {
					dao.deleteBoardLike(num, info.getUserId()); // 공감 취소
				}
				
				boardLikeCount = dao.countBoardLike(num);

				state = "true";
			} catch (SQLException e) {
				state = "liked";
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("state", state);
			job.put("boardLikeCount", boardLikeCount);

			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print(job.toString());
		}

		// 리플 리스트 - AJAX:TEXT
		private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();
			MyUtil util = new MyUtil();

			try {
				int num = Integer.parseInt(req.getParameter("num"));
				String pageNo = req.getParameter("pageNo");
				int current_page = 1;
				if (pageNo != null)
					current_page = Integer.parseInt(pageNo);

				int rows = 5;
				int total_page = 0;
				int replyCount = 0;

				replyCount = dao.dataCountReply(num);
				total_page = util.pageCount(rows, replyCount);
				if (current_page > total_page) {
					current_page = total_page;
				}

				int start = (current_page - 1) * rows + 1;
				int end = current_page * rows;

				// 리스트에 출력할 데이터
				List<ReplyDTO> listReply = dao.listReply(num, start, end);

				// 엔터를 <br>
				for (ReplyDTO dto : listReply) {
					dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				}

				// 페이징 처리 : AJAX 용 - listPage : 자바스크립트 함수명
				String paging = util.pagingMethod(current_page, total_page, "listPage");

				req.setAttribute("listReply", listReply);
				req.setAttribute("pageNo", current_page);
				req.setAttribute("replyCount", replyCount);
				req.setAttribute("total_page", total_page);
				req.setAttribute("paging", paging);
				
				forward(req, resp, "/WEB-INF/saem/bbs/listReply.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendError(405);
			
		}

		// 리플 또는 답글 저장 - AJAX:JSON
		private void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String state = "false";
			try {
				ReplyDTO dto = new ReplyDTO();

				int num = Integer.parseInt(req.getParameter("num"));
				dto.setNum(num);
				dto.setUserId(info.getUserId());
				dto.setContent(req.getParameter("content"));
				String answer = req.getParameter("answer");
				if (answer != null) {
					dto.setAnswer(Integer.parseInt(answer));
				}
				
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

		// 리플 또는 답글 삭제 - AJAX:JSON
		private void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
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

		// 댓글 좋아요 / 싫어요 저장 - AJAX:JSON
		private void insertReplyLike(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String state = "false";
			int likeCount = 0;
			int disLikeCount = 0;

			try {
				int replyNum = Integer.parseInt(req.getParameter("replyNum"));
				int replyLike = Integer.parseInt(req.getParameter("replyLike"));

				ReplyDTO dto = new ReplyDTO();

				dto.setReplyNum(replyNum);
				dto.setUserId(info.getUserId());
				dto.setReplyLike(replyLike);

				dao.insertReplyLike(dto);

				Map<String, Integer> map = dao.countReplyLike(replyNum);

				if (map.containsKey("likeCount")) {
					likeCount = map.get("likeCount");
				}

				if (map.containsKey("disLikeCount")) {
					disLikeCount = map.get("disLikeCount");
				}

				state = "true";
			} catch (SQLException e) {
				if(e.getErrorCode() == 1) {
					state = "liked";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("state", state);
			job.put("likeCount", likeCount);
			job.put("disLikeCount", disLikeCount);

			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print(job.toString());
		}

		// 댓글 좋아요 / 싫어요 개수 - AJAX:JSON
		private void countReplyLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			int likeCount = 0;
			int disLikeCount = 0;

			try {
				int replyNum = Integer.parseInt(req.getParameter("replyNum"));
				Map<String, Integer> map = dao.countReplyLike(replyNum);

				if (map.containsKey("likeCount")) {
					likeCount = map.get("likeCount");
				}

				if (map.containsKey("disLikeCount")) {
					disLikeCount = map.get("disLikeCount");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("likeCount", likeCount);
			job.put("disLikeCount", disLikeCount);

			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print(job.toString());
		}

		// 답글 저장 - AJAX:JSON
		private void insertReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			insertReply(req, resp);
		}

		// 리플의 답글 리스트 - AJAX:TEXT
		private void listReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();

			try {
				int answer = Integer.parseInt(req.getParameter("answer"));

				List<ReplyDTO> listReplyAnswer = dao.listReplyAnswer(answer);

				// 엔터를 <br>(스타일 => style="white-space:pre;")
				for (ReplyDTO dto : listReplyAnswer) {
					dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				}

				req.setAttribute("listReplyAnswer", listReplyAnswer);

				forward(req, resp, "/WEB-INF/saem/bbs/listReplyAnswer.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendError(405);
		}

		// 리플 답글 삭제 - AJAX:JSON
		private void deleteReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			deleteReply(req, resp);
		}

		// 리플의 답글 개수 - AJAX:JSON
		private void countReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();
			int count = 0;

			try {
				int answer = Integer.parseInt(req.getParameter("answer"));
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
