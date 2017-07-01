<%--
  Created by IntelliJ IDEA.
  User: Zhang zhongtao
  Date: 2015/8/4
  Time: 21:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.service.query.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/pay.js"></script>
</head>
<body>
<input type="hidden" value="${modulus}" id="modulus"/>
<input type="hidden" value="${exponent}" id="exponent"/>
<input type="hidden" value="${param.id}" id="id"/>

<div class="">
    <form id="form">
        <table class="userMsgTableb" style="margin:63px 0;">
            <tr>
                <th><spring:message code="admin.service.lbl.payPwd"/> :</th>
                <td colspan="3">
                    <div class="mohusearch" style=" float:left; ">
                        <input type="password" class="searchSimpleInputb xw_searchInput" id="pwd" name="pwd"
                               data-rule-required="true"
                               style="width:200px;"/>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li id="btnConfirm"><a><spring:message code="common.btn.confirm"/> </a></li>
        <li id="btnCancel"><a><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
</body>
</html>
