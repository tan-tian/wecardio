<%--
  User: tantian
  Date: 2015/8/7
  Time: 11:44
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.payment.notify"/></title>
    <style type="text/css">
   		div {
		position: absolute;
 		left: 40%;
	 	top: 40%;
		width:300px;
		height:300px;
		}
	</style>
</head>
<body>
<div class="span24" style= "text-align:center; font-size:18px;">
        
            <c:choose>
                <c:when test="${payment.success == 0}">
                    <spring:message code="patient.payment.waitTitle"/>
                </c:when>
                <c:when test="${payment.success == 1}">
                    <c:choose>
                        <c:when test="${payment.type == 'recharge'}">
                            <spring:message code="patient.payment.depositTitle"/>
                        </c:when>
                    </c:choose>
                </c:when>
                <c:when test="${payment.success == 2}">
                    <spring:message code="patient.payment.failureTitle"/>
                </c:when>
            </c:choose>
        
       		<br/>
            <%--<c:choose>
                <c:when test="${payment.type == 'recharge'}">
                    <a href=""><spring:message code="patient.payment.deposit"/></a>
                </c:when>
            </c:choose>
            | --%>
            <a href="${path}/patient"><spring:message code="patient.main.homeNav"/></a>
</div>
</body>
</html>
