<%--
  User: Sebarswee
  Date: 2015/7/13
  Time: 16:35
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.doctor.add.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/org/doctor/css/upload.css"/>
    <script type="text/javascript" src="${path}/resources/org/doctor/js/edit.js"></script>
    <script type="text/javascript">
        var imageIndex = ${fn:length(doctor.doctorImages)};
    </script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/doctor"><spring:message code="org.bread.doctor"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.doctor.edit"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <form id="form" action="${path}/org/doctor/reCreate" method="post">
        <input type="hidden" id="isSubmit" name="isSubmit" value="0"/>
        <input type="hidden" id="did" name="id" value="${doctor.id}"/>
        <input type="hidden" id="guid" name="guid" value="${doctor.guid}"/>
        <div class="userMain" style="margin-bottom:10px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>1</font><spring:message code="org.form.baseInfo"/></h1>
                </div>
                <div class="userDetail" style=" padding-bottom:10px;">
                    <div class="sboxWrap" style="height:480px; padding-left:250px; position:relative;">
                        <div>
                            <table class="userMsgTableb">
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.firstName"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="firstName" style="width:166px;" value="${doctor.firstName}"/>
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.secondName"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="secondName" style="width:166px;" value="${doctor.secondName}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.account"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" style="width:166px;" value="${doctor.account}" disabled/>
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.password"/>:</th>
                                    <td>
                                        <input class="proInput" type="password" name="password" style="width:166px;" value="${doctor.password}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><spring:message code="org.doctor.form.sex"/>:</th>
                                    <td>
                                        <select id="sex" name="sex">
                                            <option value="1" <c:if test="${doctor.sex == 'female'}">selected</c:if>><spring:message code="common.enum.sex.female"/></option>
                                            <option value="2" <c:if test="${doctor.sex == 'male'}">selected</c:if>><spring:message code="common.enum.sex.male"/></option>
                                        </select>
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.roles"/>:</th>
                                    <td>
                                        <input type="hidden" id="roles" name="roles" value="${doctor.roles}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.mobile"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="mobile" style="width:166px;" value="${doctor.mobile}"/>
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.email"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="email" style="width:166px;" value="${doctor.email}" placeholder="<spring:message code="common.message.password.email"/>" />
                                    </td>
                                </tr>
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.birthday"/>:</th>
                                    <td>
                                        <fmt:formatDate value="${doctor.doctorBirthday}" var="doctorBirthday" pattern="yyyy-MM-dd" />
                                        <input class="zjInput" type="text" style="width:166px;" name="doctorBirthday" onclick="WdatePicker({})"
                                               value="${doctorBirthday}" readonly />
                                    </td>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.ic"/>:</th>
                                    <td>
                                        <input class="proInput" type="text" name="ic" style="width:166px;" value="${doctor.ic}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><font class="redstar">*</font><spring:message code="org.doctor.form.address"/>:</th>
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
                                        <input class="proInput" type="text" name="address" style="width:90%;" value="${doctor.address}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th><spring:message code="org.doctor.form.intro"/>:</th>
                                    <td colspan="5">
                                        <textarea cols="30" rows="5" class="textareaStyle" id="intro" name="intro" style="width:90%; padding:4px 8px;"
                                                  data-displayMsg="<spring:message code="common.message.textlimit"/>"><c:out value="${doctor.intro}"/></textarea>
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

        <div class="userMain" style="margin-bottom:10px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>2</font><spring:message code="org.form.doctorQualification"/></h1>
                    <ul class="toolBarList" style="margin-top:5px;">
                        <li><a id="picker"><spring:message code="common.btn.image.upload"/></a></li>
                    </ul>
                    <%--<div class="allCheckMain" style="margin-top:3px;"><a rel="0" class="allCheck xw_allCheck">全选</a></div>--%>
                </div>
                <div class="xw_pannal" style="padding:5px; overflow:hidden; background:#fff; height: 180px;">
                    <ul class="gongneng xw_fujian" id='imgList'>
                        <c:if test="${!empty doctor.doctorImages}">
                            <c:forEach var="doctorImage" items="${doctor.doctorImages}" varStatus="status">
                                <li style="padding-top:0px;">
                                    <img src="${basePath}/file?path=${doctorImage.thumbnail}" alt=""/>
                                    <input type="hidden" name="doctorImages[${status.index}].filePath" value="${doctorImage.source}"/>
                                    <div class="file-panel">
                                        <span class="cancel">删除</span>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>
        <div class="ctrolToolbar">
            <ul class="ctrolToolbarUl">
                <li><a id="saveBtn"><spring:message code="common.btn.save"/></a></li>
                <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
                <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
            </ul>
            <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
        </div>
    </form>
</div>
</body>
</html>
