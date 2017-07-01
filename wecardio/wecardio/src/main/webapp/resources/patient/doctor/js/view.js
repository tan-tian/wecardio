/**
 * Created by Sebarswee on 2015/7/31.
 */
seajs.use([], function () {
    $(document).ready(function() {
        // 图片事件
        imageEvents();
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
});