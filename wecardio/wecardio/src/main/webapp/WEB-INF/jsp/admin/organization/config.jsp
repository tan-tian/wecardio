<%--
  User: Sebarswee
  Date: 2015/7/10
  Time: 16:29
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.organization.audit.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/organization/js/config.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/admin/organization/rate/set" method="post">
        <input type="hidden" id="oid" name="oid" value="${oid}"/>
        <table class="userMsgTableb">
            <tr>
                <th></th>
                <td>

                </td>
            </tr>
            <tr>
                <th><spring:message code="admin.organization.form.rate"/>:</th>
                <td colspan="3">
                    <div class="mohusearch" style=" float:left; ">
                        <input type="text" id="rate" name="rate" class="searchSimpleInputb xw_searchInput" style="width:180px;" value="${rate}" />
                    </div>
                    <span style="margin-top:10px; display:block;">%</span>
                </td>
            </tr>
            <tr>
                <th></th>
                <td colspan="td" style="padding: 0">
                    <span id="msg" style="height: 10px"></span>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
