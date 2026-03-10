package admin.controller;

import user.dao.AddressDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import admin.model.User;
import user.model.UserProfile;
import user.service.ProfileService;
import user.dao.UserDAO;

import java.io.IOException;

@WebServlet(name = "AdminSettingController", urlPatterns = {"/admin/setting", "/admin/update-profile", "/admin/change-password", "/admin/upload-avatar"})
@jakarta.servlet.annotation.MultipartConfig
public class AdminSettingController extends HttpServlet {
    private ProfileService profileService;
    private UserDAO userDAO;
    private static final int USER_ID = 1;

    @Override
    public void init() throws ServletException {
        this.profileService = new ProfileService();
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin admin
        UserProfile adminProfile = profileService.getUserProfile(USER_ID);
        request.setAttribute("adminProfile", adminProfile);
        request.getRequestDispatcher("/admin/pages/CaiDat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("update-profile")) {
            handleUpdateProfile(request, response);
        } else if (uri.endsWith("upload-avatar")) {
            handleUploadAvatar(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String houseNumber = request.getParameter("houseNumber");
        String road = request.getParameter("road");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String city = request.getParameter("city");
        String hamlet = request.getParameter("hamlet");
        // Lấy avt_url hiện tại nếu không upload mới
        String avtUrl = null;
        User currentUser = userDAO.getUserById(USER_ID);
        if (currentUser != null) {
            avtUrl = currentUser.getAvt_url();
            if (name == null || name.isEmpty()) name = currentUser.getName();
            if (email == null || email.isEmpty()) email = currentUser.getEmail();
            if (phone == null || phone.isEmpty()) phone = currentUser.getPhone_number();
        }
        AddressDao addressDao = new AddressDao();
        user.model.Address currentAddress = null;
        java.util.List<user.model.Address> addresses = addressDao.getAllAddressesByUserId(USER_ID);
        if (addresses != null && !addresses.isEmpty()) {
            currentAddress = addresses.get(0);
            if (houseNumber == null || houseNumber.isEmpty()) houseNumber = currentAddress.getHouseNumber();
            if (road == null || road.isEmpty()) road = currentAddress.getRoad();
            if (district == null || district.isEmpty()) district = currentAddress.getDistrict();
            if (city == null || city.isEmpty()) city = currentAddress.getCity();
            if (ward == null || ward.isEmpty()) ward = currentAddress.getWard();
            if (hamlet == null || hamlet.isEmpty()) hamlet = currentAddress.getHamlet();
        }
        Part filePart = null;
        try {
            filePart = request.getPart("avatar");
        } catch (Exception ignored) {}
        if (filePart != null && filePart.getSize() > 0) {
            try {
                String submitted = filePart.getSubmittedFileName();
                String fileName = System.currentTimeMillis() + (submitted != null ? "_" + submitted : "");
                String uploadPath = getServletContext().getRealPath("/libraries/ckfinder/userfiles/");
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
        userDAO.updateName(USER_ID, name);
        userDAO.updatePhone(USER_ID, phone);
        userDAO.updateEmail(USER_ID, email);
        userDAO.updateAvatar(USER_ID, avtUrl);
        boolean isDefault = true;
        if (currentAddress == null) {
            addressDao.addAddress(USER_ID, houseNumber, road, district, city, hamlet, ward, isDefault);
        } else {
            addressDao.updateAddress(currentAddress.getId(), houseNumber, road, district, city, hamlet, ward, isDefault, USER_ID);
        }
        response.sendRedirect(request.getContextPath() + "/admin/setting");
    }


    private void handleUploadAvatar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý upload file 
        Part filePart = request.getPart("avatar");
        if (filePart != null && filePart.getSize() > 0) {
            String submitted = filePart.getSubmittedFileName();
            String fileName = System.currentTimeMillis() + (submitted != null ? "_" + submitted : "");
            String uploadPath = getServletContext().getRealPath("/libraries/ckfinder/userfiles/");
            if (uploadPath != null) {
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                String filePath = uploadPath + java.io.File.separator + fileName;
                filePart.write(filePath);
                userDAO.updateAvatar(USER_ID, request.getContextPath() + "/libraries/ckfinder/userfiles/" + fileName);
            } else {
                System.out.println("[WARN] uploadPath is null, skipping avatar write");
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/setting");
    }
}
