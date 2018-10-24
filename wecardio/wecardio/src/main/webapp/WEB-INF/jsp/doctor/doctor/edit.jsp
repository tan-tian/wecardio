<%--
  User: tantian
  Date: 2015/8/21
  Time: 17:32
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="doctor.edit.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/org/doctor/css/upload.css"/>
    <script type="text/javascript" src="${path}/resources/doctor/doctor/js/edit.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/doctor/home"><spring:message code="doctor.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="doctor.bread.doctor.edit"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="doctor.bread.back"/></a>
    </div>

    <form id="form" action="${path}/doctor/doctor/update" method="post">
        <input type="hidden" id="did" name="id" value="${doctor.id}"/>
        <input type="hidden" id="guid" name="guid" value="${doctor.guid}"/>
        <div class="userMain" style="margin-bottom:10px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>1</font><spring:message code="doctor.form.baseInfo"/></h1>
                </div>
                <div class="userDetail" style=" padding-bottom:10px;">
                    <div class="sboxWrap" style="height:360px; padding-left:250px; position:relative;">
                        <div>
                            <table class="userMsgTableb">
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="doctor.form.mobile"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="mobile" style="width:166px;" value="${doctor.mobile}"/>
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="doctor.form.email"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="email" style="width:166px;" value="${doctor.email}" placeholder="<spring:message code="common.message.password.email"/>" />
                                    </td>
                                </tr>
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="doctor.form.address"/>:</th>
                                    <td colspan="3">
                                        <input type="hidden" id="nationName" name="nationName" value="${doctor.nationName}"/>
                                        <input type="hidden" id="nationCode" name="nationCode" value="${doctor.nationCode}"/>
                                        <input type="hidden" id="provinceName" name="provinceName" value="${doctor.provinceName}"/>
                                        <input type="hidden" id="provinceCode" name="provinceCode" value="${doctor.provinceCode}"/>
                                        <input type="hidden" id="cityName" name="cityName" value="${doctor.cityName}"/>
                                        <input type="hidden" id="cityCode" name="cityCode" value="${doctor.cityCode}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th></th>
                                    <td colspan="3">
                                        <input class="proInput" type="text" name="address" style="width: 90%;" value="${doctor.address}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><spring:message code="doctor.form.intro"/>:</th>
                                    <td colspan="5">
                                        <textarea cols="30" rows="5" class="textareaStyle" name="intro" style="width:90%; padding:4px 8px;resize:none;"><c:out value="${doctor.intro}"/></textarea>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="zgpicture">
                            <div class="zgpictureMain">
                                <input type="hidden" id="headPath" name="headPath" value="${doctor.headPath}"/>
                                <c:choose>
                                    <c:when test="${doctor.headImg != null && doctor.headImg != ''}">
                                        <img id="head" width="230" src="${basePath}/file?path=${doctor.headImg}" alt=""/>
                                    </c:when>
                                    <c:otherwise>
                                        <img id="head" width="230" src="" alt="" style="display: none"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <a class="uploadBt" id="headPicker"><spring:message code="common.btn.head.upload"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="ctrolToolbar">
            <ul class="ctrolToolbarUl">
                <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
                <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
            </ul>
            <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
        </div>
    </form>
</div>
</body>
</html>
