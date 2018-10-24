/**
 * Created by tantian on 2015/7/14.
 */
seajs.use(['msgBox', 'pageBar', 'jTemplates', 'util', 'template'], function (msgBox, pageBar, jTemplates, util, template) {
    var chartCache = {};
    var chartCacheLableData = {};

    $(document).ready(function () {

        setpageSize();

        /*评论收起放下*/
        $("a.xw_pingjia").click(function () {
            $(this).parents("div.xw_talkBarDivMain").find("div.xw_pingjiaDiv").slideToggle();
            setParenHei();
        });

        $(window).bind("resize", resizebind);

        // 医嘱分页栏
        $('#advicePageBar').pageBar({
            pageNumber: $('#advicePageNo').val(),
            pageSize: $('#pageSize').val(),
            onSelectPage: function (page) {
                queryAdvice(page);
            }
        });

        // 活动记录分页栏
        $('#recordPageBar').pageBar({
            pageNumber: $('#recordPageNo').val(),
            pageSize: $('#pageSize').val(),
            onSelectPage: function (page) {
                queryRecord(page);
            }
        });

        // 检查记录分页栏
        $('#checkRecordPageBar').pageBar({
            pageNumber: $('#checkRecordPageNo').val(),
            pageSize: $('#pageSize').val(),
            onSelectPage: function (page) {
                queryCheckRecord(page);
            }
        });

        // 医嘱分页栏
        $('#consultationPageBar').pageBar({
            pageNumber: $('#consultationPageNo').val(),
            pageSize: $('#pageSize').val(),
            onSelectPage: function (page) {
                queryConsultation(page);
            }
        });

        // 查询医嘱
        queryAdvice();
        // 查询活动记录
        queryRecord();
        // 查询检查记录
        queryCheckRecord();
        // 查询医嘱
        queryConsultation();

        template.helper('dateFormat', function (date) {
            return util.DateUtil.format(new Date(date * 1000), 'yy/MM/dd hh:mm');
        });

        //时间切换
        $('.list_time li').click(function () {
            var $this = $(this);
            $this.addClass('up').siblings().removeClass("up");
        });

        //查询按钮
        $('.btnQuery').click(function () {
            $('#selectTime').val(true);

            var $this = $('.timeType .up');
            var $chartsContainer = $this.closest('div.chartsContainer');
            var idx = $chartsContainer.index() - 1;
            var $tab = $('ul.tab li').eq(idx);
            //$this.toggleClass('ico_chart');
            //ico_list
            if(!$chartsContainer.find('a.ico_list').hasClass('ico_chart')) {
                initCharts($chartsContainer, $tab, idx);
            }else {
                initList($chartsContainer, $tab, idx);
            }
        });

        //数据统计菜单切换
        $("ul.xw_objBoxHeadTab li").click(function () {
            $('#selectTime').val(false);

            var $this = $(this);

            $this.addClass('lion').siblings().removeClass("lion");

            var idx = $this.index();
            $("div.xw_userDetailA").slideUp().eq(idx).slideDown();

            //加载标识
            if ($this.data('loaded')) {
                return;
            }

            var $chartsContainer = $('div.chartsContainer').eq(idx);

            initCharts($chartsContainer, $this, idx);

            $this.data('loaded', true);
        }).get(0).click();

        //图表与列表切换
        $('div.chartsContainer .ico_list').click(function () {
            var $this = $(this);
            var $chartsContainer = $this.closest('.chartsContainer');
            var idx = $chartsContainer.index() - 1;
            var $tab = $('ul.tab li').eq(idx);
            $this.toggleClass('ico_chart');

            if ($this.hasClass('ico_chart')) {
                $chartsContainer.find('.charts').hide().end().find('.list').show();
                initList($chartsContainer, $tab, idx);
            } else {
                $chartsContainer.find('.list').hide().end().find('.charts').show();
                initCharts($chartsContainer, $tab, idx);
            }
        });

        function initList($chartsContainer, $tab, idx) {
            getChartData($chartsContainer, $tab, idx, function (data) {
                var html = template('templateList' + idx, data);
                $chartsContainer.find('.list tbody').empty().append(html);
            });
        }

        var chartTypeCfgByIdx = [
            //心率失常
            [
                {colName: ['hr'], serName: ['HR']},
                {colName: ['qt', 'pr'], serName: ['QT', 'PR']},
                {colName: ['qrs', 'qtc'], serName: ['QRS', 'QTC']}
            ],
            //心肌缺血
            [
                {colName: ['I'], serName: ['I']}, {colName: ['II'], serName: ['II']},
                {colName: ['V1'], serName: ['V1']}, {colName: ['V2'], serName: ['V2']},
                {colName: ['V4'], serName: ['V4']}, {colName: ['V6'], serName: ['V6']}
            ],
            //心脏负荷
            [
                {colName: ['hr'], serName: ['HR']}, {colName: ['sdnn', 'rmssd'], serName: ['SDNN', 'RMSSD']},
                {colName: ['psi'], serName: ['PSI']}, {colName: ['vlf', 'lf', 'hf'], serName: ['VLF', 'LF', 'HF']}
            ]
        ];

        function initCharts($chartsContainer, $tab, idx) {
            getChartData($chartsContainer, $tab, idx, function (data) {
                createLineChart(data, chartTypeCfgByIdx[idx], idx);
            });
        }

        /**
         * 创建图表
         * @param serverData
         * @param chartType
         * @param idx
         */
        function createLineChart(serverData, chartType, idx) {

            var charts = {};

            for (var ai = 0; ai < chartType.length; ai++) {
                var chartCfg = chartType[ai];
                var seriesData = [];

                for (var aii = 0; aii < chartCfg.colName.length; aii++) {
                    var series = {
                        name: chartCfg.serName[aii],
                        type: 'line',
                        showAllSymbol: true,
                        smooth: false,
                        data: (function () {
                            var d = [];
                            var list = serverData.data.l;

                            for (var i = 0; i < list.length; i++) {
                                if(list[i]['activity']) {
                                    continue;
                                }

                                var val = '';
                                if (idx == 1) { //心肌缺血
                                    var st = list[i]['st'] || {};
                                    val = st[chartType[ai].colName];
                                } else {
                                    val = list[i][chartCfg.colName[aii]];
                                }

                                val = val || '';

                                d.push([list[i].t * 1000, val, list[i].id || '']);
                            }

                            return d;
                        })()
                    };

                    if (idx == 1) {
                        series.itemStyle = {normal: {areaStyle: {type: 'default'}}};
                    }

                    seriesData.push(series);
                }

                var tempYear = 0;
                var tempMonth = 0;
                var chartId = 'chart' + idx + ai;
                chartCacheLableData[chartId] = {};
                var option = {
                    chartId : chartId,
                    title: {
                        text: chartType[ai].serName.join(',').replace('/,/g', ' & '),
                        textStyle: {fontSize: 10}
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: function (params) {
                            var t = new Date(params.value[0]);
                            var fmt = t.getFullYear() + '-' + (t.getMonth() + 1) + '-' + t.getDate() + ' ' + t.getHours() + ':' + t.getMinutes() + ':' + t.getSeconds();
                            return params.seriesName + '<br/>' +
                                fmt + '：' + params.value[1];
                        }
                    },
                    legend: {data: chartCfg.serName},
                    xAxis: [{
                        type: 'time',
                        position: 'top',
                        min: serverData.dateTime[0],
                        max: serverData.dateTime[serverData.dateTime.length - 1],
                        splitNumber: serverData.dateTime.length - 1,
                        data:serverData.dateTime,
                        axisLabel : {
                            show:true,
                            interval: 'auto',    // {number}
                            //rotate: 45,
                            //margin: 8,
                            formatter: function (data) {
                                var data1 = null;

                                if(data instanceof Date) {
                                    data1 = data;
                                }else {
                                    data1 = new Date(data);
                                }

                                var tempChart = chartCache['#chart' + idx + ai];

                                var id = tempChart.getOption().chartId;
                                var lblData = chartCacheLableData[id];

                                if(lblData[data1]) {
                                    return lblData[data1];
                                }

                                var lbl = '';
                                var month = (data1.getMonth() + 1) + '';
                                month = month.length < 2 ? '0' + month : month;

                                var year = data1.getFullYear();

                                if(serverData.timeType == 1) {
                                    if(tempMonth == 0 || tempMonth != month) {
                                        tempMonth = month;
                                        lbl = year + '-' + month;
                                    }

                                    var day = data1.getDate() + '';
                                    day = day.length < 2 ? '0' + day : day;

                                    lbl = (lbl + '\n' + day);
                                } else {
                                    if(tempYear == 0 || tempYear != year) {
                                        tempYear = year;
                                        lbl = tempYear + "\n";
                                    }

                                    lbl += month;
                                }

                                chartCacheLableData[id][data1] = lbl;

                                return lbl;
                            }
                        }
                    }],
                    yAxis: [{type: 'value'}],
                    loadingOption:{
                        text: messDataLoading,
                        textStyle: {
                            color: '#999',
                            fontFamily: '微软雅黑',
                            fontSize: 24,
                            fontWeight: 'bold'
                        },
                        //'spin' | 'bar' | 'ring' | 'whirling' | 'dynamicLine' | 'bubble'
                        effect: 'bar'
                    },
                    calculable:false,
                    noDataLoadingOption: {
                        text: noData,
                        textStyle: {
                            color: '#999',
                            fontFamily: '微软雅黑',
                            fontSize: 24,
                            fontWeight: 'bold'
                        },
                        //'spin' | 'bar' | 'ring' | 'whirling' | 'dynamicLine' | 'bubble'
                        effect: 'whirling'
                    },
                    series: seriesData
                };

                if (ai == 0) {
                    option.toolbox = {
                        show: true,
                        feature: {
                            //dataView: {show: true, readOnly: false},
                            //restore: {show: true},
                            saveAsImage: {show: true, title: ''}
                        }
                    };

                    option.grid = {y: 70, y2: 20};
                } else {
                    option.grid = {y2: 20};
                    option.title.y = 0;
                }

                if (ai == chartType.length - 1) {
                    //option.dataZoom = {
                    //    show: true,
                    //    start: 0,
                    //    y: 222
                    //};
                    //option.grid = {y2: 60};
                }

                var myChart = echarts.init($('#chart' + idx + ai).get(0), 'macarons');
                chartCache['#chart' + idx + ai] = myChart;
                myChart.setOption(option);

                myChart.on("click", function (params) {
                    try {
                        var id = params.data[params.data.length - 1];
                        if(!id) {
                            return;
                        }
                        var url = path + '/' + userPath + '/record/' + id + '/view';
                        window.open(url);
                    } catch (e) {

                    }
                });

                charts[chartType[ai].serName.join(',')] = myChart;


            }

            //设置关联
            $.each(charts, function (key, chart) {
                var chartRef = [];

                $.each(charts, function (key1, chart1) {
                    if (key1 != key) {
                        chartRef.push(chart1);
                    }
                });

                chart.connect(chartRef);
            });
        }

        //时间类型时间切换
        $('.timeType li').click(function () {
            $('#selectTime').val(false);
            var $this = $(this);
            var $chartsContainer = $this.closest('div.chartsContainer');
            var idx = $chartsContainer.index() - 1;
            var $tab = $('ul.tab li').eq(idx);
            //$this.toggleClass('ico_chart');
            //ico_list
            if(!$chartsContainer.find('a.ico_list').hasClass('ico_chart')) {
                initCharts($chartsContainer, $tab, idx);
            }else {
                initList($chartsContainer, $tab, idx);
            }
        });

        /**
         * 获取图表数据
         * @param $chartsContainer 图表容器对象
         * @param $tab tab容器
         * @param idx 当前序号
         */
        function getChartData($chartsContainer, $tab, idx, callBack) {
            var type = $tab.attr('data-type-value');
            var timeType = $chartsContainer.find('.timeType li.up').attr('data-value');
            var selectTime = $('#selectTime').val();
            var startTime = $('#startTime' + idx).val();
            var endTime = $('#endTime' + idx).val();

            var params = {
                patientId: iId,
                //patientId: 10322,
                type: type,
                timeType: timeType,
                selectTime:selectTime,
                startTime:startTime,
                endTime:endTime
            };

            $.post(path + '/' + userTypePath + '/patient/getData', params, function (data) {
                callBack && callBack(data);
            });

        }

        //图标中的列表数据点击事件
        $('body').on('click','.chartDtList', function () {
            var id = $(this).attr('data-value-id');
            if(!id) {
                return;
            }
            var url = path + '/' + userPath + '/record/' + id + '/view';
            //window.location.href = url;
            window.open(url);
        })

        $('#addAdviceBtn').on('click', openDialog_addAdvice);
        $('#actRecordBtn').on('click', openDialog_actRecord);
    });

    /*--------浏览器窗口重置事----------*/
    function resizebind() {
        setpageSize();

    }

    /*--------浏览器窗口重置事-END---------*/


    /**
     * 页面自适应大小
     */
    function setpageSize() {

        var winWid = $(window).width();
        var winHei = $(window).height();
        setParenHei();

    }


    /*更改父窗口高度*/
    function setParenHei() {
        setTimeout(function () {
            var theHei = document.body.scrollHeight;
            try {
                parent.window.setIframeHeight(theHei);
            } catch (e) {
//            alert("不支持静态页面！~");
            }
        }, 500);
    }

    /**
     * 分页查询
     * @param page
     */
    function queryAdvice(page) {
        if (page) {
            $('#advicePageNo').val(page);
        }

        $.post(path + '/' + userTypePath + '/patient/queryAdvice', $('#qform').serializeArray().concat($("#qAdviceform").serializeArray()), function (data) {
            $("#advicelist").empty();
            var total = data['total'];
            $('#adviceTotalNum').text(data['total']);
            if (total <= 0) {
                $("#advicelist").html('<h1 class="nodata">'+noData+'</h1>');
            }
            else {
                initAdviceListDiv(data.content);
            }

            $('#advicePageBar').pageBar({
                pageNumber: page || 1,
                pageSize: $('#pageSize').val(),
                total: data.total,
                onSelectPage: function (pageNumber, pageSize) {
                    queryAdvice(pageNumber, pageSize);
                }
            });
        });
    }

    var tempId = 0;

    /**
     * 使用模板初始化
     * @param jsonData
     */
    function initAdviceListDiv(jsonData) {
        $.each(jsonData, function (i, data) {
            $("#advicelist").append('<div id="_hiteam_Advices_list_' + tempId
                + '" billId="' + data['id']
                + '"></div>');
            var $template = $("#_hiteam_Advices_list_" + tempId);
            $template.setTemplateElement("adviceTemplate");
            $template.processTemplate(data);

            tempId++;
        });
    }

    /**
     * 分页查询
     * @param page
     */
    function queryRecord(page) {
        if (page) {
            $('#recordPageNo').val(page);
        }

        $.post(path + '/' + userTypePath + '/patient/queryRecord', $('#qform').serializeArray().concat($("#qRecordform").serializeArray()), function (data) {
            $("#recordlist").empty();
            $('#adviceRecordTotalNum').text(data['total']);

            var total = data['total'];
            if (total <= 0) {
                $("#recordlist").html('<h1 class="nodata">'+noData+'</h1>');
            }
            else {
                initRecordListDiv(data.content);
            }

            $('#recordPageBar').pageBar({
                pageNumber: page || 1,
                pageSize: $('#pageSize').val(),
                total: data.total,
                onSelectPage: function (pageNumber, pageSize) {
                    queryRecord(pageNumber, pageSize);
                }
            });
        });
    }

    var tempId = 0;

    /**
     * 使用模板初始化
     * @param jsonData
     */
    function initRecordListDiv(jsonData) {
        $.each(jsonData, function (i, data) {
            $("#recordlist").append('<div id="_hiteam_Record_list_' + tempId
                + '" billId="' + data['id']
                + '"></div>');
            var $template = $("#_hiteam_Record_list_" + tempId);
            $template.setTemplateElement("recordTemplate");
            $template.processTemplate(data);

            tempId++;
        });
    }

    /**
     * 分页查询
     * @param page
     */
    function queryCheckRecord(page) {
        if (page) {
            $('#checkRecordPageNo').val(page);
        }

        $.post(path + '/' + userType + '/record/pageByPatient', $('#qform').serializeArray().concat($("#qCheckRecordform").serializeArray()), function (data) {
            $("#checkRecordlist").empty();
            $('#checkRecordTotalNum').text(data['total']);
            var total = data['total'];
            if (total <= 0) {
                $("#checkRecordlist").html('<h1 class="nodata">'+noData+'</h1>');
            }
            else {
                initCheckRecordListDiv(data.content);
            }

            $('#checkRecordPageBar').pageBar({
                pageNumber: page || 1,
                pageSize: $('#pageSize').val(),
                total: data.total,
                onSelectPage: function (pageNumber, pageSize) {
                    queryCheckRecord(pageNumber, pageSize);
                }
            });
        });
    }

    var tempId = 0;

    /**
     * 使用模板初始化
     * @param jsonData
     */
    function initCheckRecordListDiv(jsonData) {
        $.each(jsonData, function (i, data) {
            $("#checkRecordlist").append('<div id="_hiteam_CheckRecord_list_' + tempId
                + '" billId="' + data['id']
                + '"></div>');
            var $template = $("#_hiteam_CheckRecord_list_" + tempId);
            $template.setTemplateElement("checkRecordTemplate");
            $template.processTemplate(data);
            listEvents();
            tempId++;
        });
    }

    /**
     * 分页查询
     * @param page
     */
    function queryConsultation(page) {
        if (page) {
            $('#consultationPageNo').val(page);
        }

        $.post(path + '/' + userType + '/consultation/page', $('#qform').serializeArray().concat($("#qConsultationform").serializeArray()), function (data) {
            $("#consultationlist").empty();
            $('#consultationTotalNum').text(data['total']);
            var total = data['total'];
            if (total <= 0) {
                $("#consultationlist").html('<h1 class="nodata">'+noData+'</h1>');
            }
            else {
                initConsultationListDiv(data.content);
            }

            $('#consultationPageBar').pageBar({
                pageNumber: page || 1,
                pageSize: $('#pageSize').val(),
                total: data.total,
                onSelectPage: function (pageNumber, pageSize) {
                    queryConsultation(pageNumber, pageSize);
                }
            });
        });
    }

    var tempId = 0;

    /**
     * 使用模板初始化
     * @param jsonData
     */
    function initConsultationListDiv(jsonData) {
        $.each(jsonData, function (i, data) {
            $("#consultationlist").append('<div id="_hiteam_Consultation_list_' + tempId
                + '" billId="' + data['id']
                + '"></div>');
            var $template = $("#_hiteam_Consultation_list_" + tempId);
            $template.setTemplateElement("consultationTemplate");
            $template.processTemplate(data);
            listEvents();
            tempId++;
        });
    }

    /**
     * 列表事件
     */
    function listEvents() {
        $(".xw_sgListLiMainCenter_Record").click(function () {
            var $container = $(this).closest('div.xw_sgListLiMainCenter_Record');
            var id = $container.attr('id');
            window.location.href = path + '/' + userType + '/record/' + id + '/view';
        });

        $(".xw_sgListLiMainCenter_Consultation").click(function () {
            var $container = $(this).closest('div.xw_sgListLiMainCenter_Consultation');
            var id = $container.attr('id');
            window.location.href = path + '/' + userType + '/consultation/' + id + '/view';
        });

    }

    //新增医嘱
    function openDialog_addAdvice() {
        msgBox.exWindow.open({
            title : addConsultation,
            width : '680px',
            height : '250px',
            url : path + '/'+userTypePath+'/patient/addAdvice?iId=' + iId,
            close : function(result)
            {
                if(result==1)
                {
                    // 查询医嘱
                    queryAdvice();
                }
            }
        });
    }

    //活动记录
    function openDialog_actRecord() {
        msgBox.exWindow.open({
            title : recordTitle,
            width : '680px',
            height : '380px',
            url : path + '/'+userTypePath+'/patient/actRecord?iId=' + iId,
            close : function(result)
            {
                if(result==1)
                {
                    // 查询活动记录
                    queryRecord();
                }
            }
        });
    }
});

