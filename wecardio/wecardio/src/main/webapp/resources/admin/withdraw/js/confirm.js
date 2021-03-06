/**
 * Created by tantian on 2015/8/14.
 */
seajs.use(['msgBox'], function (msgBox) {
    $(document).ready(function () {
        // buttons
        $('#submitBtn').on('click', function() {
            var $form = $('#form');
            var url = $form.attr('action');
            $.post(url, $form.serialize(), function(message) {
                msgBox.tips(message['content'], 1, function() {
                    if (message['type'] == 'success') {
                        msgBox.exWindow.close(true);
                    }
                });
            });
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });
});