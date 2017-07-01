package com.hiteam.common.pojo.enums;

/**
 * <pre>
 * Description:
 * Author:Zhang zhongtao
 * Since: Ver 1.1
 * Date: 2015-05-04 22:50
 * </pre>
 */
public class EnumTransformData {
    /**表名，对应WhsEnum.sEnumTblName*/
    private String tableName;
    /**字段名，对应WhsEnum.sEnumColName*/
    private String colName;
    /**枚举值存储的字段名*/
    private String valField;
    /**翻译枚举后存储的字段名*/
    private String nameField;

    public EnumTransformData() {
    }

    public EnumTransformData(String tableName, String colName, String valField, String nameField) {
        this.tableName = tableName;
        this.colName = colName;
        this.valField = valField;
        this.nameField = nameField;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getValField() {
        return valField;
    }

    public void setValField(String valField) {
        this.valField = valField;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }
}
