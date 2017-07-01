<%@tag description="查询控件" pageEncoding="utf-8"%>
<%@ attribute name="id" required="true" description="控件ID(必填)，页面唯一" rtexprvalue="true" %>
<%@ attribute name="name" required="true"  description="控件名称(必填)，表单唯一"  rtexprvalue="true" %>
<%@ attribute name="width" required="false" description="控件宽度" rtexprvalue="true" %>
<%@ attribute name="value" required="false" description="控件值" rtexprvalue="true" %>
<div class="crsearch">
    <input type="text" id="${id}" name="${name}" value="${value}" style="width:${(empty width)? 200: width}px;" placeholder="输入人员名称搜索" />
    <a class="btnQuery"></a>
</div>
