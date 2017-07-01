package com.hiteam.common.util;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
* @author wengsiwei
 * 对session进行包装，从session读写数据统一用此工具类进行操作
 * 如果到时将session数据存入分布式缓存服务器，有可以统一进行缓存读写操作
 */
public class SessionUtil {
    
	//记录当前正登录系统的所有账号
	private static Map<String,Map<String,Object>> singleLogin=new HashMap<String, Map<String,Object>>();
	
	public static Map<String, Map<String, Object>> getSingleLogin() {
		return singleLogin;
	}

	public static void setSingleLogin(Map<String, Map<String, Object>> singleLogin) {
		SessionUtil.singleLogin = singleLogin;
	}

	/**
	 * 设置session属性值
	 * @param key
	 * @param obj
	 * @param session
	 */
	public static void  setAttribute(String key,Object obj,HttpSession session){
		session.setAttribute(key, obj);
	}
	
	/**
	 * 获取session属性值
	 * @param key
	 * @param session
	 * @return
	 */
	public static Object getAttribute(String key,HttpSession session){
		return session.getAttribute(key);
	}
	
}