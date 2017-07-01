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
    <title><spring:message code="patient.doctor.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigoushdetail_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/doctorlist_style.css" />
    <script type="text/javascript" src="${path}/resources/admin/doctor/js/detail.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/patient/home"><spring:message code="patient.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/patient/doctor/list"><spring:message code="patient.bread.doctor"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="patient.bread.doctor.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="patient.bread.back"/></a>
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

        <div class="jigouDivHeadTitle">
            <h1>${doctor.name}<i>[${doctor.roleName}]</i></h1><div style="float:right"></div>
        </div>
        <div class="jigouDivHeadBody">
            <table class="userMsgTable">
                <tr>
                    <td><spring:message code="patient.doctor.form.sex"/>:</td><th><spring:message code="common.enum.sex.${doctor.sex}"/> </th>
                    <fmt:formatDate value="${doctor.doctorBirthday}" var="doctorBirthday" pattern="yyyy-MM-dd"/>
                    <td><spring:message code="patient.doctor.form.birthday"/>:</td><th>${doctorBirthday}</th>
                    <%-- 患者不允许查看医生身份证号
                    <td><spring:message code="patient.doctor.form.ic"/>:</td><th colspan="3">${doctor.ic}</th>
                    --%>
                </tr>
                <tr>
                    <td><spring:message code="patient.doctor.form.mobile"/>:</td><th>${doctor.mobile}</th>
                    <td><spring:message code="patient.doctor.form.email"/>:</td><th>${doctor.email}</th>
                </tr>
                <tr>
                    <td><spring:message code="patient.doctor.form.address"/>:</td><th colspan="5">${doctor.address}</th>
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
    <div class="detailContent">
        <h1><spring:message code="patient.doctor.form.intro"/>:</h1>
        <p><c:out value="${doctor.intro}"/></p>
    </div>
    <%-- 患者不允许查看医生证书
    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="patient.doctor.form.qualification"/></h1>
                <ul class="toolBarList" style="margin-top:5px;">
                </ul>
            </div>
            <div class="userDetail xw_pannal" style="padding:5px; min-height:120px; overflow:hidden;">
                <ul class="gongneng xw_fujian" id='panel1' style="margin:0 auto;">
                    <c:if test="${!empty doctor.doctorImages}">
                        <c:forEach var="doctorImage" items="${doctor.doctorImages}">
                            <li>
                                <img src="${path}/file?path=${doctorImage.thumbnail}" width="250" height="180" alt="" source="${path}/file?path=${doctorImage.source}"/>
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
    --%>
</div>
<input type="hidden" id="guid" value="${doctor.guid}"/>
<input type="hidden" id="wfCode" value="${wfCode}"/>
</body>
</html>
