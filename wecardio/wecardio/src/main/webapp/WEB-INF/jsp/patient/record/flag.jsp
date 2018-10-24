<%--
  User: tantian
  Date: 2015/7/21
  Time: 15:46
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.record.flag.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/shezhibiaozhi.css"/>
    <script type="text/javascript" src="${path}/resources/patient/record/js/flag.js"></script>
</head>
<body>
<div class="">
    <div class="shezhi">
        <ul>
            <li class="qizhi xw_qizhi"><img src="${path}/resources/images/lan.png" width="48" height="48" alt=""/></li>
            <li class="qizhi xw_qizhi"><img src="${path}/resources/images/huang.png" width="48" height="48" alt=""/></li>
            <li class="qizhi xw_qizhi"><img src="${path}/resources/images/hong.png" width="48" height="48" alt=""/></li>
        </ul>
    </div>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style=" padding-right:50px;">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
<input type="hidden" id="ids" name="ids" value="${ids}"/>
</body>
</html>
