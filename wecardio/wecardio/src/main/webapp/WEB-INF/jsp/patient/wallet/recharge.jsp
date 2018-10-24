<%--
  User: tantian
  Date: 2015/8/5
  Time: 15:42
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.wallet.recharge.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhifu.css"/>
    <script type="text/javascript" src="${path}/resources/patient/wallet/js/recharge.js"></script>
</head>
<body style="padding-top: 20px; padding-right: 20px;">
<div class="addressBar">
    <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
    <span class="arrowText">&gt;</span>
    <a class="jumpback" href="${path}/patient/wallet"><spring:message code="patient.bread.wallet"/></a>
    <span class="arrowText">&gt;</span>
    <span class="normal"><spring:message code="patient.bread.wallet.recharge"/></span>
    <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
</div>
<div class="zhifu">
    <div class="zhifuTop">
        <div class="tittle"><spring:message code="patient.wallet.recharge.title"/></div>
        <div class="zhifuTopMain">
            <form id="form" action="${path}/patient/payment/submit" method="post" target="_blank">
                <input type="hidden" id="paymentPluginId" name="paymentPluginId"/>
                <input type="hidden" name="type" value="recharge"/>
                <table>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="patient.wallet.recharge.amount"/>：</td>
                        <td class="zhifuTopMain02" style="width:80%;">
                            <input type="text" id="amount" name="amount" maxlength="16" onpaste="return false;"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="patient.wallet.recharge.payment_method"/>：</td>
                        <td class="zhifuTopMain02 xw_zhifuTopMain02">
                            <c:forEach var="paymentPlugin" items="${paymentPlugins}">
                                <div class="dianji1" paymentPluginId="${paymentPlugin.id}">
                                    <img src="${path}${paymentPlugin.logo}" width="193" height="73" alt="${paymentPlugin.paymentName}" />
                                </div>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <a id="submitBtn" class="queren"><spring:message code="patient.wallet.recharge.submit"/></a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>
