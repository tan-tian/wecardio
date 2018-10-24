<%--
  User: tantian
  Date: 2015/7/23
  Time: 16:30
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="patient.record.password.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript">
        var modulus = '${modulus}';
        var exponent = '${exponent}';
    </script>
    <script type="text/javascript" src="${path}/resources/patient/record/js/password.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/patient/record/pay" method="post">
        <input type="hidden" id="rid" name="rid" value="${rid}"/>
        <input type="hidden" id="oid" name="oid" value="${oid}"/>
        <input type="hidden" id="type" name="type" value="${type}"/>
        <input type="hidden" id="serviceType" name="serviceType" value="${serviceType}"/>
        <input type="hidden" id="enPassword" name="enPassword"/>
        <table class="userMsgTableb" style="margin:50px 0;">
            <tr>
                <th><spring:message code="patient.record.password.input"/>:</th>
                <td colspan="3">
                    <div class="mohusearch" style=" float:left; ">
                        <input type="password" class="searchSimpleInputb xw_searchInput" id="password" name="password" style="width:200px;" />
                    </div>
                    <span id="msg" style="line-height: 40px;"></span>
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
