<%--
  User: Sebarswee
  Date: 2015/8/15
  Time: 13:18
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.wallet.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigouzhanghuguanli.css"/>
    <script type="text/javascript" src="${path}/resources/admin/wallet/js/index.js"></script>
</head>
<body>
<div  class="patient_purse">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>

    <table class="purseZhanghu">
        <tr class="purseZhanghu01">
            <td>
                <div class="yo">
                    <span class="word01">${wallet.saleAmount}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="admin.wallet.sale_amount"/>
                </div>
            </td>
            <td>
                <div class="yo">
                    <span class="word03">${wallet.rateAmount}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="admin.wallet.amount"/>
                </div>
            </td>
            <td>
                <div class="yo">
                    <span class="word01">${wallet.payAmount}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="admin.wallet.pay_amount"/>
                </div>
            </td>
            <td>
                <div class="yo">
                    <span class="word01">${wallet.alreadyPayAmount}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="admin.wallet.already_pay_amount"/>
                </div>
            </td>
            <td>
                <div class="yo">
                    <span class="word01">${wallet.notPayAmount}</span><spring:message code="common.unit.yuan"/>
                    <br />
                    <spring:message code="admin.wallet.not_pay_amount"/>
                </div>
            </td>
        </tr>
    </table>
    <div class="purseSearch">
        <form id="qform" action="${path}/admin/wallet/list" method="post">
            <div class="purseSearch_a">
                <div class="crsearch">
                    <input type="text" name="orgName" style="width:200px;" placeholder='<spring:message code="admin.wallet.placeholder.org_name"/>'/>
                    <a class="queryBtn"></a>
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
                <h1 class="userDetailConthText"><spring:message code="admin.wallet.account_histoy"/></h1>
                <ul class="toolBarList" style="margin-top:5px;">
                </ul>
            </div>
            <div class="userDetail" style="padding:0px;">
            </div>
        </div>
    </div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    {#if $T.length > 0}
    <table class="datalistTable">
        <thead>
        <tr>
            <th class="checkTh"><input id="checkAll" type="checkbox" /></th>
            <th><spring:message code="admin.wallet.title.org_name"/></th>
            <th><spring:message code="admin.wallet.title.sale_amount"/></th>
            <th><spring:message code="admin.wallet.title.pay_amount"/></th>
            <th><spring:message code="admin.wallet.title.unpay_amount"/></th>
        </tr>
        </thead>
        <tbody>
        {#foreach $T as record}
        <tr>
            <td>
                <input type="checkbox" name="checkbox"/>
            </td>
            <td>{$T.record.orgName}</td>
            <td>{$T.record.saleAmount}</td>
            <td>{$T.record.payAmount}</td>
            <td>{$T.record.unPayAmount}</td>
        </tr>
        {#/for}
        </tbody>
    </table>
    {#/if}
    {#if $T.length == 0}
        <div class="jiachajilu">
            <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
        </div>
        {#/if}
</textarea>
</body>
</html>
