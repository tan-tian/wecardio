seajs.use(['$', 'template', 'pageBar', 'msgBox', 'validate'], function ($, template, pageBar, msgBox, validate) {
    var modelPath = path + '/' + userPath;
    var $pageBar = null;
    var $btnQuery = null;
    var $form = null;
    var $itemList = null;
    var $selectedList = null;

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $form = $('#formDataList');
        $btnQuery = $('#btnQuery');
        $itemList = $('#itemList');
        $selectedList = $('#selectedList');

        //根据top变量，加载缓存数据
        if (window.top.servicePackMgr.form2SerializeArray) {
            //var $tempForm = window.top.servicePackMgr.form2;
            //if ($tempForm) {
            //    $tempForm.find('tbody tr').appendTo($selectedList);
            //} else {
            //    $tempForm = $form;
            //}
        }

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

        $('body').on('click', '*', function () {
            window.top.servicePackMgr.form2 = $form;
            window.top.servicePackMgr.form2SerializeArray = $form.serializeArray();
        });

        //返回上一页
        $('#btnBack').click(function () {
            window.history.go(-1);
        });

        //取消
        $('#btnCancel').click(function () {
            window.location.href = modelPath + '/service/toPage/packageMgr';
        });

        //生成服务包
        $('#btnNext').click(function () {
            if (!$form.valid()) {
                return;
            }

            var $trs = $selectedList.find('tr');

            if (!$trs.length) {
                msgBox.warn($('#msg').val());
                return;
            }

            var topAllParams = window.top.servicePackMgr.form1SerializeArray;
            var allParams = [];

            if(topAllParams) {
                for(var i = 0; i < topAllParams.length; i++) {
                    allParams.push({name: topAllParams[i].name, value: topAllParams[i].value});
                }
            }

            $trs.each(function (i, n) {
                var tempParams = $(this).find('*').serializeArray();
                var params = [];

                for (var j = 0; j < tempParams.length; j++) {
                    var param = tempParams[j];
                    var name = param.name.substr(0, param.name.indexOf('_'));
 
                    params.push({
                        name: 'types[' + i + "]." + name,
                        value: param.value
                    });
                }

                allParams = allParams.concat(params);
            });

            $.post(modelPath + '/service/editServicePackage', allParams, function (data) {
                if (data.type == 'success') {
                    window.location.href = modelPath + '/service/toPage/package3';
                } else {
                    msgBox.warn(data.content);
                }
            });
        });
    }

    function rightEvent() {

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

            $.post(modelPath + '/service/queryService', params, function (data) {
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