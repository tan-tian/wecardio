/**
 * Created by Sebarswee on 2015/6/16.
 */
if (top != window) {
    top.window.location.reload();
}

seajs.use(['$', 'validate', 'msgBox', 'base64', 'rsa', 'cookie'], function ($, validate, msgBox, base64, rsa, cookie) {
    // 记住我
    $(".mainUlLink1").on('click', function() {
        var $this = $(this);
        var $rememberMe = $('#rememberMe');

        $this.toggleClass("dianji");
        if ($this.hasClass('dianji')) {
            $rememberMe.val(true);
        } else {
            $rememberMe.val(false);
        }
    });
    // 找回密码
    $(".mainUlLink2").on('click', function() {
        msgBox.exWindow.open({
            title : '&nbsp;',
            width : '430px',
            height : '325px',
            url : path + '/org/password/find'
        });
    });

    if ($.cookie("org.username") != null) {
        $('#username').val($.cookie("org.username"));
        $('#passwrd').focus();
    } else {
        $('#username').focus();
    }

    // 绑定回车事件
    $(document).on('keydown', function(event) {
        if (event.keyCode == 13) {
            $('#submitBtn').trigger('click');
        }
    });

    // 刷新验证码
    $("#captchaImage").on('click', function() {
        $(this).attr("src", path + "/guest/common/captcha?d=" + (new Date()).valueOf());
    });

    $('.mail-tips').find('a').on('click', function() {
        $.post(path + '/org/login/mail/resend', {username: $('#username').val()}, function (data) {
            msgBox.tips(data.content, null, null);
            if (data.type === 'success') {
                $('.mail-tips').slideUp();
            }
        });
    });

    // 验证
    var $form = $('#loginForm');
    $form.validate({
        rules : {
            username : {
                required : true
            },
            password : {
                required : true
            },
            captcha : {
                required : true
            }
        },
        errorPlacement : function(error, element) {
        },
        invalidHandler : function(event, validator) {
            var errorMap = validator.errorMap;
            $.each(errorMap, function(key, value) {
                var $this = $('#' + key);
                msgBox.warn($this.attr('data-value-desc') + ":" + value, null, null);
                $this.focus();
                return false;
            });
        }
    });

    // 登录按钮事件
    $('#submitBtn').on('click', function() {
        var $tips = $(".mail-tips");
        if (!$tips.is(":hidden")) {
            $tips.slideUp();
        }
        // 验证成功后
        if ($form.valid()) {
            var $enPassword = $("#enPassword");
            var $username = $("#username");
            var $password = $("#password");

            $.cookie("org.username", $username.val(), {expires: 7 * 24 * 60 * 60});

            var rsaKey = new rsa.RSAKey();
            rsaKey.setPublic(base64.b64tohex(modulus), base64.b64tohex(exponent));
            var enPassword = base64.hex2b64(rsaKey.encrypt($password.val()));
            $enPassword.val(enPassword);
            $.post($form.attr('action'), $form.serialize(), function (data) {
                if (data.type == 'success') {
                    window.location.href = path + '/org';
                } else if (data.type == 'warn') {
                    msgBox.warn(data.result['errorInfo'], null, null);
                    exponent = data.result['exponent'];
                    modulus = data.result['modulus'];
                    $("#captchaImage").trigger('click');
                    $('#captcha').val('');
                    $('#password').val('');
                    if (data.result['errorCode'] != null && data.result['errorCode'] === '0000') {
                        $tips.slideDown();
                    }
                }
            });
        }
    });
});