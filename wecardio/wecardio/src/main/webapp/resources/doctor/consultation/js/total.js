/**
 * Created by tantian on 2015/7/27.
 */
seajs.use(['msgBox','pageBar', 'jTemplates', 'mulSelect', 'select'], function(msgBox) {
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

        // 查询
        query();
    });
    
    //编辑汇总报告
    $('#totalBtn').on('click', function() {
            var items = getCheckedItems();
            if (items.count === 0) {
            	document.getElementById('tishi').style.display = 'block';//显示
            	return;
            }
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '680px',
                height : '405px',
                url : path + '/doctor/consultation/'+$('#pId').val()+'/toedit?ids=' 
                	+ items.vals+'&startDate='+$('#startDate').val()+'&endDate='
                	+$('#endDate').val(),
                close : function(flag) {
                   if (flag !== null) {
                        window.location.href = path + '/doctor/consultation/total?pId='+$('#pId').val();
                   }
                }
            });
        });
        
        
    /**
     * 初始化搜索表单
     */
    function initSearchForm() {
        $("input.xw_searchListInput").on('input propertychange change', function() {
            var val = $(this).val();
            $(this).parents("div.xw_searchListDiv").find("ul.xw_searchListUl a").html(val);
        }).click(function() {
            $(this).parents("div.xw_searchListDiv").find("ul.xw_searchListUl").slideDown();
        });
        $("div.xw_searchListDiv").mouseleave(function() {
            $(this).find("ul.xw_searchListUl").slideUp();
        });

        $("div.xw_searchListDiv").find("ul.xw_searchListUl li").on('click', function() {
            var value = $(this).find('a').text();
            var property = $(this).find('a').attr('searchProperty');
            $('#searchValue').val(value);
            $('#searchProperty').val(property);
            query();
        });

        // 诊单状态
        $('#state').mulSelect({
            url : path + '/guest/enum/consultation/state'
        });
        // 服务类型
        $('#serviceType').select({
            width: 175,
            url : path + '/doctor/service/queryServiceName',
            valueField: 'id',
            textField: 'name'
        });
        // 更多查询条件
        $("a.moresearchForm").on('click', function() {
            var $this = $(this);
            $this.toggleClass("up");
            $this.parents("div.objBoxCont").find("div.xw_kMainContentbody").slideToggle('fast');
        });
        // 按钮事件
        $('a.queryBtn').on('click', function() {
            query();
        });
        $('a.resetBtn').on('click', function () {
            $('#qform')[0].reset();
            $('#state').mulSelect('reset');
            $('#serviceType').select('reset');
        });
    }
	//显示pdf汇总报告
	/* $.post(path + '/doctor/consultation/pdf', { id : $('#cid').val() }, function(data) {
            var pdf = data + '#view=Fit';
            var $container = $('#pdfContent');
            $container.append('<object width="100%" height="600" data="' + pdf + '" type="application/pdf">'
                + '<param name="src" value="' + pdf + '">'
                + '</object>');
        });*/
    /**
     * 分页查询
     * @param page
     */
    function query(page) {
        var pageNo = page || 1;
        $('#pageNo').val(pageNo);

        var $qform = $('#qform');
        $.post($qform.attr('action'), $qform.serialize(), function(data) {
            // 设置总数
            $('#totalNum').text(data['total']);
            // 用模板初始化列表
            var $list = $('div.muluList');
            $list.empty();
            $list.setTemplateElement('template');
            $list.processTemplate(data);
            // 列表事件
            listEvents();
            // 重设分页栏
            $('#pageBar').pageBar({
                pageNumber : pageNo,
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
        });
        // 跳转事件
        $(".xw_sgListLiMainCenter").on('click', function() {
            var $container = $(this).closest('div.sgListLiMain');
            var cid = $container.attr('cid');
            window.location.href = path + '/doctor/consultation/' + cid + '/detail';
        });
    }
    
    // 获取勾选对象
    function getCheckedItems() {
        var val = [];
        $('div.muluList').find('a.checkbox').filter('.checked').each(function() {
            var $div = $(this).closest('div.sgListLiMain');
            var id = $div.attr('cid');
            val.push(id);
        });



        return {
            vals : val.join(','),
            arr : val,
            count : val.length
        };
    }
});