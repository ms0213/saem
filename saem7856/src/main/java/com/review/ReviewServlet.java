package com.review;

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

import org.json.JSONObject;

import com.member.SessionInfo;

import saem.util.FileManager;
import saem.util.MyUploadServlet;
import saem.util.MyUtil;

@MultipartConfig
@WebServlet("/review/*")
public class ReviewServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	private String pathname;
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		if(info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		// 이미지 저장 경로(pathname)
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "review";
		
		// uri에 따른 작업 구분
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("deleteFile.do") != -1) {
			deleteFile(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("insertReviewLike.do") != -1){
			insertReviewLike(req, resp);
		} else if (uri.indexOf("insertReply.do") != -1){
			insertReply(req, resp);
		} else if (uri.indexOf("listReply.do") != -1){
			listReply(req, resp);
		} else if (uri.indexOf("deleteReply.do") != -1){
			deleteReply(req, resp);
		} else if (uri.indexOf("insertReplyLike.do") != -1){
			insertReplyLike(req, resp);
		} else if (uri.indexOf("countReplyLike.do") != -1){
			countReplyLike(req, resp);
		} else if (uri.indexOf("insertReplyAnswer.do") != -1){
			insertReplyAnswer(req, resp);
		} else if (uri.indexOf("listReplyAnswer.do") != -1){
			listReplyAnswer(req, resp);
		} else if (uri.indexOf("deleteReplyAnswer.do") != -1){
			deleteReplyAnswer(req, resp);
		} else if (uri.indexOf("countReplyAnswer.do") != -1){
			countReplyAnswer(req, resp);
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		ReviewDAO dao = new ReviewDAO();
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
				keyword = "";
			}

			// GET 방식인 경우 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}

			// 전체 데이터 개수
			int dataCount;
			if (keyword.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(condition, keyword);
			}
			
			// 전체 페이지 수
			int rows = 10;
			int total_page = util.pageCount(rows, dataCount);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			int gdsNum = Integer.parseInt(req.getParameter("gdsNum"));
			
			// 게시물 가져오기
			List<ReviewDTO> list = null;
			if (keyword.length() == 0) {
				list = dao.listReview(gdsNum, start, end);
			} else {
				list = dao.listReview(start, end, condition, keyword);
			}

			// 리스트 글번호 만들기
			int listNum, n = 0;
			for (ReviewDTO dto : list) {
				listNum = dataCount - (start + n - 1);
				dto.setListNum(listNum);
				n++;
			}

			String query = "";
			if (keyword.length() != 0) {
				query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}

			// 페이징 처리
			String listUrl = cp + "/review/list.do";
			String articleUrl = cp + "/review/article.do?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
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

		// JSP로 포워딩
		forward(req, resp, "/WEB-INF/saem/review/list.jsp");
	}

	private void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		if(info.getUserId().equals(null)) {
			resp.sendRedirect(cp + "/review/list.do");
			return;
		}
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/saem/review/write.jsp");
	}

	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		ReviewDAO dao = new ReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/review/list.do");
			return;
		}
		
		try {
			ReviewDTO dto = new ReviewDTO();

			// userId는 세션에 저장된 정보
			dto.setUserId(info.getUserId());

			// 파라미터
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setGdsNum(Integer.parseInt(req.getParameter("gdsNum")));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			dao.insertReview(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/review/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		ReviewDAO dao = new ReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		if(info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
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
			ReviewDTO dto = dao.readReview(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				resp.sendRedirect(cp + "/review/list.do?" + query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 이전글 다음글
			ReviewDTO preReadDto = dao.preReadBoard(dto.getNum(), condition, keyword);
			ReviewDTO nextReadDto = dao.nextReadBoard(dto.getNum(), condition, keyword);
			
			List<ReviewDTO> listFile = dao.listPhotoFile(num);
			
			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("listFile", listFile);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);

			// 포워딩
			forward(req, resp, "/WEB-INF/saem/review/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/review/list.do?" + query);
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		ReviewDAO dao = new ReviewDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();

		String page = req.getParameter("page");

		try {
			int num = Integer.parseInt(req.getParameter("num"));
			ReviewDTO dto = dao.readReview(num);

			if (dto == null) {
				resp.sendRedirect(cp + "/review/list.do?page=" + page);
				return;
			}

			// 게시물을 올린 사용자가 아니면
			if (! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/review/list.do?page=" + page);
				return;
			}

			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/saem/review/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/review/list.do?page=" + page);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		ReviewDAO dao = new ReviewDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/review/list.do");
			return;
		}

		String page = req.getParameter("page");
		try {
			ReviewDTO dto = new ReviewDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dto.setUserId(info.getUserId());

			dao.updateReview(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/review/list.do?page=" + page);
	}
	
	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		ReviewDAO dao = new ReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("memeber");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			int fileNum = Integer.parseInt(req.getParameter("fileNum"));
			
			ReviewDTO dto = dao.readReview(num);
			
			if (dto == null) {
				resp.sendRedirect(cp + "/review/list.do?page=" + page);
				return;
			}

			if (!info.getUserId().equals(dto.getUserId()) || !info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/review/list.do?page=" + page);
				return;
			}
			List<ReviewDTO> listFile = dao.listPhotoFile(num);
			
			for(ReviewDTO vo : listFile) {
				if(vo.getFileNum() == fileNum) {
					// 파일 삭제
					FileManager.doFiledelete(pathname, vo.getImageFilename());
					dao.deletePhotoFile("one", fileNum);
					listFile.remove(vo);
					break;
				}
			}
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", dto);
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/saem/review/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/review/list.do?page="+page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		ReviewDAO dao = new ReviewDAO();

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

			dao.deleteReview(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/review/list.do?" + query);
	}
	
	// AJAX - JSON
	private void insertReviewLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 공감 저장
		ReviewDAO dao = new ReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		int reviewLikeCount = 0;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String isNoLike = req.getParameter("isNoLike");
			
			if(isNoLike.equals("true")) {
				dao.insertReviewLike(num, info.getUserId()); // 좋아요 추가
			} else {
				dao.deleteReviewLike(num, info.getUserId()); // 좋아요 취소
			}
			reviewLikeCount = dao.countReviewLike(num); // 좋아요 갯수
			
			state = "true";
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) { // 좋아요 두 번 누른 경우
				state = "failLike";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject job = new JSONObject();
		job.put("state", state);
		job.put("reviewLikeCount", reviewLikeCount);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	// AJAX - JSON
	protected void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 저장
		ReviewDAO dao = new ReviewDAO();
		
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
			if(answer != null) {
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
	
	// AJAX - Text
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 리스트
		ReviewDAO dao = new ReviewDAO();
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
			
			forward(req, resp, "/WEB-INF/saem/review/listReply.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendError(405);
	}
	
	// AJAX - JSON
	protected void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 삭제
		ReviewDAO dao = new ReviewDAO();
		
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
	
	protected void insertReplyLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 좋아요/싫어요 추가
		ReviewDAO dao = new ReviewDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
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
			if(map.containsKey("likeCount")) {
				likeCount = map.get("likeCount");
			}
			if(map.containsKey("disLikeCount")) {
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
	
	protected void countReplyLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 좋아요/싫어요 갯수
		ReviewDAO dao = new ReviewDAO();
		
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
	
	// AJAX - JSON
	protected void insertReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글의 답글 저장
		insertReply(req, resp);
	}
	
	// AJAX - Text(HTML)
	protected void listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글의 답글 리스트
		ReviewDAO dao = new ReviewDAO();
		try {
			int answer = Integer.parseInt(req.getParameter("answer"));
			List<ReplyDTO> listReplyAnswer = dao.listReplyAnswer(answer);
			for(ReplyDTO dto : listReplyAnswer) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			req.setAttribute("listReplyAnswer", listReplyAnswer);
			forward(req, resp, "/WEB-INF/saem/review/listReplyAnswer.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendError(405);
	}
	
	// AJAX - JSON
	protected void deleteReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글의 답글 삭제
		deleteReply(req, resp);
	}
	
	// AJAX - JSON
	protected void countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글의 답글 갯수
		ReviewDAO dao = new ReviewDAO();
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
