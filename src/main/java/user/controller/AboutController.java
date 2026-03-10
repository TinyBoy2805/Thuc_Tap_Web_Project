package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.StoreReview;
import user.service.AboutService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AboutController", value = "/about")
public class AboutController extends HttpServlet {
    private AboutService aboutService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.aboutService = new AboutService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        // Nếu là AJAX request để load thêm reviews
        if ("loadMore".equals(action)) {
            loadMoreReviews(request, response);
            return;
        }

        // Trang thông thường - Load 6 reviews đầu tiên
        List<StoreReview> reviews = this.aboutService.getTopReviews();
        int totalReviews = this.aboutService.getTotalReviewsCount();

        request.setAttribute("reviews", reviews);
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("hasMore", totalReviews > 6);

        request.getRequestDispatcher("/user/pages/About.jsp").forward(request, response);
    }

    // Xử lý AJAX request load more reviews
    private void loadMoreReviews(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int page = Integer.parseInt(request.getParameter("page"));
            int pageSize = 6;

            List<StoreReview> reviews = this.aboutService.getReviews(page, pageSize);
            int totalReviews = this.aboutService.getTotalReviewsCount();
            int loadedCount = (page + 1) * pageSize;

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("reviews", reviews);
            result.put("hasMore", loadedCount < totalReviews);
            result.put("nextPage", page + 1);

            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Có lỗi xảy ra: " + e.getMessage());
            response.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
