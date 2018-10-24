/**
 * Created by tantian on 2015/7/10.
 */
seajs.use(['msgBox'], function (msgBox) {
    // 图片事件
    $('ul.picDivList img').on('click', function() {
        var $this = $(this);
        var large = $this.attr('large');
        var source = $this.attr('source');
        $('img.xw_pic').attr('src', large).attr('source', source);
    });
    $('img.xw_pic').on('click', function () {
        var $this = $(this);
        var source = $this.attr('source');
        if (source != null && source != "") {
            window.open(source);
        }
    });
    $('h1.yszs img').on('click', function () {
        var $this = $(this);
        var source = $this.attr('source');
        if (source != null && source != "") {
            window.open(source);
        }
    });
    $('#doctorHead').find('img').on('click', function () {
        var $this = $(this);
        var source = $this.attr('source');
        if (source != null && source != "") {
            window.open(source);
        }
    });

    $('#stopBtn').on('click', stop);
    $('#openBtn').on('click', reopen);
    $('#configBtn').on('click', rateConfig);

    // 停止
    function stop() {
        confirmStop(function() {
            $.post(path + '/admin/organization/stop', { oid : $('#oid').val() }, function(message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    window.location.reload(true);
                }
            });
        });
    }

    // 重开
    function reopen() {
        confirmOpen(function() {
            $.post(path + '/admin/organization/open', { oid : $('#oid').val() }, function(message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    window.location.reload(true);
                }
            });
        });
    }

    // 分成设置
    function rateConfig() {
        msgBox.exWindow.open({
            title : '&nbsp;',
            width : '400px',
            height : '200px',
            url : path + '/admin/organization/config?oid=' + $('#oid').val()
        });
    }
});