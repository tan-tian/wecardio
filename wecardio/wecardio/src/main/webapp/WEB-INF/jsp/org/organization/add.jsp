<%--
  User: Sebarswee
  Date: 2015/6/29
  Time: 15:19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="org.organization.apply.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resources/org/organization/css/upload.css"/>
    <script type="text/javascript" src="${path}/resources/org/organization/js/add.js"></script>
</head>
<body>
<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/org/home"><spring:message code="org.bread.home"/></a>
        <span class="arrowText">&gt;</span>
        <a class="jumpback" href="${path}/org/organization"><spring:message code="org.bread.organization"/></a>
        <span class="arrowText">&gt;</span>
        <span class="normal"><spring:message code="org.bread.organization.edit"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="org.bread.back"/></a>
    </div>

    <form id="form" action="${path}/org/organization/save" method="post">
        <input type="hidden" id="isSubmit" name="isSubmit" value="0"/>
        <div class="userMain" style="margin-bottom:10px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>1</font><spring:message code="org.form.baseInfo"/></h1>
                </div>
                <div class="userDetail" style=" padding-bottom:10px;">
                    <table class="userMsgTableb">
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.name"/>:</th>
                            <td colspan="3">
                                <input class="proInput" type="text" name="name" style="width:90%;"/>
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.type"/>:</th>
                            <td>
                                <select id="type" name="type" style="width:230px;">
                                    <option value="0"><spring:message code="common.enum.orgType.personal"/></option>
                                    <option value="1"><spring:message code="common.enum.orgType.organization"/></option>
                                </select>
                            </td>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.code"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="code" style="width:230px;" />
                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td>
                                <input type="checkbox" id="isSign" name="isSign" />
                                <label for="isSign"><spring:message code="org.organization.form.isSign"/></label>
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.address"/>:</th>
                            <td colspan="3">
                                <input type="hidden" id="nationName" name="nationName"/>
                                <input type="hidden" id="nationCode" name="nationCode"/>
                                <input type="hidden" id="provinceName" name="provinceName"/>
                                <input type="hidden" id="provinceCode" name="provinceCode"/>
                                <input type="hidden" id="cityName" name="cityName"/>
                                <input type="hidden" id="cityCode" name="cityCode"/>
                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td colspan="3">
                                <input class="proInput" type="text" id="address" name="address" style="width:90%;" />
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.contact"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="contact" style="width:230px;" />
                            </td>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.telephone"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="telephone" style="width:230px;" />
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.email"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="email" style="width:230px;" />
                            </td>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.postcode"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="zoneCode" style="width:230px;" />
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.firstName"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="firstName" style="width:230px;" />
                            </td>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.secondName"/>:</th>
                            <td>
                                <input class="proInput" type="text" name="secondName" style=" width:230px;" />
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.ic"/>:</th>
                            <td colspan="3">
                                <input class="proInput" type="text" name="ic" style="width:90%;"/>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="org.organization.form.sex"/>:</th>
                            <td>
                                <select id="sex" name="sex" style="width:230px;">
                                    <option value="1"><spring:message code="common.enum.sex.female"/></option>
                                    <option value="2"><spring:message code="common.enum.sex.male"/></option>
                                </select>
                            </td>
                            <th><spring:message code="org.organization.form.birthday"/>:</th>
                            <td>
                                <input class="zjInput" type="text" name="orgBirthday" style="width:230px; height:30px;" onclick="WdatePicker()" readonly />
                            </td>
                        </tr>
                        <tr>
                            <th><font class="redstar">*</font><spring:message code="org.organization.form.intro"/>:</th>
                            <td colspan="3">
                                <textarea class="textareaStyle" cols="30" rows="3" style="width:90%; padding:4px 8px;" name="intro"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code="org.organization.form.image"/>:</th>
                            <td colspan="3">
                                <div id="orgImgList"></div>
                                <div class="addpicDiv"><a id="orgPicker"></a></div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <div class="userMain" style="margin-bottom:10px;">
            <div class="userDetailCont" style="padding-top:0px;">
                <div class="userDetailContHead">
                    <h1 class="userDetailConthText"><font>2</font><spring:message code="org.form.doctorQualification"/></h1>
                </div>
                <div class="userDetail xw_choseChangeproj" style="padding:0px;">
                    <div class="userDetail" style=" padding-bottom:10px;">
                        <div class="userDetail" style=" padding-bottom:10px;">
                            <div>
                                <table class="userMsgTableb">
                                    <tr>
                                        <th><font class="redstar">*</font><spring:message code="org.organization.form.doctor.firstName"/>:</th>
                                        <td>
                                            <input class="proInput" type="text" name="doctorFirstName" style="width:230px;" />
                                        </td>
                                        <th><font class="redstar">*</font><spring:message code="org.organization.form.doctor.secondName"/>:</th>
                                        <td>
                                            <input class="proInput" type="text" name="doctorSecondName" style="width:230px;" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><spring:message code="org.organization.form.doctor.sex"/>:</th>
                                        <td>
                                            <select id="doctorSex" name="doctorSex" style="width:230px;">
                                                <option value="1"><spring:message code="common.enum.sex.female"/></option>
                                                <option value="2"><spring:message code="common.enum.sex.male"/></option>
                                            </select>
                                        </td>
                                        <th><spring:message code="org.organization.form.doctor.birthday"/>:</th>
                                        <td>
                                            <input class="zjInput" type="text" name="doctorBirthDay" style="width:230px;" onclick="WdatePicker()" readonly />
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><font class="redstar">*</font><spring:message code="org.organization.form.doctor.ic"/>:</th>
                                        <td colspan="3">
                                            <input class="proInput" type="text" name="doctorIc" style="width:90%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><spring:message code="org.organization.form.doctor.certificate"/>:</th>
                                        <td colspan="3">
                                            <div id="doctorImgList"></div>
                                            <div class="addpicDiv"><a id="doctorPicker"></a></div>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <div class="ctrolToolbar">
        <ul class="ctrolToolbarUl">
            <li><a id="submitBtn"><spring:message code="common.btn.submit"/></a></li>
            <li><a id="saveBtn"><spring:message code="common.btn.save"/></a></li>
            <li><a onclick="history.go(-1);"><spring:message code="common.btn.cancel"/></a></li>
        </ul>
        <font class="ctrolToolbarText"><spring:message code="common.btn.title"/>：</font>
    </div>
</div>
</body>
</html>
