/**
 * Created by Sebarswee on 2015/8/24.
 */
seajs.use(['BaiduUploader', 'msgBox', 'validate', 'cookie', 'select'], function (BaiduUploader, msgBox) {
    $(document).ready(function () {
        // 初始化表单
        initForm();

        var headUploader = BaiduUploader.create({
            auto : true,
            server : path + '/file/upload',
            formData : {
                fileType : 0,
                token : $.cookie("token")
            },
            pick : {
                id : '#headPicker'
            },
            resize : false,
            accept : {
                title : 'Images',
                extensions : 'jpg,jpeg,bmp,png',
                mimeTypes : 'image/*'
            },
            fileNumLimit : 1,
            compress : false
        });

        headUploader.on('uploadSuccess', function (file, response) {
            // open crop window
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '375px',
                height : '424px',
                url : path + '/file/crop?image=' + response['url'],
                close : function (result) {
                    if (result) {
                        var $img = $('#head');
                        $img.attr('src', path + '/file?path=' + result).show();
                        $('#headPath').val(result);
                    }
                }
            });
            // remove file
            headUploader.removeFile(file);
        });

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
            width: 166,
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
            width: 166,
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
            width: 166,
            url: cityUrl,
            isShowQuery: true,
            onSelect: function (data) {
                var text = data['text'];
                $cityName.val(text);
            }
        });

        var $form = $('#form');
        $form.validate({
            rules : {
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
                    required: true
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
                            window.location.href = path + '/doctor/home';
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