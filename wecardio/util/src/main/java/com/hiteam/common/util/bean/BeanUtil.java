package com.hiteam.common.util.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

import com.hiteam.common.util.lang.DateUtil;

/**
 * bean工具类，主要用于扩展BeanUtils的转换器
 *
 */
public class BeanUtil extends BeanUtils {

	static {
		// 默认值为null，而不是0
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);

		// 注册日期转换器
		ConvertUtils.register(new Converter() {
			public Object convert(Class class1, Object obj) {
				return DateUtil.parseDate(obj, new String[]{DateUtil.DATE_FORMAT.YYYY_MM_DD_HH_MM_SS.getFormat()});
			}
		}, Date.class);

		// 注册字符串转换器
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class class1, Object obj) {
				if (obj == null) {
					return null;
				}

				if (obj instanceof Date) {
					return DateUtil.formatDateTime((Date) obj);
				}

				return obj.toString();
			}
		}, String.class);
	}

	/**
	 * 复制属性
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 */
	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置属性
	 * 
	 * @param bean
	 *            目标对象
	 * @param name
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public static void copyProperty(Object bean, String name, Object value) {
		try {
			BeanUtils.copyProperty(bean, name, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置属性
	 * 
	 * @param bean
	 *            目标对象
	 * @param name
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换Map对象为bean
	 * 
	 * @param clazz
	 *            bean的类型
	 * @param properties
	 *            Map对象
	 * @return
	 */
	public static <T> T populate(Class<T> clazz, Map properties) {
		try {
			T bean = clazz.newInstance();
			BeanUtils.populate(bean, properties);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换Map对象为bean
	 * 
	 * @param bean
	 *            bean对象
	 * @param properties
	 *            Map对象
	 */
	public static void populate(Object bean, Map properties) {
		try {
			BeanUtils.populate(bean, properties);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换bean为Map对象，属性值均会转为字符串类型
	 * 
	 * @param bean
	 * @return
	 */
	public static Map describe(Object bean) {
		try {
			return BeanUtils.describe(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换bean为Map对象，属性值保留原类型
	 * 
	 * @param bean
	 * @return
	 */
	public static Map describeWithType(Object bean) {
		try {
			return PropertyUtils.describe(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
