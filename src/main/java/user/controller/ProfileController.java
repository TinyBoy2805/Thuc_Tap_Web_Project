package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import admin.model.User;
import user.model.UserProfile;
import user.model.Voucher;
import user.model.order.Order;
import admin.service.OrderService;
import user.service.ProfileService;
import user.service.VoucherService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProfileController", value = "/profile/*")
public class ProfileController extends HttpServlet
{
    private ProfileService profileService;
    private VoucherService voucherService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException
    {
        this.profileService = new ProfileService();
        this.voucherService = new VoucherService();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String pathInfo = request.getPathInfo();

        HttpSession session =  request.getSession(false);
        if(session == null)
        {
            response.sendRedirect(request.getContextPath() + "/user/pages/NotFoundPage.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        if(userId == null)
        {
            response.sendRedirect(request.getContextPath() + "/user/pages/NotFoundPage.jsp");
            return;
        }




        if(pathInfo == null || pathInfo.equals("/"))
        {
            UserProfile userProfile = this.profileService.getUserProfile(userId);
            List<Voucher> userVouchers = this.voucherService.getUserVouchers(userId);
            List<Order> orders = this.orderService.getOrdersByUser(userId);

            System.out.println("USER PROFILE: " + userProfile);

            request.setAttribute("userProfile", userProfile);
            request.setAttribute("userVouchers", userVouchers);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/user/pages/Profile.jsp").forward(request, response);
            return; // đảm bảo không chạy tiếp
        }

        String action = pathInfo.substring(1);

        switch (action)
        {
            case "change-avatar"->
            {
                String avtUrl = request.getParameter("avtUrl");
                System.out.println(avtUrl);
                boolean success = this.profileService.changeAvt(userId, avtUrl);
                if(success)
                {
                    User user = (User)session.getAttribute("user");
                    user.setAvt_url(avtUrl);
                    session.setAttribute("user", user);
                }
                Gson gson = new Gson();
                String json = gson.toJson(Map.of("success", success));

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            }
            case "add-address" -> {
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                String houseNumber = (String) body.get("houseNumber");
                String road = (String) body.get("road");
                String hamlet = (String) body.get("hamlet");
                String ward = (String) body.get("ward");
                String district = (String) body.get("district");
                String city = (String) body.get("city");
                boolean isDefault = body.get("isDefault") != null && (boolean) body.get("isDefault");

                long newId = this.profileService.addAddress(userId, houseNumber, road, district, city, hamlet, ward, isDefault);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(Map.of("success", newId > 0, "id", newId)));
            }
            case "update-address" -> {
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                Object addrIdObj = body.get("addressId");
                long addressId = 0;
                if (addrIdObj instanceof Number) {
                    addressId = ((Number) addrIdObj).longValue();
                } else if (addrIdObj instanceof String && !((String) addrIdObj).isEmpty()) {
                    addressId = Long.parseLong((String) addrIdObj);
                }
                String houseNumber = (String) body.get("houseNumber");
                String road = (String) body.get("road");
                String hamlet = (String) body.get("hamlet");
                String ward = (String) body.get("ward");
                String district = (String) body.get("district");
                String city = (String) body.get("city");
                boolean isDefault = body.get("isDefault") != null && (boolean) body.get("isDefault");

                boolean success = this.profileService.updateAddress(addressId, houseNumber, road, district, city, hamlet, ward, isDefault, userId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            }
            case "delete-address" -> {
                long addressId = Long.parseLong(request.getParameter("addressId"));
                boolean success = this.profileService.deleteAddress(addressId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(Map.of("success", success)));
            }
            case "set-default-address" -> {
                long addressId = Long.parseLong(request.getParameter("addressId"));
                boolean success = this.profileService.setDefaultAddress(userId, addressId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(Map.of("success", success)));
            }
            case "update-name" -> {
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                String newName = (String) body.get("name");
                boolean success = this.profileService.updateName(userId, newName);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            }
            case "update-phone" -> {
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                String newPhone = (String) body.get("phone");
                boolean success = this.profileService.updatePhone(userId, newPhone);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            }
            case "change-password" -> {
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
                String oldPwd = (String) body.get("oldPassword");
                String newPwd = (String) body.get("newPassword");
                try {
                    boolean success = this.profileService.updatePassword(userId, oldPwd, newPwd);
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(Map.of("success", success, "message", success ? "Đổi mật khẩu thành công" : "Mật khẩu cũ không chính xác")));
                } catch (Exception e) {
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Lỗi hệ thống")));
                }
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                out.write(gson.toJson(Map.of("success", false, "message", "Bạn chưa đăng nhập hoặc session hết hạn")));
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");
            System.out.println("DEBUG: Attempting to delete account for userId = " + userId); // log để check

            boolean deleted = this.profileService.deleteAccount(userId);

            if (deleted) {
                System.out.println("DEBUG: Account deleted successfully for userId = " + userId);
                session.invalidate();
                out.write(gson.toJson(Map.of("success", true)));
            } else {
                System.out.println("DEBUG: Delete failed (returned false) for userId = " + userId);
                out.write(gson.toJson(Map.of("success", false, "message", "Không thể xóa tài khoản (có thể không tồn tại hoặc lỗi dữ liệu)")));
            }
        } catch (Exception e) {
            System.out.println("ERROR: Delete account failed for session user");
            e.printStackTrace(); // in stack trace ra console/log file server
            out.write(gson.toJson(Map.of("success", false, "message", "Có lỗi xảy ra: " + e.getMessage()))); // thêm e.getMessage() để biết lỗi gì
        }
    }


}
