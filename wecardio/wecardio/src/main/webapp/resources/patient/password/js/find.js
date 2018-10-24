/**
 * Created by tantian on 2015/10/15.
 */
seajs.use(['msgBox', 'validate'], function (msgBox) {
    $(document).ready(function() {
        // 刷新验证码
        $("#captchaImage").on('click', function() {
            $(this).attr("src", path + "/guest/common/captcha?d=" + (new Date()).valueOf());
        });

        var $form = $('#form');
        $form.validate({
            rules: {
                account: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                captcha: {
                    required: true
                }
            },
            submitHandler: function (form) {
                var $form = $(form);
                $.ajax({
                    url: $form.attr("action"),
                    type: "POST",
                    data: $form.serialize(),
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        msgBox.tips(message['content'], 1, function () {
                            if (message['type'] == 'success') {
                                msgBox.exWindow.close(true);
                            } else {
                                $("#captchaImage").trigger('click');
                            }
                        });
                    }
                });
            }
        });

        $('#submitBtn').on('click', function () {
            $('#form').submit();
        });
        $('#cancelBtn').on('click', function () {
            msgBox.exWindow.close(null);
        });
    });
});