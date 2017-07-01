/**
 * Created by Sebarswee on 2015/7/13.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates', 'mulSelect'], function (msgBox) {
    $(document).ready(function() {
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
            $('#loginState').mulSelect('reset');
            $('#roles').mulSelect('reset');
        });
        $('#deleteBtn').on('click', deleteDoctors);
        $('#changeBtn').on('click', function () {
            var items = getCheckedItems();
            if (items.count === 0) {
                return;
            }
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '375px',
                height : '420px',
                url : path + '/org/doctor/change?from=' + items.vals
            });
        });
        // 查询
        query();
    });

    // 初始化搜索表单
    function initSearchForm() {
        $('#loginState').mulSelect({
            url : path + '/guest/enum/doctor/loginState'
        });
        $('#roles').mulSelect({
            url : path + '/guest/enum/doctor/role'
        });
        $('#auditState').mulSelect({
            url : path + '/guest/enum/doctor/auditState'
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

        $.post(path + '/org/doctor/page', $('#qform').serialize(), function(data) {
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
        $("div.xw_doctorDiv").on('click', function(e) {
            var $container = $(this);//.closest('div.doctorDiv');
            var $checkbox = $(e.target).closest("a.checkbox", $container);
            if ($checkbox.length) {
                // 如果 click 事件发生在复选框内,则不做任何操作
                return;
            }
            var did = $container.attr('did');
            window.location.href = path + '/org/doctor/' + did + '/view';
        });
    }

    // 删除医生
    function deleteDoctors() {
        var items = getCheckedItems();
        if (items.count == 0) {
            return;
        }
        confirmDelete(function() {
            $.post(path + '/admin/doctor/delete', { did : items.vals }, function(message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    query(1);
                }
            });
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.doctorDiv');
            var did = $div.attr('did');
            val.push(did);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }
});