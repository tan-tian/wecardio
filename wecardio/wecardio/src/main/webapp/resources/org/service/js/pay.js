seajs.use(['$', 'msgBox', 'validate', 'base64', 'rsa'], function ($, msgBox, validate, base64, rsa) {
    var modelPath = path + '/' + userPath;

    $(function () {

        var $form = $('#form');

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
                    $(event.currentTarget).find('[name="' + v + '"]').focus();
                    msgBox.warn(validator.errorMap[v]);
                    break;
                }
            }
        });

        //确认
        $('#btnConfirm').click(function () {

            if (!$form.valid()) {
                return;
            }

            var rsaKey = new rsa.RSAKey();
            rsaKey.setPublic(base64.b64tohex($('#modulus').val()), base64.b64tohex($('#exponent').val()));
            var enPassword = base64.hex2b64(rsaKey.encrypt($('#pwd').val()));

            $.post(modelPath + '/service/package/service/pay', {
                enPassword: enPassword,
                id: $('#id').val()
            }, function (data) {
                if (data.type == 'success') {
                    msgBox.tips(data.content, null, function () {
                        msgBox.exWindow.close(true);
                    });
                } else {
                    msgBox.warn(data.content);
                }
            });
        });

        //取消
        $('#btnCancel').click(function () {
            msgBox.exWindow.close();
        });
    });
});