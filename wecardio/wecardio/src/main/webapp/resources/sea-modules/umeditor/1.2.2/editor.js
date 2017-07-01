define('umeditor/1.2.2/editor',
	['$',
		'umeditor/1.2.2/umeditor.config',
		'umeditor/1.2.2/umeditor.min',
		'umeditor/1.2.2/lang/zh-cn/zh-cn',
		'umeditor/1.2.2/themes/default/css/umeditor.css'
	], function (require, exports, module) {
		//注意修改ueditor.config.js中的配置

		//https://github.com/fex-team/umeditor
		module.exports = UM;
	});