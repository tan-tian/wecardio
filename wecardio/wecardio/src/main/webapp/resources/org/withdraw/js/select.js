/**
 * Created by Sebarswee on 2015/8/17.
 */
seajs.use(['msgBox', 'jTemplates'], function (msgBox) {
    $(document).ready(function() {
        query();

        $('#submitBtn').on('click', function () {
            var $form = $('#form');
            var $chk = $("[name=checkbox]:checkbox");
            if ($chk.filter(":checked").length === 0) {
                msgBox.warn(requiredMsg, null, null);
                return false;
            }
            var data = [];
            $chk.filter(":checked").each(function(i) {
                var $this = $(this);
                var year = $this.siblings('[name=year]:input').val();
                var month = $this.siblings('[name=month]:input').val();
                var money = $this.siblings('[name=money]:input').val();
                data.push({
                    year : year,
                    month : month,
                    money : money
                });
            });
            $.post($form.attr('action'), {id : $('#orderId').val(), data : JSON.stringify(data)}, function (message) {
                msgBox.tips(message['content'], null, function () {
                    if (message['type'] == 'success') {
                        msgBox.exWindow.close({ result : true});
                    }
                });
            });
        });
    });

    /**
     * 查询
     */
    function query() {
        var url = path + '/org/wallet/unsettlement-list';
        $.post(url, {}, function(data) {
            // 用模板初始化列表
            var $list = $('div.userDetail');
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // checkbox事件
            $('#checkAll').off('click').on('click', function () {
                $('[name=checkbox]:checkbox').prop('checked', this.checked);
            });
            $("[name=checkbox]:checkbox").on("click", function () {
                var $chk = $("[name=checkbox]:checkbox");
                $("#checkAll").prop("checked", $chk.length == $chk.filter(":checked").length);
            });
        });
    }
});