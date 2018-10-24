<%--
  User: tantian
  Date: 2015/7/28
  Time: 15:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.consultation.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdandetail_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/consultation/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/admin/consultation/list"><spring:message code="admin.bread.consultation"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.consultation.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
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
                <h2 style="padding-top:45px;">${consultation.stateName}</h2>
                <h3>[<spring:message code="admin.consultation.view.state"/>]</h3>
            </div>
            <fmt:formatDate value="${consultation.createDate}" var="createDate" pattern="yyyy-MM-dd" />
            <h1 class="YDDetailStactData">${createDate}</h1>
        </div>

        <div class="projectDetailHeadTitle">
            <fmt:formatDate value="${consultation.createDate}" var="createDate" pattern="yyyy-MM-dd HH:mm:ss" />
            <h1>${createDate} ${consultation.serviceTypeName}</h1>
            <span><spring:message code="admin.consultation.view.price"/>:<b>${consultation.price}</b></span>
        </div>
        <div class="projectDetailHeadBody">
            <div class="projetListConFoot">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="admin.consultation.view.patient"/>:</td><th>${consultation.patientName}</th>
                        <td><spring:message code="admin.consultation.view.doctor"/>:</td><th>${consultation.doctorName}</th>
                        <td><spring:message code="admin.consultation.view.paykind"/>:</td><th>${consultation.payKindName}</th>
                    </tr>
                    <tr>
                        <fmt:formatDate value="${consultation.testDate}" var="testDate" pattern="yyyy-MM-dd HH:mm:ss" />
                        <td><spring:message code="admin.consultation.view.tested"/>:</td><th>${testDate}</th>
                        <fmt:formatDate value="${consultation.recordCreateDate}" var="recordCreateDate" pattern="yyyy-MM-dd" />
                        <td><spring:message code="admin.consultation.view.created"/>:</td><th>${recordCreateDate}</th>
                        <fmt:formatDate value="${consultation.reportDate}" var="reportDate" pattern="yyyy-MM-dd" />
                        <td><spring:message code="admin.consultation.view.reported"/>:</td><th>${reportDate}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.consultation.view.symptom"/>:</td><th>${consultation.condition}</th>
                        <td><spring:message code="admin.consultation.view.org"/>:</td><th>${consultation.orgName}</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.consultation.view.doctorOpinion"/>:</td><th  colspan="3">${consultation.doctorOpinion}</th>
                    </tr>
                </table>
            </div>
        </div>
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
                <h1 class="userDetailConthText"><spring:message code="admin.consultation.view.pdf"/></h1>
                <ul class="toolBarList xw_hiddenCTR">
                    <li><a id="downloadBtn"><spring:message code="common.btn.download"/></a></li>
                </ul>
            </div>
            <div class="userDetail" id="pdfContent">
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="rid" value="${consultation.recordId}"/>
<input type="hidden" id="cid" value="${consultation.id}"/>
<input type="hidden" id="guid" value="${consultation.guid}"/>
<input type="hidden" id="wfCode" value="${wfCode}"/>
</body>
</html>
