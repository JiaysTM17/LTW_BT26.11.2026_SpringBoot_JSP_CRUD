<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activeMenu" value="user" scope="request"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} Người dùng &bull; Quản trị</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
</head>
<body>
    <jsp:include page="../../common/header.jsp" />

    <div class="app-container">
        <jsp:include page="../../common/sidebar.jsp" />

        <main class="app-content">
            <div class="card-header-row">
                <div>
                    <h1>${isEdit ? 'Chỉnh sửa Người dùng' : 'Thêm mới Người dùng'}</h1>
                    <p>Điền thông tin tài khoản người dùng và thiết lập phân quyền.</p>
                </div>
                <a href="<c:url value='/admin/users'/>" class="btn btn-secondary">
                    Quay lại danh sách
                </a>
            </div>

            <div class="card" style="max-width: 680px;">
                <form action="<c:url value='${isEdit ? \"/admin/users/update/\" : \"/admin/users/save\"}${isEdit ? user.id : \"\"}'/>" method="post">
                    
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px;">
                        <div class="form-group">
                            <label class="form-label" for="username">
                                Tên đăng nhập <span style="color:var(--error-text)">*</span>
                            </label>
                            <input type="text" id="username" name="username" 
                                   value="${user.username}" 
                                   class="form-control" 
                                   placeholder="Từ 3 đến 50 ký tự" 
                                   ${isEdit ? 'readonly style=\"background:var(--surface-muted);cursor:not-allowed;\"' : 'required autofocus'} />
                            <c:if test="${isEdit}">
                                <div class="form-help">Tên đăng nhập không thể thay đổi sau khi tạo.</div>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="password">
                                Mật khẩu <c:if test="${!isEdit}"><span style="color:var(--error-text)">*</span></c:if>
                            </label>
                            <input type="password" id="password" name="password" 
                                   class="form-control" 
                                   placeholder="${isEdit ? 'Để trống nếu giữ nguyên' : 'Nhập mật khẩu'}" 
                                   ${!isEdit ? 'required' : ''} />
                            <c:if test="${isEdit}">
                                <div class="form-help">Chỉ nhập nếu bạn muốn đặt lại mật khẩu mới.</div>
                            </c:if>
                        </div>
                    </div>

                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px;">
                        <div class="form-group">
                            <label class="form-label" for="email">
                                Địa chỉ Email <span style="color:var(--error-text)">*</span>
                            </label>
                            <input type="email" id="email" name="email" 
                                   value="${user.email}" 
                                   class="form-control" 
                                   placeholder="user@example.com" 
                                   required />
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="fullname">Họ và tên</label>
                            <input type="text" id="fullname" name="fullname" 
                                   value="${user.fullname}" 
                                   class="form-control" 
                                   placeholder="Nguyễn Văn A" />
                        </div>
                    </div>

                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px;">
                        <div class="form-group">
                            <label class="form-label" for="phone">Số điện thoại</label>
                            <input type="tel" id="phone" name="phone" 
                                   value="${user.phone}" 
                                   class="form-control" 
                                   placeholder="0901234567" />
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="avatar">URL Ảnh đại diện</label>
                            <input type="text" id="avatar" name="avatar" 
                                   value="${user.avatar}" 
                                   class="form-control" 
                                   placeholder="https://..." />
                        </div>
                    </div>

                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px; margin-bottom: 24px;">
                        <div class="form-group">
                            <label class="form-label" for="roleid">Vai trò phân quyền</label>
                            <select id="roleid" name="roleid" class="form-control">
                                <option value="1" ${user.roleid == null || user.roleid == 1 ? 'selected' : ''}>Người dùng (User)</option>
                                <option value="2" ${user.roleid == 2 ? 'selected' : ''}>Quản trị viên (Admin)</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="status">Trạng thái tài khoản</label>
                            <select id="status" name="status" class="form-control">
                                <option value="1" ${user.status == null || user.status == 1 ? 'selected' : ''}>Hoạt động</option>
                                <option value="0" ${user.status == 0 ? 'selected' : ''}>Bị khóa</option>
                            </select>
                        </div>
                    </div>

                    <div style="display:flex; gap:12px; padding-top:16px; border-top:1px solid var(--border-subtle);">
                        <button type="submit" class="btn btn-primary">
                            ${isEdit ? 'Lưu thay đổi' : 'Tạo mới người dùng'}
                        </button>
                        <a href="<c:url value='/admin/users'/>" class="btn btn-secondary">
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
