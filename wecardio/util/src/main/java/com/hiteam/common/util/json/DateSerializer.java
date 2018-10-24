package com.hiteam.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期序列化
 * 描述：Date类型的属性默认序列为Long类型，扩展为按"yyyy-MM-dd"格式序列化为字符串
 * 使用方式：在属性或方法增加注解 @JsonSerialize(using = DateSerializer.class)
 * Created by tantian on 2014/10/13.
 */
public class DateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat(getDateFormat());
        String value = formatter.format(date);
        jsonGenerator.writeString(value);
    }

    protected String getDateFormat() {
        return "yyyy-MM-dd";
    }
}
