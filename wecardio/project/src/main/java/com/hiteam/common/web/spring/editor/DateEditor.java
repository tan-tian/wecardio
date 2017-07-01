package com.hiteam.common.web.spring.editor;

import org.apache.commons.lang.time.DateUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类型转换
 * 用于Springmvc数据绑定

 *
 */
public class DateEditor extends PropertyEditorSupport {

	/**
	 * 默认日期格式
	 */
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式匹配
	 */
	private static final String[] DATE_PATTERNS = {
			"yyyy",
			"yyyy-MM",
			"yyyyMM",
			"yyyy/MM",
			"yyyy-MM-dd",
			"yyyyMMdd",
			"yyyy/MM/dd",
			"yyyy-MM-dd HH:mm:ss",
			"yyyyMMddHHmmss",
			"yyyy/MM/dd HH:mm:ss",
			"HH:mm",
			"HH:mm:ss"
	};
	
	private boolean emptyAsNull;	// 空值是否转为null
	private String dateFormat = DEFAULT_DATE_FORMAT;

	public DateEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		return date != null ? new SimpleDateFormat(this.dateFormat).format(date) : "";
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String str = text.trim();
			if ((this.emptyAsNull) && ("".equals(str))) {
				setValue(null);
			} else {
				try {
					setValue(DateUtils.parseDate(str, DATE_PATTERNS));
				} catch (ParseException e) {
					setValue(null);
				}
			}
		}
	}
}
