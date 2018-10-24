<%--
  User: tantian
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
    <script type="text/javascript" src="${path}/resources/doctor/device/js/bindLog.js"></script>
</head>
<body>
<div class="patient_purse">
    <div class="addressBar">
        <a class="leader" href="${path}/doctor/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.wallet"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>
   
    <div class="purseSearch">
        <form id="qform" action="${path}/${sessionScope.userTypePath}/device/bindLog" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="50"/>
            <input type="hidden" id="imeId" name="imeId" value="${imeId}"/>
            <div class="purseSearch_a">
                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="admin.service.daichong.time"/>：</div>
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

<textarea id="template" rows="0" cols="0" style=" display: none">
    <table>
        <thead>
        <tr class="tableTittle" >
            <td><spring:message code="common.device.operateDoctor"/></td>
            <td><spring:message code="common.device.operateType"/></td>
           	<td><spring:message code="admin.service.daichong.time"/></td>
        </tr>
        </thead>
        <tbody>
        {#foreach $T.content as record}
        <tr class="{#cycle values=['dingdan01','']}">
            <td>{$T.record.doctorName}</td>
            <td><spring:message code="{$T.record.type}"></spring:message></td>
            <td>{$T.record.time}</td>
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
