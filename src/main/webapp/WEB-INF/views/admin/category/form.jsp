<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activeMenu" value="category" scope="request"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} Danh mục &bull; Quản trị</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
</head>
<body>
    <jsp:include page="../../common/header.jsp" />

    <div class="app-container">
        <jsp:include page="../../common/sidebar.jsp" />

        <main class="app-content">
            <div class="card-header-row">
                <div>
                    <h1>${isEdit ? 'Chỉnh sửa Danh mục' : 'Thêm mới Danh mục'}</h1>
                    <p>Nhập thông tin danh mục đào tạo vào các trường bên dưới.</p>
                </div>
                <a href="<c:url value='/admin/categories'/>" class="btn btn-secondary">
                    Quay lại danh sách
                </a>
            </div>

            <div class="card" style="max-width: 640px;">
                <form action="<c:url value='${isEdit ? \"/admin/categories/update/\" : \"/admin/categories/save\"}${isEdit ? category.categoryid : \"\"}'/>" method="post">
                    <div class="form-group">
                        <label class="form-label" for="categoryname">
                            Tên danh mục <span style="color:var(--error-text)">*</span>
                        </label>
                        <input type="text" id="categoryname" name="categoryname" 
                               value="${category.categoryname}" 
                               class="form-control" 
                               placeholder="Ví dụ: Lập trình Java Web Spring Boot" 
                               required autofocus />
                        <div class="form-help">Độ dài từ 2 đến 100 ký tự.</div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="images">
                            Đường dẫn hình ảnh (URL ảnh đại diện)
                        </label>
                        <input type="text" id="images" name="images" 
                               value="${category.images}" 
                               class="form-control" 
                               placeholder="https://images.unsplash.com/photo-..." />
                        <div class="form-help">Có thể để trống hoặc dán URL hình ảnh từ internet.</div>
                    </div>

                    <div class="form-group" style="margin-bottom: 24px;">
                        <label class="form-label">Trạng thái hoạt động</label>
                        <div style="display:flex; gap:20px; align-items:center; margin-top:8px;">
                            <label style="display:flex; align-items:center; gap:6px; cursor:pointer; font-size:13px;">
                                <input type="radio" name="status" value="1" ${category.status == null || category.status == 1 ? 'checked' : ''} />
                                <span>Hoạt động</span>
                            </label>
                            <label style="display:flex; align-items:center; gap:6px; cursor:pointer; font-size:13px;">
                                <input type="radio" name="status" value="0" ${category.status == 0 ? 'checked' : ''} />
                                <span>Tạm ẩn</span>
                            </label>
                        </div>
                    </div>

                    <div style="display:flex; gap:12px; padding-top:16px; border-top:1px solid var(--border-subtle);">
                        <button type="submit" class="btn btn-primary">
                            ${isEdit ? 'Lưu thay đổi' : 'Tạo mới danh mục'}
                        </button>
                        <a href="<c:url value='/admin/categories'/>" class="btn btn-secondary">
                            Hủy bỏ
                        </a>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <jsp:include page="../../common/footer.jsp" />
</body>
</html>
