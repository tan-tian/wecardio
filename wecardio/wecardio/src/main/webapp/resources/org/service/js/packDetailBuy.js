seajs.use(['$', 'msgBox', 'util', 'template', 'pageBar'], function ($, msgBox, util, template, pageBar) {
    var modelPath = path + '/' + userPath;
    var id = null;
    var $pageBarConsumer = null;
    var $pageBarDiagnosis = null;
    var $tab = null;

    $(function () {
        initData();
        initEvent();
    });
	
	//购买
     $('#buyBtn').click(function () {
     	msgBox.exWindow.open({
                title : '&nbsp;',
                width : '680px',
                height : '450px',
                url : modelPath + '/service/daichongBuy?sid=' + $('#sid').val()
            				+'&pId='+$('#pId').val(),
                close : function(flag) {
                   if (flag !== null) {
                      window.location.href =modelPath + '/patient?iType=2';
                   }
                }
            });
         
     });
        
    function initData() {
        $tab = $("ul.xw_objBoxHeadTabUl li");

        $.get(modelPath + '/service/package/detail/' + $('#id').val(), {}, function (data) {
            $('#lbl_name').html(data.title);
            $('#lbl_price').html(data.price);
            $('#lbl_orgName').html(data.org.name);
            $('#lbl_created').html(util.DateUtil.format(new Date(data.created * 1000), 'yyyy-MM-dd'));
            $('#lbl_expired').html(data.expired);
            $('#lbl_content').html(data.content);

            id = data.id;

            $tab.get(0).click();
        });

        //初试化分页条
        $pageBarDiagnosis = $('#pageBarDiagnosis').pageBar({
            onSelectPage: function (page, pageSize) {
                queryDiagnosis();
            }
        });

        //初试化分页条
        $pageBarConsumer = $('#pageBarConsumer').pageBar({
            onSelectPage: function (page, pageSize) {
                queryConsumer();
            }
        });
    }

    function initEvent() {
        //Tab点击事件
        $tab.click(function () {
            var $this = $(this);
            var idx = $this.index();
            var selClass = 'lion';

            if (!$this.hasClass(selClass)) {
                $this.addClass(selClass).siblings().removeClass(selClass);
                $("div.xw_objBoxFirstBodyb").slideUp().eq(idx).slideDown();
            }

            //加载标识
            if ($this.data('loaded')) {
                return;
            }

            if (idx == 0) {
                querySerivce();
            } else if (idx == 1) {
                queryConsumer();
            }

            $this.data('loaded', true);
        });

        //编辑
        $('#btnEdit').click(function () {
            window.location.href = modelPath + '/service/toPage/package1?id=' + id;
        });

        //购买事件
        $('#btnBuy').click(function () {
            var id = $('#id').val();
            window.location.href = modelPath + '/service/toPage/buy?id=' + id;
        });

        //取消
        $('#btnCancel').click(function () {
            window.history.go(-1);
        });

    }

    //查询服务
    function querySerivce() {
        $.post(modelPath + '/service/package/service/' + id, {}, function (data) {
            var html = template('templateService', data);
            $('#lblCountService').html(data.total);
            $('#listService').html(html);
        });
    }

    //查询诊单
    function queryDiagnosis() {

    }

    //查询购买者
    function queryConsumer() {
        var params = [];
        var options = $pageBarConsumer.pageBar('options');

        params = params.concat([
            {name: 'pageNo', value: options.pageNumber || 1},
            {name: 'pageSize', value: 12},
            {name: 'packageId', value: $('#id').val()}
        ]);

        $.post(modelPath + '/service/package/queryConsumer', params, function (data) {
            var html = template('templateConsumer', data);
            $('#listConsumer').html(html)
                .find('img').bind('error',function () {
                    if($(this).data('isModify')){
                        return;
                    }
                $(this).data('isModify',true).attr('src',path + '/resources/images/user01.png');
            });

            $('#lblCount').html(data.total);

            //设置分页条信息
            $pageBarConsumer.pageBar({
                total: data.total,
                pageNumber: data.pageNo,
                pageSize: data.pageSize
            });
        });
    }

});