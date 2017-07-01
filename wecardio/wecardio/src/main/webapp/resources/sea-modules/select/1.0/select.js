/**
 * 下拉控件<br>
 *     <div class="searchFormChose">
 *       <dl>
 *           <dt>test：</dt>
 *           <input id="cc" value="1">
 *       </dl>
 *   </div>
 */
define('select/1.0/select', ['$', 'select/1.0/{theme}/css/select.css'],
    function (require, exports, module) {
        var SELECT_SERNO = 0;

        function getRowIndex(target, value) {
            var state = $.data(target, 'select');
            var opts = state.options;
            var data = state.data;
            for (var i = 0, l = data.length; i < l; i++) {
                if (data[i][opts.valueField] == value) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 滚动到已选
         */
        function scrollTo(target, value) {
            var opts = $.data(target, 'select').options;
            var panel = getPanel(target);
            var p;
            if (opts.size != null && opts.size == 'big') {
                p = panel.children('div.draDownToChoseMain');
            } else if (opts.size != null && opts.size == 'small') {
                p = panel.children('div.choseDivSeconListDiv');
            } else {
                p = panel.children('div.jieguo');
            }
            var item = opts.finder.getEl(target, value);
            if (item.length) {
                if (item.position().top <= 0) {
                    var h = p.scrollTop() + item.position().top;
                    p.scrollTop(h);
                } else if (item.position().top + item.outerHeight() > p.height()) {
                    var h = p.scrollTop() + item.position().top + item.outerHeight() - p.height();
                    p.scrollTop(h);
                }
            }
        }

        function nav(target, dir) {
            var opts = $.data(target, 'select').options;
            var panel = getPanel(target);
            var ul = panel.find('ul');
            var item = ul.children('li.hover');
            if (!item.length) {
                item = ul.children('li.selected');
            }
            item.removeClass('hover');
            var firstSelector = 'li:visible:not(.disabled):first';
            var lastSelector = 'li:visible:not(.disabled):last';
            if (!item.length) {
                item = ul.children(dir == 'next' ? firstSelector : lastSelector);
            } else {
                if (dir == 'next') {
                    item = item.nextAll(firstSelector);
                    if (!item.length) {
                        item = ul.children(firstSelector);
                    }
                } else {
                    item = item.prevAll(firstSelector);
                    if (!item.length) {
                        item = ul.children(lastSelector);
                    }
                }
            }
            if (item.length) {
                item.addClass('hover');
                var row = opts.finder.getRow(target, item);
                if (row) {
                    scrollTo(target, row[opts.valueField]);
                    if (opts.selectOnNavigation) {
                        select(target, row[opts.valueField]);
                    }
                }
            }
        }

        function doEnter(target) {
            var t = $(target);
            var opts = t.select('options');
            var panel = getPanel(target);
            var item = panel.find('li.hover');
            if (item.length) {
                var row = opts.finder.getRow(target, item);
                var value = row[opts.valueField];
                if (opts.multiple) {
                    if (item.hasClass('selected')) {
                        t.select('unselect', value);
                    } else {
                        t.select('select', value);
                    }
                } else {
                    t.select('select', value);
                }
            }
            var vv = [];
            $.map(t.select('getValues'), function (v) {
                if (getRowIndex(target, v) >= 0) {
                    vv.push(v);
                }
            });
            setValues(target, vv);
            if (!opts.multiple) {
                hidePanel(target);
            }
        }

        /**
         * 创建select下拉框
         */
        function createSelect(target) {
            var state = $.data(target, 'select');
            var opts = state.options;
            $(target).hide();
            var widthStyle = opts.width ? 'width:'+opts.width : '';
            var id = 'hiteam_select_' + $(target).attr('id');
            var combo, input, panel;
            if (opts.size != null && opts.size == 'big') {
                var styles = '';
                if (opts.zIndex != null && opts.zIndex != '') {
                    styles += ' z-index:' + opts.zIndex;
                }

                if(widthStyle){
                    styles += ';'+widthStyle;
                }

                combo = $('<div class="circlemohusearch xw_circlemohusearch" style="' + styles + '"></div>').insertAfter(target);
                input = $('<input id="' + id + '" name="' + id + '" type="text" class="searchSimpleInputb xw_searchInput" readonly="' + (opts.editable ? "readonly" : "false") + '"/>').appendTo(combo);
                // 下拉箭头
                $('<a class="searchSubb"></a>').appendTo(combo);
                panel = $('<div class="draDownToChose xw_draDownToChose">'
                    + '<div class="draDownToChoseMain">'
                    + '<ul class="draDownToChoselist"></ul>'
                    + '</div>'
                    + '</div>').appendTo("body");

                if (opts.width) {
                    input.width(opts.width - 25);
                    panel.width(opts.width);
                    panel.find('div.draDownToChoseMain').width(opts.width);
                }
                // 是否显示查询框
                if (opts.isShowQuery) {
                    var id = 'hiteam_select_qry_' + new Date().getTime();
                    var queryDiv = $('<div class="draDownToChoseSearch" style="clear:both; float:left; position:relative;margin-right:3px; margin-top:3px;">'
                        + '<input type="text" id="' + id + '" class="searchSimpleInput" style="width: 200px;" />'
                        + '<a class="searchSub"></a>'
                        + '</div>').insertBefore((panel.find('div.draDownToChoseMain')));
                    if (opts.width) {
                        $('#' + id).width(opts.width - 35);
                    }
                    $(queryDiv).find('a.searchSub').click(function() {
                        var param = {};
                        param[opts.queryParamName] = $(this).siblings('input.searchSimpleInput').val() || '';

                        request(target, null, param);
                    });
                }
            } else if (opts.size != null && opts.size == 'small') {
                var styles = '';
                if (opts.zIndex != null && opts.zIndex != '') {
                    styles += ' z-index:' + opts.zIndex;
                }

                combo = $('<div class="choseDiv xw_choseDiv" style="' + styles + '"></div>').insertAfter(target);
                input = $('<input id="' + id + '" name="' + id + '" type="text" class="choseDivInput xw_choseDivInput" readonly="' + (opts.editable ? "readonly" : "false") + '"/>').appendTo(combo);
                // 下拉箭头
                $('<a class="choseDivSubA"></a>').appendTo(combo);
                panel = $('<div class="choseDivRefault xw_choseDivRefault">'
                    + '<div class="choseDivSeconListDiv">'
                    + '<ul class="choseDivSeconListUl"></ul>'
                    + '</div>'
                    + '</div>').appendTo("body");

                if (opts.width) {
                    input.width(opts.width - 25);
                    panel.width(opts.width);
                    panel.find('div.choseDivSeconListDiv').width(opts.width);
                }
                // 是否显示查询框
                if (opts.isShowQuery) {
                    var id = 'hiteam_select_qry_' + new Date().getTime();
                    var queryDiv = $('<div class="choseDivSecondSearch" style="clear:both; float:left; position:relative;margin-right:3px; margin-top:3px;">'
                        + '<input type="text" id="' + id + '" class="choseDivSecondInput" />'
                        + '<a class="choseDivSeconSubA"></a>'
                        + '</div>').insertBefore(panel.find('div.choseDivSeconListDiv'));
                    if (opts.width) {
                        $('#' + id).width(opts.width - 35);
                    }
                    $(queryDiv).find('a.choseDivSeconSubA').click(function() {
                        var param = {};
                        param[opts.queryParamName] = $(this).siblings('input.choseDivSecondInput').val() || '';

                        request(target, null, param);
                    });
                }
            } else {
                combo = $('<div class="mohusearch" style="float: left; margin-left: 0px; margin-top: 3px;"></div>').insertAfter(target);
                input = $('<input id="' + id + '" style="'+widthStyle+'" name="' + id + '" type="text" class="searchSimpleInputb xw_searchInput" readonly="' + (opts.editable ? "readonly" : "false") + '"/>').appendTo(combo);
                // 下拉箭头
                $('<a class="searchSubb"></a>').appendTo(combo);
                panel = $('<div class="searchDoult xw_searchDoult">'
                    + '<div class="jieguo">'
                    + '<ul class="jieguolist"></ul>'
                    + '</div>'
                    + '</div>').appendTo("body");

                if (opts.width) {
                    input.width(opts.width - 25);
                    panel.width(opts.width);
                    panel.find('div.jieguo').width(opts.width);
                }
                // 是否显示查询框
                if (opts.isShowQuery) {
                    var id = 'hiteam_select_qry_' + new Date().getTime();
                    var queryDiv = $('<div class="searchSimple" style="margin-right: 3px; margin-top: 3px;">'
                        + '<input type="text" id="' + id + '" class="searchSimpleInput" style="width: 180px;" />'
                        + '<a class="searchSub"></a>'
                        + '</div>').insertBefore(panel.find('div.jieguo'));
                    if (opts.width) {
                        $('#' + id).width(opts.width - 35);
                    }
                    $(queryDiv).find('a.searchSub').click(function() {
                        var param = {};
                        param[opts.queryParamName] = $(this).siblings('input.searchSimpleInput').val() || '';

                        request(target, null, param);
                    });
                }
            }
            return {
                combo : combo,
                panel : panel
            };
        }

        function getPanel(target) {
            return $.data(target, "select").panel;
        }

        function hidePanel(target) {
            return $.data(target, "select").panel.slideUp('fast');
        }

        function bindEvents(target) {
            var state = $.data(target, "select");
            var opts = state.options;
            var combo = $.data(target, "select").combo;
            var panel = $.data(target, "select").panel;
            var input = combo.find("input");

            // 委托mousedown事件到document上,只要不是点击panel区域,就关闭所有下拉面板
            $(document).off(".sel").on("mousedown.sel", function(e) {
                var panels, p;
                if (opts.size != null && opts.size == 'big') {
                    panels = $("body>div.draDownToChose");
                    p = $(e.target).closest("div.draDownToChose", panels);
                } else if (opts.size != null && opts.size == 'small') {
                    panels = $("body>div.choseDivRefault");
                    p = $(e.target).closest("div.choseDivRefault", panels);
                } else {
                    panels = $("body>div.searchDoult");
                    p = $(e.target).closest("div.searchDoult", panels);
                }
                if (p.length) {
                    // 如果mousedown事件发生在下拉面板内,则不做任何操作
                    return;
                }
                // 关闭面板
                panels.slideUp('fast');
            });
            combo.unbind(".sel");
            panel.unbind(".sel");
            input.unbind(".sel");
            if (!opts.disabled && !opts.readonly) {
                input.siblings('a').on("mousedown.sel", function(e) {
                    // 阻止事件冒泡
                    showPanel(target);
                    scrollTo(target, $(target).val());
                    e.stopPropagation();
                });
                input.on("mousedown.sel", function(e) {
                    // 阻止事件冒泡
                    showPanel(target);
                    scrollTo(target, $(target).val());
                    e.stopPropagation();
                }).on("keydown", function (e) {
                    switch (e.keyCode) {
                        case 38:
                            opts.keyHandler.up.call(target, e);
                            break;
                        case 40:
                            opts.keyHandler.down.call(target, e);
                            break;
                        case 37:
                            opts.keyHandler.left.call(target, e);
                            break;
                        case 39:
                            opts.keyHandler.right.call(target, e);
                            break;
                        case 13:
                            e.preventDefault();
                            opts.keyHandler.enter.call(target, e);
                            return false;
                        case 9:
                        case 27:
                            hidePanel(target);
                            break;
                        default:
                            if (opts.editable) {
                                if (state.timer) {
                                    clearTimeout(state.timer);
                                }
                                state.timer = setTimeout(function () {
                                    var q = input.val();
                                    if (state.previousValue != q) {
                                        state.previousValue = q;
                                        panel.slideDown('fast');
                                        opts.keyHandler.query.call(target, input.val(), e);
                                    }
                                }, opts.delay);
                            }
                    }
                });
            }
        }

        function showPanel(target) {
            var opts = $.data(target, "select").options;
            var combo = $.data(target, "select").combo;
            var panel = $.data(target, "select").panel;
            panel.css({
                left : combo.offset().left,
                top : fixedTop()
            });
            panel.slideDown('fast');
            /**
             * 纠正下拉面板left
             */
            function fixedLeft() {
                var left = combo.offset().left;
                if (left + panel._outerWidth() > $(window)._outerWidth()
                    + $(document).scrollLeft()) {
                    left = $(window)._outerWidth() + $(document).scrollLeft()
                        - panel._outerWidth();
                }
                if (left < 0) {
                    left = 0;
                }
                return left;
            }
            /**
             * 纠正下拉面板top
             */
            function fixedTop() {
                var top = combo.offset().top + combo.outerHeight();
                if (top + panel.outerHeight() > $(window).outerHeight()
                    + $(document).scrollTop()) {
                    top = combo.offset().top - panel.outerHeight();
                }
                if (top < $(document).scrollTop()) {
                    top = combo.offset().top + combo.outerHeight();
                }
                return top;
            }
        }

        /**
         * select the specified value
         */
        function select(target, value) {
            var opts = $.data(target, 'select').options;
            var values = $(target).select('getValues');
            if ($.inArray(value + '', values) == -1) {
                if (opts.multiple) {
                    values.push(value);
                } else {
                    values = [ value ];
                }
                setValues(target, values);
                opts.onSelect.call(target, findRowBy(target, value));
            }
        }

        /**
         * unselect the specified value
         */
        function unselect(target, value) {
            var opts = $.data(target, 'select').options;
            var values = $(target).select('getValues');
            var index = $.inArray(value + '', values);
            if (index >= 0) {
                values.splice(index, 1);
                setValues(target, values);
                opts.onUnselect.call(target, findRowBy(target, value));
            }
        }

        /**
         * set values
         */
        function setValues(target, values, remainText) {
            var opts = $.data(target, 'select').options;
            var combo = $.data(target, "select").combo;
            var panel = getPanel(target);

            if (!$.isArray(values)) {
                values = values.split(opts.separator)
            }
            panel.find('li').removeClass('hover');
            panel.find('li').removeClass('selected');
            combo.find('input').val('');

            var vv = [], ss = [];
            for ( var i = 0; i < values.length; i++) {
                var v = values[i];
//			var s = v;
                var s = null;
                opts.finder.getEl(target, v).addClass('selected');
                var row = opts.finder.getRow(target, v);
                if (row) {
                    s = row[opts.textField];
                }
                vv.push(v);
                ss.push(s);
            }

            $(target).val(vv.join(opts.separator));

            if (!remainText && ss.length > 0) {
                combo.find('input').val(ss.join(opts.separator));
            }
        }

        function getValues(target) {
//		var vv = [];
//		$(target).siblings("div.mohusearch").find('ul.jieguolist li').each(function() {
//			if ($(this).hasClass('selected')) {
//				vv.push($(this).attr('value') + '');
//			}
//		});
//		return vv;

            var opts = $.data(target, 'select').options;
            var val = $(target).val();
            return ((val != '' && val) ? val.split(opts.separator) : []);
        }

        function getRemainText(target) {
            var opts = $.data(target, 'select').options;
            var combo = $.data(target, "select").combo;
            return combo.find('input').val();
        }

        function findRowBy(target, value, param, isGroup) {
            var state = $.data(target, 'select');
            var opts = state.options;
            if (isGroup) {
                return _findRow(state.groups, param, value);
            } else {
                return _findRow(state.data, (param ? param : opts.valueField), value);
            }

            function _findRow(data, key, value) {
                for ( var i = 0; i < data.length; i++) {
                    var row = data[i];
                    if (row[key] == value) {
                        return row;
                    }
                }
                return null;
            }
        }

        /**
         * request remote data if the url property is setted.
         */
        function request(target, url, param, remainText) {
            var opts = $.data(target, 'select').options;
            if (url) {
                opts.url = url;
            }
            // if (!opts.url) return;
            param = param || {};

            if (opts.onBeforeLoad.call(target, param) == false) {
                return;
            }

            opts.loader.call(target, param, function(data) {
                loadData(target, data, remainText);
            }, function() {
                opts.onLoadError.apply(this, arguments);
            });
        }

        /**
         * load data, the old list items will be removed.
         */
        var itemIndex = 1;
        function loadData(target, data, remainText) {
            var state = $.data(target, 'select');
            var opts = state.options;
            state.data = opts.loadFilter.call(target, data);
            state.groups = [];
            data = state.data;

            var selected = $(target).select('getValues');
            var dd = [];
            var group = undefined;
            for ( var i = 0; i < data.length; i++) {
                var row = data[i];
                var v = row[opts.valueField] + '';
                var s = row[opts.textField];
                var g = row[opts.groupField];

                // TODO 选项分组？
                if (g) {
                    if (group != g) {
                        group = g;
                        var grow = {
                            value : g,
                            domId : ('_tenhiUI_select_' + itemIndex++)
                        };
                        state.groups.push(grow);
                        dd.push('<li id="' + grow.domId + '" val="' + v + '" class="combobox-group">');
                        dd.push(opts.groupFormatter ? opts.groupFormatter.call(target, g) : g);
                        dd.push('</li>');
                    }
                } else {
                    group = undefined;
                }

                var cls = '';//'combobox-item' + (row.disabled ? ' combobox-item-disabled' : '') + (g ? ' combobox-gitem' : '');
                dd.push('<li id="' + (state.itemIdPrefix + '_' + i) + '" class="' + cls + '">');
                dd.push(opts.formatter ? opts.formatter.call(target, row) : s);
                dd.push('</li>');

                if (row['selected'] && $.inArray(v, selected) == -1) {
                    selected.push(v);
                }
            }
            getPanel(target).find('ul').html(dd.join(''));

            getPanel(target).find('li').unbind().bind('click', function (e) {
                var item = $(this);
                if (!item.length || item.hasClass('disabled')) {
                    return
                }
                var row = opts.finder.getRow(target, item);
                if (!row) {
                    return
                }
                var value = row[opts.valueField];
                if (opts.multiple) {
                    if (item.hasClass('selected')) {
                        unselect(target, value);
                    } else {
                        select(target, value);
                    }
                } else {
                    select(target, value);
                    hidePanel(target);
                }
                e.stopPropagation();
            });

            if (opts.multiple) {
                setValues(target, selected, remainText);
            } else {
                setValues(target, selected.length ? [ selected[selected.length - 1] ] : [], remainText);
            }

            opts.onLoadSuccess.call(target, data);
        }

        $.fn.select = function(options, param){
            if (typeof options == 'string') {
                var method = $.fn.select.methods[options];
                if (method) {
                    return method(this, param);
                }
            }

            options = options || {};
            return this.each(function() {
                var state = $.data(this, 'select');
                if (state) {
                    $.extend(state.options, options);
                    createSelect(this);
                } else {
                    state = $.data(this, 'select', {
                        options: $.extend({}, $.fn.select.defaults, $.fn.select.parseOptions(this), options),
                        data: []
                    });
                    var r = createSelect(this);
                    state = $.data(this, "select", {
                        options : $.extend({}, $.fn.select.defaults,
                            $.fn.select.parseOptions(this), options),
                        combo : r.combo,
                        panel : r.panel,
                        data : []
                    });
                    SELECT_SERNO++;
                    state.itemIdPrefix = '_hiteam_sel_i' + SELECT_SERNO;
                    state.groupIdPrefix = '_hiteam_sel_g' + SELECT_SERNO;
                    bindEvents(this);
                    var data = $.fn.select.parseData(this);
                    if (data.length) {
                        loadData(this, data);
                    }
                }

                if (state.options.data) {
                    loadData(this, state.options.data);
                }
                request(this);
            });
        };

        $.fn.select.parseOptions = function(target) {
            // $.fn.validatebox.parseOptions(target),
            return $.extend({}, {});
        };

        $.fn.select.parseData = function(target) {
            var data = [];
            var opts = $(target).select('options');
            if (opts.size != null && opts.size == 'big') {
                $(target).siblings('div.xw_circlemohusearch').find('ul.draDownToChoselist li').each(function() {
                    _parseItem(this);
                });
            } else if (opts.size != null && opts.size == 'small') {
                $(target).siblings('div.choseDiv').find('ul.choseDivSeconListUl li').each(function() {
                    _parseItem(this);
                });
            } else {
                $(target).siblings('div.mohusearch').find('ul.jieguolist li').each(function() {
                    _parseItem(this);
                });
            }

            return data;

            function _parseItem(el) {
                var t = $(el);
                var row = {};
                row[opts.valueField] = t.attr('val') != undefined ? t.attr('val') : t.html();
                row[opts.textField] = t.html();
                row['selected'] = t.is(':selected');
                row['disabled'] = t.is(':disabled');

                data.push(row);
            }
        };

        $.fn.select.methods = {
            options: function(jq) {
                return $.data(jq[0], "select").options;
            },
            getData : function(jq) {
                return $.data(jq[0], 'select').data;
            },
            setValues : function(jq, values) {
                return jq.each(function() {
                    setValues(this, values);
                });
            },
            setValue : function(jq, value) {
                return jq.each(function() {
                    setValues(this, [ value ]);
                });
            },
            getValues : function(jq) {
                return getValues(jq[0]);
            },
            reset : function(jq) {
                return jq.each(function() {
                    $(this).select('setValues', []);
//				var opts = $(this).select('options');
//				if (opts.multiple) {
//					$(this).select('setValues', opts.originalValue);
//				} else {
//					$(this).select('setValue', opts.originalValue);
//				}
                });
            },
            loadData : function(jq, data) {
                return jq.each(function() {
                    loadData(this, data);
                });
            },
            reload : function(jq, url) {
                return jq.each(function() {
                    request(this, url);
                });
            },
            select : function(jq, value) {
                return jq.each(function() {
                    select(this, value);
                });
            },
            unselect : function(jq, value) {
                return jq.each(function() {
                    unselect(this, value);
                });
            },
            getText : function(jq) {
                return getRemainText(jq[0]);
            }
        };

        $.fn.select.defaults = {
            valueField : 'value',
            textField : 'text',
            method : 'post',
            url : null,
            data : null,
            separator : ',',
            isShowQuery : false,
            editable : false,
            width : null,
            queryParamName : 'name',

            keyHandler: {
                up: function (e) {
                    nav(this, 'prev');
                    e.preventDefault()
                },
                down: function (e) {
                    nav(this, 'next');
                    e.preventDefault()
                },
                left: function (e) {
                },
                right: function (e) {
                },
                enter: function (e) {
                    doEnter(this);
                },
                query: function (q, e) {
//                doQuery(this, q)
                }
            },
            filter : function(q, row) {
                var opts = $(this).select('options');
                return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) == 0;
            },
            formatter : function(row) {
                var opts = $(this).select('options');
                return row[opts.textField];
            },
            loader : function(param, success, error) {
                var opts = $(this).select('options');
                if (!opts.url) {
                    return false;
                }
                $.ajax({
                    type : opts.method,
                    url : opts.url,
                    data : param,
                    dataType : 'json',
                    success : function(data) {
                        success(data);
                    },
                    error : function() {
                        error.apply(this, arguments);
                    }
                });
            },
            loadFilter : function(data) {
                return data;
            },
            finder: {
                getEl: function (target, value) {
                    var index = getRowIndex(target, value);
                    var id = $.data(target, 'select').itemIdPrefix + '_' + index;
                    return $('#' + id);
                },
                getRow: function (target, p) {
                    var state = $.data(target, 'select');
                    var index = (p instanceof jQuery) ? p.attr('id').substr(state.itemIdPrefix.length + 1) : getRowIndex(target, p);
                    return state.data[parseInt(index)];
                }
            },
            onBeforeLoad : function(param) {
            },
            onLoadSuccess : function() {
            },
            onLoadError : function() {
            },
            onSelect : function(record) {
            },
            onUnselect : function(record) {
            }
        };

    });