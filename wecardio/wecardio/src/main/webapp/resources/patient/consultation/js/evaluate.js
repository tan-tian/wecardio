/**
 * Created by Sebarswee on 2015/9/4.
 */
seajs.use(['msgBox', 'validate', 'select'], function (msgBox) {
    $(document).ready(function () {
        $('#score').select({
            url : path + '/guest/enum/evaluate/score'
        });
        var $form = $('#form');
        $form.validate({
            rules: {
                score: {
                    required: true
                },
                content: {
                    required: true,
                    maxlength: 1024
                }
            },
            ignore: ".ignore",
            errorPlacement: function(error, element) {
                if (element.is('hidden')) {
                    element.closest('td').append(error);
                } else {
                    error.insertAfter(element);
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