seajs.use(['$', 'msgBox'], function ($, msgBox) {
    $(function () {
        $('#btnSubmit').click(function () {
            msgBox.exWindow.close({
                result: {
                    remark:$('#remark').val()
                }
            });
        });

        $('#btnCancel').click(function () {
            msgBox.exWindow.close();
        });
    });
});