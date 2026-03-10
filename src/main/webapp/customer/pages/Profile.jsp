<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setAttribute("activeTab", "");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MichiShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/styles/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
        integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script>
        window.APP_CONTEXT_PATH = `${pageContext.request.contextPath}`;
    </script>
</head>
<body>

    <div class="toast-container"></div>
    <div class="scroll-to-top-btn"><i class="fa-solid fa-circle-up"></i></div>
    <jsp:include page="/customer/components/Header.jsp"/>

    <main class="main">

        <div class="main__profile">

            <aside class="sidebar">
                <nav>
                    <ul>
                        <li class="profile-tab active" data-tab="personal-info">
                            <i class="fa-regular fa-user"></i>
                            <span>Thông tin cá nhân</span>
                        </li>
                        <li class="profile-tab" data-tab="purchase-info">
                            <i class="fa-solid fa-receipt"></i>
                            <span>Đơn hàng của tôi</span>
                        </li>
                        <li class="profile-tab" data-tab="address-info">
                            <i class="fa-solid fa-location-dot"></i>
                            <span>Địa chỉ của tôi</span>
                        </li>
                        <li class="profile-tab" data-tab="voucher-info">
                            <i class="fa-solid fa-ticket"></i>
                            <span>Ưu đãi của tôi</span>
                        </li>
                        <li class="profile-tab" data-tab="security-info">
                            <i class="fa-solid fa-shield-halved"></i>
                            <span>Bảo mật</span>
                        </li>
                    </ul>
                </nav>
            </aside>

            <section class="content">
                <!-- Personal Info Tab -->
                <article id="personal-info" class="profile-content active">
                    <div class="content-header">
                        <h2>Thông tin cá nhân</h2>
                        <p class="subtitle">Quản lý thông tin của bạn để bảo mật tài khoản</p>
                    </div>

                    <div class="profile-main-card">
                        <div class="avatar-wrapper">
                            <div class="avatar-container">
                                <img src="${userProfile.avt_url}"
                                     alt="Avatar" class="avatar-image" id="avatar-img">
                                <div class="status-badge">
                                    <i class="fa-solid fa-circle"></i>
                                </div>
                            </div>
                            <button class="change-avatar-btn">
                                <i class="fa-solid fa-camera"></i>
                                <span>Đổi ảnh</span>
                            </button>
                        </div>

                        <div class="profile-details">
                            <div class="detail-row">
                                <label>Họ và tên</label>
                                <div class="detail-value">
                                    <span id="user-name">${userProfile.name}</span>
                                    <button class="edit-btn" id="edit-name-btn"><i class="fa-solid fa-pen"></i></button>
                                </div>
                            </div>

                            <div class="detail-row">
                                <label>Email</label>
                                <div class="detail-value">
                                    <span id="user-email">${userProfile.email}</span>
                                    <span class="verified-badge"><i class="fa-solid fa-circle-check"></i> Đã xác minh</span>
                                </div>
                            </div>

                            <div class="detail-row">
                                <label>Số điện thoại</label>
                                <div class="detail-value">
                                    <span id="user-phone">${userProfile.phone_number}</span>
                                    <button class="edit-btn" id="edit-phone-btn"><i class="fa-solid fa-pen"></i></button>
                                </div>
                            </div>

                        </div>
                    </div>

                </article>

                <!-- Purchase Info Tab -->
                <article id="purchase-info" class="profile-content">
                    <div class="content-header">
                        <h2>Đơn hàng của tôi</h2>
                        <p class="subtitle">Theo dõi và quản lý đơn hàng của bạn</p>
                    </div>

                    <div class="order-tabs">
                        <button class="order-tab-btn active" data-status="all">
                            Tất cả
                        </button>
                        <button class="order-tab-btn" data-status="pending">
                            <i class="fa-solid fa-clock"></i>
                            Chờ xác nhận
                        </button>
                        <button class="order-tab-btn" data-status="shipping">
                            <i class="fa-solid fa-truck"></i>
                            Đang giao
                        </button>
                        <button class="order-tab-btn" data-status="delivered">
                            <i class="fa-solid fa-circle-check"></i>
                            Đã giao
                        </button>
                        <button class="order-tab-btn" data-status="cancelled">
                            <i class="fa-solid fa-circle-xmark"></i>
                            Đã hủy
                        </button>
                    </div>

                    <div class="orders-container">
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <c:forEach var="order" items="${orders}">
                                    <div class="order-card-modern">
                                        <div class="order-card-header">
                                            <div class="order-id-section">
                                                <span class="order-label">Mã đơn hàng:</span>
                                                <span class="order-number">#${order.orderCode}</span>
                                            </div>
                                            <span class="order-status-badge ${order.orderStatus.toLowerCase()}">
                                                <c:choose>
                                                    <c:when test="${order.orderStatus eq 'PENDING'}">Chờ xác nhận</c:when>
                                                    <c:otherwise>${order.orderStatus}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>

                                        <div class="order-card-body">
                                            <c:forEach var="oi" items="${order.items}">
                                                <div class="order-product">
                                                    <img src="${oi.productUrl}" alt="${oi.productName}">
                                                    <div class="order-product-info">
                                                        <h4>${oi.productName}</h4>
                                                        <p class="order-product-qty">Số lượng: x${oi.quantity}</p>
                                                    </div>
                                                    <div class="order-product-price">
                                                        <span class="price-label">Đơn giá</span>
                                                        <span class="price-value"><fmt:formatNumber value="${oi.priceAtPurchase}" pattern="#,###"/>₫</span>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <div style="text-align: right; padding: 10px 0; border-top: 1px solid #eee;">
                                                <span style="color: #666;">Tổng số tiền: </span>
                                                <span style="color: var(--c6); font-size: 1.2rem; font-weight: 700;">
                                                    <fmt:formatNumber value="${order.finalAmount}" pattern="#,###"/>₫
                                                </span>
                                            </div>
                                        </div>

                                        <div class="order-card-footer">
                                            <div class="order-date">
                                                <i class="fa-regular fa-calendar"></i>
                                                <span><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                                            </div>
                                            <div class="order-actions">
                                                <a href="${pageContext.request.contextPath}/order-detail?orderId=${order.id}" class="btn-secondary">Xem chi tiết</a>
                                                <button class="btn-primary">Mua lại</button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div style="text-align: center; padding: 40px; color: #666;">
                                    <i class="fa-solid fa-box-open" style="font-size: 3rem; margin-bottom: 20px;"></i>
                                    <p>Bạn chưa có đơn hàng nào.</p>
                                    <a href="${pageContext.request.contextPath}/products" class="btn-primary" style="display: inline-block; margin-top: 20px;">Mua sắm ngay</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </article>

                <!-- Address Info Tab (NEW) -->
                <article id="address-info" class="profile-content">
                    <div class="content-header">
                        <h2>Địa chỉ của tôi</h2>
                        <p class="subtitle">Quản lý địa chỉ giao hàng của bạn</p>
                    </div>

                    <button class="add-address-btn" id="add-address-btn">
                        <i class="fa-solid fa-plus"></i>
                        Thêm địa chỉ mới
                    </button>

                    <div class="address-list" id="address-list">
                        <!-- Địa chỉ sẽ được load bằng JavaScript -->
<%--                        <p style="text-align: center; color: #666;">Đang tải...</p>--%>
                        <c:forEach var="ad" items="${userProfile.myAddresses}">
                            <div class="address-card" data-id="${ad.id}">
                                <div class="address-card-header">
                                    <div class="address-name">
                                        <h4>Địa chỉ ${ad.id}</h4>
                                        <c:if test="${ad.isDefault}">
                                            <span class="default-badge">Mặc định</span>
                                        </c:if>
                                    </div>
                                    <button class="address-edit-btn btn-edit-address"
                                            data-id="${ad.id}"
                                            data-house="${ad.houseNumber}"
                                            data-road="${ad.road}"
                                            data-hamlet="${ad.hamlet}"
                                            data-ward="${ad.ward}"
                                            data-district="${ad.district}"
                                            data-city="${ad.city}">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                </div>

                                <div class="address-card-body">
                                    <div class="address-detail">
                                        <i class="fa-solid fa-location-dot"></i>
                                        <span>
                                            ${ad.houseNumber}${not empty ad.road ? ', ' : ''}${ad.road}
                                            ${not empty ad.hamlet ? ', ' : ''}${ad.hamlet}
                                            ${not empty ad.ward ? ', ' : ''}${ad.ward}
                                            ${not empty ad.district ? ', ' : ''}${ad.district}
                                            ${not empty ad.city ? ', ' : ''}${ad.city}
                                        </span>
                                    </div>
                                </div>

                                <div class="address-card-footer">
                                    <c:if test="${!ad.isDefault}">
                                        <button class="btn-text-secondary btn-set-default" data-id="${ad.id}">
                                            Đặt làm mặc định
                                        </button>
                                    </c:if>
                                    <button class="btn-text btn-delete-address" data-id="${ad.id}">Xóa</button>
                                </div>
                            </div>
                        </c:forEach>


                    </div>
                </article>

                <!-- Voucher Info Tab -->
                <article id="voucher-info" class="profile-content">
                    <div class="content-header">
                        <h2>Ưu đãi của tôi</h2>
                        <p class="subtitle">Các voucher và mã giảm giá của bạn</p>
                    </div>

                    <div class="voucher-grid">
                        <c:forEach var="v" items="${userVouchers}">
                            <div class="voucher-card-modern ${v.voucher_type == 'SHIPPING' ? 'freeship' : ''}">
                                <div class="voucher-icon-badge ${v.voucher_type == 'SHIPPING' ? 'shipping' : (v.discount_percentage > 20 ? 'fire' : 'premium')}">
                                    <c:choose>
                                        <c:when test="${v.voucher_type == 'SHIPPING'}">
                                            <i class="fa-solid fa-truck-fast"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa-solid fa-percent"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="voucher-content">
                                    <h3>
                                        <c:choose>
                                            <c:when test="${v.voucher_type == 'SHIPPING'}">Miễn phí vận chuyển</c:when>
                                            <c:when test="${v.discount_percentage > 0}">Giảm ${v.discount_percentage}%</c:when>
                                            <c:otherwise>Giảm <fmt:formatNumber value="${v.discount_amount}" pattern="#,###"/>₫</c:otherwise>
                                        </c:choose>
                                    </h3>
                                    <p class="voucher-condition">Đơn từ <fmt:formatNumber value="${v.min_order_value}" pattern="#,###"/>₫</p>
                                    <div class="voucher-meta">
                                        <span class="voucher-code">${v.code}</span>
                                        <span class="voucher-expire">HSD: ${v.end_date}</span>
                                    </div>
                                </div>
                                <button class="use-voucher-btn" onclick="copyVoucherCode('${v.code}')">Dùng ngay</button>
                            </div>
                        </c:forEach>
                        <c:if test="${empty userVouchers}">
                            <div class="no-voucher">
                                <i class="fa-solid fa-ticket"></i>
                                <p>Bạn chưa có voucher nào. Hãy khám phá thêm ưu đãi nhé!</p>
                                <a href="${pageContext.request.contextPath}/voucher" class="btn-primary">Nhận voucher</a>
                            </div>
                        </c:if>
                    </div>
                </article>

                <!-- Security Tab -->
                <article id="security-info" class="profile-content">
                    <div class="content-header">
                        <h2>Bảo mật</h2>
                        <p class="subtitle">Quản lý mật khẩu và bảo mật tài khoản</p>
                    </div>

                    <div class="security-section">
                        <div class="security-item">
                            <div class="security-info">
                                <i class="fa-solid fa-lock"></i>
                                <div>
                                    <h4>Mật khẩu</h4>
                                    <p>Thay đổi mật khẩu thường xuyên để bảo mật tài khoản</p>
                                </div>
                            </div>
                            <button class="btn-secondary" id="change-password-btn">Đổi mật khẩu</button>
                        </div>

                        <div class="security-item">
                            <div class="security-info">
                                <i class="fa-solid fa-envelope"></i>
                                <div>
                                    <h4>Email xác minh</h4>
                                    <p id="security-email">${userProfile.email}</p>
                                </div>
                            </div>
                            <span class="verified-badge"><i class="fa-solid fa-circle-check"></i> Đã xác minh</span>
                        </div>

                        <%-- Tạm ẩn 2FA --%>
                        <%--
                        <div class="security-item">
                            <div class="security-info">
                                <i class="fa-solid fa-shield-halved"></i>
                                <div>
                                    <h4>Xác thực hai bước</h4>
                                    <p>Bảo vệ tài khoản với lớp bảo mật thứ hai</p>
                                </div>
                            </div>
                            <button class="btn-secondary">Bật</button>
                        </div>
                        --%>

                        <div class="security-item danger-zone">
                            <div class="security-info">
                                <i class="fa-solid fa-triangle-exclamation"></i>
                                <div>
                                    <h4>Vùng nguy hiểm</h4>
                                    <p>Đăng xuất hoặc xóa tài khoản vĩnh viễn</p>
                                </div>
                            </div>
                            <div class="danger-actions">
                                <button class="btn-outline" id="logout-btn">
                                    <i class="fa-solid fa-right-from-bracket"></i>
                                    Đăng xuất
                                </button>
                                <button class="btn-danger" id="delete-account-btn" data-username="${userProfile.name}">
                                    <i class="fa-solid fa-trash"></i>
                                    Xóa tài khoản
                                </button>
                            </div>
                        </div>
                    </div>
                </article>

            </section>



        </div>



    </main>
    <div id="delete-modal" class="modal" style="display: none;">
        <div class="modal-content">
            <h3>Xác nhận xóa tài khoản</h3>
            <p>Nhập <strong>USERNAME + MichiShop</strong> để xác nhận:</p>
            <input type="text" id="confirm-input" placeholder="Nhập xác nhận">
            <div class="modal-buttons">
                <button id="confirm-delete" class="btn btn-danger">Xác nhận</button>
                <button id="cancel-delete" class="btn btn-secondary">Hủy</button>
            </div>
            <p id="delete-msg" class="error-msg"></p>
        </div>
    </div>


    <jsp:include page="/customer/components/Footer.jsp"/>


    <div class="avt-modal-overlay" id="address-modal">
        <div class="avt-modal address-modal-content">
            <div class="avt-modal-header">
                <h3 id="address-modal-title">Thêm địa chỉ mới</h3>
                <button class="avt-modal-close" id="address-close-btn">&times;</button>
            </div>
            <div class="address-form-grid" style="padding: 20px; display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                <input type="hidden" id="modal-address-id">
                <div class="input-group">
                    <label>Số nhà</label>
                    <input type="text" id="modal-house" placeholder="Ví dụ: 123/45">
                </div>
                <div class="input-group">
                    <label>Đường</label>
                    <input type="text" id="modal-road" placeholder="Ví dụ: Cách Mạng Tháng 8">
                </div>
                <div class="input-group">
                    <label>Ấp/Hẻm</label>
                    <input type="text" id="modal-hamlet" placeholder="">
                </div>
                <div class="input-group">
                    <label>Phường/Xã</label>
                    <input type="text" id="modal-ward" placeholder="">
                </div>
                <div class="input-group">
                    <label>Quận/Huyện</label>
                    <input type="text" id="modal-district" placeholder="">
                </div>
                <div class="input-group">
                    <label>Tỉnh/Thành phố</label>
                    <input type="text" id="modal-city" placeholder="">
                </div>
                <div class="input-group" style="grid-column: span 2;" id="modal-default-wrapper">
                    <label style="display: flex; align-items: center; gap: 10px; cursor: pointer;">
                        <input type="checkbox" id="modal-default"> Đặt làm địa chỉ mặc định
                    </label>
                </div>
            </div>
            <div style="padding: 0 20px 20px; display: flex; gap: 10px; justify-content: flex-end;">
                <button class="btn-secondary" id="address-modal-cancel">Hủy</button>
                <button class="btn-primary" id="address-modal-submit">Lưu địa chỉ</button>
            </div>
        </div>
    </div>
    <div class="avt-modal-overlay" id="avt-modal">
        <div class="avt-modal">
            <div class="avt-modal-header">
                <h3>Chọn avatar</h3>
                <button class="avt-modal-close" id="avt-close-btn">&times;</button>
            </div>
            <div class="avt-category">
                <!-- copy các .avt-item từ trang hiện tại -->
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=PhatCute" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Naruto" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Itachi" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Luffy" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=BigMom" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Nobita" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Akatsuki" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Kakashi" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Zoro" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=BoaHancock" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Orochimaru" alt=""></div>
                <div class="avt-item"><img src="https://api.dicebear.com/7.x/avataaars/svg?seed=HoaThanhQue" alt=""></div>
            </div>
            <button class="avt-modal-confirm" id="avt-confirm-btn" disabled>Đồng ý</button>
        </div>
    </div>

    <!-- Modal cho Đổi tên -->
    <div class="avt-modal-overlay" id="name-modal">
        <div class="avt-modal">
            <div class="avt-modal-header">
                <h3>Thay đổi họ tên</h3>
                <button class="avt-modal-close" id="name-close-btn">&times;</button>
            </div>
            <div style="padding: 20px;">
                <div class="input-group">
                    <label>Họ và tên mới</label>
                    <input type="text" id="input-new-name" value="${userProfile.name}" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px;">
                </div>
            </div>
            <div style="padding: 0 20px 20px; display: flex; gap: 10px; justify-content: flex-end;">
                <button class="btn-secondary" id="name-cancel-btn">Hủy</button>
                <button class="btn-primary" id="name-submit-btn">Lưu thay đổi</button>
            </div>
        </div>
    </div>

    <!-- Modal cho Đổi số điện thoại -->
    <div class="avt-modal-overlay" id="phone-modal">
        <div class="avt-modal">
            <div class="avt-modal-header">
                <h3>Thay đổi số điện thoại</h3>
                <button class="avt-modal-close" id="phone-close-btn">&times;</button>
            </div>
            <div style="padding: 20px;">
                <div class="input-group">
                    <label>Số điện thoại mới</label>
                    <input type="text" id="input-new-phone" value="${userProfile.phone_number}" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px;">
                </div>
            </div>
            <div style="padding: 0 20px 20px; display: flex; gap: 10px; justify-content: flex-end;">
                <button class="btn-secondary" id="phone-cancel-btn">Hủy</button>
                <button class="btn-primary" id="phone-submit-btn">Lưu thay đổi</button>
            </div>
        </div>
    </div>

    <!-- Modal cho Đổi mật khẩu -->
    <div class="avt-modal-overlay" id="password-modal">
        <div class="avt-modal">
            <div class="avt-modal-header">
                <h3>Đổi mật khẩu</h3>
                <button class="avt-modal-close" id="password-close-btn">&times;</button>
            </div>
            <div style="padding: 20px; display: flex; flex-direction: column; gap: 15px;">
                <div class="input-group">
                    <label>Mật khẩu hiện tại</label>
                    <input type="password" id="input-old-pwd" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px;">
                </div>
                <div class="input-group">
                    <label>Mật khẩu mới</label>
                    <input type="password" id="input-new-pwd" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px;">
                </div>
                <div class="input-group">
                    <label>Xác nhận mật khẩu mới</label>
                    <input type="password" id="input-confirm-pwd" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px;">
                </div>
            </div>
            <div style="padding: 0 20px 20px; display: flex; gap: 10px; justify-content: flex-end;">
                <button class="btn-secondary" id="password-cancel-btn">Hủy</button>
                <button class="btn-primary" id="password-submit-btn">Cập nhật mật khẩu</button>
            </div>
        </div>
    </div>



    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/main.js"></script>
<script type="module" src="${pageContext.request.contextPath}/customer/scripts/profile/Profile.js"></script>
<script type="module" src="${pageContext.request.contextPath}/customer/scripts/profile/changeAvt.js"></script>
<script type="module" src="${pageContext.request.contextPath}/customer/scripts/profile/deleteAccount.js"></script>


<%--<script src="${pageContext.request.contextPath}/user/scripts/Order.js"></script>--%>
</body>
</html>
