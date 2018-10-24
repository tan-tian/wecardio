/**
 *<pre>
 * Description: 微信工具方法
 * Author：tantian
 * Date：2014-11-24 16:54
 *</pre>
 */

define('weixin/2.0/wxUtil', function(require, exports, module) {
    var _this = (function(){
        /***
         * 过滤微信不支持的标签及内容
         */
        function filterTag(htmlVal){
            if(!htmlVal){
                return htmlVal;
            }
            //替换<p>、</p>、<br/>、a标签中的target属性
            return htmlVal.replace(/(((<|<\/)p>)|(<br\/>)|(target="(_blank|_self|_parent|_top|view_frame)"))/gi,'');
        }

        return {
            filterTag: filterTag
        };
    })();

    module.exports = _this;
});
