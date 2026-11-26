<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ &bull; Cổng thông tin người dùng</title>
</head>
<body>
    <div style="max-width: 1040px; margin: 0 auto; padding-top: 24px;">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="card" style="display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h1 style="font-size:22px; margin-bottom:4px;">Cổng thông tin môn học Lập trình Web</h1>
                <p>Khoa Công nghệ Thông tin &bull; Trường Đại học Sư phạm Kỹ thuật TP.HCM (HCMUTE)</p>
            </div>
            <c:if test="${not empty currentUser and currentUser.roleid == 2}">
                <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-primary">
                    Truy cập Trang Quản trị (Admin)
                </a>
            </c:if>
        </div>

        <c:if test="${not empty currentUser}">
            <div class="card">
                <h2 style="font-size:16px; margin-bottom:16px; padding-bottom:8px; border-bottom:1px solid var(--border-subtle);">
                    Thông tin tài khoản đăng nhập
                </h2>
                <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px; font-size:13px;">
                    <div>
                        <div style="color:var(--text-muted); margin-bottom:2px;">Tên đăng nhập:</div>
                        <div style="font-weight:600;">${currentUser.username}</div>
                    </div>
                    <div>
                        <div style="color:var(--text-muted); margin-bottom:2px;">Họ và tên:</div>
                        <div style="font-weight:600;">${currentUser.fullname != null ? currentUser.fullname : "(Chưa cập nhật)"}</div>
                    </div>
                    <div>
                        <div style="color:var(--text-muted); margin-bottom:2px;">Email:</div>
                        <div style="font-weight:600;">${currentUser.email}</div>
                    </div>
                    <div>
                        <div style="color:var(--text-muted); margin-bottom:2px;">Vai trò hệ thống:</div>
                        <div>
                            <c:choose>
                                <c:when test="${currentUser.roleid == 2}">
                                    <span class="badge badge-role-admin">Quản trị viên (Admin)</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-role-user">Người dùng (User)</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="card">
            <div class="card-header-row">
                <div>
                    <h2>Danh mục môn học &amp; chuyên đề</h2>
                    <p>Các chuyên mục đang hoạt động trong hệ thống</p>
                </div>
                <div class="mono-badge">Tổng: ${categories.size()} danh mục</div>
            </div>

            <div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap:16px;">
                <c:forEach items="${categories}" var="cat">
                    <div style="border: 1px solid var(--border-subtle); border-radius: var(--radius-md); overflow:hidden; background:var(--surface);">
                        <c:choose>
                            <c:when test="${not empty cat.images}">
                                <img src="${cat.images}" alt="${cat.categoryname}" style="width:100%; height:130px; object-fit:cover;" />
                            </c:when>
                            <c:otherwise>
                                <div style="width:100%; height:130px; background:var(--surface-muted); display:flex; align-items:center; justify-content:center; color:var(--text-muted); font-size:13px;">
                                    Không có ảnh
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <div style="padding:14px;">
                            <div style="font-weight:600; font-size:14px; margin-bottom:6px; color:var(--text-primary);">${cat.categoryname}</div>
                            <div style="display:flex; justify-content:space-between; align-items:center; font-size:12px;">
                                <span class="tabular-nums" style="color:var(--text-muted);">Mã: #${cat.categoryid}</span>
                                <span class="badge badge-active">Hoạt động</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>
