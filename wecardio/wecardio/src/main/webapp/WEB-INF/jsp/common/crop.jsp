<%--
  User: Sebarswee
  Date: 2015/8/19
  Time: 10:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="common.crop.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/common/js/crop.js"></script>
</head>
<body>
<div id="container" style="margin:0 auto; height: 315px;">
    <form id="form" action="${path}/file/crop" method="post">
        <input type="hidden" id="image" name="image" value="${image}"/>
        <input type="hidden" id="width" name="width" value="${width}"/>
        <input type="hidden" id="height" name="height" value="${height}"/>
        <input type="hidden" id="scale" name="scale" value="1"/>
        <input type='hidden' id='x' name='x' value='0'/>
        <input type='hidden' id='y' name='y' value='0'/>
        <input type='hidden' id='w' name='w' value='0'/>
        <input type='hidden' id='h' name='h' value='0'/>
    </form>
    <img id="img" src="" alt=""/>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
