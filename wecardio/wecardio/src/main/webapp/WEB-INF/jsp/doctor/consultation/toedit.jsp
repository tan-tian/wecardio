<%--
  User: tantian
  Date: 2015/9/2
  Time: 11:12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="common.password.reset.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/doctor/consultation/js/toedit.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/doctor/consultation/toedit/submit" method="post">
        <table class="userMsgTableb" style="margin:40px -20px;">
        	<input type="hidden" name="pId" value="${pId}"/>
        	<input type="hidden" name="ids" value="${ids}"/>
        	<input type="hidden" name="startDate" value="${startDate}"/>
        	<input type="hidden" name="endDate" value="${endDate}"/>
             <tr>
                <th><spring:message code="doctor.consultation.view.doctorOpinion"/>:</th>
                <td colspan="3">
                    <textarea class="textareaStyle" name="opinion" cols="30" rows="5" style="width:90%; padding:4px 8px; height:110px;">${autoOpinion}</textarea>
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
