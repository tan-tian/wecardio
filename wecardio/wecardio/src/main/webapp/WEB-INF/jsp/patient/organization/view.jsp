<%--
  User: tantian
  Date: 2015/7/31
  Time: 14:57
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.organization.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/hospital_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/doctorlist_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/organization/js/view.js"></script>
</head>
<body>
<div class="pageMain">
<div class="addressBar">
    <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
    <span class="arrowText">&gt;</span>
    <a class="jumpback" href="${path}/patient/organization/list"><spring:message code="patient.bread.organization"/></a>
    <span class="arrowText">&gt;</span>
    <span class="normal"><spring:message code="patient.bread.organization.view"/></span>
    <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
</div>

<div class="ctrolToolbar">
    <ul class="ctrolToolbarUl">
        <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
    <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
</div>

<div class="jigouDivHead">
    <div class="picDiv">
        <h1>
            <c:choose>
                <c:when test="${!empty org.orgImages}">
                    <img class="xw_pic" src="${basePath}/file?path=${org.orgImages[0].large}" source="${basePath}/file?path=${org.orgImages[0].source}"/>
                </c:when>
                <c:otherwise>
                    <img class="xw_pic" src="${path}/resources/images/addimg.jpg"/>
                </c:otherwise>
            </c:choose>
        </h1>
        <ul class="picDivList xw_picDivList">
            <c:if test="${!empty org.orgImages}">
                <c:forEach var="orgImage" items="${org.orgImages}">
                    <li>
                        <img src="${basePath}/file?path=${orgImage.thumbnail}" alt="" large="${basePath}/file?path=${orgImage.large}" source="${basePath}/file?path=${orgImage.source}"/>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>

    <div class="jigouDivHeadTitle">
        <input type="hidden" id="oid" value="${org.id}"/>
        <h1>${org.name}</h1>
    </div>
    <div class="jigouDivHeadBody">
        <table class="userMsgTable">
            <tr>
                <td><spring:message code="patient.organization.form.code"/>:</td><th>${org.code}</th>
                <td><spring:message code="patient.organization.form.type"/>:</td><th><spring:message code="common.enum.orgType.${org.type}"/></th>
                <td><spring:message code="patient.organization.form.sign"/>:</td><th><spring:message code="common.enum.isSign.${org.isSign}"/></th>
            </tr>
            <tr>
                <td><spring:message code="patient.organization.form.contact"/>:</td><th>${org.contact}</th>
                <td><spring:message code="patient.organization.form.telephone"/>:</td><th>${org.telephone}</th>
                <td><spring:message code="patient.organization.form.email"/>:</td><th>${org.email}</th>
            </tr>
            <tr>
                <td><spring:message code="patient.organization.form.address"/>:</td><th colspan="5">${org.nationName}${org.provinceName}${org.cityName} ${org.address}</th>
            </tr>
        </table>
        <c:if test="${org.auditState == 2}">
            <%-- 已认证图标 --%>
            <div class="renzhengDivA">
                <div class="renzhengDiv">
                    <h1></h1>
                    <div class="renzhengM">
                        <table>
                            <tr><td><a class="ico_rz"></a></td></tr>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>
<div class="dataTablediv">
    <table class="dataTable">
        <tr>
            <td>
                <div class="dataDiv ico_doctor02" onclick="window.location.href='${path}/patient/doctor/list';">
                    <h1>${org.doctorNum}</h1>
                    <h2><spring:message code="patient.organization.form.doctorNum"/></h2>
                </div>
            </td>
            <td>
                <div class="dataDiv ico_huanzhe02">
                    <h1>${org.patientNum}</h1>
                    <h2><spring:message code="patient.organization.form.patientNum"/></h2>
                </div>
            </td>
            <td>
                <div class="dataDiv ico_hospitalbg" onclick="window.location.href='${path}/patient/service/toPage/query';">
                    <h1>${org.serviceNum}</h1>
                    <h2><spring:message code="patient.organization.form.serviceNum"/></h2>
                </div>
            </td>
            <th>
                <div class="dataDiv ico_fuwu02" onclick="window.location.href='${path}/patient/consultation/list';">
                    <h1>${org.orderNum}</h1>
                    <h2><spring:message code="patient.organization.form.orderNum"/></h2>
                </div>
            </th>
        </tr>
    </table>
</div>
<div class="detailContent">
    <h1><spring:message code="patient.organization.form.intro"/>:</h1>
    <p>
        <c:out value="${org.intro}"/>
    </p>
</div>

<div class="userMain" style="margin:10px 0px;">
    <div class="userDetailCont" style="padding-top:0px;">
        <div class="userDetailContHead">
            <h1 class="userDetailConthText"><spring:message code="patient.organization.form.doctor_list"/></h1>
        </div>
        <form id="qform" action="${path}/patient/doctor/page" method="post" style="display: none;">
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="15"/>
            <%-- 只查主治医师 --%>
            <input type="hidden" name="roles" value="2"/>
        </form>
        <div id="list" class="userDetail">
        </div>
        <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
    </div>
</div>
<div>
    <div class="userMain" style="margin:10px 0px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="patient.organization.form.evaluation"/></h1>
                <h1 class="dataTotalTitle"  style="padding:5px 10px 0px 0px; float:right;"><spring:message code="common.toolbar.total.title"/> <font>${org.commentNum}</font><spring:message code="common.toolbar.bill.title"/></h1>
            </div>
            <div class="userDetail">
                <c:forEach var="forumInfo" items="${forumInfos}">
                    <div class="talkBarDiv1">
                        <div class="userpicDiv1">
                            <img src="${path}/resources/images/user/uer002.jpg" width="60" height="60" />
                            <a>${forumInfo.createName}</a>
                        </div>
                        <div class="talkBarDiv1Main xw_talkBarDivMain">
                            <div class="talkBarDivHead">
                                <h1>
                                    <c:choose>
                                        <c:when test="${forumInfo.score < 3}">
                                            <span class="chaping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="patient.organization.evaluation.low"/>
                                        </c:when>
                                        <c:when test="${forumInfo.score < 5}">
                                            <span class="zhongping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="patient.organization.evaluation.middle"/>
                                        </c:when>
                                        <c:when test="${forumInfo.score == 5}">
                                            <span class="haoping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="patient.organization.evaluation.high"/>
                                        </c:when>
                                    </c:choose>
                                </h1>
                                <a href="${path}/patient/consultation/${forumInfo.consultationId}/view"><spring:message code="patient.organization.form.tradeNo"/>：${forumInfo.consultationCode}</a>
                            </div>
                            <div class="talkBarDivBody">
                                <p><c:out value="${forumInfo.content}"/></p>
                            </div>
                            <div class="talkBarDivfoot">
                                <fmt:formatDate value="${forumInfo.createdDate}" var="createdDate" pattern="yyyy-MM-dd HH:mm:ss" />
                                <font class="timeshowText">${createdDate}</font>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty forumInfos}">
                    <div class="jiachajilu">
                        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</div>

<textarea id="template" rows="0" cols="0" style="display: none">
    <table class="doctorTable">
        <tbody>
        <tr>
            {#foreach $T.content as record}
            <td class="doctorTd">
                <div class="doctorDiv xw_doctorDiv" did="{$T.record.id}">
                    <h1>
                        <img src="{#if $T.record.headImg != null && $T.record.headImg != ''}${basePath}/file?path={$T.record.headImg}{#else}${path}/resources/images/user/user01.png{#/if}" alt="" />
                        <b class="nameB">{$T.record.name}<i>[{$T.record.roleName}]</i><font class="{#if $T.record.loginState == 'onLine'}zhuangtai1{#else}zhuangtai2{#/if}">[{$T.record.onlineState}]</font></b>
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
