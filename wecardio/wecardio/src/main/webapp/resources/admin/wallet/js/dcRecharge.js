/**
 * Created by Sebarswee on 2015/8/6.
 */
seajs.use(['validate'], function () {
    $(document).ready(function () {
        var $form = $('#form');
        var $amount = $('#amount');
        var $submitBtn = $('#submitBtn');
        var timeout;

        // 充值金额
        $amount.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                calculateFee();
            }
        });

        // 支付方式选择事件
        $("div.dianji1").on('click', function () {
            var $this = $(this);
            $("div.dianji1").not(this).removeClass("dianji2");
            $this.addClass("dianji2");
            $('#paymentPluginId').val($this.attr('paymentPluginId'));
            calculateFee();
        }).eq(0).trigger('click');

        // 计算支付手续费
        function calculateFee() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {

            }, 500);
        }

        $submitBtn.on('click', function() {
            $form.submit();
        });

        // 表单验证
        $form.validate({
            rules: {
                amount: {
                    required: true,
                    number: true,
                    min: 0.01
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
});