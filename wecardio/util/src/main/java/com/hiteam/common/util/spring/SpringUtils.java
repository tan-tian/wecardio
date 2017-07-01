package com.hiteam.common.util.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring工具类
 */
@Component
@Lazy(value = false)
public final class SpringUtils implements DisposableBean, ApplicationContextAware {
	private static ApplicationContext context;

	/**
	 * 不可实例化
	 */
	private SpringUtils() {}

	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public void destroy() {
		context = null;
	}

	/**
	 * 获取applicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}


	/**
	 * 从Spring容器中获取指定名称的对象
	 *
	 * @param beanName bean名称
	 * @param <T>      对象类型
	 * @return spring容器中的对象
	 */
	public static <T> T getBean(String beanName) {
		Assert.hasText(beanName);
		return (T) context.getBean(beanName);
	}
}
