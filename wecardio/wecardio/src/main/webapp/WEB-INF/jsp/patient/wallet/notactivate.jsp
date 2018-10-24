<%--
  User: tantian
  Date: 2015/8/3
  Time: 10:13
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.wallet.noactivate.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhanghujihuo.css"/>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>

    <div class="hospitalTips">
        <div class="hospitalTipsDiv">
            <div class="hospitalTipsTable">
                <table>
                    <tr>
                        <td><spring:message code="patient.wallet.no_activate"/></td>
                    </tr>
                </table>
            </div>
            <a class="zhanghuTipsA" href="${path}/patient/wallet/activate"><spring:message code="patient.wallet.activate"/></a>
        </div>
    </div>

</div>
</body>
</html>
