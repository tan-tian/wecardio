/**
 * Created by tantian on 2015/7/13.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates', 'select', 'mulSelect'], function (msgBox) {
    $(document).ready(function() {
        // 初始化搜索表单
        initSearchForm();
        // 分页栏
        $('#pageBar').pageBar({
            pageNumber : $('#pageNo').val(),
            pageSize : $('#pageSize').val(),
            onSelectPage : function(page) {
                query(page);
            }
        });

        // 更多查询条件
        $("a.moresearchForm").on('click', function() {
            var $this = $(this);
            $this.toggleClass("up");
            $this.parents("div.objBoxCont").find("div.xw_kMainContentbody").slideToggle('fast');
        });
        // 按钮事件
        $('a.queryBtn').on('click', function() {
            query(1);
        });
        $('a.resetBtn').on('click', function () {
            $('#qform')[0].reset();
            $('#isWalletActive').mulSelect('reset');
            $('#doctor').select('reset');
            $('#org').select('reset');
        });

        $('a.giveupBt').on('click', function () {
            $('#qform')[0].reset();
            $('#isWalletActive').mulSelect('reset');
            $('#doctor').select('reset');
            $('#org').select('reset');
        });

        $('#bindingBtn').on('click', binding);
        //设置标记事件
        $('#markBtn').on('click', function () {
        	var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '680px',
                height : '450px',
                url : path +'/'+userTypePath+'/device/setMark?imeId='+ids.vals,
                close : function(flag) {
                   if (flag !== null) {
                      window.location.href = path + '/doctor/device/holter620';
                   }
                }
            });
        });
        
        //绑定患者
         $('#bindPatientBtn').on('click', function () {
        	var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            window.location.href = path + '/'+userTypePath+'/device/bindPatient?imeId='+ids.vals;
        });
        
         //解除绑定
        $('#removeBindBtn').on('click', function () {
        	var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '680px',
                height : '450px',
                url : path +'/'+userTypePath+'/device/removeBindPage?imeId='+ids.vals,
                close : function(flag) {
                   if (flag !== null) {
                      window.location.href = path + '/'+userTypePath+'/device/holter620?iType=2';
                   }
                }
            });
        });
        
        //查看日志
         $('#logBtn').on('click', function () {
        	var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            window.location.href = path + '/'+userTypePath+'/device/logPage?imeId='+ids.vals;
        });
        
        //holter上传文件
         $('#holterupBtn').on('click', function () {
        	var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            window.location.href = path + '/'+userTypePath+'/patient/holter?imeId='+ids.vals;
        });
        
        $('#addBtn').on('click', function () {
            addEdit(1);
        });
        $('#editBtn').on('click', function () {
            addEdit(2);
        });
		
		//新增设备
        $('#addDevBtn').on('click', function () {
        	
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '680px',
                height : '450px',
                url : path +'/'+userTypePath+'/device/addDevicePage',
                close : function(flag) {
                   if (flag !== null) {
                      window.location.href = path + '/'+userTypePath+'/device/holter620';
                   }
                }
            });
        });
        
        // 查询
        query();
    });

    // 初始化搜索表单
    function initSearchForm() {
       $('#org').select({
            url : path + '/'+userTypePath+'/organization/sel',
            isShowQuery : true
        });
    }

    /**
     * 分页查询
     * @param page
     */
    function query(page) {
        if (page) {
            $('#pageNo').val(page);
        }

        $.post(path + '/'+userTypePath+'/device/page', $('#qform').serialize(), function(data) {
            // 设置总数
            $('#totalNum').text(data['total']);
            // 用模板初始化列表
            var $list = $('#list');
            $list.empty();
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // 列表事件
            listEvents();
            // 重设分页栏
            $('#pageBar').pageBar({
                total : data['total']
            });
        });
    }

    /**
     * 列表事件
     */
    function listEvents() {
        // 勾选事件
        $("a.checkbox").off('click').on('click', function() {
            $(this).toggleClass("checked");
        });

        $("a.xw_allcheck").off('click').on('click',function() {
            var isopen = parseInt($(this).attr('rel'));

            if (isopen == 0) {
                $("a.checkbox").addClass("checked");
                $(this).addClass("checked");
                $(this).attr("rel", 1)
            } else {
                $("a.checkbox").removeClass("checked");
                $(this).removeClass("checked");
                $(this).attr("rel", 0)
            }
        }).attr('rel', 0).removeClass('checked');
        // 跳转事件
        $("div.xw_huanzheDiv h1").on('click', function() {
            var $container = $(this).closest('div.huanzheDiv');
            var id = $container.attr('id');
            //window.location.href = path + '/'+userTypePath+'/patient/' + id + '/detail?iType='+iType;
        });

        //解除绑定
        $("a.pingjia").on('click', function() {
            var $container = $(this).closest('div.huanzheDiv');
            var id = $container.attr('id');

            msgBox.confirm({
                msg : comfireReleaseBinding,
                callback : function(btnType) {
                    if (btnType == msgBox.ButtonType.OK) {
                        $.post(path + '/'+userTypePath+'/patient/releaseBinding', { ids : id }, function(message) {
                            msgBox.tips(message['content'], null, null);
                            if (message['type'] == 'success') {
                                query();
                            }
                        });
                    }
                }
            });
        });
    }

   // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.huanzheDiv');
            var id = $div.attr('id');
            val.push(id);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }

    // 绑定邀请
    function binding() {
        var url = path+'/'+userTypePath+'/patient/invite';
        location.href = url;
    }

    // 新增编辑患者
    function addEdit(fag) {
        var url = path+'/'+userTypePath+'/patient/addEdit';
        if(fag=='2')//编辑公告数据
        {
            var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips(editSelect,null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips(editOneSelect,null,null);
                return;
            }
            url+="/"+ids.vals;
        }
        else
        {
            url+="/-1";
        }

        location.href = url+'?iType='+iType;
    }
});