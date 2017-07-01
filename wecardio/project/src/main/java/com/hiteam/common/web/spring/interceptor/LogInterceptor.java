package com.hiteam.common.web.spring.interceptor;

import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.bean.BeanUtil;
import com.hiteam.common.util.json.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

//import org.jboss.logging.MDC;

/**
 * 操作日志拦截器
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
	public static final Logger ACCESS_LOG = LoggerFactory.getLogger("web-access");

	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
	
	private static final Long DEFAULT_MILLS = Long.parseLong(ConfigUtils.config.getProperty("slowRequestMillis", "0"));
	
	/**
	 * 设定的请求时间基准
	 */
	private Long slowRequestMillis = DEFAULT_MILLS;

	/**
	 * 执行时间ThreadLocal
	 */
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("executeTimeWatch");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();
		startTimeThreadLocal.set(beginTime);	// 记录开始时间
		logAccess2(request);
		return true;
	}

	public static void logAccess2(HttpServletRequest request) {
		String username = getUsername();
		MDC.put("loginUserName",username);

		String jsessionId = request.getRequestedSessionId();
		String ip = getIpAddr(request);
		String accept = request.getHeader("accept");
		String userAgent = request.getHeader("User-Agent");
		String url = request.getRequestURI();
		String params = getParams(request);
		String headers = getHeaders(request);

		StringBuilder s = new StringBuilder();
		s.append(getBlock(jsessionId));
		s.append(getBlock(ip));
		s.append(getBlock(accept));
		s.append(getBlock(userAgent));
		s.append(getBlock(url));
		s.append(getBlock(params));
		s.append(getBlock(headers));
		s.append(getBlock(request.getHeader("Referer")));
		ACCESS_LOG.info(s.toString());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 计算执行时间
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTimeThreadLocal.get();
		
		// 记录超过设定时间的请求
		if (executeTime > slowRequestMillis) {
			logger.warn("记录慢请求！请求路径：{} | 执行方法：{} | 耗时：{}ms", new Object[] { request.getRequestURI(), handler, executeTime});
		}

		MDC.remove("loginUserName");

	}

	protected static String getUsername() {
		Object o = SecurityUtils.getSubject().getPrincipal();
		String username = "游客";

		if(o != null){
			try {
				username = BeanUtil.getProperty(o,"username");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return username;
	}

	public static String getIpAddr(HttpServletRequest request) {
		if (request == null) {
			return "unknown";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	protected static String getParams(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		return JsonUtils.toJson(params);
	}


	private static String getHeaders(HttpServletRequest request) {
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		Enumeration<String> namesEnumeration = request.getHeaderNames();
		while(namesEnumeration.hasMoreElements()) {
			String name = namesEnumeration.nextElement();
			Enumeration<String> valueEnumeration = request.getHeaders(name);
			List<String> values = new ArrayList<String>();
			while(valueEnumeration.hasMoreElements()) {
				values.add(valueEnumeration.nextElement());
			}
			headers.put(name, values);
		}
		return JsonUtils.toJson(headers);
	}

	public static String getBlock(Object msg) {
		if (msg == null) {
			msg = "";
		}
		return "[" + msg.toString() + "]";
	}

	
}
