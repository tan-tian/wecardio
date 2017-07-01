/**
 * Created by Sebarswee on 2015/7/20.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates'], function (msgBox) {
    $(document).ready(function() {
        // 分页栏
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            showNumInput : false,
            onSelectPage : function(page) {
                query(page);
            }
        });
        // 按钮事件
        $('a.queryBtn').on('click', function() {
            query(1);
        });
        $('#submitBtn').on('click', function() {
            var items =  getCheckedItems();
            if (items.count !== 1) {
                return;
            }
            var url = path + '/org/doctor/devolve';
            var datas = {
                from : $('#from').val(),
                to : items.vals
            };
            $.post(url, datas, function(message) {
                msgBox.tips(message['content'], 1, function() {
                    if (message['type'] == 'success') {
                        msgBox.exWindow.close(true);
                    }
                });
            });
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
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
            // 用模板初始化列表
            var $list = $('#list');
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

    // 列表事件
    function listEvents() {
        $("div.xw_yishengliebiao span.yslbSpan").off('click').on('click', function() {
            $("div.xw_yishengliebiao span.yslbSpan").removeClass("yslbSpan2");
            $(this).addClass("yslbSpan2");
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('table.userMsgTableb').find('span.yslbSpan').filter('.yslbSpan2').each(function() {
            var $div = $(this).closest('div.yishengliebiao');
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