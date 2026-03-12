package admin.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import admin.model.MonthlyRevenue;
import admin.model.TopInventory;
import jakarta.ws.rs.core.Request;
import user.model.product.Product;
import admin.service.DashboardService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "DashboardController", value = "/admin/dashboard/*")
public class DashboardController extends HttpServlet {
    private final DashboardService dashboardService = new DashboardService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //lấy phần sau /dashboard/
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            request.getRequestDispatcher("/admin/pages/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        String action = pathInfo.substring(1);

        switch (action) {
            case "api" -> {
                this.setDashboardData(request, response);
            }
            case "api/revenue" -> {

            }
            case "api/top-products" -> {

            }
            case "api/monthly-revenue" -> {

            }
            case "api/top-inventory" -> {

            }
            default -> {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }


    private void setDashboardData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> topProducts = this.dashboardService.getTopProducts();
        List<MonthlyRevenue> topMonthlyRevenue = this.dashboardService.getMonthlyRevenue(2025);
        List<TopInventory> topInventory = this.dashboardService.getTopInventory();

        Long dayRevenue = this.dashboardService.getTodayRevenue();
        Long monthRevenue = this.dashboardService.getMonthRevenue();
        Long yearRevenue = this.dashboardService.getYearRevenue();


        Map<String, Object> map = new HashMap<>();
        map.put("dayRevenue", dayRevenue);
        map.put("monthRevenue", monthRevenue);
        map.put("yearRevenue", yearRevenue);
        map.put("topProducts", topProducts);
        map.put("topMonthlyRevenue", topMonthlyRevenue);
        map.put("topInventory", topInventory);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(map));
    }
}
