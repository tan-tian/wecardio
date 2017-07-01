<%--
  User: Sebarswee
  Date: 2015/8/3
  Time: 9:48
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.wallet.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/purse.css"/>
    <script type="text/javascript" src="${path}/resources/patient/wallet/js/index.js"></script>
</head>
<body>
<div class="patient_purse">
    <div class="addressBar">
        <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>
    <div class="purseXinxi">
        <div class="purseZhanghu xw_purseZhanghu">
            <table>
                <tr>
                    <td>
                        <div class="qianbaoDiv hongse xw_hongse" action="${path}/patient/wallet/service/page">
                            <div class="fw"><spring:message code="patient.wallet.history.service"/></div>
                        </div>
                    </td>
                    <td>
                        <div class="qianbaoDiv lanse xw_lanse" action="${path}/patient/wallet/history/page">
                            <div class="xf"><spring:message code="patient.wallet.history.consume"/></div>
                        </div>
                    </td>
                    <td>
                        <div class="qianbaoDiv ">
                            <div class="zh">
                                <spring:message code="patient.wallet.current_balance"/>：<span class="word04">${deposit}</span><spring:message code="common.unit.yuan"/>
                                <br />
                                <a class="word05" href="${path}/patient/wallet/recharge"><spring:message code="patient.wallet.recharge"/></a>
                                <a class="zfmima" title='<spring:message code="patient.wallet.password.reset"/>'></a>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="purseSearch">
        <form id="qform" action="${path}/patient/wallet/service/page" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="50"/>
            <div class="purseSearch_a">
                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="patient.wallet.bill_time"/>：</div>
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
    <div class="purseDingdan">
        <div class="purseDingdan_a xw_jiaoyiliebiao"></div>
        <div class="purseDingdan_a xw_fuwuliebiao" style="display:none;"></div>
        <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
    </div>
</div>
<textarea id="template0" rows="0" cols="0" style=" display: none">
    <table>
        <thead>
        <tr class="tableTittle">
            <td><spring:message code="patient.wallet.title.service_name"/></td>
            <td><spring:message code="patient.wallet.title.org_name"/></td>
            <td><spring:message code="patient.wallet.title.effective_time"/></td>
            <td><spring:message code="patient.wallet.title.total_count"/></td>
            <td><spring:message code="patient.wallet.title.remain_count"/></td>
        </tr>
        </thead>
        <tbody>
        {#foreach $T.content as record}
        <tr class="{#cycle values=['dingdan01','']}">
            <td>{$T.record.serviceName}</td>
            <td>{$T.record.orgName}</td>
            <td>{$T.record.expiredDate}</td>
            <td>{$T.record.totalCount}</td>
            <td>{$T.record.count}</td>
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
<textarea id="template1" rows="0" cols="0" style=" display: none">
    <table>
        <thead>
        <tr class="tableTittle" >
            <td><spring:message code="patient.wallet.title.exchange_type"/></td>
            <td><spring:message code="patient.wallet.title.exchange_time"/></td>
            <td><spring:message code="patient.wallet.title.exchange_money"/></td>
            <td><spring:message code="patient.wallet.title.trade_no"/></td>
        </tr>
        </thead>
        <tbody>
        {#foreach $T.content as record}
        <tr class="{#cycle values=['dingdan01','']}">
            <td>{$T.record.typeName}{$T.record.definition}</td>
            <td>{$T.record.createDate}</td>
            <td>{$T.record.typeMoney}{$T.record.money}</td>
            <td>{$T.record.no}</td>
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
</body>
</html>
