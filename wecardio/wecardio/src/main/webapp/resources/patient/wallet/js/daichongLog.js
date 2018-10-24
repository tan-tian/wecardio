/**
 * Created by tantian on 2015/8/3.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates'], function (msgBox) {
    var index = 0;
    $(document).ready(function() {
        query();
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
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // 重设分页栏
            $('#pageBar').pageBar({
                pageNumber : $('#pageNo').val(),
                total : data['total']
            });
        });
    }
});