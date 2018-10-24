<%--
  Created by IntelliJ IDEA.
  User: tantian
  Date: 2015/8/7
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.service.query.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/commonstyle.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/forum/js/index.js"></script>
</head>
<body>
<input type="hidden" id="score" value="<spring:message code="admin.forum.lbl.scoreLbl"/>"/>
<input type="hidden" id="all" value="<spring:message code="admin.forum.lbl.all"/>"/>

<div class="BodyMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.main.evaluateNav"/></span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>
    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="form0" name="form0">
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" id="content" name="content" style="width:200px;"
                           placeholder="<spring:message code="admin.forum.form.txt.search"/> "/>
                    <a></a>
                </div>

                <div class="search_type" style="padding-left:23px; padding-top:0px; padding-right:0px;">
                    <spring:message code="admin.forum.lbl.time"/> ：
                </div>
                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" name="createdStart" id="createdStart"
                           onclick="WdatePicker({el:'createdStart',maxDate:'#F{$dp.$D(\'createdEnd\')||\'1970-00-00\'}'})"/>
                </div>
                <span class="formText" style="margin:8px 10px 0;">~</span>

                <div class="dateDiv">
                    <input class="zjInput" type="text" style="width:140px;" id="createdEnd" name="createdEnd"
                           onclick="WdatePicker({el:'createdEnd',minDate:'#F{$dp.$D(\'createdStart\')||\'1970-00-00\'}'})"/>
                </div>

                <div class="searchBtMain">
                    <a class="subBt btnQuery"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt btnReset"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>
        </form>
        <div class="khKmainbody xw_kMainContentbody"
             style=" background:#F4F2F3;min-height:75px; padding:0px; border-top:0px;display:none;">
            <form id="form1" name="form1">
                <div class="searchFormChose">
                    <dl>
                        <dt><spring:message code="admin.forum.lbl.score"/>：</dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="scores" name="scores"/>
                        </dd>
                    </dl>

                </div>
                <div class="searchFormBtMain">
                    <ul class="toolBarList" style="float:none; margin:0 auto; width:150px; overflow:hidden;">
                        <li class="btnReset"><a><spring:message code="common.btn.reset"/></a></li>
                        <li class="btnQuery"><a><spring:message code="common.btn.query"/></a></li>
                    </ul>
                </div>
            </form>
        </div>
        <div class="searchFormFoot">
            <a class="moresearchForm">
                <span class="acc-openbar-txt"><spring:message code="common.btn.slidedown"/> </span>
                <span class="acc-openbar-txtb"><spring:message code="common.btn.slideup"/></span>
                <spring:message code="common.btn.more"/>
            </a>
        </div>
    </div>


    <div class="titleBar">
        <spring:message code="admin.forum.lbl.count"/>
    </div>

    <div id="list" class="muluList" style="min-height:480px;">

    </div>

    <ui:pageBar id="pageBar"/>


</div>
<textarea id="templateList" style="display: none;">
        {{each content as item}}
         <div class="talkBarDiv1">
             <div class="userpicDiv1">
                 <img src="${path}/file?path={{item.createIdHeadPath}}" width="60" height="60"/>
                 <a>{{item.createName}}</a>
             </div>

             <div class="talkBarDiv1Main xw_talkBarDivMain">
                 <div class="talkBarDivHead">
                     <h1><span class="haoping">&nbsp;&nbsp;&nbsp;&nbsp;</span>{{item.score}}<spring:message
                             code="admin.forum.lbl.scoreLbl"/>,{{item.orgName}}</h1>
                     <a href="${path}/${sessionScope.userTypePath}/consultation/{{item.consultationId}}/view"><spring:message code="admin.forum.lbl.consultationId"/> ：{{item.consultationId}}</a>
                 </div>
                 <div class="talkBarDivBody">
                     <p>{{item.content}}</p>
                 </div>
                 <div class="talkBarDivfoot">
                     <font class="timeshowText">{{item.createdDate}}</font>
                 </div>
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
