/**
 * Created by Sebarswee on 2015/7/13.
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
            $('#type').mulSelect('reset');
            $('#org').select('reset');
        });

        $('a.giveupBt').on('click', function () {
            $('#qform')[0].reset();
            $('#type').mulSelect('reset');
            $('#org').select('reset');
        });

        $('#addBtn').on('click', function () {
            addEdit(1);
        });
        $('#editBtn').on('click', function () {
            addEdit(2);
        });
        $('#deleteBtn').on('click', deletes);

        // 查询
        query();
    });

    // 初始化搜索表单
    function initSearchForm() {
        $('#type').mulSelect({
            url : path + '/guest/enum/message/type'
        });

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

        $.post(path + '/'+userTypePath+'/message/page', $('#qform').serialize(), function(data) {
            var total = data.length > 0 ? data[0].total : 0;
            $("#list").empty();
            $('#totalNum').text(total);
            initListDiv(data);
            $('#pageBar').pageBar({
                pageNumber : page || 1,
                pageSize : $('#pageSize').val(),
                total : total,
                onSelectPage : function(pageNumber, pageSize) {
                    query(pageNumber);
                }
            });

            if(total == 0) {
                $('#list').append('<div class="jiachajilu">'+
                    '<h1 class="nodata">'+notData+'</h1>'+
                    '</div>');
            }

        });
    }

    /**
     * 列表事件
     */
    function listEvents(id) {
        // 勾选事件
        $("a.xw_checkbox").off('click').on('click', function() {
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
        $("#refuseBtn"+id).on('click', function() {
            var id = $(this).attr('val');
            operMessage(id,1);
        });

        $("#acceptBtn"+id).on('click', function() {
            var id = $(this).attr('val');
            operMessage(id,2);
        });
    }

    // 删除患者
    function deletes() {
        var items = getCheckedItems();
        if (items.count == 0) {
            top.msgBox.tips('请选择要删除的记录！', null, null);
            return;
        }
        confirmDelete(function() {
            $.post(path + '/'+userTypePath+'/message/delete', { ids : items.vals }, function(message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    query(1);
                }
            });
        });
    }

    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.xw_checkbox').filter('.checked').each(function() {
            var id = $(this).attr('id');
            val.push(id);
        });

        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }

    // 新增编辑患者
    function addEdit(fag) {
        var url = path+'/'+userTypePath+'/message/addEdit';
        if(fag=='2')//编辑公告数据
        {
            var ids = [];
            ids = getCheckedItems();
            if(ids.count == 0){
                top.msgBox.tips('请选择要编辑的数据！',null,null);
                return;
            }

            if(ids.count > 1){
                top.msgBox.tips('请选择一条数据进行编辑！',null,null);
                return;
            }
            url+="/"+ids.vals;
        }
        else
        {
            url+="/-1";
        }
        msgBox.exWindow.open({
            title : '&nbsp;',
            width : '680px',
            height : '500px',
            url : url,
            close : function(result)
            {
                if(result==1)
                {
                    query();
                }
            }
        });
    }

    var tempId = 0;
    /**
     * 使用模板初始化
     * @param jsonData
     */
    function initListDiv(jsonData) {
        $.each(jsonData, function(i, data) {
            $("#list").append('<div id="_hiteam_Message_list_' + tempId
                + '" billId="' + data['id']
                + '"></div>');
            var $template = $("#_hiteam_Message_list_" + tempId);
            $template.setTemplateElement("template");
            $template.processTemplate(data);
            listEvents(data['id']);
            tempId++;
        });
    }

    function operMessage(iId,iType) {
        var msg = "";
        if(iType=='1')//拒绝
        {
            msg = refuseMsg;
        }
        if(iType=='2')//接受
        {
            msg = acceptMsg;
        }
        msgBox.confirm({
            msg : msg,
            callback : function(btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    $.ajax({
                        url: path+'/'+userTypePath+'/message/operMessage',
                        type: "POST",
                        data: {iId:iId,iType:iType},
                        dataType: "json",
                        success: function(message) {
                            top.msgBox.tips(message['content'], null, null);
                            if (message['type'] == "success") {
                                window.location.href = path + '/'+userTypePath+'/message';
                            }
                        }
                    });
                }
            }
        });
    }

    function confirmDelete(fn) {
        msgBox.confirm({
            msg : deleteMsg,
            callback : function(btnType) {
                if (btnType == msgBox.ButtonType.OK) {
                    fn();
                }
            }
        });
    }
});