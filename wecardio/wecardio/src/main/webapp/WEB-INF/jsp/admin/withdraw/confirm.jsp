<%--
  User: Sebarswee
  Date: 2015/8/14
  Time: 10:35
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.withdraw.confirm.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/admin/withdraw/js/confirm.js"></script>
</head>
<body>
<div class="">
    <form id="form" action="${path}/admin/withdraw/confirm" method="post">
        <input type="hidden" id="orderId" name="orderId" value="${orderId}"/>
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="admin.withdraw.form.confirm.bank_seq"/>:</th>
                <td colspan="3">
                    <input type="text" class="normalInput" name="bankSeq" style="width: 90%;"/>
                </td>
            </tr>
            <tr>
                <th><spring:message code="admin.withdraw.form.confirm.remark"/>:</th>
                <td colspan="3">
                    <textarea name="remark" class="textareaStyle" rows="5" cols="30" style="width:90%; padding:4px 8px; height:110px;"></textarea>
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
