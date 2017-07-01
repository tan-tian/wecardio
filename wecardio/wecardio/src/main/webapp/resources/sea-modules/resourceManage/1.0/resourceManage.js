/**
 * 资源管理
 */
define('resourceManage/1.0/resourceManage', ['$', 'layer/1.8.5/layer'], function (require, exports, module) {
	var layer = require('layer/1.8.5/layer');
    /**
     * 资源管理类
     */
    var resourceManage = {
		open : function (option, path, callbackFn) {
			var $url = path + '/admin/resourceManage/toResourceManage/type=' + (option.type || 0);
			var $width = function (o) {
				if (o && o.width) {
					return o.width;
				}
				return document.body.offsetWidth * 0.8;
			}(option);
			var $height = function (o) {
				if (o && o.height) {
					return o.height;
				}
				return top.document.body.offsetHeight * 0.8;
			}(option);
		    $.layer({
		        type: 2,
		        title: [
		            '资源管理',
		            'background:#2B2E37; height:40px; color:#fff; border:none;' //自定义标题样式
		        ],
		        border:[10],
		        area: [$width, $height],
		        iframe: {src: $url},
		        btns: 2,
		        btn: ['确定', '取消'],
		        yes: function(index){
		        	if (callbackFn) {
		        		var jsonData = JSON.parse(layer.getChildFrame('#result', index).val());
						var resultData = jsonData instanceof Array ? jsonData : [jsonData];
						if (resultData.length < 1) {
							layer.msg('未选择任何文件', 1);
							return false;
						}
		        		callbackFn(resultData);
		        	}
		        	layer.close(index);
		        }
		    });
		}
    };

    module.exports = resourceManage;
});