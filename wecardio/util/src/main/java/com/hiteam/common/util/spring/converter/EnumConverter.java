package com.hiteam.common.util.spring.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * <pre>
 * Description: 字符串（枚举对象的名称）或整型转换(枚举的ordinal)为枚举类型
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-04 16:36
 * </pre>
 */
public class EnumConverter implements ConverterFactory<String, Enum> {

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new ValueToEnum(targetType);
    }

    private class ValueToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        public ValueToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            //空字符串，则默认为0，便于获取枚举第一个对象
            if(StringUtils.isBlank(source)){
                source = "0";
            }

            //数字类型，则使用ordinal进行获取名称
            if(NumberUtils.isNumber(source)){
                source = this.enumType.getDeclaredFields()[NumberUtils.toInt(source,0)].getName();
            }

            return (T) Enum.valueOf(this.enumType,source);
        }
    }
}
