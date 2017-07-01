package com.hiteam.common.base.pojo.search;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 筛选

 *
 */
public class Filter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6779608679210820377L;

	/**
	 * 默认是否忽略大小写
	 */
	private static final boolean DEFAULT_IGNORE_CASE = false;
	
	private String property;			// 属性
	private Operator operator;	// 运算操作
	private Object value;				// 值
	private Boolean ignoreCase = DEFAULT_IGNORE_CASE;
	
	public Filter() {}
	
	/**
	 * 初始化Filter对象
	 * @param property	属性
	 * @param operator	运算符
	 * @param value		值
	 */
	public Filter(String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	/**
	 * 初始化Filter对象
	 * @param property	属性
	 * @param operator	运算符
	 * @param value		值
	 * @param ignoreCase 忽略大小写
	 */
	public Filter(String property, Operator operator, Object value, boolean ignoreCase) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.ignoreCase = Boolean.valueOf(ignoreCase);
	}
	
	public static Filter eq(String property, Object value) {
		return new Filter(property, Operator.eq, value);
	}
	
	public static Filter eq(String property, Object value, boolean ignoreCase) {
		return new Filter(property, Operator.eq, value, ignoreCase);
	}
	
	public static Filter ne(String property, Object value) {
		return new Filter(property, Operator.ne, value);
	}

	public static Filter ne(String property, Object value, boolean ignoreCase) {
		return new Filter(property, Operator.ne, value, ignoreCase);
	}

	public static Filter gt(String property, Object value) {
		return new Filter(property, Operator.gt, value);
	}

	public static Filter lt(String property, Object value) {
		return new Filter(property, Operator.lt, value);
	}

	public static Filter ge(String property, Object value) {
		return new Filter(property, Operator.ge, value);
	}

	public static Filter le(String property, Object value) {
		return new Filter(property, Operator.le, value);
	}

	public static Filter like(String property, Object value) {
		return new Filter(property, Operator.like, value);
	}

	public static Filter in(String property, Object value) {
		return new Filter(property, Operator.in, value);
	}
	
	public static Filter ni(String property, Object value) {
		return new Filter(property, Operator.ni, value);
	}

	public static Filter isNull(String property) {
		return new Filter(property, Operator.isNull, null);
	}

	public static Filter isNotNull(String property) {
		return new Filter(property, Operator.isNotNull, null);
	}
	
	public static Filter dgt(String property, Object value) {
		return new Filter(property, Operator.dgt, value);
	}

	public static Filter dlt(String property, Object value) {
		return new Filter(property, Operator.dlt, value);
	}

	public static Filter dge(String property, Object value) {
		return new Filter(property, Operator.dge, value);
	}

	public static Filter dle(String property, Object value) {
		return new Filter(property, Operator.dle, value);
	}
	
	public Filter ignoreCase() {
		this.ignoreCase = Boolean.valueOf(true);
		return this;
	}
	
	/**
	 * 运算符枚举值

	 *
	 */
	public enum Operator {
		eq, ne, gt, lt, ge, le, like, in, ni, isNull, isNotNull,
		dgt, dlt, dge, dle;

		public static Operator fromString(String value) {
			return valueOf(value.toLowerCase());
		}
	}

	/*
	 * =------------------------------------------------------------=
	 * get & set methods
	 * =------------------------------------------------------------=
	 */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
	
	/*
	 * =------------------------------------------------------------=
	 * Generate equals & hashCode methods
	 * =------------------------------------------------------------=
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		Filter filter = (Filter) obj;
		
		return new EqualsBuilder().append(this.getProperty(), filter.getProperty()).append(this.getOperator(), filter.getOperator()).append(this.getValue(), filter.getValue()).isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.getProperty()).append(this.getOperator()).append(this.getValue()).toHashCode();
	}
}
