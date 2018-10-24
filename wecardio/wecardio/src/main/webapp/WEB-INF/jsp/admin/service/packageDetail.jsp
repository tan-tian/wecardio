<%--
  Created by IntelliJ IDEA.
  User: tantian
  Date: 2015/7/31
  Time: 15:41
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
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <style type="text/css">
        #lbl_orgName {
            margin: 0px 5px;
            font-family: Arial;
            letter-spacing: 1px;
            font-size: 16px;
            color: #eed205;
        }
    </style>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
        var remarkTitle = '<spring:message code="admin.service.title.remark"/>';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/packageDetail.js"></script>
</head>
<body>
<div class="pageMain">
    <input type="hidden" id="id" name="id" value="${param.id}"/>

    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
            <spring:message code="admin.service.lbl.packMgr"/>
        </a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.lbl.pack.detail"/> </span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <shiro:hasAnyRoles name="org">
                <li id="btnEdit"><a><spring:message code="common.btn.edit"/> </a></li>
            </shiro:hasAnyRoles>
            <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/> ：</font>
    </div>

    <div class="projectDetailHead" style="margin-bottom:15px;">
        <div class="YDDetailStact">
            <div class="YDDetailStactDiv">
                <h2><spring:message code="admin.service.lbl.pack.hot"/></h2>
            </div>
        </div>

        <div class="projectDetailHeadTitle">
            <h1 id="lbl_name">...</h1>
            <span><spring:message code="common.unit.money"/><b id="lbl_price">...</b></span>
        </div>
        <div class="projectDetailHeadBody">
            <div class="projetListConFoot">
                <table class="userMsgTable">
                    <tr>
                        <td><spring:message code="admin.service.lbl.pack.refOrg"/> :</td>
                        <th id="lbl_orgName">...</th>
                        <td><spring:message code="admin.service.lbl.pack.time"/>:</td>
                        <th id="lbl_created">...</th>
                    </tr>
                    <tr>
                        <td><spring:message code="admin.service.lbl.pack.valid.date"/>:</td>
                        <th id="lbl_expired">...</th>
                        <td></td>
                        <th></th>
                    </tr>
                </table>
            </div>
        </div>
        <shiro:hasAnyRoles name="patient">
            <a class="buyA" id="btnBuy"><spring:message code="admin.service.lbl.pack.buy"/></a>
        </shiro:hasAnyRoles>
    </div>

    <div class="detailContent">
        <h1><spring:message code="admin.service.lbl.pack.desc"/> :</h1>

        <p id="lbl_content">...</p>
    </div>

    <div class="objBox objBoxFirstTab">
        <div class="objBoxHead objBoxHeadTab">
            <ul class="objBoxHeadTabUl xw_objBoxHeadTabUl">
                <li class="lion"><spring:message code="admin.service.title"/></li>
                <%--<li><spring:message code="admin.service.lbl.pack.diagnosis"/></li>--%>
                <li><spring:message code="admin.service.lbl.pack.consumer"/></li>
            </ul>
        </div>
        <div class="objBoxBody xw_objBoxFirstTabBody" style="padding:0px; border:0px; min-height:600px;">
            <!--服务项-->
            <div class="objBoxBodyb xw_objBoxFirstBodyb" style="display:block;">
                <div class="tabToolBar">
                    <h1 class="dataTotalTitle"><spring:message code="admin.service.lbl.pack.serviceCount"/></h1>
                </div>
                <div id="listService" class="datalistMain"
                     style="min-height:330px; padding:10px; overflow:hidden; background:#f1f1f1;">
                </div>
            </div>
            <!--已购用户-->
            <div class="objBoxBodyb xw_objBoxFirstBodyb">
                <div class="tabToolBar">
                    <h1 class="dataTotalTitle"><spring:message code="admin.service.lbl.pack.buyCount"/></h1>
                </div>
                <div class="datalistMain" style="min-height:330px; padding:10px; overflow:hidden; background:#efefef;">
                    <div id="listConsumer">

                    </div>
                    <ui:pageBar id="pageBarConsumer"/>
                </div>
            </div>


        </div>
    </div>

</div>
<textarea id="templateService" style="display: none;">
        {{each content as item}}
    <div class="serListBDiv xw_YDlistDiv">
        <div class="stactTipsc"><a class="checkbox uncheck" data-value-id="{{item.code}}"></a></div>
        <div class="arror_jump"><a></a></div>

        <div class="serListBDivData">
            <table class="serListBDivDataT">
                <tr>
                    <th class="tha">{{item.type}}</th>
                    <th>{{item.name}}</th>
                    <th>{{item.code}}</th>
                    <th><spring:message code="common.unit.money"/><i>{{item.price}}</i></th>
                    <th>{{item.serviceCount}}</th>
                    <th class="tha timeh timeOn"></th>
                </tr>
                <tr>
                    <td>[<spring:message code="admin.service.form.lbl.type"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.code"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.code"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.price"/>]</td>
                    <td>[<spring:message code="admin.service.form.lbl.count"/>]</td>
                    <td>[<spring:message code="admin.service.is24H"/>]</td>
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

<textarea id="templateDiagnosis" style="display: none;">
    {{each content as item}}
    <div class="sgListLiMain xw_sgListLiMain">
        <div class="listStatic color02">
            <h1>待分析</h1>

            <h2>[2015.03.04]</h2>
        </div>
        <div class="sgListLiMainCenter xw_sgListLiMainCenter">
            <div class="sgListHead"><h1>2015-05-04体检报告</h1><span>诊单号:<b>YU-110000</b></span></div>
            <ul class="sgMsg">
                <li><span>提交人:</span><a>David</a></li>
                <li><span>主诊医生:</span><a>冯家成</a></li>
                <li><span>所属机构：</span><a>中山医院附属第三医院</a></li>
                <li><span>支付方式：</span><a>网银支付</a></li>
                <li><span>价格：</span><a>588.00</a></li>
            </ul>
        </div>
    </div>
    {{/each}}
</textarea>

<textarea id="templateConsumer" style="display: none;">
    <table class="doctorTable">
        {{each content as item}}
        {{if $index % 5 == 0}}
        <tr>
            {{/if}}
            <td class="doctorTd">
                <div class="huanzheDiv">
                    <h1><img src="${path}/{{item.headPath}}"/></h1>
                    <ul class="usermsg">
                        <li><spring:message code="admin.service.lbl.pack.rname"/> :<b>{{item.name}}</b></li>
                        <li><spring:message code="admin.service.lbl.pack.sex"/>:<b>
                            {{if item.sex == 1 }}
                            <spring:message code="common.enum.sex.female"/>
                            {{else if item.sex == 2}}
                            <spring:message code="common.enum.sex.male"/>
                            {{else}}
                            <spring:message code="common.enum.sex.other"/>
                            {{/if}}
                        </b></li>
                        <li><spring:message code="admin.service.lbl.pack.mobile"/>:<b>{{item.mobile}}</b></li>
                    </ul>
                    <div class="bingshi">
                        <span><spring:message code="admin.service.lbl.pack.indication"/>:</span>
                        <a>{{item.indication}}</a>
                    </div>
                </div>
            </td>
            {{if ($index + 1) % 5 == 0}}
        </tr>
        {{/if}}
        {{/each}}
    </table>
    {{if content.length == 0}}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {{/if}}
</textarea>
</body>
</html>
