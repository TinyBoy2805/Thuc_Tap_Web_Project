package admin.controller;

import user.dao.BlogDAO;
import admin.model.Blog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.File;

@WebServlet("/admin/manage_blog")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class BlogDetailController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        if ("delete".equals(action)) {
            //
            try {
                int id = Integer.parseInt(idStr);
                BlogDAO.deleteBlogById(id);
            } catch (NumberFormatException ignored) {}
            response.sendRedirect(request.getContextPath() + "/admin/blog");
            return;
        }
        if ("update".equals(action)) {
            try {
                int id = Integer.parseInt(idStr);
                String title = request.getParameter("title");
                String url = request.getParameter("url");
                String createdAt = request.getParameter("created_at");
                String content = request.getParameter("content");
                String thumbnailOld = request.getParameter("thumbnail_old");
                String thumbnail = thumbnailOld;

                // Xử lý upload file ảnh
                Part filePart = request.getPart("image");
                if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {
                    String uploadsDir = getServletContext().getRealPath("/uploads");
                    File uploadsFolder = new File(uploadsDir);
                    if (!uploadsFolder.exists()) uploadsFolder.mkdirs();
                    String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    String filePath = uploadsDir + File.separator + fileName;
                    filePart.write(filePath);
                    thumbnail = request.getContextPath() + "/uploads/" + fileName;
                }

                java.sql.Timestamp createdAtTs = null;
                try {
                    createdAtTs = java.sql.Timestamp.valueOf(createdAt + " 00:00:00");
                } catch (Exception e) {
                    createdAtTs = new java.sql.Timestamp(System.currentTimeMillis());
                }
                java.sql.Timestamp updatedAtTs = new java.sql.Timestamp(System.currentTimeMillis());
                Blog blog = BlogDAO.getBlogById(id);
                if (blog != null) {
                    blog.setTitle(title);
                    blog.setUrl(url);
                    blog.setCreatedAt(createdAtTs);
                    blog.setContent(content);
                    blog.setThumbnail(thumbnail);
                    blog.setUpdatedAt(updatedAtTs);
                    BlogDAO.updateBlog(blog);
                }
            } catch (Exception ignored) {}
            response.sendRedirect(request.getContextPath() + "/admin/blog");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/blog");
    }
    private final BlogDAO blogDao = new BlogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        Blog blog = null;
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                blog = BlogDAO.getBlogById(id);
            } catch (NumberFormatException ignored) {}
        }
        if (blog == null) {
            response.sendRedirect(request.getContextPath() + "/admin/blog?msg=notfound");
            return;
        }
        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/admin/pages/QuanLyBlog.jsp").forward(request, response);
    }
}
