/**
 * Created by Sebarswee on 2015/7/7.
 */
seajs.use(['pageBar', 'jTemplates'], function() {
    $(document).ready(function() {
        // 分页栏
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            onSelectPage : function(page) {
                query(page);
            }
        });
        // 按钮事件
        $('a.queryBtn').on('click', function() {
            query();
        });
        $('a.giveupBt').on('click', function () {
            $('#qform')[0].reset();
        });
        // 查询
        query();
    });

    /**
     * 分页查询
     * @param page
     */
    function query(page) {
        var pageNo = page || 1;
        $('#pageNo').val(pageNo);

        $.post(path + '/admin/organization/todoList', $('#qform').serialize(), function(data) {
            // 设置总数
            $('#totalNum').text(data['total']);
            // 用模板初始化列表
            var $list = $('#list');
            $list.empty();
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // 列表事件
            listEvents();
            // 重设分页栏
            $('#pageBar').pageBar({
                pageNumber : pageNo,
                total : data['total']
            });
        });
    }

    /**
     * 列表事件
     */
    function listEvents() {
        // 勾选事件
        $("a.checkbox").off('click').on('click', function() {
            $(this).toggleClass("checked");
        });

        $("a.xw_allcheck").off('click').on('click',function() {
            var isopen = parseInt($(this).attr('rel'));

            if (isopen == 0) {
                $("a.checkbox").addClass("checked");
                $(this).addClass("checked");
                $(this).attr("rel", 1)
            } else {
                $("a.checkbox").removeClass("checked");
                $(this).removeClass("checked");
                $(this).attr("rel", 0)
            }
        });
        // 跳转事件
        $(".xw_sgListLiMainCenter").on('click', function() {
            var $container = $(this).closest('div.sgListLiMain');
            var oid = $container.attr('oid');
            window.location.href = path + '/admin/organization/' + oid + '/view';
        });
    }
});