package com.image;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;

@MultipartConfig
@WebServlet("/image/upload.do")
public class ImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		String cp = req.getContextPath();

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "image";
		File f = new File(pathname);
		if (! f.exists()) {
			f.mkdirs();
		}

		JSONObject job = new JSONObject();
		try {
			Part p = req.getPart("upload"); // upload : ckeditor5 static const
			String originalFilename = getOriginalFilename(p);
			if (originalFilename == null || originalFilename.length() == 0) {
				job.put("error", "{\"message\": \"could not upload this image\"}");
				job.put("uploaded", false);
			} else {
				String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
				String saveFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
				saveFilename += System.nanoTime();
				saveFilename += fileExt;

				String fullpath = pathname + File.separator + saveFilename;
				p.write(fullpath);

				String url = cp + "/uploads/image/" + saveFilename;

				job.put("url", url);
				job.put("uploaded", true);
			}

		} catch (Exception e) {
			job.put("error", "{\"message\": \"could not upload this image\"}");
			job.put("uploaded", false);

			e.printStackTrace();
		}

		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

	private String getOriginalFilename(Part p) {
		try {
			for (String s : p.getHeader("content-disposition").split(";")) {
				if (s.trim().startsWith("filename")) {
					return s.substring(s.indexOf("=") + 1).trim().replace("\"", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
