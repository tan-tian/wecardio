seajs.use(['$', 'template'], function ($, template) {
    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        //最新医嘱记录
        $.post(path + '/patient/patient/getNewestOpinion', {}, function (data) {
            var html = template('templateNewestOpinion', data);
            var $target = $('#newestOpinion').append(html);
            imgLoadError($target);
        });

        //最新活动记录
        $.post(path + '/patient/patient/getNewestActivity', {}, function (data) {
            var html = template('templateActivity', data);
            var $target = $('#newestActivity').append(html);
            imgLoadError($target);
        });

        //个人信息
        $.post(path + '/patient/patient/getCurrent', {}, function (data) {
            iPatientId = data.id;
            var html = template('templatePatientMsg', data);
            $('#patientTitle').after(html);
            imgLoadError($('#patientContainer'));

            //解除绑定
            $("#releaseBinding").on('click', function() {
                msgBox.confirm({
                    msg : comfireReleaseBinding,
                    callback : function(btnType) {
                        if (btnType == msgBox.ButtonType.OK) {
                            $.post(path + '/'+userTypePath+'/patient/releaseBinding', { ids : iPatientId }, function(message) {
                                msgBox.tips(message['content'], null, null);
                                if (message['type'] == 'success') {
                                    window.location.href = path + '/patient/home';
                                }
                            });
                        }
                    }
                });
            });
            //编辑患者
            $("#editPatient").on('click', function() {
                window.location.href = path + '/patient/patient/addEdit/' + iPatientId + '?iType=1';
            });
        });

        //最新检查记录
        $.post(path + '/patient/patient/getNewestRecord', {}, function (data) {
            var html = template('templateList', data);
            $('#patientRecodeName').after(html);
        });
    }

    function imgLoadError($container) {
        $container.find('img').bind('error', function () {
            //错误修改标记
            if ($(this).data('isModify')) {
                return;
            }
            $(this).data('isModify', true).attr('src', path + '/resources/images/user01.png');
        });
    }

    function initEvent() {
        $('#patientRecodeName').click(function () {
            window.location.href = path + '/patient/record';
        });

        $('#recordList').on('click', '.record', function () {
            window.location.href = path + '/patient/record/' + $(this).attr('data-id') + '/view';
        });

        $('#patientContainer').on('click','.bingshi_tittle', function () {
            window.location.href = path + '/patient/patient/toDetail';
        })
    }
});