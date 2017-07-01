/**
 * <pre>
 * Description: 非Html5浏览器的placeholder特性支持，当文本框值为空时，设置为placeholder的值
 * Author：Zhang zhongtao
 * Date：2014-11-11 10:56
 * </pre>
 */
define('placeholder/1.0/placeholder', ['$'], function (require, exports,
                                                             module) {
    // input 是否支持 placeholder
    var isInputSupported = 'placeholder' in document.createElement('input');

    var valHooks = $.valHooks;
    var hooks;
    var placeholder;
    if (isInputSupported) {
        // 本身已支持 placeholder
        placeholder = $.fn.placeholder = function () {
            return this;
        }
    } else {
        placeholder = $.fn.placeholder = function () {
            var $this = $(this);
            $this.filter(':input[placeholder]')
                .not('.placeholder')
                .not(':password')   // TODO password的处理
                .on('focus.placeholder', clearPlaceholder)
                .on('blur.placeholder', setPlaceholder)
                .data('placeholder-enabled', true)
                .trigger('blur.placeholder');
            return $this;
        };

        hooks = {
            'get': function (element) {
                var $element = $(element);
                return $element.data('placeholder-enabled') && $element.hasClass('placeholder') ? '' : element.value;
            },
            'set': function (element, value) {
                var $element = $(element);

                if (!$element.data('placeholder-enabled')) {
                    return element.value = value;
                }
                if (value === '') {
                    element.value = value;
                    if (element != safeActiveElement()) {
                        setPlaceholder.call(element);
                    }
                } else if ($element.hasClass('placeholder')) {
                    clearPlaceholder.call(element, true, value) || (element.value = value);
                } else {
                    element.value = value;
                }
                return $element;
            }
        };

        if (!isInputSupported) {
            valHooks.input = hooks;
        }
    }

    /**
     * 清除placeholder
     * @param event
     * @param value
     */
    function clearPlaceholder(event, value) {
        var input = this;
        var $input = $(input);
        if (input.value == $input.attr('placeholder')) {
            input.value = '';
            $input.removeClass('placeholder');
            input == safeActiveElement() && input.select();
        }
    }

    function safeActiveElement() {
        try {
            return document.activeElement;
        } catch (exception) {
        }
    }

    /**
     * 设置 placeholder
     */
    function setPlaceholder() {
        var input = this;
        var $input = $(input);
        if (input.value === '') {
            $input.addClass('placeholder');
            $input[0].value = $input.attr('placeholder');
        } else {
            $input.removeClass('placeholder');
        }
    }

    //默认起作用
    $(function(){
        // 支持 placeholder
        if (typeof $.fn.placeholder != 'undefined') {
            $('input, textarea').placeholder();
        }
    });

});