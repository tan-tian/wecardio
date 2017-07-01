package com.hiteam.common.util.json;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 处理返回json格式时，空串和日期的格式化
 * 
 *
 */
public class JsonDataMapping extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5605424237698352497L;

	public JsonDataMapping() {

		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 枚举类型返回ordinal，而不是名称
		configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
		// 遇到未知属性不报错
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		this.getSerializerProvider().setNullValueSerializer(
				new JsonSerializer<Object>() {

					@Override
					public void serialize(Object value, JsonGenerator jg,
							SerializerProvider sp) throws IOException,
							JsonProcessingException {
						jg.writeString("");
					}
				});

	}
}
