/**
 * Created by Sebarswee on 2015/7/23.
 */
seajs.use(['msgBox', 'jTemplates'], function (msgBox) {
    $(document).ready(function () {
        query();
    });

    /**
     * 查询
     */
    function query() {
        var url = path + '/patient/record/service/available';
        var data = {
            rid : $('#rid').val()
        };
        $.post(url, data, function(data) {
            // 用模板初始化列表
            var $list = $('div.hzzfBodyTab');
            $list.empty();
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            if (data != null && data.length != 0) {
                $('#totalNum').text(data.length);
                $('.toolBarList').show();
                // 列表事件
                listEvents();
                $('#createBtn').on('click', function () {
                    var item = getCheckedItems();
                    if (item.count === 0) {
                        return;
                    }
                    msgBox.exWindow.open({
                        title : '&nbsp;',
                        width : '400px',
                        height : '265px',
                        url : path + '/patient/record/password?rid=' + $('#rid').val() + '&oid=' + item.oid + '&type=' + item.type + '&serviceType=' + item.serviceType,
                        close : function(flag) {
                            if (flag) {
                                window.location.href = path + '/patient/record';
                            }
                        }
                    });
                });
            }
        });
    }

    /**
     * 列表事件
     */
    function listEvents() {
        // 勾选事件
        $("div.xw_YDlistHeadRight span.hzzfSpan").on('click', function() {
            $("div.xw_YDlistHeadRight span.hzzfSpan").not(this).removeClass("hzzfSpan2");
            $(this).toggleClass("hzzfSpan2");
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var oid = [];
        var type = [];
        var serviceType = [];
        $('div.hzzfBodyTab').find('span.hzzfSpan').filter('.hzzfSpan2').each(function() {
            var $div = $(this).closest('div.YDlistDiv');
            oid.push($div.attr('oid'));
            type.push($div.attr('type'));
            serviceType.push($div.attr('serviceType'));
        });

        return {
            oid : oid.join(','),
            type : type.join(','),
            serviceType : serviceType.join(','),
            count : oid.length
        };
    }
});