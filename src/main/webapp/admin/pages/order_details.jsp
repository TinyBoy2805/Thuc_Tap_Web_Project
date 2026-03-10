<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MiChiShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/pages/order_details.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>

<body>
<div class="order_details main">
    <aside class="sidebar">
        <% request.setAttribute("activePage", "orders"); %>
        <%@ include file="../components/sidebar.jsp" %>
    </aside>
    <div class="container">
        <%@ include file="../components/header.jsp" %>

        <!-- contert -->
        <div class="container__content">
            <!-- header incules name of section and the back button -->
            <div class="content__header">
                <a href="${pageContext.request.contextPath}/admin/orders">
                    <ion-icon name="chevron-back-outline"></ion-icon>
                </a>
                <h2 class="content__title">Chi tiết đơn hàng</h2>
            </div>
            <!-- First card about image of order and some info about order -->
            <div class="content__info">
                <c:forEach items="${orderItems}" var="item">
                    <div class="orders">
                        <div class="orders__img">
                            <img src="${item.img_url}" alt="${item.name}"/>
                            <span class="orders__status confirm">${item.orderStatus}</span>
                        </div>
                        <div class="orders__info">
                            <p class="orders__name">${item.name}<span class="orders__id">
                                ${item.orderCode}</span></p>
                            <div class="order-sumary">
                                <div class="order-sumary__details">
                                    <p class="orders__price">Số lượng: ${item.quantity}</p>
                                    <p class="orders__price">Giá: <fmt:formatNumber value="${item.priceAtPurchase}"
                                                                                    type="number"
                                                                                    groupingUsed="true"/>đ</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <!-- Second card about user who buy this order -->
                <div class="customer">
                    <p class="customer__name">${customer.name}</p>
                    <div class="customer__info">
                        <div class="info">
                            <p>Email:</p>
                            <p class="info-detail">${customer.email}</p>
                        </div>
                        <div class="info">
                            <p>Số điện thoại:</p>
                            <p class="info-detail">${customer.phoneNumber}</p>
                        </div>
                        <div class="info">
                            <p>Địa chỉ:</p>
                            <p class="info-detail">${customer.address}</p>
                        </div>
                        <div class="info">
                            <p>Hình thức thanh toán:</p>
                            <p class="info-detail">${customer.paymentMethod}</p>
                        </div>
                        <div class="info">
                            <p>Ngày tạo đơn:</p>
                            <p class="info-detail"><fmt:formatDate value="${customer.orderCreateAtDate}"
                                                                   pattern="HH:mm:ss dd/MM/yyyy"/></p>
                        </div>
                    </div>
                </div>

                <div class="total-summary">
                    <div class="total-info">
                        <div class="info">
                            <p class="orders__price">Tổng tiền:</p>
                            <p class="info-detail"><fmt:formatNumber value="${totalPrice.totalPrice}"
                                                 type="number"
                                                 groupingUsed="true"/>đ</p>
                        </div>
                        <div class="info">
                            <p class="orders__voucher">Ưu đãi:</p>
                            <p class="info-detail">-<fmt:formatNumber value="${totalPrice.discountAmount}"
                                                  type="number"
                                                  groupingUsed="true"/>đ</p>
                        </div>
                        <div class="info">
                            <p class="orders__ship-fee">Vận chuyển:</p>
                            <p class="info-detail"><fmt:formatNumber value="${totalPrice.shippingFee}"
                                                 type="number"
                                                 groupingUsed="true"/>đ</p>
                        </div>
                        <div class="info">
                            <p class="total__price">Tổng thanh toán:</p>
                            <P class="info-detail"><fmt:formatNumber value="${totalPrice.finalAmount}"
                                                 type="number"
                                                 groupingUsed="true"/>đ</P>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<!-- link to javascript for burger button -->
<script src="${pageContext.request.contextPath}/admin/scripts/components/extendSidebar.js"></script>
<script type="module" src="${pageContext.request.contextPath}/admin/scripts/order/order.js"></script>

</body>

</html>