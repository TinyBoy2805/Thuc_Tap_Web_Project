package admin.controller;

import admin.dao.AuthDao;
import admin.service.AuthService;
import user.dao.AddressDao;
import admin.model.User;
import user.model.Address;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CustomerDetailController", value = "/admin/manage_customer")
@MultipartConfig
public class CustomerDetailController extends HttpServlet {
    private final AuthDao authDao = new AuthDao();
    private final AddressDao addressDao = new AddressDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/admin/customer");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/admin/customer");
            return;
        }

        User customer = authDao.getUserById(id);
        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/admin/customer");
            return;
        }

        Address address = addressDao.getDefaultAddressByUserId((long) id);
        request.setAttribute("customer", customer);
        request.setAttribute("address", address);
        request.getRequestDispatcher("/admin/pages/Quanlykhachhang.jsp").forward(request, response);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        request.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            String qs = request.getQueryString();
            if (qs != null) {
                for (String part : qs.split("&")) {
                    if (part.startsWith("id=")) {
                        idParam = part.substring(3);
                        break;
                    }
                }
            }
        }
        if (idParam == null || idParam.isEmpty()) {
            throw new IllegalArgumentException("Missing id parameter");
        }
        int id = Integer.parseInt(idParam);
        String action = request.getParameter("action");


        // Xử lý đổi mật khẩu bởi admin
            if ("change_password".equals(action)) {
                String newPassword = request.getParameter("new_password");
                String confirmPassword = request.getParameter("confirm_password");
                AuthService authService = new AuthService();
                boolean success = authService.adminChangeUserPassword(id, newPassword, confirmPassword);
                HttpSession session = request.getSession();
                if (success) {
                    session.setAttribute("message", "Đổi mật khẩu thành công!");
                } else {
                    session.setAttribute("error", "Mật khẩu không hợp lệ hoặc xác nhận không khớp!");
                }
                response.sendRedirect(request.getContextPath() + "/admin/manage_customer?id=" + id);
                return;
            }


            // Xử lý cập nhật thông tin
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone_number");
            String houseNumber = request.getParameter("house_number");
            String road = request.getParameter("road");
            String district = request.getParameter("district");
            String city = request.getParameter("city");
            String hamlet = request.getParameter("hamlet");
            String ward = request.getParameter("ward");

            // Lấy avt_url hiện tại nếu không upload mới
            String avtUrl = null;
            User currentUser = authDao.getUserById(id);
            if (currentUser != null) {
                avtUrl = currentUser.getAvt_url();
            }
            // Nếu không đổi gì, giữ nguyên giá trị cũ
            if (currentUser != null) {
                if (name == null || name.isEmpty()) name = currentUser.getName();
                if (email == null || email.isEmpty()) email = currentUser.getEmail();
                if (phone == null || phone.isEmpty()) phone = currentUser.getPhone_number();
            }
            user.model.Address currentAddress = addressDao.getDefaultAddressByUserId((long) id);
            if (currentAddress != null) {
                if (houseNumber == null || houseNumber.isEmpty()) houseNumber = currentAddress.getHouseNumber();
                if (road == null || road.isEmpty()) road = currentAddress.getRoad();
                if (district == null || district.isEmpty()) district = currentAddress.getDistrict();
                if (city == null || city.isEmpty()) city = currentAddress.getCity();
                if (hamlet == null || hamlet.isEmpty()) hamlet = currentAddress.getHamlet();
                if (ward == null || ward.isEmpty()) ward = currentAddress.getWard();
            }
            Part filePart = null;
            try {
                filePart = request.getPart("avatar");
            } catch (Exception ignored) {}
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    String submitted = filePart.getSubmittedFileName();
                    String fileName = System.currentTimeMillis() + (submitted != null ? "_" + submitted : "");
                    String uploadPath = request.getServletContext().getRealPath("/libraries/ckfinder/userfiles/");
                    if (uploadPath != null) {
                        java.io.File uploadDir = new java.io.File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();
                        String filePath = uploadPath + java.io.File.separator + fileName;
                        filePart.write(filePath);
                        avtUrl = request.getContextPath() + "/libraries/ckfinder/userfiles/" + fileName;
                    } else {
                        System.out.println("[WARN] uploadPath is null, skipping avatar write");
                    }
                } catch (Exception e) {
                    System.out.println("[WARN] Failed to save uploaded avatar: " + e.getMessage());
                }
            }

            authDao.updateUserInfo(id, name, email, phone, avtUrl);
            boolean isDefault = true;
            if (currentAddress == null) {
                // Thêm mới địa chỉ mặc định
                addressDao.addAddress(id, houseNumber, road, district, city, hamlet, ward, isDefault);
            } else {
                // Cập nhật địa chỉ hiện tại
                addressDao.updateAddress(currentAddress.getId(), houseNumber, road, district, city, hamlet, ward, isDefault, id);
            }
            response.sendRedirect(request.getContextPath() + "/admin/customer");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý cập nhật khách hàng: " + ex.getMessage());
        }
    }
}