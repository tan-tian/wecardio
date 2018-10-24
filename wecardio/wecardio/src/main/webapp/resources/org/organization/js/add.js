/**
 * Created by tantian on 2015/6/29.
 */
seajs.use(['$', 'BaiduUploader', 'select', 'validate', 'cookie'], function ($, BaiduUploader) {
    // 初始化表单
    initForm();

    var orgImageIndex = 0;
    var orgUploader = BaiduUploader.create({
        auto : true,
        server : path + '/file/upload',
        formData : {
            fileType : 0,
            token : $.cookie("token")
        },
        pick : {
            id : '#orgPicker'
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

    orgUploader.on('fileQueued', function(file) {
        var $list = $('#orgImgList');
        var $li = $(
            '<h1 id="' + file.id + '" class="addimgDiv xw_addimgDiv">' +
            '<img>' +
            '</h1>'
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
            $btns.stop().animate({height: 30});
        });

        $li.on('mouseleave', function() {
            $btns.stop().animate({height: 0});
        });

        $btns.on('click', 'span', function() {
            var index = $(this).index();

            switch (index) {
                case 0:
                    orgUploader.removeFile( file );
                    return;
            }
        });

        if (file.getStatus() === 'invalid') {
            showError( file.statusText );
        } else {
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            orgUploader.makeThumb(file, function(error, src) {
                if (error) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }

                $img.attr('src', src);
            }, 168, 100);
        }
    });

    orgUploader.onFileDequeued = function(file) {
        var $li = $('#' + file.id);
        $li.off().find('.file-panel').off().end().remove();
    };

    orgUploader.on('uploadSuccess', function (file, response) {
        var $li = $('#' + file.id);
        $(
            '<input type="hidden" name="orgImages[' + orgImageIndex + '].filePath" ' +
            'value="' + response['url'] +
            '"/>'
        ).appendTo( $li );
        orgImageIndex++;
    });

    var doctorImageIndex = 0;
    var doctorUploader = BaiduUploader.create({
        auto : true,
        server : path + '/file/upload',
        formData : {
            fileType : 0,
            token : $.cookie("token")
        },
        pick : {
            id : '#doctorPicker'
        },
        resize : false,
        accept : {
            title : 'Images',
            extensions : 'jpg,jpeg,bmp,png',
            mimeTypes : 'image/*'
        },
        compress : false
    });

    doctorUploader.on('fileQueued', function(file) {
        var $list = $('#doctorImgList');
        var $li = $(
            '<h1 id="' + file.id + '" class="addimgDiv xw_addimgDiv">' +
            '<img>' +
            '</h1>'
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
            $btns.stop().animate({height: 30});
        });

        $li.on('mouseleave', function() {
            $btns.stop().animate({height: 0});
        });

        $btns.on('click', 'span', function() {
            var index = $(this).index();

            switch (index) {
                case 0:
                    doctorUploader.removeFile( file );
                    return;
            }
        });

        if (file.getStatus() === 'invalid') {
            showError( file.statusText );
        } else {
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            doctorUploader.makeThumb(file, function(error, src) {
                if (error) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }
                $img.attr('src', src);
            }, 168, 100);
        }
    });

    doctorUploader.onFileDequeued = function(file) {
        var $li = $('#' + file.id);
        $li.off().find('.file-panel').off().end().remove();
    };

    doctorUploader.on('uploadSuccess', function (file, response) {
        var $li = $('#' + file.id);
        $(
            '<input type="hidden" name="doctorImages[' + doctorImageIndex + '].filePath" ' +
            'value="' + response['url'] +
            '"/>'
        ).appendTo( $li );
        doctorImageIndex++;
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

    var typeData = function() {
        var options = [];
        $('#type').find('option').each(function() {
            options.push({
                text : $(this).text(),
                value : $(this).val()
            });
        });
        return options;
    };
    $('#type').select({
        data : typeData()
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
        data : sexData()
    });

    var doctorSexData = function() {
        var options = [];
        $('#doctorSex').find('option').each(function() {
            options.push({
                text : $(this).text(),
                value : $(this).val()
            });
        });
        return options;
    };
    $('#doctorSex').select({
        data : doctorSexData()
    });

    var $form = $('#form');
    $form.validate({
        rules : {
            name : {
                required : true,
                maxlength : 100
            },
            type : {
                required : true
            },
            code : {
                required : true,
                maxlength : 100
            },
            cityCode: {
                required : true
            },
            address : {
                required : true,
                maxlength : 200
            },
            contact : {
                required : true,
                maxlength : 20
            },
            telephone : {
                required : true,
                maxlength : 20
            },
            email : {
                required : true,
                email : true,
                maxlength : 50
            },
            zoneCode : {
                required : true,
                maxlength : 20
            },
            firstName : {
                required : true,
                maxlength : 50
            },
            secondName : {
                required : true,
                maxlength : 50
            },
            ic : {
                required : true,
                maxlength : 50
            },
            intro : {
                required : true,
                maxlength : 4000
            },
            doctorFirstName : {
                required : true,
                maxlength : 50
            },
            doctorSecondName : {
                required : true,
                maxlength : 50
            },
            doctorIc : {
                required : true,
                maxlength : 50
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
                        window.location.href = path + '/org/organization';
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