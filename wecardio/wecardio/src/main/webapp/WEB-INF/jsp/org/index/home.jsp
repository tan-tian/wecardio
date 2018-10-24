<%--
  User: tantian
  Date: 2015/6/18
  Time: 16:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title>机构管理员首页</title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/indexa_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/js/echarts/echarts-all.js"></script>
    <script type="text/javascript" src="${path}/resources/admin/index/js/home.js"></script>
</head>
<body>
<input type="hidden" id="monthName" value="<spring:message code="home.chart.lbl.month"/>"/>
<input type="hidden" id="mountName"
       value="<spring:message code="home.mount.lbl.receiveAmount"/>,<spring:message code="home.mount.lbl.notReceivedAmount"/>,<spring:message code="home.mount.lbl.alreadyReceivedAmount"/>"/>

<div class="pageMain">
    <div class="dataTablediv">
        <table class="dataTable">
            <tr>
                <td>
                    <div class="dataDiv ico_doctor02" onclick="window.location.href='${path}/org/doctor?auditState=2';">
                        <h1 id="doctorCount">0</h1>

                        <h2><spring:message code="home.count.lbl.doctorCount"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_huanzhe02" onclick="window.location.href='${path}/org/patient?iType=2';">
                        <h1 id="patientCouont">0</h1>

                        <h2><spring:message code="home.count.lbl.patientCouont"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_fuwu02" onclick="window.location.href='${path}/org/consultation/list';">
                        <h1 id="consultationCount"></h1>

                        <h2><spring:message code="home.count.lbl.consultationCount"/></h2>
                    </div>
                </td>
                <th>
                    <div class="dataDiv ico_dingdan02"
                         onclick="window.location.href='${path}/org/service/toPage/packageMgr';">
                        <h1 id="servicePackageCount">0</h1>

                        <h2><spring:message code="home.count.lbl.servicePackageCount"/></h2>
                    </div>
                </th>
            </tr>
        </table>
    </div>
    <div class="chartDiv">
        <div class="topChartLMain">
            <div class="topChartLMainL">
                <h1 id="grossSalesTotal">0</h1>

                <h2><spring:message code="home.mount.lbl.grossSalesTotal"/></h2>
                <ul class="numberUl">
                    <li><spring:message code="home.mount.lbl.salesTotal"/>:<b id="salesTotal">0</b></li>
                    <li><spring:message code="home.mount.lbl.receiveAmount"/>:<b id="receiveAmount">0</b></li>
                    <li><spring:message code="home.mount.lbl.notReceivedAmount"/>:<b id="notReceivedAmount">0</b></li>
                    <li><spring:message code="home.mount.lbl.alreadyReceivedAmount"/>:<b
                            id="alreadyReceivedAmount">0</b></li>
                </ul>
            </div>
            <div class="topChartLMainR">
                <div class="lineChartB" id="chart" style="height:310px;"></div>
            </div>
        </div>
    </div>
    <div class="dingdanDiv">
        <div class="indexObjBox">
            <div class="indexObjBoxHead">
                    <h1><spring:message code="admin.service.lbl.pack.diagnosis"></spring:message></h1>
                    <a class="moreDD" href="${path}/org/consultation/list">[<spring:message code="common.consultation.more"></spring:message>]</a>
            </div>
            <div class="indexObjBoxBody" id="listConsultation" style="padding:10px 10px 0;">
            </div>
        </div>
    </div>
</div>
<textarea id="templateConsultation" rows="0" cols="0" style="display: none">
        {{each content as item}}
<div class="sgListLiMain xw_sgListLiMain" cid="{{item.id}}" guid="{{item.guid}}">
    <div class="listStatic">
        <h1>{{item.stateName}}</h1>

        <h2>[{{item.createDate.replace(/-/g, '.')}}]</h2>
    </div>
    <div class="sgListLiMainCenter xw_sgListLiMainCenter">
        <div class="sgListHead"><h1>{{item.serviceDate}} {{item.serviceTypeName}}</h1></div>
        <ul class="sgMsg">
                <li><span><spring:message code="org.consultation.list.patient"/>:</span><a>{{item.patientName}}</a></li>
                <li><span><spring:message code="org.consultation.list.doctor"/>:</span><a>{{item.doctorName}}</a></li>
                <li><span><spring:message code="org.consultation.list.org"/>：</span><a>{{item.orgName}}</a></li>
                <li><span><spring:message code="org.consultation.list.paykind"/>：</span><a>{{item.payKindName}}</a></li>
                <li><span><spring:message code="org.consultation.list.price"/>：</span><a>{{item.price}}</a></li>
        </ul>
    </div>
</div>
        {{/each}}
    {{if content.length == 0}}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {{/if}}
</textarea>
</body>
</html>
