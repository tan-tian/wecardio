package com.borsam.plugin.file;

import com.borsam.plugin.StoragePlugin;
import com.borsam.pojo.file.FileInfo;
import com.hiteam.common.util.ConfigUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Plugin - 本地存储
 * Created by Sebarswee on 2015/6/30.
 */
@Component("filePlugin")
public class FilePlugin extends StoragePlugin {

    @Override
    public boolean getIsEnabled() {
        return Boolean.valueOf(ConfigUtils.config.getProperty("isFilePluginEnable"));
    }

    @Override
    public void upload(String path, File file, String contentType) {
        File destFile = new File(ConfigUtils.config.getProperty("uploadPath"), path);
        try {
            FileUtils.moveFile(file, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl(String path) {
        return path;
    }

    @Override
    public List<FileInfo> browser(String path) {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        File directory = new File(ConfigUtils.config.getProperty("uploadPath"), path);
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(file.getName());
                fileInfo.setUrl(ConfigUtils.config.getProperty("siteUrl") + "/file?path=" + path + file.getName());
                fileInfo.setIsDirectory(file.isDirectory());
                fileInfo.setSize(file.length());
                fileInfo.setLastModified(new Date(file.lastModified()));
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }
}
