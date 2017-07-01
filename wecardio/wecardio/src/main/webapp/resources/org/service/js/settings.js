seajs.use(['$', 'msgBox', 'pageBar', 'template', 'mulSelect'], function ($, msgBox, pageBar, template, mulSelect) {
    var modelPath = path + '/' + userPath;
    var $pageBar = null;
    var $list = null;
    var $select = null;
    var $form0 = null;
    var $form1 = null;
    var $btnQuery = null;


    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $list = $('#list');
        $form0 = $('#form0');
        $form1 = $('#form1');
        $btnQuery = $('.btnQuery');

        //初试化分页条
        $pageBar = $('#pageBar').pageBar({
            onSelectPage: function (page, pageSize) {
                $btnQuery.click();
            }
        });

        $select = $('#isEnableds').mulSelect({
            url: path + '/guest/enum/service_type/i_is_enabled',
            multiple: true,
            valueField: 'value',
            textField: 'text',
            showAll: true
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
                $form1.find('[type="id"]').val("");
                $('#isEnableds').mulSelect('setValues', '');
            }
        });

        $btnQuery.click(function () {
            var params = $form0.serializeArray();
            var options = $pageBar.pageBar('options');

            params = params.concat([
                {name: 'pageNo', value: options.pageNumber || 1},
                {name: 'pageSize', value: options.pageSize}
            ]);

            if ($form1.is(':visible')) {
                params = params.concat($form1.serializeArray());
            }

            $.post(modelPath + '/service/query', params, function (data) {
                var html = template('templateItem', data);
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

        //新增服务
        $('#btnAddService').click(function () {
            window.location.href = modelPath + '/service/toPage/edit';
        });

        //编辑服务
        $('#btnEditService').click(function () {
            var ids = getSelectedItem();

            if (ids.length == 0) {
                msgBox.warn($('#msg').val());
                return;
            }
            window.location.href = modelPath + '/service/toPage/edit?id=' + ids[0];
        });

        //回调
        function callBack(data) {
            if (data.type == 'success') {
                msgBox.tips(data.content, null, function () {
                    $btnQuery.get(0).click();
                })
            } else {
                msgBox.warn(data.content);
            }
        }

        //禁用服务
        $('#btnDisable').click(function () {
            var ids = getSelectedItem();

            if (ids.length == 0) {
                msgBox.warn($('#msg').val());
                return;
            }

            msgBox.confirm({
                title: $('#title').val(), msg: $('#confirmMsg').val(), callback: function (btnType) {
                    if (btnType == msgBox.ButtonType.OK) {
                        $.post(modelPath + '/service/disableItem', {ids: ids.join(',')}, function (data) {
                            callBack(data);
                        });
                    }

                }
            });


        });

        //启用服务
        $('#btnEnable').click(function () {
            var ids = getSelectedItem();

            if (ids.length == 0) {
                msgBox.warn($('#msg').val());
                return;
            }

            msgBox.confirm({
                title: $('#title').val(), msg: $('#confirmMsg').val(), callback: function (btnType) {
                    if (btnType == msgBox.ButtonType.OK) {
                        $.post(modelPath + '/service/enableItem', {ids: ids.join(',')}, function (data) {
                            callBack(data);
                        });
                    }

                }
            });
        });

        /**
         * 复选框的选择事件
         */
        $('body').on('click', '.checkbox', function () {
            var $this = $(this);
            $this.toggleClass('checked');

            if ($this.hasClass('allCheck')) {
                var $checkBoxs = $list.find('.checkbox');
                $this.hasClass('checked') ? $checkBoxs.addClass('checked') : $checkBoxs.removeClass('checked');
            }
        });

        function getSelectedItem() {
            var ids = [];
            $list.find('.checkbox.checked').each(function () {
                ids.push($(this).attr('data-value-id'));
            });

            return ids;
        }
    }
});