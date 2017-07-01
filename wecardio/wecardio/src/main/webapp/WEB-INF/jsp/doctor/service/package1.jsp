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
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addservibao_style.css"/>
    <shiro:hasAnyRoles name="org">
        <c:if test="${not empty orgInit and not empty orgWalletInit }">
        <script type="text/javascript">

            var orgInit = ${orgInit};
            var orgWalletInit = ${orgWalletInit};

            seajs.use(['$', 'msgBox'], function ($, msgBox) {
                $(function () {
                    if (!orgInit) {
                        msgBox.tips("<spring:message code="admin.service.msg.orgInit"/>", 5, function () {
                            window.location.href = path + '/org/organization/add';
                        });
                    } else if (!orgWalletInit) {
                        msgBox.tips("<spring:message code="admin.service.msg.orgWalletInit"/>", 5, function () {
                            window.location.href = path + '/org/wallet/notactivate';
                        });
                    }

                });
            });

        </script>
        </c:if>
    </shiro:hasAnyRoles>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
        var remarkTitle = '<spring:message code="admin.service.title.remark"/>';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/package1.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
            <spring:message code="admin.service.lbl.packMgr"/>
        </a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.lbl.base"/></span>

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
                        <div class="flowBox"><a>2</a>

                            <h1><spring:message code="admin.service.lbl.cfg"/></h1></div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox"><a>3</a>

                            <h1><spring:message code="admin.service.lbl.success"/></h1></div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:30%;"></a></h1></div>
        </div>
    </div>


    <div class="userMain" style="margin-bottom:10px;">

        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="admin.service.lbl.base"/></h1>
            </div>
            <div class="userDetail" style=" padding-bottom:10px;">
                <form id="form1" name="form1">
                    <input type="hidden" name="id" id="id" value="${data.id}"/>
                    <table class="userMsgTableb">
                        <tr>
                            <th><spring:message code="admin.service.lbl.pack.name"/>:</th>
                            <td colspan="3">
                                <input id="title" name="title" class="proInput"
                                       data-rule-required="true" data-rule-maxlength="50" value="${data.title}"
                                       type="text" style="width:90%;"/><font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.service.lbl.pack.valid.date"/>:</th>
                            <td>
                                <input class="proInput" type="text" style="width:166px;" id="expired" name="expired"
                                       value="${data.expired}"
                                       data-rule-required="true" data-rule-min="0" data-rule-digits="true"/>
                                <font class="redstar">*</font>
                            </td>
                            <th><spring:message code="admin.service.lbl.pack.price"/>:</th>
                            <td>
                                <input id="price" name="price" class="proInput"
                                       value="${data.price}"
                                       data-rule-required="true" data-rule-min="0" data-rule-max="9999999"
                                       data-rule-number="true"
                                       type="text" style="width:166px;"/>
                                <font class="redstar">*</font>
                            </td>
                            <th><spring:message code="common.service.internalPackage"/>:</th>
                             <td>
                             	<a><spring:message code="common.service.isInternal"/></a>
                            	<input type="radio" id="type" name="type" value="1"/>
                            	<a><spring:message code="common.service.noInternal"/></a>
                            	<input type="radio" id="type" name="type" value="0" checked="checked" />
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.service.lbl.pack.desc"/>:</th>
                            <td colspan="3">
                            <textarea class="textareaStyle" id="content" name="content"
                                      data-rule-maxlength="200"
                                      style="width:90%; padding:4px 8px; height:180px;">${data.content}</textarea>
                            </td>
                        </tr>


                    </table>
                </form>
            </div>
        </div>

    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li id="btnNext"><a><spring:message code="common.btn.next"/> </a></li>
            <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
        </ul>
    </div>


</div>
</body>
</html>
