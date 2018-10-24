/**
 * Created by tantian on 2015/7/15.
 */
seajs.use(['msgBox'], function (msgBox) {
    $(document).ready(function() {
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
        // 图片事件
        imageEvents();
        // 审核按钮
        $('#auditBtn').on('click', function () {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '600px',
                height : '300px',
                url : path + '/admin/doctor/' + $('#did').val() + '/audit',
                close : function(flag) {
                    if (flag) {
                        window.location.href = path + '/admin/doctor/todo';
                    }
                }
            });
        });
    });

    // 图片事件
    function imageEvents() {
        $('ul.gongneng').find('img').on('click', function () {
            var $this = $(this);
            var source = $this.attr('source');
            if (source != null && source != "") {
                window.open(source);
            }
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