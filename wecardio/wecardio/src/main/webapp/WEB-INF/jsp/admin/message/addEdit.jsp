<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.message.list.addTitle"/></title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/addform_style.css" />
<link rel="stylesheet" type="text/css" href="${path}/resources/css/serviselist_style.css" />
<script type="text/javascript" src="${path}/resources/admin/message/addEdit.js"></script>

<script type="text/javascript">
    var iId = '${entity.id}';
</script>
</head>

<body>

<div class="">
    <form id="form" action="${path}/${sessionScope.userTypePath}/message/save" method="post">
        <input type="hidden" id="iId" name="iId" value="${entity.id}"/>
        <input type="hidden" id="recevied" name="recevied" value="0"/>
        <input type="hidden" id="iDeal" name="iDeal" value="0"/>
        <table class="userMsgTableb">
            <tr>
                <th><spring:message code="message.template.toPerson"></spring:message>:</th>
                <td>
                    <input name="from_id"  type="hidden"  value="${entity.from_id}" />
                    <input name="from_type"  type="hidden" value="${entity.from_type}" />
                    <input class="proInput"name="from_name"  type="text" style="width:166px;" 
                    		value="<spring:message code='${entity.from_name}'/>" readonly/> 
                   <!-- <textarea class="dd" name="dd"  rows="5" cols="60" readonly>
                    	<spring:message code="${entity.from_name}"></spring:message>
                    </textarea>-->
                </td>
                <th><spring:message code="message.template.toTime"></spring:message>:</th>
                <td>
                    <input type="hidden" id="type" name="type" value="6"/>
                    <fmt:formatDate value="${nowTime}" var="nowTime" pattern="yyyy-MM-dd HH:mm:ss" />
                    <input class="zjInput" type="text" style="width:166px;" name="dateRecevied_time" onclick="WdatePicker({
                                readOnly : true,
                                minDate : '#F{$dp.$D(\'startDate\',{d:0})||\'1900-01-01\'}'
                            })"
                           value="${nowTime}" readonly />
                </td>
            </tr>
            <tr>
                <th><spring:message code="message.template.receiveType"></spring:message>:</th>
                <td>
                    <input type="hidden" id="to_type" name="to_type" value="${entity.to_type}" />
                </td>
                <th><spring:message code="message.template.receiver"></spring:message>:</th>
                <td>
                    <input type="hidden" id="to_name" name="to_name" value="${entity.to_name}"/>
                    <input type="hidden" id="to_id" name="to_id" value="${entity.to_id}"/>
                </td>
            </tr>
            <tr>
                <th><spring:message code="message.template.msgTest" />:</th><td colspan="3"><textarea class="textareaStyle" style="width:92%; padding:4px 8px; height:180px;" name="content">${entity.content}</textarea><font class="redstar">*</font></td>
            </tr>


        </table>
    </form>
</div>


<div class="comfirBar">
	<ul class="ctrolToolbarUl ctrolToolbarUlB" style="float:none;">
        <li><a id="submitBtn"><spring:message code="message.template.confirm" /></a></li>
        <li><a id="cancelBtn"><spring:message code="message.template.cancel" /></a></li>
    </ul>
</div>

</body>
</html>
