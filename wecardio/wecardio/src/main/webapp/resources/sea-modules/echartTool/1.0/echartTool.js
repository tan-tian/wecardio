/**
 * echart工具类模块
 */
define(
		'echartTool/1.0/echartTool',
		[ '$', 'echartTool/1.0/default/css/echartTool.css' ],
		function(require, exports, module) {
			var EchartTool = {
				/**
				 * 使echart地图能在触屏上移动
				 * 
				 * @param echart
				 *            地图对象
				 * @param mapType
				 *            地图类型
				 */
				echartMapEnableMove : function(echart, mapType) {
					var map = echart.chart.map;
					map._needRoam = true;
					map._roamMap[mapType] = true;
					// 去除默认的移动事件处理
					map.zr.un('mousedown', map._onmousedown);

					var $echart = $(echart.dom);
					// 是否支持触摸事件
					var isTouch = false;

					// 获取鼠标（手指）在地图的相对位置
					var getOffset = function(e) {
						var event = e.originalEvent;
						// 获取地图容器在页面的相对位置
						var echartOffset = $echart.offset();

						var offset = {};
						if (!isTouch) {// 鼠标事件
							offset.left = event.pageX - echartOffset.left;
							offset.top = event.pageY - echartOffset.top;
						} else {// 触摸事件
							var touch = event.changedTouches[0];
							offset.left = touch.pageX - echartOffset.left;
							offset.top = touch.pageY - echartOffset.top;
						}

						return offset;
					};

					var _onTouchStartOrMouseDown = function(e) {
						e.preventDefault();

						if (!e.originalEvent.targetTouches
								|| (e.originalEvent.targetTouches.length == 1 && e.originalEvent.changedTouches.length == 1)) {
							var offset = getOffset(e);
							var mx = offset.left;
							var my = offset.top;
							var posMapType = map._findMapTypeByPos(mx, my);
							if (posMapType == mapType) {
								map._mousedown = true;
								map._mx = mx;
								map._my = my;
								map._curMapType = mapType;

								if (isTouch) {
									// 记录下touch的id
									map._touchIdentifier = e.originalEvent.targetTouches[0].identifier;
									// 添加元素的滑动事件处理
									$echart.bind("touchmove",
											_onTouchMoveOrMouseMove);
									// 添加元素的滑动结束事件处理
									$echart.bind("touchend",
											_onTouchEndOrMouseUp);
								} else {
									$echart.bind("mousemove",
											_onTouchMoveOrMouseMove);
									$echart.bind("mouseup",
											_onTouchEndOrMouseUp);
								}
							}
						} else {
							map._mousedown = false;
						}
					};

					var _onTouchMoveOrMouseMove = function(e) {
						e.preventDefault();

						if (!map._mousedown || !map._isAlive
								|| map.zrenderRefreshing) {
							return;
						}

						if (!e.originalEvent.targetTouches
								|| (e.originalEvent.targetTouches.length == 1
										&& e.originalEvent.changedTouches.length == 1 && map._touchIdentifier == e.originalEvent.targetTouches[0].identifier)) {
							map.zrenderRefreshing = true;

							var offset = getOffset(e);
							var mx = offset.left;
							var my = offset.top;

							if (map.htmlElements) {
								for ( var i = 0; i < map.htmlElements.length; i++) {
									var eleData = map.htmlElements[i];
									if (eleData.mapType == map._curMapType) {
										var element = $('#' + eleData.id);
										element.css({
											left : (parseInt(element
													.css('left'))
													+ mx - map._mx)
													+ "px",
											top : (parseInt(element.css('top'))
													+ my - map._my)
													+ "px"
										});
									}
								}
							}

							var transform = map._mapDataMap[map._curMapType].transform;
							transform.hasRoam = true;
							transform.left -= map._mx - mx;
							transform.top -= map._my - my;
							map._mx = mx;
							map._my = my;
							map._mapDataMap[map._curMapType].transform = transform;

							for ( var i = 0, l = map.shapeList.length; i < l; i++) {
								if (map.shapeList[i]._mapType == map._curMapType) {
									map.shapeList[i].position[0] = transform.left;
									map.shapeList[i].position[1] = transform.top;
									map.zr.modShape(map.shapeList[i].id);
								}
							}

							var ecConfig = echarts.config;
							map.messageCenter.dispatch(ecConfig.EVENT.MAP_ROAM,
									e.originalEvent, {
										type : 'move'
									}, map.myChart);

							map.clearEffectShape(true);
							map.zr.refresh(function() {
								map.zrenderRefreshing = false;
							});
							map._justMove = true;
						} else {
							map._mousedown = false;
						}
					};

					var _onTouchEndOrMouseUp = function(e) {
						e.preventDefault();
						var offset = getOffset(e);
						map._mx = offset.left;
						map._my = offset.top;
						map._mousedown = false;
						setTimeout(
								function() {
									map._justMove && map.animationEffect();
									map._justMove = false;

									if (isTouch) {
										$echart.unbind("touchmove",
												_onTouchMoveOrMouseMove);
										$echart.unbind("touchend",
												_onTouchEndOrMouseUp);
									} else {
										$echart.unbind("mousemove",
												_onTouchMoveOrMouseMove);
										$echart.unbind("mouseup",
												_onTouchEndOrMouseUp);
									}
								}, 120);
					};

					// 添加元素的触摸开始事件处理
					$echart.bind("touchstart", function(e) {
						isTouch = true;
						_onTouchStartOrMouseDown(e);
					});
					$echart.bind("mousedown", function(e) {
						if (!isTouch) {
							_onTouchStartOrMouseDown(e);
						}
					});
				},

				/**
				 * 缩放echart地图
				 * 
				 * @param echart
				 *            地图对象
				 * @param mapType
				 *            地图类型，可选，默认为全部类型
				 * @param delta
				 *            缩放倍数
				 * @param mx
				 *            缩放中心x坐标
				 * @param my
				 *            缩放中心y坐标
				 * @param event
				 *            原始事件对象，可选，默认为空
				 * @param isLoose
				 *            中心坐标是否不需要在该地图类型显示区域内，可选，默认为需要
				 */
				echartMapScale : function(echart, mapType, delta, mx, my,
						event, isLoose) {
					var ecConfig = echarts.config;
					var map = echart.chart.map;
					var EchartTool = this;

					var _scaleType = function(mapType) {
						if (!map._roamMap[mapType]) {
							return;
						}

						if (isLoose || map._findMapTypeByPos(mx, my) == mapType) {
							var transform = map._mapDataMap[mapType].transform;
							var left = transform.left;
							var top = transform.top;
							var width = transform.width;
							var height = transform.height;
							// 位置转经纬度
							var geoAndPos = map.pos2geo(mapType, [ mx - left,
									my - top ]);

							if (delta > 0) {
								if (typeof map._scaleLimitMap[mapType].max != 'undeined'
										&& transform.baseScale >= map._scaleLimitMap[mapType].max) {
									return; // 缩放限制
								}
							} else {
								if (typeof map._scaleLimitMap[mapType].min != 'undeined'
										&& transform.baseScale <= map._scaleLimitMap[mapType].min) {
									return; // 缩放限制
								}
							}
							transform.baseScale *= delta;
							transform.scale.x *= delta;
							transform.scale.y *= delta;
							transform.width = width * delta;
							transform.height = height * delta;

							map._mapDataMap[mapType].hasRoam = true;
							map._mapDataMap[mapType].transform = transform;
							// 经纬度转位置
							geoAndPos = map.geo2pos(mapType, geoAndPos);
							// 保持视觉中心
							transform.left -= geoAndPos[0] - (mx - left);
							transform.top -= geoAndPos[1] - (my - top);
							map._mapDataMap[mapType].transform = transform;

							EchartTool.echartMapRefreshHtmlElement(echart,
									mapType);

							map.clearEffectShape(true);
							for ( var i = 0, l = map.shapeList.length; i < l; i++) {
								if (map.shapeList[i]._mapType == mapType) {
									map.shapeList[i].position[0] = transform.left;
									map.shapeList[i].position[1] = transform.top;
									if (map.shapeList[i].type == 'path'
											|| map.shapeList[i].type == 'symbol'
											|| map.shapeList[i].type == 'circle'
											|| map.shapeList[i].type == 'rectangle'
											|| map.shapeList[i].type == 'polygon'
											|| map.shapeList[i].type == 'line'
											|| map.shapeList[i].type == 'ellipse') {
										map.shapeList[i].scale[0] *= delta;
										map.shapeList[i].scale[1] *= delta;
									} else if (map.shapeList[i].type == 'mark-line') {
										map.shapeList[i].style.pointListLength = undefined;
										map.shapeList[i].style.pointList = false;
										geoAndPos = map.geo2pos(mapType,
												map.shapeList[i]._geo[0]);
										map.shapeList[i].style.xStart = geoAndPos[0];
										map.shapeList[i].style.yStart = geoAndPos[1];
										geoAndPos = map.geo2pos(mapType,
												map.shapeList[i]._geo[1]);
										map.shapeList[i].style.xEnd = geoAndPos[0];
										map.shapeList[i].style.yEnd = geoAndPos[1];
									} else if (map.shapeList[i].type == 'icon') {
										geoAndPos = map.geo2pos(mapType,
												map.shapeList[i]._geo);
										map.shapeList[i].style.x = map.shapeList[i].style._x = geoAndPos[0]
												- map.shapeList[i].style.width
												/ 2;
										map.shapeList[i].style.y = map.shapeList[i].style._y = geoAndPos[1]
												- map.shapeList[i].style.height
												/ 2;
									} else {
										geoAndPos = map.geo2pos(mapType,
												map.shapeList[i]._geo);
										map.shapeList[i].style.x = geoAndPos[0];
										map.shapeList[i].style.y = geoAndPos[1];
										if (map.shapeList[i].type == 'text') {
											map.shapeList[i]._style.x = map.shapeList[i].highlightStyle.x = geoAndPos[0];
											map.shapeList[i]._style.y = map.shapeList[i].highlightStyle.y = geoAndPos[1];
										}
									}

									map.zr.modShape(map.shapeList[i].id);
								}
							}

							map.zr.refresh();

							clearTimeout(map._refreshDelayTicket);
							map._refreshDelayTicket = setTimeout(function() {
								map && map.shapeList && map.animationEffect();
							}, 100);

							map.messageCenter.dispatch(ecConfig.EVENT.MAP_ROAM,
									event, {
										type : 'scale'
									}, map.myChart);
						}
					};

					if (mapType) {
						_scaleType(mapType);
					} else {
						for ( var type in map._roamMap) {
							_scaleType(type);
						}
					}
				},

				/**
				 * 使echart地图能在触屏上缩放
				 * 
				 * @param echart
				 *            地图对象
				 * @param mapType
				 *            地图类型
				 */
				echartMapEnableZoom : function(echart, mapType) {
					var map = echart.chart.map;
					map._needRoam = true;
					map._roamMap[mapType] = true;
					// 去除默认的缩放事件处理
					map.zr.un('mousewheel', map._onmousewheel);

					// 缩放系数
					var zoomCoefficient = 1.5;
					var EchartTool = this;

					$$(echart.dom)
							.pinchIn(
									function(event) {
										var delta = 1 / zoomCoefficient;
										var x = (event.currentTouch[0].x + event.currentTouch[1].x) / 2;
										var y = (event.currentTouch[0].y + event.currentTouch[1].y) / 2;
										EchartTool.echartMapScale(echart,
												mapType, delta, x, y,
												event.originalEvent);
									});

					$$(echart.dom)
							.pinchOut(
									function(event) {
										var delta = zoomCoefficient;
										var x = (event.currentTouch[0].x + event.currentTouch[1].x) / 2;
										var y = (event.currentTouch[0].y + event.currentTouch[1].y) / 2;
										EchartTool.echartMapScale(echart,
												mapType, delta, x, y,
												event.originalEvent);
									});

					map.zr.on('mousewheel', function(param) {
						var event = param.event;
						var zrEvent = zrender.tool.event;
						var x = zrEvent.getX(event);
						var y = zrEvent.getY(event);
						var delta = zrEvent.getDelta(event);
						delta = delta > 0 ? (1.2) : 1 / 1.2;

						EchartTool.echartMapScale(echart, mapType, delta, x, y,
								event);
					});
				},

				/**
				 * 在echart地图里面显示缩放按钮
				 * 
				 * @param echart
				 *            地图对象
				 */
				echartMapShowZoomButton : function(echart) {
					// 缩放系数
					var zoomCoefficient = 1.5;
					var EchartTool = this;

					var $echart = $(echart.dom);
					$echart.append('<div class="echartMap-zoomIn"></div>');
					$echart.append('<div class="echartMap-zoomOut"></div>');

					$$(echart.dom).find('.echartMap-zoomIn').tap(
							function(event) {
								var delta = 1 / zoomCoefficient;
								var x = $echart.width() / 2;
								var y = $echart.height() / 2;

								EchartTool.echartMapScale(echart, null, delta,
										x, y, event.originalEvent, true);
							});
					$$(echart.dom).find('.echartMap-zoomOut').tap(
							function(event) {
								var delta = zoomCoefficient;
								var x = $echart.width() / 2;
								var y = $echart.height() / 2;

								EchartTool.echartMapScale(echart, null, delta,
										x, y, event.originalEvent, true);
							});
				},

				/**
				 * echart地图新增或更新标记点
				 * 
				 * @param echart
				 *            地图对象
				 * @param seriesIdx
				 *            地图系列的索引号
				 * @param markData
				 *            标记点数据
				 */
				echartMapAddOrUpdateMarkPoint : function(echart, seriesIdx,
						markData) {
					// 清空该系列上已经存在的点
					var pointDatas = markData.data;
					for ( var i = 0; i < pointDatas.length; i++) {
						echart.delMarkPoint(seriesIdx, pointDatas[i].name);
					}

					// 添加新点
					echart.addMarkPoint(seriesIdx, markData);

					// 纠正新点的位置，echart框架本身的bug
					var map = echart.chart.map;
					var desMapType = map.series[seriesIdx].mapType;
					var transform = map._mapDataMap[desMapType].transform;

					for ( var i = 0, l = map.shapeList.length; i < l; i++) {
						if (map.shapeList[i]._mapType == desMapType
								&& map.shapeList[i].type == 'icon') {
							map.shapeList[i].position[0] = transform.left;
							map.shapeList[i].position[1] = transform.top;
							map.zr.modShape(map.shapeList[i].id);
						}
					}

					map.zr.refresh();
				},

				/**
				 * 向echart地图添加html元素
				 * 
				 * @param echart
				 *            地图对象
				 * @param mapType
				 *            地图类型
				 * @param x
				 *            html元素的经度
				 * @param y
				 *            html元素的纬度
				 * @param elementId
				 *            元素标识
				 * @param eX
				 *            元素锚点x坐标
				 * @param eY
				 *            元素锚点y坐标
				 */
				echartMapAddHtmlElement : function(echart, mapType, x, y,
						elementId, eX, eY) {
					var map = echart.chart.map;
					if (!map.htmlElements) {
						map.htmlElements = [];
					}

					var element = $("#" + elementId);
					if (element.length > 0) {
						var elementData = {
							id : elementId,
							mapType : mapType,
							x : x,
							y : y,
							eX : (eX || 0),
							eY : (eY || 0)
						};
						map.htmlElements.push(elementData);

						var transform = map._mapDataMap[mapType].transform;
						var left = transform.left;
						var top = transform.top;

						var pos = map.geo2pos(mapType, [ x, y ]);
						element.appendTo(echart.dom);

						element.css({
							"position" : "absolute",
							left : (pos[0] + left - eX) + "px",
							top : (pos[1] + top - eY) + "px"
						});
					}
				},

				/**
				 * 刷新echart地图中html元素的坐标
				 * 
				 * @param echart
				 *            地图对象
				 * @param mapType
				 *            地图类型
				 */
				echartMapRefreshHtmlElement : function(echart, mapType) {
					var map = echart.chart.map;
					var transform = map._mapDataMap[mapType].transform;
					if (map.htmlElements) {
						for ( var i = 0; i < map.htmlElements.length; i++) {
							var eleData = map.htmlElements[i];
							if (eleData.mapType == mapType) {
								var element = $('#' + eleData.id);
								var pos = map.geo2pos(mapType, [ eleData.x,
										eleData.y ]);
								element
										.css({
											left : (pos[0] + transform.left - eleData.eX)
													+ "px",
											top : (pos[1] + transform.top - eleData.eY)
													+ "px"
										});
							}
						}
					}
				},

				/**
				 * echart地图类型扩展
				 * 
				 * @param mapTypeConfig
				 *            地图类型配置，为<mapType, geoJson文件路径>的键值对，例如：
				 * 
				 * <pre>
				 * {
				 * 	'gz' : 'guangzhou.json',//广州
				 * 	'sz' : 'shenzhen.json'//深圳
				 * }
				 * </pre>
				 * 
				 * @param loadMapTypeCallback
				 *            加载地图类型文件后的回调<br>
				 *            形参为<mapType, geoJson数据对象>的键值对
				 */
				echartMapExtendMapType : function(mapTypeConfig,
						loadMapTypeCallback) {
					var mapData = {};
					var count = 0;

					for ( var mapType in mapTypeConfig) {
						count++;
						(function(mapType) {
							$.getJSON(mapTypeConfig[mapType], function(res) {
								mapData[mapType] = res;
								count--;

								if (!count) {
									loadMapTypeCallback
											&& loadMapTypeCallback(mapData);
								}
							});
						})(mapType);

						echarts.util.mapData.params.params[mapType] = function(
								mapType) {
							return {
								getGeoJson : function(callback) {
									callback(mapData[mapType]);
								}
							};
						}(mapType);
					}

					if (!count) {
						loadMapTypeCallback && loadMapTypeCallback({});
					}
				}
			};

			module.exports = EchartTool;
		});