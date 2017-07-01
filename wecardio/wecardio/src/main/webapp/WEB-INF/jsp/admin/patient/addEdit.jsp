<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.patient.list.title"/></title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css" />
<link rel="stylesheet" type="text/css" href="${path}/resources/org/doctor/css/upload.css"/>
<script type="text/javascript" src="${path}/resources/admin/patient/js/addEdit.js"></script>
<script type="text/javascript">
    var iId = '${entity.id}';
    var iType = '${iType}';
    var iOrgId = '${entity.org.id}';
    var repeatMobile = "<spring:message code="admin.patient.addEdit.repeatMobile"/>";
    var repeatEmail = "<spring:message code="admin.patient.addEdit.repeatEmail"/>";
</script>
</head>

<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">></span>
        <a class="jumpback" href="${path}/${sessionScope.userTypePath}/patient?iType=${iType}"><spring:message code="admin.patient.title"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="admin.patient.form.add"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>

    <form id="form" action="${path}/${sessionScope.userTypePath}/patient/save" method="post">
        <input type="hidden" id="iId" name="iId" value="${entity.id}"/>
    <div class="userMain" style="margin-bottom:10px;">

        <div class="userDetailCont" style="padding-top:0px;">
            <div class="userDetailContHead">
                <h1 class="userDetailConthText"><font>1</font><spring:message code="admin.patient.addEdit.baseInfo"/></h1>
            </div>
            <div class="userDetail" style=" padding-bottom:10px;">

                <div class="sboxWrap" style="height:450px; padding-left:250px; position:relative;">
                     <div>
                     <table class="userMsgTableb">

                        <tr>
                        	<th><spring:message code="admin.patient.addEdit.firstName"/>:</th>
                            <td>
                                <input class="proInput" name="firstName" type="text"  style="width:166px;" value="${entity.firstName}"/><font class="redstar">*</font>
                            </td>
                        	<th><spring:message code="admin.patient.addEdit.secondName"/>:</th>
                            <td>
                                <input class="proInput" name="secondName" type="text"  style="width:166px;" value="${entity.secondName}"/><font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                        	<th><spring:message code="admin.patient.addEdit.account"/>:</th>
                            <td>
                                <input class="proInput" name="emailV" type="text"  style="width:166px;" value="${entity.email}" <c:if test="${entity.id != null && entity.id != ''}">readonly</c:if>/><font class="redstar">*</font>
                            </td>
                        	<th><spring:message code="admin.patient.addEdit.birthday"/>:</th>
                            <td>
                                <fmt:formatDate value="${entity.dateBirthday}" var="dateBirthday" pattern="yyyy-MM-dd" />
                                <input class="zjInput" type="text" style="width:166px;" name="dateBirthday" onclick="WdatePicker({})"
                                       value="${dateBirthday}" readonly />
                                <font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.patient.addEdit.sex"/>:</th>
                            <td>
                                <select id="sex" name="sex">
                                    <option value="1" <c:if test="${entity.sex == 'female'}">selected</c:if>><spring:message code="common.enum.sex.female"/></option>
                                    <option value="2" <c:if test="${entity.sex == 'male'}">selected</c:if>><spring:message code="common.enum.sex.male"/></option>
                                </select>
                                <font class="redstar">*</font>
                            </td>
                            <th><spring:message code="admin.patient.addEdit.mobile"/>:</th>
                            <td>
                                <input class="proInput" type="text"  style="width:166px;" name="mobile" value="${entity.mobile}"/><font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.patient.addEdit.org"/>:</th>
                            <td>
                            	<c:if test="${isempty==false}">
                                	<input class="proInput" name="sOrgName" type="text"  style="width:166px;" value="${entity.org.name}" readonly/>
                                	<input type="hidden" id="iOrgId" name="iOrgId" value="${entity.org.id}"/>
                                </c:if>
                                <c:if test="${isempty==true}">
                                	<input class="proInput" name="sOrgName" type="text"  style="width:166px;" value="" readonly/>
                                	<input type="hidden" id="iOrgId" name="iOrgId" value=""/>
                                </c:if>
                                <font class="redstar">*</font>
                            </td>
                           
                            <th><spring:message code="admin.patient.addEdit.doctor"/>:</th>
                            <td>
                            	<input type="hidden" id="roles" name="roles" value="${sessionScope.userTypePath}"/>
                            	<input type="hidden" id="iDoctorId" name="iDoctorId" value="${entity.doctor.id}"/>
                            	<c:if test="${sessionScope.userTypePath=='patient'}">
                            		${entity.doctor.name}
                            	</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.patient.addEdit.address"/>:</th>
                            <td colspan="3">
                                <input class="proInput" type="text"  style="width:90%; padding:4px 8px;" name="address" value="${entity.address}"/><font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.patient.addEdit.indication"/>:</th>
                            <td colspan="3">
                            	<textarea class="textareaStyle" style="width:90%; padding:4px 8px;" name="indication">${entity.indication}</textarea>
                                <font class="redstar">*</font>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="admin.patient.addEdit.medicineSituation"/>:</th>
                            <td colspan="3">
                            	<textarea class="textareaStyle" style="width:90%; padding:4px 8px;" name="medicine">${entity.medicine}</textarea>
                                <font class="redstar">*</font>
                            </td>
                        </tr>

                    </table>
                    </div>
                    <div class="zgpicture">
                    	<div class="zgpictureMain">
                        	<!--<img width="230" src="images/user/uer002.jpg" />-->
                            <input type="hidden" id="headPath" name="headPath" value="${entity.headPath}"/>
                            <c:choose>
                                <c:when test="${entity.headPath != null && entity.headPath != ''}">
                                    <img id="head" width="230" src="${path}/file?path=${entity.headPath}" alt=""/>
                                </c:when>
                                <c:otherwise>
                                    <img id="head" width="230" src="" alt="" style="display: none"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <a class="uploadBt" id="headPicker"><spring:message code="admin.patient.addEdit.submitPhoto"/></a>
                    </div>
                </div>

            </div>
        </div>

    </div>
</form>




    <div class="ctrolToolbar">
    	<ul class="ctrolToolbarUl">
        	<li><a id="submitBtn"><spring:message code="admin.patient.addEdit.submit"/></a></li>
        	<li><a href="javaScript:void(0)" onClick="history.go(-1);"><spring:message code="admin.patient.addEdit.cancel"/></a></li>
        </ul>
    	<font class="ctrolToolbarText"><spring:message code="admin.patient.addEdit.cando"/>：</font>
    </div>

</div>

</body>
</html>
