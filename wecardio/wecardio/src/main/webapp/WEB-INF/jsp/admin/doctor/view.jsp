<%--
  User: tantian
  Date: 2015/7/15
  Time: 15:56
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.doctor.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigoushdetail_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/doctor/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/admin/doctor/todo"><spring:message code="admin.bread.doctor.todo"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.doctor.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li><a id="auditBtn"><spring:message code="common.btn.audit"/></a></li>
            <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>

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

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.doctor.form.baseInfo"/></h1>
            </div>
            <div class="userDetail">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="admin.doctor.form.name"/>:</td><th colspan="3">${doctor.name}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.doctor.form.ic"/>:</td><th>${doctor.ic}</th>
                        <td><spring:message code="admin.doctor.form.sex"/>:</td><th><spring:message code="common.enum.sex.${doctor.sex}"/></th>
                    </tr>
                    <tr>
                        <fmt:formatDate value="${doctor.doctorBirthday}" var="birthday" pattern="yyyy-MM-dd" />
                        <td><spring:message code="admin.doctor.form.birthday"/>:</td><th>${birthday}</th>
                        <td><spring:message code="admin.doctor.form.roles"/>:</td><th>${doctor.roleName}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.doctor.form.org"/>:</td><th>${doctor.orgName}</th>
                        <td><spring:message code="admin.doctor.form.account"/>:</td><th>${doctor.account}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.doctor.form.mobile"/>:</td><th>${doctor.mobile}</th>
                        <td><spring:message code="admin.doctor.form.email"/>:</td><th>${doctor.email}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.doctor.form.intro"/>:</td><th colspan="3"><p><c:out value="${doctor.intro}"/></p></th>
                    </tr>
                    <tr>
                        <td>医生头像:</td>
                        <th colspan="3">
                            <h1 class="addimgDiv xw_addimgDiv">
                                <c:choose>
                                    <c:when test="${doctor.headImg != null && doctor.headImg != ''}">
                                        <img src="${basePath}/file?path=${doctor.headImg}" alt="" />
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/resources/images/user/user01.png" alt=""/>
                                    </c:otherwise>
                                </c:choose>
                            </h1>
                        </th>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.doctor.form.qualification"/></h1>
                <ul class="toolBarList" style="margin-top:5px;">
                </ul>
            </div>
            <div class="userDetail xw_pannal" style="padding:5px; min-height:120px; overflow:hidden;">
                <ul class="gongneng xw_fujian" id='panel1'>
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
<input type="hidden" id="did" value="${doctor.id}"/>
<input type="hidden" id="guid" value="${doctor.guid}"/>
<input type="hidden" id="wfCode" value="${wfCode}"/>
</body>
</html>
