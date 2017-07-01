/**
 * 加载提示框模块
 */
define(
		'loadingTip/1.0/loadingTip',
		[ '$' ],
		function(require, exports, module) {
			// 模块路径
			var modulePath = seajs.resolve('loadingTip/1.0/loadingTip');
			// 模块父路径
			var moduleParentPath = modulePath.replace("loadingTip.js", "");
			var msg = (typeof (messDataLoading) == 'undefined' ?  '加载中……' : messDataLoading);
			// 显示计数
			var showCount = 0;

			var LoadingTip = {
				/**
				 * 加载提示框类型：top - 顶部，middle - 中间，bottom - 底部
				 */
				/* private */loadingTipType : null,
				/**
				 * 默认加载提示框类型
				 */
				defLoadingTipType : "middle",

				/**
				 * 显示加载提示框
				 */
				show : function() {
					var LoadingTip = this;

					if (!LoadingTip.isShow()) {
						var type = LoadingTip.loadingTipType
								|| LoadingTip.defLoadingTipType;
						if ($(".loadingTip-" + type).length == 0) {
							var tipWrapHtml = '<div class="loadingTip-' + type
									+ '"></div>';
							if (type == "top") {
								$("body").prepend(tipWrapHtml);
							} else if (type == "middle") {
								$("body").append(tipWrapHtml);
							} else if (type == "bottom") {
								$("body").append(tipWrapHtml);
							}
						}

						var $loadingTip = $(".loadingTip-" + type);
						if ($loadingTip.attr("init") != "true") {
							$loadingTip.addClass("loadingTip");

							var divStyle;
							var imgStyle;
							var spanStyle;
							if (type == "middle") {
								$loadingTip.css({
									"display" : "none",
									"background-color" : "#000",
									"opacity" : "0.8",
									"width" : "150px",
									"height" : "100px",
									"border-radius" : "10px",
									"position" : "fixed",
									"top" : "50%",
									"margin-top" : "-50px",
									"left" : "50%",
									"margin-left" : "-75px",
									"z-index" : "99999"
								});

								divStyle = "position:absolute;bottom:35px;left:50%;margin-left:-100px;width:200px;text-align:center;";
								imgStyle = "vertical-align: middle;width:30px;height:30px;";
								spanStyle = "vertical-align: middle;margin-left:12px;font-family:'微软雅黑';font-size:14px;color:#FFF;";
							} else {
								$loadingTip.css({
									"display" : "none",
									"width" : "100%",
									"height" : "30px",
									"position" : "relative",
									"overflow" : "hidden"
								});

								divStyle = "position:absolute;bottom:3px;left:50%;margin-left:-100px;width:200px;text-align:center;";
								imgStyle = "vertical-align: middle;width:24px;height:24px;";
								spanStyle = "vertical-align: middle;margin-left:12px;font-family:'微软雅黑';font-size:14px;color:#353535;";
							}

							$loadingTip
									.append('<div style="'
											+ divStyle
											+ '"><img style="'
											+ imgStyle
											+ '" src="' + moduleParentPath + 'images/loading.gif"/><span style="'
											+ spanStyle
											+ '">'+msg+'</span></div>');

							$loadingTip.attr("init", "true");
						}

						if (type == "top") {
							$loadingTip.attr("loading", "true");
							$loadingTip.animate({
								height : "30px"
							}, 500);
							$loadingTip.find("img").attr("src",
									moduleParentPath + "images/loading.gif");
							$loadingTip.find("span").html(msg);
						} else {
							$loadingTip.show();
						}
					}

					showCount++;1
					//console.log('show:' + showCount);
				},

				/**
				 * 隐藏加载提示框
				 */
				hide : function() {
					showCount--;
					showCount = showCount < 0 ? 0 : showCount;

					if (showCount == 0) {
						$(".loadingTip").fadeOut("slow", function() {
							var $top = $(".loadingTip-top");
							$top.attr("loading", "false");
							$top.height(0);
							$top.show();
							//console.log('hide2:' + showCount);
						});
					}
					//console.log('hide1:' + showCount);
				},

				/**
				 * 是否显示状态
				 */
				isShow : function() {
					return showCount > 0;
				}
			};

			module.exports = LoadingTip;
		});