package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.UserProfile;
import user.model.Voucher;
import user.model.cart.Cart;
import admin.service.OrderService;
import user.service.ProfileService;
import user.service.VoucherService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PaymentController", value = "/payment")
public class PaymentController extends HttpServlet {
    private OrderService orderService;
    private ProfileService profileService;
    private VoucherService voucherService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
        this.profileService = new ProfileService();
        this.voucherService = new VoucherService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Cart cart = (Cart) session.getAttribute("cart");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        if (cart == null || cart.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        UserProfile profile = profileService.getUserProfile(userId);
        List<Voucher> userVouchers = voucherService.getUserVouchers(userId);

        request.setAttribute("userProfile", profile);
        request.setAttribute("cart", cart);
        request.setAttribute("userVouchers", userVouchers);

        request.getRequestDispatcher("/user/pages/Payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("check-voucher".equals(action)) {
            handleCheckVoucher(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Cart cart = (Cart) session.getAttribute("cart");

        if (userId == null || cart == null || cart.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String addressIdParam = request.getParameter("addressId");
        if (addressIdParam == null || addressIdParam.isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn địa chỉ giao hàng.");
            doGet(request, response);
            return;
        }
        int addressId = Integer.parseInt(addressIdParam);
        String voucherCode = request.getParameter("voucherCode");
        double shippingFee = 35000;
        double discountAmount = 0;
        int voucherId = -1;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            Voucher voucher = voucherService.getVoucherByCode(voucherCode);
            if (voucher != null && cart.getTotalAmount() >= voucher.getMin_order_value()) {
                discountAmount = (voucher.getDiscount_percentage() / 100.0) * cart.getTotalAmount();
                voucherId = voucher.getId();
            }
        }

        try {
            orderService.placeOrder(userId, addressId, cart, shippingFee, discountAmount);

            // Mark voucher as used if one was applied
            if (voucherId != -1) {
                voucherService.markVoucherAsUsed(userId, voucherId);
            }

            // Clear cart after successful order
            session.setAttribute("cart", new Cart());
            response.sendRedirect(request.getContextPath() + "/profile?tab=purchase-info");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi đặt hàng!");
            doGet(request, response);
        }
    }

    private void handleCheckVoucher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        Voucher voucher = voucherService.getVoucherByCode(code);
        Map<String, Object> res = new HashMap<>();

        if (voucher == null) {
            res.put("success", false);
            res.put("message", "Mã giảm giá không tồn tại!");
        } else if (cart.getTotalAmount() < voucher.getMin_order_value()) {
            res.put("success", false);
            res.put("message", "Đơn hàng chưa đạt giá trị tối thiểu để áp dụng mã này!");
        } else {
            double discount = (voucher.getDiscount_percentage() / 100.0) * cart.getTotalAmount();
            res.put("success", true);
            res.put("discount", discount);
            res.put("code", voucher.getCode());
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(res));
    }
}
