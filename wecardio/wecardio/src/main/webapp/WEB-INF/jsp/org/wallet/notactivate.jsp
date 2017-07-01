<%--
  User: Sebarswee
  Date: 2015/8/3
  Time: 10:13
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.wallet.noactivate.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhanghujihuo.css"/>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <div class="hospitalTips">
        <div class="hospitalTipsDiv">
            <div class="hospitalTipsTable">
                <table>
                    <tr>
                        <td><spring:message code="org.wallet.no_activate"/></td>
                    </tr>
                </table>
            </div>
            <a class="zhanghuTipsA" href="${path}/org/wallet/activate"><spring:message code="org.wallet.activate"/></a>
        </div>
    </div>

</div>
</body>
</html>
