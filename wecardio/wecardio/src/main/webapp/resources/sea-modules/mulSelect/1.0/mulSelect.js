/**
 * 多选控件<br>
 *     <div class="searchFormChose">
 *       <dl>
 *           <dt>test：</dt>
 *           <input id="cc" value="1">
 *       </dl>
 *   </div>
 */
define('mulSelect/1.0/mulSelect', ['$', 'mulSelect/1.0/lang/{language}', 'mulSelect/1.0/{theme}/css/mulSelect.css'],
    function (require, exports, module) {
        /**
         * 创建select下拉框
         */
        function createSelect(target) {
            $(target).hide();
            $('<dd class="moreSelect">' + '</dd>').insertAfter(target)
        }


        /**
         * select the specified value
         */
        function select(target, value) {
            var opts = $.data(target, 'mulSelect').options;
            var values = $(target).mulSelect('getValues');
            if ($.inArray(value + '', values) == -1) {
                if (opts.multiple) {
                    values.push(value);
                } else {
                    values = [value];
                }
                setValues(target, values);
                opts.onSelect.call(target, findRowBy(target, value));
            }
        }

        /**
         * unselect the specified value
         */
        function unselect(target, value) {
            var opts = $.data(target, 'mulSelect').options;
            var values = $(target).mulSelect('getValues');
            var index = $.inArray(value + '', values);
            if (index >= 0) {
                values.splice(index, 1);
                if (values.length == 0) {
                    values.push('');
                }
                setValues(target, values);
                opts.onUnselect.call(target, findRowBy(target, value));
            }
        }

        /**
         * set values
         */
        function setValues(target, values, remainText) {
            var opts = $.data(target, 'mulSelect').options;

            $(target).siblings("dd.moreSelect").find("div.sel").removeClass('selected');

            var vv = [], ss = [];
            for (var i = 0; i < values.length; i++) {
                var v = values[i];
                var s = v;
                var row = findRowBy(target, v);
                // 当点击了不限选项时，清空其他已选（v == '' 认为是选择了不限）
                if (v == null || v == "") {
                    $(target).siblings("dd.moreSelect").find("div.sel").removeClass('selected');
                    if (row) {
                        s = row[opts.textField];
                        $('#' + row.domId).addClass('selected');
                    }
                    vv = [];
                    ss = [];
                    vv.push(v);
                    ss.push(v);
                    break;
                }
                if (row) {
                    s = row[opts.textField];
                    $('#' + row.domId).addClass('selected');
                }
                vv.push(v);
                ss.push(s);
            }

            $(target).val(vv.join(opts.separator));

            if (!remainText) {
                $(target).attr('selText', ss.join(opts.separator));
            }
        }

        function getValues(target) {
            var opts = $.data(target, 'mulSelect').options;
            var val = $(target).val();
            return ((val != '' && val) ? val.split(opts.separator) : []);
        }

        function findRowBy(target, value, param, isGroup) {
            var state = $.data(target, 'mulSelect');
            var opts = state.options;
            if (isGroup) {
                return _findRow(state.groups, param, value);
            } else {
                return _findRow(state.data, (param ? param : opts.valueField), value);
            }

            function _findRow(data, key, value) {
                for (var i = 0; i < data.length; i++) {
                    var row = data[i];
                    if (row[key] + "" == value) {
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
            var opts = $.data(target, 'mulSelect').options;
            if (url) {
                opts.url = url;
            }
            // if (!opts.url) return;
            param = param || {};

            if (opts.onBeforeLoad.call(target, param) == false) {
                return;
            }

            opts.loader.call(target, param, function (data) {
                if (opts.showAll) {
                    var a = {};
                    a[opts.valueField] = '';
                    a[opts.textField] = mulSelectLanguage.all;
                    a['selected'] = true;
                    data.splice(0, 0, a);
                }
                loadData(target, data, remainText);
            }, function () {
                opts.onLoadError.apply(this, arguments);
            });
        }

        /**
         * load data, the old list items will be removed.
         */
        var itemIndex = 1;

        function loadData(target, data, remainText) {
            var state = $.data(target, 'mulSelect');
            var opts = state.options;
            state.data = opts.loadFilter.call(target, data);
            state.groups = [];
            data = state.data;

            var selected = $(target).mulSelect('getValues');
            var dd = [];
            var group = undefined;
            for (var i = 0; i < data.length; i++) {
                var row = data[i];
                var v = row[opts.valueField] + '';
                var s = row[opts.textField];
                var g = row[opts.groupField];

                // TODO 选项分组？
                if (g) {
                    if (group != g) {
                        group = g;
                        var grow = {
                            value: g,
                            domId: ('_tenhiUI_mulselect_' + itemIndex++)
                        };
                        state.groups.push(grow);
                        dd.push('<div id="' + grow.domId + '" value="' + v + '" class="sel">');
                        dd.push('<a>');
                        dd.push(opts.groupFormatter ? opts.groupFormatter.call(target, g) : g);
                        dd.push('</a><i></i>');
                        dd.push('</div>');
                    }
                } else {
                    group = undefined;
                }

                var cls = 'sel';//'combobox-item' + (row.disabled ? ' combobox-item-disabled' : '') + (g ? ' combobox-gitem' : '');
                row.domId = '_tenhiUI_mulselect_' + itemIndex++;
                dd.push('<div id="' + row.domId + '" value="' + v + '" class="' + cls + '">');
                dd.push('<a>');
                dd.push(opts.formatter ? opts.formatter.call(target, row) : s);
                dd.push('</a><i></i>');
                dd.push('</div>');

                if (row['selected'] && $.inArray(v, selected) == -1) {
                    selected.push(v);
                }
            }
            $(target).siblings('dd.moreSelect').html(dd.join(''));

            $(target).siblings('dd.moreSelect').find("div.sel").off('click').on('click', function () {
                var val = $(this).attr('value');
                var text = $(this).find('a').html();

                if (opts.multiple) {
                    if ($(this).hasClass('selected')) {
                        unselect(target, val, text);
                    } else {
                        select(target, val, text);
                    }
                } else {
                    select(target, val, text);
                }

            });

            if (opts.multiple) {
                setValues(target, selected, remainText);
            } else {
                setValues(target, selected.length ? [selected[selected.length - 1]] : [], remainText);
            }

            opts.onLoadSuccess.call(target, data);
        }

        $.fn.mulSelect = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.mulSelect.methods[options];
                if (method) {
                    return method(this, param);
                }
            }

            options = options || {};
            return this.each(function () {
                var state = $.data(this, 'mulSelect');
                if (state) {
                    $.extend(state.options, options);
                    createSelect(this);
                } else {
                    state = $.data(this, 'mulSelect', {
                        options: $.extend({}, $.fn.mulSelect.defaults, $.fn.mulSelect.parseOptions(this), options),
                        data: []
                    });
                    createSelect(this);
                    var data = $.fn.mulSelect.parseData(this);
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

        $.fn.mulSelect.parseOptions = function (target) {
//		return $.extend({}, $.fn.validatebox.parseOptions(target), {});
            return $.extend({}, {});
        };

        $.fn.mulSelect.parseData = function (target) {
            var data = [];
            var opts = $(target).mulSelect('options');
            $(target).siblings('dd.moreSelect').find('div.sel').each(function () {
                _parseItem(this);
            });
            return data;

            function _parseItem(el) {
                var t = $(el);
                var row = {};
                row[opts.valueField] = t.attr('value') != undefined ? t.attr('value') : t.find('a').html();
                row[opts.textField] = t.find('a').html();
                row['selected'] = t.is(':selected');
                row['disabled'] = t.is(':disabled');

                data.push(row);
            }
        };

        $.fn.mulSelect.methods = {
            options: function (jq) {
                return $.data(jq[0], "mulSelect").options;
            },
            getData: function (jq) {
                return $.data(jq[0], 'mulSelect').data;
            },
            setValues: function (jq, values) {
                return jq.each(function () {
                    setValues(this, values);
                });
            },
            setValue: function (jq, value) {
                return jq.each(function () {
                    setValues(this, [value]);
                });
            },
            getValues: function (jq) {
                return getValues(jq[0]);
            },
            reset: function (jq) {
                return jq.each(function () {

                    $(this).mulSelect('setValues', ['']);
//				var opts = $(this).mulSelect('options');
//				if (opts.multiple) {
//					$(this).mulSelect('setValues', opts.originalValue);
//				} else {
//					$(this).mulSelect('setValue', opts.originalValue);
//				}
                });
            },
            loadData: function (jq, data) {
                return jq.each(function () {
                    loadData(this, data);
                });
            },
            reload: function (jq, url) {
                return jq.each(function () {
                    request(this, url);
                });
            },
            select: function (jq, value) {
                return jq.each(function () {
                    select(this, value);
                });
            },
            unselect: function (jq, value) {
                return jq.each(function () {
                    unselect(this, value);
                });
            }
        };

        $.fn.mulSelect.defaults = {
            valueField: 'value',
            textField: 'text',
            method: 'post',
            url: null,
            data: null,
            separator: ',',
            multiple: true,
            showAll: false,

            keyHandler: {},
            filter: function (q, row) {
                var opts = $(this).mulSelect('options');
                return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) == 0;
            },
            formatter: function (row) {
                var opts = $(this).mulSelect('options');
                return row[opts.textField];
            },
            loader: function (param, success, error) {
                var opts = $(this).mulSelect('options');
                if (!opts.url) {
                    return false;
                }
                $.ajax({
                    type: opts.method,
                    url: opts.url,
                    data: param,
                    dataType: 'json',
                    success: function (data) {
                        success(data);
                    },
                    error: function () {
                        error.apply(this, arguments);
                    }
                });
            },
            loadFilter: function (data) {
                return data;
            },
            onBeforeLoad: function (param) {
            },
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onSelect: function (record) {
            },
            onUnselect: function (record) {
            }
        };
    });