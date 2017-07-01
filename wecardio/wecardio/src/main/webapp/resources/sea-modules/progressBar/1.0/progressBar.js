/**
 * 进度条
 */
define('progressBar/1.0/progressBar', ['$'], function (require, exports, module) {
    /**
     * 进度条类
     */
    var progressBar = {
        /**
         * 当前进度
         */
        /* private */currentProgress: 0,

        /**
         * 显示进度条
         *
         * @param msg
         *            提示文本
         * @param progress
         *            进度值，0-100
         * @param callback
         *            显示进度后回调方法，例如：<br>
         *            function(msg, progress){...}
         *
         */
        show: function (msg, progress, callback) {
            if (progress < 0 || progress > 100) {
                return;
            }

            if ($(".progressBar").length == 0) {
                var tableStyle = "background:#fff; opacity:0.8;position:fixed;left:15%;top:50%;margin-top:-40px;z-index:99999;width:70%;height:80px;border-radius:5px;";
                var tdStyle = 'width:100%;text-align:center;font-size:14px;color:#353535;font-family:"微软雅黑";height:34px;';
                var divStyle = "display:block;width:86%;height:4px;background:#bebebe;border-radius:4px;margin:auto 7%;";
                var spanStyle = "float:left;width:0%;height:2px;border:1px solid #bebebe;background:#18b4f2;border-radius:2px;";
                var fontStyle = "font-size:14px;color:#18b4f2;margin-left:5px;";

                var html = '<table class="progressBar" style="' + tableStyle + '">'
                    + '<tr><td style="' + tdStyle + '"><font class="msg">'
                    + msg + '</font><font class="progress" style="' + fontStyle
                    + '">0%</font></td></tr>' + '<tr><td style="' + tdStyle
                    + '"><div style="' + divStyle + '"><span style="'
                    + spanStyle + '"></span></div></td></tr>' + '</table>';
                $("body").append(html);
            }

            if (progress < this.currentProgress) {
                $(".progressBar font.progress").text("0%");
                $(".progressBar span").css("width", "0%");
            }
            this.currentProgress = progress;

            $(".progressBar font.msg").text(msg);
            $(".progressBar").show();
            $(".progressBar font.progress").text(progress + "%");
            $(".progressBar span").css("width", progress + "%");

            //		$(".progressBar span").animate({
            //			width : progress + "%"
            //		}, 500, function() {
            callback && callback(msg, progress);
            //		});
        },

        /**
         * 隐藏进度条
         */
        hide: function () {
            $(".progressBar").hide();
        }

    };

    module.exports = progressBar;
});