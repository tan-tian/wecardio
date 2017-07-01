seajs.use(['$', 'msgBox', 'validate'], function ($, msgBox, validate) {
    var cacheData = null;
    var $form1 = null;

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        $form1 = $('#form1');

        cacheData = getPackMgr();

        if (cacheData.form1) {
            $.each(cacheData.form1SerializeArray, function (i, n) {
                var $ctr = $('#' + n.name);
                if ($ctr.length) {
                    $ctr.val(n.value);
                }
            });
        }

        $form1.validate({
            ignore: ".ignore",
            debug: false,
            onsubmit: true,
            onfocusout: false,
            onkeyup: false,
            errorPlacement: function (error, element) {
            },
            invalidHandler: function (event, validator) {
                for (v in validator.errorMap) {
                    $(event.currentTarget).find('[name="' + v + '"]:first').focus();
                    msgBox.warn(validator.errorMap[v]);
                    break;
                }
            }
        });
    }

    function initEvent() {

        $('#btnNext').click(function () {
            if (!$form1.valid()) {
                return;
            }

            cacheData.form1 = $form1;
            cacheData.form1SerializeArray = $form1.serializeArray();
            window.location.href = path + '/' + userPath + '/service/toPage/package2?id=' + $('#id').val();
        });

        $('#btnCancel').click(function () {
            cacheData.form1 = null;
            cacheData.form1SerializeArray = null;
            window.location.href = path + '/' + userPath + '/service/toPage/packageMgr';
        })
    }

    function getPackMgr() {
        var servicePackMgr = window.top.servicePackMgr;
        if (!servicePackMgr) {
            servicePackMgr = {
                form1: null,
                form1SerializeArray:null,
                /**包对应的服务项*/
                form2: null,
                form2SerializeArray:null,
            };
            window.top.servicePackMgr = servicePackMgr;
        }

        return servicePackMgr;
    }
});