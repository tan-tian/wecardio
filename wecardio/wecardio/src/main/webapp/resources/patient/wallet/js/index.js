/**
 * Created by tantian on 2015/8/3.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates'], function (msgBox) {
    var index = 0;
    $(document).ready(function() {
        query();
        $('div.qianbaoDiv').on('click', function() {
            var $this = $(this);
            if (!$this.attr('action')) {
                return;
            }
            var clickIndex = $('div.qianbaoDiv').index($this);
            if (clickIndex != index) {
                index = clickIndex;
                var $qform = $('#qform');
                $qform.attr('action', $this.attr('action'));

                var $list = $('div.purseDingdan_a');
                $list.hide();
                $list.eq(index).show();
                // 查询
                query();
            }
        });
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            onSelectPage : function(pageNumber) {
                query(pageNumber);
            }
        });
        $('a.queryBtn').on('click', function() {
            query(1);
        });
        $('a.resetBtn').on('click', function() {
            $('#qform')[0].reset();
        });
        $('a.zfmima').on('click', function() {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '400px',
                height : '325px',
                url : path + '/patient/wallet/password/reset'
            });
        });
    });

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
            // 用模板初始化列表
            var $list = $('div.purseDingdan_a').eq(index);
            $list.setTemplateElement('template' + index);
            $list.processTemplate(data);
            // 重设分页栏
            $('#pageBar').pageBar({
                pageNumber : $('#pageNo').val(),
                total : data['total']
            });
        });
    }
});