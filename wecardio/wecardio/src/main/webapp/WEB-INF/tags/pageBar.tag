<%@tag description="分页条控件" pageEncoding="utf-8" %>
<%@ attribute name="id" required="true" description="控件ID(必填)，页面唯一" rtexprvalue="true" %>
<%@ attribute name="className" required="false" description="控件样式名称" rtexprvalue="true" %>
<%@ attribute name="pageSize" required="false" description="每页显示的记录数" rtexprvalue="true" %>
<%@ attribute name="showNumInput" required="false" description="是否显示跳转输入框" rtexprvalue="true" %>
<%@ attribute name="entriesNum" required="false" description="可点击超链接数量，不包括首尾" rtexprvalue="true" %>
<%@ attribute name="edgeEntriesNum" required="false" description="首尾可点击超链接数" rtexprvalue="true" %>

<div id="${id}" class="${className}"
     style="border:1px solid #dadbdb; border-bottom:2px solid #AEAEAE; padding:3px 0px 0px; margin:5px 0; background:#fff;"></div>

<script type="text/javascript">
    seajs.use(['$', 'pageBar'], function ($, pageBar) {
        $('#' + '${id}').pageBar({
            pageSize:${empty pageSize ? 12 : pageSize},
            showNumInput:${empty showNumInput ? 'true' : showNumInput},
            entriesNum:${empty entriesNum ? 4 : entriesNum},
            edgeEntriesNum:${empty edgeEntriesNum ? 1 : edgeEntriesNum}
        });
    });
</script>
