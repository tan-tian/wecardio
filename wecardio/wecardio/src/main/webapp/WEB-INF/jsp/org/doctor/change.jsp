<%--
  User: tantian
  Date: 2015/7/20
  Time: 10:19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.doctor.change.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css"/>
    <script type="text/javascript" src="${path}/resources/org/doctor/js/change.js"></script>
</head>
<body>
<div style="margin:0 auto; height: 315px;">
    <input type="hidden" id="from" value="${from}"/>
    <form id="qform" action="${path}/org/doctor/page" method="post">
        <input type="hidden" id="pageNo" name="pageNo" value="1"/>
        <input type="hidden" id="pageSize" name="pageSize" value="5"/>
        <table class="userMsgTableb" style="height: 280px;">
            <thead>
            <tr>
                <td>
                    <div class="crsearch" >
                        <input type="text" name="name" style="width:315px;" placeholder='<spring:message code="org.doctor.placeholder.name"/>' />
                        <a class="queryBtn"></a>
                    </div>
                </td>
            </tr>
            </thead>
            <tbody id="list"></tbody>
        </table>
    </form>
    <div id="pageBar"></div>
</div>
<div class="comfirBar">
    <ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
        <li><a id="cancelBtn"><spring:message code="common.btn.cancel"/></a></li>
    </ul>
</div>
<textarea rows="0" cols="0" id="template" style="display: none">
    {#foreach $T.content as record}
    <tr>
        <td style="padding:5px 8px;">
            <div class="yishengliebiao xw_yishengliebiao" did="{$T.record.id}">
                <font>{$T.record.name}</font>
                <span class="yslbSpan"></span>
            </div>
        </td>
    </tr>
    {#/for}
</textarea>
</body>
</html>
