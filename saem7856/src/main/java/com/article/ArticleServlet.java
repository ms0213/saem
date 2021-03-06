package com.article;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;

import com.member.SessionInfo;

import saem.util.FileManager;
import saem.util.MyUploadServlet;
import saem.util.MyUtil;

@MultipartConfig
@WebServlet("/article/*")
public class ArticleServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;

	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		// String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo) session.getAttribute("member");
		/*
		if (info == null) { // 로그인 안 된 경우
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		*/
		
		// 이미지 저장 경로(pathname)
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "articlePhoto";
		
		// uri에 따른 작업 구분
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
		} else if (uri.indexOf("insertArticleLike.do") != -1) {
			insertArticleLike(req, resp);
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 게시글 리스트
		ArticleDAO dao = new ArticleDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if (condition == null) {
				condition = "all";
				keyword="";
			}
			
			// GET 방식일때 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}
			
			// 전체 데이터 개수
			int dataCount;
			if(keyword.length()==0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.DataCount(condition, keyword);
			}
			
			// 전체 페이지 수
			int rows = 10;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			// 게시물 가져올 시작과 끝위치
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			
			// 게시물 가져오기
			List<ArticleDTO> list = null;
			if(keyword.length()==0) {
				list = dao.listArticle(start, end);
			} else {
				list = dao.listArticle(start, end, condition, keyword);
			}
			
			String query="";
			if(keyword.length()!=0) {
				query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			// 페이징 처리
			String listUrl = cp + "/article/list.do";
			String articleUrl = cp + "/article/article.do?page=" + current_page;
			if(query.length()!=0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			String paging = util.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("page",  current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward(req, resp, "/WEB-INF/saem/article/list.jsp");
	}
	
	private void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 글쓰기 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/saem/article/write.jsp");
	}
	
	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 게시물 저장
		ArticleDAO dao = new ArticleDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/article/list.do");
			return;
		}
		
		// admin만 등록
		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/article/list.do");
			return;
		}
		
		try {
			ArticleDTO dto = new ArticleDTO();
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setLink(req.getParameter("link"));
			
			String filename = null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) {
				filename = map.get("saveFilename");
				// originalFiles = map.get("originalFilenames");
			}
			
			if(filename != null) {
				dto.setimageFilename(filename);
			}
			dao.writeArticle(dto);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/article/list.do");
		
		
	}
	
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 게시물 보기
		ArticleDAO dao = new ArticleDAO();
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
			if( condition == null ) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			}
			dao.updateHitCount(num); // 조회수
			
			ArticleDTO dto = dao.readArticle(num);
			if(dto == null) {
				resp.sendRedirect(cp + "/article/list.do?page="+page);
				return;
			}
			
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			
			// 로그인 유저 좋아요 유무
			boolean isUserLike = dao.isUserArticleLike(num, info.getUserId());
			if( info != null) {
				dao.isUserArticleLike(num, info.getUserId());
			}
			// 이전글, 다음 글
			ArticleDTO preReadDto = dao.preReadArticle(dto.getNum(), condition, keyword);
			ArticleDTO nextReadDto = dao.nextReadArticle(dto.getNum(), condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			
			req.setAttribute("isUserLike", isUserLike);
			
			forward(req, resp, "/WEB-INF/saem/article/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/article/list.do?page=" + page);
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 수정 폼
		ArticleDAO dao = new ArticleDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			ArticleDTO dto = dao.readArticle(num);
			
			if (dto == null) {
				resp.sendRedirect(cp + "/article/list.do?page=" + page);
				return;
			}
			
			if(!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/article/list.do?page="+page);
			}
						
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/saem/article/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/article/list.do?page=" + page);
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 수정 완료
		ArticleDAO dao = new ArticleDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/article/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		
		try {
			ArticleDTO dto = new ArticleDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setLink(req.getParameter("link"));
			
			String imageFilename = req.getParameter("imageFilename");
			dto.setimageFilename(imageFilename);
			
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) {
				String filename = map.get("saveFilename");
				FileManager.doFiledelete(pathname, imageFilename);
				dto.setimageFilename(filename);
			}
			
			dao.updateArticle(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/article/list.do?page=" + page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// 삭제
		ArticleDAO dao = new ArticleDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			}
			
			ArticleDTO dto = dao.readArticle(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/article/list.do?page="+page);
				return;
			}
			
			if(!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/notice/list.do"+query);
				return;
			}
			
			// 이미지 파일 삭제
			FileManager.doFiledelete(pathname, dto.getimageFilename());
			
			dao.deleteArticle(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/article/list.do?page=" + page);
		
		
	}
	
	private void insertArticleLike(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		ArticleDAO dao = new ArticleDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String state = "false";
		int articleLikeCount = 0;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String isNoLike = req.getParameter("isNoLike");
			
			if(isNoLike.equals("true")) {
				dao.insertArticleLike(num, info.getUserId()); // 좋아요
			} else {
				dao.deleteArticleLike(num, info.getUserId()); // 좋아요 취소
			}
			
			articleLikeCount = dao.countArticleLike(num);
			
			state = "true";
		} catch (SQLException e) {
			state = "liked";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		job.put("articleLikeCount", articleLikeCount);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
}
