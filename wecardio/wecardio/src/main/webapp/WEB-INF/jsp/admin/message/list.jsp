<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.message.title"/></title>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css" />
<script type="text/javascript" src="${path}/resources/admin/message/list.js"></script>
    <script type="text/javascript">
        var refuseMsg = "<spring:message code="common.message.confirm.refuse"/>";
        var acceptMsg = "<spring:message code="common.message.confirm.accept"/>";
        var deleteMsg = "<spring:message code="common.message.confirm.delete"/>";
        var notData = "<spring:message code="common.message.noData"/>";
    </script>
</head>

<body>
<div class="BodyMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.message.title"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>

    <form id="qform" action="" method="post">
        <input type="hidden" id="pageNo" name="pageNo" value="1"/>
        <input type="hidden" id="pageSize" name="pageSize" value="5"/>

        <div class="objBoxCont" style="margin-bottom:10px;">
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                  <input type="text" name="name" style="width:200px;" placeholder="<spring:message code="admin.patient.list.search"/>" />
                  <a class="queryBtn"></a>
                </div>

                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;"><spring:message code="admin.message.list.sendTime"/>：</div>
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

            <div class="khKmainbody xw_kMainContentbody" style=" background:#F4F2F3;min-height:75px; padding:0px; border-top:0px;display:none;">

                <div class="searchFormChose" id="select">
                    <dl>
                      <dt><spring:message code="admin.message.list.type"/>：</dt>
                      <dd class="moreSelect">
                           <input type="hidden" id="type" name="type"/>
                      </dd>
                    </dl>
                </div>

                <c:if test="${sessionScope.userTypePath=='patient' || sessionScope.userTypePath=='admin' }">
                    <div class="searchFormChose">
                        <dl>
                            <dt><spring:message code="admin.organization.form.name"/>：</dt>
                            <dd class="moreSelect">
                                <input type="hidden" id="org" name="org"/>
                            </dd>
                        </dl>
                    </div>
                </c:if>

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
        </div>
        </form>
	
    <div class="titleBar">
        <a class="checkbox uncheck xw_allcheck" rel="0" style=" margin:0px; margin-right:10px; height:28px;"></a>
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font id="totalNum">0</font><spring:message code="common.toolbar.bill.title"/></h1>
        <ul class="toolBarList xw_hiddenCTR">
            <%--新增消息--%>
            <%--编辑消息--%>
            <c:if test="${sessionScope.userTypePath=='admin'||sessionScope.userTypePath=='org'}">
                <li><a id="addBtn"><spring:message code="common.btn.message.add"/></a></li>
                <%--<li><a id="editBtn"><spring:message code="common.btn.message.edit"/></a></li>--%>
                <li><a id="deleteBtn"><spring:message code="common.btn.message.delete"/></a></li><%--删除消息--%>
            </c:if>
        </ul>
    </div>

    <div id="list" class="muluList" style="min-height:480px;"></div>
    <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
</div>

<textarea id="template" rows="0" cols="0" style="display: none">

    <div class="talkBarDiv">
        <a class="filecheckbox checkbox xw_checkbox" id="{$T.id}"></a>
        <div class="userpicDiv">
            <img src="{#if $T.from_HeadPath != null && $T.from_HeadPath != ''}${path}/file?path={$T.from_HeadPath}{#else}${path}/resources/images/userdetails/touxiang.jpg{#/if}" alt=""  width="60" height="60"/>
            <a>{$T.from_name}</a>
        </div>

        <div class="talkBarDivMain xw_talkBarDivMain" id="{$T.id}">
            <div class="talkBarDivHead">
                <h1><span><spring:message code="admin.message.list.type"/>：</span>
                    {#if $T.type=='0'}
                    <spring:message code="admin.message.MessageType.consultationResult"/>
                    {#/if}
                    {#if $T.type=='1'}
                    <spring:message code="admin.message.MessageType.healthMessage"/>
                    {#/if}
                    {#if $T.type=='2'}
                    <spring:message code="admin.message.MessageType.systemMesage"/>
                    {#/if}
                    {#if $T.type=='3'}
                    <spring:message code="admin.message.MessageType.invitePatient"/>
                    {#/if}
                    {#if $T.type=='4'}
                    <spring:message code="admin.message.MessageType.patientComfireAdd"/>
                    {#/if}
                    {#if $T.type=='5'}
                    <spring:message code="admin.message.MessageType.patientRefuse"/>
                    {#/if}
                    {#if $T.type=='6'}
                    <spring:message code="admin.message.MessageType.commonMessage"/>
                    {#/if}
                    {#if $T.type=='7'}
                    <spring:message code="admin.message.MessageType.doctorOpinionMessage"/>
                    {#/if}
                    {#if $T.type=='8'}
                    <spring:message code="admin.message.MessageType.doctorRelievePatientMessage"/>
                    {#/if}
                    {#if $T.type=='9'}
                    <spring:message code="admin.message.MessageType.patientRelievePatientMessage"/>
                    {#/if}
                </h1>
            </div>
            <div class="talkBarDivBody">
                <p>{$T.content}</p>
            </div>
            <div class="talkBarDivfoot">
                <c:if test="${sessionScope.userTypePath!='admin'}">
                    <%--类型为【3 邀请患者加入机构信息】，并且未处理的消息才显示--%>
                        {#if $T.type=='3'&&($T.iDeal==0||$T.iDeal==''||$T.iDeal==NULL)}
                            <a class="pingjia" id="refuseBtn{$T.id}" val="{$T.id}"><spring:message code="admin.message.MessageType.refuse"/></a>
                            <a class="pingjia" id="acceptBtn{$T.id}" val="{$T.id}"><spring:message code="admin.message.MessageType.accept"/></a>
                        {#/if}
                </c:if>
                <font class="timeshowText">{$T.dateCreate}</font>
                <font class="timeshowText"><span><spring:message code="admin.message.MessageType.receiver"/>：</span>{$T.to_name}</font>
            </div>
        </div>
    </div>
</textarea>
</body>
</html>
