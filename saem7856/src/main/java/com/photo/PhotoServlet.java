package com.photo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;

import saem.util.FileManager;
import saem.util.MyUploadServlet;
import saem.util.MyUtil;

@MultipartConfig
@WebServlet("/photo/*")
public class PhotoServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	private String pathname;
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		/*
		if(info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}*/
		// 이미지 저장 경로(pathname)
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "photo";
		
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
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		PhotoDAO dao = new PhotoDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 전체 데이터 개수
			int dataCount = dao.dataCount();
			
			// 전체 페이지 수
			int rows = 6;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			
			// 게시물 가져오기
			List<PhotoDTO> list = dao.listPhoto(start, end);
			
			// 페이징 처리
			String listUrl = cp + "/photo/list.do";
			String articleUrl = cp + "/photo/article.do?page="+current_page;
			String paging = util.paging(current_page, total_page, listUrl);
			// 포워딩 할 list.jsp에 넘길 값
			req.setAttribute("list", list);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("page", page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
		} catch (Exception e) {
			e.printStackTrace();
		}
		forward(req, resp, "/WEB-INF/saem/gallery/list.jsp");
	}
	
	private void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/saem/gallery/write.jsp");
	}

	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 저장
		PhotoDAO dao = new PhotoDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/photo/list.do");
			return;
		}
		try {
			PhotoDTO dto = new PhotoDTO();
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			dao.insertPhoto(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do");
	}
	
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 보기
		PhotoDAO dao = new PhotoDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			PhotoDTO dto = dao.readPhoto(num);
			if(dto == null) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			PhotoDTO preReadDto = dao.preReadPhoto(num);
			PhotoDTO nextReadDto = dao.nextReadPhoto(num);
			
			List<PhotoDTO> listFile = dao.listPhotoFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			
			forward(req, resp,"/WEB-INF/saem/gallery/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page="+page);
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		PhotoDAO dao = new PhotoDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			PhotoDTO dto = dao.readPhoto(num);
			/*
			if(dto == null) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}
			// 관리자가 아니면
			if(!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}*/
			List<PhotoDTO> listFile = dao.listPhotoFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);
			
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/saem/gallery/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/photo/list.do?page="+page);
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		PhotoDAO dao = new PhotoDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/photo/list.do");
			return;
		}
		String page = req.getParameter("page");
		
		try {
			PhotoDTO dto = new PhotoDTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			dao.updatePhoto(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page="+page);
	}
	
	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		PhotoDAO dao = new PhotoDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("memeber");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			int fileNum = Integer.parseInt(req.getParameter("fileNum"));
			
			PhotoDTO dto = dao.readPhoto(num);
			
			if(dto == null || !info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}
			List<PhotoDTO> listFile = dao.listPhotoFile(num);
			
			for(PhotoDTO vo : listFile) {
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
			
			forward(req, resp, "/WEB-INF/saem/gallery/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page="+page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제 완료
		PhotoDAO dao = new PhotoDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			PhotoDTO dto = dao.readPhoto(num);
			if(dto == null) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}
			if(!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/photo/list.do?page="+page);
				return;
			}
			
			List<PhotoDTO> listFile = dao.listPhotoFile(num);
			for(PhotoDTO vo : listFile) {
				FileManager.doFiledelete(pathname, vo.getImageFilename());
			}
			dao.deletePhotoFile("all", num);
			
			// 테이블 데이터 삭제
			dao.deletePhoto(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page="+page);
	}
}
