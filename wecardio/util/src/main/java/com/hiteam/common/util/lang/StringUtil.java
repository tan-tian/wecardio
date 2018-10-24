package com.hiteam.common.util.lang;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串工具类
 */
public final class StringUtil extends StringUtils{
	
	/**
	 * 判断字符串是否为空
	 * @param val
	 * @return
	 */
	public static boolean isBlank(String val) {
		return isEmpty(val);
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

	/**
	 * 获取当前日期
	 * @return
	 */
    public static String getNowDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

	public static Date parses(String nowTime, String yyyyMMddHHmmss) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(yyyyMMddHHmmss);
	}

	public static String getNowTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}
