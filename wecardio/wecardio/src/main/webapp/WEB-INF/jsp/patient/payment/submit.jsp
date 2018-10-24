<%--
  User: tantian
  Date: 2015/8/6
  Time: 20:35
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.payment.submit"/></title>
</head>
<body onload="javascript: document.forms[0].submit();">
<form action="${requestUrl}" method="${requestMethod}" accept-charset="${requestCharset}">
    <c:forEach var="entry" items="${parameterMap}">
        <input type="hidden" name="${entry.key}" value="${entry.value}" />
    </c:forEach>
</form>
</body>
</html>
