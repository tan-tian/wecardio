/**
 * 移动端或微信端专用
 * 模块别名及对应版本配置模板<br>
 * 注：请将该模板复制到项目中使用，不建议直接使用该模板
 */

// seajs模块配置
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

		/**
		 * 自定义模块版本，模块之间的版本依赖建议不要使用此变量
		 */
		moduleVersion : '1.0',

		/**
		 * 项目自定义模块根路径
		 */
		projectModuleBase : 'seajs/../../project-modules',

		/**
		 * 脚本根目录<br>
		 * 相对于base目录，不能以.或..开头
		 */
		scriptRootHome : 'seajs/../../..'
	},
	alias : {
		/** *************开源第三方脚本库************ */
		// jquery库
		'$' : 'jquery/2.0.3/jquery',
		// artTemplate 模板引擎
		'template' : 'template/3.0.0/template',
		// 模板引擎
		'jTemplates' : 'jTemplates/0.8.4/jTemplates',
		// 百度地图
		'baiduMap' : 'baiduMap/1.5/baiduMap',
        //使用xpaht解析json对象
		'jsonPath' : 'jsonPath/0.8.0/jsonPath',
		// 图片延迟加载
		'bttrlazyloading' : 'bttrLazyLoading/1.0.7/jquery.bttrlazyloading',
		// 弹出层组件PC版
		'layer' : 'layer/1.8.5/layer',
		// 弹出层组件移动版
		'layer.mobile' : 'layer.mobile/1.2/layer.m',
		//jquery cookie插件
		'cookie':'cookie/1.4.1/cookie',
		// js验证框架
		'validate' : 'validation/1.13.1/validate',
		//base64工具类
		'base64':'code/base64/{moduleVersion}/base64',
		//RSA工具类
		'rsa':'code/rsa/{moduleVersion}/rsa',
		//微信内置浏览器提供的微信JS方法
		'WeChatJS' : 'weixin/{moduleVersion}/WeixinJSBridge',
		
		/** *************框架级别模块************ */
		// 文件操作
		'file' : 'file/{moduleVersion}/file',
		// 网络接口操作
		'net' : 'net/{moduleVersion}/net',
		// 定位服务
		'location' : 'location/{moduleVersion}/location',
		// 工具类
		'util' : 'util/{moduleVersion}/util',
		// 系统信息
		'system' : 'system/{moduleVersion}/system',
		// 资源管理
		'resourceManage' : 'resourceManage/1.0/resourceManage',

		/** *************ui级别模块************ */
		// 界面公共
		'ui' : 'ui/{moduleVersion}/ui',
		// echart工具类
		'echartTool' : 'echartTool/{moduleVersion}/echartTool',
		// 进度条
		'progressBar' : 'progressBar/{moduleVersion}/progressBar',
		// 消息提示
		'msgBox' : 'msgBox/{moduleVersion}/msgBox',
		// 底部工具条
		'bottomBar' : 'bottomBar/{moduleVersion}/bottomBar',
		// 快键菜单
		'shortcutMenu' : 'shortcutMenu/{moduleVersion}/shortcutMenu',
		// 建议框
		'suggestion' : 'suggestion/{moduleVersion}/suggestion',

		// base
		'baseMobile' : 'common/mobile/{moduleVersion}/base',
		// 截图插件
		'jcrop' : 'jcrop/0.9.12/jquery.Jcrop.min'
	},
	/**预先加载base基础文件*/
	preload : [ 'common/mobile/1.0/base.js']
});
