package com.contact;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.member.SessionInfo;

import saem.util.MyServlet;
import saem.util.MyUtil;

@WebServlet("/contact/*")
public class ContactServlet extends MyServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
		if(uri.indexOf("write.do")!=-1) {
			forward(req, resp, "/WEB-INF/saem/contact/write.jsp");
		} else if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("write_ok.do")!=-1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("read_ok.do")!=-1) {
			// 게시글 확인 여부
			readOk(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);			
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		ContactDAO dao = new ContactDAO();
		MyUtil util = new MyUtil();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		String cp = req.getContextPath();
		try {

			if(info == null || (! info.getUserId().equals("admin")) ) {
				resp.sendRedirect(cp+"/");
				return;
			}
			
			String page = req.getParameter("page");
			int current_page = 1;
			if(page!= null) current_page = Integer.parseInt(page);
			
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
			
			int rows = 10;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page>total_page) current_page = total_page;
			
			int start = (current_page-1)*rows+1;
			int end = current_page*rows;
			
			// 게시물 가져오기
			List<ContactDTO> list = null;
			if(keyword.length()!=0) list = dao.listContact(start, end, condition, keyword);
			else list = dao.listContact(start, end);
			
			int listNum, n =0;
			for(ContactDTO dto : list) {
				listNum = dataCount - (start+n-1);
				dto.setListNum(listNum);
				n++;
			}
			
			// 공감 유무
			//int num = Integer.parseInt(req.getParameter("num"));
			//boolean isChecked = dao.isChecked(num);
			
			String query = "";
			if(keyword.length()!=0) {
				query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			// 페이징 처리
			String listUrl = cp + "/contact/list.do";
			String articleUrl = cp+"/contact/article.do?page="+current_page;
			if(query.length()!=0) {
				listUrl += "?"+query;
				articleUrl +="&"+query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			//req.setAttribute("isChecked", isChecked);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		forward(req, resp, "/WEB-INF/saem/contact/list.jsp");
	}

	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		ContactDAO dao = new ContactDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/contact/list.do");
			return;
		}
		
		try {
			ContactDTO dto = new ContactDTO();
			dto.setFirstName(req.getParameter("firstName"));
			dto.setLastName(req.getParameter("lastName"));
			dto.setLeague(req.getParameter("league"));
			dto.setMember(req.getParameter("member"));
			dto.setComment(req.getParameter("comments"));
			dto.setEmail(req.getParameter("email"));
			dto.setFullname(req.getParameter("firstName"), req.getParameter("lastName"));
			dao.insertContact(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/contact/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 상세보기
		ContactDAO dao = new ContactDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		try {
			if(info == null || (! info.getUserId().equals("admin")) ) {
				resp.sendRedirect(cp+"/");
				return;
			}
			
			int num = Integer.parseInt(req.getParameter("num"));
			
			// 게시물가져오기
			ContactDTO dto = dao.readContact(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/contact/list.do?"+query);
				return;
			}
			dto.setComment(util.htmlSymbols(dto.getComment()));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/saem/contact/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/contact/list.do?"+query);
	}

	// AJAX:JSON
	private void readOk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContactDAO dao = new ContactDAO();
		
		String state = "false";
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String isCheck = req.getParameter("isCheck");
			
			if(isCheck.equals("true")) dao.insertContactCheck(num);
			else dao.deleteContactCheck(num); // 공감취소
			
			state="true";
			
		} catch (SQLException e) {
			state = "liked";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContactDAO dao = new ContactDAO();
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			ContactDTO dto = dao.readContact(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/contact/list.do?"+query);
				return;
			}
			
			dao.deleteContact(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/contact/list.do?"+query);
	}

}
