package com.hiteam.common.util.lang;


import org.apache.commons.lang.StringUtils;

/**
 * Json工具类
 */
public final class StringUtil extends StringUtils{
	
	/**
	 * 判断字符串是否为空
	 * @param val
	 * @return
	 */
	public static boolean isBlank(String val) {
		boolean res=false;
		if (val==null  || val.equals("")){
			res=true;
		}
			
		return res;
	}

	public static Object toString(Object result) {
		
		return String.valueOf(result);
	}


    public static boolean checkStr(String targetUrl) {
		return true;
    }

	public static boolean checkObj(Object sUrl) {
		return true;
	}

	public static String getIPAddress() {
		String ipAddress="";
		return ipAddress;
	}
}
