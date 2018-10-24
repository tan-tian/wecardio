<%--
  User: tantian
  Date: 2015/7/31
  Time: 14:51
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.organization.list.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/hospital_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/organization/js/list.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.organization"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <div class="searchFormMain" style="margin-bottom:0px;">
            <form id="qform" action="${path}/patient/organization/page" method="post">
                <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                <input type="hidden" id="pageSize" name="pageSize" value="15"/>
                <div class="crsearch">
                    <input type="text" name="name" style="width:200px;" placeholder="<spring:message code="patient.organization.placeholder.search"/>" />
                    <a class="queryBtn"></a>
                </div>

                <div class="searchBtMain">
                    <a class="subBt queryBtn"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt"><spring:message code="common.btn.reset"/></a>
                </div>
            </form>
        </div>
    </div>

    <div class="titleBar">
        <a class="checkbox uncheck xw_allcheck" rel="0" style=" margin:0px; margin-right:10px; height:28px;"></a>
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font id="totalNum">0</font><spring:message code="common.toolbar.bill.title"/></h1>

        <ul class="tabToolBarUl">
        </ul>
    </div>

    <div class="dataList" id="list"></div>
    <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    <table class="hospitalTable">
        <tbody>
        <tr>
            {#foreach $T.content as record}
            <td class="doctorTd">
                <div class="hospitalDiv" oid="{$T.record.id}">
                    <h1>
                        <a class="filecheckbox checkbox"></a>
                        <img src="${basePath}/file?path={$T.record.image}" alt="" />
                        {#if $T.record.auditState == 3}
                        <i title="<spring:message code="patient.organization.list.stop.title"/>" class="stop"><em></em></i>
                        {#elseif $T.record.auditState == 2}
                        <b></b>
                        {#else}
                        <i title="<spring:message code="patient.organization.list.unauthenticated.title"/>"><em></em></i>
                        {#/if}
                    </h1>
                    <div class="hospitalDivCli xw_hospitalDivCli">
                        <h2>{$T.record.name}</h2>
                        <div class="hospitalNumber">
                            <table class="hospitalNumberTable">
                                <tr>
                                    <th>{$T.record.patientNum}</th>
                                    <th>{$T.record.doctorNum}</th>
                                    <th>{$T.record.orderNum}</th>
                                </tr>
                                <tr>
                                    <td><spring:message code="patient.organization.list.patientNum"/></td>
                                    <td><spring:message code="patient.organization.list.doctorNum"/></td>
                                    <td><spring:message code="patient.organization.list.orderNum"/></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </td>
            {#if $T.record$index != 0 && ($T.record$index + 1) % 5 == 0}
        </tr>
        <tr>
            {#/if}
            {#/for}

            {#if $T.content.length % 5 != 0}
            {#param name=remain value=(5 - ($T.content.length % 5))}
            {#for begin = 1 to $P.remain}
            <td class="doctorTd"><div class="hospitalDiv"></div></td>
            {#/for}
            {#/if}
        </tr>
        </tbody>
    </table>
    	{#if $T.content.length == 0}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {#/if}
</textarea>
</body>
</html>
