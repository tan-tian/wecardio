/**
 * Created by Sebarswee on 2015/7/31.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates'], function (msgBox) {
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
        if (page) {
            $('#pageNo').val(page);
        }
        var $qform = $('#qform');
        $.post($qform.attr('action'), $qform.serialize(), function(data) {
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
        $("div.xw_hospitalDivCli").on('click', function() {
            var $container = $(this).closest('div.hospitalDiv');
            var oid = $container.attr('oid');
            window.location.href = path + '/patient/organization/' + oid + '/view';
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.dataList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.hospitalDiv');
            var oid = $div.attr('oid');
            val.push(oid);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }
});