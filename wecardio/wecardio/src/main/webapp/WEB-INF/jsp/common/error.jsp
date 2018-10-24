<%--
  User: tantian
  Date: 2015/6/29
  Time: 10:22
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/jstlLib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><spring:message code="common.error.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="author" content="hiteam" />
    <meta name="copyright" content="hiteam" />
</head>
<body>
<div>
    <div>
        <div>
            <dl>
                <dt><spring:message code="common.error.message"/></dt>
                <c:if test="${!empty message}">
                    <dd>${message.content}</dd>
                </c:if>
                <dd>
                    <a href="javascript:;" onclick="window.history.back(); return false;"><spring:message code="common.error.back"/></a>
                </dd>
            </dl>
        </div>
    </div>
</div>
</body>
</html>
