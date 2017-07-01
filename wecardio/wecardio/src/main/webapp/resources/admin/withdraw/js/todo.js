/**
 * Created by Sebarswee on 2015/8/13.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates', 'mulSelect', 'select'], function (msgBox) {
    $(document).ready(function () {
        // 初始化搜索表单
        initSearchForm();
        // 分页栏
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            onSelectPage : function(page) {
                query(page);
            }
        });
        // 查询
        query();
    });

    /**
     * 初始化搜索表单
     */
    function initSearchForm() {
        $('#state').mulSelect({
            data : state
        });
        // 更多查询条件
        $("a.moresearchForm").on('click', function() {
            var $this = $(this);
            $this.toggleClass("up");
            $this.parents("div.objBoxCont").find("div.xw_kMainContentbody").slideToggle('fast');
        });
        // 按钮事件
        $('a.queryBtn').on('click', function() {
            query(1);
        });
        $('a.resetBtn').on('click', function () {
            $('#qform')[0].reset();
            $('#state').mulSelect('reset');
        });
    }

    /**
     * 分页查询
     * @param page
     */
    function query(page) {
        if (page) {
            $('#pageNo').val(page);
        }

        var $qfrom = $('#qform');
        $.post($qfrom.attr('action'), $qfrom.serialize(), function(data) {
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
                pageNumber : $('#pageNo').val(),
                pageSize : $('#pageSize').val(),
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
        }).attr('rel', 0).removeClass('checked');
        // 跳转事件
        $("div.xw_sgListLiMainCenter").on('click', function() {
            var $container = $(this).closest('div.sgListLiMain');
            var order = $container.attr('order');
            window.location.href = path + '/admin/withdraw/' + order + '/view';
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.sgListLiMain');
            var order = $div.attr('order');
            val.push(order);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }
});