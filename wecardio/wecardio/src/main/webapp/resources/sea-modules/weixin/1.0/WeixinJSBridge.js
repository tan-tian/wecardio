/**
 * 微信内置浏览器提供的微信JS方法
 * 20141105
 * 
 * 判断是否在微信打开该页面
 * {pass}显示微信中网页右上角菜单按钮[showOptionMenu]
 * {pass}隐藏微信中网页右上角菜单按钮[hideOptionMenu]
 * {pass}显示微信中网页底部导航栏[showToolbar]
 * {pass}隐藏微信中网页底部导航栏[hideToolbar]
 * {pass}网页获取用户网络状态[getNetworkType]
 * {pass}关闭当前网页窗口[closeWindow]
 * {pass}调起微信Native的图片播放组件[imagePreview] : function(curSrc, srcList)
 * 
 * 
 * 
 * 分享到好友[shareSendAppMessage] : function(wxData)
 * 分享到朋友圈[shareTimeline] : function(wxData)
 * 
 */
define('weixin/1.0/WeixinJSBridge', ['$'], function(require, exports, module) {
	
	//微信分享的数据列表
	var wxData = {
		appid: '',
		title: '',//标题
		img_url: '',//引用图片地址
		desc: '',//简介
		link: ''//链接地址
	}
	
	var WeChatJS = {
		//判断是否在微信打开该页面
		
			
		//显示微信中网页右上角菜单按钮
		showOptionMenu : function(){
			WeixinJSBridge.call('showOptionMenu');
		},
		
		//隐藏微信中网页右上角菜单按钮
		hideOptionMenu : function(){
			WeixinJSBridge.call('hideOptionMenu');
		},
		
		//显示微信中网页底部导航栏
		showToolbar : function(){
			WeixinJSBridge.call('showToolbar');
		},
		
		//隐藏微信中网页底部导航栏
		hideToolbar : function(){
			WeixinJSBridge.call('hideToolbar');
		},
		
		//网页获取用户网络状态
		/**
		 	network_type:wifi wifi网络
			network_type:edge 非wifi,包含3G/2G
			network_type:fail 网络断开连接
			network_type:wwan（2g或者3g）
		 */
		getNetworkType : function(){
			WeixinJSBridge.invoke('getNetworkType',{},
		 		function(e){
					return e.err_msg;
		 	    });
		},
		
		//关闭当前网页窗口
		/**
			关闭成功返回“close_window:ok”
			关闭失败返回“close_window:error”。
		 */
		closeWindow : function(){
			WeixinJSBridge.invoke('closeWindow',{},function(res){
			    return res.err_msg;
			});
		},

		//调起微信Native的图片播放组件
		/**
		 * curSrc	首页显示图
		 * srcList	图片数组集
		 */
		imagePreview : function(curSrc, srcList){
			WeixinJSBridge.invoke('imagePreview', {
				'current' : curSrc,
				'urls' : srcList
			});
		},
		
		//分享到好友
		shareSendAppMessage : function(wxData, callback){
			WeixinJSBridge.on('menu:share:appmessage', function(argv) {
				WeixinJSBridge.invoke('sendAppMessage', {
					appid: wxData.appid,
					title: wxData.shareTitle,
					img_url: wxData.imgUrl,
	        		desc: wxData.descContent,
	        		link: wxData.lineLink,
	        		img_width: '640',
	        		img_height: '640'
	        	}, function(res) {
	        		// 不执行回调方法
	        		// alert(JSON.stringify(res));
	        		// _report('weibo', res.err_msg);
	        	})
        	});
		},
		
		//分享到朋友圈
		shareTimeline : function(wxData, callback){
			WeixinJSBridge.on('menu:share:timeline', function(argv) {
				WeixinJSBridge.invoke('shareTimeline', {
					appid: wxData.appid,
					title: wxData.shareTitle,
					img_url: wxData.imgUrl,
	        		desc: wxData.descContent,
	        		link: wxData.lineLink,
	        		img_width: '640',
	        		img_height: '640'
	        	}, function(res) {
	        		// 不执行回调方法
	        		// alert(JSON.stringify(res));
	        		// _report('timeline', res.err_msg);
	        	});
	    	});
		}
	};
	
	module.exports = WeChatJS;
});