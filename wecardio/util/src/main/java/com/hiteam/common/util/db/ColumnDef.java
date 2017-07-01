package com.hiteam.common.util.db;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 数据库列定义
 *
 */
@XStreamAlias("o:Column")
public class ColumnDef {

	/**
	 * 列名称
	 */
	@XStreamAlias("a:Name")
	private String name;
	/**
	 * 数据库列名
	 */
	@XStreamAlias("a:Code")
	private String code;
	/**
	 * 列注释
	 */
	@XStreamAlias("a:Comment")
	private String comment;
	/**
	 * 列类型
	 */
	@XStreamAlias("a:DataType")
	private String dataType;
	/**
	 * 列长度
	 */
	@XStreamAlias("a:Length")
	private Integer length;
	/**
	 * 列精度
	 */
	@XStreamAlias("a:Precision")
	private Integer precision;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

}
