<%--
  User: Sebarswee
  Date: 2015/7/31
  Time: 15:30
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.doctor.list.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/doctorlist_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/doctor/js/list.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.doctor"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="qform" action="${path}/patient/doctor/page" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="15"/>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" name="name" style="width:200px;" placeholder='<spring:message code="patient.doctor.placeholder.name"/>' />
                    <a class="queryBtn"></a>
                </div>

                <div class="searchBtMain">
                    <a class="subBt queryBtn"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt resetBtn"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>

            <div class="khKmainbody xw_kMainContentbody" style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">
                <div class="searchFormChose" id="select">
                    <dl>
                        <dt><spring:message code="patient.doctor.form.state"/>：</dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="loginState" name="loginState"/>
                        </dd>
                    </dl>
                    <%-- 患者只能查主治医师&改成后台进行判断 --%>
                    <input type="hidden" name="roles" value="2,3,6,10,7,11,14,15"/> 
                    <%--<dl>
                        <dt><spring:message code="patient.doctor.form.roles"/>：</dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="roles" name="roles"/>
                        </dd>
                    </dl>--%>
                    <c:if test="${!isVip}">
                        <div class="searchBar" style=" position:relative;z-index:3;">
                            <span class="spanName"><spring:message code="patient.doctor.form.org"/>：</span>
                            <input type="hidden" id="org" name="org"/>
                        </div>
                    </c:if>
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
        <ul class="tabToolBarUl">
        </ul>
    </div>
    <div id="list" class="muluList"></div>
    <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    <table class="doctorTable">
        <tbody>
        <tr>
            {#foreach $T.content as record}
            {#if $T.record.id==${doid} || ${!bindType}}
            <td class="doctorTd">
                <div class="doctorDiv xw_doctorDiv" did="{$T.record.id}">
                    <h1>
                        <img src="{#if $T.record.headImg != null && $T.record.headImg != ''}${basePath}/file?path={$T.record.headImg}{#else}${path}/resources/images/user/user01.png{#/if}" alt="" />
                        <b class="nameB">{$T.record.name}<i>[{$T.record.roleName}]</i><font class="{#if $T.record.loginState == 'onLine'}zhuangtai1{#else}zhuangtai2{#/if}">[{$T.record.onlineState}]</font></b>
                        <a class="filecheckbox checkbox"></a>
                    </h1>
                    <p>{$T.record.intro}</p>
                    <div class="doctorNumber">
                        <table class="doctorNumberTable">
                            <tr>
                                <th>{$T.record.patientNum}</th>
                                <th>{$T.record.orderNum}</th>
                            </tr>
                            <tr>
                                <td><spring:message code="patient.doctor.form.patientNum"/></td>
                                <td><spring:message code="patient.doctor.form.orderNum"/></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </td>
            {#/if}
            {#if $T.record$index != 0 && ($T.record$index + 1) % 5 == 0}
        </tr>
        <tr>
            {#/if}
            {#/for}

            {#if $T.content.length % 5 != 0}
            {#param name=remain value=(5 - ($T.content.length % 5))}
            {#for begin = 1 to $P.remain}
            <td class="doctorTd"><div class="doctorDiv xw_doctorDiv"></div></td>
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
