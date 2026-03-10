<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activeTab", "");
%>


<%@ page import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ page import="user.model.cart.CartItem" %>
<%@ page import="user.model.cart.Cart" %>
<%@ page import="java.util.Map" %>
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

    if (!isLoggedIn)
    {
        response.sendRedirect(request.getContextPath() + "/user/pages/NotFoundPage.jsp");
        return;
    }
//    String username = isLoggedIn ? (String) session.getAttribute("username") : "";

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
    System.out.println("My cart: " + myCart);

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

    <div class="scroll-to-top-btn"><i class="fa-solid fa-circle-up"></i></div>

    <jsp:include page="/customer/components/Header.jsp"/>

    <main class="main">

        <div class="cart">
            <div class="cart__header">
                <h2 class="cart__title">Giỏ hàng</h2>
                <a href="${pageContext.request.contextPath}/home" class="cart__link">&lt; Tiếp tục mua hàng</a>
            </div>

            <table class="cart__table" aria-label="Giỏ hàng">
                <thead class="cart__table-head">
                    <tr class="cart__row cart__row--head">
                        <th class="--text-center" ><input type="checkbox" id="check-all" name=""></th>
                        <th class="--text-left">Sản phẩm</th>
                        <th class="--text-center">Đơn giá</th>
                        <th class="--text-center">Số lượng</th>
                        <th class="--text-center">Thành tiền</th>
                        <th class="--text-center">Thao tác</th>
                    </tr>
                </thead>

                <tbody class="cart__table-body">
                    <%
                        for(Map.Entry<Integer, CartItem> entry: myCart.getCart().entrySet())
                        {
                    %>
                    <tr class="cart__row" id="<%= entry.getKey()%>">

                        <td class="cart__select --text-center">
                            <input type="checkbox" class="cart__select-input" aria-label="Chọn sản phẩm" id="<%= entry.getKey()%>">
                        </td>

                        <td class="cart__product --text-left">
                            <img src="<%= entry.getValue().getProduct().getImg_url() %>" alt="Tên sản phẩm" class="cart__product-thumb" width="60" height="60">
                            <div class="cart__product-info">
                                <div class="cart__product-name"><%= entry.getValue().getProduct().getName()%></div>
<%--                                <div class="cart__product-variant">Vị: Nho • Loại: chai</div>--%>
                            </div>
                        </td>

                        <td class="cart__price --text-center"><%= df.format(entry.getValue().getPrice())%>₫</td>

                        <td class="cart__quantity --text-center">
                            <input type="number" class="cart__quantity-input" min="1" data-id="<%= entry.getKey() %>" value="<%= entry.getValue().getQuantity()%>" aria-label="Số lượng sản phẩm A">
                        </td>

                        <td class="cart__subtotal --text-center"><%= df.format(entry.getValue().getPrice() * entry.getValue().getQuantity())%>₫</td>

                        <td class="cart__action --text-center">
                            <button type="button" class="cart__remove-btn" aria-label="Xóa sản phẩm">
                                <i class="fa fa-trash"></i>
                            </button>
                        </td>

                    </tr>

                    <%
                        }
                    %>
                </tbody>

                <tfoot class="cart__table-foot">
                    <tr>
                        <td colspan="4" class="cart__note">Mã giảm giá có thể áp dụng ở bước thanh toán.</td>
                        <td class="cart__total-label">Tổng:</td>
                        <td class="cart__total-value"><%= df.format(total)%>₫</td>
                    </tr>
                    <tr>
                        <td colspan="3"></td>
                        <td></td>
                        <td></td>
                        <td class="cart__actions">
                            <a href="${pageContext.request.contextPath}/payment" class="pay-btn" style="text-decoration: none; display: inline-block; text-align: center;">Thanh toán</a>
                        </td>
                    </tr>
                </tfoot>
            </table>

            <div class="cart__empty" <%= (myCart == null || myCart.getCart().isEmpty()) ? "" : "hidden" %>>
                <p>Giỏ hàng của bạn đang trống.</p>
                <a class="cart__btn cart__btn--continue" href="<%= request.getContextPath() %>/product" style="color: var(--c6)">Tiếp tục mua sắm</a>
            </div>
        </div>


    </main>


    <jsp:include page="/customer/components/Footer.jsp"/>




<script type="module" src="${pageContext.request.contextPath}/customer/scripts/main.js"></script>
<script type="module" src="${pageContext.request.contextPath}/customer/scripts/cart/CartHandler.js"></script>
<script type="module" src="${pageContext.request.contextPath}/customer/scripts/cart/updateCart.js"></script>

</body>
</html>
