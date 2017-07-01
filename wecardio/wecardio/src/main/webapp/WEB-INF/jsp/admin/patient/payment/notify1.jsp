<%--
  User: Sebarswee
  Date: 2015/9/2
  Time: 11:12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="common.password.reset.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/patient/js/lead3bind.js"></script>
</head>
<body >
<div class=""  style= "text-align:center; font-size:18px;">
		<table class="userMsgTableb" style="margin:40px -20px;">
        	<input type="hidden" name="pId" value="${pId}"/>
        	<tr>
                <th colspan="2"></th>
                <td>
                 <c:choose>
                <c:when test="${payment.success == 0}">
                    &nbsp
                </c:when>
                <c:when test="${payment.success == 1}">
                     &nbsp
                </c:when>
                <c:when test="${payment.success == 2}">
                    &nbsp
                </c:when>
           		 </c:choose>
                </td>
            </tr>
         </table>
		<br/>
       <table>
       
        <tr>
        <c:choose>
                <c:when test="${payment.success == 0}">
                    <spring:message code="patient.payment.waitTitle"/>
                </c:when>
                <c:when test="${payment.success == 1}">
                      <spring:message code="common.daichong.purchaseSuccess"/>
                </c:when>
                <c:when test="${payment.success == 2}">
                    <spring:message code="patient.payment.failureTitle"/>
                </c:when>
            </c:choose>
        </tr>
        <br/>
       </table>
	</div>
</body>
</html>
