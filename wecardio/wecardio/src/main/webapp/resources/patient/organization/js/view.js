/**
 * Created by tantian on 2015/7/31.
 */
seajs.use(['pageBar', 'jTemplates'], function () {
    $(document).ready(function () {
        // 图片事件
        $('ul.picDivList img').on('click', function() {
            var $this = $(this);
            var large = $this.attr('large');
            var source = $this.attr('source');
            $('img.xw_pic').attr('src', large).attr('source', source);
        });
        $('img.xw_pic').on('click', function () {
            var $this = $(this);
            var source = $this.attr('source');
            if (source != null && source != "") {
                window.open(source);
            }
        });
        $('h1.yszs img').on('click', function () {
            var $this = $(this);
            var source = $this.attr('source');
            if (source != null && source != "") {
                window.open(source);
            }
        });
        $('#doctorHead').find('img').on('click', function () {
            var $this = $(this);
            var source = $this.attr('source');
            if (source != null && source != "") {
                window.open(source);
            }
        });

        // 分页栏
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            onSelectPage : function(page) {
                query(page);
            }
        });
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
        $("div.xw_doctorDiv").on('click', function() {
            var $container = $(this);//.closest('div.doctorDiv');
            var did = $container.attr('did');
            window.location.href = path + '/patient/doctor/' + did + '/view';
        });
    }
});