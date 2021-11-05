package com.announce;


import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;

import saem.util.MyUploadServlet;

@MultipartConfig
@WebServlet("/announce/*")
public class AnnounceServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		/*
		if(uri.indexOf("list.do") == -1 && info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		*/
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if(uri.indexOf("deleteList.do") != -1) {
			deleteList(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();
		saem.util.MyUtil util = new saem.util.MyUtil();
		
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

			List<AnnounceDTO> list;
			if (keyword.length() != 0) {
				list = dao.listAnnounce(start, end, condition, keyword);
			} else {
				list = dao.listAnnounce(start, end);
			}

			// 공지글
			List<AnnounceDTO> listAnnounce = null;
			listAnnounce = dao.listAnnounce();
			for (AnnounceDTO dto : listAnnounce) {
				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			// 리스트 글번호 만들기
			int listNum, n = 0;
			for (AnnounceDTO dto : list) {
				listNum = dataCount - (start + n - 1);
				dto.setListNum(listNum);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
				n++;
			}

			String query = "";
			String listUrl;
			String articleUrl;

			listUrl = cp + "/announce/list.do?rows=" + rows;
			articleUrl = cp + "/announce/article.do?page=" + current_page + "&rows=" + rows;
			if (keyword.length() != 0) {
				query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");

				listUrl += "&" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩 jsp에 전달할 데이터
			req.setAttribute("list", list);
			req.setAttribute("listAnnounce", listAnnounce);
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
		forward(req, resp, "/WEB-INF/saem/announce/list.jsp");
	}
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String rows = req.getParameter("rows");

		// admin만 글을 등록
		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do?rows=" + rows);
			return;
		}

		req.setAttribute("mode", "write");
		req.setAttribute("rows", rows);
		forward(req, resp, "/WEB-INF/saem/announce/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/notice/list.do");
			return;
		}
		
		// admin만 글을 등록
		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}

		String rows = req.getParameter("rows");
		try {
			AnnounceDTO dto = new AnnounceDTO();
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dao.insertAnnounce(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?rows=" + rows);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		String query = "page=" + page + "&rows=" + rows;

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

			// 조회수
			dao.updateHitCount(num);

			// 게시물 가져오기
			AnnounceDTO dto = dao.readAnnounce(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/announce/list.do?" + query);
				return;
			}

			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 이전글/다음글
			AnnounceDTO preReadDto = dao.preReadAnnounce(dto.getAnum(), condition, keyword);
			AnnounceDTO nextReadDto = dao.nextReadAnnounce(dto.getAnum(), condition, keyword);


			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);

			forward(req, resp, "/WEB-INF/saem/announce/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?" + query);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		String rows = req.getParameter("rows");

		try {
			int num = Integer.parseInt(req.getParameter("num"));

			AnnounceDTO dto = dao.readAnnounce(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/announce/list.do?page=" + page + "&rows=" + rows);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);

			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/saem/announce/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?page=" + page + "&rows=" + rows);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}
		
		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}

		String page = req.getParameter("page");
		String rows = req.getParameter("rows");

		try {
			AnnounceDTO dto = new AnnounceDTO();
			
			dto.setAnum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dao.updateAnnounce(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?page=" + page + "&rows=" + rows);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AnnounceDAO dao = new AnnounceDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		String query = "page=" + page + "&rows=" + rows;

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

			AnnounceDTO dto = dao.readAnnounce(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/announce/list.do?" + query);
				return;
			}

			// 게시글 삭제
			dao.deleteAnnounce(num);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?" + query);
	}

	protected void deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();

		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/announce/list.do");
			return;
		}

		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		String query = "rows=" + rows + "&page=" + page;

		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");

		try {
			if (keyword != null && keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			}

			String[] nn = req.getParameterValues("nums");
			int nums[] = null;
			nums = new int[nn.length];
			for (int i = 0; i < nn.length; i++) {
				nums[i] = Integer.parseInt(nn[i]);
			}

			AnnounceDAO dao = new AnnounceDAO();

			// 게시글 삭제
			dao.deleteAnnounceList(nums);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/announce/list.do?" + query);
	}

}
