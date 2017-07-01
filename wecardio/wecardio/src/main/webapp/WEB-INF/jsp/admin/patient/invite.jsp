<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.patient.list.invite"/></title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/huanzheyaoqing.css" />
<script type="text/javascript" src="${path}/resources/admin/patient/js/invite.js"></script>
<script type="text/javascript">
    var searchAccountMsg = "<spring:message code="admin.patient.list.searchAccount"/>";
    var searchTipAccountMsg = "<spring:message code="admin.patient.list.searchTipAccount"/>";
</script>
</head>

<body>
	<div class="invitation">
        <div class="addressBar">
            <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
            <span class="arrowText">></span>
            <span class="normal"><spring:message code="admin.patient.list.invite"/></span>
            <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
        </div>

        <form id="qform" action="" method="post">
            <input type="hidden" id="iType" name="iType" value="3"/><%--在邀请页面查询符合条件散户：没有被邀请的散户--%>
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="16"/>

            <div class="invitationMain">
                <div class="invitationSearch" >
                    <table>
                        <tr>
                            <th><spring:message code="admin.patient.list.searchAccount"/>
                            </th>
                            <td>
                                <input type="text" id="email" name="email" value="<spring:message code="admin.patient.list.searchAccount"/>" onblur="if(this.value=='') {this.value='<spring:message code="admin.patient.list.searchAccount"/>'};" onfocus="if(this.value=='<spring:message code="admin.patient.list.searchAccount"/>') {this.value=''};this.onmouseout=''" />
                                <a id="queryBtn"></a>
                            </td>
                        </tr>
                    </table>

                </div>
                <div id="list" class="invitationLiebiao"></div>
                <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
            </div>
        </form>
    </div>

    <textarea id="template" rows="0" cols="0" style="display: none">
        <table class="doctorTable xw_doctorTable">
            <tr>
                {#foreach $T as record}
                <td class="doctorTd">
                    <div class="huanzheDiv xw_huanzheDiv">
                        <a title="<spring:message code="admin.patient.list.select"/>" class="filecheckbox checkbox"></a>
                        <h1><img src="{#if $T.record.HeadPath != null && $T.record.HeadPath != ''}${path}/{$T.record.HeadPath}{#else}${path}/resources/images/user/user01.png{#/if}" alt="" /></h1>
                        <ul class="usermsg xw_usermsg hasbind">
                            <li><spring:message code="admin.patient.list.name"/><b>{$T.record.fullName}</b></li>
                            {#if $T.record.sexName=='male'}
                            <li><spring:message code="admin.patient.list.sex"/><b><spring:message code="common.enum.sex.male"/></b></li>
                            {#/if}
                            {#if $T.record.sexName=='female'}
                            <li><spring:message code="admin.patient.list.sex"/><b><spring:message code="common.enum.sex.female"/></b></li>
                            {#/if}
                            {#if $T.record.sexName=='other'}
                            <li><spring:message code="admin.patient.list.sex"/><b><spring:message code="common.enum.sex.other"/></b></li>
                            {#/if}
                            <li><spring:message code="admin.patient.list.mobile"/><b>{$T.record.mobile}</b></li>
                        </ul>
                        <div class="bingshi">
                            <span><spring:message code="admin.patient.list.indication"/></span>
                            <a>{$T.record.indication}</a>
                        </div>
                        <div class="yaoqing" id="yaoqingBtn{$T.record.id}" val="{$T.record.id}"><spring:message code="admin.patient.form.inviteBtn"/></div>
                    </div>
                </td>
                {#if $T.record$index != 0 && ($T.record$index+1) % 4 == 0}
            </tr>
            <tr>
                {#/if}
                {#/for}
            </tr>
        </table>
    </textarea>
</body>
</html>
  