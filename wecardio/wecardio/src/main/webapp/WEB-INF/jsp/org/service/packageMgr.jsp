<%--套餐查询或选购--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.service.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/commonstyle.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
        var remarkTitle = '<spring:message code="admin.service.title.remark"/>';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/packageMgr.js"></script>
</head>
<body>
<jsp:useBean id="now" class="java.util.Date"/>
<input type="hidden" id="msg" name="msg" value="<spring:message code="common.message.select.data"/>"/>
<input type="hidden" id="title" name="title" value="<spring:message code="common.message.warn"/>"/>
<input type="hidden" id="confirm" name="confirm" value="<spring:message code="common.message.confirm.delete"/>"/>
<input type="hidden" id="oneSelect" name="oneSelect" value="<spring:message code="common.message.edit.oneSelect"/>"/>

<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.lbl.packMgr"/></span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="form0" name="form0">
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                    <input type="text" style="width:200px;" name="title"
                           placeholder="<spring:message code="admin.service.query.name"/>"/>
                    <a class="btnQuery"></a>
                </div>

                <div class="searchBtMain">
                    <a class="subBt btnQuery"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt btnReset"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>
        </form>
        <div class="khKmainbody xw_kMainContentbody"
             style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">
            <form id="form1" name="form1">
                <div class="searchFormChose" id="select">
				<!-- 机构和医生只能查询本机构的套餐
                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="admin.service.lbl.pack.refOrg"/> ：</span>
                        <input type="hidden" id="orgId" name="orgId"/>
                    </div>
				 -->
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


    <div class="objBox objBoxFirstTab">

        <div class="objBoxBody xw_objBoxFirstTabBody"
             style="padding:0px; border:0px; border-top:1px solid #ddd; background:none;">

            <!--订单列表-->
            <div class="objBoxBodyb xw_objBoxFirstBodyb" style="display:block; background:none; border:0px;">
                <div class="titleBar">
                    <a class="checkbox xw_allcheck allCheck" rel="0"
                       style=" margin:0px; margin-right:10px; height:28px;"></a>

                    <h1 class="dataTotalTitle"><spring:message code="admin.service.lbl.count"/></h1>
                    <shiro:hasAnyRoles name="admin,org">
                        <ul class="tabToolBarUl" style="float:right;">
                            <shiro:hasAnyRoles name="org">
                                <li><a href="${path}/${sessionScope.userTypePath}/service/toPage/package1">
                                    <spring:message code="common.btn.add"/></a></li>
                                <li id="btnEdit"><a><spring:message code="common.btn.edit"/></a></li>
                            </shiro:hasAnyRoles>
                            <li id="btnDel"><a><spring:message code="common.btn.del"/></a></li>
                        </ul>
                    </shiro:hasAnyRoles>
                </div>

                <div class="dataList" style="min-height:600px;">
                    <div id="list">

                    </div>

                </div>
                <ui:pageBar id="pageBar"/>
            </div>

        </div>
    </div>

</div>
<textarea id="templateList" style="display: none;">
    <table class="serviseBTable">
        <tr>
            {{each content as item}}
            <td class="doctorTd" style="width: 25%;">
                <div class="serviseDivBar xw_serviseBaoDivBar">
                    <a class="filecheckbox checkbox" data-value-id="{{item.id}}"></a>

                    <div class="serbaoDiv xw_serbaoDiv detail" data-value-id="{{item.id}}">
                        <table class="serviseTable">
                            <thead>
                            <tr>
                                <td><spring:message code="admin.service.lbl.pack.name"/>:</td>
                                <th>{{item.title}}</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><spring:message code="admin.service.lbl.pack.valid.date"/>:</td>
                                <th><div class="thtext">{{item.expired}}</div></th>
                            </tr>
                            <tr>
                                <td><spring:message code="admin.service.lbl.pack.refOrg"/>:</td>
                                <th title="{{item.org.name}}">
                                    <div class="thtext">{{item.org.name}}</div>
                                </th>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td><spring:message code="admin.service.lbl.pack.desc"/>:</td>
                                <th title="{{item.content}}">
                                    <div style="height: 32px;overflow: hidden;border-collapse: collapse;">
                                        {{item.content}}
                                    </div>
                                </th>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div class="bottomDiv">
                        <h1><spring:message code="common.unit.money"/><b>{{item.price}}</b></h1>
                        <shiro:hasRole name="patient">
                            <a class="buybuybuy btnBuy" data-value-id="{{item.id}}">
                                <spring:message code="admin.service.lbl.pack.buy"/>
                            </a>
                        </shiro:hasRole>
                    </div>
                </div>
            </td>
            {{if ($index + 1) % listSize == 0}}
        </tr>
        <tr>
            {{/if}}
            {{/each}}
            {{each emptyList}}
            <td class="doctorTd" style="width: 25%;">&nbsp;</td>
            {{/each}}
        </tr>
    </table>
     {{if content.length == 0}}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {{/if}}
</textarea>
</body>
</html>
