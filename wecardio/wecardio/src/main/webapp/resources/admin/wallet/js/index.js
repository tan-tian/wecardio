/**
 * Created by Sebarswee on 2015/8/15.
 */
seajs.use(['jTemplates'], function () {
    $(document).ready(function() {
        query();
        $('a.queryBtn').on('click', function() {
            query();
        });
        $('a.resetBtn').on('click', function() {
            $('#qform')[0].reset();
        });
    });

    /**
     * 查询
     */
    function query() {
        var $qfrom = $('#qform');
        $.post($qfrom.attr('action'), $qfrom.serialize(), function(data) {
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