<%--
  User: tantian
  Date: 2015/8/17
  Time: 15:02
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.withdraw.choose.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigouzhanghuguanli.css"/>
    <script type="text/javascript" src="${path}/resources/org/withdraw/js/select.js"></script>
</head>
<body>
<div class="userMain" style="margin-bottom:10px;">
    <div class="userDetailCont" style="padding-top:0px;">
        <div class="userDetailContHead">
            <h1 class="userDetailConthText"><spring:message code="org.wallet.account_histoy"/></h1>
            <ul class="toolBarList" style="margin-top:5px;">
                <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
            </ul>
        </div>

        <div class="userDetail" style="padding:0px;">
        </div>
    </div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    <table class="datalistTable">
        <thead>
        <tr>
            <th class="checkTh"><input id="checkAll" type="checkbox" /></th>
            <th><spring:message code="org.wallet.title.year_month"/></th>
            <th><spring:message code="org.wallet.title.money"/></th>
        </tr>
        </thead>
        <tbody>
        {#foreach $T as record}
        <tr>
            <td>
                <input type="hidden" name="year" value="{$T.record.year}"/>
                <input type="hidden" name="month" value="{$T.record.month}"/>
                <input type="hidden" name="money" value="{$T.record.money}"/>
                <input type="checkbox" name="checkbox"/>
            </td>
            <td>{$T.record.year}.{$T.record.month}</td>
            <td>{$T.record.money}</td>
        </tr>
        {#/for}
        </tbody>
    </table>
</textarea>
<script type="text/javascript">
    var requiredMsg = '<spring:message code="org.withdraw.message.choose"/>';
</script>
<form id="form" action="${path}/org/withdraw/settlement/add" method="post" style="display: none">
    <input type="hidden" id="orderId" name="id" value="${id}"/>
</form>
</body>
</html>
