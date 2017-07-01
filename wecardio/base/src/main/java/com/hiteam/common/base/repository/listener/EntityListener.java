package com.hiteam.common.base.repository.listener;

import com.hiteam.common.base.repository.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Listener - 实体持久化监听器
 */
public class EntityListener {

	/**
	 * 保存前处理
	 */
	@PrePersist
	public void prePersist(BaseEntity entity) {

	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate(BaseEntity entity) {

	}
}
