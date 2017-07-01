package com.hiteam.common.base.repository.entity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.listener.EntityListener;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@EntityListeners({EntityListener.class})
@MappedSuperclass
public abstract class StringEntity  extends BaseEntity<String> {

}
