<%--
  User: Sebarswee
  Date: 2015/7/28
  Time: 20:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="doctor.consultation.edit.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/doctor/consultation/js/edit.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/doctor/consultation/edit" method="post">
        <input type="hidden" id="cid" name="cid" value="${cid}"/>
        <table class="userMsgTableb">
            <tr>
                <th>QT(ms):</th>
                <td>
                    <input class="proInput" type="text" name="qt" style="width:166px;" />
                </td>
                <th>QTC(ms):</th>
                <td>
                    <input class="proInput" type="text" name="qtc" style="width:166px;" />
                </td>
            </tr>
            <tr>
                <th>PR(ms):</th>
                <td>
                    <input class="proInput" type="text" name="pr" style="width:166px;" />
                </td>
                <th>QRS(ms):</th>
                <td>
                    <input class="proInput" type="text" name="qrs" style="width:166px;" />
                </td>
            </tr>
            <tr>
                <th>RR(ms):</th>
                <td>
                    <input class="proInput" type="text" name="rr" style="width:166px;" />
                </td>
                <th></th>
                <td></td>
            </tr>
            <tr>
                <th><spring:message code="doctor.consultation.form.opinion"/>:</th>
                <td colspan="3">
                    <textarea class="textareaStyle" name="opinion" cols="30" rows="5" style="width:90%; padding:4px 8px; height:110px;"></textarea>
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
