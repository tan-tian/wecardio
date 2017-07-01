<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.patient.addEditAdvice.addRecord"/></title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css" />
<link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css" />
<script type="text/javascript" src="${path}/resources/admin/patient/js/record.js"></script>
</head>

<body>

<div style="padding:10px;">
    <form id="form" action="${path}/${sessionScope.userTypePath}/patient/saveRecord" method="post">
        <input type="hidden" id="iId" name="iId" value="${iId}"/>
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="admin.patient.addEditAdvice.patientName"/>:</th>
                <td>
                    <input class="proInput" name="actTime" type="text" style="width:166px;" name="patientName" value="${patientProfile.fullName}" readonly/>
                </td>
                <%--<th><spring:message code="admin.patient.addEditAdvice.editTime"/>:</th>
                <td>
                    <input class="zjInput" type="text" style="width:166px;" name="dateStartTime" onclick="WdatePicker({})" value="${dateStartTime}"
                           value="" readonly />
                </td>--%>
            </tr>

            <tr>
                <th><spring:message code="admin.patient.addEditAdvice.adviceRecord"/>:</th><td colspan="3"><textarea class="textareaStyle" name="detail" style="width:90%; padding:4px 8px; height:160px;"></textarea><font class="redstar">*</font></td>
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
