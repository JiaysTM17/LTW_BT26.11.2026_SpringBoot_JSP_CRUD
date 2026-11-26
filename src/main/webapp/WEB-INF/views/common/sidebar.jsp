<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="app-sidebar">
    <div>
        <div class="nav-group-title">Quản trị hệ thống</div>
        <a href="<c:url value='/admin/categories/searchpaginated'/>" class="nav-link">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <rect x="3" y="3" width="7" height="7" rx="1"></rect>
                <rect x="14" y="3" width="7" height="7" rx="1"></rect>
                <rect x="14" y="14" width="7" height="7" rx="1"></rect>
                <rect x="3" y="14" width="7" height="7" rx="1"></rect>
            </svg>
            Quản lý Danh mục (Category)
        </a>
        <a href="<c:url value='/admin/users/searchpaginated'/>" class="nav-link">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
                <circle cx="9" cy="7" r="4"></circle>
                <path d="M22 21v-2a4 4 0 0 0-3-3.87"></path>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
            </svg>
            Quản lý Người dùng (User)
        </a>

        <div class="nav-group-title" style="margin-top: 24px;">Điều hướng</div>
        <a href="<c:url value='/home'/>" class="nav-link">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                <polyline points="9 22 9 12 15 12 15 22"></polyline>
            </svg>
            Trang chủ Portal
        </a>
    </div>

    <div>
        <div style="padding: 12px; background: var(--surface-muted); border-radius: var(--radius-sm); font-size: 11px; color: var(--text-muted);">
            <div><strong>Sinh viên:</strong> Trương Gia Kiệt</div>
            <div class="tabular-nums"><strong>MSSV:</strong> 24110032</div>
            <div><strong>Lớp:</strong> 24110CTNB</div>
        </div>
    </div>
</aside>
