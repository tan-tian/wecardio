<%--
  User: tantian
  Date: 2015/8/13
  Time: 21:04
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.withdraw.view.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/tixianxiangqing.css"/>
    <script type="text/javascript" src="${path}/resources/admin/withdraw/js/view.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/admin/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/admin/withdraw/todo"><spring:message code="admin.bread.withdraw.todo"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="admin.bread.withdraw.view"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>
    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <c:choose>
                <c:when test="${order.state == 1}">
                    <li><a id="auditBtn"><spring:message code="common.btn.audit"/></a></li>
                </c:when>
                <c:when test="${order.state == 2}">
                    <li><a id="confirmBtn"><spring:message code="common.btn.out"/></a></li>
                </c:when>
            </c:choose>
            <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>
    <div class="projectDetailHead" style="margin-bottom:15px;">
        <div class="YDDetailStact">
            <div class="YDDetailStactDiv">
                <h2>${order.stateName}</h2>
                <h3>[<spring:message code="admin.withdraw.query.state"/>]</h3>
            </div>
            <fmt:formatDate value="${order.createDate}" var="createDate" pattern="yyyy-MM-dd" />
            <h1 class="YDDetailStactData">${createDate}</h1>
        </div>

        <div class="projectDetailHeadTitle">
            <h1>${order.orgName}</h1>
            <span><spring:message code="admin.withdraw.code"/>:<b>${order.orderNo}</b></span>
        </div>
        <div class="projectDetailHeadBody">
            <div class="projetListConFoot">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="admin.withdraw.money"/>:</td><th><b>${order.money}</b><span><spring:message code="common.unit.yuan"/></span></th>
                        <td><spring:message code="admin.withdraw.create_name"/>:</td><th><b>${order.createName}</b></th>
                        <td><spring:message code="admin.withdraw.create"/>:</td><th><b>${createDate}</b></th>
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
                <h1 class="userDetailConthText"><spring:message code="admin.withdraw.info"/></h1>
            </div>
            <div class="userDetail xw_pannal" style="padding:0px; min-height:120px; overflow:hidden;">
                <table class="datalistTable">
                    <thead>
                    <tr>
                        <th class="checkTh"><input id="checkAll" type="checkbox" /></th>
                        <th style="width: 2%;"><spring:message code="admin.withdraw.title.seq"/></th>
                        <th><spring:message code="admin.withdraw.title.year_month"/></th>
                        <th><spring:message code="admin.withdraw.title.money"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${order.monthClears}" varStatus="status">
                        <tr>
                            <td>
                                <input type="hidden" name="year" value="${record.year}"/>
                                <input type="hidden" name="month" value="${record.month}"/>
                                <input type="hidden" name="money" value="${record.applyMoney}"/>
                                <input type="checkbox" name="checkbox"/>
                            </td>
                            <td>${status.index + 1}</td>
                            <td>${record.year}.${record.month}</td>
                            <td>${record.applyMoney}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="order" value="${order.id}"/>
<input type="hidden" id="guid" value="${order.guid}"/>
<input type="hidden" id="wfCode" value="${wfCode}"/>
</body>
</html>
