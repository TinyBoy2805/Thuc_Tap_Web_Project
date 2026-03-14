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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/pages/login.css?v=1.1">
</head>

<body>

<div class="page-login">
    <main class="main-content">
        <div class="welcome-section">
            <h1 class="welcome-section__title">Chào mừng đến với MichiShop</h1>
            <p class="welcome-section__text">Nơi cung cấp sữa, thực phẩm dinh dưỡng tốt nhất cho bé yêu </p>
            <div class="product-showcase">
                <img src="${pageContext.request.contextPath}/admin/imgs/michishop.png" alt="MichiShop - Dinh dưỡng cho bé"
                     class="product-showcase__banner-image">
            </div>
            <p class="welcome-section__promotion-text">Khám phá ngay các sản phẩm, ưu đãi hấp dẫn cùng MichiShop!
            </p>
        </div>

        <div class="form-section">
            <div class="tab-list">
                <button class="tab-list__button ${activeTab == 'register' ? '' : 'tab-list__button--active'}" data-form="login">Đăng nhập</button>
                <button class="tab-list__button ${activeTab == 'register' ? 'tab-list__button--active' : ''}" data-form="register">Đăng ký</button>
            </div>

            <form id="login-form" class="auth-form ${activeTab == 'register' ? 'auth-form--hidden' : 'auth-form--active'}" action="${pageContext.request.contextPath}/auth/<%= secretLoginAction() %>" method="post">
                <!-- <h2 class="auth-form__title">Đăng nhập</h2> -->
                <div class="input-group">
                    <label for="login-email" class="input-group__label">Email hoặc Số điện thoại</label>
                    <input type="text" id="login-email" name="login_account"
                           placeholder="" class="input-group__input" autocomplete="off" required>
                </div>
                <div class="input-group password-wrapper">
                    <label for="login-password" class="input-group__label">Mật khẩu</label>
                    <input type="password" id="login-password" name="login_password" placeholder=""
                           class="input-group__input" autocomplete="off" required>
                    <i class="fa-solid fa-eye-slash toggle-password" data-target="login-password"></i>
                </div>
                <div class="auth-form__row auth-form__row--between">
<%--                    <label class="small-checkbox"><input type="checkbox" name="is_admin" id="admin-checkbox"--%>
<%--                                                         class="small-checkbox__input"> Đăng--%>
<%--                        nhập với vai trò Admin</label>--%>
                    <a href="forgot__password.jsp" class="forgot-password">Quên mật khẩu?</a>
                </div>
                <c:if test="${not empty loginError}">
                    <p class="text-danger">${loginError}</p>
                </c:if>
                <button type="submit" class="auth-form__submit-btn">Đăng nhập</button>
            </form>

            <form id="register-form" class="auth-form  ${activeTab == 'register' ? 'auth-form--active' : 'auth-form--hidden'}" action="${pageContext.request.contextPath}/auth/register" method="post">
                <!-- <h2 class="auth-form__title">Đăng ký tài khoản mới</h2> -->
                <div class="input-group">
                    <label for="reg-name" class="input-group__label">Họ và tên</label>
                    <input type="text" id="reg-name" name="username" placeholder="" value="${username}"
                           class="input-group__input" autocomplete="off" required>
                </div>
                <div class="input-group">
                    <label for="reg-email" class="input-group__label">Email</label>
                    <input type="text" id="reg-email" name="email" placeholder="" value="${email}"
                           class="input-group__input" autocomplete="off" required>
                </div>
                <div class="input-group">
                    <label for="reg-phone" class="input-group__label">Số điện thoại</label>
                    <input type="text" id="reg-phone" name="phone" placeholder="" value="${phone}"
                           class="input-group__input" autocomplete="off" required>
                </div>
                <div class="input-group password-wrapper">
                    <label for="reg-password" class="input-group__label">Mật khẩu</label>
                    <input type="password" id="reg-password" name="password" placeholder=""
                           class="input-group__input" autocomplete="off" required>
                    <i class="fa-solid fa-eye-slash toggle-password" data-target="reg-password"></i>
                </div>
                <div class="input-group password-wrapper">
                    <label for="reg-password" class="input-group__label">Xác nhận mật khẩu</label>
                    <input type="password" id="reg-confirm-password" name="confirm_password"
                           placeholder="" class="input-group__input" autocomplete="off" required>
                    <i class="fa-solid fa-eye-slash toggle-password" data-target="reg-confirm-password"></i>
                </div>
                <c:if test="${errors != null && errors.hasError()}">
                    <div class="alert alert-danger">
                        <c:if test="${not empty errors.usernameError}">
                            <p class="text-danger">${errors.usernameError}</p>
                        </c:if>
                        <c:if test="${not empty errors.emailError}">
                            <p class="text-danger">${errors.emailError}</p>
                        </c:if>
                        <c:if test="${not empty errors.phoneError}">
                            <p class="text-danger">${errors.phoneError}</p>
                        </c:if>
                        <c:if test="${not empty errors.passwordError}">
                            <p class="text-danger">${errors.passwordError}</p>
                        </c:if>
                        <c:if test="${not empty errors.confirmPasswordError}">
                            <p class="text-danger">${errors.confirmPasswordError}</p>
                        </c:if>
                    </div>
                </c:if>
                <button type="submit" class="auth-form__submit-btn auth-form__submit-btn--register">Đăng ký</button>
            </form>
        </div>
    </main>

</div>
    <script src="${pageContext.request.contextPath}/admin/scripts/components/login.js"></script>
    <script src="${pageContext.request.contextPath}/admin/scripts/components/showPassword.js"></script>
</body>
</html>