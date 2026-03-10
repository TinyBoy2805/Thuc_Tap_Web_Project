package user.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import admin.model.User;
import user.model.Voucher;
import user.service.VoucherService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VoucherController", value = "/voucher")
public class VoucherController extends HttpServlet
{

    private VoucherService voucherService;

    @Override
    public void init() throws ServletException
    {
        this.voucherService = new VoucherService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String pageParam = request.getParameter("page");
        int page = 1;
        int pageSize = 6;

        if (pageParam != null && !pageParam.isEmpty())
        {
            page = Integer.parseInt(pageParam);
        }

        List<Voucher> vouchers = this.voucherService.getVouchers(page, pageSize);

        String ajaxParam = request.getParameter("ajax");

        if ("true".equals(ajaxParam))
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()) // yyyy-MM-dd
                    )
                    .create();

            try
            {
                String json = gson.toJson(vouchers);
                response.getWriter().write(json);
            } catch (Exception e)
            {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Cannot get vouchers\"}");
            }
        }else
        {
            request.getRequestDispatcher("/user/pages/Voucher.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = resp.getWriter();

        try {
            // Lấy user từ session
            HttpSession session = req.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                out.write(gson.toJson(Map.of("success", false, "message", "Bạn chưa đăng nhập")));
                return;
            }

            // Đọc body JSON
            @SuppressWarnings("unchecked")
            Map<String, Object> body = gson.fromJson(req.getReader(), Map.class);
            Object vIdObj = body.get("voucherId");
            int voucherId = 0;
            if (vIdObj instanceof Number) {
                voucherId = ((Number) vIdObj).intValue();
            } else if (vIdObj instanceof String) {
                voucherId = Integer.parseInt((String) vIdObj);
            }

            boolean added = this.voucherService.addVoucherToUser(user.getId(), voucherId);

            if (added) {
                out.write(gson.toJson(Map.of("success", true)));
            } else {
                out.write(gson.toJson(Map.of("success", false, "message", "Bạn đã nhận voucher này rồi")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", "Lỗi server")));
        }
    }

}
