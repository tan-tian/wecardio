define('pageBar/1.0/pageBar', ['$','pageBar/1.0/lang/{language}', 'pageBar/1.0/css/{theme}/pageBar.css'], function (require, exports, module) {

    function createPagination(target) {
//		var opts = $.data(target, "pagination").options;
        var _target = $(target).addClass("pageBar").empty().show();
        $('<div class="snPages"></div>').appendTo(_target);

        // 插入分页链接
        drawLinks(target);
    }


    /**
     * 根据显示的链接数，计算中间可点击链接的起止点
     */
    function getInterval(target) {
        var opts = $.data(target, "pagination").options;
        var neHalf = Math.ceil(opts.entriesNum / 2);
        var totalPage = Math.ceil(opts.total / opts.pageSize);
        var upperLimit = totalPage - opts.entriesNum;
        var start = opts.pageNumber > neHalf ? Math.max(Math.min(~~opts.pageNumber - neHalf, upperLimit), 0) : 0;
        var end = opts.pageNumber > neHalf ? Math.min(~~opts.pageNumber + neHalf, totalPage) : Math.min(opts.entriesNum, totalPage);
        return [start, end];
    }

    /**
     * 分页操作
     */
    function pageSelected(target, pageNumber) {
        var opts = $.data(target, "pagination").options;
        var totalPage = Math.ceil(opts.total / opts.pageSize) || 1;
        var pageNum = pageNumber;
        if (pageNumber < 1) {
            pageNum = 1;
        }
        if (pageNumber > totalPage) {
            pageNum = totalPage;
        }
        opts.pageNumber = pageNum;
        // 重新渲染分页链接
        drawLinks(target);
        opts.onSelectPage.call(target, pageNum, opts.pageSize);
        //setPageInfo(target);
    }

    /**
     * 将分页链接插入到容器中
     */
    function drawLinks(target) {
        var opts = $.data(target, "pagination").options;
        var _target = $(target).find('div.snPages').empty();
        var interval = getInterval(target);
        var totalPage = Math.ceil(opts.total / opts.pageSize) || 1;

        var appendItem = function (pageNum, appendopts) {
            pageNum = pageNum < 0 ? 0 : (pageNum < totalPage ? pageNum : totalPage - 1);
            appendopts = jQuery.extend({text: pageNum + 1, classes: ""}, appendopts || {});
            var lnk;
            if ((pageNum + 1) == opts.pageNumber) {
                lnk = $('<a href="javascript:void(0);" class="current" style="color: rgb(255, 102, 0);">' + (appendopts.text) + '</a>');
            } else {
                lnk = $('<a href="javascript:void(0);">' + (appendopts.text) + '</a>').bind('click', function () {
                    pageSelected(target, (pageNum + 1));
                });
            }
            if (appendopts.classes) {
                lnk.addClass(appendopts.classes);
            }
            _target.append(lnk);
        };

        // 上一页
        if (opts.showPrev) {
            var style = "";
            if (opts.pageNumber > 1) {
                style = "border-color: #fff #000000 #fff #fff;";
            }
            var prev = $('<span class="prev" title="'+pageBarLanguage.previous+'"><b style="' + style + '"></b></span>').bind('click', function () {
                if (opts.pageNumber > 1) {
                    pageSelected(target, ~~opts.pageNumber - 1);
                }
            });
            _target.append(prev);
        }

        // 首页
        if (interval[0] > 0 && opts.edgeEntriesNum > 0) {
            var end = Math.min(opts.edgeEntriesNum, interval[0]);
            for (var i = 0; i < end; i++) {
                appendItem(i);
            }
            if (opts.edgeEntriesNum < interval[0] && opts.ellipseText) {
                $('<span>' + opts.ellipseText + '</span>').appendTo(_target);
            }
        }

        // 中间可点击链接
        for (var i = interval[0]; i < interval[1]; i++) {
            appendItem(i);
        }

        // 末页
        if (interval[1] < totalPage && opts.edgeEntriesNum > 0) {
            if (totalPage - opts.edgeEntriesNum > interval[1] && opts.ellipseText) {
                $('<span>' + opts.ellipseText + '</span>').appendTo(_target);
            }

            var begin = Math.max(totalPage - opts.edgeEntriesNum, interval[1]);
            for (var i = begin; i < totalPage; i++) {
                appendItem(i);
            }
        }

        // 下一页
        if (opts.showNext) {
            var style = "";
            if (opts.pageNumber >= totalPage) {
                style = "border-color: #fff #fff #fff #b1b1b1;";
            }
            var next = $('<a class="next" href="javascript:void(0);" title="'+pageBarLanguage.next+'"><b style="' + style + '"></b></a>').bind('click', function () {
                var totalPage = Math.ceil(opts.total / opts.pageSize);
                if (opts.pageNumber < totalPage) {
                    pageSelected(target, ~~opts.pageNumber + 1);
                }
            });
            _target.append(next);
        }

        // 是否显示跳转输入框
        if (opts.showNumInput) {
            $('<div>'+pageBarLanguage.jumpTo+'<input type="text" />'+pageBarLanguage.pageNo+'<input type="button" class="pagesubmit" value="'+pageBarLanguage.confirm+'" /></div>').appendTo(_target);

            _target.find('input[type=text]').bind('keydown', function (e) {
                if (e.keyCode == 13) {
                    var pageNum = parseInt($(this).val()) || 1;
                    pageSelected(target, pageNum);
                    return false;
                }
            });
            _target.find('input[type=button]').bind('click', function () {
                var pageNum = parseInt($(this).siblings('input[type=text]').val()) || 1;
                pageSelected(target, pageNum);
                return false;
            });
        }
    }

    /**
     * 设置分页信息
     */
    function setPageInfo(target) {
        // TODO 待扩展，显示当前分页信息
        var opts = $.data(target, "pagination").options;
        var totalPage = Math.ceil(opts.total / opts.pageSize) || 1;
        var num = $(target).find("input.pagination-num");
        num.val(opts.pageNumber);
        num.parent().next().find("span").html(opts.afterPageText.replace(/{pages}/, totalPage));
        var disMsg = opts.displayMsg;
        disMsg = disMsg.replace(/{from}/, opts.pageSize * (opts.pageNumber - 1) + 1);
        disMsg = disMsg.replace(/{to}/, Math.min(opts.pageSize * (opts.pageNumber), opts.total));
        disMsg = disMsg.replace(/{total}/, opts.total);
        $(target).find(".pagination-info").html(disMsg);

        if (opts.loading) {
            $(target).find("a[icon=pagination-load]").find(".pagination-load").addClass("pagination-loading");
        } else {
            $(target).find("a[icon=pagination-load]").find(".pagination-load").removeClass("pagination-loading");
        }
    }

    $.fn.pageBar = function (options, param) {
        if (typeof options == "string") {
            return $.fn.pageBar.methods[options](this, param);
        }
        options = options || {};
        return this.each(function () {
            var opts;
            var state = $.data(this, "pagination");
            if (state) {
                opts = $.extend(state.options, options);
            } else {
                opts = $.extend({}, $.fn.pageBar.defaults, options);
                $.data(this, "pagination", {
                    options: opts
                });
            }
            createPagination(this);
            setPageInfo(this);
        });
    };

    // methods
    $.fn.pageBar.methods = {
        options: function (jq) {
            return $.data(jq[0], "pagination").options;
        }
    };
    $.fn.pageBar.defaults = {
        total: 1,
        entriesNum: 4,		// 可点击超链接数量，不包括首尾
        edgeEntriesNum: 1,	// 首尾可点击超链接数
        ellipseText: '...',
        showNumInput: true,// 是否显示跳转输入框
        showPrev: true,
        showNext: true,
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 20, 30, 50],
        loading: false,
        buttons: null,
        showPageList: false,
        showRefresh: false,
        onSelectPage: function (pageNumber, pageSize) {
        },
        onBeforeRefresh: function (pageNumber, pageSize) {
        },
        onRefresh: function (pageNumber, pageSize) {
        },
        onChangePageSize: function (pageSize) {
        },
        beforePageText: pageBarLanguage.beforePageText,
        afterPageText: pageBarLanguage.afterPageText,
        displayMsg: pageBarLanguage.displayMsg
    };
});