package com.hiteam.common.util.xml;

import java.util.TimeZone;

import com.hiteam.common.util.lang.DateUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;

/**
 * Xstream工具类
 *
 */
public class XstreamUtil {

	/**
	 * xstream核心对象，忽略null元素
	 */
	private static XStream xstream = createXStream(true, true);
	/**
	 * xstream核心对象，不忽略null元素
	 */
	private static XStream notIgnoreNullXstream;
	/**
	 * 锁对象
	 */
	private static Object lockObj = new Object();

	/**
	 * 创建xstream对象
	 * 
	 * @param isAddCDATA
	 *            转xml时是否输出CDATA
	 * @param ignoreNullElement
	 *            转xml时是否忽略null元素
	 * @return
	 */
	private static XStream createXStream(boolean isAddCDATA,
			boolean ignoreNullElement) {
		XStream xstream = XStreamFactory.init(isAddCDATA);
		xstream.ignoreUnknownElements();// 忽略不可识别元素
		// 注册日期转换器
		xstream.registerConverter(new DateConverter(DateUtil.yyyyMMddHHmmss,
				new String[] { DateUtil.yyyyMMddHHmm, DateUtil.yyyyMMdd },
				TimeZone.getDefault(), false) {
			@Override
			public Object fromString(String str) {
				if (StringUtil.isBlank(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		// 注册以下类型的转换器，支持将空字符串转为null
		xstream.registerConverter(new IntConverter() {
			@Override
			public Object fromString(String str) {
				if (StringUtil.isBlank(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		xstream.registerConverter(new LongConverter() {
			@Override
			public Object fromString(String str) {
				if (StringUtil.isBlank(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		xstream.registerConverter(new DoubleConverter() {
			@Override
			public Object fromString(String str) {
				if (StringUtil.isBlank(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});

		if (!ignoreNullElement) {
			xstream.registerConverter(
					new MyReflectionConverter(xstream.getMapper(), xstream
							.getReflectionProvider()),
					XStream.PRIORITY_VERY_LOW);
		}

		return xstream;
	}

	/**
	 * 通过Xstream注解方式转换xml字符串为bean对象
	 *
	 * @param xml
	 *            xml字符串
	 * @param cls
	 *            目标对象类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String xml, Class<T> cls) {
		xstream.processAnnotations(cls);// 解析类注解
		T obj = (T) xstream.fromXML(xml);
		return obj;
	}

	/**
	 * 通过Xstream注解方式转换bean对象为xml字符串
	 *
	 * @param obj
	 *            bean对象
	 * @return
	 */
	public static String toXml(Object obj) {
		return toXml(obj, true);
	}

	/**
	 * 通过Xstream注解方式转换bean对象为xml字符串
	 * 
	 * @param obj
	 *            bean对象
	 * @param ignoreNullElement
	 *            是否忽略null元素
	 * @return
	 */
	public static String toXml(Object obj, boolean ignoreNullElement) {
		XStream xs = ignoreNullElement ? xstream : getNotIgnoreNullXstream();

		// 如果没有这句，xml中的根元素会是<包.类名>；
		// 或者说：注解根本就没生效，所以的元素名就是类的属性
		xs.processAnnotations(obj.getClass()); // 解析类注解
		return xs.toXML(obj);
	}

	/**
	 * 获取不忽略null元素的xstream对象
	 * 
	 * @return
	 */
	private static XStream getNotIgnoreNullXstream() {
		if (notIgnoreNullXstream == null) {
			synchronized (lockObj) {
				if (notIgnoreNullXstream == null) {
					notIgnoreNullXstream = createXStream(true, false);
				}
			}
		}

		return notIgnoreNullXstream;
	}

}
