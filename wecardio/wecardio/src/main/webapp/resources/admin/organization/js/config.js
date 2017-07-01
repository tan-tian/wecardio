/**
 * Created by Sebarswee on 2015/7/10.
 */
seajs.use(['msgBox', 'validate'], function (msgBox) {
    $(document).ready(function () {
        $('#form').validate({
            rules : {
                rate : {
                    required : true,
                    digits : true,
                    min : 0,
                    max : 100
                }
            },
            ignore: ".ignore",
            errorPlacement: function(error, element) {
                $('#msg').append(error);
            },
            submitHandler : function(form) {
                var $form = $(form);
                var url = $form.attr('action');
                $.ajax({
                    url: url,
                    type: "POST",
                    data: $form.serialize(),
                    dataType: "json",
                    success: function(message) {
                        msgBox.tips(message['content'], 1, function() {
                            if (message['type'] == 'success') {
                                msgBox.exWindow.close(null);
                            }
                        });
                    }
                });
            }
        });
        // buttons
        $('#submitBtn').on('click', function() {
            $('#form').submit();
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });
});