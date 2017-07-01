seajs.use(['$', 'template', 'pageBar', 'msgBox', 'validate'], function ($, template, pageBar, msgBox, validate) {
    var modelPath = path + '/' + userPath;
    var $pageBar = null;
    var $btnQuery = null;
    var $form = null;

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $form = $('#formDataList');
        $btnQuery = $('#btnQuery');
        $itemList = $('#itemList');
        $selectedList = $('#selectedList');

        //初试化分页条
        $pageBar = $('#pageBar').pageBar({
            onSelectPage: function (page, pageSize) {
                $btnQuery.click();
            }
        });

        $form.validate({
            ignore: ".ignore",
            debug: false,
            onsubmit: true,
            onfocusout: false,
            onkeyup: false,
            errorPlacement: function (error, element) {
            },
            invalidHandler: function (event, validator) {
                for (v in validator.errorMap) {
                    $(event.currentTarget).find('[name="' + v + '"]:first').focus();
                    msgBox.warn(validator.errorMap[v]);
                    break;
                }
            }
        });
    }

    function initEvent() {

        leftEvent();

        rightEvent();

        //发布服务事件
        $('#btnSubmit').click(function () {
            if (!$form.valid()) {
                return;
            }

            var $trs = $selectedList.find('tr');

            if (!$trs.length) {
                msgBox.warn($('#msg').val());
                return;
            }

            var allParams = [];

            $trs.each(function (i, n) {
                var tempParams = $(this).find('*').serializeArray();
                var params = [];

                for (var j = 0; j < tempParams.length; j++) {
                    var param = tempParams[j];
                    var name = param.name.substr(0, param.name.indexOf('_'));

                    params.push({
                        name: 'services[' + i + "]." + name,
                        value: param.value
                    });
                }

                allParams = allParams.concat(params);
            });

            $.post(modelPath + '/service/editService', allParams, function (data) {
                if (data.type == 'success') {
                    msgBox.tips(data.content);
                    window.history.go(-1);
                } else {
                    msgBox.warn(data.content);
                }
            });
        });

        //取消事件
        $('#btnCancel').click(function () {
            window.history.go(-1);
        });
    }

    function rightEvent() {

        //备注点击事件
        $selectedList.on('click', '.editRemark', function () {
            var $this = $(this);
            var type = $this.attr('data-value-type');
            var $remark = $this.closest('tr').find('input[name="remark_' + type + '"]');

            msgBox.exWindow.open({
                title: remarkTitle,
                url: modelPath + '/service/toPage/remark',
                width: '650px',
                height: '250px',
                close: function (data) {
                    var remarkVal = '';

                    if (data) {
                        remarkVal = data.remark;
                    }

                    $remark.val(remarkVal);
                }
            });
        });

        $('#btnDel').click(function () {
            var selTr = $selectedList.find('input:checkbox:checked').closest('tr');

            if (!selTr.length) {
                msgBox.warn($('#msg').val());
                return;
            }

            msgBox.confirm({
                title: $('#title').val(), msg: $('#confirm').val(), callback: function (btnType) {
                    if (btnType == msgBox.ButtonType.OK) {
                        selTr.remove();
                        $('#allSelectedCheckbox').prop('checked', false);
                    }

                }
            });

        });

        //已选择列表中的事件
        $('#allSelectedCheckbox').change(function () {
            var checked = $(this).is(':checked');
            $selectedList.find('input:checkbox').prop('checked', checked);
        });
    }

    /**
     * 左边控件事件
     */
    function leftEvent() {
        //查询服务项
        $btnQuery.click(function () {

            var params = $('#form').serializeArray();
            var options = $pageBar.pageBar('options');

            params = params.concat([
                {name: 'pageNo', value: options.pageNumber || 1},
                {name: 'pageSize', value: 12}
            ]);

            $.post(modelPath + '/service/query', params, function (data) {
                var html = template('templateItem', data);
                $('#itemList').html(html);

                //设置分页条信息
                $pageBar.pageBar({
                    total: data.total,
                    pageNumber: data.pageNo,
                    pageSize: data.pageSize
                });
            });
        }).click();


        //region 左边列表事件

        //服务项全选事件
        $('#allChecked').change(function () {
            var checked = $(this).is(':checked');
            $itemList.find('input:checkbox').prop('checked', checked).each(function () {
                var $this = $(this);
                selOrRemoveItem($this);
            });
        });

        //表格单行复选框事件
        $itemList.on('change', 'input:checkbox', function () {
            var $this = $(this);
            selOrRemoveItem($this);
        });

        //endregion
    }

    function selOrRemoveItem($this) {
        if (!$this.length) {
            return;
        }

        var checked = $this.is(':checked');

        var item = {
            type: $this.attr('data-value-id'),
            oid: '',
            itemName: $this.attr('data-value-name'),
            from: $this.attr('data-value-from'),
            to: $this.attr('data-value-to')
        };

        if (checked) {
            addItem(item);
        } else {
            removeItem(item);
        }
    }

    function getSelectedItem() {
        $itemList.find('input:checkbox:checked').each(function () {
            //alert($(this).attr());
        });
    }

    function addItem(item) {
        if ($('#tr_' + item.type).length > 0) {
            return;
        }

        var html = template('templateSelected', item);
        $selectedList.append(html);
    }

    function removeItem(item) {
        $selectedList.find('#tr_' + item.type).remove();
    }
});