/**
 * 模块别名及对应版本配置模板<br>
 * 注：请将该模板复制到项目中使用，不建议直接使用该模板
 */
seajs.config({
	/**
	 * 模块根路径<br>
	 * 默认为seajs目录的上级目录或seajs.js文件的所在目录<br>
	 * 如果要配置，不能以.或..开头
	 */
	base : 'agent/../../',
	/** 调试模式 */
	debug : true,
	/** 模块文件编码格式 */
	charset : 'UTF-8',
	/** 模块中用到的动态变量,目前暂只控制版本、样式、脚本根目录文件 */
	vars : {

		/**
		 * 样式，默认使用default文件夹下的样式 注：样式相关文件必须放在default文件夹下 //TODO [zzt]
		 * js及模板文件是否也需要根据样式文件存放？
		 */
		theme : 'default',
		/**easyui样式*/
		easyUItheme : 'bootstrap',

		/**
		 * 自定义模块版本，模块之间的版本依赖建议不要使用此变量
		 */
		moduleVersion : '1.0',
		projectModulePrefix : 'project-modules',

		/**
		 * 项目自定义模块根路径
		 */
		projectModuleBase : 'seajs/../../project-modules',
		/**默认国际化语言*/
		language: (typeof(language) == 'undefined' ? 'zh_CN' : language),
		/**
		 * 脚本根目录<br>
		 * 相对于base目录，不能以.或..开头
		 */
		scriptRootHome : 'seajs/../../..'
	},
	alias : {
		/** *************开源第三方脚本库************ */
		// jquery库
		'$' : 'jquery/1.11.2/jquery',
		// artTemplate 模板引擎
		'template' : 'template/3.0.0/template',
		'jTemplates' : 'jTemplates/0.8.4/jTemplates',
        //使用xpaht解析json对象
		'jsonPath' : 'jsonPath/0.8.0/jsonPath',
		// 图片延迟加载
		'bttrlazyloading' : 'bttrLazyLoading/1.0.7/jquery.bttrlazyloading',
		// 弹出层组件PC版
		//'layer' : 'layer/1.8.5/layer',
		'layer' : 'layer/1.9.1/layer',
		// 弹出层组件移动版
		//'layer.mobile' : 'layer.mobile/1.2/layer.m',
		//jquery cookie插件
		'cookie':'cookie/1.4.1/cookie',
		// js验证框架
		'validate' : 'validation/1.13.1/validate',
		//base64工具类
		'base64':'code/base64/{moduleVersion}/base64',
		//RSA工具类
		'rsa':'code/rsa/{moduleVersion}/rsa',
		//百度富文本编辑器
		/**
		 * var ue = UE.getEditor('container');
		 */
		'uEeditor':'ueditor/1.4.3/editor',
		//百度富文本编辑器(轻量级)
		/**
		 * var ue = UM.getEditor('container');
		 */
		'umEditor': 'umeditor/1.2.2/editor',
		
		/***easyui 框架***/
		'easyuiParser' : 'easyui/1.4.1/parser',
		'easyuiLinkbutton' : 'easyui/1.4.1/linkbutton',
		'easyuiPagination' : 'easyui/1.4.1/pagination',
		'easyuiDatagrid' : 'easyui/1.4.1/datagrid',
		'easyuiPanel' : 'easyui/1.4.1/panel',
		/**日期控件*/
		'My97DatePicker' : 'My97DatePicker/4.8/date',
		'pageBar':'pageBar/1.0/pageBar',

		/** *************框架级别模块************ */

		// 工具类
		'util' : 'util/{moduleVersion}/util',
		'uploadFile' : 'uploadFile/{moduleVersion}/uploadFile',
		//附件列表面板
		'filePanelList' : '{projectModulePrefix}/filePanelList/{moduleVersion}/filePanelList',
		/** *************ui级别模块************ */
		// 界面公共
		'ui' : 'ui/{moduleVersion}/ui',
		// 消息提示
        'msgBox' : 'msgBox/pc/{moduleVersion}/msgBox',
		// 建议框
		'suggestion' : 'suggestion/{moduleVersion}/suggestion',
		'base':'common/pc/{moduleVersion}/base',
		'loadingTip':'loadingTip/1.0/loadingTip',
		// 截图插件
		'jcrop' : 'jcrop/0.9.12/jquery.Jcrop.min',
		'placeholder':'placeholder/{moduleVersion}/placeholder',
		//多选控件
		'mulSelect':'mulSelect/{moduleVersion}/mulSelect',
		//下拉控件
		'select':'select/{moduleVersion}/select',
        // 百度上传控件
        'WebUploader' : 'webuploader/0.1.5/webuploader.nolog',
        'BaiduUploader' : 'webuploader/0.1.5/baiduUploader',
        // 文本框还能输入xx个字
        'textLimit': 'hiteam/textLimit/{moduleVersion}/textLimit'
	},
	/**预先加载base基础文件*/
	preload : [ 'common/pc/{moduleVersion}/base','placeholder/{moduleVersion}/placeholder']
});
