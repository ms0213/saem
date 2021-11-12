package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.article.ArticleDAO;
import com.article.ArticleDTO;

import saem.util.MyServlet;

@WebServlet("/main.do")
public class MainServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
		if(uri.indexOf("main.do") != -1) {
			list(req, resp);
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ArticleDAO dao = new ArticleDAO();		
		
		try {
			List<ArticleDTO> list = null;
			list = dao.listArticle(1, 4);
			
			req.setAttribute("list", list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward(req, resp, "/WEB-INF/saem/main/main.jsp");
	}

}
