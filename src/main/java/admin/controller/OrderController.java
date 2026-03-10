package admin.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import admin.model.orders.OrderCard;
import admin.model.orders.PageInformation;
import admin.service.OrderService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


@WebServlet(name = "OrderController", value = "/admin/orders/*")
public class OrderController extends HttpServlet {

    private final OrderService orderService = new OrderService();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                return context.serialize(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            })
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // lấy phần sau /admin/orders/
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            req.getRequestDispatcher("/admin/pages/order.jsp")
                    .forward(req, resp);
            return;
        }

        String action = pathInfo.substring(1); //lấy action để fetch api
        if (action.contains("page-index-")) {
            String pageNumber = action.substring(action.lastIndexOf("-") + 1);
            this.getOrderCards(req, resp, Integer.parseInt(pageNumber));
        }

        if (action.contains("search")) {
            String pageParam = req.getParameter("page");
            int page = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            this.getSearchOrder(req, resp, page);
        }

        if (action.contains("filter")) {
            String pageParam = req.getParameter("page");
            int page = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            this.getFilterOrders(req, resp, page);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/details")) {
            getOrderDetails(req, resp);
            return;
        }

        this.doGet(req, resp);
    }

    public void getOrderCards(HttpServletRequest request, HttpServletResponse response, int pageIndex) throws ServletException, IOException {
        PageInformation<OrderCard> page = this.orderService.getOrders(pageIndex);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(page));
    }

    private void getSearchOrder(HttpServletRequest req, HttpServletResponse resp, int pageIndex) throws IOException {
        PageInformation<OrderCard> searchOrderCards = orderService.searchOrder(req.getParameter("name"), pageIndex);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(searchOrderCards));
    }

    private void getFilterOrders(HttpServletRequest req, HttpServletResponse resp, int pageIndex) throws IOException {
        String jsonString = req.getReader().lines().collect(Collectors.joining());

        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.HashMap<String, Object>>(){}.getType();

        java.util.HashMap<String, Object> filterMap = gson.fromJson(jsonString, type);

        PageInformation<OrderCard> filterOrders = orderService.filterOrders(filterMap, pageIndex);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(filterOrders));
    }

    private void getOrderDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String orderId = req.getParameter("orderId");
            if (orderId == null || orderId.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId");
                return;
            }

            var orderItems = orderService.getOrderItemByID(orderId);
            var customerInfo = orderService.getCustomerInfoByOrder(orderId);
            var totalPrice = orderService.getTotalPriceByOrder(orderId);

            req.setAttribute("orderItems", orderItems);
            req.setAttribute("customer", customerInfo);
            req.setAttribute("totalPrice", totalPrice);

            req.getRequestDispatcher("/admin/pages/order_details.jsp")
                    .forward(req, resp);


        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("""
                    { "Error": %s }
                    """.formatted(e.toString()));
        }
    }
}
