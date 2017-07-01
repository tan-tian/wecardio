package com.hiteam.common.util.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <pre>
 * Description: 枚举转换为json，使用name，而不是ordinal,
 * 使用方式：在属性或方法添加：@JsonSerialize(using = EnumSerializer.class)
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-05 16:56
 * </pre>
 */
public class EnumSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.name());
    }
}
