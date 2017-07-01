seajs.use(['$', 'msgBox', 'pageBar', 'template', 'mulSelect', 'select'], function ($, msgBox, pageBar, template, mulSelect, select) {
    var modelPath = path + '/' + userPath;
    var $btnQuery = null;
    var $pageBar = null;
    var $form0 = null;
    var $form1 = null;
    var $list = null;

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $form0 = $('#form0');
        $form1 = $('#form1');
        $list = $('#list');
        $btnQuery = $('.btnQuery');
        //初试化分页条
        $pageBar = $('#pageBar').pageBar({
            onSelectPage: function (page, pageSize) {
                $btnQuery.click();
            }
        });

        var score = $('#score').val();

        $('#scores').mulSelect({
            data: [
                {text: $('#all').val(), value: ''},
                {text: '1' + score, value: 1},
                {text: '2' + score, value: 2},
                {text: '3' + score, value: 3},
                {text: '4' + score, value: 4},
                {text: '5' + score, value: 5}],
            multiple: true,
            valueField: 'value',
            textField: 'text',
            showAll: true
        });

        $('#oid').select({
            url : path + '/'+userTypePath+'/organization/sel',
            isShowQuery : true
        });
    }

    function initEvent() {
        //展开收起更多查询条件
        $("a.moresearchForm").click(function () {
            $(this).toggleClass("up")
                .parents("div.objBoxCont").find("div.xw_kMainContentbody").slideToggle('fast');
        });

        //复选框勾选事件
        $('body').on('click', '.checkbox', function () {
            var $this = $(this);
            $this.toggleClass('checked');

            if ($this.hasClass('allCheck')) {
                var checkbox = $list.find('.checkbox');
                if ($this.hasClass('checked')) {
                    checkbox.addClass('checked');
                } else {
                    checkbox.removeClass('checked');
                }
            }
        });

        //重置
        $('.btnReset').click(function () {
            $form0[0].reset();
            if ($form1.is(':visible')) {
                $form1[0].reset();
                $form1.find('[type="id"]').val("");
                $('#scores').mulSelect('setValues', '');
                $('#oid').select('reset');
            }
        });

        //查询事件
        $btnQuery.click(function () {
            var params = $form0.serializeArray();
            var options = $pageBar.pageBar('options');

            params = params.concat([
                {name: 'pageNo', value: options.pageNumber || 1},
                {name: 'pageSize', value: 12}
            ]);

            if ($form1.is(':visible')) {
                params = params.concat($form1.serializeArray());
            }

            $.post(modelPath + '/forum/info/query', params, function (data) {
                var html = template('templateList', data);

                $list.html(html).find('img').bind('error', function () {
                    if ($(this).data('isModify')) {
                        return;
                    }
                    $(this).data('isModify', true).attr('src', path + '/resources/images/user01.png');
                });

                $('#lblCount').html(data.total);

                //设置分页条信息
                $pageBar.pageBar({
                    total: data.total,
                    pageNumber: data.pageNo,
                    pageSize: data.pageSize
                });
            });

        }).get(0).click();
    }

});
