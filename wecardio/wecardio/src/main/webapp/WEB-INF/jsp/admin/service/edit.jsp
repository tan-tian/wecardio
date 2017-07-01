<%--服务设置新增或编辑--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.service.query.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/edit.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/query">
            <spring:message code="admin.service.query.title"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.btn.add.service"/></span>

        <a class="goback" onclick="history.go(-1);"><spring:message code="common.page.back"/></a>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <form id="form" name="form">
            <input type="hidden" name="uuid" id="uuid" value="${data.uuid}"/>
            <input type="hidden" name="enabled" id="enabled" value="${data.enabled}"/>

            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>1</font><spring:message code="common.page.base"/></h1>
                </div>
                <div class="userDetail" style=" padding-bottom:10px;">

                    <table class="userMsgTableb">
                        <tr>
                            <th><spring:message code="admin.service.form.lbl.type"/>:</th>
                            <td colspan="3">
                                <c:if test="${not empty data}">
                                    <input type="hidden" name="id" value="${data.id}"/>
                                    <label>${data.id}</label>
                                </c:if>
                                <c:if test="${empty data}">
                                    <input class="proInput" type="text" style="width:90%;"
                                           id="id" name="id" value="${data.id}"
                                           data-rule-number="true" data-rule-required="true" data-rule-min="1"
                                           data-rule-max="256"
                                    />
                                    <font class="redstar">*</font>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.service.form.lbl.name"/>:</th>
                            <td colspan="3">
                                <input class="proInput" type="text" style="width:90%;"
                                       id="name" name="name" value="${data.name}"
                                       data-rule-required="true" data-rule-maxlength="200"
                                />
                                <font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.service.form.lbl.priceRange"/>:</th>
                            <td>
                                <input class="proInput" id="from" name="from" type="text" style="width:43.5%;"
                                       value="${data.from}"
                                       data-rule-required="true" data-rule-min="0" data-rule-number="true"
                                       data-rule-maxlength="10"
                                />
                                ~<input class="proInput" id="to" name="to" type="text" style="width:43.5%;"
                                        value="${data.to}"
                                        data-rule-required="true" data-rule-min="0" data-rule-number="true"
                                        data-rule-maxlength="10"
                            /><font class="redstar">*</font>
                            </td>
                        </tr>
                    </table>

                </div>
            </div>
        </form>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li id="btnSave"><a><spring:message code="common.btn.save"/></a></li>
            <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>


</div>
</body>
</html>
