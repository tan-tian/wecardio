<%--
  User: Sebarswee
  Date: 2015/8/11
  Time: 20:35
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.withdraw.success.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addservibao_style.css"/>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/wallet"><spring:message code="org.bread.wallet"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.withdraw.success"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>
    <div class="flowMainDiv">
        <div class="flowMain">
            <table class="flowMainTable">
                <tr>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>1</a>
                            <h1><spring:message code="org.withdraw.flow.choose"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>2</a>
                            <h1><spring:message code="org.withdraw.flow.payment"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox flowBoxOn">
                            <a>3</a>
                            <h1><spring:message code="org.withdraw.flow.success"/></h1>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:100%;"></a></h1></div>
        </div>
    </div>
    <div class="seccessMsg">
        <h1 class="seccessMsg"><spring:message code="org.withdraw.message.success"/></h1>
    </div>
    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li><a href="${path}/org/wallet"><spring:message code="org.withdraw.return"/></a></li>
        </ul>
    </div>
</div>
</body>
</html>
