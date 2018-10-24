/**
 * <pre>
 * Description:easyui 的分页插件 jquery.pagination.js
 * Author：tantian
 * Date：2014-11-05 11:51
 * </pre>
 */
define('easyui/1.4.1/pagination', [ '$', 'easyui/1.4.1/plugins/jquery.parser',
		'easyui/1.4.1/plugins/jquery.linkbutton',
		'easyui/1.4.1/plugins/jquery.pagination',
		'easyui/1.4.1/locale/easyui-lang-zh_CN',
		'easyui/1.4.1/themes/{easyUItheme}/pagination.css' ], function(require,
		exports, module) {

	var defalutSettings = {
		defaultLayout : [ 'first', 'prev', 'links', 'next', 'last', 'sep' ]
	};

	module.exports = defalutSettings;
});