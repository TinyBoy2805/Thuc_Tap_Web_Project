<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- Sidebar dùng chung cho các trang admin --%>
<c:set var="uri" value="${pageContext.request.requestURI}" />
<div class="sidebar__header">
  <img src="${pageContext.request.contextPath}/admin/imgs/logo.png" alt="Logo">
  <h2>MiChiShop</h2>
</div>
<div class="sidebar__list">
  <ul class="list">
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="${fn:contains(uri, '/admin/pages/dashboard.jsp') ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-chart-line"></i></div>
          <p class="title">Thống kê</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/orders" class="${fn:contains(uri, '/admin/pages/order.jsp') ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-shopping-cart"></i></div>
          <p class="title">Đơn hàng</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/products" class="${fn:contains(uri, '/admin/pages/product.jsp') ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-box"></i></div>
          <p class="title">Kho hàng</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/customer" class="${fn:contains(uri, '/admin/user') || fn:contains(uri, '/admin/manage_customer') || fn:contains(uri, '/admin/pages/KhachHang.jsp') || fn:contains(uri, '/admin/pages/Quanlykhachhang.jsp')  ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-users"></i></div>
          <p class="title">Khách hàng</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/voucher" class="${fn:contains(uri, '/admin/voucher') || fn:contains(uri, '/admin/manage_voucher') || fn:contains(uri, '/admin/add_voucher') || fn:contains(uri, '/admin/pages/UuDai.jsp') || fn:contains(uri, '/admin/pages/ThemUuDai.jsp') || fn:contains(uri, '/admin/pages/QuanLyUuDai.jsp')  ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-gift"></i></div>
          <p class="title">Ưu đãi</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/blog" class="${fn:contains(uri, '/admin/blog') || fn:contains(uri, '/admin/manage_blog') || fn:contains(uri, '/admin/add_blog') || fn:contains(uri, '/admin/pages/Blog.jsp') || fn:contains(uri, '/admin/pages/QuanLyBlog.jsp')  ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class=" fa-solid fa-blog"></i></div>
          <p class="title">Bài Viết</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/contact" class="${fn:contains(uri, '/admin/pages/Email.jsp') || fn:contains(uri, '/admin/contact') ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-envelope"></i></div>
          <p class="title">Thông Báo</p>
        </div>
      </a>
    </li>
    <li class="list__item">
      <a href="${pageContext.request.contextPath}/admin/setting" class="${fn:contains(uri, '/admin/pages/CaiDat.jsp') || fn:contains(uri, '/admin/setting') ? 'active' : ''}">
        <div class="section">
          <div class="icon"><i class="fa-solid fa-cog"></i></div>
          <p class="title">Cài đặt</p>
        </div>
      </a>
    </li>
  </ul>
</div>
<div class="sidebar__bottom">
  <a href="${pageContext.request.contextPath}/index.jsp">
    <div class="icon"><ion-icon name="exit-outline"></ion-icon></div>
    <p class="title">Đăng xuất</p>
  </a>
</div>
