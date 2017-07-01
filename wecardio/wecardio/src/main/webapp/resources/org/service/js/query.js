seajs.use(['$', 'msgBox', 'pageBar', 'template', 'mulSelect', 'select'], function ($, msgBox, pageBar, template, mulSelect, select) {
    var modelPath = path + '/' + userPath;
    var $btnQuery = null;
    var $pageBar = null;
    var $form0 = null;
    var $form1 = null;
    var $list = null;
    var $select = null;

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

        $select = $('#isEnableds').mulSelect({
            url: path + '/guest/enum/service_type/i_is_enabled',
            multiple: true,
            valueField: 'value',
            textField: 'text',
            showAll: true
        });

        $('#itemId').select({
            width : 166,
            valueField: 'id',
            textField: 'name',
            url: modelPath + '/service/queryServiceName',
            loadFilter: function (data) {
                data.splice(0, 0, {
                    id: '',
                    name: '全部',
                    selected: true
                });
                return data;
            }
        });
        
          $('#staffId').select({
            width: 166,
            valueField: 'value',
            textField: 'text',
            url: modelPath + '/service/sel',
            loadFilter: function (data) {
                data.splice(0, 0, {
                    value: '',
                    text: '全部',
                    selected: true
                });
                return data;
            }

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

        //启用事件
        $('#btnEnable').click(function () {
            setEnableVal(true);
        });

        //禁用事件
        $('#btnDisable').click(function () {
            setEnableVal(false);
        });

        //重置
        $('.btnReset').click(function () {
            $form0[0].reset();
            if ($form1.is(':visible')) {
                $form1[0].reset();
                $form1.find('[type="id"]').val("");
                $('#isEnableds').mulSelect('setValues', '');
                $('#itemId').select('setValues','');
            }
        });

        //发布服务
        $('#btnPubService').click(function () {
            window.location.href = modelPath + '/service/toPage/publish';
        });

        //购买服务
        $('#btnBuyService').click(function () {
            msgBox.warn('进行中...');
            //window.location.href = modelPath + '/service/toPage/edit';
        });

        //查询事件
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

            $.post(modelPath + '/service/queryService', params, function (data) {
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
    }

    function getSelectItems() {
        var ids = [];

        $list.find('.checkbox.checked').each(function () {
            ids.push($(this).attr('data-value-id'));
        });

        return ids;
    }

    function setEnableVal(enable) {
        var ids = getSelectItems();
        if (ids.length == 0) {
            msgBox.warn($('#msg').val());
            return;
        }

        var methodName = enable ? 'enable' : 'disable';

        msgBox.confirm({
            title: $('#confirm').val(), msg: $('#confirmMsg').val(), callback: function (btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    $.post(modelPath + '/service/' + methodName, {ids: ids.join(',')}, function (data) {
                        if (data.type == 'success') {
                            msgBox.tips(data.content);
                            $btnQuery.get(0).click();
                        } else {
                            msgBox.warn(data.content);
                        }
                    });
                }
            }
        });
    }
});
