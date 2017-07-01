seajs.use(['$', 'msgBox', 'validate'], function ($, msgBox, validate) {
    var modelPath = path + '/' + userPath;

    $(function () {
        $('#btnConfirmBuy').click(function () {
            msgBox.exWindow.open({
                title: $('#payTitle').val(),
                url: modelPath + '/service/toPage/pay?type=service&id=' + $('#id').val(),
                width: '400px',
                height: '290px',
                close: function (data) {
                    if(data) {
                        window.history.go(-1);
                    }
                }
            });
        });

        //充值
        $('#btnRecharge').click(function () {
            window.location.href = path + '/patient/wallet/recharge';
        });
    });
});