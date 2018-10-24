<%--
  User: tantian
  Date: 2015/10/15
  Time: 15:43
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="common.password.find.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/password/js/find.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/patient/password/find" method="post">
        <table class="userMsgTableb" style="margin:20px 0;">
            <tr>
                <th><spring:message code="common.password.account"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="text" id="account" name="account" class="searchSimpleInputb xw_searchInput" style="width:200px;" />
                    </div>
                </td>
            </tr>
            <tr>
                <th><spring:message code="common.password.email"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="text" id="email" name="email" class="searchSimpleInputb xw_searchInput" style="width:200px;" />
                    </div>
                </td>
            </tr>
            <tr>
                <th><spring:message code="common.password.captcha"/>:</th>
                <td>
                    <div class="mohusearch" style=" float:left; ">
                        <input type="text" id="captcha" name="captcha" maxlength="4" autocomplete="off" class="searchSimpleInputb xw_searchInput" style="width:85px;" />
                        <img id="captchaImage" height="20" width="115" src="${path}/guest/common/captcha" title="<spring:message code="org.captcha.imageTitle"/>"/>
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
