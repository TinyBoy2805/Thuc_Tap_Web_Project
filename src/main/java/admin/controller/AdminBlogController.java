package admin.controller;

import admin.Enums.Role;
import admin.model.User;
import user.dao.BlogDAO;
import admin.model.Blog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.Part;
import user.exception.InsertFailedException;

@WebServlet("/admin/blog")
@jakarta.servlet.annotation.MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
public class AdminBlogController extends HttpServlet {

    private final BlogDAO blogDao = new BlogDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        // Xử lý upload file ảnh
        Part filePart = request.getPart("thumbnail");
        String fileName = "";
        String thumbnailPath = "";

        if (Objects.nonNull(filePart) && filePart.getSize() > 0) {
            fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("/uploads");
            java.io.File uploadFolder = new java.io.File(uploadDir);
            if (!uploadFolder.exists())
                uploadFolder.mkdir();
            String filePath = uploadDir + java.io.File.separator + fileName;
            filePart.write(filePath);
            thumbnailPath = "uploads/" + fileName;
        }
        String url = request.getParameter("url");
        String userIdStr = request.getParameter("user_id");
        int userId = 0;
        if (userIdStr != null && !userIdStr.isEmpty()) {
            userId = Integer.parseInt(userIdStr);
        } else {
            // Lấy user admin từ session, nếu chưa có thì mặc định là 1
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if (user.getRole() == Role.ADMIN) {
                    userId = user.getId();
                } else {
                    // Nếu không phải admin, có thể trả về lỗi hoặc gán mặc định
                    response.sendRedirect("/admin/pages/Blog.jsp?msg=not_admin");
                    return;
                }
            } else {
                // Nếu chưa đăng nhập, gán mặc định user admin id = 1
                userId = 1;
            }
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Lấy ngày đăng từ form, nếu có
        String createdAtStr = request.getParameter("created_at");
        Timestamp createdAt = now;
        if (createdAtStr != null && !createdAtStr.isEmpty()) {
            try {
                Date date = Date.valueOf(createdAtStr);
                createdAt = new Timestamp(date.getTime());
            } catch (Exception ex) {
                // Nếu lỗi format, giữ nguyên now
            }
        }

        Blog blog = Blog.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .thumbnail(thumbnailPath)
                .url(url)
                .createdAt(createdAt)
                .updatedAt(now)
                .build();

        // xứ lý khi lỗi blog
        try {
            boolean success = blogDao.insertBlog(blog);
            if(!success) {
                throw new InsertFailedException("Thêm Blog thất bại");
            }
        }catch (InsertFailedException e) {
            e.printStackTrace();
            request.setAttribute("LỖI", e.getMessage());
            request.getRequestDispatcher("/admin/blog").forward(request,response);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        List<Blog> blogs = blogDao.getAllBlogs();
        request.setAttribute("blogs", blogs);
        request.getRequestDispatcher("/admin/pages/Blog.jsp").forward(request, response);
    }
}
