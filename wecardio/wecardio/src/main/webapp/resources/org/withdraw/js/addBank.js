/**
 * Created by Sebarswee on 2015/8/12.
 */
seajs.use(['msgBox', 'select', 'validate'], function (msgBox) {
    $.fn.serializeJSON = function () {
        var serializedObject, formAsArray;
        formAsArray = this.serializeArray(); // {name, value}
        serializedObject = {};
        $.each(formAsArray, function (i, input) {
            serializedObject[input.name] = input.value;
        });
        return serializedObject;
    };

    $(document).ready(function () {
        $('#bankType').select({
            url : path + '/guest/enum/bank/type',
            onSelect : function (record) {
                $('#bankName').val(record.text);
            },
            onLoadSuccess : function () {
                $('#bankType').select('select', 0);
            }
        });
        var $form = $('#form');
        $form.validate({
            rules : {
                accountName : {
                    required : true,
                    maxlength : 50
                },
                bankNo : {
                    required : true,
                    digits : true,
                    maxlength : 100
                }
            },
            messages : {
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
                msgBox.exWindow.close({
                    result : $(form).serializeJSON()
                });
            }
        });
        $('#submitBtn').on('click', function() {
            $form.submit();
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });
});