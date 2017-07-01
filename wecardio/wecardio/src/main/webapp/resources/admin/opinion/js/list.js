seajs.use(['$','select', 'msgBox', 'pageBar', 'template'], function ($,select,msgBox,pageBar,template) {
    var modelPath = path + '/' + userPath;
    var $pageBar = null;
    var $list = null;
    var $form0 = null;
    var $form1 = null;
    var $btnQuery = null;

    $(function () {
        $(function () {
            initData();
            initEvent();
        });
    });

    function initData() {
        $list = $('#list');
        $form0 = $('#form0');
        $form1 = $('#form1');
        $btnQuery = $('.btnQuery');

        //初试化分页条
        $pageBar = $('#pageBar').pageBar({
            onSelectPage: function (page, pageSize) {
                $btnQuery.get(0).click();
            }
        });

        $('#doctor').select({
            url : modelPath+'/doctor/sel',
            isShowQuery : true
        });

        $('#org').select({
            url : modelPath+'/organization/sel',
            isShowQuery : true,
            onSelect: function (data) {
                $('#doctor').select('reload', modelPath+'/doctor/sel?iOrgId=' + data.value);
            }
        });
    }

    function initEvent() {
        //展开收起更多查询条件
        $("a.moresearchForm").click(function () {
            $(this).toggleClass("up")
                .parents("div.objBoxCont").find("div.xw_kMainContentbody").slideToggle('fast');
        });

        //重置
        $('.btnRest').click(function () {
            $form0[0].reset();
            if ($form1.is(':visible')) {
                $form1[0].reset();
                $('.select').select('setValues', '');
                $form1.find('[type="id"]').val("");
            }
        });

        $btnQuery.click(function () {
            var params = $form0.serializeArray();
            var options = $pageBar.pageBar('options');

            params = params.concat([
                {name: 'pageNo', value: options.pageNumber || 1},
                {name: 'pageSize', value: options.pageSize}
            ]);

            if ($('.xw_kMainContentbody').is(':visible')) {
                params = params.concat($form1.serializeArray());
            }

            $.post(modelPath + '/opinion/query', params, function (data) {
                var html = template('templateList', data);
                $list.html(html);

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
