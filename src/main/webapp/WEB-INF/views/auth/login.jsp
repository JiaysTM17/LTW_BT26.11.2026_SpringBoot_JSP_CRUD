<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập &bull; Hệ thống quản trị</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
</head>
<body style="background-color: var(--bg-app);">
    <div class="auth-wrapper">
        <div class="auth-card">
            <div style="display:flex; align-items:center; gap:8px; margin-bottom:16px;">
                <div class="brand-logo" style="width:32px; height:32px; font-size:15px;">SP</div>
                <div style="font-size:14px; font-weight:600; color:var(--text-secondary);">Hệ thống Quản trị</div>
            </div>

            <h1 class="auth-title">Đăng nhập tài khoản</h1>
            <p class="auth-subtitle">Nhập thông tin xác thực để truy cập bảng điều khiển.</p>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <form action="<c:url value='/login'/>" method="post">
                <div class="form-group">
                    <label class="form-label" for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="Nhập tên đăng nhập" required autofocus />
                </div>

                <div class="form-group" style="margin-bottom: 24px;">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required />
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; height: 40px;">
                    Đăng nhập
                </button>
            </form>

            <div style="margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--border-subtle); display: flex; justify-content: space-between; font-size: 13px;">
                <span style="color: var(--text-secondary);">Chưa có tài khoản?</span>
                <a href="<c:url value='/register'/>" style="color: var(--accent-primary); font-weight: 500; text-decoration: none;">Đăng ký ngay</a>
            </div>

            <div style="margin-top: 16px; padding: 12px; background: var(--surface-muted); border-radius: var(--radius-sm); font-size: 12px; color: var(--text-secondary);">
                <div><strong>Tài khoản mẫu:</strong></div>
                <div>Quản trị: <span class="mono-badge">admin</span> / <span class="mono-badge">123456</span></div>
                <div>Sinh viên: <span class="mono-badge">kiet24110032</span> / <span class="mono-badge">123456</span></div>
            </div>
        </div>
    </div>
</body>
</html>
