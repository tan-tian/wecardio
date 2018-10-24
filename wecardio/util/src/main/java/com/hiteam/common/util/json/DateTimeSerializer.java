package com.hiteam.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间序列化
 * 描述：Date类型的属性默认序列为Long类型，扩展为按"yyyy-MM-dd HH:mm:ss"格式序列化为字符串
 * 使用方式：在属性或方法增加注解 @JsonSerialize(using = DateTimeSerializer.class)
 * Created by tantian on 2014/10/13.
 */
public class DateTimeSerializer extends DateSerializer {

    @Override
    protected String getDateFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
