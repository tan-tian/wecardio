/**
 * Created by tantian on 2015/7/21.
 */
seajs.use(['msgBox'], function (msgBox) {
    $(document).ready(function () {
        $("li.xw_qizhi").on('click', function() {
            $("li.xw_qizhi").removeClass("qizhiA");
            $(this).addClass("qizhiA");
        });

        $('#submitBtn').on('click', function() {
            var flag = 0;
            $('li.xw_qizhi').each(function(i) {
                if ($(this).hasClass('qizhiA')) {
                    flag = i + 1;
                }
            });
            var url = path + '/patient/record/flag/set';
            var datas = {
                ids : $('#ids').val(),
                flag : flag
            };
            $.post(url, datas, function(message) {
                msgBox.tips(message['content'], 1, function() {
                    if (message['type'] == 'success') {
                        msgBox.exWindow.close(flag);
                    }
                });
            });
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });
});
