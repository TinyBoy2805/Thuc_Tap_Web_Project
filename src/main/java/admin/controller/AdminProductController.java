package admin.controller;

import admin.model.product.AdminProductCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import admin.model.orders.PageInformation;
import user.model.product.*;
import admin.service.ProductService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "AdminProductController", value = "/admin/products/*")
public class AdminProductController extends HttpServlet {
    private final ProductService productService = new ProductService();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                return context.serialize(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            })
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // lấy phần sau /admin/products/
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {

            req.getRequestDispatcher("/admin/pages/product.jsp")
                    .forward(req, resp);
            return;
        }

        if (pathInfo.equals("/add-product")) {
            req.getRequestDispatcher("/admin/pages/add__product.jsp")
                    .forward(req, resp);
            return;
        }

        if (pathInfo.equals("/edit-product")) {
            String productIDStr = req.getParameter("productID");
            if (productIDStr != null && !productIDStr.isEmpty()) {
                try {
                    int productID = Integer.parseInt(productIDStr);

                    // Gọi user.service để lấy dữ liệu sản phẩm
                    Product product = this.productService.getProductByID(productID);
                    List<ProductImage> images = this.productService.getImagesByID(productID);

                    // Đưa dữ liệu vào request attribute để JSP có thể hiển thị
                    req.setAttribute("product", product);
                    req.setAttribute("images", images);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            // Forward sang trang chi tiết
            req.getRequestDispatcher("/admin/pages/product__details.jsp").forward(req, resp);
            return;
        }

        String action = pathInfo.substring(1); //lấy action để fetch api
        if (action.contains("page-index-")) {
            String pageNumber = action.substring(action.lastIndexOf("-") + 1);
            this.getProductCards(req, resp, Integer.parseInt(pageNumber));
        }

        if (action.contains("search")) {
            String pageParam = req.getParameter("page");
            int page = 1;
            if(pageParam != null && !pageParam.isEmpty())
            {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            this.getSearchProduct(req, resp, page);
        }

        if (action.contains("filter")) {
            String pageParam = req.getParameter("page");
            int page = 1;
            if(pageParam != null && !pageParam.isEmpty())
            {
                try
                {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e)
                {
                    page = 1;
                }
            }
            this.getFilterProduct(req, resp, page);
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/add-product")) {
            this.addNewProduct(req, resp);
            return;
        }

        if (pathInfo != null && pathInfo.equals("/edit-product")) {
            this.editProduct(req, resp);
            return;
        }

        if (pathInfo != null && pathInfo.equals("/delete-product")) {
            this.deleteProduct(req, resp);
            return;
        }

        this.doGet(req, resp);
    }

    private void getProductCards(HttpServletRequest request, HttpServletResponse response, int pageIndex) throws IOException {
        PageInformation<AdminProductCard> page = this.productService.getProduct(pageIndex);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(page));
    }

    private void getSearchProduct(HttpServletRequest request, HttpServletResponse response, int pageIndex) throws IOException {
        PageInformation<AdminProductCard> searchProductCards = this.productService.searchProduct(request.getParameter("name"), pageIndex);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(searchProductCards));
    }

    private void getFilterProduct(HttpServletRequest request, HttpServletResponse response, int pageIndex) throws IOException {
        String jsonString = request.getReader().lines().collect(Collectors.joining());

        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.HashMap<String, Object>>(){}.getType();

        java.util.HashMap<String, Object> filterMap = gson.fromJson(jsonString, type);

        PageInformation<AdminProductCard> filterProducts = this.productService.filterProducts(filterMap, pageIndex);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(filterProducts));
    }

    private void addNewProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productName = req.getParameter("product-name");
        String category = req.getParameter("category");
        String brand = req.getParameter("brand");
        String startDate = req.getParameter("product-receipt-day");
        String endDate = req.getParameter("product-expire-day");
        String description = req.getParameter("product-description");
        int quantity = Integer.parseInt(req.getParameter("unit-qty[]"));
        int price = Integer.parseInt(req.getParameter("unit-price[]"));

        Product product = new Product();
        product.setName(productName);
        product.setBrand(brand);
        product.setCategory(category);
        product.setDescription(description);
        product.setStartDate(Date.valueOf(startDate));
        product.setEndDate(Date.valueOf(endDate));
        product.setPrice(price);
        product.setQuantity(quantity);


        List<ProductImage> productImage = new ArrayList<>();

        String mainImage = req.getParameter("mainImage");
        if (mainImage != null && !mainImage.isBlank()) {
            productImage.add(new ProductImage(mainImage, 1));
        }

        // ảnh phụ
        String[] secondaryImages = req.getParameterValues("secondaryImages[]");
        if (secondaryImages != null) {
            for (String url : secondaryImages) {
                if (url != null && !url.isBlank()) {
                    productImage.add(new ProductImage(url, 0));
                }
            }
        }

        int productID = this.productService.addNewProduct(product, productImage);
        System.out.println(productID);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void editProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productID = req.getParameter("productID");
        if (productID == null || productID.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing productID");
            return;
        }

        String productName = req.getParameter("product-name");
        String category = req.getParameter("category");
        String brand = req.getParameter("brand");
        String startDate = req.getParameter("product-receipt-day");
        String endDate = req.getParameter("product-expire-day");
        String description = req.getParameter("product-description");
        int quantity = Integer.parseInt(req.getParameter("unit-qty[]"));
        int price = Integer.parseInt(req.getParameter("unit-price[]"));

        Product productEdit = new Product();
        productEdit.setName(productName);
        productEdit.setBrand(brand);
        productEdit.setCategory(category);
        productEdit.setDescription(description);
        if (startDate != null && !startDate.isEmpty()) {
            productEdit.setStartDate(Date.valueOf(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            productEdit.setEndDate(Date.valueOf(endDate));
        }
        productEdit.setQuantity(quantity);
        productEdit.setPrice(price);


        List<ProductImage> productImage = new ArrayList<>();

        String mainImage = req.getParameter("mainImage");
        if (mainImage != null && !mainImage.isBlank()) {
            productImage.add(new ProductImage(mainImage, 1));
        }

        // ảnh phụ
        String[] secondaryImages = req.getParameterValues("secondaryImages[]");
        if (secondaryImages != null) {
            for (String url : secondaryImages) {
                if (url != null && !url.isBlank()) {
                    productImage.add(new ProductImage(url, 0));
                }
            }
        }

        this.productService.updateProduct(productEdit, productImage, Integer.parseInt(productID));

        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productIDStr = req.getParameter("productID");

        if (productIDStr != null && !productIDStr.isEmpty()) {
            try {
                int productID = Integer.parseInt(productIDStr);

                // Gọi user.service để xóa (Bạn cần đảm bảo Service có hàm này)
                boolean isDeleted = this.productService.deleteProduct(productID);

                if (isDeleted) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Success");
                } else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể xóa sản phẩm");
                }
            } catch (NumberFormatException | IOException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
            }
        }
    }
}
