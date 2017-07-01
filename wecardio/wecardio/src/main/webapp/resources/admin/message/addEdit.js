/**
 * Created by Sebarswee on 2015/7/14.
 */
seajs.use(['msgBox', 'select', 'validate', 'cookie'], function (msgBox) {
    // 初始化表单
    initForm();

    // 初始化表单
    function initForm() {
       /* $('#type').select({
            width : 166,
            url : path + '/guest/enum/message/type?notIn=0,1,2,3,4,5,7,8,9'
        });*/

        $('#to_type').select({
            width : 166,
            url : path + '/guest/enum/message/UserType',
            onSelect : function(data) {
                var value = data['value'];
                var text = data['text'];
                $('#to_id').select('reload', path + '/'+userTypePath+'/message/select?to_type=' + value+'&iType=2');
            }
        });

        $('#to_id').select({
            width : 166,
            onSelect : function(data) {
                var value = data['value'];
                var text = data['text'];
                $('#to_name').val(text);
            }
        });

        var $form = $('#form');
        $form.validate({
            rules : {
                content : {
                    required : true,
                    maxlength : 1000
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
            submitHandler : function(form) {
                $.ajax({
                    url: $(form).attr('action'),
                    type: "POST",
                    data: $(form).serialize(),
                    dataType: "json",
                    success: function(message) {
                        top.msgBox.tips(message['content'], null, null);
                        if (message['type'] == "success") {
                            msgBox.exWindow.close(1);
                        }
                    }
                });
            }
        });
    }

    $('#submitBtn').on('click', function() {
        $('#form').submit();
    });

    $('#cancelBtn').on('click', function() {
        msgBox.exWindow.close(null);
    });
});