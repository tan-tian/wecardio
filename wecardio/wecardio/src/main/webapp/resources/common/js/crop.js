/**
 * Created by tantian on 2015/8/19.
 */
seajs.use(['msgBox', 'jcrop'], function (msgBox) {
    var scaleFactor = 1;
    var jcrop_api;

    $(document).ready(function () {
        var $img = $('#img');
        // 计算缩放比例开始
        var realWidth = $('#width').val();
        var realHeight = $('#height').val();

        // 画布大小
        var $container = $('#container');
        var canvasWidth = $container.width() - 2;
        var canvasHeight = $container.height();

        debugger;

        // 图片缩放比例
        if (realWidth > canvasWidth && realHeight > canvasHeight) {
            if (realWidth / canvasWidth > realHeight / canvasHeight) {
                scaleFactor = canvasWidth / realWidth;
            } else {
                scaleFactor = canvasHeight / realHeight;
            }
        }

        // 计算缩放比例结束
        $('#scale').val(scaleFactor);

        var currentWidth = Math.round(scaleFactor * realWidth);
        var currentHeight = Math.round(scaleFactor * realHeight);

        $img.css({
            width	: currentWidth + "px",
            height	: currentHeight + "px"
        });

        $img.attr("src", path + '/file?path=' + $('#image').val());
        if (jcrop_api) {
            jcrop_api.destroy();
        }
        $img.Jcrop({
            bgColor: 'black',
            bgOpacity: .4,
            aspectRatio : 1,
            onChange : showCoords,
            onSelect : showCoords
        }, function() {
            jcrop_api = this;
            jcrop_api.setSelect([0, 0, 100, 100]);
        });

        $('#submitBtn').on('click', function() {
            var $form = $('#form');
            $.ajax({
                url: $form.attr('action'),
                type: "POST",
                data: $form.serialize(),
                dataType: "json",
                success: function(message) {
                    msgBox.tips(message['content'], 1, function() {
                        if (message['type'] == 'success') {
                            msgBox.exWindow.close({
                                result : message['result']
                            });
                        }
                    });
                }
            });
        });
        $('#cancelBtn').on('click', function() {
            msgBox.exWindow.close(null);
        });
    });

    /**
     * 响应Jcrop选框触发事件
     * @param c
     */
    function showCoords(c) {
        $('#x').val(c.x);
        $('#y').val(c.y);
        $('#w').val(c.w);
        $('#h').val(c.h);
    }
});