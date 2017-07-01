seajs.use(['$', 'msgBox', 'validate'], function ($, msgBox, validate) {
    var modelPath = path + '/' + userPath;
    var $form = null;

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $form = $('#form');

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
                    $(event.currentTarget).find('[name="' +v + '"]').focus();
                    msgBox.warn(validator.errorMap[v]);
                    break;
                }
            }
        });
    }

    function initEvent() {
        //保存
        $('#btnSave').click(function () {
            if (!$form.valid()) {
                return;
            }

            $.post(modelPath + '/service/edit', $form.serializeArray(), function (data) {
                if (data.type == 'success') {
                    msgBox.tips(data.content, null, function () {
                        history.go(-1);
                    });
                } else {
                    msgBox.warn(data.content);
                }
            });
        });

        $('#btnCancel').click(function () {
            history.go(-1);
        })
    }
});
