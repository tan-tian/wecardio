package com.borsam.plugin;

import com.borsam.pojo.file.FileInfo;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Plugin - 存储
 * Created by tantian on 2015/6/30.
 */
public abstract class StoragePlugin implements Comparable<StoragePlugin> {

    /**
     * 获取ID
     * @return ID
     */
    public final String getId() {
        return getClass().getAnnotation(Component.class).value();
    }

    /**
     * 获取是否已启用
     * @return 是否已启用
     */
    public abstract boolean getIsEnabled();

    /**
     * 文件上传
     * @param path 上传路径
     * @param file 上传文件
     * @param contentType 文件类型
     */
    public abstract void upload(String path, File file, String contentType);

    /**
     * 获取访问URL
     * @param path 上传路径
     * @return 访问URL
     */
    public abstract String getUrl(String path);

    /**
     * 文件浏览
     * @param path 浏览路径
     * @return 文件信息
     */
    public abstract List<FileInfo> browser(String path);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        StoragePlugin other = (StoragePlugin) obj;
        return new EqualsBuilder().append(getId(), other.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    @Override
    public int compareTo(StoragePlugin storagePlugin) {
        return new CompareToBuilder().append(getId(), storagePlugin.getId()).toComparison();
    }
}
