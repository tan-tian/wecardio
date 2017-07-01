<%--
  User: Sebarswee
  Date: 2015/6/16
  Time: 17:14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <shiro:authenticated>
        <%response.sendRedirect(request.getContextPath() + "/patient");%>
    </shiro:authenticated>
    <title><spring:message code="system.name"/> | <spring:message code="patient.login.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/login.css" />
    <script type="text/javascript">
        var modulus = '${modulus}';
        var exponent = '${exponent}';
    </script>
    <script type="text/javascript" src="${path}/resources/patient/login/js/login.js"></script>
</head>
<body>
<div  class="loginDiv">
    <div class="loginTop">
        <div class="logo">
            <!--<img src="${path}/resources/images/logo.png" width="677" height="90" />-->
            <spring:message code="system.name"/>
        </div>
    </div>
    <div class="loginCenter">
        <form id="loginForm" action="${path}/patient/login" method="post">
            <input type="hidden" id="enPassword" name="enPassword"/>
            <input type="hidden" id="rememberMe" name="rememberMe" value="false"/>
            <div class="loginMainCenter">
                <div class="loginMainCenterBox">
                    <div class="tittle"><h1><spring:message code="patient.login.title"/></h1><%--<a class="zhuce" href=""><spring:message code="patient.login.register"/></a>--%></div>
                    <ul class="mainUl">
                        <li class="liText"><spring:message code="patient.login.username"/>:</li>
                        <li class="inputLi mainUl1">
                            <input type="text" placeholder="" id="username" name="username" data-value-desc="<spring:message code="patient.login.username"/>" />
                        </li>
                        <li class="liText"><spring:message code="patient.login.password"/>:</li>
                        <li class="inputLi mainUl2">
                            <input type="password" placeholder="" id="password" name="password" autocomplete="off" data-value-desc="<spring:message code="patient.login.password"/>" />
                        </li>
                        <li class="liText"><spring:message code="patient.login.captcha"/>:</li>
                        <li class="inputLi mainUl3">
                            <input type="text" style=" float:left;width:198px;" placeholder="" id="captcha" name="captcha" maxlength="4" autocomplete="off" data-value-desc="<spring:message code="patient.login.captcha"/>" />
                            <img id="captchaImage" height="30" width="115" src="${path}/guest/common/captcha" title="<spring:message code="patient.captcha.imageTitle"/>"/>
                        </li>
                        <li class="mainUl4">
                            <a class="mainUlLink1"><spring:message code="patient.login.rememberMe"/></a>
                            <a class="mainUlLink2"><spring:message code="patient.login.findPassword"/></a>
                        </li>
                    </ul>
                    <a class="mainLink xw_mainLink" id="submitBtn"><spring:message code="patient.login.submit"/></a>
                </div>
            </div>
        </form>
    </div>
    <div class="bottom">
        <div class="bottomCenter">
            <%--<div class="bottomLeft"><spring:message code="system.footer.copyright"/></div>--%>
            <%--<div class="bottomRight"><spring:message code="system.footer.support"/></div>--%>
            <spring:message code="system.footer.copyright"/>
        </div>
    </div>
</div>
</body>
</html>
