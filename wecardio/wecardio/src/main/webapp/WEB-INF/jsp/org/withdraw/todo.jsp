<%--
  User: tantian
  Date: 2015/8/13
  Time: 17:24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.withdraw.todo.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/tixianshenhe.css"/>
    <script type="text/javascript" src="${path}/resources/org/withdraw/js/todo.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.withdraw.todo"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="qform" action="${path}/org/withdraw/todo" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="10"/>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" name="orderNo" style="width:200px;" placeholder='<spring:message code="org.withdraw.placeholder.code"/>' />
                    <a class="queryBtn"></a>
                </div>

                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="org.withdraw.query.create"/>：</div>
                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" id="startDate" name="startDate" onclick="WdatePicker({
                                readOnly : true,
                                maxDate : '#F{$dp.$D(\'endDate\',{d:0})||\'2040-10-01\'}'
                            });" />
                </div>
                <span class="formText" style="margin:8px 10px 0;">~</span>
                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" id="endDate" name="endDate" onclick="WdatePicker({
                                readOnly : true,
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

    <div class="titleBar">
        <a class="checkbox uncheck xw_allcheck" rel="0" style=" margin:0px; margin-right:10px; height:28px;"></a>
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font id="totalNum">0</font><spring:message code="common.toolbar.bill.title"/></h1>
        <ul class="toolBarList xw_hiddenCTR">
        </ul>
    </div>

    <div id="list" class="muluList"></div>
    <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    {#foreach $T.content as record}
    <div class="sgListLiMain xw_sgListLiMain" order="{$T.record.id}" guid="{$T.record.guid}">
        <a class="filecheckbox checkbox"></a>
        <div class="listStatic color02">
            <h1>{$T.record.stateName}</h1>
            <h2>[{$T.record.createDate.replace(/-/g, '.')}]</h2>
        </div>
        <div class="sgListLiMainCenter xw_sgListLiMainCenter">
            <div class="sgListHead"><h1>{$T.record.orgName}</h1><span><spring:message code="admin.withdraw.money"/>:<b>{$T.record.money}</b></span></div>
            <ul class="sgMsg">
                <li><span><spring:message code="org.withdraw.create_name"/>:</span><a>{$T.record.createName}</a></li>
                <li><span><spring:message code="org.withdraw.create"/>:</span><a>{$T.record.createDate}</a></li>
                <li><span><spring:message code="org.withdraw.code"/>:</span><a>{$T.record.orderNo}</a></li>
            </ul>
        </div>
    </div>
    {#/for}
    	{#if $T.content.length == 0}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {#/if}
</textarea>
</body>
</html>
