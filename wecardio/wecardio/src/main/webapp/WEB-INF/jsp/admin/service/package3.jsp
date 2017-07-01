<%--
  Created by IntelliJ IDEA.
  User: Zhang zhongtao
  Date: 2015/7/27
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="admin.service.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addservibao_style.css"/>
    <script type="text/javascript">
        var userPath = '${sessionScope.userTypePath}';
        var remarkTitle = '<spring:message code="admin.service.title.remark"/>';
    </script>
    <script type="text/javascript" src="${path}/resources/org/service/js/package3.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="common.page.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
            <spring:message code="admin.service.lbl.packMgr"/>
        </a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/package1">
            <spring:message code="admin.service.lbl.add.packMgr"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/service/toPage/package2">
                <spring:message code="admin.service.lbl.cfg"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.service.lbl.success"/></span>
    </div>

    <div class="flowMainDiv">
        <div class="flowMain">
            <table class="flowMainTable">
                <tr>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>1</a>

                            <h1><spring:message code="admin.service.lbl.base"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>2</a>

                            <h1><spring:message code="admin.service.lbl.cfg"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>3</a>

                            <h1><spring:message code="admin.service.lbl.success"/></h1>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:100%;"></a></h1></div>
        </div>
    </div>

    <div class="seccessMsg">
        <h1 class="seccessMsg"><spring:message code="admin.service.lbl.success"/> ！</h1>
    </div>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li id="btnNew"><a><spring:message code="admin.service.lbl.add.packMgr"/> </a></li>
            <li id="btnPckMgr"><a><spring:message code="admin.service.lbl.packMgr"/> </a></li>
        </ul>
    </div>
</div>

</body>
</html>
