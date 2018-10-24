/**
 * Created by tantian on 2015/7/23.
 */
seajs.use(['msgBox', 'base64', 'rsa', 'validate'], function (msgBox, base64, rsa) {
    $(document).ready(function() {
        var $form = $('#form');

        $form.validate({
            rules : {
                password : {
                    required : true
                }
            },
            errorPlacement: function(error, element) {
                $('#msg').append(error);
            }
        });

        $('#submitBtn').on('click', function() {
            // 验证成功后
            if ($form.valid()) {
                var $enPassword = $("#enPassword");
                var $password = $("#password");
                var rsaKey = new rsa.RSAKey();
                rsaKey.setPublic(base64.b64tohex(modulus), base64.b64tohex(exponent));
                var enPassword = base64.hex2b64(rsaKey.encrypt($password.val()));
                $enPassword.val(enPassword);

                var datas = {
                    rid : $('#rid').val(),
                    oid : $('#oid').val(),
                    type : $('#type').val(),
                    serviceType : $('#serviceType').val(),
                    enPassword : $('#enPassword').val()
                };
                $.post($form.attr('action'), datas, function (message) {
                    if (message['type'] != 'success') {
                        msgBox.warn(message['content'], null, null);
                    } else {
                        msgBox.tips(message['content'], 1, function() {
                            msgBox.exWindow.close(true);
                        });
                    }
                });
            }
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });
});