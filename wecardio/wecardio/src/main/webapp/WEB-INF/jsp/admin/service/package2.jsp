<%--
  Created by IntelliJ IDEA.
  User: Zhang zhongtao
  Date: 2015/7/27
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.service.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/commonstyle.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/chose_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addservibao_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
        var remarkTitle = '<spring:message code="admin.service.title.remark"/>';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/package2.js"></script>
</head>
<body>
<input type="hidden" id="msg" name="msg" value="<spring:message code="common.message.select.data"/>"/>
<input type="hidden" id="title" name="title" value="<spring:message code="common.message.warn"/>"/>
<input type="hidden" id="confirm" name="confirm" value="<spring:message code="common.message.confirm.delete"/>"/>

<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
            <spring:message code="admin.service.lbl.packMgr"/>
        </a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/package1"><spring:message code="admin.service.lbl.base"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.lbl.cfg"/> </span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="flowMainDiv">
        <div class="flowMain">
            <table class="flowMainTable">
                <tr>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>1</a>

                            <h1><spring:message code="admin.service.lbl.base"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox flowBoxOn"><a>2</a>

                            <h1><spring:message code="admin.service.lbl.cfg"/></h1></div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox"><a>3</a>

                            <h1><spring:message code="admin.service.lbl.success"/></h1></div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:66%;"></a></h1></div>
        </div>
    </div>


    <div class="pageMainB">

        <div class="leftMain">

            <div class="objBoxContB" style=" margin-bottom:0px;">
                <div class="objBoxContBHead">
                    <h1 class="objBoxContBhText"><spring:message code="admin.service.lbl.select.service"/></h1>
                </div>
                <div class="objBoxContBBody" style="background:#fff; padding:0px 0px 0;">

                    <div class="shebeiBoxTitle">
                        <form id="form" name="form" id="form">
                            <input type="hidden" name="isEnableds" value="true"/>

                            <div class="crsearch" style=" margin-right:0px;margin-left:5px;">
                                <input type="text" style="width:150px;" id="name" name="name"
                                       placeholder="<spring:message code="admin.service.query.name"/> "/>
                                <a id="btnQuery"></a>
                            </div>
                        </form>
                    </div>
                    <div class="datalistMain" style="height:410px; padding:0; overflow:hidden;">
                        <table class="datalistTable">
                            <thead>
                            <th class="checkTh"><input id="allChecked" type="checkbox"/></th>
                            <th><spring:message code="admin.service.form.lbl.name"/></th>
                            </thead>
                            <tbody id="itemList"></tbody>
                        </table>
                    </div>
                    <ui:pageBar id="pageBar" showNumInput="false" entriesNum="3"/>

                </div>
            </div>

        </div>


        <div class="rightMain xw_rightMain">


            <div class="objBoxContB" style=" margin-bottom:0px; border-left:1px solid #eeeeee;">
                <div class="objBoxContBHead">
                    <h1 class="objBoxContBhText"><spring:message code="admin.service.lbl.setParams"/></h1>
                </div>
                <div class="objBoxContBBody" style="background:#fff; padding:0px 0px 0;">
                    <div class="shebeiBoxTitle">
                        <ul class="toolBarList" style="margin-left:5px;">
                            <li id="btnDel"><a><spring:message code="common.btn.del"/></a></li>
                        </ul>
                    </div>
                    <div class="datalistMain" style="height:435px; padding:0; overflow:hidden;">
                        <form id="formDataList">
                            <table class="datalistTable">
                                <thead>
                                <th class="checkTh"><input id="allSelectedCheckbox" type="checkbox"/></th>
                                <th><spring:message code="admin.service.form.lbl.name"/></th>
                                <th><spring:message code="admin.service.lbl.time"/></th>
                                </thead>
                                <tbody id="selectedList">

                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
            </div>


        </div>

    </div>


    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li id="btnNext"><a><spring:message code="admin.service.lbl.create"/></a></li>
            <li id="btnBack"><a><spring:message code="common.page.back"/></a>
            </li>
            <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>
</div>

<textarea id="templateItem" style="display: none;">
        {{each content as item}}
        <tr>
            <td><input type="checkbox"
                       data-value-from="{{item.from}}"
                       data-value-to="{{item.to}}"
                       data-value-name="{{item.name}}"
                       data-value-id="{{item.type}}"/></td>
            <td>{{item.name}}</td>
        </tr>
        {{/each}}
</textarea>

<textarea id="templateSelected" style="display: none;">
<tr id="tr_{{type}}">
    <td>
        <input type="checkbox"/>
        <input type="hidden" name="type_{{type}}" value="{{type}}"/>
    </td>
    <td>{{itemName}}</td>
    <th><input type="text" name="times_{{type}}"
               data-rule-required="true" data-rule-min="0" data-rule-number="true"
               class="proInput"/></th>
</tr>
</textarea>
</body>
</html>
