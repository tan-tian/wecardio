<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<title><spring:message code="admin.patient.list.title"/></title>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/huanzhelist_style.css" />
<script type="text/javascript" src="${path}/resources/doctor/device/js/bindPatient.js"></script>
    <style type="text/css">
        a.pingjia{ float:right; display:inherit; padding:0 10px; margin-top:5px; height:18px; line-height:18px;
            font-family:'微软雅黑'; letter-spacing:1px; font-size:12px; color:#fff; text-align:center;
            background:#f7a247; border-bottom:2px solid #ee983c; border-radius:3px; cursor:pointer;}
        a.pingjia:hover{background:#fd751f; border-bottom:2px solid #e95a00; color:#ff0;}
    </style>
    <script type="text/javascript">
        var iType = "${iType}";
        var deleteSelect = "<spring:message code="common.message.delete.select"/>";
        var editSelect = "<spring:message code="common.message.edit.select"/>";
        var editOneSelect = "<spring:message code="common.message.edit.oneSelect"/>";
        var releaseBindingSelect = "<spring:message code="common.message.releaseBinding.select"/>";
        var selectAdvice = "<spring:message code="admin.patient.list.selectAdvice"/>";
        var selectOnePatient = "<spring:message code="admin.patient.list.selectOnePatient"/>";
        var selectAddRecordPatient = "<spring:message code="admin.patient.list.selectAddRecordPatient"/>";
        var comfireReleaseBinding = "<spring:message code="admin.message.patient.comfireReleaseBinding"/>";
    </script>
</head>

<body>

<div class="pageMain">
    <div class="addressBar">
        <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
        <span class="arrowText">></span>
        <span class="normal"><spring:message code="org.main.deviceNav"/></span>
        <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
    </div>
	
    <div class="objBoxCont" style="margin-bottom:10px;">
        <form id="qform" action="" method="post">
            <input type="hidden" id="iType" name="iType" value="${iType}"/>
            <input type="hidden" id="pageNo" name="pageNo" value="1"/>
            <input type="hidden" id="pageSize" name="pageSize" value="16"/>
            <div class="searchFormMain" style="margin-bottom:0px;">
                <div class="crsearch">
                  <input type="text" name="name" style="width:200px;" placeholder="<spring:message code="admin.patient.list.search"/>" />
                  <a class="queryBtn"></a>
                </div>

                <div class="searchBtMain">
                    <a class="subBt queryBtn"><spring:message code="common.btn.query"/></a>
                    <a class="giveupBt"><spring:message code="common.btn.reset"/></a>
                </div>
            </div>

            <div class="khKmainbody xw_kMainContentbody" style=" background:#f6f6f6;min-height:75px; padding:0px; border-top:0px;display:none;">

                <div class="searchFormChose" id="select">

                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="admin.record.query.mobile"/>：</span>
                        <input type="text" id="mobile" name="mobile" class="normalInput"/>
                    </div>

                    <div class="searchBar" style=" position:relative;z-index:3;">
                        <span class="spanName"><spring:message code="admin.record.query.email"/>：</span>
                        <input type="text" id="email" name="email" class="normalInput"/>
                    </div>

                    <dl>
                      <dt><spring:message code="admin.patient.list.status"/></dt>
                        <dd class="moreSelect">
                            <input type="hidden" id="isWalletActive" name="isWalletActive"/>
                        </dd>
                    </dl>
					<!-- 平台管理权限以外的只能查询本机构-->
					<c:if test="${sessionScope.userTypePath =='admin'}">
                    <div class="searchBar" style=" position:relative;z-index:2;">
                        <span class="spanName"><spring:message code="admin.patient.list.org"/></span>
                        <input type="hidden" id="org" name="org"/>
                    </div>
                    </c:if>
					
                    <div class="searchBar" style=" position:relative;z-index:2;">
                        <span class="spanName"><spring:message code="admin.patient.list.doctor"/></span>
                        <input type="hidden" id="doctor" name="doctor"/>
                        <input type="hidden" id="imeId" name="imeId" value="${imeId}"/>
                    </div>
                </div>
                <div class="searchFormBtMain">
                    <ul class="toolBarList" style="float:none; margin:0 auto; width:150px; overflow:hidden;">
                        <li><a class="resetBtn"><spring:message code="common.btn.reset"/></a></li>
                        <li><a class="queryBtn"><spring:message code="common.btn.query"/></a></li>
                    </ul>
                </div>
            </div>
        </form>
        <div class="searchFormFoot">
            <a class="moresearchForm">
                <span class="acc-openbar-txt"><spring:message code="common.btn.slidedown"/></span>
                <span class="acc-openbar-txtb"><spring:message code="common.btn.slideup"/></span><spring:message code="common.btn.more"/>
            </a>
        </div>
    </div>
    
	<div class="titleBar">
        <a class="checkbox uncheck xw_allcheck" rel="0" style=" margin:0px; margin-right:10px; height:28px;"></a>
        <h1 class="dataTotalTitle"><spring:message code="common.toolbar.total.title"/><font id="totalNum">0</font><spring:message code="common.toolbar.bill.title"/></h1>
        <ul class="toolBarList xw_hiddenCTR">
            <c:if test="${sessionScope.userTypePath!='admin'}">
                <c:if test="${iType=='2'}"><%--VIP--%>
                    <c:if test="${sessionScope.userTypePath=='doctor'}">
                        <li><a id="submitBtn"><spring:message code="common.device.bindSubmit"/></a></li><%--确定提交--%>
                    </c:if>
                     <li><a id="addBtn"><spring:message code="common.btn.patient.addPatient"/></a></li><%--新增患者--%>
                </c:if>
             </c:if>
        </ul>
    </div>
	
    <div class="dataList" style="min-height:610px;">
        <div id="list" class="muluList"></div>
        <div id="pageBar" class="pageBar" style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#f7f7f7;"></div>
    </div>
</div>

<textarea id="template" rows="0" cols="0" style="display: none">
    <table class="doctorTable">
        <tbody>
        <tr>
            {#foreach $T.content as record}
            <td class="doctorTd">
                <div class="huanzheDiv xw_huanzheDiv" id="{$T.record.id}">
                    <a title="<spring:message code="admin.patient.list.select"/>" class="filecheckbox checkbox"></a>
                    <h1>
                        <img src="{#if $T.record.headPath != null && $T.record.headPath != ''}${path}/file?path={$T.record.headPath}{#else}${path}/resources/images/userdetails/touxiang.jpg{#/if}" alt="" />
                    </h1>
                    <ul class="usermsg xw_usermsg" style="height:100px">
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
                        <li><spring:message code="admin.patient.list.doctor"/><b>{$T.record.doctorName}</b></li>
                        <c:if test="${sessionScope.userTypePath=='admin'}"><%--平台管理员可以对 0、1这两个状态进行解绑   然后状态变成2--%>
                            {#if $T.record.bindType=='0'||$T.record.bindType=='1'}
                            <li>
                                <a class="pingjia" id="releaseBinding"><spring:message code="admin.message.patient.releaseBinding"/></a>
                            </li>
                            {#/if}
                        </c:if>
                        <c:if test="${sessionScope.userTypePath=='org'}"><%--平台管理员可以对 1这两个状态进行解绑   然后状态变成2--%>
                            {#if $T.record.bindType=='1'}
                                <li>
                                    <a class="pingjia" id="releaseBinding"><spring:message code="admin.message.patient.releaseBinding"/></a>
                                </li>
                            {#/if}
                        </c:if>
                    </ul>
                    <div class="bingshi">
                        <span><spring:message code="admin.patient.list.indication"/></span>
                        <a>{$T.record.indication}</a>
                    </div>
                </div>
            </td>
            {#if $T.record$index != 0 && ($T.record$index+1) % 4 == 0}
        </tr>
        <tr>
            {#/if}
            {#/for}

            {#if $T.content.length % 4 != 0}
            {#param name=remain value=(4 - ($T.content.length % 4))}
            {#for begin = 1 to $P.remain}
            <td class="doctorTd"><div class="huanzheDiv"></div></td>
            {#/for}
            {#/if}
        </tr>
        </tbody>
    </table>
    	{#if $T.content.length == 0}
    <div class="jiachajilu">
        <h1 class="nodata"><spring:message code="common.message.noData"/></h1>
    </div>
    {#/if}
</textarea>
<script type="text/javascript">
    function confirmDelete(fn) {
        msgBox.confirm({
            msg : '<spring:message code="common.message.confirm.delete"/>',
            callback : function(btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    fn();
                }
            }
        });j
    }
</script>
</body>
</html>
