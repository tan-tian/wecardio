package com.hiteam.common.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.util.json.EnumSerializer;
import com.hiteam.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 消息
 */
public class Message implements Serializable {
	@JsonSerialize(using = EnumSerializer.class)
	private Message.Type type;	// 消息类型

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 消息结果，可以传参
	 */
	private Object result;

	/**
	 * 错误编号
	 */
	private String errorCode;

	public Message() {
	}

	public Message(Type type, String content) {
		this.type = type;
		this.content = content;
	}

	public Message(Type type, String content, Object... args) {
		this.type = type;
		String i18inMsg = I18Util.getMessage(content, args);

		if (StringUtils.isNotBlank(i18inMsg)) {
			this.content = i18inMsg;
		} else {
			this.content = content;
		}

		if (StringUtils.isNotBlank(this.getContent()) && this.getContent().contains("{0}")) {
			for (int i = 0; i < args.length; i++) {
				content = content.replaceAll("\\{"+i+"\\}", String.valueOf(args[i]));
			}

			this.content = content;
		}
	}

	public static Message success(String content, Object... args) {
		return new Message(Type.success, content, args);
	}

	public static Message warn(String content, Object... args) {
		return new Message(Type.warn, content, args);
	}

	public static Message error(String content, Object... args) {
		return new Message(Type.error, content, args);
	}

	public static Message error() {
		return new Message(Type.error, "common.message.error",new Object[0]);
	}

	public static Message warn() {
		return new Message(Type.warn, "common.message.warn",new Object[0]);
	}

	/**
	 * 添加返回结果
	 *
	 * @param result 返回结果
	 * @return Message
	 */
	public Message addResult(Object result) {
		this.setResult(result);
		return this;
	}

	/***
	 * 添加返回的错误码
	 *
	 * @param errorCode 错误码
	 * @return Message
	 */
	public Message addErrorCode(String errorCode) {
		this.setErrorCode(errorCode);
		return this;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String toString() {
		return I18Util.getMessage(this.content, new Object[0]);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public enum Type {
		success,  warn,  error;
	}
}
