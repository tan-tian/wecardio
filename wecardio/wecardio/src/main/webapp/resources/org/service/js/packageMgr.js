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
                $btnQuery.get(0).click();
            }
        });

        $select = $('#status').mulSelect({
            url: modelPath + '/service/packageStatus',
            multiple: true,
            valueField: 'value',
            textField: 'text',
            showAll: true
        });

        $('#orgId').select({
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

        //详情
        $list.on('click','.detail', function () { //详情
            var id = $(this).attr('data-value-id');
            window.location.href = modelPath + '/service/toPage/packageDetail?id=' + id;
        }).on('click','.btnBuy', function () { //购买
            var id = $(this).attr('data-value-id');
            window.location.href = modelPath + '/service/toPage/buy?id=' + id;
        });

        //编辑
        $('#btnEdit').click(function () {
            var ids = getSelectItems();

            if (!ids.length) {
                msgBox.warn($('#msg').val());
                return;
            }

            if(ids.length > 1) {
                msgBox.warn($('#oneSelect').val());
                return;
            }

            window.location.href = modelPath + '/service/toPage/package1?id=' + ids[0];
        });

        //删除
        $('#btnDel').click(function () {
            var ids = getSelectItems();

            if (!ids.length) {
                msgBox.warn($('#msg').val());
                return;
            }

            msgBox.confirm({
                title: $('#title').val(), msg: $('#confirm').val(), callback: function (btnType) {
                    if (btnType == msgBox.ButtonType.OK) {
                        $.post(modelPath + '/service/delPackage', {ids: ids.join(',')}, function (data) {
                            if (data.type == 'success') {
                                msgBox.tips(data.content, null, function () {
                                    $btnQuery.get(0).click();
                                });
                            } else {
                                msgBox.warn(data.content);
                            }
                        });
                    }

                }
            });
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
                $('#status').mulSelect('setValues', '');
                $('#orgId').select('setValues', '');
                $('#typeId').select('setValues', '');
            }
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

            $.post(modelPath + '/service/queryServicePackage', params, function (data) {
                data.emptyList = [];
                //每行显示的个数
                data.listSize = 4;

                if(data.content && data.content.length) {
                    var size = data.listSize - (data.content.length % data.listSize);
                    for(var i = 0; i < size; i++){
                        data.emptyList.push({});
                    }
                }

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
