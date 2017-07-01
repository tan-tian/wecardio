/**
 * 消息提示框<br>
 * 提供以下功能：<br>
 * 1、警告提示框alert<br>
 * 2、确认提示框confirm<br>
 * 3、消息提示框tips
 */
define(
		'msgBox/1.0/msgBox',
		[ '$', 'layer.mobile/1.2/layer.m', 'msgBox/1.0/{theme}/css/msgBox.css' ],
		function(require, exports, module) {
			// 弹出层组件
			// var Layer = require('layer.mobile/1.2/layer.m');

			var titleStyle = 'margin:0px; font-weight:bold; background-color:#09C1FF; color:#fff;';
			var alertContentStyle = 'word-break:normal; word-wrap:break-word;';
			var confirmContentStyle = alertContentStyle;
			var tipsContentStyle = 'background-color:#000; filter:alpha(opacity=80);opacity:.8; color:#fff; border:none; min-width:20px; max-width:none;';

			/**
			 * 消息提示框
			 */
			var msgBox = {

				/** 按钮类型 */
				ButtonType : {
					/** 确定 */
					OK : 'ok',
					/** 取消 */
					CANCEL : 'cancel'
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
				 * 	callback : function() {
				 * 	}// 这是回调函数，可选
				 * }
				 * </pre>
				 * 
				 * 字符串类型则代表只有内容部分。
				 */
				alert : function(options) {
					var targetWindow = window;
					if (window != top) {
						targetWindow = top;
					}

					targetWindow.seajs.use('layer.mobile/1.2/layer.m',
							function(Layer) {
								var o;
								if (typeof options == 'object') {
									o = options;
								} else {
									o = {
										msg : options
									};
								}

								var param = {
									content : o.msg,
									style : alertContentStyle,
									btn : [ '确定' ]
								};

								if (o.title) {
									param.title = [ o.title, titleStyle ];
								}

								if (o.callback) {
									param.end = function() {
										o.callback();
									};
								}

								Layer.open(param);
							});
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
				confirm : function(options) {
					var targetWindow = window;
					if (window != top) {
						targetWindow = top;
					}

					targetWindow.seajs
							.use(
									'layer.mobile/1.2/layer.m',
									function(Layer) {
										var param = {
											content : options.msg,
											style : confirmContentStyle,
											btn : [ '确认', '取消' ],
											shadeClose : false
										};

										if (options.title) {
											param.title = [ options.title,
													titleStyle ];
										}

										if (options.callback) {
											param.yes = function(index) {
												Layer.close(index);
												options
														.callback(msgBox.ButtonType.OK);
											};
											param.cancel = param.no = function() {
												options
														.callback(msgBox.ButtonType.CANCEL);
											};
										}

										Layer.open(param);
									});
				},

				/**
				 * 消息提示框，会自动关闭
				 * 
				 * @param msg
				 *            提示内容
				 */
				tips : function(msg, time, callback) {
					var targetWindow = window;
					if (window != top) {
						targetWindow = top;
					}

					targetWindow.seajs.use('layer.mobile/1.2/layer.m',
							function(Layer) {
								Layer.open({
									shade : false,
									content : msg,
									style : tipsContentStyle,
									time : time || 1.5,
									end : callback
								});
							});
				}
			};

			module.exports = msgBox;
		});