package com.hiteam.common.pojo.enums;

import java.lang.annotation.*;

/**
 * <pre>
 * Description: 枚举翻译注解,配置到类的属性字段中
 * Author:Zhang zhongtao
 * Since: Ver 1.1
 * Date: 2015-05-05 15:35
 * </pre>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface EnumTrans {
    /**
     * 表名，对应WhsEnum.sEnumTblName
     * 如果不填，则从当前属性或方法所在的类中查找表名注解
     *
     * @return
     */
    String tableName();

    /**
     * 列名，对应WhsEnum.sEnumColName
     * 必填
     * @return
     */
    String colName();

    /**
     * 翻译枚举后存储的字段名,必填
     * @return
     */
    String nameField();
}
