<%--
  User: Sebarswee
  Date: 2015/7/23
  Time: 10:14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.record.choose.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/huizhenzhifu.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdanlist_ctr.css"/>
    <script type="text/javascript" src="${path}/resources/patient/record/js/choose.js"></script>
</head>
<body>
<div class="huizihenzhifu">
    <div class="addressBar">
        <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/patient/record"><spring:message code="patient.bread.record"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.record.choose"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>

    <div class="titleBar">
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font id="totalNum">0</font><spring:message code="common.toolbar.bill.title"/></h1>

        <ul class="toolBarList xw_hiddenCTR" style="display: none;">
            <li><a id="createBtn"><spring:message code="common.btn.pay"/></a></li>
        </ul>
    </div>

    <div class="hzzfBody xw_hzzfBody">
        <div class="hzzfBodyTab xw_hzzfBodyTab1">
        </div>
    </div>

</div>
<input type="hidden" id="rid" value="${rid}"/>
<textarea rows="0" cols="0" id="template" style="display: none">
    {#if $T == null || $T.length == 0}
    <spring:message code="patient.record.message.empty_service" arguments="${path}/patient/service/toPage/packageMgr"/>
    {#else}
        {#foreach $T as record}
        <div class="YDlistDiv xw_YDlistDiv" oid="{$T.record.oid}" serviceType="{$T.record.serviceType}" type="{$T.record.type}">
            <div class="YDlistHeadBar xw_YDlistHeadBar">
                <%--<img src="${path}/resources/images/building/building02.jpg"  width="120" height="80" style="float:left"/>--%>
                <table class="YDlistHeadBarTable">
                    <tr>
                        <th style="width: 25%;"><spring:message code="patient.record.choose.list.org"/>:<font class="textNuma">{$T.record.orgName}</font></th>
                        <td style="width: 25%;"><spring:message code="patient.record.choose.list.service"/>:<font class="textNuma">{$T.record.serviceName}</font></td>
                        {#if $T.record.type == 0}
                        <td style="width: 25%;"><spring:message code="patient.record.choose.list.count"/>:<font class="textNuma">{$T.record.count}</font><spring:message code="patient.record.choose.list.unit"/></td>
                        {#else}
                        <td style="width: 25%;"><spring:message code="patient.record.choose.list.price"/>:<font class="textNuma">{$T.record.price}</font><spring:message code="patient.record.choose.list.yuan"/></td>
                        {#/if}
                    </tr>
                </table>
                <span class="yslbSpan"></span>
            </div>
            <div class="YDlistHeadRight xw_YDlistHeadRight">
                <span class="hzzfSpan"></span>
            </div>
        </div>
        {#/for}
    {#/if}
</textarea>
</body>
</html>
