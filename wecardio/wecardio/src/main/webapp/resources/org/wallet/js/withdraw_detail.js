/**
 * 实收金额详情记录
 * Created by Sebarswee on 2016/3/14.
 */
seajs.use(['easyuiDatagrid', 'pageBar'], function () {
    $('#list').datagrid({
        singleSelect: true
    });
    $('div.pageBar').pageBar({
        pageNumber: 1,
        pageSize: 12,
        total: 0,
        onSelectPage: function (pageNo, pageSize) {
            query(pageNo, pageSize);
        }
    });

    function query(pageNo, pageSize) {
        $.post(path + '/org/wallet/withdraw/detail/page', {type: $('#type').val(), pageNo: pageNo, pageSize: pageSize}, function (datas) {
            $('#list').datagrid('loadData', datas.content);
            $('div.pageBar').pageBar({
                pageNumber: pageNo,
                pageSize: pageSize,
                total: datas.total
            });
        });
    }

    query(1, 12);
});