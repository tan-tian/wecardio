<%--
  Created by IntelliJ IDEA.
  User: Zhang zhongtao
  Date: 2015/10/19
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
    <title><spring:message code="admin.opinion.list.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/dingdanlist_ctr.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/customerbaifang_style.css"/>
    <style type="text/css">
        div.talkBarDiv1 {
            padding-left: 0px;
            margin-bottom: 5px;
            margin-top: 0px;
            min-height: 35px;
        }

        font.timeshowText {
            float: left;
            height: 24px;
            line-height: 24px;
            font-family: 微软雅黑;
            letter-spacing: 1px;
            font-size: 12px;
            color: #333;
            text-align: center;
        }

        div.talkBarDivBody {
            clear: both;
            height: 30px;
            padding: 5px;
            color: #858484;
        }

        div.talkBarDiv1Main {
            margin-left: 0px;
        }
    </style>
    <script type="text/javascript" src="${path}/resources/admin/opinion/js/list.js"></script>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
</head>
<body>
<div class="pageMain">

    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.opinion.list.title"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="form0" class="formQuery">
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" name="content" style="width:200px;"
                           placeholder="<spring:message code="common.btn.query1"/>"/>
                    <a class="btnQuery"></a>
                </div>

                <div class="searchBtMain">
                    <a class="subBt btnQuery"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>
        </form>
        <form id="form1" class="formQuery">
            <c:if test="${sessionScope.userTypePath!='doctor'}">
                <div class="khKmainbody xw_kMainContentbody"
                     style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">

                    <div class="searchFormChose" id="select">
                        <shiro:hasAnyRoles name="admin,patient">
                        <div class="searchBar" style=" position:relative;z-index:2;">
                            <span class="spanName"><spring:message code="admin.patient.list.org"/></span>
                            <input type="hidden" class="select" id="org" name="org"/>
                        </div>
                        </shiro:hasAnyRoles>
                        <shiro:hasAnyRoles name="admin,patient,org">
                        <div class="searchBar" style=" position:relative;z-index:3;">
                            <span class="spanName"><spring:message code="admin.patient.list.doctor"/></span>
                            <input type="hidden" class="select" id="doctor" name="doctor"/>
                        </div>
                        </shiro:hasAnyRoles>
                    </div>
                    <div class="searchFormBtMain">
                        <ul class="toolBarList" style="float:none; margin:0 auto; width:150px; overflow:hidden;">
                            <li class="btnRest"><a><spring:message code="common.btn.reset"/></a></li>
                            <li class="btnQuery"><a><spring:message code="common.btn.query"/></a></li>
                        </ul>
                    </div>
                </div>

                <div class="searchFormFoot">
                    <a class="moresearchForm">
                        <span class="acc-openbar-txt"><spring:message code="common.btn.slidedown"/></span>
                        <span class="acc-openbar-txtb"><spring:message code="common.btn.slideup"/></span><spring:message
                            code="common.btn.more"/>
                    </a>
                </div>
            </c:if>
        </form>
    </div>

    <div class="titleBar">
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font
                id="lblCount">0</font><spring:message code="common.toolbar.bill.title"/></h1>
    </div>

    <div class="dataList" style="min-height:610px;">
        <div id="list" class="muluList"></div>
        <ui:pageBar id="pageBar"/>
    </div>
</div>

<textarea style="display: none;" id="templateList">
     {{each content as item}}
        <div class="talkBarDiv1">
            <div class="talkBarDiv1Main xw_talkBarDivMain">
                <div class="talkBarDivBody"><p>{{item.content}}</p></div>
                <div class="talkBarDivfoot">
                    <ul class="sgMsg">
                        <li><span><spring:message code="admin.main.role.patient"/>:</span><a>{{item.receive_id.fullName
                            || '-'}}</a></li>
                        <li><span><spring:message code="admin.record.list.org"/>:</span><a>{{item.send_id.orgName ||
                            '-'}}</a>
                        </li>
                        <li><span><spring:message code="admin.main.role.doctor"/>:</span><a>{{item.send_id.name ||
                            '-'}}</a>
                        </li>
                        <li><span><spring:message
                                code="admin.message.list.sendTime"/>:</span><a>{{item.dateCreated}}</a>
                        </li>
                    </ul>
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
