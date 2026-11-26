<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Người dùng &bull; Phân trang &amp; Tìm kiếm</title>
</head>
<body>
    <div class="card-header-row">
        <div>
            <h1>Quản lý Người dùng (User)</h1>
            <p>Tìm kiếm và phân trang tài khoản người dùng bằng Spring Boot 3.</p>
        </div>
        <a href="<c:url value='/admin/users/add'/>" class="btn btn-primary">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
            Thêm người dùng (Add user)
        </a>
    </div>

    <!-- Hiển thông báo -->
    <c:if test="${message != null}">
        <div class="alert alert-success">
            <i>${message}</i>
        </div>
    </c:if>

    <div class="card">
        <!-- Search Form -->
        <div class="search-toolbar">
            <form action="<c:url value='/admin/users/searchpaginated'/>" method="get" class="search-form">
                <input type="text" name="name" id="name" value="${name}" class="form-control" placeholder="Tìm theo tên đăng nhập, họ tên, email..." />
                <button type="submit" class="btn btn-secondary">Tìm kiếm (Search)</button>
                <c:if test="${not empty name}">
                    <a href="<c:url value='/admin/users/searchpaginated'/>" class="btn btn-secondary">Hủy lọc</a>
                </c:if>
            </form>

            <!-- Page Size Selector -->
            <form action="<c:url value='/admin/users/searchpaginated'/>" method="get" style="display:flex; align-items:center; gap:8px; font-size:13px;">
                <input type="hidden" name="name" value="${name}" />
                <label for="size" style="color:var(--text-secondary); white-space:nowrap;">Kích thước trang (Page size):</label>
                <select name="size" id="size" class="form-control" style="width:75px; height:34px;" onchange="this.form.submit()">
                    <option ${userPage.size == 3 ? 'selected' : ''} value="3">3</option>
                    <option ${userPage.size == 5 ? 'selected' : ''} value="5">5</option>
                    <option ${userPage.size == 10 ? 'selected' : ''} value="10">10</option>
                    <option ${userPage.size == 15 ? 'selected' : ''} value="15">15</option>
                    <option ${userPage.size == 20 ? 'selected' : ''} value="20">20</option>
                </select>
            </form>
        </div>

        <c:choose>
            <c:when test="${!userPage.hasContent()}">
                <div style="text-align:center; padding:32px; color:var(--text-muted);">
                    Không có người dùng nào (No User).
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th style="width: 60px;">STT</th>
                                <th style="width: 60px;">Avatar</th>
                                <th>Tên đăng nhập</th>
                                <th>Họ và tên</th>
                                <th>Email</th>
                                <th style="width: 120px;">Vai trò</th>
                                <th style="width: 120px;">Trạng thái</th>
                                <th style="width: 160px; text-align: right;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${userPage.content}" var="u" varStatus="STT">
                                <tr>
                                    <td class="tabular-nums">${STT.index + 1}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty u.avatar}">
                                                <img src="${u.avatar}" alt="${u.username}" class="user-avatar-sm" />
                                            </c:when>
                                            <c:otherwise>
                                                <div class="user-avatar-sm" style="display:flex;align-items:center;justify-content:center;background:#e2e8f0;font-size:11px;font-weight:600;">
                                                    ${u.username.substring(0, 1).toUpperCase()}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="font-weight: 600;">${u.username}</td>
                                    <td>${u.fullname != null && !u.fullname.isEmpty() ? u.fullname : "<span style='color:var(--text-muted)'>Chưa đặt</span>"}</td>
                                    <td style="color: var(--text-secondary);">${u.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.roleid == 2}">
                                                <span class="badge badge-role-admin">Admin</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-role-user">User</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.status == 1}">
                                                <span class="badge badge-active">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-inactive">Bị khóa</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: right;">
                                        <a href="<c:url value='/admin/users/edit/${u.id}'/>" class="btn btn-secondary btn-sm">Sửa</a>
                                        <c:choose>
                                            <c:when test="${sessionScope.currentUser.id != u.id}">
                                                <a href="<c:url value='/admin/users/delete/${u.id}'/>" 
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa tài khoản [${u.username}] không?');">
                                                    Xóa
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="btn btn-secondary btn-sm" style="opacity:0.4; cursor:not-allowed;" title="Đang đăng nhập">Đang dùng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Pagination Bar -->
        <c:if test="${userPage.totalPages > 0}">
            <div class="pagination-container">
                <div>
                    Trang <strong class="tabular-nums">${userPage.number + 1}</strong> / <strong class="tabular-nums">${userPage.totalPages}</strong> 
                    (Tổng số: <span class="tabular-nums">${userPage.totalElements}</span> người dùng)
                </div>
                <div class="pagination-controls">
                    <c:forEach items="${pageNumbers}" var="pageNumber">
                        <c:if test="${userPage.totalPages > 1}">
                            <a href="<c:url value='/admin/users/searchpaginated?name=${name}&size=${userPage.size}&page=${pageNumber}'/>" 
                               class="pagination-btn ${pageNumber == userPage.number + 1 ? 'active' : ''}">
                                ${pageNumber}
                            </a>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>
