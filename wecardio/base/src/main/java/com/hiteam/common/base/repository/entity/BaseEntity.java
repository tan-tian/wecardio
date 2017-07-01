package com.hiteam.common.base.repository.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.hiteam.common.base.repository.listener.EntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * Entity - 基类
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@EntityListeners({EntityListener.class})
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Serializable {

	public static final String ID_FIELD_NAME = "id";					// 主键字段名
	
	/**
	 * 保存验证组
	 */
	public abstract interface Save extends Default {}

	/**
	 * 更新验证组
	 */
	public abstract interface Update extends Default {}
	
	private T id;

    @Transient
	public T getId() {
		return id;
	}
	
	public void setId(T id) {
		this.id = id;
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
		
		if (this == obj) {
			return true;
		}
		
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity entity = (BaseEntity) obj;
		return getId() != null && getId().equals(entity.getId());
	}

	public int hashCode() {
		int i = 17;
		i += (getId() == null ? 0 : getId().hashCode() * 31);
		return i;
	}
	
}

