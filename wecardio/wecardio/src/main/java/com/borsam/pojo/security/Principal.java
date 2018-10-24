package com.borsam.pojo.security;

import com.borsam.pub.UserType;

import java.io.Serializable;

/**
 * 身份信息
 * Created by tantian on 2015/6/17.
 */
public class Principal implements Serializable {

    private Long id;                // 用户ID
    private UserType userType;      // 用户类型
    private String username;        // 用户账号
    private String name;            // 用户名称

    public Principal(Long id, UserType userType, String username, String name) {
        this.id = id;
        this.userType = userType;
        this.username = username;
        this.name = name;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置用户ID
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户类型
     * @return 用户类型
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     * @param userType 用户类型
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * 获取用户账号
     * @return 用户账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户账号
     * @param username 用户账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户姓名
     * @return 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 重新toString方法，返回用户账号
     * @return 用户账号
     */
    @Override
    public String toString() {
        return this.username;
    }
}
