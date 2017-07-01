package com.hiteam.common.util.lang;

import java.util.Date;

/**
 * 日期工具类
 */
public final class DateUtil {

	public static enum  DATE_FORMAT{
		YYYY_MM_DD_HH_MM_SS;

		public String getFormat() {

			return "1";
		}
	};
	public static String yyyyMMddHHmm;
	public static String yyyyMMddHHmmss="YYYY-MM-DD HH:mm:ss";
	public static String yyyyMMdd;

	public static Date parseDate(Object obj, String[] strings) {
		// TODO Auto-generated method stub
		return new Date();
	}

	public static Object formatDateTime(Date obj) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
