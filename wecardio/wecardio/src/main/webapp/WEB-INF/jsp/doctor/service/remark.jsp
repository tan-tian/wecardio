<%--
  Created by IntelliJ IDEA.
  User: tantian
  Date: 2015/7/24
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.service.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css" />
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/remark.js"></script>
</head>
<body>
<div class="">

    <table class="userMsgTableb">

        <tr>
            <td colspan="3"><textarea id="remark" class="textareaStyle" style="width:96%; padding:4px 8px; height:110px;">${param.remark}</textarea></td>
        </tr>


    </table>

</div>


<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li id="btnSubmit"><a><spring:message code="common.btn.submit"/></a></li>
        <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
