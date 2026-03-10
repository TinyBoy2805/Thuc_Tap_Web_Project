package admin.controller;

import admin.dao.ContactDAO;
import admin.model.Contact;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminContactController", value = {"/admin/contact", "/admin/contact/reply", "/admin/contact/read", "/admin/contact/delete"})

public class AdminContactController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy message/error từ session, set vào request, rồi xóa khỏi session
        HttpSession session = request.getSession();
        Object msg = session.getAttribute("message");
        Object err = session.getAttribute("error");
        if (msg != null) {
            request.setAttribute("message", msg);
            session.removeAttribute("message");
        }
        if (err != null) {
            request.setAttribute("error", err);
            session.removeAttribute("error");
        }
        ContactDAO contactDao = new ContactDAO();
        List<Contact> contacts = contactDao.getAllContacts();
        request.setAttribute("emails", contacts);
        request.getRequestDispatcher("/admin/pages/Email.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        // Đánh dấu đã đọc
        if (servletPath.equals("/admin/contact/read")) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    ContactDAO contactDao = new ContactDAO();
                    contactDao.markAsRead(id);
                    response.setStatus(200);
                    response.getWriter().write("OK");
                    return;
                } catch (Exception e) {
                    response.setStatus(400);
                    response.getWriter().write("ERROR");
                    return;
                }
            }
        }
        // Xử lý xóa
        else if (servletPath.equals("/admin/contact/delete")) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    ContactDAO contactDao = new ContactDAO();
                    contactDao.deleteContact(id);
                    request.getSession().setAttribute("message", "Đã xóa liên hệ thành công!");
                } catch (Exception e) {
                    request.getSession().setAttribute("error", "Xóa liên hệ thất bại!");
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/contact");
            return;
        }
        // Xử lý phản hồi (rep)
        else if (servletPath.equals("/admin/contact/reply")) {
            String idStr = request.getParameter("emailId");
            String replyContent = request.getParameter("replyContent");
            int adminId = 1; 
            if (idStr != null && replyContent != null && !replyContent.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    ContactDAO contactDao = new ContactDAO();
                    contactDao.replyContact(id, replyContent, adminId);
                    request.getSession().setAttribute("message", "Đã gửi phản hồi thành công!");
                } catch (Exception e) {
                    request.getSession().setAttribute("error", "Gửi phản hồi thất bại!");
                }
            } else {
                request.getSession().setAttribute("error", "Vui lòng nhập nội dung phản hồi.");
            }
            response.sendRedirect(request.getContextPath() + "/admin/contact");
            return;
        }
        doGet(request, response);
    }
    
}
