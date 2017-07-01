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
<body>
<div class="">
    <form id="form" action="${path}/doctor/patient/lead3bind/submit" method="post">
        <table class="userMsgTableb" style="margin:40px -20px;">
        	<input type="hidden" name="pId" value="${pId}"/>
        	<tr>
                <th colspan="2"><spring:message code="doctor.view.passCode"/>:</th>
                <td>
                    <input type="text" id="code" name="code" />
                </td>
            </tr>
         </table>
    </form>
</div>

<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float: none">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
