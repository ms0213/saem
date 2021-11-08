package com.bbs;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

			// 이전글 다음글
			BoardDTO preReadDto = dao.preReadBoard(dto.getNum(), condition, keyword);
			BoardDTO nextReadDto = dao.nextReadBoard(dto.getNum(), condition, keyword);

			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);

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
}
