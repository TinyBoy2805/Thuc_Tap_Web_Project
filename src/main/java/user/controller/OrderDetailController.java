package user.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.order.Order;
import admin.service.OrderService;

import java.io.IOException;

@WebServlet(name = "OrderDetailController", value = "/order-detail")
public class OrderDetailController extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);
        request.setAttribute("order", order);
        request.getRequestDispatcher("/user/pages/OrderDetail.jsp").forward(request, response);
    }
}
