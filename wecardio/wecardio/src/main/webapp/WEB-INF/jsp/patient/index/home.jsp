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
    <title>患者首页</title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/patientHome.css"/>
    <script type="text/javascript" src="${path}/resources/patient/index/js/home.js">
    </script>
    <script type="text/javascript">
        var iPatientId = 0;
        var comfireReleaseBinding = '<spring:message code="admin.message.patient.comfireReleaseBinding"/>';
    </script>
</head>
<body>
<div class="patient_home">

    <div class="homeBottom">
        <div class="yizhu xw_yizhu">
            <table>
                <th class="yizhu_tittle">
                    <spring:message code="patient.home.lbl.newestOpinion"/>
                </th>
                <td class="yizhu_xiaoxi" id="newestOpinion">

                </td>
            </table>
        </div>
        <div class="middle">
            <div class="shuju">
                <ul>
                    <li class="shuju_1">
                        <font><spring:message code="patient.home.lbl.patientMsg.heart"/>(bpm):<span></span></font>

                    </li>
                    <li class="shuju_1">
                        <font>PR(ms):<span></span></font>

                    </li>
                    <li class="shuju_1">
                        <font>QRS(ma):<span></span></font>

                    </li>
                    <li class="shuju_1">
                        <font>QT(ms):<span></span></font>

                    </li>
                    <li class="shuju_1">
                        <font>QTC(ms):<span></span></font>

                    </li>
                </ul>
            </div>
        </div>
        <div class="jiancha xw_jiancha" id="recordList">
            <table>
                <th class="jiancha_tittle" id="patientRecodeName">
                    <spring:message code="patient.home.lbl.patientMsg.recode"/>
                </th>
            </table>
        </div>
        <div class="huodong xw_huodong">
            <table>
                <th class="huodong_tittle">
                    <spring:message code="patient.home.lbl.newestActivity"/>
                </th>
                <td class="huodong_t">
                    <div class="talkBarDiv" id="newestActivity">

                    </div>
                </td>
            </table>
        </div>
        <div class="bingshi xw_bingshi" id="patientContainer">
            <table id="patientTitle">
                <th class="bingshi_tittle">
                    <spring:message code="patient.home.lbl.patientMsg"/>
                </th>
            </table>
        </div>
    </div>

</div>
<textarea id="templateList" rows="0" cols="0" style="display: none;">
    {{each}}
    <td class="jiancha_t xw_jiancha_t record" data-id="{{$value.id}}">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{$value.createDate}}
    </td>
    {{/each}}
</textarea>
<textarea rows="0" cols="0" style="display: none;" id="templateActivity">
    <div class="userpicDiv">
        <img src="${path}/file?path=" width="60" height="60"/>
        <a>{{create_name}}</a>
    </div>
    <div class="talkBarDivMain xw_talkBarDivMain">
        <div class="talkBarDivHead">
            <h1><spring:message code="patient.home.lbl.newestActivity.log"/></h1>

        </div>
        <div class="talkBarDivBody">
            <p>{{detail}}</p>
        </div>
        <div class="talkBarDivfoot">
            <font class="timeshowText">{{dateCreated}}</font>
        </div>
    </div>
</textarea>
<textarea rows="0" cols="0" id="templateNewestOpinion" style="display: none;">
    <div class="talkBarDiv">
        <div class="userpicDiv">
            <img src="${path}/file?path={{send_id.headPath}}" width="60" height="60"/>
            <a>{{send_id.name}}</a>
        </div>
        <div class="talkBarDivMain xw_talkBarDivMain">
            <div class="talkBarDivHead">
                <h1><spring:message code="patient.home.lbl.newestOpinion.log"/></h1>

            </div>
            <div class="talkBarDivBody">
                <p id="opinionLog">{{content}}</p>
            </div>
            <div class="talkBarDivfoot">
                <font class="timeshowText" id="opinionTime">{{dateCreated}}</font>
            </div>
        </div>
    </div>
</textarea>
<textarea rows="0" cols="0" style="display: none;" id="templatePatientMsg">
    <div class="userpicDiv" style="margin-top:40px;">
        <img src="${path}/file?path={{headPath}}" width="60" height="60"/>
        <a>{{name}}</a>
    </div>
    <div class="ziliao">
        <ul>
            <li><span><spring:message
                    code="patient.doctor.form.account"/>：</span><font>{{email}}</font></li>
            <li><span><spring:message
                    code="patient.home.lbl.patientMsg.sex"/>：</span>
                <font>
                    {{if sexName == 'male'}}<spring:message code="common.enum.sex.male"/>
                    {{else if sexName == 'female'}}<spring:message code="common.enum.sex.female"/>
                    {{else}}<spring:message code="common.enum.sex.other"/>{{/if}}
                </font>
            </li>
            <li><span><spring:message code="patient.organization.form.doctor.birthday"/>：</span>
                <font>{{dateBirthday}}</font></li>
            <li><span><spring:message code="patient.home.lbl.patientMsg.indication"/>：</span>
                <font class="huangse">{{indication}}</font></li>
                <li>
                    <a class="pingjia" id="editPatient"><spring:message code="admin.service.btn.edit.service"/></a>
                    {{if bindType == '1'}}
                    <a class="pingjia" id="releaseBinding"><spring:message code="admin.message.patient.releaseBinding"/></a>
                    {{/if}}
                </li>
        </ul>
    </div>
</textarea>
</body>
</html>
