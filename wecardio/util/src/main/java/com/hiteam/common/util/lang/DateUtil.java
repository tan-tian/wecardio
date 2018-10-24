package com.hiteam.common.util.lang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public final class DateUtil {


	public enum  DATE_FORMAT{
		YYYY_MM_DD_HH_MM_SS;
		public String getFormat() {
			return "1";
		}
	}
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

	// 获取月份间隔
	public static Integer getMonthsBetween(Date time, Date time1) {
		return null;
	}

	// 获取间隔天数
	public static Integer getDaysBetween(Date time, Date time1) {
		return null;
	}

	public static void clearTime(Calendar startCal) {
	}

	// 获取格式化时间字符串
	public static String format(Date time, String yyyyMMdd) {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(yyyyMMdd);
		return simpleDateFormat.format(time);
	}

	// 转化为日期类
	public static Calendar toCalendar(Date endTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		return calendar;
	}
}
