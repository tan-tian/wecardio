package com.hiteam.common.util.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json工具类
 */
public final class JsonUtils {

	private static ObjectMapper objMapper = new JsonDataMapping();

	/**
	 * 不可实例化
	 */
	private JsonUtils() {
	}

	/**
	 * 对象转json字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String toJson(Object value) {
		try {
			return objMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json串转对象
	 * 
	 * @param json
	 *            json串
	 * @param valueType
	 *            对象类型
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> valueType) {
		Assert.hasText(json);
		Assert.notNull(valueType);

		try {
			return objMapper.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json串转对象
	 * 
	 * @param json
	 *            json串
	 * @param typeReference
	 *            对象类型
	 * @return
	 */
	public static <T> T toObject(String json, TypeReference<?> typeReference) {
		Assert.hasText(json);
		Assert.notNull(typeReference);

		try {
			return objMapper.readValue(json, typeReference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json串转对象
	 * 
	 * @param json
	 *            json串
	 * @param javaType
	 *            对象类型
	 * @return
	 */
	public static <T> T toObject(String json, JavaType javaType) {
		Assert.hasText(json);
		Assert.notNull(javaType);

		try {
			return objMapper.readValue(json, javaType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * json串转具有指定泛型的List对象
	 * 
	 * @param json
	 *            json串
	 * @param valueType
	 *            对象类型
	 * @return
	 */
	public static List toList(String json, Class<?> valueType) {
		Assert.hasText(json);
		Assert.notNull(valueType);

		JavaType javaType = objMapper.getTypeFactory().constructParametricType(
				ArrayList.class, valueType);
		try {
			return (List) objMapper.readValue(json, javaType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将对象转换为json流
	 * 
	 * @param writer
	 *            writer
	 * @param value
	 *            对象
	 */
	public static void writeValue(Writer writer, Object value) {
		try {
			objMapper.writeValue(writer, value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
