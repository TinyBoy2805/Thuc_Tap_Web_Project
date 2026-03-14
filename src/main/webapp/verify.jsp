<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%!
    private String secretLoginAction() {
        int key = 9;
        int[] encoded = {109, 106, 100, 100, 59, 57, 59, 63, 104, 107, 106, 108, 108, 108};
        StringBuilder builder = new StringBuilder(encoded.length);
        for (int value : encoded) {
            builder.append((char) (value ^ key));
        }
        return builder.toString();
    }
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập/Đăng ký - MiChiShop</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/pages/verify.css?v=1.1">
</head>

<body data-verifiedsuccess="${verifiedSuccess}" data-emailchanged="${emailChangeSuccess}">

<div class="page-login">
    <header class="header">
        <nav class="nav">
            <div class="nav__top">
                <a class="nav__top-logo" href="#">
                    <div class="nav__logo-img">
                        <img src="admin/imgs/logo.png" alt="">
                    </div>
                    <h1>MichiShop</h1>
                </a>
                <div class="nav__top-input">
                    <form action="#">
                        <input type="text" placeholder="Sản phẩm bạn cần tìm..." class="--no-border --no-outline">
                        <button class="--no-border --no-outline">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            Tìm kiếm
                        </button>
                    </form>
                </div>
                <div class="nav__top-actions">
                    <ul>
                        <li class="hovercart">
                            <a href="#" class="--color4"><i class="fa-solid fa-cart-shopping --size20"></i></a>
                        </li>
                        <li>
                            <a href="#" class="--color4"><i class="fa-solid fa-bell --size20"></i></a>
                        </li>
                        <li class="hover-avt --is-login">
                            <a href="#" class="--color4"><i class="fa-solid fa-user-ninja --size20"></i></a>
                        </li>
                        <li class="login-btn --is-not-login"><a href="${pageContext.request.contextPath}/auth/<%= secretLoginAction() %>">Đăng nhập</a></li>
                    </ul>
                </div>
            </div>
            <div class="nav__line"></div>
            <div class="nav__bottom">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp" class="">Trang đăng nhập</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/register">Đăng ký</a></li>
                    <li><a href="${pageContext.request.contextPath}/forgot__password.jsp">Quên mật khẩu</a></li>
                </ul>
            </div>
        </nav>
    </header>

    <main class="main-content">
        <div class="welcome-section">
            <h1>Xác minh tài khoản</h1>
            <p>Một mã OTP đã được gửi đến email: <b id="email-display-header">${email != null ? email : sessionScope.otp_email}</b></p>
            <div class="product-showcase">
                <img src="admin/imgs/michishop.png" alt="MichiShop - Dinh dưỡng cho bé"
                     class="product-showcase__banner-image">
            </div>
            <p class="welcome-section__promotion-text">Khám phá ngay các sản phẩm, ưu đãi hấp dẫn cùng MichiShop!
            </p>
        </div>

        <div class="form-section">

            <form id="verify-step-1" class="reset-form reset-form--active" action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="verify"/>
                <h2 class="reset-form__title">Nhập mã OTP</h2>
                <p class="reset-form__instruction">Nhập 6 số OTP được gửi tới email của bạn.</p>

                <div class="otp-input-group">
                    <div class="otp-input-group__inputs">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d1" autocomplete="off">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d2" autocomplete="off">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d3" autocomplete="off">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d4" autocomplete="off">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d5" autocomplete="off">
                        <input type="text" maxlength="1" class="otp-input-group__input" name="d6" autocomplete="off">
                    </div>
                </div>

                <input type="hidden" name="oldEmail" value="${sessionScope.otp_email}">
                <c:if test="${not empty error}">
                    <p class="text-danger">${error}</p>
                </c:if>
                <button type="submit" class="submit-button" data-next-step="2">Xác minh</button>
                <a href="resend-otp?email=${email}" class="reset-form__link reset-form__link--resend">Gửi lại mã OTP</a>
                <p class="change-email">
                    Chưa nhận được mã?
                    <span id="open-change-email" class="change-email__link">Đổi email</span>
                </p>
            </form>

            <form id="verify-step-2" class="reset-form reset-form--hidden" action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="change_email"/>
                <h2 class="reset-form__title">Cập nhật lại email</h2>

                <div class="input-group">
                    <label for="new-email" class="input-group__label">Email mới</label>
                    <input type="hidden" name="oldEmail" value="${sessionScope.otp_email}">
                    <input type="text" id="new-email" name="newEmail" placeholder=""
                           class="input-group__input" autocomplete="off" required>
                </div>

                <c:if test="${not empty emailError}">
                    <p class="text-danger">${emailError}</p>
                </c:if>

                <button type="submit" class="submit-button">Cập nhật</button>
                <p id="back-to-otp" class="reset-form__link" style="text-align: left">Quay lại</p>
            </form>

            <form id="verify-step-3" class="reset-form reset-form--hidden">
                <h2 class="reset-form__title">Xác minh thành công!</h2>
                <p class="reset-form__instruction reset-form__instruction--success">Tài khoản đã được kích hoạt.</p>
                <a href="index.jsp" class="submit-button">Đi đến đăng nhập</a>
            </form>
        </div>
    </main>

</div>
<footer class="site-footer">
    <div class="site-footer__top">

        <div class="site-footer__col site-footer__col--1">
            <div class="site-footer__col1-top">

                <div class="site-footer__col1-top-logo">
                    <i class="fa-solid fa-shop"></i>
                    <h3 class="--size20">MichiShop</h3>
                </div>
                <p class="site-footer__text">MiChiShop – Bé khỏe, mẹ vui, cả nhà hạnh phúc</p>

            </div>

            <div class="site-footer__col1-bottom">
                <h3 class="site-footer__heading">Về cửa hàng</h3>
                <p class="site-footer__text">MichiShop luôn muốn mang đến khách hàng những sản phẩm chất lượng và uy
                    tín</p>
            </div>
        </div>

        <div class="site-footer__col site-footer__col--2">
            <h3 class="site-footer__heading">Liên hệ</h3>
            <ul class="site-footer__list">
                <li class="site-footer__list-item">Hotline: 0901 234 567</li>
                <li class="site-footer__list-item">Địa chỉ: 123 Nguyễn Văn Cừ, TP.HCM</li>
                <li class="site-footer__list-item">Giờ mở cửa: 8h - 20h</li>
            </ul>
        </div>

        <div class="site-footer__col site-footer__col--3">
            <h3 class="site-footer__heading">Liên kết nhanh</h3>
            <ul class="site-footer__list">
                <li class="site-footer__list-item"><a href="${pageContext.request.contextPath}/index.jsp" class="site-footer__link">Trang
                    chủ</a>
                </li>
                <li class="site-footer__list-item"><a href="${pageContext.request.contextPath}/forgot__password.jsp" class="site-footer__link">Quên
                    mật khẩu</a>
                </li>
                <li class="site-footer__list-item"><a href="${pageContext.request.contextPath}/auth/<%= secretLoginAction() %>" class="site-footer__link">Đăng
                    nhập</a>
                </li>
                <li class="site-footer__list-item"><a href="${pageContext.request.contextPath}/index.jsp" class="site-footer__link">Tài
                    khoản</a>
                </li>
            </ul>
        </div>

        <div class="site-footer__col site-footer__col--4">
            <div class="site-footer__form-newsletter">
                <h3 class="site-footer__heading">Đăng kí để nhận thêm thông tin</h3>
                <input type="text" placeholder="Để lại email của bạn..." class="site-footer__input">
                <button class="site-footer__button">Đăng kí!</button>
            </div>
        </div>

    </div>
    <div class="site-footer__line"></div>

    <div class="site-footer__bottom">
        <div class="site-footer__social-logos">
            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" aria-label="Facebook"
               class="site-footer__social-link">
                <i class="fa-brands fa-facebook-f"></i>
            </a>
            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" aria-label="Instagram"
               class="site-footer__social-link">
                <i class="fa-brands fa-instagram"></i>
            </a>
            <a href="https://tiktok.com" target="_blank" rel="noopener noreferrer" aria-label="TikTok"
               class="site-footer__social-link">
                <i class="fa-brands fa-tiktok"></i>
            </a>
            <a href="https://youtube.com" target="_blank" rel="noopener noreferrer" aria-label="YouTube"
               class="site-footer__social-link">
                <i class="fa-brands fa-youtube"></i>
            </a>
        </div>
        <h4 class="site-footer__copyright">&copy; Copyright. All rights reserved.</h4>
    </div>
</footer>
</body>
<script src="./admin/scripts/components/verify.js"></script>
</html>
