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
    <title><spring:message code="org.withdraw.choose.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addservibao_style.css"/>
    <style type="text/css">
        ul.ctrolToolbarUl li input {
            border: none;
            width: 124px;
            height: 38px;
            line-height: 38px;
            float: left;
            border-radius: 3px;
            font-family: '微软雅黑';
            font-size: 16px;
            color: #fff;
            text-align: center;
            letter-spacing: 1px;
            border-bottom: 3px solid #fd751f;
            background: #fe8e15;
            cursor: pointer;
        }

        ul.ctrolToolbarUl li input:hover {
            background: #fd751f;
            border-bottom: 3px solid #e95a00;
        }
    </style>
    <script type="text/javascript" src="${path}/resources/org/withdraw/js/choose.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/wallet"><spring:message code="org.bread.wallet"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.withdraw.choose"/></span>
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
                        <div class="flowBox">
                            <a>2</a>
                            <h1><spring:message code="org.withdraw.flow.payment"/></h1>
                        </div>
                    </td>
                    <th></th>
                    <td>
                        <div class="flowBox">
                            <a>3</a>
                            <h1><spring:message code="org.withdraw.flow.success"/></h1>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:30%;"></a></h1></div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="org.withdraw.account_histoy"/></h1>
            </div>
            <div class="userDetail" style=" padding-bottom:10px;">
                <input type="hidden" name="_page" value="0"/>
                <table class="datalistTable">
                    <thead>
                    <tr>
                        <th class="checkTh"><input id="checkAll" type="checkbox" /></th>
                        <th><spring:message code="org.wallet.title.year_month"/></th>
                        <th><spring:message code="org.wallet.title.money"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${unSettlementList}">
                    <tr>
                        <td>
                            <input type="hidden" name="year" value="${record.year}"/>
                            <input type="hidden" name="month" value="${record.month}"/>
                            <input type="hidden" name="money" value="${record.money}"/>
                            <input type="checkbox" name="checkbox" <c:if test="${record.selected}">checked="checked"</c:if> />
                        </td>
                        <td>${record.year}.${record.month}</td>
                        <td>${record.money}</td>
                    </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <form id="form" method="post">
        <input type="hidden" id="submitType"/>
        <div class="ctrolToolbar">
            <ul class="ctrolToolbarUl">
                <li>
                    <%--<a href="10_2_2提现申请02.html">下一步</a>--%>
                    <input type="submit" id="submitBtn" name="_target1" value='<spring:message code="common.btn.next"/>'/>
                </li>
                <li>
                    <%--<a href="7_5机构管理员钱包管理.html">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>--%>
                    <input type="submit" id="cancelBtn" name="_cancel" value='<spring:message code="common.btn.cancel"/>' />
                </li>
            </ul>
        </div>
    </form>
</div>
<script type="text/javascript">
    var requiredMsg = '<spring:message code="org.withdraw.message.choose"/>';
</script>
</body>
</html>
