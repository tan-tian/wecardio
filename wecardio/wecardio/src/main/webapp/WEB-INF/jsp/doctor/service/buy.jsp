<%--
  Created by IntelliJ IDEA.
  User: Zhang zhongtao
  Date: 2015/8/4
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.service.query.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhifu.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/buy.js"></script>
</head>
<body style="padding:20px 20px 20px 0;">
<input type="hidden" id="payTitle" value="<spring:message code="admin.service.lbl.pay"/> "/>
<input type="hidden" id="id" value="${param.id}"/>
<div class="addressBar">
    <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
    <span class="arrowText">></span>
    <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
        <spring:message code="admin.service.lbl.packMgr"/>
    </a>
    <span class="arrowText">></span>
    <span class="normal"><spring:message code="admin.service.lbl.pack.buy"/> </span>

    <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
</div>
<div class="zhifu">
    <div class="zhifu">
        <div class="zhifuTop">
            <div class="tittle">
                <spring:message code="admin.service.lbl.pack.buy"/>
            </div>
            <div class="zhifuTopMain">
                <table>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="admin.service.lbl.mountOfpayment"/> ：</td>
                        <td class="zhifuTopMain02">
                            <h1 class="moneyNumberA">
                                <b>${data.price}</b>
                                <spring:message code="admin.service.lbl.rmb"/>
                            </h1>
                        </td>
                    </tr>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="admin.service.lbl.accountBalance"/> ：</td>
                        <td class="zhifuTopMain02">
                            <h1 class="moneyNumberB">
                                <b>${wallet.accountBalance}</b>
                                <spring:message code="admin.service.lbl.rmb"/>
                            </h1>
                            <c:if test="${!wallet.isEnoughPay}">
                                <a class="chongzhi" id="btnRecharge"><spring:message code="admin.service.lbl.recharge"/></a>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3"><a class="queren" id="btnConfirmBuy"><spring:message
                                code="admin.service.lbl.confirmPay"/> </a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
