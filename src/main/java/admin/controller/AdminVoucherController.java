package admin.controller;

import user.dao.VoucherDAO;
import admin.dao.CategoryDao;
import user.model.Voucher;
import admin.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AdminVoucherController", urlPatterns = {"/admin/voucher", "/admin/add_voucher", "/admin/manage_voucher"})
public class AdminVoucherController extends HttpServlet {
    private final VoucherDAO voucherDao = new VoucherDAO();
    private final CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/admin/add_voucher".equals(servletPath)) {
            List<Category> categories = categoryDao.findAll();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/pages/ThemUuDai.jsp").forward(request, response);
            return;
        }

        if ("/admin/manage_voucher".equals(servletPath)) {
            String idParam = request.getParameter("id");
            Voucher voucher = null;
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    voucher = voucherDao.findById(id);
                } catch (NumberFormatException ignored) { }
            }
            List<Category> categories = categoryDao.findAll();
            request.setAttribute("voucher", voucher);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/pages/QuanLyUuDai.jsp").forward(request, response);
            return;
        }

        List<Voucher> vouchers = voucherDao.findAll();
        request.setAttribute("vouchers", vouchers);
        request.getRequestDispatcher("/admin/pages/UuDai.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();
        if ("/admin/voucher".equals(servletPath)) {
            Voucher voucher = parseVoucher(request);
            try {
                voucherDao.insert(voucher);
                response.sendRedirect(request.getContextPath() + "/admin/voucher");
                return;
            } catch (Exception e) {
                // Kiểm tra lỗi trùng code
                String errorMsg = null;
                if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                    errorMsg = "Mã ưu đãi đã tồn tại. Vui lòng chọn mã khác.";
                } else {
                    errorMsg = "Đã xảy ra lỗi khi thêm ưu đãi. Vui lòng thử lại.";
                }
                List<Category> categories = categoryDao.findAll();
                request.setAttribute("categories", categories);
                request.setAttribute("error", errorMsg);
                request.setAttribute("voucher", voucher);
                request.getRequestDispatcher("/admin/pages/ThemUuDai.jsp").forward(request, response);
                return;
            }
        }

        if ("/admin/manage_voucher".equals(servletPath)) {
            String action = request.getParameter("action");
            if ("delete".equals(action)) {
                String idParam = request.getParameter("id");
                try {
                    int id = Integer.parseInt(idParam);
                    voucherDao.deleteById(id);
                } catch (NumberFormatException ignored) { }
                response.sendRedirect(request.getContextPath() + "/admin/voucher");
                return;
            }

            if ("update".equals(action)) {
                String idParam = request.getParameter("id");
                Integer id = null;
                try { id = Integer.parseInt(idParam); } catch (NumberFormatException ignored) { }

                Long categoryId = parseLong(request.getParameter("category_id"));
                if (id == null || categoryId == null) {
                    Voucher voucher = (id != null) ? voucherDao.findById(id) : null;
                    List<Category> categories = categoryDao.findAll();
                    request.setAttribute("voucher", voucher);
                    request.setAttribute("categories", categories);
                    request.setAttribute("error", "Thiếu dữ liệu bắt buộc. Vui lòng kiểm tra lại.");
                    request.getRequestDispatcher("/admin/pages/QuanLyUuDai.jsp").forward(request, response);
                    return;
                }

                Voucher voucher = parseVoucher(request);
                voucher.setId(id != null ? id : 0);
                voucher.setCategory_name(categoryId != null ? String.valueOf(categoryId) : null);
                String typeParam = request.getParameter("voucher_type");
                user.model.VoucherType type = null;
                try {
                    if (typeParam != null) type = user.model.VoucherType.valueOf(typeParam.trim().toUpperCase());
                } catch (IllegalArgumentException ignored) {}
                if (type == null) {
                    Voucher existing = voucherDao.findById(id);
                    if (existing != null) {
                        type = existing.getVoucher_type();
                    }
                }
                voucher.setVoucher_type(type);
                voucherDao.update(voucher);
                response.sendRedirect(request.getContextPath() + "/admin/voucher");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/voucher");
    }

    private Voucher parseVoucher(HttpServletRequest request) {
        Voucher voucher = new Voucher();
        voucher.setCategory_name(trimToNull(request.getParameter("category_id")));
        voucher.setCode(trimToNull(request.getParameter("code")));
        voucher.setDescription(trimToNull(request.getParameter("description")));
        Double discountAmount = parseDouble(request.getParameter("discount_amount"));
        if (discountAmount != null) voucher.setDiscount_amount(discountAmount);
        Double discountPercentage = parseDouble(request.getParameter("discount_percentage"));
        if (discountPercentage != null) voucher.setDiscount_percentage(discountPercentage);
        java.sql.Date sqlStartDate = parseDate(request.getParameter("start_date"));
        java.sql.Date sqlEndDate = parseDate(request.getParameter("end_date"));
        voucher.setStart_date(sqlStartDate != null ? sqlStartDate.toLocalDate() : null);
        voucher.setEnd_date(sqlEndDate != null ? sqlEndDate.toLocalDate() : null);
        Integer usageLimit = parseInt(request.getParameter("usage_limit"));
        if (usageLimit != null) voucher.setUsage_limit(usageLimit);
        voucher.setCurrent_amount(usageLimit != null ? usageLimit : 0);
        Double minOrderValue = parseDouble(request.getParameter("min_order_value"));
        if (minOrderValue != null) voucher.setMin_order_value(minOrderValue);
        String typeParam = request.getParameter("voucher_type");
        user.model.VoucherType type = null;
        try {
            if (typeParam != null) type = user.model.VoucherType.valueOf(typeParam.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {}
        voucher.setVoucher_type(type);
        return voucher;
    }

    private String trimToNull(String val) {
        if (val == null) return null;
        String trimmed = val.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Long parseLong(String val) {
        try {
            return val != null ? Long.parseLong(val.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String val) {
        try {
            return val != null ? Integer.parseInt(val.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String val) {
        try {
            return val != null ? Double.parseDouble(val.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private Date parseDate(String val) {
        try {
            return val != null && !val.trim().isEmpty() ? Date.valueOf(val.trim()) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
