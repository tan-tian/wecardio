<%--
  User: tantian
  Date: 2015/6/16
  Time: 17:16
  Desc: 公共头部，使用模块js进行加载处理
--%>
<%@include file="jstlLib.jsp" %>
<%@ page import="com.hiteam.common.util.ConfigUtils" %>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Pragma" content="no-cache"/>
<META http-equiv="Cache-Control" CONTENT="no-store, must-revalidate">
<META http-equiv="expires" CONTENT="-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta name="author" content="hiteam"/>
<meta name="copyright" content="hiteam"/>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<link type="text/css" rel="stylesheet" href="${path}/resources/css/commonstyle.css"/>
<script type="text/javascript">
    var language = '${cookie.language.value}' || '${sessionScope.locale}';
    var path = '${path}';
    var basePath = '${basePath}';
    var userTypePath = '${sessionScope.userTypePath}';
    var messDataLoading = '<spring:message code="common.data.loading"/>';
</script>
<script type="text/javascript" src="${path}/resources/sea-modules/jquery/1.11.2/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${path}/resources/sea-modules/seajs/sea.js"></script>
<script type="text/javascript" src="${path}/resources/sea-modules/seajs/seajs-preload.js"></script>
<script type="text/javascript" src="${path}/resources/sea-modules/seajs/init.js"></script>
<script type="text/javascript">

    seajs.use(['$', 'My97DatePicker'], function ($, My97DatePicker) {
        $(function () {

        });
    });
</script>
