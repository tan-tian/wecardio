package com.hiteam.common.web.controller;

import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.spring.editor.DateEditor;
import com.hiteam.common.web.spring.editor.HtmlCleanEditor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class BaseController {

	/**
	 * 错误视图
	 */
	protected static  String ERROR_VIEW = "/guest/common/error";
	
	/**
	 * 错误消息
	 */
	protected static  Message ERROR_MSG = Message.error("common.message.error", new Object[0]);
	
	/**
	 * 验证码错误消息
	 */
	protected static  Message CODEERROR_MSG = Message.error("common.message.codeerror", new Object[0]);
	
	/**
	 * 警告消息
	 */
	protected static  Message WARN_MSG = Message.warn("common.message.warn", new Object[0]);

	/**
	 * imei号已 存在
	 */
	protected static  Message IMEI_MSG = Message.warn("common.message.imei.exist", new Object[0]);
	
	/**
	 * 验证结果参数名
	 */
	protected static  String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";
	
	/**
	 * 成功消息
	 */
	protected static  Message SUCCESS_MSG = Message.success("common.message.success", new Object[0]);
	
	@Resource(name = "validator")
	private Validator validator;

	/**
	 * 初始化数据绑定
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}
	
	/**
	 * 数据验证
	 * @param target 验证对象
	 * @param groups 验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validate(target,groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}

	protected Set<ConstraintViolation<Object>> validate(Object target, Class<?>... groups){
		return validator.validate(target, groups);
	}
	
	/**
	 * 数据验证
	 * @param type		类型
	 * @param property	属性
	 * @param value		值
	 * @param groups	验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Class<?> type, String property, Object value, Class<?>... groups) {
		Set<?> constraintViolations = validator.validateValue(type, property, value, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}
	
	/**
	 * 获取国际化消息
	 * @param code 代码
	 * @param args 参数
	 * @return 国际化消息
	 */
	protected String message(String code, Object... args) {
		return I18Util.getMessage(code, args);
	}
	
	/**
	 * 添加日志
	 * @param content 内容
	 */
	protected void addLog(String content) {
		if (content != null) {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute("日志内容属性名", content, RequestAttributes.SCOPE_REQUEST);
		}
	}

	/**
	 * 打开页面
	 */
	@RequestMapping(value = "/toPage/{url}",method= RequestMethod.GET)
	public ModelAndView toPage(@PathVariable("url") String url,
							   @RequestParam Map params,
							   @RequestHeader("Referer") String referer,
							   ModelMap model) {
		RequestMapping requestMapping = AnnotationUtils.getAnnotation(this.getClass(), RequestMapping.class);
		String[] strings = requestMapping.value();
		if(strings != null && strings.length > 0){
			url = strings[0] + "/" + url;
		}

		return new ModelAndView(url);
	}
	
}
