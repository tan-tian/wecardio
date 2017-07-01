<%--
  User: Sebarswee
  Date: 2015/7/7
  Time: 11:46
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.organization.audit.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/organization/js/audit.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/admin/organization/audit" method="post">
        <input type="hidden" id="oid" name="oid" value="${oid}"/>
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="admin.organization.form.audit.isPass"/>:</th>
                <td colspan="3">
                    <select id="isPass" name="isPass">
                        <option value="0"><spring:message code="common.enum.isPass.no"/></option>
                        <option value="1"><spring:message code="common.enum.isPass.yes"/></option>
                    </select>
                </td>
            </tr>
            <tr>
                <th><spring:message code="admin.organization.form.audit.remark"/>:</th>
                <td colspan="3">
                    <textarea name="remark" class="textareaStyle" rows="5" cols="30" style="width:90%; padding:4px 8px; height:110px;"></textarea>
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
