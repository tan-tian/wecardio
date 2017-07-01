/**
 * Created by Sebarswee on 2015/8/17.
 */
seajs.use(['msgBox', 'jTemplates'], function (msgBox) {
    $(document).ready(function() {
        loadList();
        // 加载流程图
        loadFlowInfo();
        // 详情里流程列表的隐藏展示事件
        $("a.xw_sliderFlowList").off('click').on('click', function() {
            $(this).toggleClass("sliderFlowListUp");
            $(this).parents("div.xw_flowContent").find("div.xw_flowContentBodyDiv").slideToggle('fast');
            if ($(this).attr('loaded') == 'false') {
                // 加载处理信息
                loadBillProcessInfo();
                $(this).attr('loaded', 'true')
            }
        });

        // 提交按钮
        $('#submitBtn').on('click', function () {
            var url = path + '/org/withdraw/recreate';
            $.post(url, {id : $('#order').val()}, function (message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    window.location.href = path + "/org/withdraw/todo";
                }
            });
        });

        // 新增按钮
        $('#addBtn').on('click', function () {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '600px',
                height : '500px',
                url : path + '/org/withdraw/' + $('#order').val() + '/settlement/select',
                close : function(flag) {
                    if (flag) {
                        loadList();
                    }
                }
            });
        });

        // 删除按钮
        $('#delBtn').on('click', function () {
            var $chk = $("[name=checkbox]:checkbox");
            if ($chk.filter(":checked").length === 0) {
                msgBox.warn(requiredMsg, null, null);
                return false;
            }
            var data = [];
            $chk.filter(":checked").each(function(i) {
                var $this = $(this);
                var year = $this.siblings('[name=year]:input').val();
                var month = $this.siblings('[name=month]:input').val();
                data.push({
                    year : year,
                    month : month
                });
            });
            var url = path + '/org/withdraw/remove';
            $.post(url, {id : $('#order').val(), data : JSON.stringify(data)}, function (message) {
                msgBox.tips(message['content'], null, null);
                if (message['type'] == 'success') {
                    loadList();
                }
            });
        });
    });

    // 加载月结信息
    function loadList() {
        var url = path + '/org/withdraw/settlement/list';
        $.post(url, {id : $('#order').val()}, function (data) {
            var $list = $('div.userDetail');
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // checkbox事件
            $('#checkAll').off('click').on('click', function () {
                $('[name=checkbox]:checkbox').prop('checked', this.checked);
            });
            $("[name=checkbox]:checkbox").on("click", function () {
                var $chk = $("[name=checkbox]:checkbox");
                $("#checkAll").prop("checked", $chk.length == $chk.filter(":checked").length);
            });
        });
    }

    // 流程图
    var FLOW_LI_STYLE = ['', 'left: 143px;', 'left: 308px;', 'left: 473px;', 'left: 638px;'];
    function loadFlowInfo() {
        $.ajax({
            url: path + '/wf/getFlowInfo',
            type: "POST",
            data: {
                wfCode : $('#wfCode').val(),
                guid : $('#guid').val()
            },
            dataType: "json",
            cache: false,
            success: function(data) {
                var html = '';
                for (var i = 0, l = data.length; i < l; i++) {
                    html += '<li class="' + data[i]['cls'] + '" style="' + FLOW_LI_STYLE[i] + '">';
                    html += '<span>' + data[i]['no'] + '</span>';
                    html += '<a>' + data[i]['name'] + '</a>';
                    html += '</li>';
                }
                $('ul.flowContentHead').append(html);
            }
        });
    }

    // 处理信息
    function loadBillProcessInfo() {
        $.ajax({
            url: path + '/wf/billProcessInfo',
            type: "POST",
            data: {
                guid : $('#guid').val()
            },
            dataType: "json",
            cache: false,
            success: function(data) {
                var tbl = $('table.flowContentBodyTable');
                var html = '<tbody>';
                for (var i = 0, l = data.length; i < l; i++) {
                    html += '<tr>';
                    html += '<td>' + data[i]['exeDate'] + '</td>';
                    html += '<td>' + data[i]['actName'] + '</td>';
                    html += '<td>' + data[i]['actorName'] + '</td>';
                    html += '<td>' + data[i]['result'] + '</td>';
                    html += '<td>' + data[i]['remark'] + '</td>';
                    html += '</tr>';
                }
                html += '</tbody>';
                tbl.append(html);
            }
        });
    }
});