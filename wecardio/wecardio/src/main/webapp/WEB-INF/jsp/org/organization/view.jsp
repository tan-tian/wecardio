<%--
  User: tantian
  Date: 2015/6/29
  Time: 15:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.organization.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/hospital_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css" />
    <script type="text/javascript" src="${path}/resources/org/organization/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/organization"><spring:message code="org.bread.organization"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.organization.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li><a href="${path}/org/organization/${org.id}/edit"><spring:message code="common.btn.edit"/></a></li>
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

        <c:if test="${org.auditState == 1}">
            <%-- 申请认证按钮 --%>
            <a class="buyA" href="${path}/org/organization/${org.id}/edit"><spring:message code="common.btn.org.authentication"/></a>
        </c:if>

        <div class="jigouDivHeadTitle">
            <h1>${org.name}</h1>
            <div style="float:right"><h2><spring:message code="org.organization.form.code"/>：<a>${org.code}</a></h2></div>
        </div>
        <div class="jigouDivHeadBody">
            <table class="userMsgTable">
                <tr>
                    <td><spring:message code="org.organization.form.userName"/>:</td><th>${org.username}</th>
                    <td style="width: 100px;"><spring:message code="org.organization.form.ic"/>:</td><th>${org.ic}</th>
                    <fmt:formatDate value="${org.orgBirthday}" var="orgBirthday" pattern="yyyy-MM-dd" />
                    <td style="width: 100px;"><spring:message code="org.organization.form.birthday"/>:</td><th>${orgBirthday}</th>
                </tr>
                <tr>
                    <td><spring:message code="org.organization.form.type"/>:</td><th><spring:message code="common.enum.orgType.${org.type}"/></th>
                    <td><spring:message code="org.organization.form.sign"/>:</td><th><spring:message code="common.enum.isSign.${org.isSign}"/></th>
                    <td><spring:message code="org.organization.form.sex"/>:</td><th>${org.sexText}</th>
                </tr>
                <tr>
                    <td><spring:message code="org.organization.form.contact"/>:</td><th>${org.contact}</th>
                    <td><spring:message code="org.organization.form.telephone"/>:</td><th>${org.telephone}</th>
                    <td><spring:message code="org.organization.form.email"/>:</td><th>${org.email}</th>
                </tr>
                <tr>
                    <td><spring:message code="org.organization.form.rate"/>:</td>
                    <th>${org.ratePercent}</th>
                </tr>
                <tr>
                    <td><spring:message code="org.organization.form.address"/>:</td><th colspan="5">${org.nationName}${org.provinceName}${org.cityName} ${org.address}</th>
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
    <!--医生完成审核后需要隐藏这个模块-->
    <c:if test="${org.auditState != 2}">
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
    <div class="dataTablediv">
        <table class="dataTable">
            <tr>
                <td>
                    <div class="dataDiv ico_doctor02" onclick="window.location.href='${path}/org/doctor';">
                        <h1>${org.doctorNum}</h1>
                        <h2><spring:message code="org.organization.form.doctorNum"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_huanzhe02" onclick="window.location.href='${path}/org/patient?iType=2';">
                        <h1>${org.patientNum}</h1>
                        <h2><spring:message code="org.organization.form.patientNum"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_hospitalbg" onclick="window.location.href='${path}/org/service/toPage/query';">
                        <h1>${org.serviceNum}</h1>
                        <h2><spring:message code="org.organization.form.serviceNum"/></h2>
                    </div>
                </td>
                <th>
                    <div class="dataDiv ico_fuwu02" onclick="window.location.href='${path}/org/consultation/list';">
                        <h1>${org.orderNum}</h1>
                        <h2><spring:message code="org.organization.form.orderNum"/></h2>
                    </div>
                </th>
            </tr>
        </table>
    </div>
    <div class="detailContent">
        <h1><spring:message code="org.organization.form.intro"/>:</h1>
        <p>
            <c:out value="${org.intro}"/>
        </p>
    </div>

    <div class="userMain" style="margin:10px 0px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="org.form.doctorQualification"/></h1>
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
                                <td><spring:message code="org.organization.form.doctor.ic"/>:</td><th>${org.doctorIc}</th>
                                <td><spring:message code="org.organization.form.doctor.sex"/>:</td><th><spring:message code="common.enum.sex.${org.doctorSex}"/></th>
                                <fmt:formatDate value="${org.doctorBirthDay}" var="doctorBirthDay" pattern="yyyy-MM-dd" />
                                <td><spring:message code="org.organization.form.doctor.birthday"/>:</td><th>${doctorBirthDay}</th>
                            </tr>
                            <tr>
                                <td><spring:message code="org.organization.form.doctor.certificate"/>:</td>
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
    <div>
        <div class="userMain" style="margin:10px 0px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><spring:message code="org.organization.form.evaluation"/></h1>
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
                                        <span class="chaping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="org.organization.evaluation.low"/>
                                    </c:when>
                                    <c:when test="${forumInfo.score < 5}">
                                        <span class="zhongping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="org.organization.evaluation.middle"/>
                                    </c:when>
                                    <c:when test="${forumInfo.score == 5}">
                                        <span class="haoping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="org.organization.evaluation.high"/>
                                    </c:when>
                                </c:choose>
                                </h1>
                                <a href="${path}/org/consultation/${forumInfo.consultationId}/view"><spring:message code="org.organization.form.tradeNo"/>：${forumInfo.consultationCode}</a>
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
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="wfCode" value="${wfCode}"/>
<input type="hidden" id="guid" value="${org.guid}"/>
</body>
</html>
