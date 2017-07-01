<%--
  User: Sebarswee
  Date: 2015/7/7
  Time: 11:45
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.organization.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigoushdetail_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/organization/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/admin/organization"><spring:message code="admin.bread.organization"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.organization.view"/></span>
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
                <h1 class="userDetailConthText"><spring:message code="admin.organization.form.baseInfo"/></h1>
            </div>
            <div class="userDetail">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="admin.organization.form.name"/>:</td>
                        <th>${org.name}</th>
                        <td><spring:message code="admin.organization.form.code"/>:</td>
                        <th>${org.code}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.type"/>:</td><th>${org.typeName}</th>
                        <td><spring:message code="admin.organization.form.sign"/>:</td><th><spring:message code="common.enum.isSign.${org.isSign}"/></th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.contact"/>:</td><th>${org.contact}</th>
                        <td><spring:message code="admin.organization.form.telephone"/>:</td><th>${org.telephone}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.address"/>:</td>
                        <th colspan="3">${org.nationName}${org.provinceName}${org.cityName} ${org.address}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.email"/>:</td><th>${org.email}</th>
                        <td><spring:message code="admin.organization.form.zoneCode"/>:</td><th>${org.zoneCode}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.firstName"/>:</td><th>${org.firstName}</th>
                        <td><spring:message code="admin.organization.form.secondName"/>:</td><th>${org.secondName}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.ic"/>:</td>
                        <th colspan="3">${org.ic}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.sex"/>:</td><th>${org.sexText}</th>
                        <fmt:formatDate value="${org.orgBirthday}" var="orgBirthday" pattern="yyyy-MM-dd" />
                        <td><spring:message code="admin.organization.form.birthday"/>:</td><th>${orgBirthday}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.intro"/>:</td>
                        <th colspan="3"><c:out value="${org.intro}"/></th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.organization.form.image"/>:</td>
                        <th colspan="3">
                            <div id="orgImgList">
                                <c:if test="${!empty org.orgImages}">
                                    <c:forEach var="orgImage" items="${org.orgImages}">
                                        <h1 class="addimgDiv xw_addimgDiv">
                                            <img src="${basePath}/file?path=${orgImage.medium}" alt="" source="${basePath}/file?path=${orgImage.source}"/>
                                        </h1>
                                    </c:forEach>
                                </c:if>
                            </div>
                        </th>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="userMain" style="margin:10px 0px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.organization.form.doctorQualification"/></h1>
            </div>
            <div class="userDetail">
                <div class="jigouDivHead">
                    <div class="picDiv">
                        <h1 id="doctorHead">
                            <c:choose>
                                <c:when test="${!empty org.doctorImages}">
                                    <img src="${basePath}/file?path=${org.doctorImages[0].large}" source="${basePath}/file?path=${org.doctorImages[0].source}"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/resources/images/addimg.jpg"/>
                                </c:otherwise>
                            </c:choose>
                        </h1>
                    </div>

                    <div class="jigouDivHeadTitle">
                        <h1>${org.doctorName}</h1>
                    </div>
                    <div class="jigouDivHeadBody">
                        <table class="userMsgTableB">
                            <tr>
                                <td><spring:message code="admin.organization.form.doctor.ic"/>:</td><th>${org.doctorIc}</th>
                                <td><spring:message code="admin.organization.form.doctor.sex"/>:</td><th><spring:message code="common.enum.sex.${org.doctorSex}"/></th>
                                <fmt:formatDate value="${org.doctorBirthDay}" var="doctorBirthDay" pattern="yyyy-MM-dd" />
                                <td><spring:message code="admin.organization.form.doctor.birthday"/>:</td><th>${doctorBirthDay}</th>
                            </tr>
                            <tr>
                                <td><spring:message code="admin.organization.form.doctor.certificate"/>:</td>
                                <th colspan="5">
                                    <c:if test="${!empty org.doctorImages}">
                                        <c:forEach var="doctorImage" items="${org.doctorImages}">
                                            <h1 class="yszs">
                                                <img width="100" src="${basePath}/file?path=${doctorImage.thumbnail}" alt="" large="${basePath}/file?path=${doctorImage.large}" source="${basePath}/file?path=${doctorImage.source}"/>
                                            </h1>
                                        </c:forEach>
                                    </c:if>
                                </th>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="wfCode" value="${wfCode}"/>
    <input type="hidden" id="oid" value="${org.id}"/>
    <input type="hidden" id="guid" value="${org.guid}"/>
</div>
</body>
</html>
