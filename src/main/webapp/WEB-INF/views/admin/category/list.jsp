<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Danh mục &bull; Phân trang &amp; Tìm kiếm</title>
</head>
<body>
    <div class="card-header-row">
        <div>
            <h1>Quản lý Danh mục (Category)</h1>
            <p>Tìm kiếm và phân trang danh mục bài viết / môn học bằng Spring Boot 3.</p>
        </div>
        <a href="<c:url value='/admin/categories/add'/>" class="btn btn-primary">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
            Thêm danh mục (Add category)
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
            <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" class="search-form">
                <input type="text" name="name" id="name" value="${name}" class="form-control" placeholder="Nhập từ khóa để tìm..." />
                <button type="submit" class="btn btn-secondary">Tìm kiếm (Search)</button>
                <c:if test="${not empty name}">
                    <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-secondary">Hủy lọc</a>
                </c:if>
            </form>

            <!-- Page Size Selector -->
            <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" style="display:flex; align-items:center; gap:8px; font-size:13px;">
                <input type="hidden" name="name" value="${name}" />
                <label for="size" style="color:var(--text-secondary); white-space:nowrap;">Kích thước trang (Page size):</label>
                <select name="size" id="size" class="form-control" style="width:75px; height:34px;" onchange="this.form.submit()">
                    <option ${categoryPage.size == 3 ? 'selected' : ''} value="3">3</option>
                    <option ${categoryPage.size == 5 ? 'selected' : ''} value="5">5</option>
                    <option ${categoryPage.size == 10 ? 'selected' : ''} value="10">10</option>
                    <option ${categoryPage.size == 15 ? 'selected' : ''} value="15">15</option>
                    <option ${categoryPage.size == 20 ? 'selected' : ''} value="20">20</option>
                </select>
            </form>
        </div>

        <c:choose>
            <c:when test="${!categoryPage.hasContent()}">
                <div style="text-align:center; padding:32px; color:var(--text-muted);">
                    Không có danh mục nào (No Category).
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th style="width: 60px;">STT</th>
                                <th style="width: 80px;">Hình ảnh</th>
                                <th>Category name</th>
                                <th style="width: 140px;">Status</th>
                                <th style="width: 160px; text-align: right;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${categoryPage.content}" var="cate" varStatus="STT">
                                <tr>
                                    <td class="tabular-nums">${STT.index + 1}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty cate.images}">
                                                <img src="${cate.images}" alt="${cate.name}" class="table-thumb" />
                                            </c:when>
                                            <c:otherwise>
                                                <div class="table-thumb" style="display:flex;align-items:center;justify-content:center;color:var(--text-muted);font-size:10px;">
                                                    Trống
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="font-weight: 600;">${cate.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cate.status == 1}">
                                                <span class="badge badge-active">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-inactive">Tạm ẩn</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: right;">
                                        <a href="<c:url value='/admin/categories/edit/${cate.id}'/>" class="btn btn-secondary btn-sm">Sửa</a>
                                        <a href="<c:url value='/admin/categories/delete/${cate.id}'/>" 
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục [${cate.name}] không?');">
                                            Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Pagination Bar -->
        <c:if test="${categoryPage.totalPages > 0}">
            <div class="pagination-container">
                <div>
                    Trang <strong class="tabular-nums">${categoryPage.number + 1}</strong> / <strong class="tabular-nums">${categoryPage.totalPages}</strong> 
                    (Tổng số: <span class="tabular-nums">${categoryPage.totalElements}</span> bản ghi)
                </div>
                <div class="pagination-controls">
                    <c:forEach items="${pageNumbers}" var="pageNumber">
                        <c:if test="${categoryPage.totalPages > 1}">
                            <a href="<c:url value='/admin/categories/searchpaginated?name=${name}&size=${categoryPage.size}&page=${pageNumber}'/>" 
                               class="pagination-btn ${pageNumber == categoryPage.number + 1 ? 'active' : ''}">
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
