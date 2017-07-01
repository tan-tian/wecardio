<%--
  User: Sebarswee
  Date: 2015/9/4
  Time: 14:43
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.consultation.evaluate.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/consultation/js/evaluate.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/patient/consultation/${cid}/evaluate" method="post">
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="patient.consultation.evaluate.score"/>:</th>
                <td colspan="3">
                    <input type="hidden" id="score" name="score"/>
                </td>
            </tr>
            <tr>
                <th><spring:message code="patient.consultation.evaluate.content"/>:</th>
                <td colspan="3">
                    <textarea name="content" cols="30" rows="5" class="textareaStyle" style="width:85%; padding:4px 8px; height:110px;"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float: none">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
