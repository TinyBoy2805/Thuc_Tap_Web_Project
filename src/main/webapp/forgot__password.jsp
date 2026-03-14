<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - MiChiShop</title>
    <link rel="stylesheet" href="admin/styles/pages/forgot__password.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous">
</head>

<body>
    <div class="page-login">
        <main class="main-content">
            <div class="welcome-section">
                <h1>Chào mừng đến với MichiShop</h1>
                <p>Nơi cung cấp sữa, thực phẩm dinh dưỡng tốt nhất cho bé yêu </p>
                <div class="product-showcase">
                    <img src="admin/imgs/michishop.png" alt="MichiShop - Dinh dưỡng cho bé"
                         class="product-showcase__banner-image">
                </div>
                <p class="welcome-section__promotion-text">Khám phá ngay các sản phẩm, ưu đãi hấp dẫn cùng MichiShop!</p>
            </div>

            <div class="form-section">

                <form id="reset-step-1" class="reset-form">
                    <p class="reset-form__instruction">Vui lòng nhập Email để nhận mã xác minh.</p>
                    <div class="input-group">
                        <label for="reset-email" class="input-group__label">Email</label>
                        <input type="email" id="reset-email" name="email" placeholder="" class="input-group__input" autocomplete="off" required>
                    </div>
                    <button type="submit" class="submit-button">Gửi yêu cầu</button>
                    <p id="reset-msg" class="reset-msg" style="display:none; color: green; margin: 10px auto; width: 100%; text-align: center;"></p>
                    <a href="index.jsp" class="reset-form__link">Quay lại Đăng nhập</a>
                </form>


            </div>
        </main>
    </div>

        <script>
            const resetForm = document.getElementById("reset-step-1");
            const resetMsg = document.getElementById("reset-msg");

            resetForm.addEventListener("submit", function(e)
            {
                e.preventDefault();


                const emailInput = document.getElementById("reset-email").value.trim();

                if (!emailInput)
                {
                    resetMsg.style.color = "red";
                    resetMsg.textContent = "Vui lòng nhập email.";
                    resetMsg.style.display = "block";
                    return;
                }

                resetMsg.style.color = "rgb(224,214,19)";
                resetMsg.textContent = `Đang gửi email xác minh!`;
                resetMsg.style.display = "block";
                // Giả lập gửi request lên server
                // Nếu bạn có API, có thể dùng fetch() ở đây
                // Ví dụ: fetch('/api/send-reset', { method:'POST', body: JSON.stringify({email: emailInput}) })

                fetch("${pageContext.request.contextPath}/auth/forgot-pass", {
                    method: 'POST',
                    headers: {  'Content-Type': 'application/x-www-form-urlencoded' },
                    body: new URLSearchParams({
                        email: emailInput
                    })
                })
                    .then(res => res.json())
                    .then(data =>
                    {

                        // Hiển thị thông báo thành công
                        resetMsg.style.color = "rgb(19, 224, 19)";
                        resetMsg.textContent = `Đã gửi email xác minh!`;
                        resetMsg.style.display = "block";
                    })
                    .catch(err =>
                    {
                        resetMsg.style.color = "red";
                        resetMsg.textContent = "Có lỗi khi gửi email!";
                        resetMsg.style.display = "block";
                        console.error(err);
                    });

            });

        </script>
<script src="./admin/scripts/components/forgot__password.js"></script>
<script src="./admin/scripts/components/showPassword.js"></script>
</body>

</html>