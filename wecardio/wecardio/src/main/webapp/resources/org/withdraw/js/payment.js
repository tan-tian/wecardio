/**
 * Created by tantian on 2015/8/12.
 */
seajs.use(['msgBox', 'jTemplates'], function (msgBox) {
    $(document).ready(function () {
        $(".xw_userMsgTableb div.xw_userMsgTab1").on('click', function () {
            $(".TableTd div.xw_userMsgTab").removeClass("a");
            $(this).addClass("a");
            $(".xw_zhifuType").show();
        });
        $(".xw_userMsgTableb div.xw_userMsgTab2").on('click', function () {
            $(".TableTd div.xw_userMsgTab").removeClass("a");
            $(this).addClass("a");
            $(".xw_zhifuType").hide();
        });
        $("#list").on('click', 'li.xw_zhifuTypeLi', function () {
            $("li.xw_zhifuTypeLi").removeClass("dianji");
            $(this).addClass("dianji");
        });

        // 新增银行账户
        $('#addBtn').on('click', function () {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '650px',
                height : '275px',
                url : path + '/org/withdraw/wizard?_target2=',
                close : function(obj) {
                    if (obj !== undefined && obj != null) {
                        obj['bankIcon'] = getBankIcon(obj['bankType']);
                        obj['bankNoWithMask'] = maskNo(obj['bankNo']);
                        var $li = $('<li class="xw_zhifuTypeLi"></li>').insertBefore($('li.zengjia'));
                        $li.setTemplateElement('template');
                        $li.processTemplate(obj);
                        $('li.zengjia').hide();
                    }
                }
            });
        });

        function getBankIcon(type) {
            switch (type) {
                case "0":
                    return "/resources/images/tixianshenqing/zhongguo.png";
                    break;
                case "1":
                    break;
                case "2":
                    return "/resources/images/tixianshenqing/gonghang.png";
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
            }
        }

        function maskNo(no) {
            var start = no.substring(0, 6);
            var end = no.substring(no.length - 4);
            var mask = '';
            for (var i = 0; i < no.length - 10; i++) {
                mask += '*';
            }
            return start + mask + end;
        }

        $('#submitBtn').on('click', function () {
            $('#submitType').val('submit');
        });
        $('#cancelBtn').on('click', function () {
            $('#submitType').val('cancel');
        });

        $('#form').submit(function() {
            if ($('#submitType').val() === 'submit') {
                var $form = $(this);
                var $type = $('div.xw_userMsgTab').filter('.a');
                if ($type.hasClass("xw_userMsgTab1")) {
                    var $bank = $('li.xw_zhifuTypeLi').filter('.dianji');
                    if ($bank.length === 0) {
                        msgBox.warn(requiredMsg, null, null);
                        return false;
                    } else {
                        $form.append(
                                '<input type="hidden" name="outInfo.type" value="0"/>' +
                                '<input type="hidden" name="outInfo.bankType" value="' + $bank.find('input[name="bankType"]').val() + '"/>' +
                                '<input type="hidden" name="outInfo.accountName" value="' + $bank.find('input[name="bankUsername"]').val() + '"/>' +
                                '<input type="hidden" name="outInfo.bankNo" value="' + $bank.find('input[name="bankNo"]').val() + '"/>' +
                                '<input type="hidden" name="outInfo.bankName" value="' + $bank.find('input[name="bankBranch"]').val() + '"/>'
                        );
                    }
                } else if ($type.hasClass("xw_userMsgTab2")) {
                    $form.append(
                            '<input type="hidden" name="outInfo.type" value="1"/>' +
                            '<input type="hidden" name="outInfo.bankType" value=""/>' +
                            '<input type="hidden" name="outInfo.accountName" value=""/>' +
                            '<input type="hidden" name="outInfo.bankNo" value=""/>' +
                            '<input type="hidden" name="outInfo.bankName" value=""/>'
                    );
                }
            }
        });
    });
});