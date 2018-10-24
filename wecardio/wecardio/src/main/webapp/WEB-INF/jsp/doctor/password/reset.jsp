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
    <script type="text/javascript" src="${path}/resources/doctor/password/js/reset.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/doctor/password/reset" method="post">
        <table class="userMsgTableb" style="margin:20px 0;">
            <tr>
                <th><spring:message code="common.password.oldPassword"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="password" id="oldPassword" name="oldPassword" class="searchSimpleInputb xw_searchInput" style="width:200px;" />
                    </div>
                </td>
            </tr>
            <tr>
                <th><spring:message code="common.password.newPassword"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="password" id="newPassword" name="newPassword" class="searchSimpleInputb xw_searchInput" style="width:200px;" />
                    </div>
                </td>
            </tr>
            <tr>
                <th><spring:message code="common.password.rePassword"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="password" name="rePassword" class="searchSimpleInputb xw_searchInput" style="width:200px;" />
                    </div>
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
