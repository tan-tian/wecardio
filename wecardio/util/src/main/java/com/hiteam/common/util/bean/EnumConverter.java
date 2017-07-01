package com.hiteam.common.util.bean;

import org.apache.commons.beanutils.converters.AbstractConverter;

/**
 * 枚举类型转换
 * @author wengsiwei
 *
 */
public class EnumConverter extends AbstractConverter {
	
	/**
	 * 枚举类型
	 */
	private final Class<?> enumClass;
	
	
	public EnumConverter(Class<?> enumClass) {
		this(enumClass, null);
	}
	
	public EnumConverter(Class<?> enumClass, Object defaultValue) {
		super(defaultValue);
		this.enumClass = enumClass;
	}
	
	/**
	 * 转换为字符串
	 */
	protected String convertToString(Object value) {
		return value.toString();
	}

	/**
	 * 转换为枚举对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object convertToType(Class type, Object value) throws Throwable {
		String s = value.toString().trim();
		return Enum.valueOf(type, s);
	}

	@Override
	protected Class<?> getDefaultType() {
		return this.enumClass;
	}

}
