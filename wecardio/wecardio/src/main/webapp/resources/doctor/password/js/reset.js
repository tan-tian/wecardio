/**
 * Created by tantian on 2015/9/2.
 */
seajs.use(['msgBox', 'validate'], function (msgBox) {
    $(document).ready(function () {
        var $form = $('#form');
        $form.validate({
            rules: {
                oldPassword: {
                    required: true
                },
                newPassword: {
                    required: true,
                    minlength: 6
                },
                rePassword: {
                    required: true,
                    equalTo: "#newPassword"
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
    })
});