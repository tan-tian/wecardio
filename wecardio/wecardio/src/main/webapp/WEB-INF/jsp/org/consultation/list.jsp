<%--
  User: tantian
  Date: 2015/7/29
  Time: 19:26
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.consultation.list.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdanlist_ctr.css"/>
    <script type="text/javascript" src="${path}/resources/org/consultation/js/list.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.consultation"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="qform" action="${path}/org/consultation/page" method="post">
            <input type="hidden" id="pId" name="pId" value="${pId}"/>
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="15"/>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <%--
                <div class="crsearch">
                    <input type="text" name="serviceName" style="width:200px;" placeholder="<spring:message code="org.consultation.placeholder.name"/>"/>
                    <a class="queryBtn"></a>
                </div>
                --%>
                <div class="searchListDiv xw_searchListDiv">
                    <input type="hidden" id="searchProperty" name="searchProperty"/>
                    <input type="hidden" id="searchValue" name="searchValue"/>
                    <input type="text" class="xw_searchListInput" />
                    <ul class="searchListUl xw_searchListUl">
                    	<li><spring:message code="admin.consultation.query.msg"/></li>
                        <li><span><spring:message code="org.consultation.query.fullName"/>:</span><a searchProperty="patient.fullName"></a></li>
                        <li><span><spring:message code="org.consultation.query.mobile"/>:</span><a searchProperty="patient.mobile"></a></li>
                        <li><span><spring:message code="org.consultation.query.email"/>:</span><a searchProperty="patient.email"></a></li>
                    </ul>
                </div>

                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="org.consultation.query.created"/>：</div>
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
                <div class="searchFormChose" id="select">
                    <dl>
                        <dt style="width: 100px;"><spring:message code="org.consultation.query.state"/>：</dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="state" name="state"/>
                        </dd>
                    </dl>
                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="org.consultation.query.doctor"/>：</span>
                        <input type="text" id="doctor" name="doctor" class="normalInput"/>
                    </div>
                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="org.consultation.query.editDoctor"/>：</span>
                        <input type="text" name="editDoctor" class="normalInput"/>
                    </div>
                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="org.consultation.query.auditDoctor"/>：</span>
                        <input type="text" name="auditDoctor" class="normalInput"/>
                    </div>
                    <shiro:hasAnyRoles name="admin">
                    <div class="searchBar" style=" position:relative;z-index:1;">
                        <span class="spanName"><spring:message code="org.consultation.query.org"/>：</span>
                        <input type="text" id="org" name="org" class="normalInput"/>
                    </div>
                    </shiro:hasAnyRoles>
                    <div class="searchBar" style=" position:relative;z-index:1;">
                        <span class="spanName"><spring:message code="org.consultation.query.serviceType"/>：</span>
                        <input type="hidden" id="serviceType" name="serviceType"/>
                    </div>
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

    <div class="muluList"></div>
    <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    {#foreach $T.content as record}
    <div class="sgListLiMain xw_sgListLiMain" cid="{$T.record.id}" guid="{$T.record.guid}">
        <a class="filecheckbox checkbox"></a>
        <div class="listStatic {#if $T.record.state == 0}{#elseif $T.record.state == 1}listStatic_green{#else}color02{#/if}">
            <h1>{$T.record.stateName}</h1>
            <h2>[{$T.record.createDate.substring(0, 10).replace(/-/g, '.')}]</h2>
        </div>
        <div class="sgListLiMainCenter xw_sgListLiMainCenter">
            <div class="sgListHead"><h1>{$T.record.createDate} {$T.record.serviceTypeName}</h1></div>
            <ul class="sgMsg">
                <li><span><spring:message code="org.consultation.list.patient"/>:</span><a>{$T.record.patientName}</a></li>
                <li><span><spring:message code="org.consultation.list.doctor"/>:</span><a>{$T.record.doctorName}</a></li>
                <li><span><spring:message code="org.consultation.list.org"/>：</span><a>{$T.record.orgName}</a></li>
                <li><span><spring:message code="org.consultation.list.paykind"/>：</span><a>{$T.record.payKindName}</a></li>
                <li><span><spring:message code="org.consultation.list.price"/>：</span><a>{$T.record.price}</a></li>
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
