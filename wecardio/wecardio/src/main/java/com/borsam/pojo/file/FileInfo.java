package com.borsam.pojo.file;

import java.util.Date;

/**
 * 文件信息
 * Created by tantian on 2015/6/30.
 */
public class FileInfo {

    /**
     * 文件类型
     */
    public enum FileType {
        image, flash, media, file
    }

    private String name;            // 文件名
    private String url;             // URL
    private Boolean isDirectory;    // 是否为目录
    private Long size;              // 文件大小
    private Date lastModified;      // 最后修改时间

    /**
     * 获取名称
     * @return 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取URL
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置URL
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取是否为目录
     * @return 是否为目录
     */
    public Boolean getIsDirectory() {
        return isDirectory;
    }

    /**
     * 设置是否为目录
     * @param isDirectory 是否为目录
     */
    public void setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    /**
     * 获取大小
     * @return 大小
     */
    public Long getSize() {
        return size;
    }

    /**
     * 设置大小
     * @param size 大小
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 获取最后修改时间
     * @return 最后修改时间
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * 设置最后修改时间
     * @param lastModified 最后修改时间
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
