<%--
  User: Sebarswee
  Date: 2015/8/12
  Time: 17:57
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.withdraw.payment.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/jigoubangdingzhanghao_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/org/withdraw/js/addBank.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="" method="post">
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="org.withdraw.choose_bank"/>:</th>
                <td colspan="3">
                    <input type="hidden" id="bankName" name="bankName"/>
                    <input type="hidden" id="bankType" name="bankType"/>
                </td>
            </tr>
            <tr>
                <th><spring:message code="org.withdraw.bankUsername"/>:</th>
                <td colspan="3">
                    <input class="textareaStyle" id="accountName" name="accountName" style="width:80%; padding:4px 8px; height:30px;"/>
                </td>
            </tr>
            <tr>
                <th><spring:message code="org.withdraw.bankNo"/>:</th>
                <td colspan="3">
                    <input class="textareaStyle" id="bankNo" name="bankNo" style="width:80%; padding:4px 8px; height:30px;"/>
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
