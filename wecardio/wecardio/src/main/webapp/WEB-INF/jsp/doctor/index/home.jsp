<%--
  User: tantian
  Date: 2015/7/14
  Time: 16:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.doctor.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigoushdetail_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/doctorlist_style.css" />
    <script type="text/javascript" src="${path}/resources/admin/doctor/js/detail.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/doctor/home"><spring:message code="doctor.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.doctor.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="doctor.bread.back"/></a>
    </div>

    <div class="jigouDivHead">
        <div class="picDiv">
            <h1>
                <c:choose>
                    <c:when test="${doctor.headImg != null && doctor.headImg != ''}">
                        <img class="xw_pic" src="${basePath}/file?path=${doctor.headImg}" alt="" />
                    </c:when>
                    <c:otherwise>
                        <img class="xw_pic" src="${path}/resources/images/user/user01.png" alt=""/>
                    </c:otherwise>
                </c:choose>
            </h1>
        </div>

        <a class="buyA" href="${path}/doctor/doctor/${doctor.id}/edit"><spring:message code="common.btn.doctor.edit"/></a>

        <div class="jigouDivHeadTitle">
            <h1>${doctor.name}<i>[${doctor.roleName}]</i></h1>
            <div style="float:right">
                <h2><spring:message code="admin.doctor.form.org"/>：${doctor.orgName}</h2>
            </div>
        </div>
        <div class="jigouDivHeadBody">
            <table class="userMsgTable">
                <tr>
                    <td><spring:message code="admin.doctor.form.sex"/>:</td><th><spring:message code="common.enum.sex.${doctor.sex}"/> </th>
                    <td><spring:message code="admin.doctor.form.ic"/>:</td><th>${doctor.ic}</th>
                    <td><spring:message code="admin.doctor.form.account"/>:</td><th>${doctor.account}</th>
                </tr>
                <tr>
                    <fmt:formatDate value="${doctor.doctorBirthday}" var="doctorBirthday" pattern="yyyy-MM-dd"/>
                    <td><spring:message code="admin.doctor.form.birthday"/>:</td><th>${doctorBirthday}</th>
                    <td><spring:message code="admin.doctor.form.mobile"/>:</td><th>${doctor.mobile}</th>
                    <td><spring:message code="admin.doctor.form.email"/>:</td><th>${doctor.email}</th>
                </tr>
                <tr>
                    <td><spring:message code="admin.doctor.form.address"/>:</td><th colspan="5">${doctor.nationName}${doctor.provinceName}${doctor.cityName}${doctor.address}</th>
                </tr>
            </table>
        </div>
        <%-- 认证图标 --%>
        <c:if test="${doctor.auditState != 2}">
            <div class="renzhengDivA">
                <div class="renzhengDiv">
                    <h1></h1>
                    <div class="renzhengM">
                        <table>
                            <tr><td><a class="ico_xz01"></a></td></tr>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
    <!--医生完成审核后需要隐藏这个模块-->
    <c:if test="${doctor.auditState != 2}">
        <div class="flowContent xw_flowContent" style="margin-bottom:10px;">
            <div class="flowContentHeadDiv">
                <ul class="flowContentHead">
                </ul>
                <a class="sliderFlowList xw_sliderFlowList" loaded="false"></a>
            </div>
            <div class="flowContentBodyDiv xw_flowContentBodyDiv">
                <table class="flowContentBodyTable">
                    <thead>
                    <tr>
                        <th><spring:message code="common.wf.title.exeDate"/></th>
                        <th><spring:message code="common.wf.title.act"/></th>
                        <th><spring:message code="common.wf.title.actorName"/></th>
                        <th><spring:message code="common.wf.title.result"/></th>
                        <th><spring:message code="common.wf.title.remark"/></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </c:if>
    <!-------------------------->
    <div class="detailContent">
        <h1><spring:message code="admin.doctor.form.intro"/>:</h1>
        <p><c:out value="${doctor.intro}"/></p>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.doctor.form.qualification"/></h1>
                <ul class="toolBarList" style="margin-top:5px;">
                </ul>
            </div>
            <div class="userDetail xw_pannal" style="padding:5px; min-height:120px; overflow:hidden;">
                <ul class="gongneng xw_fujian" id='panel1' style="margin:0 auto;">
                    <c:if test="${!empty doctor.doctorImages}">
                        <c:forEach var="doctorImage" items="${doctor.doctorImages}">
                            <li>
                                <img src="${basePath}/file?path=${doctorImage.thumbnail}" width="250" height="180" alt="" source="${basePath}/file?path=${doctorImage.source}"/>
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="guid" value="${doctor.guid}"/>
<input type="hidden" id="wfCode" value="${wfCode}"/>
</body>
</html>
