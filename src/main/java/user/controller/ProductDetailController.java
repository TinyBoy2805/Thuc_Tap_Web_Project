package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.ProductReview;
import user.model.product.Product;
import user.model.product.ProductCard;
import user.model.product.ProductImage;
import admin.service.ProductService;

import java.io.IOException;
import java.util.List;
import admin.service.OrderService;
import java.util.Map;

@WebServlet(name = "ProductDetailController", value = "/product-detail/*")
public class ProductDetailController extends HttpServlet
{
    private ProductService productService = new ProductService();
    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String pathInfo = request.getPathInfo(); // Lấy phần sau /home/
        Gson gson = new Gson();

        // Handle main product page
        if (pathInfo == null || pathInfo.equals("/"))
        {
            int id = Integer.parseInt(request.getParameter("product_id"));
            List<ProductImage> images = this.productService.getImagesByProductId(id);
            Product product = this.productService.getOneProduct(id);
            product.setImages(images);
            double avgRating = this.productService.getAvgRating(this.productService.getProductReviewsByProductId(id));

            // Check if user can review
            HttpSession session = request.getSession(false);
            boolean canReview = false;
            if (session != null && session.getAttribute("userId") != null) {
                int userId = (Integer) session.getAttribute("userId");
                canReview = orderService.hasUserPurchasedProduct(userId, id);
            }

            request.setAttribute("avgRating", Double.isNaN(avgRating) ? 0.0 : avgRating);
            request.setAttribute("product", product);
            request.setAttribute("canReview", canReview);
            request.getRequestDispatcher("/user/pages/ProductDetail.jsp").forward(request, response);
            return;
        }

        String action = pathInfo.substring(1);
        int id = -1;
        if (request.getParameter("product_id") != null) {
            id = Integer.parseInt(request.getParameter("product_id"));
        }

        switch (action)
        {
            case "review" ->
            {
                if (id == -1) return;
                String pageReviewParam = request.getParameter("pageReview");
                int pageReview = (pageReviewParam == null || pageReviewParam.isEmpty()) ? 1 : Integer.parseInt(pageReviewParam);
                int pageReviewSize = 4;
                List<ProductReview> reviews = this.productService.getProductReviewsByProductIdHasPagination(id, pageReview, pageReviewSize);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String json = gson.toJson(reviews);
                response.getWriter().write(json);
            }
            case "product" ->
            {
                if (id == -1 && request.getParameter("product_id") != null) {
                    id = Integer.parseInt(request.getParameter("product_id"));
                }
                Product p = productService.getOneProduct(id);
                String pageProductParam = request.getParameter("pageProduct");
                int pageProduct = (pageProductParam == null || pageProductParam.isEmpty()) ? 1 : Integer.parseInt(pageProductParam);
                int pageProductSize = 8;
                List<ProductCard> productCards = this.productService.getProductsByCategoryHasPagination(p.getCategory(), pageProduct, pageProductSize);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String json = gson.toJson(productCards);
                response.getWriter().write(json);
            }
            case "save-review" -> {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("userId") == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                int userId = (Integer) session.getAttribute("userId");

                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                int productId = ((Number) body.get("productId")).intValue();
                int rating = ((Number) body.get("rating")).intValue();
                String comment = (String) body.get("comment");

                if (orderService.hasUserPurchasedProduct(userId, productId)) {
                    productService.saveReview(userId, productId, rating, comment);
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(Map.of("success", true)));
                } else {
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Bạn chưa mua sản phẩm này!")));
                }
            }
            // Handling for 'product' case properly requires slightly more context than I replaced, but I'll add it back similar to original logic
            // Wait, I replaced the block containing 'product' case logic. I need to be careful.
            // The original logic relied on `product` being fetched at the top.
            // I moved the fetch inside the `if (pathInfo == null)` block.
            // So `product` variable is not available in switch.
            // I should put the `product` fetching back or fetch it inside the case.
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}
