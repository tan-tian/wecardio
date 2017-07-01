<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
        <%@include file="/common/headModule.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <%--<title><spring:message code="admin.patient.detail.title"/></title>--%>
        <link rel="stylesheet" type="text/css" href="${path}/resources/css/userDetails.css"/>
        <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdandetail_style.css"/>
        <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdanlist_ctr.css"/>
        <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css"/>
        <style type="text/css">
            .chartDtList{
                cursor: pointer;
            }
        </style>
        <script type="text/javascript" src="${path}/resources/js/echarts/echarts-all.js"></script>
        <script type="text/javascript">
                var iId = "${entity.id}";
                var userType = '${userType}';
                var userPath = '${sessionScope.userTypePath}';
                var noData = "<spring:message code="common.message.noData"/>";
                var recordTitle = "<spring:message code="admin.patient.list.recordTitle"/>";
                var addConsultation = "<spring:message code="admin.patient.list.addConsultation"/>";
        </script>
        <script type="text/javascript" src="${path}/resources/admin/patient/js/detail.js"></script>

</head>

<body>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" var="nowFmt" pattern="yyyy-MM"/>

<input type="hidden" value="时间" id="timeLbl"/>
<input type="hidden" value="通道" id="stLbl"/>
<input type="hidden" name="selectTime" id="selectTime" value="false"/>

<div class="userDetails">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/patient?iType=${iType}"><spring:message
                code="admin.patient.title"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.patient.form.detail"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>

        <form id="qform" action="" method="post">
            <input type="hidden" id="iId" name="iId" value="${entity.id}"/>
            <input type="hidden" id="pId" name="pId" value="${entity.id}"/>
            <input type="hidden" id="patientId" name="patientId" value="${entity.id}"/>
            <input type="hidden" id="pageSize" name="pageSize" value="5"/>
        </form>
        <form id="qAdviceform" action="" method="post">
            <input type="hidden" id="advicePageNo" name="pageNo" value="1"/>
        </form>
        <form id="qCheckRecordform" action="" method="post">
            <input type="hidden" id="checkRecordPageNo" name="pageNo" value="1"/>
        </form>
        <form id="qConsultationform" action="" method="post">
            <input type="hidden" id="consultationPageNo" name="pageNo" value="1"/>
        </form>
        <form id="qRecordform" action="" method="post">
            <input type="hidden" id="recordPageNo" name="pageNo" value="1"/>
        </form>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <c:if test="${sessionScope.userTypePath =='org' || sessionScope.userTypePath == 'doctor'}">
                <li><a id="addAdviceBtn"><spring:message code="common.btn.patient.addAdvice"/></a></li><%--新增医嘱--%>
            </c:if>
            <c:if test="${sessionScope.userTypePath =='org' || sessionScope.userTypePath == 'doctor' || sessionScope.userTypePath == 'patient'}">
                <li><a id="actRecordBtn"><spring:message code="common.btn.patient.actRecord"/></a></li><%--活动记录--%>
            </c:if>
            <c:if test="${sessionScope.userTypePath == 'doctor'}">
                <li><a id="totalBtn" href="${path}/doctor/consultation/total?pId=${entity.id}"><spring:message code="admin.patient.detail.statis.total"/></a></li><%--新增医嘱--%>
            </c:if>
            <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li><%--取消--%>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>

    <div class="detailsTop">
        <div class="detailsTop_a">
            <c:if test="${entity.headPath == ''||entity.headPath==null}">
                <img src="${path}/resources/images/userdetails/touxiang.jpg" width="260" height="260"/>
            </c:if>
            <c:if test="${entity.headPath != ''&&entity.headPath != null}">
                <img src="${path}/file?path=${entity.headPath}" width="260" height="260"/>
            </c:if>
        </div>
        <table>
            <tr class="tableTittle">
                <td><spring:message code="admin.patient.detail.name"/>：</td>
                <th>${entity.fullName}</th>
                <td><spring:message code="admin.patient.detail.sysAccount"/>：</td>
                <th>
                    <c:choose>
                        <c:when test="${not empty entity.email}">
                            ${entity.email}
                        </c:when>
                        <c:otherwise>
                            ${entity.mobile}
                        </c:otherwise>
                    </c:choose>
                </th>
            </tr>
            <tr>
                <td><spring:message code="admin.patient.detail.sex"/>：</td>
                <th><spring:message code="common.enum.sex.${entity.sexName}"/></th>
                <fmt:formatDate value="${entity.dateBirthday}" var="dateBirthday" pattern="yyyy-MM-dd"/>
                <td><spring:message code="admin.patient.detail.birthday"/>：</td>
                <th>${dateBirthday}</th>
            </tr>
            <tr>
                <td><spring:message code="admin.patient.detail.mobile"/>：</td>
                <th>${entity.mobile}</th>
                <td><spring:message code="admin.patient.detail.address"/>：</td>
                <th>${entity.address}</th>
            </tr>
            <tr>
                <td><spring:message code="admin.patient.detail.org"/>：</td>
                <th>
                    <c:if test="${entity.org.id!=0}">
                        ${entity.org.name}
                    </c:if>
                </th>
                <td><spring:message code="admin.patient.detail.doctor"/>：</td>
                <th>
                    <c:if test="${entity.doctor.id!=0}">
                        ${entity.doctor.name}
                    </c:if>
                </th>
            </tr>
            <tr>
                <td><spring:message code="admin.patient.detail.indication"/>：</td>
                <th colspan="3"><span>${entity.indication}</span></th>
            </tr>
            <tr>
                <td><spring:message code="admin.patient.detail.medicineSituation"/>：</td>
                <th colspan="3">${entity.medicine}</th>
            </tr>
        </table>
    </div>

        <div class="userMain" style="margin-bottom:10px;">
                <div class="userDetailCont" style="padding-top:0px;">

                        <div class="userDetailContHead">
                                <h1 class="userDetailConthText"><spring:message code="admin.patient.detail.statis.title"/></h1>
                            <ul class="objBoxHeadTabUl xw_objBoxHeadTabUl xw_objBoxHeadTab tab">
                                        <li class="lion" data-type-value="1"><spring:message
                                                code="admin.patient.detail.statis.type01"/></li>
                                        <li data-type-value="3"><spring:message code="admin.patient.detail.statis.type02"/></li>
                                        <li data-type-value="5"><spring:message code="admin.patient.detail.statis.type03"/></li>
                            </ul>

                        </div>

                        <div class="userDetail xw_userDetailA chartsContainer">
                                <div class="list_timeBar">
                                        <ul class="list_time xw_listTime timeType">
                                                <spring:message code="admin.patient.detail.timeTypes"/>
                                        </ul>
                                        <div style=" float:left; padding-top:10px;">
                                            <div class="dateDiv">
                                                <input name="startTime0" class="zjInput" type="text" style="width:120px; height:16px;" id="startTime0"
                                                       onclick="WdatePicker({el:'startTime0',dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'endTime0\',{M:-6})||\'${nowFmt}\'}',maxDate:'#F{$dp.$D(\'endTime0\',{M:-1})||\'${nowFmt}\'}'})" />
                                            </div>
                                            <span class="formText" style="margin:8px 10px 0;">~</span>
                                            <div class="dateDiv">
                                                <input name="endTime0" value="${nowFmt}" class="zjInput" type="text" style="width:120px; height:16px;" id="endTime0"
                                                       onclick="WdatePicker({el:'endTime0',isShowClear:false,dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'startTime0\',{M:+1})||\'${nowFmt}\'}',maxDate:'%y-{%M}'})" />
                                            </div>
                                        </div>
                                    <ul style="float:left;margin-top:10px; margin-left: 10px;" class="toolBarList">
                                        <li class="btnQuery"><a><spring:message code="common.btn.query1"/> </a></li>
                                    </ul>
                                        <a class="icoA ico_list"></a>
                                </div>
                                <div class="table_xindian charts">
                                        <div id="chart00" style="height: 220px;">

                                        </div>
                                        <div id="chart01" style="height:270px;">

                                        </div>

                                        <div id="chart02" style="height:270px;">

                                </div>
                                </div>
                                <div class="table_xindian list" style="display:none;">
                                        <table class="datalistTable">
                                                <thead>
                                                <th class="checkTh"></th>
                                                <th><spring:message code="admin.patient.detail.col.time"/></th>
                                                <th>HR(bpm)</th>
                                                <th>QT(ms)</th>
                                                <th>QRS(ms)</th>
                                                <th>PR(ms)</th>
                                                <th>QTC(ms)</th>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                        </table>
                                </div>
                        </div>
                        <div class="userDetail xw_userDetailA chartsContainer" style="display:none;">
                            <div class="list_timeBar">
                                <ul class="list_time xw_listTime timeType">
                                    <spring:message code="admin.patient.detail.timeTypes"/>
                                </ul>
                                <div style=" float:left; padding-top:10px;">
                                    <div class="dateDiv">
                                        <input name="startTime1" class="zjInput" type="text" style="width:120px; height:16px;" id="startTime1"
                                               onclick="WdatePicker({el:'startTime1',dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'endTime1\',{M:-6})||\'${nowFmt}\'}',maxDate:'#F{$dp.$D(\'endTime1\',{M:-1})||\'${nowFmt}\'}'})" />
                                    </div>
                                    <span class="formText" style="margin:8px 10px 0;">~</span>
                                    <div class="dateDiv">
                                        <input name="endTime1" value="${nowFmt}" class="zjInput" type="text" style="width:120px; height:16px;" id="endTime1"
                                               onclick="WdatePicker({el:'endTime1',isShowClear:false,dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'startTime1\',{M:+1})||\'${nowFmt}\'}',maxDate:'%y-{%M}'})" />
                                    </div>
                                </div>
                                <ul style="float:left;margin-top:10px; margin-left: 10px;" class="toolBarList">
                                    <li class="btnQuery"><a><spring:message code="common.btn.query1"/> </a></li>
                                </ul>
                                <a class="icoA ico_list"></a>
                            </div>
                                <div class="table_xindian charts">
                                        <div id="chart10" style="height:280px;">

                                        </div>
                                        <div id="chart11" style="height:280px;">

                                        </div>
                                        <div id="chart12" style="height:280px;">

                                        </div>
                                        <div id="chart13" style="height:280px;">

                                        </div>
                                        <div id="chart14" style="height:280px;">

                                        </div>
                                        <div id="chart15" style="height:280px;">

                                        </div>
                                </div>
                                <div class="table_xindian list" style="display:none;">
                                        <table class="datalistTable">
                                                <thead>
                                                <th class="checkTh"></th>
                                                <th><spring:message code="admin.patient.detail.col.time"/></th>
                                                <th>HR(bpm)</th>
                                                <th>RR(ms)</th>
                                                <th>PR(ms)</th>
                                                <th>QRS(ms)</th>
                                                <th>QT(ms)</th>
                                                <th>QTC(ms)</th>
                                                <th>I</th>
                                                <th>II</th>
                                                <th>V1</th>
                                                <th>V2</th>
                                                <th>V4</th>
                                                <th>V6</th>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                        </table>
                                </div>
                        </div>
                        <div class="userDetail xw_userDetailA chartsContainer" style="display:none;">
                            <div class="list_timeBar">
                                <ul class="list_time xw_listTime timeType">
                                    <spring:message code="admin.patient.detail.timeTypes"/>
                                </ul>

                                <div style=" float:left; padding-top:10px;">
                                    <div class="dateDiv">
                                        <input name="startTime2" class="zjInput" type="text" style="width:120px; height:16px;" id="startTime2"
                                               onclick="WdatePicker({el:'startTime2',dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'endTime2\',{M:-6})||\'${nowFmt}\'}',maxDate:'#F{$dp.$D(\'endTime2\',{M:-1})||\'${nowFmt}\'}'})" />
                                    </div>
                                    <span class="formText" style="margin:8px 10px 0;">~</span>
                                    <div class="dateDiv">
                                        <input name="endTime2" value="${nowFmt}" class="zjInput" type="text" style="width:120px; height:16px;" id="endTime2"
                                               onclick="WdatePicker({el:'endTime2',isShowClear:false,dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'startTime2\',{M:+1})||\'${nowFmt}\'}',maxDate:'%y-{%M}'})" />
                                    </div>
                                </div>
                                <ul style="float:left;margin-top:10px; margin-left: 10px;" class="toolBarList">
                                    <li class="btnQuery"><a><spring:message code="common.btn.query1"/> </a></li>
                                </ul>
                                <a class="icoA ico_list"></a>
                            </div>
                                <div class="table_xindian charts">
                                        <div id="chart20" style="height:280px;">

                                        </div>
                                        <div id="chart21" style="height:280px;">

                                        </div>
                                        <div id="chart22" style="height:280px;">

                                        </div>
                                        <div id="chart23" style="height:280px;">

                                        </div>
                                </div>
                                <div class="table_xindian list" style="display:none;">
                                        <table class="datalistTable">
                                                <thead>
                                                <th class="checkTh"></th>
                                                <th><spring:message code="admin.patient.detail.col.time"/></th>
                                                <th>HR(bpm)</th>
                                                <th>PSI(ms)</th>
                                                <th>SDNN(ms)</th>
                                                <th>RMSSD(ms)</th>
                                                <th>VLF(ms)</th>
                                                <th>LF</th>
                                                <th>HF</th>
                                                <th>N.U</th>
                                                </thead>
                                                <tbody></tbody>
                                        </table>
                                </div>
                        </div>
                </div>
        </div>


    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.patient.detail.adviceList"/>（<spring:message
                        code="common.toolbar.total.title"/><font id="adviceTotalNum">0</font><spring:message
                        code="common.toolbar.bill.title"/>）</h1>
            </div>
            <div class="yizhuliebiao" id="advicelist">
            </div>
            <div id="advicePageBar" class="advicePageBar"
                 style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
        </div>
    </div>


    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message
                        code="admin.patient.detail.checkRecord"/>（<spring:message
                        code="common.toolbar.total.title"/><font id="checkRecordTotalNum">0</font><spring:message
                        code="common.toolbar.bill.title"/>）</h1>
                <a href="${path}/${userType}/record?pId=${entity.id}"><spring:message
                        code="admin.patient.detail.more"/></a>
            </div>
            <div class="jiachajilu" id="checkRecordlist">
            </div>
            <div id="checkRecordPageBar" class="checkRecordPageBar"
                 style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message
                        code="admin.patient.detail.consultationManage"/>（<spring:message
                        code="common.toolbar.total.title"/><font id="consultationTotalNum">0</font><spring:message
                        code="common.toolbar.bill.title"/>）</h1>
                <a href="${path}/${userType}/consultation/list?pId=${entity.id}"><spring:message
                        code="admin.patient.detail.more"/></a>
            </div>
            <div class="jiachajilu" id="consultationlist">
            </div>
            <div id="consultationPageBar" class="consultationPageBar"
                 style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message
                        code="admin.patient.detail.adviceRecord"/>（<spring:message
                        code="common.toolbar.total.title"/><font id="adviceRecordTotalNum">0</font><spring:message
                        code="common.toolbar.bill.title"/>）</h1>
            </div>
            <div class="yizhuliebiao" id="recordlist">
            </div>
            <div id="recordPageBar" class="recordPageBar"
                 style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
        </div>
    </div>
</div>

     <textarea id="adviceTemplate" rows="0" cols="0" style="display: none">
         <div class="talkBarDiv1">
             <div class="userpicDiv1">
                 <img src="{#if $T.headPath != null && $T.headPath != ''}${path}/file?path={$T.headPath}{#else}${path}/resources/images/user/user01.png{#/if}"
                      alt=""/>
                 <a>{$T.send_id.name}</a>
             </div>
             <div class="talkBarDiv1Main xw_talkBarDivMain">
                 <div class="talkBarDivBody">
                     <p>{$T.content}</p>
                 </div>
                 <div class="talkBarDivfoot">
                     <font class="timeshowText">{$T.dateCreated}</font>
                 </div>
             </div>
         </div>
     </textarea>

     <textarea id="recordTemplate" rows="0" cols="0" style="display: none">
         <div class="talkBarDiv1">
             <div class="userpicDiv1">
                 <img src="{#if $T.headPath != null && $T.headPath != ''}${path}/file?path={$T.headPath}{#else}${path}/resources/images/user/user01.png{#/if}"
                      width="60" height="60"/>
                 <a>{$T.create_name}</a>
             </div>
             <div class="talkBarDiv1Main xw_talkBarDivMain">
                 <div class="talkBarDivBody">
                     <p>{$T.detail}</p>
                 </div>
                 <div class="talkBarDivfoot">
                     <font class="timeshowText">{$T.dateCreated}</font>
                 </div>
             </div>
         </div>
     </textarea>

     <textarea id="checkRecordTemplate" rows="0" cols="0" style="display: none">
         <div class="sgListLiMain xw_sgListLiMain">
             <div class="listStatic {#if !$T.isCommit}color02{#/if}">
                 <h1>
                     {#if !$T.isCommit}<spring:message code="org.record.list.state.todo"/>{#else}<spring:message
                         code="org.record.list.state.done"/>{#/if}
                 </h1>

                 <h2>[{$T.createDate}]</h2>
             </div>
             <div class="sgListLiMainCenter xw_sgListLiMainCenter_Record" id="{$T.id}">
                 <div class="sgListHead"><h1>{$T.testDate} {$T.serviceTypeName}</h1></div>
                 <ul class="sgMsg">
                     <li><span><spring:message code="admin.patient.detail.checkRecordType"/>:</span><a>{$T.serviceTypeName}</a>
                     </li>
                     <li><span><spring:message code="admin.patient.detail.submiter"/>:</span><a>{$T.doctorName}</a></li>
                     <li><span><spring:message code="admin.patient.detail.testTime"/>:</span>
                             {$T.testDate}
                     </li>
                 </ul>
                 <img src="${path}/resources/images/heartIco/{#if $T.stoplight == '1'}xin1{#elseif $T.stoplight == '2'}xin3{#else}xin2{#/if}.png"
                      width="24" height="24" alt="" style="float:right;"/>
             </div>
         </div>
     </textarea>

     <textarea id="consultationTemplate" rows="0" cols="0" style="display: none">
         <div class="sgListLiMain xw_sgListLiMain">
             <div class="listStatic {#if $T.state == 0}color02{#else}{#/if}
             ">
                 <h1>{$T.stateName}</h1>

                 <h2>[{$T.createDate}]</h2>
             </div>
             <div class="sgListLiMainCenter xw_sgListLiMainCenter_Consultation" id="{$T.id}">
                 <div class="sgListHead"><h1>{$T.verdict}</h1></div>
                 <ul class="sgMsg">
                     <li><span><spring:message code="admin.patient.detail.consultationType"/>:</span><a>{$T.serviceTypeName}</a>
                     </li>
                     <li><span><spring:message code="admin.patient.detail.submiter"/>:</span><a>{$T.patientName}</a>
                     </li>
                     <li><span><spring:message code="admin.patient.detail.doctor"/>:</span><a>{$T.doctorName}</a></li>
                     <li><span><spring:message code="admin.patient.detail.submitTime"/>:</span><a><a>{$T.createDate}</a></a>
                     </li>
                     <li><span><spring:message code="admin.patient.detail.org"/></span><a><a>{$T.orgName}</a></a></li>
                 </ul>
             </div>
         </div>
     </textarea>

<textarea rows="0" cols="0" id="templateList0" style="display: none;">
        {{each data.l}}
<tr data-value-id="{{$value.id}}" class="chartDtList">
    <td><img
            src="${path}/resources/images/heartIco/{{if $value.s == 0}}xin2{{else if $value.s == 1}}xin1{{else}}xin3{{/if}}.png"
            width="28" height="28"/></td>
    <td>{{$value.t | dateFormat}}</td>
    {{if $value.activity}}
    <td colspan="5">{{$value.activity}}</td>
    {{else}}
    <td>{{$value.hr}}</td>
    <td>{{$value.qt}}</td>
    <td>{{$value.qrs}}</td>
    <td>{{$value.pr}}</td>
    <td>{{$value.qtc}}</td>
    {{/if}}
</tr>
        {{/each}}
</textarea>
<textarea rows="0" cols="0" id="templateList1" style="display: none;">
{{each data.l}}
<tr data-value-id="{{$value.id}}" class="chartDtList">
    <td><img
            src="${path}/resources/images/heartIco/{{if $value.s == 0}}xin2{{else if $value.s == 1}}xin1{{else}}xin3{{/if}}.png"
            width="28" height="28"/></td>
    <td>{{$value.t | dateFormat}}</td>
    {{if $value.activity}}
    <td colspan="12">{{$value.activity}}</td>
    {{else}}
    <td>{{$value.hr}}</td>
    <td>{{$value.rr}}</td>
    <td>{{$value.pr}}</td>
    <td>{{$value.qrs}}</td>
    <td>{{$value.qt}}</td>
    <td>{{$value.qtc}}</td>
    <td>{{$value.st && $value.st.i}}</td>
    <td>{{$value.st && $value.st.ii}}</td>
    <td>{{$value.st && $value.st.v1}}</td>
    <td>{{$value.st && $value.st.v2}}</td>
    <td>{{$value.st && $value.st.v4}}</td>
    <td>{{$value.st && $value.st.v6}}</td>
    {{/if}}
</tr>
        {{/each}}
</textarea>
<textarea rows="0" cols="0" id="templateList2" style="display: none;">
{{each data.l}}
<tr data-value-id="{{$value.id}}" class="chartDtList">
    <td><img
            src="${path}/resources/images/heartIco/{{if $value.s == 0}}xin2{{else if $value.s == 1}}xin1{{else}}xin3{{/if}}.png"
            width="28" height="28"/></td>
    <td>{{$value.t | dateFormat}}</td>
    {{if $value.activity}}
    <td colspan="7">{{$value.activity}}</td>
    {{else}}
    <td>{{$value.hr}}</td>
    <td>{{$value.psi}}</td>
    <td>{{$value.rmssd}}</td>
    <td>{{$value.vlf}}</td>
    <td>{{$value.lf}}</td>
    <td>{{$value.hf}}</td>
    <td>{{$value.hfnu}}</td>
    {{/if}}
</tr>
        {{/each}}
</textarea>
</body>
</html>
