<%--
  User: Sebarswee
  Date: 2015/6/29
  Time: 15:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.organization.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/hospital_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css" />
    <script type="text/javascript" src="${path}/resources/admin/organization/js/detail.js"></script>
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
        <c:if test="${org.auditState == 2}">
        <li><a id="stopBtn"><spring:message code="common.btn.stop"/></a></li>
        </c:if>
        <c:if test="${org.auditState == 3}">
        <li><a id="openBtn"><spring:message code="common.btn.reopen"/></a></li>
        </c:if>
        <li><a id="configBtn"><spring:message code="common.btn.rateConfig"/></a></li>
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
                <td><spring:message code="admin.organization.form.code"/>:</td><th>${org.code}</th>
                <td><spring:message code="admin.organization.form.type"/>:</td><th><spring:message code="common.enum.orgType.${org.type}"/></th>
                <td><spring:message code="admin.organization.form.sign"/>:</td><th><spring:message code="common.enum.isSign.${org.isSign}"/></th>
            </tr>
            <tr>
                <td><spring:message code="admin.organization.form.contact"/>:</td><th>${org.contact}</th>
                <td><spring:message code="admin.organization.form.telephone"/>:</td><th>${org.telephone}</th>
                <td><spring:message code="admin.organization.form.email"/>:</td><th>${org.email}</th>
            </tr>
            <tr>
                <td><spring:message code="admin.organization.form.account"/>:</td>
                <th>${account}</th>
                <td><spring:message code="admin.organization.form.rate"/>:</td>
                <th>${org.ratePercent}</th>
            </tr>
            <tr>
                <td><spring:message code="admin.organization.form.address"/>:</td><th colspan="5">${org.nationName}${org.provinceName}${org.cityName} ${org.address}</th>
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
                <div class="dataDiv ico_doctor02" onclick="window.location.href='${path}/admin/doctor';">
                    <h1>${org.doctorNum}</h1>
                    <h2><spring:message code="admin.organization.form.doctorNum"/></h2>
                </div>
            </td>
            <td>
                <div class="dataDiv ico_huanzhe02" onclick="window.location.href='${path}/admin/patient?iType=2';">
                    <h1>${org.patientNum}</h1>
                    <h2><spring:message code="admin.organization.form.patientNum"/></h2>
                </div>
            </td>
            <td>
                <div class="dataDiv ico_hospitalbg" onclick="window.location.href='${path}/admin/service/toPage/query';">
                    <h1>${org.serviceNum}</h1>
                    <h2><spring:message code="admin.organization.form.serviceNum"/></h2>
                </div>
            </td>
            <th>
                <div class="dataDiv ico_fuwu02" onclick="window.location.href='${path}/admin/consultation/list';">
                    <h1>${org.orderNum}</h1>
                    <h2><spring:message code="admin.organization.form.orderNum"/></h2>
                </div>
            </th>
        </tr>
    </table>
</div>
<div class="detailContent">
    <h1><spring:message code="admin.organization.form.intro"/>:</h1>
    <p>
        <c:out value="${org.intro}"/>
    </p>
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
                                            <img width="100" src="${path}/file?path=${doctorImage.thumbnail}" alt="" large="${path}/file?path=${doctorImage.large}" source="${path}/file?path=${doctorImage.source}"/>
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
                <h1 class="userDetailConthText"><spring:message code="admin.organization.form.evaluation"/></h1>
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
                                        <span class="chaping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="admin.organization.evaluation.low"/>
                                    </c:when>
                                    <c:when test="${forumInfo.score < 5}">
                                        <span class="zhongping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="admin.organization.evaluation.middle"/>
                                    </c:when>
                                    <c:when test="${forumInfo.score == 5}">
                                        <span class="haoping">&nbsp;&nbsp;&nbsp;&nbsp;</span><spring:message code="admin.organization.evaluation.high"/>
                                    </c:when>
                                </c:choose>
                            </h1>
                            <a href="${path}/org/consultation/${forumInfo.consultationId}/view"><spring:message code="admin.organization.form.tradeNo"/>：${forumInfo.consultationCode}</a>
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
<script type="text/javascript">
    function confirmStop(fn) {
        msgBox.confirm({
            msg : "<spring:message code="common.message.confirm.stop"/>",
            callback : function(btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    fn();
                }
            }
        });
    }
    function confirmOpen(fn) {
        msgBox.confirm({
            msg : "<spring:message code="common.message.confirm.reopen"/>",
            callback : function(btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    fn();
                }
            }
        });
    }
</script>
</body>
</html>
