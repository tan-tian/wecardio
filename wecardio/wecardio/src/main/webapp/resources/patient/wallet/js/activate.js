/**
 * Created by Sebarswee on 2015/8/4.
 */
seajs.use(['validate', 'base64', 'rsa'], function (validate, base64, rsa) {
    $(document).ready(function() {
        var $form = $('#form');
        $form.validate({
            rules : {
                password: {
                    required: true,
                    minlength: 6
                },
                rePassword: {
                    required: true,
                    equalTo: "#password"
                }
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
                    enPassword : $('#enPassword').val()
                };
                $.post($form.attr('action'), datas, function (message) {
                    if (message['type'] != 'success') {
                        msgBox.warn(message['content'], null, null);
                    } else {
                        msgBox.tips(message['content'], 1, function() {
                            window.location.href = path + '/patient/wallet';
                        });
                    }
                });
            }
        });
    });
});