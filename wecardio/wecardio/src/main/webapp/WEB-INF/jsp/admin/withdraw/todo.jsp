<%--
  User: Sebarswee
  Date: 2015/8/13
  Time: 17:24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.withdraw.todo.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/tixianshenhe.css"/>
    <script type="text/javascript" src="${path}/resources/admin/withdraw/js/todo.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.withdraw.todo"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="qform" action="${path}/admin/withdraw/todo" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="10"/>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" name="orgName" style="width:200px;" placeholder='<spring:message code="admin.withdraw.placeholder.name"/>' />
                    <a class="queryBtn"></a>
                </div>

                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="admin.withdraw.query.create"/>：</div>
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

            <div class="khKmainbody xw_kMainContentbody" style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">
                <div class="searchFormChose">
                    <dl>
                        <dt style="width: 100px;"><spring:message code="admin.withdraw.query.state"/>：</dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="state" name="state"/>
                        </dd>
                    </dl>
                </div>
                <div class="searchBar" style=" position:relative;z-index:3;">
                    <span class="spanName" style="width: 109px;"><spring:message code="admin.withdraw.query.code"/>：</span>
                    <input type="text" class="normalInput" id="orderNo" name="orderNo"/>
                </div>
                <div class="searchFormBtMain">
                    <ul class="toolBarList" style="float:none; margin:0 auto; width:150px; overflow:hidden;">
                        <li><a class="resetBtn"><spring:message code="common.btn.reset"/></a></li>
                        <li><a class="queryBtn"><spring:message code="common.btn.query"/></a></li>
                    </ul>
                </div>
            </div>
            <div class="searchFormFoot">
                <a class="moresearchForm">
                    <span class="acc-openbar-txt"><spring:message code="common.btn.slidedown"/></span>
                    <span class="acc-openbar-txtb"><spring:message code="common.btn.slideup"/></span><spring:message code="common.btn.more"/>
                </a>
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
                <li><span><spring:message code="admin.withdraw.create_name"/>:</span><a>{$T.record.createName}</a></li>
                <li><span><spring:message code="admin.withdraw.create"/>:</span><a>{$T.record.createDate}</a></li>
                <li><span><spring:message code="admin.withdraw.code"/>:</span><a>{$T.record.orderNo}</a></li>
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
<script type="text/javascript">
    var state = [{
        "value" : "1", "text" : '<spring:message code="common.enum.withdraw.state.audit"/>'
    }, {
        "value" : "2", "text" : '<spring:message code="common.enum.withdraw.state.confirm"/>'
    }];
</script>
</body>
</html>
