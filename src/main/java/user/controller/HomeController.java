package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.product.ProductCard;
import user.service.HomeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeController", value = "/home/*")
public class HomeController extends HttpServlet {
    private int PAGE_SIZE = 16;
    private HomeService homeService = new HomeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); // Lấy phần sau /home/

        if (pathInfo == null || pathInfo.equals("/")) {
            this.setHomeData(request);
            request.getRequestDispatcher("/user/pages/Home.jsp").forward(request, response);
            return;
        }

        String action = pathInfo.substring(1);
//        System.out.println(action);

        switch (action) {
            case "trending":
                this.showTrending(request, response);
                return;
            case "product":
                this.showProductByPage(request, response);
                return;
            case "review":
                this.saveStoreReview(request, response);
                return;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        }

//        this.setHomeData(request);
//        request.getRequestDispatcher("/customer/pages/Home.jsp").forward(request, response);

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void setHomeData(HttpServletRequest request) {
        request.setAttribute("user_count_formatted", formatNumber(this.homeService.getAmountUsers()));
        request.setAttribute("avg_rating", this.homeService.getAvgRating());
        request.setAttribute("categories", this.homeService.getCategories());
        request.setAttribute("vouchers", this.homeService.getVouchers());
    }


    private void saveStoreReview(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("đã nhận");
        String review = request.getParameter("review");
        int stars = Integer.parseInt(request.getParameter("stars"));


        this.homeService.saveStoreReview(review, stars);
    }

    private void showTrending(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        //top tim kiem - top danh gia - top luot ban
        String trending_type = request.getParameter("trending_type");

        List<ProductCard> products = new ArrayList<>();
        if (trending_type != null) {
            switch (trending_type) {
                case "search":
                    products = this.homeService.getSearchTrendings();
                    break;
                case "buy_count":
                    products = this.homeService.getSellTrendings();
                    break;
                case "rating":
                    products = this.homeService.getRatingTrendings();
                    break;
                default:
                    products = new ArrayList<>();
                    break;
            }

            String json = gson.toJson(products);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    private void showProductByPage(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        String pageParam = request.getParameter("page");
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            List<ProductCard> products = homeService.getProductByPage(page, PAGE_SIZE);

            for (ProductCard pc : products) {
                pc.setAvg_rating(Math.floor(pc.getAvg_rating()));
            }

            String json = gson.toJson(products);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            // Nghìn
            double thousands = number / 1000.0;
            if (thousands == (int) thousands) {
                return String.format("%d nghìn", (int) thousands);
            } else {
                return String.format("%.1f nghìn", thousands);
            }
        } else if (number < 1000000000) {
            // Triệu
            double millions = number / 1000000.0;
            if (millions == (int) millions) {
                return String.format("%d triệu", (int) millions);
            } else {
                return String.format("%.1f triệu", millions);
            }
        } else {
            // Tỷ
            double billions = number / 1000000000.0;
            if (billions == (int) billions) {
                return String.format("%d tỷ", (int) billions);
            } else {
                return String.format("%.1f tỷ", billions);
            }
        }
    }
}