/**
 * Created by tantian on 2015/7/14.
 */
seajs.use(['BaiduUploader', 'select', 'validate', 'cookie'], function (BaiduUploader) {
    // 初始化表单
    initForm();

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
        fileNumLimit : 4 - $('#imgList').find('img').length,
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

    // 初始化表单
    function initForm() {
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

        $('#iDoctorId').select({
            width : 166,
            url : path + '/'+userTypePath+'/doctor/sel'
        });

        $('#iOrgId').select({
            width : 166,
            url : path + '/'+userTypePath+'/organization/sel'
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
                mobile : {
                    required : true,
                    maxlength : 20
                },
                medicine : {
                    maxlength : 1000
                },
                indication : {
                    maxlength : 1000
                },
                address : {
                    required : true
                },
                dateBirthday : {
                    required : true
                },
                sex : {
                    required : true
                },
                iDoctorId : {
                    required : true
                },
                iOrgId : {
                    required : true
                },
                indication : {
                    required : true
                },
                medicine : {
                    required : true
                },
                emailV : {
                    required : true,
                    email : true,
                    remote : {
                        url: path + '/'+userTypePath+'/patient/checkEmail?iId='+iId,
                        cache : false
                    }
                }
            },
            messages : {
                emailV : {
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
                    success: function(message) {
                        top.msgBox.tips(message['content'], null, null);
                        if (message['type'] == "success") {
                            window.location.href = path + '/'+userTypePath+'/patient';
                        }
                    }
                });
            }
        });
    }

    $('#submitBtn').on('click', function() {
        $('#form').submit();
    });

});