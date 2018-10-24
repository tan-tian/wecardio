/**
 * Created by tantian on 2015/7/14.
 */
seajs.use(['BaiduUploader','msgBox', 'select', 'validate', 'cookie'], function (BaiduUploader, msgBox) {
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
                    $img.attr('width', '230px');
                    $img.attr('height', '150px');
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

        /*$('#iOrgId').select({
            width : 166,
            url : path + '/'+userTypePath+'/organization/sel',
            onSelect : function(rec) {
                var id = rec.value;
                $('#iDoctorId').select('reload', path + '/'+userTypePath+'/doctor/sel?iOrgId='+id);
            },
            onUnselect : function(record) {
                $('#iDoctorId').select('reload', path + '/'+userTypePath+'/doctor/sel');
            }
        });*/

        //加载审核通过的主治医生
    if ($('#roles').val()!='patient')
    {
    	   $('#iDoctorId').select({
           	 width : 166,
           	 url : path + '/'+userTypePath+'/doctor/sel?iOrgId='+iOrgId+"&iType=2"
       		 });
    }
       
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
                    maxlength : 20,
                    mobile: true,
                   	remote : {
                        url: path + '/'+userTypePath+'/patient/checkMobile?iId='+iId,
                        cache : false
                    }
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
                    required : false
                },
                medicine : {
                    required : false
                },
                emailV : {
                    required : true,
                    email_mobile:true,
                    remote : {
                        url: path + '/'+userTypePath+'/patient/checkEmail_Mobile?iId='+iId,
                        cache : false
                    }
                }
            },
            messages : {
                emailV : {
                    remote : repeatEmail
                },
                mobile : {
                    remote : repeatMobile
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
                            history.go(-1);
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