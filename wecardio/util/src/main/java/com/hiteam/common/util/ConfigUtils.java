package com.hiteam.common.util;

import com.hiteam.common.util.spring.SpringUtils;

import java.util.Properties;

/**
 * 配置属性工具类
 * 使用枚举实现单例
 */
public enum ConfigUtils {

	config;

	private Properties properties = new Properties();

	/**
	 * 构造方法加载配置文件
	 */
	private ConfigUtils() {
		properties = SpringUtils.getBean("configProperties");
	}

	/**
	 * 获取配置属性
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取配置属性，获取不到则使用默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * 获取配置属性(整数)，获取不到则使用默认值
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 整数
	 */
	public Integer getProperty(String key, Integer defaultValue){
		String value = getProperty(key,defaultValue.toString());
		return Integer.valueOf(value);
	}
}
