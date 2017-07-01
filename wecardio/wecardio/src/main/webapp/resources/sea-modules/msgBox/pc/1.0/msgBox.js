/**
 * 消息提示框模块PC版<br>
 * 提供以下功能：<br>
 * 1、提示框alert<br>
 * 2、确认提示框confirm<br>
 * 3、消息提示框tips
 * 4、警告提示框warn
 */
define(
    'msgBox/pc/1.0/msgBox',
    ['$', 'msgBox/pc/1.0/lang/{language}', 'layer/1.9.1/layer', 'msgBox/pc/1.0/css/msgBox.css'],
    function (require, exports, module) {
        // 弹出层组件
        var layer = require('layer/1.9.1/layer');
        // 临时ID
        var tempId = 0;

        function getHandlerFunName(idx){
            return msgBox.exWindow.tempId + "_Handler_" + idx;
        }

        function getExWindowOptions(options){
            if(!$.isPlainObject(options)){
                options = {id: null, result: options};
            }

            options = options || {id: null, result: null};

            if (!options.id) {
                //根据名称获取索引
                options.id = (window != top ? top : window).layer.getFrameIndex(window.name);
            }

            return options;
        }

        /**
         * 消息提示框
         */
        var msgBox = {

            /** 按钮类型 */
            ButtonType: {
                /** 确定 */
                OK: 'ok',
                /** 取消 */
                CANCEL: 'cancel'
            },

            /**
             * 警告提示框
             *
             * @param options
             *            支持字符串类型和对象类型 对象类型格式：
             *
             * <pre>
             * {
				 * 	title : '这是标题',// 可选
				 * 	msg : '这是内容',// 必须
				 * 	showCloseBtn:true,按钮左上
				 * 	callback : function() {
				 * 	}// 这是回调函数，可选
				 * }
             * </pre>
             *
             * 字符串类型则代表只有内容部分。
             */
            alert: function (options) {
                if (window != top) {
                    top.seajs.use(module.id, function (msgBox) {
                        msgBox.alert(options);
                    });
                } else {

                    if (options.showCloseBtn) {
                        var id = new Date().getTime() + "notice";
                        $('body').append(
                            '<ul id="' + id + '" class="layer_notice layui-layer-wrap" style="display: none;">' +
                            '<li>' + options.msg + '</li></ul>');
                        var $notice = $('#' + id);

                        //捕获页
                        layer.open({
                            type: 1,
                            shade: false,
                            title: false, //不显示标题
                            content: $notice,
                            cancel: function (index) {
                                layer.close(index);
                                $notice.remove();
                                options.callback && options.callback(msgBox.ButtonType.OK);
                            }
                        });

                    } else {
                        var param = $.extend({title: options.title || msgBoxLanguage.confirmTitle}, options);
                        layer.alert(options.msg, param, function (index) {
                            layer.close(index);
                            options.callback && options.callback(msgBox.ButtonType.OK);
                        });
                    }

                    tempId++;
                }
            },

            /**
             * 确认提示框，包含确定、取消按钮
             *
             * @param options
             *            选项，格式：
             *
             * <pre>
             * {
				 * 	title : '这是标题',// 可选
				 * 	msg : '这是内容',// 必须
				 * 	callback : function(btnType) {
				 * 	}// 这是回调函数，可选，btnType为点击按钮类型，具体参考ButtonType枚举
				 * }
             * </pre>
             *
             */
            confirm: function (options) {
                if (window != top) {
                    top.seajs.use(module.id, function (msgBox) {
                        msgBox.confirm(options);
                    });
                } else {
                    var param = $.extend({title: (options.title || msgBoxLanguage.confirmTitle), btn: [msgBoxLanguage.ok, msgBoxLanguage.no]}, options);
                    param.title = (options.title || msgBoxLanguage.confirmTitle);
                    //询问框
                    layer.confirm(options.msg, param, function (index) {
                        layer.close(index);
                        options.callback && options.callback(msgBox.ButtonType.OK);
                    }, function (index) {
                        layer.close(index);
                        options.callback && options.callback(msgBox.ButtonType.CANCEL);
                    });

                    tempId++;
                }
            },
            /**
             * 警告
             * @param msg
             * @param time
             * @param callback
             */
            warn: function (msg, time, callback) {
                if (window != top) {
                    top.seajs.use(module.id, function (msgBox) {
                        msgBox.warn(msg, time, callback);
                    });
                } else {
                    layer.msg(msg, {
                        shift: 6,
                        time: time ? Math.ceil(time) * 1000 : 3000
                    }, function () {
                        callback && callback();
                    });
                    tempId++;
                }
            },
            /**
             * 消息提示框，会自动关闭
             *
             * @param msg
             *            提示内容
             * @param time
             *            显示秒数，整数类型
             * @param callback
             *            回调函数
             */
            tips: function (msg, time, callback) {
                if (window != top) {
                    top.seajs.use(module.id, function (msgBox) {
                        msgBox.tips(msg, time, callback);
                    });
                } else {
                    layer.msg(msg, {
                        //icon: 1,
                        time: time ? Math.ceil(time) * 1000 : 2000
                    }, function () {
                        callback && callback();
                    });
                    tempId++;
                }
            },
            /**
             * 窗口对象
             */
            exWindow: {
                tempId:'exWindowResult',
                /***
                 * 打开页面
                 * @param options
                 * {
				 * 	title:'窗口标题',
				 * 	url:'http地址',
				 * 	width:'宽度px',
				 * 	height:'高度px',
				 * 	success:'成功打开时的回调函数,会回传当前的窗口标识ID',
				 * 	close:'窗口关闭的时候，回调函数'
				 * }
                 * @return 返回当前窗口ID，用于关闭窗口的标识
                 */
                open: function (options) {
                    var _this = this;
                    var idx = null;
                    if (window != top) {
                        top.seajs.use(module.id, function (msgBox) {
                            idx = msgBox.exWindow.open(options);
                        });
                    } else {
                        var params = {
                            type: 2,
                            title: options.title || false,
                            shadeClose: false,
                            shade: 0.5,
                            zIndex: 1000,
                            area: 'auto',
                            content: options.url,
                            success: function (layero, index) {
                                options.success && options.success(index);
                                //没设置高度，则进行自适应控制
                                if (!options.height) {
                                    top.layer.iframeAuto(index);
                                    var $iframe = $('#layui-layer-iframe' + index);
                                    var height = $iframe.outerHeight();
                                    $iframe.css({height: height + 10});
                                }
                            },
                            end: function () {
                                try {
                                    options.close && options.close(top[_this.tempId]);
                                } finally {
                                    //清理资源
                                    delete top[_this.tempId];
                                    delete top[getHandlerFunName(options.id)];
                                }
                            }
                        };

                        if (!options.height && options.width) {
                            params.area = options.width;
                        }

                        if (options.height && options.width) {
                            params.area = [options.width, options.height];
                        }

                        //iframe层
                        idx = layer.open(params);

                        if(options.handler){
                            top[getHandlerFunName(idx)] = options.handler;
                        }
                    }

                },
                /**
                 * 窗口与页面交互函数，弹出的窗口主动与父页面中的handler
                 * @param options
                 *  {id:'窗口索引ID，如果为空，则自动计算',result:'回调函数参数'}
                 */
                handler:function(options){
                    options = getExWindowOptions(options);
                    if (window != top) {
                        top.seajs.use(module.id, function (msgBox) {
                            msgBox.exWindow.handler(options);
                        });
                    } else {
                        var funName = getHandlerFunName(options.id);
                        top[funName] && top[funName](options.result);
                    }
                },
                /**
                 * 关闭指定的窗口,并触发close回调函数
                 * @param options
                 * {
                 *  id:'如果指定，则默认根据名称进行关闭窗口',
                 *  result:'回传的结果，可以自定义'
                 * }
                 */
                close:function(options) {
                    options = getExWindowOptions(options);

                    if (window != top) {
                        top.seajs.use(module.id, function (msgBox) {
                            msgBox.exWindow.close(options);
                        });
                    } else {
                        if (options.result) {
                            top[this.tempId] = options.result;
                        }
                        layer.close(options.id);
                    }
                }
            }

        };

        module.exports = msgBox;
    });