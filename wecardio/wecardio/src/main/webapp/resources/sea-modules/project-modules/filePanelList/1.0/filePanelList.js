/**
 * 文件列表面板
 */
define(
    '{projectModulePrefix}/filePanelList/1.0/filePanelList',
    ['$', 'uploadFile/1.0/uploadFile','msgBox/pc/1.0/msgBox'],
    function (require, exports, module) {
        var uploadFile = require('uploadFile/1.0/uploadFile');
        var msgBox = require('msgBox/pc/1.0/msgBox');

        var key = 'filePanelList';

        var opType = {
            del:'delData',
            get:'getData'
        };

        function createPanel(target,data){

            var $this = $(target);
            var id = $this.attr('id');
            var pannalouterWid = $('#Panel_' + id).find('div.xw_pannalb').outerWidth() - 10;
            var showNumb = Math.floor(pannalouterWid/135);

            //初始化所有图片
            var itemContent = [];

            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                itemContent.push(
                    '<li>' +
                    '   <h6 title="'+item.fileName+'">'+ item.fileName +'</h6>' +
                    '   <a data-value-id="'+item.id+'" class="xw_check"></a>' +
                    '</li>');
            }

            //初始化面板对象
            panel = new BasePanel('#' + key + 'Panel_' + id, {
                /**面板可视范围内显示的元素个数，如果展开后，
                 * 则是disItem = disItem * expanRow*/
                disItem:showNumb,
                /**显示面板宽度*/
                width:pannalouterWid,
                /**面板里需要承载的html内容*/
                contentHtml: itemContent,
                /**是否显示展开按钮*/
                isExpan: false,
                /**是否自动展开*/
                isAutoExpan: false,
                /**展开后显示的行数*/
                expanRows: 2,
                /**箭头位置：top：顶部；middle：中间*/
                arrowAlign: 'middle',
                /**面板移动完成后事件*/
                onmoveend: function (pageNo) {
                },
                itemClick:function(idex,$this){
                    //选中文件
                    if($this.hasClass('xw_check')){
                        $this.toggleClass('checked');
                    }
                }
            });
        }

        function createFilePanelList(target){
            var $this = $(target);
            var id = $this.attr('id');
            var state = $.data(target, key);
            var opts = state.options;

            $this.hide();

            $this.after('<div class="userMain" id="Panel_' + id +'" style="margin-bottom:10px;">'+
            '    <div class="userDetailCont" style="padding-top:0px;">'+
                '    <div class="userDetailContHead">'+
                '    <h1 class="userDetailConthText"><font>'+opts.panelIndex+'</font>附件列表</h1>'+
                '<ul class="toolBarList" style="margin-top:5px;">'+
                '<li id="btnAddFile_' + id +'"><a>上传新的附件</a></li>'+
                '<li id="btnDelFile_' + id +'"><a>删除所选附件</a></li>'+
                '</ul>'+
                '<div class="allCheckMain" style="margin-top:3px;"><a rel="0" id="allCheck_'+id+'" class="allCheck xw_allCheck">全选</a></div>'+
                '</div>'+
                '<div class="userDetail xw_pannalb" style="padding:5px; overflow:hidden;">'+
                '<ul class="fujianlist xw_fujian" id="'+key + 'Panel_'+id+'"></ul>'+
            '</div>'+
            '    </div>'+
            '</div>');
        }

        function delRequest(target,type, param) {
            var opts = $.data(target, key).options;

            // if (!opts.url) return;
            param = param || [];

            opts.loader.call(target, {ids:param.join(',')}, type, function (data) {

                if(data.type == 'success'){
                    //删除原有数据
                    for(var i = opts.data.length - 1; i >= 0; i--){
                        var item = opts.data[i];
                        for(var j = 0; j < param.length; j++){
                            if(param[j] == item.id){
                                opts.data.splice(i,1);
                            }
                        }
                    }
                    loadData(target);
                }
                msgBox.tips(data.content);
            }, function () {
                opts.onLoadError.apply(this, arguments);
            });
        }

        /**
         * request remote data if the url property is setted.
         */
        function request(target, url, param) {
            var opts = $.data(target, key).options;

            if (url) {
                opts.url = url;
            }

            // if (!opts.url) return;
            param = param || {};

            if (opts.onBeforeLoad.call(target, param) == false) {
                return;
            }

            opts.loader.call(target, param, opType.get, function (data) {
                opts.data = data;
                loadData(target);
            }, function () {
                opts.onLoadError.apply(this, arguments);
            });
        }

        function loadData(target){
            createPanel(target,$.data(target, key).options.data);
        }

        function addFilePanelListEvent(target){
            var $this = $(target);
            var id = $this.attr('id');
            var state = $.data(target, key);
            var opts = state.options;

            //删除附件事件
            $('#btnDelFile_' + id).click(function(){
                var checkItems = $('#'+key+"Panel_"+id).find('a.checked');
                if(!checkItems.length){
                    msgBox.tips('请选择删除的附件！');
                    return;
                }

                msgBox.confirm({title:'提示',msg:'确认删除?',callback:function(btnType){
                    if(btnType == msgBox.ButtonType.CANCEL){
                        return;
                    }

                    var ids = [];
                    $.each(checkItems,function(){
                        ids.push($(this).attr('data-value-id'));
                    });

                    delRequest(target,opType.del,ids);
                }});

            });

            //全选按钮
            $('#allCheck_' + id).click(function(){
                var $this = $(this);
                $this.toggleClass('checked');
                var $check = $('#'+key+"Panel_"+id).find('a.xw_check');
                if($this.hasClass('checked')){
                    $check.addClass('checked');
                }else{
                    $check.removeClass('checked');
                }
            });

            //上传附件事件
            $('#btnAddFile_' + id).click(function(){
                //打开上传界面
                uploadFile.open({
                    relTable: opts.relTable,
                    relId: opts.relId,
                    guid:opts.guid,
                    fileSizeLimit:opts.fileSizeLimit,
                    multi:opts.multi,
                    fileTypeExts:opts.fileTypeExts,
                    handler: function (data) {
                        if(data && data.length > 0) {
                            var dt = $(target).filePanelList('options').data;
                            for (var i = 0; i < data.length; i++) {
                                var item = data[i];
                                dt.push(item);
                            }
                        }
                        loadData(target);
                    },
                    //上传界面关闭回调函数
                    close: function (data) {

                    }
                });
            });

        }



        $.fn.filePanelList = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.filePanelList.methods[options];
                if (method) {
                    return method(this, param);
                }
            }

            options = options || {};
            return this.each(function () {
                var state = $.data(this, key);
                if (state) {
                    $.extend(state.options, options);
                    createFilePanelList(this);
                } else {
                    state = $.data(this, key, {
                        options: $.extend({}, $.fn.filePanelList.defaults, $.fn.filePanelList.parseOptions(this), options),
                        data: []
                    });

                    createFilePanelList(this);
                    addFilePanelListEvent(this);

                    var data = $.fn.filePanelList.parseData(this);

                    if (data.length) {
                        loadData(this, data);
                    }
                }

                if(state.options.relId && state.options.relTable){
                    request(this,'',{
                        relTable:state.options.relTable,
                        relId:state.options.relId,
                        guid:state.options.guid
                    });
                }
            });
        };

        $.fn.filePanelList.parseOptions = function (target) {
            return $.extend({}, {});
        };

        $.fn.filePanelList.parseData = function (target) {
            return $(target).filePanelList('options').data;
        };

        $.fn.filePanelList.defaults = {
            /**关联的业务表名*/
            relTable: '',
            /**关联的业务主键ID*/
            relId: '',
            /**存储文件列表字段*/
            storeValField:'',
            /**流程相关的guid*/
            guid:'',
            /**面板序号*/
            panelIndex:'',
            /**多附件上传还是单附件*/
            multi:true,
            /**上传允许的文件类型*/
            fileTypeExts : '*.jpg;*.png;*.bmp',
            /**允许上传的文件大小*/
            fileSizeLimit:5*1024*1024,
            method: 'post',
            url: path + '/admin/file/list',
            delUrl: path + '/admin/file/remove',
            data: [],
            width: null,
            keyHandler: {},
            loader: function (param, type,success, error) {
                var opts = $(this).filePanelList('options');
                var url = '';
                if(type == 'getData'){
                    url = opts.url;
                }else if(type == 'delData'){
                    url = opts.delUrl;
                }

                if(!url){
                    return false;
                }

                $.ajax({
                    type: opts.method,
                    url: url,
                    data: param,
                    dataType: 'json',
                    success: function (data) {
                        success && success(data,type);
                    },
                    error: function () {
                        error && error.apply(this, arguments);
                    }
                });
            },
            onBeforeLoad: function (type,param) {
            },
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            }
        };

        $.fn.filePanelList.methods = {
            options: function (jq) {
                return $.data(jq[0], key).options;
            },
            /**
             * 获取当前的附件信息
             * @param jq
             * @returns {jQuery.data|Function|jQuery}
             */
            getValues: function (jq) {
                return $(jq[0]).filePanelList('options').data;
            },
            /**
             * 同步信息到数据库
             * @param jq
             * @param params
             * {
             *   relId:'业务ID，如果为空，则获取初始化设置的值'
             *   bckFun:'回调函数'
             * }
             * @returns {*}
             */
            sync2Db:function(jq,params){
                var opt = $(jq[0]).filePanelList('options');
                var optionsData = opt.data;

                if(!optionsData || optionsData.length == 0){
                    params.bckFun && params.bckFun({content:'操作成功'});
                    return;
                }

                var fileIds = [];

                for(var i = 0; i < optionsData.length; i++){
                    fileIds.push(optionsData[i].id);
                }

                $.post(path + '/admin/file/updateRelInfo',{
                    fileIds: fileIds.join(','),
                    relTable: opt.relTable,
                    relId: params.relId || opt.relId,
                    guid: opt.guid
                },function(data){
                    if(data.type != 'success'){
                        msgBox.warn(data.content);
                        return;
                    }

                    params.bckFun && params.bckFun(data);
                });
            }
        };
    }
);