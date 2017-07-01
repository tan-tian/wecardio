<%--
  User: Sebarswee
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
    <script type="text/javascript" src="${path}/resources/admin/patient/js/bind620.js"></script>
    <script type="text/javascript">
		var waitup='<spring:message code="common.device.bindState"/>';
	</script>
	
</head>
<body>
<div class="">
    <form id="form" action="${path}/doctor/patient/bind620/submit" method="post">
        <table class="userMsgTableb" style="margin:40px -20px;">
        	<input type="hidden" name="pId" id="pId" value="${pId}"/>
        	<tr>
                <th colspan="1"><spring:message code="common.device.bindState"/>:</th>
                <td>
                    <c:choose>
                    	<c:when test="${isBind==true}">
                    		<spring:message code="common.device.isbind"></spring:message>
                    	</c:when>
                    	<c:otherwise>
                    		<spring:message code="common.device.notbind"></spring:message>
                    	</c:otherwise>
                    </c:choose>
                </td>
                
            </tr>
             
            <tr>
             <th colspan="1" style="color:red"><spring:message code="common.device.downHint"/>*:</th>
            	<td>
            		<p><spring:message code="common.device.extreaContent"></spring:message></p>
            	</td>
            </tr>
         </table>
    </form>
</div>

<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float: none">
        <li><a id="downloadBtn"><spring:message code="common.device.download"/></a></li>
        <li><a id="refreshBtn"><spring:message code="common.btn.patient.refreshBtn"/></a></li>
    </ul>
</div>
</body>
</html>
