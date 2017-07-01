package com.hiteam.common.base.pojo.search;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 排序

 *
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3581537656282848869L;
	
	/**
	 * 默认排序方向
	 */
	private static final Direction DEFAULT_DIRECTION = Direction.asc;
	
	/**
	 * 属性
	 */
	private String property;
	
	/**
	 * 排序方向
	 */
	private Direction direction = DEFAULT_DIRECTION;
	
	public Order() {}
	
	public Order(String property, Direction direction) {
		this.property = property;
		this.direction = direction;
	}
	
	/**
	 * 升序排序
	 * @param property
	 * @return
	 */
	public static Order asc(String property) {
		return new Order(property, Direction.asc);
	}
	
	/**
	 * 降序排序
	 * @param property
	 * @return
	 */
	public static Order desc(String property) {
		return new Order(property, Direction.desc);
	}

	/**
	 * 排序方向

	 *
	 */
	public enum Direction {
		asc, desc;

		public static Direction fromString(String value) {
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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/*
	 * =------------------------------------------------------------=
	 * Generate equals & hashCode methods
	 * =------------------------------------------------------------=
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty()).append(getDirection()).toHashCode();
	}

	@Override
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
		
		Order order = (Order) obj;
		
		return new EqualsBuilder().append(getProperty(), order.getProperty()).append(getDirection(), order.getDirection()).isEquals();
	}
	
}
