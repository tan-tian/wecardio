<%--
  User: tantian
  Date: 2015/6/18
  Time: 16:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="system.name"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/index_style.css"/>
    <script type="text/javascript" src="${path}/resources/patient/index/js/main.js"></script>
</head>
<body>
<div class="headDiv">
    <div class="systemLogoAndName">
        <h1><spring:message code="system.name"/></h1>
        <h2>Remote Ecg Monitoring System</h2>
    </div>
    <div class="headDivUser">
        <shiro:hasAnyRoles name="org,doctor">
            <span class="userpic" style="display: none;"><img src="${path}/resources/images/user/user01.png" /></span>
        </shiro:hasAnyRoles>
        <h1 class="usermsg">
            <b title="<shiro:principal type="com.borsam.pojo.security.Principal" property="username"/>">
                <shiro:principal type="com.borsam.pojo.security.Principal" property="username"/>
            </b>
            <i id="identity">
                <c:if test="${sessionScope.userTypePath == 'admin'}">
                    <spring:message code="admin.main.role"/>
                </c:if>
                <c:if test="${sessionScope.userTypePath == 'org'}">
                    <spring:message code="admin.main.role.org"/>
                </c:if>
                <c:if test="${sessionScope.userTypePath == 'doctor'}">
                    <spring:message code="admin.main.role.doctor"/>
                </c:if>
                <c:if test="${sessionScope.userTypePath == 'patient'}">
                    <spring:message code="admin.main.role.patient"/>
                </c:if>
            </i>
        </h1>
        <!--<a class="language" href="#" data-value-lang="zh_CN">中文</a>|<a class="language" href="#" data-value-lang="en_US">English</a>-->

        <a class="headA ico_set"></a>
        <a class="headA ico_exit" href="${path}/${sessionScope.userTypePath}/logout"></a>
        <div class="toggleLanguage xw_toggleLanguage" id="langContent">

        </div>
    </div>
</div>

<div class="BodyMain">
    <div class="leftMain xw_leftMain">
        <div id="listBox">
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_home menuUlBoxHeadOn" pagetype="0" hrefvalue="${path}/patient/home" clicktype="3">
                    <h1 class="up"><spring:message code="patient.main.profileNav"/></h1>
                    <h2>Profile</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_hospital" pagetype="0" hrefvalue="${path}/patient/organization/list" clicktype="3">
                    <h1><spring:message code="patient.main.orgNav"/></h1>
                    <h2>Organization</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_doctor" pagetype="0" hrefvalue="${path}/patient/doctor/list" clicktype="3">
                    <h1><spring:message code="patient.main.doctorNav"/></h1>
                    <h2>Doctor</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_jiancha" pagetype="0" hrefvalue="${path}/${sessionScope.userTypePath}/patient/<shiro:principal property="id" type="com.borsam.pojo.security.Principal"/>/detail?iType=2" clicktype="3">
                    <h1><spring:message code="patient.main.patient.chartCount"/></h1>
                    <h2>chartCount</h2><a></a>
                </div>
            </div>
            <shiro:hasAnyRoles name="patient">
                <div class="menuUlBox">
                    <div class="menuUlBoxHead ico_gouwuche" pagetype="0"
                         hrefvalue="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr" clicktype="3">
                        <h1><spring:message code="patient.main.serviceNav"/></h1>

                        <h2>Service</h2><a></a>
                    </div>
                </div>
            </shiro:hasAnyRoles>
            <shiro:hasAnyRoles name="admin,org,doctor">
                <div class="menuUlBox">
                    <div class="menuUlBoxHead ico_gouwuche" clicktype="2">
                        <h1><spring:message code="admin.main.serviceNav"/></h1>

                        <h2>Service</h2><a></a>
                    </div>
                    <ul class="menuBtListUl">
                        <li pagetype="2" hrefvalue="${path}/${sessionScope.userTypePath}/service/toPage/packageMgr">
                            <shiro:hasAnyRoles name="org,doctor">
                                <spring:message code="admin.main.service.packageMgr"/>
                            </shiro:hasAnyRoles>
                            <shiro:hasAnyRoles name="admin">
                                <spring:message code="admin.main.service.packageNav"/>
                            </shiro:hasAnyRoles>
                        </li>
                        <shiro:hasAnyRoles name="admin,org,doctor">
                            <li pagetype="2" hrefvalue="${path}/${sessionScope.userTypePath}/service/toPage/query">
                                <shiro:hasAnyRoles name="org,doctor">
                                    <spring:message code="admin.main.service.queryMgr"/>
                                </shiro:hasAnyRoles>
                                <shiro:hasAnyRoles name="admin">
                                    <spring:message code="admin.main.service.queryNav"/>
                                </shiro:hasAnyRoles>
                            </li>
                        </shiro:hasAnyRoles>
                        <shiro:hasAnyRoles name="admin,org,doctor">
                            <li pagetype="2" hrefvalue="${path}/${sessionScope.userTypePath}/service/toPage/settings">
                                <shiro:hasAnyRoles name="org,doctor">
                                    <spring:message code="admin.main.service.settingQuery"/>
                                </shiro:hasAnyRoles>
                                <shiro:hasAnyRoles name="admin">
                                    <spring:message code="admin.main.service.settingNav"/>
                                </shiro:hasAnyRoles>
                            </li>
                        </shiro:hasAnyRoles>
                    </ul>
                </div>
            </shiro:hasAnyRoles>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_list" pagetype="0" hrefvalue="${path}/patient/consultation/list" clicktype="3">
                    <h1><spring:message code="patient.main.diagnosisNav"/></h1>
                    <h2>Diagnosis</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_jiancha" pagetype="0" hrefvalue="${path}/patient/record" clicktype="3">
                    <h1><spring:message code="patient.main.checkNav"/></h1>
                    <h2>Check</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_jiancha" pagetype="0" hrefvalue="${path}/${sessionScope.userTypePath}/opinion/list"
                     clicktype="3">
                    <h1><spring:message code="admin.main.opinionNav"/></h1>

                    <h2>Doctor advice</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_xiaoxi" pagetype="0" hrefvalue="${path}/${sessionScope.userTypePath}/message" clicktype="3">
                    <h1><spring:message code="patient.main.messageNav"/></h1>
                    <h2>Message</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_money" pagetype="0" hrefvalue="${path}/patient/wallet" clicktype="3">
                    <h1><spring:message code="patient.main.accountNav"/></h1>
                    <h2>Account</h2><a></a>
                </div>
            </div>
            <div class="menuUlBox noArrow">
                <div class="menuUlBoxHead ico_fuwu" pagetype="0" hrefvalue="${path}/${sessionScope.userTypePath}/forum/info/toPage/index" clicktype="3">
                    <h1><spring:message code="admin.main.evaluateNav"/></h1>
                    <h2>Evaluation</h2><a></a>
                </div>
            </div>
        </div>
    </div>

    <div class="rightPage xw_rightMain">
        <iframe id="iframe" frameborder="0" width="100%" scrolling="no" src="${path}/patient/home" onLoad="setFrameHeight()"></iframe>
    </div>
</div>
<textarea id="templateLang" style="display: none;">
    <h2 class="textH localLang">{{currentLangName}}</h2>
            <div class="toggleLanguageSlider xw_toggleLanguageSlider">
                {{each langs as item}}
                <h3 class="language xw_language" href="#" data-value-lang="{{item.local}}">{{item.name}}</h3>
                {{/each}}
            </div>
</textarea>
</body>
</html>
