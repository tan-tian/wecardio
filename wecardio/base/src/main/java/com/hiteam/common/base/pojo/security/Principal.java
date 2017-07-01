package com.hiteam.common.base.pojo.security;

import java.io.Serializable;

/**
 * shiro身份
 * @author wengsiwei
 *
 */
public class Principal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9147976480804708711L;

	private Long id;			// ID
	private String username;	// 用户名
	private String name;		// 姓名

	public Principal(Long id, String username) {
		this.id = id;
		this.username = username;
	}
	
	public Principal(Long id, String username, String name) {
		this.id = id;
		this.username = username;
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return this.username;
	}
}
