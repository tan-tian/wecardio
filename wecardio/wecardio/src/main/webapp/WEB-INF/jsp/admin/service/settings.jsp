<%--服务设置--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.service.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/settings.js"></script>
</head>
<body>
<input type="hidden" id="msg" name="msg" value="<spring:message code="common.message.select.data"/>"/>
<input type="hidden" id="title" name="title" value="<spring:message code="common.message.warn"/>"/>
<input type="hidden" id="confirmMsg" name="confirmMsg" value="<spring:message code="common.message.confirm.msg"/>"/>

<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">><spring:message code="admin.service.title.settings"/></span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="form0" class="formQuery">
            <shiro:hasAnyRoles name="org">
                <input type="hidden" name="isEnableds" value="1"/>
            </shiro:hasAnyRoles>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" style="width:200px;" name="name" id="name"
                           placeholder="<spring:message code="admin.service.query.name"/>"/>
                    <a class="btnQuery"></a>
                </div>
                <div class="searchBtMain">
                    <a class="subBt btnQuery"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt btnRest"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>
        </form>
        <shiro:hasAnyRoles name="admin">
            <form id="form1" class="formQuery">
                <div class="khKmainbody xw_kMainContentbody"
                     style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">

                    <div class="searchFormChose" id="select">
                        <dl>
                            <dt><spring:message code="admin.service.form.lbl.status"/>：</dt>
                            <dd class="moreSelect">
                                <input type="hidden" id="isEnableds" name="isEnableds"/>
                            </dd>
                        </dl>
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
                        <span class="acc-openbar-txt"><spring:message code="common.btn.slidedown"/> </span>
                        <span class="acc-openbar-txtb"><spring:message code="common.btn.slideup"/></span>
                        <spring:message code="common.btn.more"/>
                    </a>
                </div>
            </form>
        </shiro:hasAnyRoles>
    </div>


    <div class="objBox objBoxFirstTab">
        <div class="objBoxBody xw_objBoxFirstTabBody"
             style="padding:0px; border:0px; border-top:1px solid #ddd; background:none;">

            <div class="objBoxBodyb xw_objBoxFirstBodyb" style="display:block;background:none; border:0px;">
                <div class="titleBar">
                    <a class="checkbox xw_allcheck allCheck" rel="0"
                       style=" margin:0px; margin-right:10px; height:28px;"></a>

                    <h1 class="dataTotalTitle"><spring:message code="admin.service.lbl.count"/></h1>
                    <shiro:hasAnyRoles name="admin">
                        <ul class="tabToolBarUl" style="float:right;">
                            <li id="btnAddService"><a><spring:message code="admin.service.btn.add.service"/></a></li>
                            <li id="btnEditService"><a><spring:message code="admin.service.btn.edit.service"/></a></li>
                            <li id="btnDisable"><a><spring:message code="admin.service.btn.disable"/></a></li>
                            <li id="btnEnable"><a><spring:message code="admin.service.btn.enable"/></a></li>
                        </ul>
                    </shiro:hasAnyRoles>
                </div>

                <div id="list" class="dataList" style="min-height:600px;">
                </div>

                <ui:pageBar id="pageBar"/>
            </div>
        </div>
    </div>

</div>
<textarea id="templateItem" style="display: none;">
        {{each content as item}}
    <div class="serListBDiv xw_YDlistDiv">
        <div class="stactTipsc"><a class="checkbox uncheck" data-value-id="{{item.id}}"></a></div>
        <div class="arror_jump"><a></a></div>

        <div class="serListBDivData">
            <table class="serListBDivDataT">
                <tr>
                    <th class="tha">{{item.id}}</th>
                    <th>{{item.name}}</th>
                    <th><spring:message code="common.unit.money"/><i>{{item.from}} ~ {{item.to}}</i></th>
                    <th class="tha stactT {{if item.enabled}}stactTOn{{else}}{{/if}}"></th>
                    <th class="tha">{{item.createTime.substring(0,10)}}</th>
                </tr>
                <tr>
                    <td>[<spring:message code="admin.service.form.lbl.type"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.name"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.price"/>]</td>
                    <td>[<spring:message code="admin.service.lbl.status"/>:{{if item.enabled}}<spring:message
                            code="admin.service.btn.enable"/>{{else}}<spring:message code="admin.service.btn.disable"/>{{/if}}]
                    </td>
                    <td>[<spring:message code="admin.service.form.lbl.createTime"/>]</td>
                </tr>
            </table>
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
