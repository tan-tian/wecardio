/**
 * Created by Sebarswee on 2015/7/13.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates'], function (msgBox) {
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

        // 按钮事件
        $('#queryBtn').on('click', function() {
            query(1);
        });
    });

    // 初始化搜索表单
    function initSearchForm() {

    }

    /**
     * 分页查询
     * @param page
     */
    function query(page) {
        var email = $('#email').val();
        if(email==null||email==''||email==searchAccountMsg)
        {
            top.msgBox.tips(searchTipAccountMsg,null,null);
            return;
        }
        if (page) {
            $('#pageNo').val(page);
        }

        $.post(path + '/'+userTypePath+'/patient/queryPatient', $('#qform').serialize(), function(data) {
            // 设置总数
            $('#totalNum').text(data.length);
            // 用模板初始化列表
            var $list = $('#list');
            $list.empty();
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // 列表事件
            listEvents(data);
            // 重设分页栏
            $('#pageBar').pageBar({
                total : data.length
            });
        });
    }

    /**
     * 列表事件
     */
    function listEvents(jsonData) {
        $.each(jsonData, function(i, data) {
            // 跳转事件
            $("#yaoqingBtn"+data['id']).on('click', function() {
                var id = $(this).attr('val');
                yaoqing(id);
            });
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.huanzheDiv');
            var id = $div.attr('id');
            val.push(id);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }

    // 发送邀请申请
    function yaoqing(iId) {
        $.ajax({
            url: path+'/'+userTypePath+'/message/inviteAdd',
            type: "POST",
            data: {iId:iId},
            dataType: "json",
            success: function(message) {
                top.msgBox.tips(message['content'], null, null);
                if (message['type'] == "success") {
                    window.location.href = path + '/'+userTypePath+'/patient/invite';
                }
            }
        });
    }

});