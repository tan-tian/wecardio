seajs.use(['$', 'msgBox'], function ($, msgBox) {

    $(function () {
        initData();
        initEvent();
    });

    function initData() {
        window.top.servicePackMgr = null;
    }

    function initEvent() {

        $('#btnNew').click(function () {
            window.location.href = path + '/' + userPath + '/service/toPage/package1';
        });

        $('#btnPckMgr').click(function () {
            window.location.href = path + '/' + userPath + '/service/toPage/packageMgr';
        });
    }
});