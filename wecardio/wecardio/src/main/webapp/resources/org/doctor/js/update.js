/**
 * Created by Sebarswee on 2015/7/14.
 */
seajs.use(['msgBox', 'select', 'mulSelect', 'validate', 'cookie'], function (msgBox) {
    $(document).ready(function () {
        // 初始化表单
        initForm();
    });

    // 初始化表单
    function initForm() {
        var $nationName = $('#nationName'),
            $nationCode = $('#nationCode'),
            $provinceName = $('#provinceName'),
            $provinceCode = $('#provinceCode'),
            $cityName = $('#cityName'),
            $cityCode = $('#cityCode');
        var nationCode = $nationCode.val();
        var provinceCode = $provinceCode.val();
        $nationCode.select({
            url: path + '/guest/pub/country/countrySel',
            isShowQuery: true,
            onSelect: function (data) {
                var value = data['value'];
                var text = data['text'];
                $provinceCode.select('reload', path + '/guest/pub/province/provinceSel?countryId=' + value);
                $provinceCode.select('reset');
                $nationName.val(text);
            }
        });
        var provinceUrl = '';
        if (nationCode != null && nationCode != '') {
            provinceUrl = path + '/guest/pub/province/provinceSel?countryId=' + nationCode;
        }
        $provinceCode.select({
            url: provinceUrl,
            isShowQuery: true,
            onSelect: function (data) {
                var nationCode = $nationCode.val();
                var value = data['value'];
                var text = data['text'];
                $cityCode.select('reload', path + '/guest/pub/city/citySel?countryId=' + nationCode + '&provinceId=' + value);
                $provinceName.val(text);
            }
        });
        var cityUrl = '';
        if (nationCode != null && provinceCode != null
            && nationCode != '' && provinceCode != '') {
            cityUrl = path + '/guest/pub/city/citySel?countryId=' + nationCode + '&provinceId=' + provinceCode;
        }
        $cityCode.select({
            url: cityUrl,
            isShowQuery: true,
            onSelect: function (data) {
                var text = data['text'];
                $cityName.val(text);
            }
        });

        var sexData = function() {
            var options = [];
            $('#sex').find('option').each(function() {
                options.push({
                    text : $(this).text(),
                    value : $(this).val()
                });
            });
            return options;
        };
        $('#sex').select({
            width : 166,
            data : sexData()
        });

        $('#roles').mulSelect({
            width : 166,
            url : path + '/guest/enum/doctor/role',
            loadFilter: function(data) {
                var orgType = $('#orgType').val();
                if (orgType === 'personal') {
                    // 个人机构不允许添加主治医生
                    $.each(data, function (i, val) {
                        if (val.value == "2") {
                            data.splice(i, 1);
                            return false;
                        }
                    });
                }
                return data;
            }
        });

        var $form = $('#form');
        $form.validate({
            rules : {
                password : {
                    required : true
                },
                roles : {
                    required : true
                },
                mobile : {
                    required : true,
                    maxlength : 20
                },
                email : {
                    required : true,
                    email : true,
                    maxlength : 50
                },
                cityCode: {
                    required : true
                },
                address : {
                    required : true,
                    maxlength : 256
                },
                intro : {
                    required : true,
                    maxlength : 1000
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
                $.ajax({
                    url: $(form).attr('action'),
                    type: "POST",
                    data: $(form).serialize(),
                    dataType: "json",
                    success: function(message) {
                        top.msgBox.tips(message['content'], null, null);
                        if (message['type'] == "success") {
                            window.location.href = path + '/org/doctor';
                        }
                    }
                });
            }
        });

        $('#submitBtn').on('click', function() {
            $('#form').submit();
        });
    }
});