<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="app-header">
    <a href="<c:url value='/home'/>" class="brand-wrapper">
        <div class="brand-logo">SP</div>
        <div>
            <div class="brand-title">Spring Boot Admin Portal</div>
        </div>
    </a>
    <div class="header-user-meta">
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <div class="user-pill">
                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser.avatar}">
                            <img src="${sessionScope.currentUser.avatar}" alt="Avatar" class="user-avatar-sm" />
                        </c:when>
                        <c:otherwise>
                            <div class="user-avatar-sm" style="display:flex;align-items:center;justify-content:center;background:#e2e8f0;font-size:12px;font-weight:600;">
                                ${sessionScope.currentUser.username.substring(0, 1).toUpperCase()}
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <span>${sessionScope.currentUser.fullname != null ? sessionScope.currentUser.fullname : sessionScope.currentUser.username}</span>
                    <c:if test="${sessionScope.currentUser.roleid == 2}">
                        <span class="badge badge-role-admin">Admin</span>
                    </c:if>
                </div>
                <a href="<c:url value='/logout'/>" class="btn btn-secondary btn-sm">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value='/login'/>" class="btn btn-secondary btn-sm">Đăng nhập</a>
                <a href="<c:url value='/register'/>" class="btn btn-primary btn-sm">Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>
