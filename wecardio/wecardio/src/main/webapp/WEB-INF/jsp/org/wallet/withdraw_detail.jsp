<%--
  User: Sebarswee
  Date: 2016/3/14
  Time: 10:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.wallet.title"/></title>
    <style type="text/css">
        .datagrid-header-row td {
            border-right: 1px dotted transparent!important;
        }
        .datagrid-body td {
            border-right: 1px dotted transparent!important;
        }
    </style>
    <script type="text/javascript" src="${path}/resources/org/wallet/js/withdraw_detail.js"></script>
</head>
<body>
<table id="list" style="width: 1150px; height: 325px; border: 0; display: none">
    <thead>
    <tr>
        <th data-options="field:'fromTradeNo', align: 'center', halign: 'center', width: 200"><spring:message code="org.wallet.withdraw_detail.title.code"/></th>
        <th data-options="field:'ticketTypeName', align: 'center', halign: 'center', width: 150"><spring:message code="org.wallet.withdraw_detail.title.ticketType"/></th>
        <th data-options="field:'verdict', align: 'center', halign: 'center', width: 250"><spring:message code="org.wallet.withdraw_detail.title.verdict"/></th>
        <th data-options="field:'money', align: 'center', halign: 'center', width: 150"><spring:message code="org.wallet.withdraw_detail.title.money"/></th>
        <th data-options="field:'rate', align: 'center', halign: 'center', width: 100"><spring:message code="org.wallet.withdraw_detail.title.rate"/></th>
        <th data-options="field:'realMoney', align: 'center', halign: 'center', width: 150, formatter: function (value, row, index) {
            if (value > 0) {
                return '<span style=\'color: #f00;\'>' + value + '</span>';
            } else if (value < 0) {
                return '<span style=\'color: #0f0;\'>' + value + '</span>';
            } else {
                return value;
            }
        }"><spring:message code="org.wallet.withdraw_detail.title.realMoney"/></th>
        <th data-options="field:'sumMoney', align: 'center', halign: 'center', width: 150"><spring:message code="org.wallet.withdraw_detail.title.sumMoney"/></th>
    </tr>
    </thead>
</table>
<div class="pageBar"></div>
<input type="hidden" id="type" value="${type}"/>
</body>
</html>
