<%--
  User: tantian
  Date: 2015/8/19
  Time: 9:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.register.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/index_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jihuo.css" />
</head>
<body>
<div class="headDiv"></div>
<div class="Page_jihuo">
    <div class="Page_Main">
        <c:choose>
            <c:when test="${message.type == 'success'}">
                <div class="Page_succeed xw_Main"><spring:message code="org.register.activated"/><p><a href="${path}/org/login"><spring:message code="org.register.login"/></a></p></div>
            </c:when>
            <c:otherwise>
                <div class="Page_fail xw_Main"><spring:message code="org.register.failure"/><p><c:out value="${message.content}"/></p></div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
