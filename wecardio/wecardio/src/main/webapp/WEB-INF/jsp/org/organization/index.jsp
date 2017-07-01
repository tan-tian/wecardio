<%--
  User: Sebarswee
  Date: 2015/6/29
  Time: 14:43
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.organization.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/hospital_style.css"/>
</head>
<body>
<div class="pageMain">

    <div class="hospitalTips">
        <div class="hospitalTipsDiv">
            <div class="hospitalTipsTable">
                <table>
                    <tr>
                        <td><spring:message code="org.organization.no_create"/></td>
                    </tr>
                </table>
            </div>
            <a class="hospitalTipsA" href="${path}/org/organization/add"><spring:message code="org.organization.apply"/></a>
        </div>
    </div>


</div>
</body>
</html>
