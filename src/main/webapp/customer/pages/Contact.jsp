<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setAttribute("activeTab", "contact");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/styles/index.css">
    <script>
        window.APP_CONTEXT_PATH = `${pageContext.request.contextPath}`;
    </script>
</head>
<body>
    <div class="scroll-to-top-btn"><i class="fa-solid fa-circle-up"></i></div>

    <jsp:include page="/customer/components/Header.jsp"/>

    <main class="main">

        <section class="main__contact-top">
            <h2>MichiShop luôn sẵn sàng phục vụ bạn!</h2>
            <p>Nếu bạn có thắc mắc, cần tư vấn sản phẩm, hoặc muốn đặt hàng, vui lòng liên hệ với chúng tôi qua email, số điện thoại hoặc fanpage. Chúng tôi sẽ phản hồi bạn trong thời gian sớm nhất.</p>
        </section>
        
        <section class="main__contact-bottom">
            <div class="main__contact-bottom-header">
                <h2>Liên hệ với cửa hàng</h2>
                <p>Chúng tôi hy vọng sẽ sớm được nghe ý kiến từ bạn</p>
            </div>
            
            <div class="main__contact-container">
                <!-- Left Side - Contact Info -->
                <div class="main__contact-info">
                    <div class="contact-info-card">
                        <div class="contact-info-icon">
                            <i class="fa-solid fa-envelope"></i>
                        </div>
                        <div class="contact-info-content">
                            <h3>Email</h3>
                            <p>taisaodattenlaikhovay@gmail.com</p>
                            <span>Phản hồi trong 24h</span>
                        </div>
                    </div>

                    <div class="contact-info-card">
                        <div class="contact-info-icon">
                            <i class="fa-solid fa-phone"></i>
                        </div>
                        <div class="contact-info-content">
                            <h3>Điện thoại</h3>
                            <p>0931 415 926</p>
                            <span>Hỗ trợ 24/7</span>
                        </div>
                    </div>

                    <div class="contact-info-card">
                        <div class="contact-info-icon">
                            <i class="fa-solid fa-location-dot"></i>
                        </div>
                        <div class="contact-info-content">
                            <h3>Địa chỉ</h3>
                            <p>Khu phố 6, Phường Linh Trung, TP. Thủ Đức</p>
                            <span>TP. Hồ Chí Minh</span>
                        </div>
                    </div>

                    <div class="contact-info-card">
                        <div class="contact-info-icon">
                            <i class="fa-solid fa-clock"></i>
                        </div>
                        <div class="contact-info-content">
                            <h3>Giờ làm việc</h3>
                            <p>Thứ 2 - Chủ nhật</p>
                            <span>8:00 AM - 8:00 PM</span>
                        </div>
                    </div>

                    <div class="contact-social">
                        <h4>Theo dõi chúng tôi</h4>
                        <div class="contact-social-links">
                            <a href="https://www.facebook.com" aria-label="Facebook"><i class="fa-brands fa-facebook-f"></i></a>
                            <a href="https://www.instagram.com" aria-label="Instagram"><i class="fa-brands fa-instagram"></i></a>
                            <a href="https://www.tiktok.com" aria-label="TikTok"><i class="fa-brands fa-tiktok"></i></a>
                            <a href="https://www.youtube.com" aria-label="YouTube"><i class="fa-brands fa-youtube"></i></a>
                        </div>
                    </div>
                </div>

                <!-- Right Side - Form -->
                <form action="${pageContext.request.contextPath}/contact" method="post" class="main__contact-form">
                    <div class="form-header">
                        <h3>Gửi tin nhắn cho chúng tôi</h3>
                        <p>Điền thông tin bên dưới và chúng tôi sẽ liên hệ lại sớm nhất</p>
                    </div>

                    <%-- Thông báo gửi tin nhắn --%>
                    <% Boolean sendStatus = (Boolean) request.getAttribute("send_status"); %>
                    <% if (sendStatus != null) { %>
                        <% if (sendStatus) { %>
                            <div class="contact-alert contact-alert-success">
                                <i class="fa-solid fa-circle-check"></i>
                                <span>Gửi tin nhắn thành công! Chúng tôi sẽ phản hồi bạn sớm nhất.</span>
                            </div>
                        <% } else { %>
                            <div class="contact-alert contact-alert-error">
                                <i class="fa-solid fa-circle-xmark"></i>
                                <span>Gửi tin nhắn thất bại! Vui lòng thử lại sau.</span>
                            </div>
                        <% } %>
                    <% } %>

                    <div class="form-group">
                        <label>Họ và tên <span class="required">*</span></label>
                        <input type="text" name="username" placeholder="Nguyễn Văn A" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Email <span class="required">*</span></label>
                            <input type="email" name="email" placeholder="example@gmail.com" required>
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại <span class="required">*</span></label>
                            <input type="tel" name="phone" placeholder="0931 415 926" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Chủ đề</label>
                        <select name="subject">
                            <option value="">Chọn chủ đề</option>
                            <option value="product">Tư vấn sản phẩm</option>
                            <option value="product">Hoàn trả sản phẩm</option>
                            <option value="product">Hoàn tiền</option>
                            <option value="product">Giảm giá sản phẩm</option>
                            <option value="product">Không thể liên hệ</option>
                            <option value="product">Lỗi mua hàng</option>
                            <option value="order">Đặt hàng</option>
                            <option value="complaint">Khiếu nại</option>
                            <option value="other">Khác</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Lời nhắn <span class="required">*</span></label>
                        <textarea name="message" placeholder="Nhập nội dung tin nhắn của bạn..." required id="message-textarea"></textarea>
                    </div>

                    <button type="submit" class="form-submit">
                        <span>Gửi tin nhắn</span>
                        <i class="fa-solid fa-paper-plane"></i>
                    </button>
                </form>
            </div>
        </section>






    </main>

    <jsp:include page="/customer/components/Footer.jsp"/>

    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/main.js"></script>
    <script type="module">
        import {initCKEditor} from "${pageContext.request.contextPath}/user/scripts/utils/initCkeditor.js";
        window.addEventListener("DOMContentLoaded", () =>
        {
            initCKEditor("#message-textarea");
        });
    </script>
</body>
</html>