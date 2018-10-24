/**
 * Created by tantian on 2015/7/14.
 */
seajs.use(['BaiduUploader', 'msgBox', 'select', 'validate', 'cookie', 'textLimit'], function (BaiduUploader, msgBox) {
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

        var imageIndex = 0;
        var uploader = BaiduUploader.create({
            auto : true,
            server : path + '/file/upload',
            formData : {
                fileType : 0,
                token : $.cookie("token")
            },
            pick : {
                id : '#picker'
            },
            resize : false,
            accept : {
                title : 'Images',
                extensions : 'jpg,jpeg,bmp,png',
                mimeTypes : 'image/*'
            },
            fileNumLimit : 4,
            compress : false
        });

        uploader.on('fileQueued', function(file) {
            var $list = $('#imgList');
            var $li = $(
                        '<li id="' + file.id + '" style="padding-top:0px;">' +
                        '<img>' +
                        '</li>'
                ),
                $btns = $(
                        '<div class="file-panel">' +
                        '<span class="cancel">删除</span>' +
                        '</div>'
                ).appendTo( $li ),
                $img = $li.find('img'),
                $info = $('<p class="error"></p>'),
                showError = function( code ) {
                    var text;
                    switch( code ) {
                        case 'exceed_size':
                            text = '文件大小超出';
                            break;
                        case 'interrupt':
                            text = '上传暂停';
                            break;
                        default:
                            text = '上传失败，请重试';
                            break;
                    }
                    $info.text(text).appendTo($li);
                };

            // $list为容器jQuery实例
            $list.append( $li );

            $li.on('mouseenter', function() {
                $btns.stop().animate({ height: 30 });
            });

            $li.on('mouseleave', function() {
                $btns.stop().animate({ height: 0 });
            });

            $btns.on('click', 'span', function() {
                var index = $(this).index();

                switch (index) {
                    case 0:
                        uploader.removeFile( file );
                        return;
                }
            });

            if (file.getStatus() === 'invalid') {
                showError( file.statusText );
            } else {
                // 创建缩略图
                // 如果为非图片文件，可以不用调用此方法。
                uploader.makeThumb(file, function(error, src) {
                    if (error) {
                        $img.replaceWith('<span>不能预览</span>');
                        return;
                    }

                    $img.attr('src', src);
                }, 250, 180);
            }
        });

        uploader.onFileDequeued = function(file) {
            var $li = $('#' + file.id);
            $li.off().find('.file-panel').off().end().remove();
        };

        uploader.on('uploadSuccess', function (file, response) {
            var $li = $('#' + file.id);
            $(
                    '<input type="hidden" name="doctorImages[' + imageIndex + '].filePath" ' +
                    'value="' + response['url'] +
                    '"/>'
            ).appendTo( $li );
            imageIndex++;
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
            width : 166,
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
            width : 166,
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
            width : 166,
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

        $('#roles').select({
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

        $('#intro').textLimit({
            max: 1000,
            displayMsg: $('#msg').val()
        });

        var $form = $('#form');
        $form.validate({
            rules : {
                firstName : {
                    required : true,
                    maxlength : 50
                },
                secondName : {
                    required : true,
                    maxlength : 50
                },
                account : {
                    required : true,
                    email : true,
                    remote : {
                        url: path + "/org/doctor/checkEmail",
                        cache : false
                    }
                },
                password: {
                    required : true
                },
                roles : {
                    required : true
                },
                doctorBirthday : {
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
                ic : {
                    required : true,
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
                account : {
                    remote : "账号已存在"
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
                    beforeSend: function() {
                        $('#submitBtn').off('click');
                    },
                    complete: function() {
                        $('#submitBtn').on('click', function() {
                            $('#form').submit();
                        });
                    },
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
            $('#isSubmit').val(1);
            $('#form').submit();
        });
        $('#saveBtn').on('click', function() {
            $('#isSubmit').val(0);
            $('#form').submit();
        });
    }
});