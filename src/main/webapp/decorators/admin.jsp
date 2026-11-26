<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>" />
    <sitemesh:write property="head"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
        <main class="app-content">
            <sitemesh:write property="body"/>
        </main>
    </div>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
