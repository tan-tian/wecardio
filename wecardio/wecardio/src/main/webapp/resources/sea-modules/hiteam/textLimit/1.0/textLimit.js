/**
 * Created by Sebarswee on 2015/9/18.
 */
define('hiteam/textLimit/1.0/textLimit', ['$', 'hiteam/textLimit/1.0/{theme}/css/textLimit.css'], function (require, exports, module) {
    'use strict';

    // 初始化
    function init(target) {
        var state = $.data(target, 'textLimit');
        var opts = state.options;

        var wrapper = $('<div class="hiteam-textarea-wrapper"></div>');
        var $info = $('<div class="hiteam-textarea-info"></div>');
        var displayMsg = opts.displayMsg;
        displayMsg = displayMsg.replace(/{remain}/, opts.max);
        $(target).wrap(wrapper);
        $info.html(displayMsg);
        $(target).parent('.hiteam-textarea-wrapper').append($info);
        state.wrapper = $(target).parent('.hiteam-textarea-wrapper');

        $(target).on('keyup focus', function () {
            setInfo(target);
        });
    }

    // 设置信息
    function setInfo(target) {
        var state = $.data(target, 'textLimit');
        var opts = state.options;
        var wrapper = state.wrapper;
        var disMsg = opts.displayMsg;
        var val = $(target).val();
        var remain = opts.max - val.length;
        if (remain <= 0) {
            val = val.substring(0, opts.max);
            remain = 0;
            $(target).val(val);
        }
        disMsg = disMsg.replace(/{remain}/, remain);
        wrapper.find(".hiteam-textarea-info").html(disMsg);
    }

    $.fn.textLimit = function(options, param){
        if (typeof options == 'string') {
            var method = $.fn.select.methods[options];
            if (method) {
                return method(this, param);
            }
        }

        options = options || {};
        return this.each(function() {
            var state = $.data(this, 'textLimit');
            if (state) {
                $.extend(state.options, options);
                init(this);
            } else {
                $.data(this, 'textLimit', {
                    options: $.extend({}, $.fn.textLimit.defaults, options)
                });
                init(this);
            }
        });
    };

    $.fn.textLimit.defaults = {
        max: 200,
        displayMsg: '<h1>您还可以输入<b>{remain}</b>个字符</h1>'
    };
});