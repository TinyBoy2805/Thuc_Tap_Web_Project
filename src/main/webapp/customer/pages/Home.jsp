<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setAttribute("activeTab", "home");
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MichiShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/styles/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script>
        window.APP_CONTEXT_PATH = `${pageContext.request.contextPath}`;
    </script>

</head>
<body>


    <div class="toast-container"></div>

    <jsp:include page="/customer/components/ProductCard.jsp"/>

    <div class="scroll-to-top-btn"><i class="fa-solid fa-circle-up"></i></div>


    <jsp:include page="/customer/components/Header.jsp"/>

    <main class="main">

        <section class="main__hero-banner">

            <div class="main__hero-banner-content">

                <div class="main__hero-banner-content-sub1">
                    <p class="--weight550"><i class="fa-solid fa-shield"></i> Cửa hàng trách nhiệm & uy tín</p>
                    <h2><span>Sữa & Thực phẩm</span> dinh dưỡng cho trẻ nhỏ</h2>
                    <p>Chúng tôi đồng hành cùng hành trình khôn lớn của bé yêu – mang đến những sản phẩm sữa và thực phẩm dinh dưỡng chính hãng, an toàn và giàu yêu thương, để mỗi bữa ăn, mỗi giọt sữa đều trở thành nguồn năng lượng trong lành nuôi dưỡng tương lai khỏe mạnh và hạnh phúc cho con.</p>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/product" class="--color6">Khám phá sản phẩm của chúng tôi</a></li>
                        <li><a href="${pageContext.request.contextPath}/customer/pages/About.jsp" class="--color6"><i class="fa-solid fa-circle-info"></i></a></li>
                    </ul>
                </div>

                <div class="main__hero-banner-content-line"></div>

                <div class="main__hero-banner-content-sub2">
                    <ul>
                        <li>
                            <h4>${user_count_formatted}</h4>
                            <p>Người dùng tin cậy</p>
                        </li>
                        <li>
                            <h4>100%</h4>
                            <p>Cam kết sản phẩm an toàn</p>
                        </li>
                        <li>
                            <h4><i class="fa-solid fa-star"></i> ${avg_rating}</h4>
                            <p>Từ 3 nghìn đánh giá</p>
                        </li>
                    </ul>
                </div>

            </div>

            <div class="main__hero-banner-image">
                <div class="main__hero-banner-image-container">
                    <img src="${pageContext.request.contextPath}/customer/imgs/herobanner2.jpg" alt="">
                </div>
            </div>

        </section>

        <section class="main__categories">
            <h3>Danh mục sản phẩm</h3>
            <div class="main__categories-list">
                <button class="left-btn"><</button>
                <button class="right-btn">></button>
                <ul data-total="${categories.size()}">
                    <c:forEach var="c" items="${categories}">
                        <li><i class="fa-solid fa-icons"></i><a href="${pageContext.request.contextPath}/product?category=${c}" title="Sữa bột">${c}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </section>
<!-- <i class="fa-solid fa-gift"></i> -->
 <!-- <i class="fa-solid fa-money-bill"></i> -->
<%--        <i class="fa-regular fa-truck"></i>--%>




        <section class="main__vouchers">
            <h3>Ưu đãi -  Khuyến mãi</h3>
            <div class="main_vouchers-list">
                <ul>

                    <c:forEach var="v" items="${vouchers}">
                        <li>
                            <div class="voucher">
                                <c:set var="voucher_color" value="background: var(--c9)" scope="page"/>
                                <c:set var="voucher_text" value="phí ship"/>
                                <c:set var="voucher_icon" value="fa-regular fa-truck"/>
                                <c:if test="${v.voucher_type == 'DISCOUNT'}">
                                    <c:set var="voucher_color" value="background: var(--c6)" scope="page"/>
                                    <c:set var="voucher_text" value="giảm giá"/>
                                    <c:set var="voucher_icon" value="fa-solid fa-money-bill"/>
                                </c:if>

                                <div class="voucher__left" style="${voucher_color}">
                                    <p class="--size20">
                                        Ưu đãi ${voucher_text}
                                        <i class="${voucher_icon}"></i>
                                    </p>
                                    <p class="--size64 --weight700">-<fmt:formatNumber value="${v.discount_percentage}" type="number" maxFractionDigits="0" pattern="#,###"/>%</p>
                                    <p class="--size16">
                                        Áp dụng cho đơn từ
                                        <fmt:formatNumber value="${v.min_order_value}" type="number" maxFractionDigits="0" pattern="#,###"/> đ
                                    </p>
                                    <p class="left__info">còn lại: ${v.current_amount}</p>
                                </div>
                                <div class="voucher__right">
                                    <button type="button" data-voucher-id="${v.id}">Nhận ưu đãi</button>
                                    <p>HSD: ${v.end_date}</p>
                                </div>
                            </div>
                        </li>
                    </c:forEach>


                </ul>
            </div>
        </section>


        <section class="main__trending">

            <div class="main__trending-tabs">

                    <input type="hidden" value="" id="typeInput" name="trending_type">
                    <ul>
                        <li class="main__trending-tab active" id="search_trending">Top tìm kiếm</li>
                        <li class="main__trending-tab " id="sell_trending">Top lượt bán</li>
                        <li class="main__trending-tab " id="rating_trending">Top đánh giá</li>
                        <div class="main__trending-tab-line"></div>
                    </ul>

            </div>
            <!-- <div class="main__trending-line"></div> -->


            <div class="main__trending-content active">
                <ul class="main__trending-content-ul">

                </ul>
            </div>

        </section>

        <!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
        <section class="main__today-suggestion">
            <h3 class="main__today-suggestion-heading">Gợi ý hôm nay</h3>
            <div class="main__today-suggestion-list">
                <ul class="main__today-suggestion-list-ul">
                    <form action="${pageContext.request.contextPath}/product-detail" method="get" style="display: none;" id="product__form">
                        <input type="hidden" value="" name="product_id" id="product__id">
                    </form>
                </ul>
                <button class="main__today-suggestion-more" id="home-more-button">Xem thêm</button>
            </div>
        </section>

        <section class="main__rating">
            <form action="" class="main__rating-form">
                <h3 class="main__rating-form-heading">Đánh giá cửa hàng</h3>
                <fieldset>
                    <ul>
                        <li><i class="fa-solid fa-star st"></i></li>
                        <li><i class="fa-solid fa-star st"></i></li>
                        <li><i class="fa-solid fa-star st"></i></li>
                        <li><i class="fa-solid fa-star st"></i></li>
                        <li><i class="fa-solid fa-star st"></i></li>
                    </ul>
                </fieldset>
                <fieldset>
                    <h4>Nhận xét</h4>
                    <textarea name="" id="review-textarea"></textarea>
                </fieldset>
                <button>Gửi đánh giá</button>
            </form>
        </section>



        <section class="main__contact">
            <h3>Các vấn đề thường gặp</h3>
            <div class="main__contact-list">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp">Hoàn trả sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp">Hoàn tiền</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp">Giảm giá sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp">Không thể liên hệ</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/pages/Contact.jsp">Lỗi mua hàng</a></li>
                </ul>
            </div>
        </section>


    </main>


    <jsp:include page="/customer/components/Footer.jsp"/>


    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js" defer></script>
    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/main.js" defer></script>
    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/home/Home.js" defer></script>
    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/product/addToCart.js" defer></script>
    <script type="module" src="${pageContext.request.contextPath}/customer/scripts/voucherPage/useVoucher.js" defer></script>
    <script type="module">
        import {initCKEditor} from "${pageContext.request.contextPath}/user/scripts/utils/initCkeditor.js";
        window.addEventListener("DOMContentLoaded", () => {
            initCKEditor("#review-textarea");
        });
    </script>
</body>

</html>
