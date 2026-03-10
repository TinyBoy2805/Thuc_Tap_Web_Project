package admin.controller;

import admin.dao.AuthDao;
import admin.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCustomerController", value = "/admin/customer")
public class AdminCustomerController extends HttpServlet {
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        List<User> customers;
        if (search != null && !search.trim().isEmpty()) {
            customers = authDao.searchCustomersByName(search.trim());
            request.setAttribute("search", search);
        } else {
            customers = authDao.getAllCustomers();
        }
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/admin/pages/KhachHang.jsp").forward(request, response);
    }
}