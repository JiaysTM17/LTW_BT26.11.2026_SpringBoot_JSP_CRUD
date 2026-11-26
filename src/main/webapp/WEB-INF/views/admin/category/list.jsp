<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activeMenu" value="category" scope="request"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Danh mục &bull; Bảng điều khiển Quản trị</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
</head>
<body>
    <jsp:include page="../../common/header.jsp" />

    <div class="app-container">
        <jsp:include page="../../common/sidebar.jsp" />

        <main class="app-content">
            <div class="card-header-row">
                <div>
                    <h1>Quản lý Danh mục (Category)</h1>
                    <p>Danh sách các danh mục đào tạo và chủ đề trong hệ thống.</p>
                </div>
                <a href="<c:url value='/admin/categories/create'/>" class="btn btn-primary">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <line x1="12" y1="5" x2="12" y2="19"></line>
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                    Thêm danh mục
                </a>
            </div>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">${errorMessage}</div>
            </c:if>

            <div class="card">
                <!-- Search Toolbar -->
                <div class="search-toolbar">
                    <form action="<c:url value='/admin/categories'/>" method="get" class="search-form">
                        <input type="text" name="keyword" value="${keyword}" class="form-control" placeholder="Tìm kiếm theo tên danh mục..." />
                        <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
                        <c:if test="${not empty keyword}">
                            <a href="<c:url value='/admin/categories'/>" class="btn btn-secondary" title="Xóa bộ lọc">Hủy lọc</a>
                        </c:if>
                    </form>
                    <div class="tabular-nums" style="font-size: 13px; color: var(--text-secondary);">
                        Tìm thấy <strong>${totalElements}</strong> bản ghi
                    </div>
                </div>

                <!-- Table -->
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;" class="tabular-nums">Mã ID</th>
                                <th style="width: 80px;">Hình ảnh</th>
                                <th>Tên danh mục</th>
                                <th style="width: 140px;">Trạng thái</th>
                                <th style="width: 150px; text-align: right;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty categoryPage.content}">
                                    <c:forEach items="${categoryPage.content}" var="item">
                                        <tr>
                                            <td class="tabular-nums" style="font-weight: 500; color: var(--text-muted);">
                                                #${item.categoryid}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty item.images}">
                                                        <img src="${item.images}" alt="${item.categoryname}" class="table-thumb" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-thumb" style="display:flex;align-items:center;justify-content:center;color:var(--text-muted);font-size:10px;">
                                                            Trống
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="font-weight: 600; color: var(--text-primary);">
                                                ${item.categoryname}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.status == 1}">
                                                        <span class="badge badge-active">Hoạt động</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-inactive">Tạm ẩn</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align: right;">
                                                <a href="<c:url value='/admin/categories/edit/${item.categoryid}'/>" class="btn btn-secondary btn-sm">Sửa</a>
                                                <a href="<c:url value='/admin/categories/delete/${item.categoryid}'/>" 
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục [${item.categoryname}] không?');">
                                                   Xóa
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="5" style="text-align: center; padding: 32px; color: var(--text-muted);">
                                            Không có danh mục nào phù hợp với yêu cầu tìm kiếm.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <div>
                            Trang <strong class="tabular-nums">${currentPage + 1}</strong> trên <strong class="tabular-nums">${totalPages}</strong>
                        </div>
                        <div class="pagination-controls">
                            <!-- Nút Trước -->
                            <c:choose>
                                <c:when test="${currentPage > 0}">
                                    <a href="<c:url value='/admin/categories?page=${currentPage - 1}&keyword=${keyword}'/>" class="pagination-btn">Trước</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="pagination-btn disabled">Trước</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Các số trang -->
                            <c:forEach begin="0" end="${totalPages - 1}" var="p">
                                <a href="<c:url value='/admin/categories?page=${p}&keyword=${keyword}'/>" 
                                   class="pagination-btn ${p == currentPage ? 'active' : ''}">
                                    ${p + 1}
                                </a>
                            </c:forEach>

                            <!-- Nút Sau -->
                            <c:choose>
                                <c:when test="${currentPage + 1 < totalPages}">
                                    <a href="<c:url value='/admin/categories?page=${currentPage + 1}&keyword=${keyword}'/>" class="pagination-btn">Sau</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="pagination-btn disabled">Sau</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </div>
        </main>
    </div>

    <jsp:include page="../../common/footer.jsp" />
</body>
</html>
