package com.hiteam.common.util.pojo;

/**
 * 枚举 - Bean
 * 供下拉框使用
 * @author wengsiwei
 *
 */
public class EnumBean {

    private Long value;		// 实际值
    private String text;	// 显示名称
    /**其他附带数据*/
    private Object data;

    public EnumBean() {
    }

    public EnumBean(Long value, String text) {
        this.value = value;
        this.text = text;
    }

    public EnumBean(Integer value, String text) {
        this.value = Long.valueOf(value.longValue());
        this.text = text;
    }

    public EnumBean(Long value, String text, Object data) {
        this(value,text);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
