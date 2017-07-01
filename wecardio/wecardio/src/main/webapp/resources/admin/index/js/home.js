seajs.use(['$', 'template'], function ($, template) {
    var modePath = path + '/' + userPath;
    var $tab = null;

    $(function () {
        initData();
        initEvent();
    });

    /**
     * 初始化图表:机构图表
     */
    function initChartsOrg() {
        var legendMountNames = $('#mountName').val().split(',');

        //获取图表数据
        $.post(modePath + '/yearSum', null, function (data) {

            $.each(data, function (key, val) {
                $('#' + key).html(val);
            });

            var series = [];

            for (var i = 0; i < legendMountNames.length; i++) {
                series.push({
                    name: legendMountNames[i],
                    type: 'bar',
                    data: []
                });
            }

            $.each(data.datas, function (key, val) {
                series[0].data.push(val.receiveAmount);
                series[1].data.push(val.notReceivedAmount);
                series[2].data.push(val.alreadyReceivedAmount);
            });

            initChart(legendMountNames, series);

        });
    }

    /***
     * 初始化图表:平台图表
     */
    function initChartsAdmin() {

        var legendMountNames = $('#mountName').val().split(',');

        //获取图表数据
        $.post(modePath + '/yearSum', null, function (data) {

            $.each(data, function (key, val) {
                $('#' + key).html(val);
            });

            var series = [];

            for (var i = 0; i < legendMountNames.length; i++) {
                series.push({
                    name: legendMountNames[i],
                    type: 'bar',
                    data: []
                });
            }

            $.each(data.datas, function (key, val) {
                series[0].data.push(val.payAmount);
                series[1].data.push(val.notPayAmount);
                series[2].data.push(val.alreadyPayAmount);
            });

            initChart(legendMountNames, series);

        });
    }

    function initChart(legendMountNames, series) {
        var months = [];
        var monthName = $('#monthName').val();

        for (var i = 0; i <= 11; i++) {
            months.push((i + 1) + monthName);
        }

        var myChart = echarts.init($('#chart').get(0),'macarons');

        var option = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: legendMountNames
            },
            toolbox: {
                show: true,
                feature: {
                    //mark: {show: true},
                    //magicType: {show: true, type: ['line', 'bar']},
                    //restore: {show: true},
                    saveAsImage: {show: true,title:''}
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    data: months
                }
            ],
            yAxis: [{type: 'value'}],
            series: series
        };

        myChart.setOption(option);
    }

    function initData() {
        //获取统计数据
        $.post(modePath + '/sum', null, function (data) {
            $.each(data, function (key, val) {
                $('#' + key).html(val);
            })
        });

        if (userPath == 'admin') {
            $tab = $("ul.xw_objBoxHeadTabUl li");
            initChartsAdmin();
        } else {
            initChartsOrg();
            queryConsultation();
        }
    }

    /**
     * 初试化事件
     */
    function initEvent() {
        if (userPath == 'admin') {
            initEventAdmin();
        } else {
            initEventOrg();
        }
    }

    function initEventOrg() {
        // 跳转事件
        $('#listConsultation').on('click','.xw_sgListLiMainCenter', function () {
            var $container = $(this).closest('div.sgListLiMain');
            var cid = $container.attr('cid');
            window.location.href = modePath + '/consultation/' + cid + '/view';
        });
    }

    function initEventAdmin() {
        /*----Tab事件---*/
        $tab.click(function () {
            var $this = $(this);
            var idx = $this.index();
            var selClass = 'lion';

            if (!$this.hasClass(selClass)) {
                $this.addClass(selClass).siblings().removeClass(selClass);
                $("div.xw_indexObjBoxBody").slideUp().eq(idx).slideDown();
            }

            //加载标识
            if ($this.data('loaded')) {
                return;
            }

            if (userPath == 'admin') {
                tabQueryAdmin(idx);
            } else {
                tabQueryOrg(idx);
            }

            $this.data('loaded', true);
        });

        $tab.get(0).click();
        //机构审核列表
        $('#listOrg').on('click', '.sgListLiMainCenter', function () {
            var $container = $(this).closest('div.sgListLiMain');
            var oid = $container.attr('oid');
            window.location.href = modePath + '/organization/' + oid + '/view';
        });

        //医生审核列表
        $('#listDoctor').on('click', '.sgListLiMainCenter', function () {
            var $container = $(this).closest('div.sgListLiMain');
            var did = $container.attr('did');
            window.location.href = modePath + '/doctor/' + did + '/view';
        });
    }

    /**
     * 机构tab列表查询
     * @param idx
     */
    function tabQueryOrg(idx) {
        queryConsultation();
    }

    /**
     * 查询诊单列表
     */
    function queryConsultation() {
        //String serviceName, Date startDate, Date endDate, Integer[] state,
        //    String doctor, String org, Pageable pageable
        var params = {
            pageNo: 1,
            pageSize: 10,
            serviceName:'',
            startDate:'',
            endDate:'',
            state:'',
            doctor:'',
            org:''
        };

        $.post(modePath + '/consultation/page', params, function (data) {
            var html = template('templateConsultation', data);
            $('#listConsultation').html(html);
        });
    }

    /**
     * 平台管理员tab列表查询
     * @param idx
     */
    function tabQueryAdmin(idx) {
        if (idx == 0) {
            queryOrgAdmin();
        } else if (idx == 1) {
            queryDoctorAdmin();
        }
    }

    /**
     * 查询机构审核列表
     */
    function queryOrgAdmin() {
        $.post(modePath + '/organization/todoList', {pageNo: 1, pageSize: 10}, function (data) {
            var html = template('templateOrg', data);
            $('#listOrg').html(html);
        });
    }

    /**
     * 查询医生审核列表
     */
    function queryDoctorAdmin() {
        $.post(modePath + '/doctor/todoList', {pageNo: 1, pageSize: 10}, function (data) {
            var html = template('templateDoctor', data);
            $('#listDoctor').html(html);
        });

    }
});