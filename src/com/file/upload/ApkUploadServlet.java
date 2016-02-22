package com.file.upload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ApkUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -1634812577572667155L;

	/**
	 * Constructor of the object.
	 */
	public ApkUploadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = this.getServletContext().getRealPath("");
	}
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		if (request.getSession().getAttribute(SystemConstants.USER_SESSION_KEY) == null) {
//			response.getWriter().write(
//					"<script language='javascript'>top.location.href='"
//							+ request.getContextPath() + "/';</script>");
//			return;
//		}
		
		String remotePath = File.separator + "a" + File.separator;
		String savePath = "" + remotePath;
		File dfile = new File(savePath);
		if (!dfile.exists()) {
			dfile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List<FileItem> fileList = null;
		
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			return;
		}
		Iterator<FileItem> it = fileList.iterator();
		String name = "";
		String extName = "";
		String requestPath = "";
		long apkSize = 0;
		while (it.hasNext()) {
			FileItem item = it.next();
			if (item.isFormField()) {
				System.out.println(item.getString());
			}
			if (!item.isFormField()) {
				name = item.getName();
				long size = item.getSize();
				String type = item.getContentType();
				System.out.println(size + " " + type);
				if (name == null || name.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (name.lastIndexOf(".") >= 0) {
					extName = name.substring(name.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					name = UUID.randomUUID().toString();
					file = new File(savePath + name + extName);
				} while (file.exists());
				try {
					item.write(file);
					requestPath = name + extName;
					requestPath += ":" + item.getSize();//格式为：路径:大小
				} catch (Exception e) {
					e.printStackTrace();
					requestPath = "";
				}
			}
		}
		response.getWriter().write(requestPath);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {

	}
}
