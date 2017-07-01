/**
 * 引用jquery验证框架，由于jquery validate不合乎seajs规范，所以此文件定为模块文件，再引用其他文件
 */
define('validation/1.13.1/validate',
    ['$', 'validation/1.13.1/jquery.validate',
        'validation/1.13.1/css/reset.css',
        'validation/1.13.1/css/cmxformTemplate.css',
        'validation/1.13.1/css/cmxform.css'],

    function (require, exports, module) {

        var $ = require('$');

        seajs.use('validation/1.13.1/localization/messages_' + (language || 'zh_CN'));
        //region 处理不支持html5的浏览器中的placeholder验证问题

        //判断浏览器是否支持placeholder特性
        var hasPlaceholderSupport = function () {
            var attr = "placeholder";
            var input = document.createElement("input");
            return attr in input;
        };

        var support = hasPlaceholderSupport();

        if (!support) {

            $.validator.addMethod("required4PlaceHolder", function (value, element, params) {
                if (value == $(element).attr('placeholder')) {
                    return false;
                }
                return true;
            }, $.validator.messages.required);
        } else {
            $.validator.addMethod("required4PlaceHolder", function (value, element, params) {
                return true;
            }, $.validator.messages.required);
        }

        //endregion
    }
);
