<%--
  User: tantian
  Date: 2015/8/4
  Time: 20:15
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.wallet.activate.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhanghujihuo.css"/>
    <script type="text/javascript">
        var modulus = '${modulus}';
        var exponent = '${exponent}';
    </script>
    <script type="text/javascript" src="${path}/resources/patient/wallet/js/activate.js"></script>
</head>
<body style=" padding:20px 20px 20px 0;">
<div class="addressBar">
    <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
    <span class="arrowText">&gt;</span>
    <a class="jumpback" href="${path}/patient/wallet"><spring:message code="patient.bread.wallet"/></a>
    <span class="arrowText">&gt;</span>
    <span class="normal"><spring:message code="patient.bread.wallet.activate"/></span>
    <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
</div>
<div class="zhifu">
    <div class="zhifuTop">
        <div class="tittle"><spring:message code="patient.wallet.activate.form.title"/></div>
        <div class="zhifuTopMain">
            <form id="form" action="${path}/patient/wallet/activate/submit" method="post">
                <input type="hidden" id="enPassword" name="enPassword"/>
                <table>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="patient.wallet.activate.form.password"/>：</td>
                        <td class="zhifuTopMain02"><input type="password" id="password" name="password" autocomplete="off"/></td>
                    </tr>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="patient.wallet.activate.form.rePassword"/>：</td>
                        <td class="zhifuTopMain02"><input type="password" name="rePassword" autocomplete="off"/></td>
                    </tr>
                </table>
            </form>
            <ul>
                <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
                <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
