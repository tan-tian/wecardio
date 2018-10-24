/**
 * Created by tantian on 2015/8/11.
 */
seajs.use(['msgBox', 'jTemplates'], function (msgBox) {
    $(document).ready(function() {
        query();
        $('a.queryBtn').on('click', function() {
            query();
        });
        $('a.resetBtn').on('click', function() {
            $('#qform')[0].reset();
        });

        $('a[name="withdrawBtn"]').on('click', function() {
            var $form = $('#form');
            $("[name=checkbox]:checkbox").filter(":checked").each(function(i) {
                var $this = $(this);
                var year = $this.siblings('[name=year]:input').val();
                var month = $this.siblings('[name=month]:input').val();
                var money = $this.siblings('[name=money]:input').val();
                $form.append(
                    '<input type="hidden" name="monthClears[' + i + '].year" value="' + year + '"/>' +
                    '<input type="hidden" name="monthClears[' + i + '].month" value="' + month + '"/>' +
                    '<input type="hidden" name="monthClears[' + i + '].applyMoney" value="' + money + '"/>'
                );
            });
            $form.submit();
        });

        $('#grandTotal').on('click', function () {
            msgBox.exWindow.open({
                title: '&nbsp;',
                url: path + '/org/wallet/withdraw/detail?type=all',
                width: '1150px',
                height: '400px'
            });
        });

        $('#realTotal').on('click', function () {
            msgBox.exWindow.open({
                title: '&nbsp;',
                url: path + '/org/wallet/withdraw/detail?type=real',
                width: '1150px',
                height: '400px'
            });
        });

        $('#withdrawTotal').on('click', function () {
            msgBox.exWindow.open({
                title: '&nbsp;',
                url: path + '/org/wallet/withdraw/detail?type=withdraw',
                width: '1150px',
                height: '400px'
            });
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