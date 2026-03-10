<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MiChiShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/header.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/sidebar.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/pages/KhachHang.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
  <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
  <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</head>

<body>
  <div class="KhachHang main">
    <aside class="sidebar">
      <% request.setAttribute("activePage", "khachhang"); %>
      <%@ include file="../components/sidebar.jsp" %>
    </aside>
    <div class="container">
      <%@ include file="../components/header.jsp" %>
  
      <!-- Main content -->
      <main class="content" aria-labelledby="customer-title">
        <h3 class="content__title">Khách hàng</h3>
        <div class="content__panel">
        <div class="panel-header">
        <div class="tabs" role="tablist" aria-label="Lọc khách hàng">
      <button type="button" class="tab-btn" aria-selected="true" onclick="window.location.href='${pageContext.request.contextPath}/admin/customer'">Tất cả khách hàng</button>
      </div>
      <div class="search__filter">
      <form method="get" action="${pageContext.request.contextPath}/admin/customer" class="search__box search__box--form">
       <input type="text" name="search" id="customer-search" placeholder="Tìm kiếm khách hàng" value="${search != null ? search : ''}" class="search__input" />
       <button type="submit" class="search__submit">
       <i class="fa-solid fa-search"></i>
      </button>
      </form>
      </div>
      </div>

      <input type="radio" name="kh_tab" id="kh_tab_all" checked hidden>
      
          <div class="customers customers-all" role="list">
            <c:choose>
              <c:when test="${not empty customers}">
                <c:forEach var="customer" items="${customers}">
                  <article class="card" role="listitem">
                    <figure class="card__figure">
                      <img src="${empty customer.avt_url ? '../imgs/logo.png' : customer.avt_url}" alt="Avatar">
                    </figure>
                    <div class="info">
                      <h4>${customer.name}</h4>
                      <p><i class="fa-solid fa-envelope"></i> ${customer.email}</p>
                      <p><i class="fa-solid fa-phone"></i> ${customer.phone_number}</p>
                      <button class="detail-btn" type="button"
                              onclick="window.location.href='${pageContext.request.contextPath}/admin/manage_customer?id=${user.id}'">
                        Chi tiết
                      </button>
                    </div>
                  </article>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <p>Chưa có khách hàng nào.</p>
              </c:otherwise>
            </c:choose>
          </div> 
        </div> 
    </div> 
    <script src="${pageContext.request.contextPath}/admin/scripts/components/extendSidebar.js"></script>
</body>

</html>