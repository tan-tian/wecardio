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
    <script type="text/javascript" src="${path}/resources/doctor/consultation/pdf/js/edit.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/doctor/consultation/pdf/edit/submit" method="post">
        <table class="userMsgTableb" style="margin:40px -20px;">
        	<input type="hidden" name="cid" value="${cid}"/>
             <tr>
                <th><spring:message code="doctor.consultation.view.doctorOpinion"/>:</th>
                <td colspan="3">
                    <textarea class="textareaStyle" name="opinion" cols="30" rows="5" style="width:90%; padding:4px 8px; height:110px;"></textarea>
                </td>
            </tr>
            <tr>
           		<th><spring:message code="doctor.consultation.query.state"/>:</th>
            	<td>
            	<input type="radio" id="stoplight0" name="stoplight" value="normal"
            	<c:if test="${consultation.stoplight == 'normal'}">checked</c:if>/>
            	<spring:message code="doctor.consultation.stoplight.normal"/>
            	<img src="${path}/resources/images/heartIco/xin2.png"
            	width="18" height="18"/>&nbsp;&nbsp;
            	<input type="radio" id="stoplight1" name="stoplight" value="red"
            	<c:if test="${consultation.stoplight == 'red'}">checked</c:if>/>
            	<spring:message code="doctor.consultation.stoplight.red"/>
            	<img src="${path}/resources/images/heartIco/xin1.png"
            	width="18" height="18"/>&nbsp;&nbsp;
            	<input type="radio" id="stoplight2" name="stoplight" value="yellow"
            	<c:if test="${consultation.stoplight == 'yellow'}">checked</c:if>/>
            	<spring:message code="doctor.consultation.stoplight.yellow"/>
            	<img src="${path}/resources/images/heartIco/xin3.png"
            	width="18" height="18"/>
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
