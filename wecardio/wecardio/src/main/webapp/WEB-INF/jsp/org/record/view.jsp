<%--
  User: Sebarswee
  Date: 2015/7/22
  Time: 10:27
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.record.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdandetail_style.css"/>
    <script type="text/javascript" src="${path}/resources/org/record/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/record"><spring:message code="org.bread.record"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.record.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>

    <div class="projectDetailHead projectDetailHeadA">
        <div class="YDDetailStact">
            <div class="YDDetailStactDiv YDDetailStactDivA">
                <h2 style="padding-top:45px;">
                    <c:choose>
                        <c:when test="${record.isCommit}">
                            <spring:message code="org.record.list.state.done"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="org.record.list.state.todo"/>
                        </c:otherwise>
                    </c:choose>
                </h2>
                <h3>[<spring:message code="org.record.view.state"/>]</h3>
            </div>
            <fmt:formatDate value="${record.reportDate}" var="reportDate" pattern="yyyy-MM-dd" />
            <h1 class="YDDetailStactData">${reportDate}</h1>
        </div>

        <div class="projectDetailHeadTitle">
            <fmt:formatDate value="${record.testDate}" var="testDate" pattern="yyyy-MM-dd HH:mm:ss" />
            <h1>${testDate} ${record.serviceTypeName}</h1>
        </div>
        <div class="projectDetailHeadBody">
            <div class="projetListConFoot">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="org.record.view.serviceType"/>:</td><th>${record.serviceTypeName}</th>
                        <td><spring:message code="org.record.view.doctor"/>:</td><th>${record.doctorName}</th>
                        <td><spring:message code="org.record.view.ext"/>:</td>
                        <th>
                            <c:choose>
                                <c:when test="${record.stoplight == 'red'}">
                                    <img src="${path}/resources/images/heartIco/xin1.png" width="24" height="24" alt=""/>
                                </c:when>
                                <c:when test="${record.stoplight == 'yellow'}">
                                    <img src="${path}/resources/images/heartIco/xin3.png" width="24" height="24" alt=""/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/resources/images/heartIco/xin2.png" width="24" height="24" alt=""/>
                                </c:otherwise>
                            </c:choose>
                        </th>

                    </tr>
                    <tr>
                        <fmt:formatDate value="${record.testDate}" var="testDate" pattern="yyyy-MM-dd HH:mm:ss" />
                        <td><spring:message code="org.record.view.tested"/>:</td><th>${testDate}</th>
                        <fmt:formatDate value="${record.createDate}" var="createDate" pattern="yyyy-MM-dd" />
                        <td><spring:message code="org.record.view.created"/>:</td><th>${createDate}</th>
                        <td></td>
                        <th id="flag"></th>
                    </tr>
                    <tr>
                        <td><spring:message code="org.record.view.symptom"/>:</td><th>${record.condition}</th>
                        <td><spring:message code="org.record.view.org"/>:</td><th>${record.orgName}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="org.record.view.findings"/>:</td><th colspan="3">${record.findings}</th>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">

            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="org.record.view.pdf"/></h1>
                <ul class="toolBarList xw_hiddenCTR">
                    <li><a id="downloadBtn"><spring:message code="common.btn.download"/></a></li>
                </ul>
            </div>
            <div class="userDetail" id="pdfContent">
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="rid" value="${record.id}"/>
</body>
</html>
