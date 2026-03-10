<%@ page import="user.model.cart.Cart" %>
<%@ page import="user.model.cart.CartItem" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ page import="admin.model.User" %>
<%@ page import="admin.service.OrderService" %>
<%
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat df = new DecimalFormat("#,###", symbols);
%>

<%

    boolean isLoggedIn = session.getAttribute("isLoggedIn") != null
                        ? (Boolean) session.getAttribute("isLoggedIn")
                        : false;

    String username = isLoggedIn ? (String) session.getAttribute("username") : "";
    User user = isLoggedIn ? (User) session.getAttribute("user") : null;

    Cart myCart = (Cart) session.getAttribute("cart");

    if(myCart == null) myCart = new Cart();

    double total = 0;
    if (myCart != null)
    {
        for (CartItem item : myCart.getCart().values())
        {
            total += item.getPrice() * item.getQuantity();
        }
    }

%>



<header class="header ${activeTab eq 'product_detail' ? 'active' : ''}">
    <nav class="nav">
        <div class="nav__top">
            <a class="nav__top-logo" href="${pageContext.request.contextPath}/home">
                <div class="nav__logo-img">
                    <img src="${pageContext.request.contextPath}/customer/imgs/Gemini_Generated_Image_c648fqc648fqc648.png" alt="">
                </div>
                <h1>MichiShop</h1>
            </a>
            <div class="nav__top-input">
                <form action="Products.jsp" id="search_form">
                    <input type="text" placeholder="Sản phẩm bạn cần tìm..." class="--no-border --no-outline" id="search_bar" value="${not empty searchKeyword ? searchKeyword : ''}">
                    <button class="--no-border --no-outline" type="submit">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        Tìm kiếm
                    </button>
                </form>
            </div>
            <div class="nav__top-actions">
                <ul>
                    <% if (isLoggedIn) { %>
                        <!-- User đã login: hiện cart, bell, avatar -->
                        <li class="hovercart">
                            <a href="${pageContext.request.contextPath}/cart" class="--color4" id="cart-icon"><i class="fa-solid fa-cart-shopping --size20"></i></a>
                            <div class="header-cart-modern">
                                <div class="cart-header">
                                    <div class="cart-title">
                                        <i class="fa-solid fa-cart-shopping"></i>
                                        <h3>Giỏ hàng của tôi</h3>
                                    </div>
                                    <span class="cart-count-badge"><%= myCart.getCart().size()%> sản phẩm</span>
                                </div>

                                <div class="cart-items-list">

                                    <%
                                        // myCart là Map<Integer, CartItem>
                                        for (Map.Entry<Integer, CartItem> entry : myCart.getCart().entrySet())
                                        {
                                            CartItem item = entry.getValue();
                                    %>
                                    <div class="cart-item">
                                        <div class="cart-item-img">
                                            <img src="<%= item.getProduct().getImg_url() %>" alt="">
                                        </div>
                                        <div class="cart-item-info">
                                            <h4><%= item.getProduct().getName() %></h4>
                                            <p class="cart-item-qty">x<%= item.getQuantity() %></p>
                                        </div>
                                        <div class="cart-item-price">
                                            <span><%= df.format(item.getPrice()) %>₫</span>
                                        </div>
                                    </div>
                                    <%
                                        } // kết thúc for loop
                                    %>



                                </div>

                                <div class="cart-footer">
                                    <div class="cart-total">
                                        <span>Tổng cộng</span>
                                        <span class="total-price"><%= df.format(total)%>₫</span>
                                    </div>
                                    <button class="view-cart-btn" onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                                        <span>Xem giỏ hàng</span>
                                        <i class="fa-solid fa-arrow-right"></i>
                                    </button>
                                </div>
                            </div>
                        </li>

<%--                        //INFORM BUTTON--%>
                        <li class="hover-notification">
                            <a href="${pageContext.request.contextPath}/notification/detail" class="--color4"><i class="fa-solid fa-bell --size20"></i></a>
<%--                            //count new inform and unread--%>
                            <div class="notif-count">2</div>

                            <div class="notification-dropdown">
                                <div class="notification-header">
                                    <h3>Thông báo</h3>
                                    <a href="${pageContext.request.contextPath}/notification/mark" class="mark-all-read">Đánh dấu đã đọc</a>
                                </div>

                                <template id="inform_template">
                                    <div class="notification-item unread">
                                        <div class="notification-icon order">
                                            <i class="fa-solid fa-box"></i>
                                        </div>
                                        <div class="notification-content">
                                            <p class="notification-text">title</p>
                                            <span class="notification-time">timestamp</span>
                                        </div>
                                    </div>
                                </template>
                                <div class="notification-list">

                                </div>

                                <div class="notification-footer">
                                    <a href="${pageContext.request.contextPath}/notification/detail" class="view-all-notifications">
                                        <span>Xem tất cả</span>
                                        <i class="fa-solid fa-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                        </li>

                        <li class="hover-avt">
                            <div class="container">
                                <a href="${pageContext.request.contextPath}/profile" class="--color4"><i class="fa-solid fa-user --size20"></i></a>
                                <div class="avt-options">
                                    <div class="avt-profile-card">
                                        <div class="avt-container">
                                            <img src="<%= user.getAvt_url()%>" alt="">
                                            <div class="online-status"></div>
                                        </div>
                                        <div class="profile-info">
                                            <h4><%= username %></h4>
                                            <p>@<%= username.toLowerCase().replace(" ", "") %></p>
                                        </div>
                                        <button class="view-profile-btn" onclick="window.location.href='${pageContext.request.contextPath}/profile'">
                                            <i class="fa-solid fa-arrow-right"></i>
                                        </button>
                                    </div>

                                    <div class="quick-stats">
                                        <%
                                            OrderService headerOrderService = new OrderService();
                                            user.service.VoucherService headerVoucherService = new user.service.VoucherService();
                                            int orderCount = headerOrderService.getOrdersByUser(user.getId()).size();
                                            int voucherCount = headerVoucherService.getUserVouchers(user.getId()).size();
                                        %>
                                        <div class="stat-item">
                                            <i class="fa-solid fa-box"></i>
                                            <div>
                                                <span class="stat-number"><%= orderCount %></span>
                                                <span class="stat-label">Đơn hàng</span>
                                            </div>
                                        </div>
                                        <div class="stat-item">
                                            <i class="fa-solid fa-ticket"></i>
                                            <div>
                                                <span class="stat-number"><%= voucherCount %></span>
                                                <span class="stat-label">Voucher</span>
                                            </div>
                                        </div>
                                    </div>

                                    <ul class="avt-menu-modern">
                                        <li onclick="window.location.href='${pageContext.request.contextPath}/profile'">
                                            <div class="menu-icon">
                                                <i class="fa-regular fa-user"></i>
                                            </div>
                                            <div class="menu-content">
                                                <span class="menu-title">Tài khoản</span>
                                                <span class="menu-desc">Quản lý thông tin cá nhân</span>
                                            </div>
                                            <i class="fa-solid fa-chevron-right menu-arrow"></i>
                                        </li>
                                        <li onclick="window.location.href='${pageContext.request.contextPath}/profile'">
                                            <div class="menu-icon">
                                                <i class="fa-solid fa-receipt"></i>
                                            </div>
                                            <div class="menu-content">
                                                <span class="menu-title">Đơn hàng</span>
                                                <span class="menu-desc">Theo dõi đơn hàng</span>
                                            </div>
                                            <i class="fa-solid fa-chevron-right menu-arrow"></i>
                                        </li>
                                        <li onclick="window.location.href='${pageContext.request.contextPath}/customer/pages/Inform.jsp'">
                                            <div class="menu-icon">
                                                <i class="fa-solid fa-bell"></i>
                                            </div>
                                            <div class="menu-content">
                                                <span class="menu-title">Thông báo</span>
                                                <span class="menu-desc">Cập nhật mới nhất</span>
                                            </div>
                                            <span class="notification-badge">2</span>
                                        </li>
                                        <li onclick="window.location.href='${pageContext.request.contextPath}/profile'">
                                            <div class="menu-icon">
                                                <i class="fa-solid fa-gear"></i>
                                            </div>
                                            <div class="menu-content">
                                                <span class="menu-title">Cài đặt</span>
                                                <span class="menu-desc">Bảo mật & quyền riêng tư</span>
                                            </div>
                                            <i class="fa-solid fa-chevron-right menu-arrow"></i>
                                        </li>
                                    </ul>

                                    <div class="avt-footer-modern">
                                        <button class="logout-btn-modern" onclick="window.location.href='${pageContext.request.contextPath}/auth/logout'">
                                            <i class="fa-solid fa-right-from-bracket"></i>
                                            <span>Đăng xuất</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </li>
                    <% } else { %>
                        <!-- User chưa login: chỉ hiện nút Đăng nhập -->
                        <li class="login-btn">
                            <a href="${pageContext.request.contextPath}/index.jsp" style="display: inline-block; padding: 8px 20px; background: #F564A9; color: white; text-decoration: none; border-radius: 6px; font-weight: 500; transition: all 0.3s;">
                                Đăng nhập
                            </a>
                        </li>
                    <% } %>
                </ul>
            </div>
        </div>
        <div class="nav__line" style="display: ${activeTab eq 'product_detail' ? 'none' : 'block'}"></div>
        <div class="nav__bottom" style="display: ${activeTab eq 'product_detail' ? 'none' : 'block'}">
            <ul>
                <li><a href="${pageContext.request.contextPath}/home" class="${activeTab eq 'home' ? 'active' : ''}">Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/product" class="${activeTab eq 'product' ? 'active' : ''}">Sản phẩm</a></li>
                <li><a href="${pageContext.request.contextPath}/voucher" class="${activeTab eq 'voucher' ? 'active' : ''}">Khuyến mãi</a></li>
                <li><a href="${pageContext.request.contextPath}/blog" class="${activeTab eq 'blog' ? 'active' : ''}">Cẩm nang</a></li>
                <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp" class="${activeTab eq 'contact' ? 'active' : ''}">Liên hệ</a></li>
                <li><a href="${pageContext.request.contextPath}/about" class="${activeTab eq 'about' ? 'active' : ''}">về cửa hàng</a></li>
            </ul>
        </div>
    </nav>
</header>
<script src="${pageContext.request.contextPath}/customer/scripts/headerScript/header.js" defer></script>
<script src="${pageContext.request.contextPath}/customer/scripts/headerScript/getInform.js" defer></script>

