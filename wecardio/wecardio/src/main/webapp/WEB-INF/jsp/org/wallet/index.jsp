<%--
  User: tantian
  Date: 2015/8/10
  Time: 15:42
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.wallet.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigouzhanghuguanli.css"/>
    <script type="text/javascript" src="${path}/resources/org/wallet/js/index.js"></script>
</head>
<body>
<div  class="patient_purse">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>
    <table class="purseZhanghu">
        <tr class="purseZhanghu01">
            <td>
                <div class="yo">
                    <span class="word01">${wallet.grandTotal}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="org.wallet.grandTotal"/>
                </div>
            </td>
            <td>
                <div class="yo">
                    <span class="word01">${wallet.realTotal}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="org.wallet.realTotal"/>
                </div>
            </td>
            <td>
                <span class="word03">${wallet.withdrawTotal}</span><spring:message code="common.unit.yuan"/>
                <br />
                <spring:message code="org.wallet.withdrawTotal"/>
            </td>
            <td style="width:100px;">
                <a class="word04" name="withdrawBtn"><spring:message code="org.wallet.btn.withdraw"/></a>
            </td>
        </tr>
    </table>

    <div class="purseSearch">
        <form id="qform" action="${path}/org/wallet/unsettlement-list" method="post">
            <div class="purseSearch_a">
                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="org.wallet.query.date"/> ：</div>
                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" id="startDate" name="startDate" onclick="WdatePicker({
                            dateFmt : 'yyyy-MM',
                            readOnly : true,
                            maxDate : '#F{$dp.$D(\'endDate\',{d:0})||\'%y-{%M-1}\'}'
                        });" />
                </div>
                <span class="formText" style="margin:8px 10px 0;">~</span>
                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" id="endDate" name="endDate" onclick="WdatePicker({
                            dateFmt : 'yyyy-MM',
                            readOnly : true,
                            maxDate : '%y-{%M-1}',
                            minDate : '#F{$dp.$D(\'startDate\',{d:0})||\'1900-01-01\'}'
                        });" />
                </div>

                <div class="searchBtMain">
                    <a class="subBt queryBtn"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt resetBtn"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>
        </form>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="org.wallet.account_histoy"/></h1>
                <ul class="toolBarList" style="margin-top:5px;">
                    <li><a name="withdrawBtn"><spring:message code="org.wallet.btn.apply_withdraw"/></a></li>
                </ul>
            </div>

            <div class="userDetail" style="padding:0px;">
            </div>
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
    	{#if $T.content.length == 0}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {#/if}
</textarea>
<form id="form" action="${path}/org/withdraw/wizard" method="post" style="display: none"></form>
</body>
</html>
