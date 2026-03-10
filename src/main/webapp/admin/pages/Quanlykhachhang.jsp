<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>MiChiShop - Quản lý khách hàng</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/header.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/components/sidebar.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/pages/Quanlykhachhang.css" />
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
      <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
      <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </head>

    <body>
      <div class="Quanlykhachhang main">
        <aside class="sidebar">
          <% request.setAttribute("activePage", "khachhang" ); %>
            <%@ include file="../components/sidebar.jsp" %>
        </aside>
        <div class="container">
          <%@ include file="../components/header.jsp" %>

            <!-- Main content -->
            <main class="content" aria-labelledby="customer-title">
              <h3 class="content__title">
                <a href="${pageContext.request.contextPath}/admin/customer" class="back-icon">
                  <i class="fa-solid fa-chevron-left"></i>
                </a>
                Quản lý khách hàng
              </h3>

              <div class="customer-edit-wrap">

                <div class="content__body">
                  <div class="form-wrap">
                    <section class="left-panel">
                      <div class="profile-section">
                        <h3 class="section-title">Thông tin cá nhân</h3>
                        <div class="profile-field">
                          <label>Họ và tên:</label>
                          <div class="field-content">
                            <input type="text" class="field-input" name="name" value="${customer.name}"
                              form="updateForm">
                            <i class="fa-solid fa-pen input-icon"></i>
                          </div>
                        </div>
                        <div class="profile-field">
                          <label>Email:</label>
                          <div class="field-content">
                            <input type="email" class="field-input" name="email" value="${customer.email}"
                              form="updateForm">
                            <i class="fa-solid fa-pen input-icon"></i>
                          </div>
                        </div>
                        <div class="profile-field">
                          <label>Số điện thoại:</label>
                          <div class="field-content">
                            <input type="text" class="field-input" name="phone_number" value="${customer.phone_number}"
                              form="updateForm">
                            <i class="fa-solid fa-pen input-icon"></i>
                          </div>
                        </div>
                        <div class="profile-field">
                          <label>Địa chỉ:</label>
                          <div class="field-content address-grid">
                            <input type="text" class="field-input" name="house_number" value="${address.houseNumber}" placeholder="Số nhà" form="updateForm">
                            <input type="text" class="field-input" name="road" value="${address.road}" placeholder="Đường" form="updateForm">
                            <input type="text" class="field-input" name="district" value="${address.district}" placeholder="Quận/Huyện" form="updateForm">
                            <input type="text" class="field-input" name="city" value="${address.city}" placeholder="Tỉnh/TP" form="updateForm">
                            <input type="text" class="field-input" name="hamlet" value="${address.hamlet}" placeholder="Khu phố" form="updateForm">
                            <input type="text" class="field-input" name="ward" value="${address.ward}" placeholder="Phường/Xã" form="updateForm">
                          </div>
                        </div>
                      </div>
                      <!-- Account Status Section -->
                      <div class="profile-section">
                        <h3 class="section-title">Cài đặt tài khoản</h3>

                        <div class="status-row" style="display: flex; flex-direction: column; align-items: flex-end; gap: 12px;">
                          <button class="reset-btn" type="button" onclick="showChangePasswordModal()" style="width: 300px; background: #fff; color: #f564a9; border: 2px solid #f564a9; border-radius: 6px; font-weight: 500; font-size: 1rem; box-shadow: 0 2px 8px #f564a93a;">
                            <i class="fa-solid fa-key" style="color:#f564a9;"></i> Đổi mật khẩu
                          </button>
                          <!-- Modal đổi mật khẩu -->
                          <div id="changePasswordModal" class="modal"
                            style="display:none;position:fixed;z-index:9999;left:0;top:0;width:100vw;height:100vh;background:rgba(0,0,0,0.3);align-items:center;justify-content:center;">
                            <div
                              style="background:#fff;padding:40px 36px 32px 36px;border-radius:16px;min-width:400px;max-width:98vw;box-shadow:0 4px 32px #0003;position:relative;">
                              <h3 style="color:#f564a9;font-weight:700;font-size:1.35rem;margin-bottom:24px;text-align:center;letter-spacing:0.5px;">Đổi mật khẩu cho khách hàng</h3>
                              <div id="change-password-fields">
                                <div style="margin-bottom:12px;position:relative;">
                                  <label style="font-weight:500;">Mật khẩu mới:</label><br>
                                  <input type="password" id="new_password_modal" required
                                    style="width:100%;padding:10px 40px 10px 12px;margin-top:6px;border-radius:8px;border:1.5px solid #e0e0e0;font-size:1rem;transition:border 0.2s;outline:none;">
                                  <span onclick="togglePassword('new_password_modal', this)" style="position:absolute;top:38px;right:16px;cursor:pointer;font-size:20px;color:#f564a9;">
                                    <i class="fa fa-eye-slash"></i>
                                  </span>
                                </div>
                                <div style="margin-bottom:12px;position:relative;">
                                  <label style="font-weight:500;">Xác nhận mật khẩu mới:</label><br>
                                  <input type="password" id="confirm_password_modal" required
                                    style="width:100%;padding:10px 40px 10px 12px;margin-top:6px;border-radius:8px;border:1.5px solid #e0e0e0;font-size:1rem;transition:border 0.2s;outline:none;">
                                  <span onclick="togglePassword('confirm_password_modal', this)" style="position:absolute;top:38px;right:16px;cursor:pointer;font-size:20px;color:#f564a9;">
                                    <i class="fa fa-eye-slash"></i>
                                  </span>
                                </div>
                                <div style="display:flex;gap:8px;justify-content:flex-end;">
                                  <button type="button" onclick="hideChangePasswordModal()"
                                    style="padding:8px 24px;background:#fff;color:#f564a9;border:1.5px solid #f564a9;border-radius:6px;font-weight:500;font-size:1rem;transition:background 0.2s;">Hủy</button>
                                  <button type="button" onclick="submitChangePassword()"
                                    style="padding:8px 24px;background:#f564a9;color:#fff;border:none;border-radius:6px;font-weight:500;font-size:1rem;box-shadow:0 2px 8px #f564a97a;transition:background 0.2s;">Đổi mật khẩu</button>
                                </div>
                              </div>
                              <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
                              <script>
                              function togglePassword(inputId, iconSpan) {
                                var input = document.getElementById(inputId);
                                var icon = iconSpan.querySelector('i');
                                if (input.type === 'password') {
                                  input.type = 'text';
                                  icon.classList.remove('fa-eye-slash');
                                  icon.classList.add('fa-eye');
                                } else {
                                  input.type = 'password';
                                  icon.classList.remove('fa-eye');
                                  icon.classList.add('fa-eye-slash');
                                }
                              }
                              </script>
                              <button onclick="hideChangePasswordModal()"
                                style="position:absolute;top:12px;right:18px;background:none;border:none;font-size:22px;color:#f564a9;">&times;</button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </section>


                    <aside class="right-panel">
                      <div class="profile-avatar-card">
                        <div class="avatar-container">
                          <img id="avatar-preview"
                            src="${empty customer.avt_url ? pageContext.request.contextPath.concat('/imgs/logo.png') : customer.avt_url}"
                            style="max-width:120px;max-height:120px;border-radius:50%;object-fit:cover;">
                        </div>
                        <label class="edit-avatar-btn" style="cursor:pointer;">
                          <i class="fa-solid fa-pen"></i> Đổi ảnh đại diện
                          <input type="file" name="avatar" id="avatar" accept="image/*" style="display:none;"
                            form="updateForm">
                        </label>
                      </div>
                    </aside>
                  </div>
                </div>

                <div class="update-info-section">
                    <c:if test="${not empty sessionScope.message}">
                      <div data-success-message style="display:none;">${sessionScope.message}</div>
                      <% session.removeAttribute("message"); %>
                    </c:if>
                    <c:if test="${not empty sessionScope.error}">
                      <div data-error-message style="display:none;">${sessionScope.error}</div>
                      <% session.removeAttribute("error"); %>
                    </c:if>
                    <div style="display:inline-flex;gap:12px;align-items:center;">
                      <button class="btn ghost" type="button"
                        onclick="window.location.href='${pageContext.request.contextPath}/admin/manage_customer?id=${user.id}'">Hủy</button>


                      <form id="updateForm" action="${pageContext.request.contextPath}/admin/manage_customer"
                        method="post" enctype="multipart/form-data"
                        style="display:inline-flex; gap:12px; align-items:center;">
                        <input type="hidden" name="action" value="update" />
                        <input type="hidden" name="id" value="${customer.id}" />
                        <button class="btn primary" type="submit">Cập Nhật</button>
                      </form>
                    </div>
                </div>
              </div>
            </main>
        </div>
      </div>

      <!-- Hidden form for change-password (moved outside main form to avoid nesting) -->
      <form id="change-password-form" method="post" action="${pageContext.request.contextPath}/admin/manage_customer"
        style="display:none;">
        <input type="hidden" name="action" value="change_password" />
        <input type="hidden" name="id" id="change_password_id_hidden" />
        <input type="hidden" name="new_password" id="new_password_hidden" />
        <input type="hidden" name="confirm_password" id="confirm_password_hidden" />
      </form>


      <script src="${pageContext.request.contextPath}/admin/scripts/components/extendSidebar.js"></script>
        <!-- Modal thông báo thành công/lỗi -->
        <div id="notifyModal" style="display:none;position:fixed;z-index:9999;left:0;top:0;width:100vw;height:100vh;background:rgba(0,0,0,0.3);align-items:center;justify-content:center;">
          <div style="background:#fff;padding:32px 24px;border-radius:8px;min-width:320px;max-width:90vw;box-shadow:0 2px 16px #0002;position:relative;text-align:center;">
            <div id="notifyMessage" style="font-size:18px;margin-bottom:16px;"></div>
            <button id="notifyOkBtn" style="padding:8px 32px;background:#f564a9;color:#fff;border:none;border-radius:6px;font-size:16px;font-weight:500;box-shadow:0 2px 8px #f564a97a;transition:background 0.2s;">OK</button>
          </div>
        </div>
      <script src="${pageContext.request.contextPath}/admin/scripts/page/Quanlykhachhang.js"></script>
    </body>

    </html>