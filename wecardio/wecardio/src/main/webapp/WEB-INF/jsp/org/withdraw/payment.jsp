<%--
  User: tantian
  Date: 2015/8/11
  Time: 20:35
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.withdraw.payment.title"/></title>
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
    <script type="text/javascript" src="${path}/resources/org/withdraw/js/payment.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/wallet"><spring:message code="org.bread.wallet"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.withdraw.payment"/></span>
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
                        <div class="flowBox">
                            <a>3</a>
                            <h1><spring:message code="org.withdraw.flow.success"/></h1>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="proceeDiv"><h1><a style="width:66%;"></a></h1></div>
        </div>
    </div>

    <div class="userMain" style="margin-bottom:10px;">
        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><spring:message code="org.withdraw.payment.title"/></h1>
            </div>
            <div class="userDetail" style=" padding-bottom:10px;">
                <table class="userMsgTableb xw_userMsgTableb">
                    <tr>
                        <th><spring:message code="org.withdraw.payment"/>:</th>
                        <td class="TableTd">
                            <div class="xw_userMsgTab xw_userMsgTab1 a"><spring:message code="org.withdraw.payment.bank"/></div>
                            <div class="xw_userMsgTab xw_userMsgTab2"><spring:message code="org.withdraw.payment.cash"/></div>
                        </td>
                    </tr>
                    <tr class="zhifuType xw_zhifuType">
                        <td class="xw_zhifuTypeTab"><spring:message code="org.withdraw.payment.choose"/>:</td>
                        <td>
                            <ul id="list">
                                <c:if test="${isBindBank}">
                                    <li class="xw_zhifuTypeLi">
                                        <input type="hidden" name="bankBranch" value="${bankBranch}"/>
                                        <input type="hidden" name="bankUsername" value="${bankUsername}"/>
                                        <input type="hidden" name="bankNo" value="${bankNo}"/>
                                        <c:if test="${!empty bankIcon}">
                                            <img src="${path}${bankIcon}" width="125" height="35" alt=""/>
                                        </c:if>
                                        <div class="kahao">${bankNoMask}</div>
                                    </li>
                                </c:if>
                                <c:if test="${outInfo != null}">
                                    <li class="xw_zhifuTypeLi">
                                        <input type="hidden" name="bankBranch" value="${outInfo.bankName}"/>
                                        <input type="hidden" name="bankUsername" value="${outInfo.accountName}"/>
                                        <input type="hidden" name="bankNo" value="${outInfo.bankNo}"/>
                                        <input type="hidden" name="bankType" value="${outInfo.bankType}"/>
                                        <c:if test="${!empty outInfo.bankIcon}">
                                            <img src="${path}${outInfo.bankIcon}" width="125" height="35" alt=""/>
                                        </c:if>
                                        <div class="kahao">${outInfo.bankNoWithMask}</div>
                                    </li>
                                </c:if>
                                <c:if test="${outInfo == null}">
                                    <li class="zengjia"><a id="addBtn"><span><img src="${path}/resources/images/zhifu/zengjia.png" width="55" height="35" alt=""/></span></a></li>
                                </c:if>
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <form id="form" method="post">
        <input type="hidden" id="submitType"/>
        <input type="hidden" name="_page" value="1"/>
        <div class="ctrolToolbar">
            <ul class="ctrolToolbarUl">
                <%--<li><input type="submit" name="_target0" value='<spring:message code="common.btn.pre"/>'/></li>--%>
                <li><input type="submit" id="submitBtn" name="_finish" value='<spring:message code="common.btn.finish"/>'/></li>
                <li><input type="submit" id="cancelBtn" name="_cancel" value='<spring:message code="common.btn.cancel"/>'/></li>
            </ul>
        </div>
    </form>
</div>
<textarea id="template" rows="0" cols="0" style="display: none">
    <input type="hidden" name="bankBranch" value="{$T.bankName}"/>
    <input type="hidden" name="bankUsername" value="{$T.accountName}"/>
    <input type="hidden" name="bankNo" value="{$T.bankNo}"/>
    <input type="hidden" name="bankType" value="{$T.bankType}"/>
    {#if $T.bankIcon != null}
        <img src="${path}{$T.bankIcon}" width="125" height="35" alt=""/>
    {#/if}
    <div class="kahao">{$T.bankNoWithMask}</div>
</textarea>
<script type="text/javascript">
    var requiredMsg = '<spring:message code="org.withdraw.message.payment"/>';
</script>
</body>
</html>
