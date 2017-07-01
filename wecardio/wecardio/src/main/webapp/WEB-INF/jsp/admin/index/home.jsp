<%--
  User: Sebarswee
  Date: 2015/6/18
  Time: 16:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title>平台管理员首页</title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/indexa_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdanlist_ctr.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/js/echarts/echarts-all.js"></script>
    <script type="text/javascript" src="${path}/resources/admin/index/js/home.js"></script>
</head>
<body>
<input type="hidden" id="monthName" value="<spring:message code="home.chart.lbl.month"/>"/>
<input type="hidden" id="mountName"
       value="<spring:message code="home.mount.lbl.payAmount"/>,<spring:message code="home.mount.lbl.notPayAmount"/>,<spring:message code="home.mount.lbl.alreadyPayAmount"/>"/>

<div class="pageMain">
    <div class="dataTablediv">
        <table class="dataTable">
            <tr>
                <td>
                    <div class="dataDiv ico_hospitalbg" onclick="window.location.href='${path}/admin/organization';">
                        <h1 id="orgCount">0</h1>

                        <h2><spring:message code="home.count.lbl.orgCount"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_doctor02" onclick="window.location.href='${path}/admin/doctor';">
                        <h1 id="doctorCount">0</h1>

                        <h2><spring:message code="home.count.lbl.doctorCount"/></h2>
                    </div>
                </td>
                <td>
                    <div class="dataDiv ico_huanzhe02" onclick="window.location.href='${path}/admin/patient?iType=2';">
                        <h1 id="patientCouont">0</h1>

                        <h2><spring:message code="home.count.lbl.patientCouont"/></h2>
                    </div>
                </td>
                <th>
                    <div class="dataDiv ico_fuwu02" onclick="window.location.href='${path}/admin/consultation/list';">
                        <h1 id="consultationCount"></h1>

                        <h2><spring:message code="home.count.lbl.consultationCount"/></h2>
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
                    <li><span title="<spring:message code="home.mount.lbl.salesTotal"/>"><spring:message code="home.mount.lbl.salesTotal"/></span><i>:</i><b id="salesTotal">0</b></li>
                    <li><span title="<spring:message code="home.mount.lbl.rateAmount"/>"><spring:message code="home.mount.lbl.rateAmount"/></span><i>:</i><b id="rateAmount">0</b></li>
                    <li><span title="<spring:message code="home.mount.lbl.payAmount"/>"><spring:message code="home.mount.lbl.payAmount"/></span><i>:</i><b id="payAmount">0</b></li>
                    <li><span title="<spring:message code="home.mount.lbl.notPayAmount"/>"><spring:message code="home.mount.lbl.notPayAmount"/></span><i>:</i><b id="notPayAmount">0</b></li>
                    <li><span title="<spring:message code="home.mount.lbl.alreadyPayAmount"/>"><spring:message code="home.mount.lbl.alreadyPayAmount"/></span><i>:</i><b id="alreadyPayAmount">0</b></li>
                </ul>
            </div>
            <div class="topChartLMainR">
                <div class="lineChartB" id="chart" style="height:310px;"></div>
            </div>
        </div>
    </div>

    <div class="dingdanDiv">
        <div class="indexObjBox xw_indexObjBox">
            <div class="objBoxHead objBoxHeadTab ">
                <ul class="objBoxHeadTabUl xw_objBoxHeadTabUl">
                    <li class="lion"><spring:message code="home.tab.name.orgAudit"/></li>
                    <li><spring:message code="home.tab.name.docAudit"/></li>
                </ul>
            </div>

            <div class="indexObjBoxBody xw_indexObjBoxBody" id="listOrg" style="padding:10px 10px 0;">

            </div>

            <div class="indexObjBoxBody xw_indexObjBoxBody" id="listDoctor" style="padding:10px 10px 0; display:none;">

            </div>
        </div>
    </div>
</div>
<textarea id="templateOrg" style="display: none;">
        {{each content as item}}
       <div class="sgListLiMain xw_sgListLiMain" oid="{{item.id}}" guid="{{item.guid}}">
           <div class="listStatic color02">
               <h1><spring:message code="admin.organization.list.status.toAudit"/></h1>

               <h2>[{{item.modifyDate.replace(/-/g, '.')}}]</h2>
           </div>
           <div class="sgListLiMainCenter xw_sgListLiMainCenter1">
               <div class="sgListHead"><h1>{{item.name}}</h1><span><spring:message code="admin.organization.list.code"/>:<b>{{item.code}}</b></span>
               </div>
               <ul class="sgMsg">
                   <li><span><spring:message code="admin.organization.list.type"/>:</span><a>{{item.typeName}}</a>
                   </li>
                   <li><span><spring:message
                           code="admin.organization.list.username"/>:</span><a>{{item.username}}</a></li>
                   <li><span><spring:message
                           code="admin.organization.list.applyDate"/>:</span><a>{{item.modifyDate}}</a></li>
                   <li><span><spring:message
                           code="admin.organization.list.doctorName"/>:</span><a>{{item.doctorName}}</a>
                   </li>
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

<textarea id="templateDoctor" style="display: none;">
        {{each content as item}}
       <div class="sgListLiMain xw_sgListLiMain" did="{{item.id}}">
           <div class="listStatic color02">
               <h1><spring:message code="admin.doctor.list.status.toAudit"/></h1>

               <h2>[{{item.createDate.replace(/-/g, '.')}}]</h2>
           </div>
           <div class="sgListLiMainCenter xw_sgListLiMainCenter2">
               <div class="sgListHead"><h1>{{item.name}}</h1></div>
               <ul class="sgMsg">
                   <li><span><spring:message code="admin.doctor.list.org"/>:</span><a>{{item.orgName}}</a></li>
                   <li><span><spring:message code="admin.doctor.list.org.type"/>:</span><a>{{item.orgTypeName}}</a>
                   </li>
                   <li><span><spring:message code="admin.doctor.list.org.contact"/>:</span><a>{{item.orgContact}}</a>
                   </li>
                   <li><span><spring:message code="admin.doctor.list.applyDate"/>:</span><a>{{item.createDate}}</a>
                   </li>
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
