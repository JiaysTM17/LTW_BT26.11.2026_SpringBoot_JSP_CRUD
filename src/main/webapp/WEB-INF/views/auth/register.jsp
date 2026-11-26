<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản &bull; Hệ thống quản trị</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
</head>
<body style="background-color: var(--bg-app);">
    <div class="auth-wrapper">
        <div class="auth-card" style="max-width: 480px;">
            <div style="display:flex; align-items:center; gap:8px; margin-bottom:16px;">
                <div class="brand-logo" style="width:32px; height:32px; font-size:15px;">SP</div>
                <div style="font-size:14px; font-weight:600; color:var(--text-secondary);">Hệ thống Quản trị</div>
            </div>

            <h1 class="auth-title">Tạo tài khoản mới</h1>
            <p class="auth-subtitle">Điền đầy đủ thông tin để hoàn tất đăng ký tài khoản.</p>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">${errorMessage}</div>
            </c:if>

            <form action="<c:url value='/register'/>" method="post">
                <div class="form-group">
                    <label class="form-label" for="username">Tên đăng nhập <span style="color:var(--error-text)">*</span></label>
                    <input type="text" id="username" name="username" class="form-control" value="${user.username}" placeholder="Từ 3 đến 50 ký tự" required autofocus />
                </div>

                <div class="form-group">
                    <label class="form-label" for="email">Địa chỉ Email <span style="color:var(--error-text)">*</span></label>
                    <input type="email" id="email" name="email" class="form-control" value="${user.email}" placeholder="example@domain.com" required />
                </div>

                <div class="form-group">
                    <label class="form-label" for="fullname">Họ và tên</label>
                    <input type="text" id="fullname" name="fullname" class="form-control" value="${user.fullname}" placeholder="Nguyễn Văn A" />
                </div>

                <div class="form-group">
                    <label class="form-label" for="phone">Số điện thoại</label>
                    <input type="tel" id="phone" name="phone" class="form-control" value="${user.phone}" placeholder="0901234567" />
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                    <div class="form-group">
                        <label class="form-label" for="password">Mật khẩu <span style="color:var(--error-text)">*</span></label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Tối thiểu 6 ký tự" required />
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="confirmPassword">Xác nhận mật khẩu <span style="color:var(--error-text)">*</span></label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu" required />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; height: 40px; margin-top: 8px;">
                    Đăng ký tài khoản
                </button>
            </form>

            <div style="margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--border-subtle); display: flex; justify-content: space-between; font-size: 13px;">
                <span style="color: var(--text-secondary);">Đã có tài khoản?</span>
                <a href="<c:url value='/login'/>" style="color: var(--accent-primary); font-weight: 500; text-decoration: none;">Đăng nhập</a>
            </div>
        </div>
    </div>
</body>
</html>
