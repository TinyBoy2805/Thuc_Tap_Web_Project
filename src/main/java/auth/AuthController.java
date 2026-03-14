package auth;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import admin.model.ValidateObject;
import admin.model.User;
import admin.service.AuthService;
import admin.service.MailService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "AuthController", value = "/auth/*")
public class AuthController extends HttpServlet {
    private final String pepper = "MICHI SHOP";
    private static final int SECRET_ROUTE_KEY = 9;
    private static final int[] SECRET_LOGIN_ACTION_ENCODED = {109, 106, 100, 100, 59, 57, 59, 63, 104, 107, 106, 108, 108, 108};

    private AuthService authService;
    private MailService mailService;


    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
        this.mailService = new MailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        String action = pathInfo.substring(1);

        if (this.isSecretLoginAction(action)) {
            try {
                this.handleLogin(request, response);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return;
        }


        switch (action) {
            case "logout" -> {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
            case "register" -> {
                try {
                    this.handleRegister(request, response);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
            case "forgot-pass" -> {
                this.handleForgotPassword(request, response);
            }
            case "verify" -> {
                this.handleVerifyAccount(request, response);
            }
            case "change-pass" -> {
                this.verifyForgotPassword(request, response);
            }
            case "reset-pass" -> {
                try {
                    this.handleResetPass(request, response);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException {
        String method = request.getMethod();

        switch (method) {
            case "GET" -> {
                this.renderInfiniteLoadingPage(request, response);
            }
            case "POST" -> {
                String input = request.getParameter("login_account");
                String password = request.getParameter("login_password");

                // 1. Tìm user theo email hoặc phone
                User user = this.authService.findByEmailOrPhone(input);

                if (user == null) {
                    request.setAttribute("loginError", "Tài khoản không tồn tại");
                    request.setAttribute("activeTab", "login");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }

                // 2. Check user đã verified chưa
                if (!user.checkVerified()) {
                    request.setAttribute("loginError", "Tài khoản chưa được xác thực. Vui lòng check email.");
                    request.setAttribute("activeTab", "login");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }

                // 3. Verify password với salt từ DB
                String storedHash = user.getPassword_hashed();
                String salt = user.getSalt();

                // Hash password user nhập vào với salt từ DB
                String inputHash = this.authService.hashPasswordUsingMD5(password, salt, pepper);
                System.out.println("inputHash: " + inputHash);
                // 4. So sánh hash
                if (!inputHash.equals(storedHash)) {
                    request.setAttribute("loginError", "Mật khẩu không đúng");
                    request.setAttribute("activeTab", "login");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }

                // 5. Login thành công - Tạo session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("role", user.getRole());
                session.setAttribute("isLoggedIn", true);

                System.out.println(user.getRole());
                // 6. Redirect
                if ("user".equalsIgnoreCase(user.getRole().toString())) {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                }

            }
        }

    }

    private boolean isSecretLoginAction(String action) {
        return this.resolveSecretLoginAction().equals(action);
    }

    private String resolveSecretLoginAction() {
        StringBuilder builder = new StringBuilder(SECRET_LOGIN_ACTION_ENCODED.length);
        for (int value : SECRET_LOGIN_ACTION_ENCODED) {
            builder.append((char) (value ^ SECRET_ROUTE_KEY));
        }
        return builder.toString();
    }

    private void renderInfiniteLoadingPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String contextPath = request.getContextPath();

        writer.printf("""
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Loading...</title>
                    <style>
                        :root {
                            --bg-start: #0f172a;
                            --bg-end: #1e293b;
                            --accent: #22d3ee;
                            --text: #e2e8f0;
                            --muted: #94a3b8;
                        }

                        * { box-sizing: border-box; }

                        body {
                            margin: 0;
                            min-height: 100vh;
                            display: grid;
                            place-items: center;
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            background: radial-gradient(circle at top left, #1d4ed8 0%%, var(--bg-start) 35%%, var(--bg-end) 100%%);
                            color: var(--text);
                            overflow: hidden;
                        }

                        .panel {
                            width: min(92vw, 520px);
                            padding: 32px 24px;
                            border-radius: 16px;
                            background: rgba(15, 23, 42, 0.7);
                            border: 1px solid rgba(148, 163, 184, 0.25);
                            backdrop-filter: blur(8px);
                            text-align: center;
                        }

                        .spinner {
                            width: 96px;
                            height: 96px;
                            margin: 0 auto 18px;
                            border: 8px solid rgba(148, 163, 184, 0.25);
                            border-top-color: var(--accent);
                            border-radius: 50%%;
                            animation: spin 0.9s linear infinite;
                        }

                        .spinner.stopped {
                            animation-play-state: paused;
                            border-top-color: #22c55e;
                        }

                        h1 {
                            margin: 0 0 8px;
                            font-size: clamp(1.4rem, 3.2vw, 1.9rem);
                        }

                        p {
                            margin: 6px 0;
                            color: var(--muted);
                            line-height: 1.5;
                        }

                        .done {
                            color: #86efac;
                            font-weight: 600;
                            display: none;
                        }

                        @keyframes spin {
                            from { transform: rotate(0deg); }
                            to { transform: rotate(360deg); }
                        }
                    </style>
                </head>
                <body>
                <section class="panel">
                    <div id="spinner" class="spinner" aria-label="Loading"></div>
                    <h1>Loading...</h1>
                    <p id="hint">Nhấn giữ tổ hợp phím Ctrl + A + B + C để dừng vòng quay.</p>
                    <p id="done" class="done">Đã dừng loading thành công.</p>
                    <p><a href="%s/index.jsp" style="color:#67e8f9;">Quay về trang chủ</a></p>
                </section>

                <script>
                    (function () {
                        const pressed = new Set();
                        const spinner = document.getElementById('spinner');
                        const hint = document.getElementById('hint');
                        const done = document.getElementById('done');

                        function normalizeKey(key) {
                            return (key || '').toLowerCase();
                        }

                        function checkCombo() {
                            const hasCtrl = pressed.has('control') || pressed.has('ctrl');
                            if (hasCtrl && pressed.has('a') && pressed.has('b') && pressed.has('c')) {
                                spinner.classList.add('stopped');
                                hint.style.display = 'none';
                                done.style.display = 'block';
                            }
                        }

                        window.addEventListener('keydown', function (event) {
                            pressed.add(normalizeKey(event.key));
                            checkCombo();
                        });

                        window.addEventListener('keyup', function (event) {
                            pressed.delete(normalizeKey(event.key));
                        });

                        window.addEventListener('blur', function () {
                            pressed.clear();
                        });
                    })();
                </script>
                </body>
                </html>
                """, contextPath);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException {
        String method = request.getMethod();

        switch (method) {
            case "GET" -> {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
            case "POST" -> {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String confirm_password = request.getParameter("confirm_password");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

//                System.out.println("========== DEBUG REGISTER PARAMS ==========");
//                System.out.println("Username: " + username);
//                System.out.println("Password: " + password);
//                System.out.println("Confirm Password: " + confirm_password);
//                System.out.println("Email: " + email);
//                System.out.println("Phone: " + phone);
//                System.out.println("===========================================");


                ValidateObject errors = this.authService.validateRegistration(username, password, confirm_password, email, phone);

                if (errors.hasError()) {
                    request.setAttribute("errors", errors);
                    request.setAttribute("activeTab", "register");

                    // Giữ lại data đã nhập
                    request.setAttribute("username", username);
                    request.setAttribute("email", email);
                    request.setAttribute("phone", phone);

                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }


                //hash password
                String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                Random ran = new Random();
                int length = 16; // Độ dài salt
                StringBuilder salt = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    salt.append(CHARACTERS.charAt(ran.nextInt(CHARACTERS.length())));
                }
                String hashed_password = this.authService.hashPasswordUsingMD5(password, salt.toString(), pepper);

                //insert
                int userId = this.authService.createNewUser(username, hashed_password, email, phone, salt.toString());


                //verify email
                String emptyTokenLink = this.mailService.createVerifyLink(request);
                String token = this.mailService.createVerifyToken();

                String verifyLink = emptyTokenLink + token;
                int minutes = 10;
                Timestamp expirationTime = this.mailService.createExpirationTime(minutes);

                this.authService.createTokenAndExpiredTime(userId, token, expirationTime, "VERIFY_EMAIL"); //luu vao db

                this.mailService.sendVerifyLink(email, username, verifyLink, minutes);


            }
        }
    }


    private void handleVerifyAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = request.getParameter("token");

        boolean valid = this.authService.checkToken(token, "VERIFY_EMAIL");
        System.out.println("valid: " + valid);
        if (!valid) response.sendRedirect(request.getContextPath() + "/index.jsp");


        int userId = this.authService.getUserIdFromVerifyToken(token);
        System.out.println("id: " + userId);
        if (userId == -1) response.sendRedirect(request.getContextPath() + "/index.jsp");

        //set verify for user
        boolean success = this.authService.setVerifyUser(userId);
        System.out.println("Success: " + success);
        if (!success) response.sendRedirect(request.getContextPath() + "/index.jsp");


        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String contextPath = request.getContextPath();
        writer.println("""
                    <!DOCTYPE html>
                           <html lang="vi">
                           <head>
                               <meta charset="UTF-8">
                               <meta name="viewport" content="width=device-width, initial-scale=1.0">
                               <title>Xác thực thành công - MiChiShop</title>
                               <style>
                                   @keyframes slideUp{from{opacity:0;transform:translateY(20px)}to{opacity:1;transform:translateY(0)}}
                                   @keyframes scaleIn{from{transform:scale(0)}to{transform:scale(1)}}
                                   @keyframes checkmark{0%%{stroke-dashoffset:50}100%%{stroke-dashoffset:0}}
                                   .container{animation:slideUp .5s ease}
                                   .icon{animation:scaleIn .4s cubic-bezier(.68,-.55,.265,1.55) .2s both}
                                   .checkmark{stroke-dasharray:50;stroke-dashoffset:50;animation:checkmark .4s ease .5s forwards}
                               </style>
                               <script>
                                   let t=5;setInterval(()=>{if(--t>0)document.querySelector('.time').textContent=t;else window.location.href='%s/index.jsp'},1000);
                               </script>
                           </head>
                           <body style="margin:0;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;background:#F564A9;min-height:100vh;display:flex;align-items:center;justify-content:center;padding:20px;">
                               <div class="container" style="background:#fff;border-radius:24px;max-width:420px;width:100%%;padding:48px 32px;text-align:center;box-shadow:0 20px 60px rgba(0,0,0,.15);">
                                   <div class="icon" style="width:72px;height:72px;background:linear-gradient(135deg,#4CAF50,#66BB6A);border-radius:50%%;display:flex;align-items:center;justify-content:center;margin:0 auto 32px;box-shadow:0 8px 24px rgba(76,175,80,.3);">
                                       <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#fff" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline class="checkmark" points="20 6 9 17 4 12"></polyline></svg>
                                   </div>
                                   <h1 style="color:#F564A9;font-size:24px;font-weight:600;margin:0 0 12px;letter-spacing:-.5px;">Xác thực thành công</h1>
                                   <p style="color:#64748b;font-size:15px;line-height:1.6;margin:0 0 32px;">Tài khoản đã được kích hoạt. Đăng nhập để bắt đầu mua sắm tại MiChiShop.</p>
                                   <a href="%s/index.jsp" style="display:inline-flex;align-items:center;gap:8px;background:#F564A9;color:#fff;text-decoration:none;padding:14px 32px;border-radius:12px;font-size:15px;font-weight:500;transition:all .2s;box-shadow:0 4px 12px rgba(245,100,169,.3);">
                                       Đăng nhập ngay
                                       <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>
                                   </a>
                                   <p style="margin:24px 0 0;font-size:13px;color:#94a3b8;">Tự động chuyển sau <span class="time" style="color:#F564A9;font-weight:600;">5</span>s</p>
                               </div>
                           </body>
                           </html>
                """.formatted(contextPath, contextPath));


    }


    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");

        System.out.println("email: " + email);

        User user = this.authService.findByEmailOrPhone(email);

        String emptyTokenLink = this.mailService.createVerifyForgotPassLink(request);
        String token = this.mailService.createVerifyToken();

        String verifyLink = emptyTokenLink + token;
        int minutes = 10;
        Timestamp expirationTime = this.mailService.createExpirationTime(minutes);

        this.authService.createTokenAndExpiredTime(user.getId(), token, expirationTime, "FORGOT_PASS"); //luu vao db

        boolean success = this.mailService.sendVerifyPasswordResetLink(email, user.getName(), verifyLink, minutes);


        Map<String, Object> res = new HashMap<>();
        res.put("success", success); // true nếu gửi email ok
        String json = new Gson().toJson(res);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    private void verifyForgotPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter("token");

        boolean valid = this.authService.checkToken(token, "FORGOT_PASS");
        if (!valid) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int userId = this.authService.getUserIdFromVerifyToken(token);
        if (userId == -1) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String contextPath = request.getContextPath();

        writer.println("""
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Đặt mật khẩu mới - MiChiShop</title>
                        <style>
                            body { margin:0; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;
                                   background:#F564A9; min-height:100vh; display:flex; align-items:center; justify-content:center; padding:20px; }
                            .container { background:#fff; border-radius:24px; max-width:420px; width:100%%; padding:48px 32px; text-align:center;
                                         box-shadow:0 20px 60px rgba(0,0,0,.15);}
                            input { width:100%%; padding:12px 16px; margin:8px 0; border:1px solid #ccc; border-radius:8px; font-size:14px; }
                            button { background:#F564A9; color:#fff; border:none; padding:14px 32px; border-radius:12px; font-size:15px;
                                     font-weight:500; cursor:pointer; margin-top:16px; }
                            .msg { margin-top:16px; font-size:14px; color:red; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1 style="color:#F564A9;">Đặt mật khẩu mới</h1>
                            <p>Vui lòng nhập mật khẩu mới cho tài khoản của bạn.</p>
                            <form id="reset-pass-form">
                                <input type="password" id="new-pass" placeholder="Mật khẩu mới" required>
                                <input type="password" id="confirm-pass" placeholder="Xác nhận mật khẩu" required>
                                <input type="hidden" id="token" value="%s">
                                <button type="submit">Đổi mật khẩu</button>
                                <p class="msg" id="msg"></p>
                            </form>
                        </div>
                        <script>
                            const form = document.getElementById("reset-pass-form");
                            const msg = document.getElementById("msg");
                
                            form.addEventListener("submit", async (e) => {
                                e.preventDefault();
                                const pass = document.getElementById("new-pass").value.trim();
                                const confirm = document.getElementById("confirm-pass").value.trim();
                                const token = document.getElementById("token").value;
                
                                if(pass.length < 6) {
                                    msg.textContent = "Mật khẩu tối thiểu 6 ký tự";
                                    return;
                                }
                                if(pass !== confirm) {
                                    msg.textContent = "Mật khẩu xác nhận không khớp";
                                    return;
                                }
                
                                try {
                                    const res = await fetch("%s/auth/reset-pass", {
                                        method: "POST",
                                        headers: { "Content-Type": "application/json" },
                                        body: JSON.stringify({ token, password: pass })
                                    });
                                    const data = await res.json();
                                    if(data.success){
                                        msg.style.color = "green";
                                        msg.textContent = "Đổi mật khẩu thành công! Chuyển hướng đến đăng nhập...";
                                        setTimeout(()=>{ window.location.href="%s/index.jsp"; }, 3000);
                                    } else {
                                        msg.style.color = "red";
                                        msg.textContent = data.message || "Có lỗi xảy ra!";
                                    }
                                } catch(err) {
                                    console.error(err);
                                    msg.style.color = "red";
                                    msg.textContent = "Có lỗi xảy ra!";
                                }
                            });
                        </script>
                    </body>
                    </html>
                """.formatted(token, contextPath, contextPath));
    }

    private void handleResetPass(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // Đọc JSON từ request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            Map<String, String> body = gson.fromJson(sb.toString(), Map.class);
            String password = body.get("password");
            String token = body.get("token");

            if (password == null || token == null) {
                out.write(gson.toJson(Map.of("success", false, "message", "Dữ liệu không hợp lệ")));
                return;
            }

            // Lấy userId từ token
            int userId = this.authService.getUserIdFromVerifyToken(token);
            if (userId == -1) {
                out.write(gson.toJson(Map.of("success", false, "message", "Token không hợp lệ")));
                return;
            }

            // Tạo salt và hash password
            String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random ran = new Random();
            StringBuilder salt = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                salt.append(CHARACTERS.charAt(ran.nextInt(CHARACTERS.length())));
            }
            String hashed_password = this.authService.hashPasswordUsingMD5(password, salt.toString(), pepper);

            boolean success = this.authService.setNewPassword(userId, hashed_password, salt);

            if (success) {
                out.write(gson.toJson(Map.of("success", true)));
            } else {
                out.write(gson.toJson(Map.of("success", false, "message", "Không thể cập nhật mật khẩu")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", "Có lỗi xảy ra!")));
        }
    }


}