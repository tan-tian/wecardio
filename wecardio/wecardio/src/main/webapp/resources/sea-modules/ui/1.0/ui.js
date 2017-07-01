/**
 * 界面相关模块
 */
define(
		'ui/1.0/ui',
		[ '$', 'loadingTip/1.0/loadingTip' ],
		function(require, exports, module) {
			// 加载提示框
			var LoadingTip = require('loadingTip/1.0/loadingTip');
			// ui模块路径
			var modulePath = seajs.resolve('ui/1.0/ui');
			// 模块父路径
			var moduleParentPath = modulePath.replace("ui.js", "");

			var UI = {
				/**
				 * 界面提示配置
				 */
				tip : {
					/** 为空时，显示的html内容 */
					emptyListHtml : ''
							+ '<div style="margin:auto;width: 100%;height: 350px;background: url(../../../Event/Images/empty.png) center no-repeat;"></div>'
				},
				
				/**
				 * 移动滚动条到容器底部事件
				 * 
				 * @param handler
				 *            事件处理器
				 * @param limit
				 *            距离底部像素值
				 * @param selector
				 *            容器选择器
				 */
				scrollToBottom : function(handler, limit, selector) {
					var onScrollToBottom = function() {
						// 当之前的加载都处理完后才触发
						if (!LoadingTip.isShow()) {
							LoadingTip.loadingTipType = "bottom";
							handler && handler();
							LoadingTip.loadingTipType = null;
						}
					};

					limit = limit == null ? 3 : limit;
					if (selector) {
						$(selector).scroll(
								function() {
									$this = $(this);

									if ($this.scrollTop() >= this.scrollHeight
											- $this.innerHeight() - limit) {
										onScrollToBottom();
									}
								});
					} else {
						$(document).scroll(
								function() {
									if ($(document).scrollTop() >= $(document)
											.height()
											- $(window).height() - limit) {
										onScrollToBottom();
									}
								});
					}
				},

				/**
				 * 是否是最后一页
				 * 
				 * @param total
				 *            总数
				 * @param pageNo
				 *            当前页码
				 * @param limit
				 *            页大小
				 * @returns {Boolean}
				 */
				isLastPage : function(total, pageNo, limit) {
					if (pageNo * limit >= total) {
						return true;
					}
					return false;
				},

				/**
				 * 向下滑动刷新事件
				 * 
				 * @param handler
				 *            刷新处理器
				 * @param selector
				 *            添加事件目标元素，默认为body，如果页面有滚动条，应选滚动条的元素
				 * @param disableSwipe
				 *            函数类型，禁用滑动刷新功能，函数中返回true则禁用功能，默认不禁用
				 */
				swipeForRefresh : function(handler, selector, disableSwipe) {
					selector = selector || "body";

					if ($(".loadingTip-top").length == 0) {
						var tipWrapHtml = '<div class="loadingTip-top"></div>';

						var $container = $("body");
						// 页面引入jquery.mobile时需要特殊处理
						var $pageContainer = $("div[data-role='page']");
						if ($pageContainer.length > 0) {
							$container = $pageContainer;
						}
						$container.prepend(tipWrapHtml);
					}

					var $loadingTip = $(".loadingTip-top");
					if ($loadingTip.attr("init") != "true") {
						$loadingTip.addClass("loadingTip");

						$loadingTip.css({
							"width" : "100%",
							"height" : "0px",
							"position" : "relative",
							"overflow" : "hidden"
						});

						var divStyle = "position:absolute;bottom:3px;left:50%;margin-left:-100px;width:200px;text-align:center;";
						var imgStyle = "vertical-align: middle;width:24px;height:24px;";
						var spanStyle = "vertical-align: middle;margin-left:12px;font-family:'微软雅黑';font-size:14px;color:#353535;";

						$loadingTip.append('<div style="' + divStyle
								+ '"><img style="' + imgStyle
								+ '" src=""/><span style="' + spanStyle
								+ '"></span></div>');

						$loadingTip.attr("init", "true");
					}

					// 执行刷新高度阀值
					var heightLimit = 80;
					// 是否达到执行刷新的条件
					var canRefresh = false;
					// 达到刷新条件时的起始位置
					var startPositionY = 0;

					// 添加元素的触摸开始事件处理
					$(selector)
							.bind(
									"touchstart mousedown",
									function(e) {
										var disable = disableSwipe
												&& disableSwipe();
										if (disable) {
											return;
										}

										// 当之前的加载都处理完后才触发
										if (!LoadingTip.isShow()) {
											// 滚动条达到顶部
											if ($(this).scrollTop() <= 0) {
												// 达到刷新条件
												canRefresh = true;
												// 记录起始位置
												startPositionY = e.screenY
														|| e.originalEvent.changedTouches[0].screenY;
											} else {
												canRefresh = false;
											}
										}
									});

					// 添加元素的滑动事件处理
					$(selector)
							.bind(
									"touchmove mousemove",
									function(e) {
										var disable = disableSwipe
												&& disableSwipe();
										if (disable) {
											return;
										}

										// 当之前的加载都处理完后才触发
										if (!LoadingTip.isShow()) {
											if (canRefresh) {
												// 滑动高度距离
												var dHeight = (e.screenY || e.originalEvent.changedTouches[0].screenY)
														- startPositionY;
												if (dHeight >= 0) {// 向下滑
													// 防止滑动事件与滚动事件冲突，阻止滚动事件
													e.preventDefault();
												} else {
													dHeight = 0;
												}

												if (dHeight >= heightLimit) {// 滑动距离达到高度阀值
													$(".loadingTip-top span")
															.html("释放刷新");
													$(".loadingTip-top img")
															.attr("src",
																	moduleParentPath + "images/icon_up.png");
												} else {// 滑动距离低于高度阀值
													$(".loadingTip-top span")
															.html("下拉刷新");
													$(".loadingTip-top img")
															.attr("src",
																	moduleParentPath + "images/icon_down.png");
												}
												$(".loadingTip-top").height(
														dHeight);
											}
										}
									});

					// 添加元素的滑动结束事件处理
					$(selector)
							.bind(
									"touchend mouseup",
									function(e) {
										// 当之前的加载都处理完后才触发
										if (!LoadingTip.isShow()) {
											if (canRefresh) {
												canRefresh = false;
												startPositionY = 0;

												if ($(".loadingTip-top")
														.height() >= heightLimit) {
													LoadingTip.loadingTipType = "top";
													// 执行刷新处理器
													handler && handler();
													LoadingTip.loadingTipType = null;
												}
												$(
														".loadingTip-top[loading!='true']")
														.height(0);
											}
										}
									});
				},

				/**
				 * 为在部分触屏设备滚动条无效添加滑动事件来模拟滚动条
				 * 
				 * @param selector
				 *            要添加滑动的元素
				 */
				noScrollOnTouchScreen : function(selector) {
					// 触摸起始坐标高度
					var ty = 0;
					// 触摸起始滚动高度
					var ts = 0;

					if ('ontouchstart' in document.documentElement) {
						// 屏蔽滚动条样式
						$(selector).css("overflow", "hidden");
						// 添加元素的触摸开始事件处理
						$(selector).bind("touchstart", function(e) {
							if (e.originalEvent.touches.length == 1) {
								e.stopPropagation();
								ty = e.originalEvent.changedTouches[0].screenY;
								ts = $(this).scrollTop();
							}
						});

						// 添加元素的滑动事件处理
						$(selector)
								.bind(
										"touchmove",
										function(e) {
											if (e.originalEvent.touches.length == 1) {
												e.preventDefault();
												e.stopPropagation();

												var dy = e.originalEvent.changedTouches[0].screenY
														- ty;

												$(this).scrollTop(ts - dy);
											}
										});
					}
				}

			};

			module.exports = UI;
		});