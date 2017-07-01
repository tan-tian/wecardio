/**
 *<pre>
 * Description: PC端模块化必须使用的js文件
 * Author：Zhang zhongtao
 * Date：2014-11-01 13:55
 *</pre>
 */
define('common/pc/1.0/base', ['$', 'cookie/1.4.1/cookie', 'msgBox/1.0/msgBox', 'loadingTip/1.0/loadingTip'], function (require, exports, module) {
    //提示组件
    window.msgBox = require('msgBox/1.0/msgBox');
    window.loadingTip = require('loadingTip/1.0/loadingTip');
    var cookie = require('cookie/1.4.1/cookie');

    function getWindow(){
        return top;
    }

    function getMsgBox(){
        if(getWindow().msgBox){
            return getWindow().msgBox;
        }

        return msgBox;
    }

    function getLoadingTip(){
        if(getWindow().loadingTip){
            return getWindow().loadingTip;
        }

        return loadingTip;
    }

    if(typeof($cfg) == 'undefined'){
        window.$cfg = {eCont:{}};
    }

    if(typeof($pdp) == 'undefined'){
        window.$pdp = {};
    }
    if(typeof($FF) == 'undefined'){
        window.$FF = {};
    }

    //region 发送请求弹出加载提示

    $(window).ajaxStart(function () {
        //setLangInCookie();
        getLoadingTip().show();
    });

    $(window).ajaxStop(function () {
        getLoadingTip().hide();
    });

    //endregion

    //region 登陆超时、令牌失效处理
    $(window).ajaxComplete(function (event, request, settings) {
        var loginStatus = request.getResponseHeader("loginStatus");
        var tokenStatus = request.getResponseHeader("tokenStatus");

        if (loginStatus == "accessDenied") {
            getMsgBox().tips("登录超时或资源未授权，2秒后自动跳转到登陆页面");
            window.setTimeout(function () {
                var target = null;
                if(top == window){
                    target = window;
                }else{
                    target = top;
                }

                target.location.reload(true);

            }, 2000);
        } else if (tokenStatus == "accessDenied") {
            var token = $.cookie("token");
            if (token != null) {
                $.extend(settings, {
                    global: false, headers: {token: token}
                });
                $.ajax(settings);
            }
        }
    });

    // 令牌
    $(document).ajaxSend(function (event, request, settings) {
        if (!settings.crossDomain && settings.type != null && settings.type.toLowerCase() == "post") {
            var token = $.cookie("token");
            if (token != null) {
                request.setRequestHeader("token", token);
            }
        }
    });

    //endregion

    //region 失败错误处理
    $(document).ajaxError(function (event,xhr,options,exc) {
        getLoadingTip().hide();
        if (xhr.status == 0) {
            try {
                console.log('Ajax请求未发送：' + options.url);
            } catch (e) {}
        } else {
            getMsgBox().tips('操作失败!', 1.5);
        }
    });

    // 针对调用成功，但服务器返回的结果中包含失败操作的处理(message.type == error)
    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
        var oldSuccessFn = options.success;
        options.success = function (data) {
            //request.getResponseHeader("exception");
            if (data && data['type'] && data.type == 'error') {

                var errorMsg = '操作失败!';

                if (data.content) {
                    //console.log('异常信息content:' + data.content);
                }

                if (data.result) {
                    //console.log('异常信息result:' + data.result);
                }

                getMsgBox().tips(errorMsg, 1.5);
            } else {
                try {
                    //捕捉业务类异常
                    oldSuccessFn(data);
                } catch (error) {
                    //console.log('', error);
                }
            }
        }
    });

    //endregion
    $(document).ready(function() {
        $("form").submit(function() {
            var $this = $(this);
            if ($this.attr("method") != null && $this.attr("method").toLowerCase() == "post" && $this.find("input[name='token']").size() == 0) {
                var token = $.cookie("token");
                if (token != null) {
                    $this.append('<input type="hidden" name="token" value="' + token + '" \/>');
                }
            }
        });

        //setLangInCookie();
    });

    function setLangInCookie() {
        //var lang = (navigator.language || navigator.browserLanguage).replace('-','_');
        //$.cookie('language', lang);
    }
});