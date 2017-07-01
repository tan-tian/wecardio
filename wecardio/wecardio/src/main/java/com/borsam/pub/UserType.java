package com.borsam.pub;

/**
 * 用户类型
 * Created by Sebarswee on 2015/6/17.
 */
public enum UserType {
    admin("admin"), org("org"), doctor("doctor"), patient("patient");

    private String path;    // 访问路径

    private UserType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return this.name();
    }
}
