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
    <script type="text/javascript" src="${path}/resources/admin/wallet/js/dcRecharge.js"></script>
</head>
<body style="padding:20px 20px 20px 0;">
<input type="hidden" id="payTitle" value="<spring:message code="admin.service.lbl.pay"/> "/>
<input type="hidden" id="id" value="${param.id}"/>
<div class="addressBar">
    <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
    <span class="arrowText">></span>
    <a class="jumpback" href="${path}/${sessionScope.userTypePath}/patient?iType=2">
        <spring:message code="common.btn.patient.daichongBtn"/>
    </a>
    <span class="arrowText">></span>
    <span class="normal"><spring:message code="admin.service.lbl.pack.buy"/> </span>

    <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
</div>
<div class="zhifu">
    <div class="zhifuTop">
        <div class="tittle"><spring:message code="patient.wallet.recharge.title"/></div>
        <div class="zhifuTopMain">
            <form id="form" action="${path}/${sessionScope.userTypePath}/patient/payment/submit" method="post" target="_blank">
                <input type="hidden" id="paymentPluginId" name="paymentPluginId"/>
                <input type="hidden" name="type" value="recharge"/>
                <input type="hidden" name="pId" value="${pId}"/>
                <input type="hidden" name="sid" value="${sid}"/>
                <input type="hidden" id="amount" name="amount" value="${price}"/>
                <table>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="doctor.daichong.pay"/>：</td>
                        <td class="zhifuTopMain02" style="width:80%;">
                           ${price}
                        </td>
                    </tr>
                    <tr>
                        <td class="zhifuTopMain01"><spring:message code="doctor.daichong.payMethod"/>：</td>
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
