package com.borsam.service.pub.impl;

import com.borsam.plugin.StoragePlugin;

import com.borsam.pojo.file.FileInfo;
import com.borsam.service.pub.FileService;
import com.borsam.service.pub.PluginService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service - 文件
 * Created by Sebarswee on 2015/6/30.
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService, ServletContextAware {

    private ServletContext servletContext;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 添加上传任务
     * @param storagePlugin 存储插件
     * @param path 上传路径
     * @param tempFile 临时文件
     * @param contentType 文件类型
     */
    private void addTask(final StoragePlugin storagePlugin, final String path, final File tempFile, final String contentType) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    storagePlugin.upload(path, tempFile, contentType);
                } finally {
                    FileUtils.deleteQuietly(tempFile);
                }
            }
        });
    }

    @Override
    public boolean isValid(FileInfo.FileType fileType, MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }
        String[] uploadExtensions;
        if (fileType == FileInfo.FileType.flash) {
            uploadExtensions = Multimedia.FLASH_EXTS;
        } else if (fileType == FileInfo.FileType.media) {
            uploadExtensions = Multimedia.VIDEO_EXTS;
        } else if (fileType == FileInfo.FileType.file) {
            uploadExtensions = Multimedia.DOCS_EXTS;
        } else {
            uploadExtensions = Multimedia.IMG_EXTS;
        }
        if (!ArrayUtils.isEmpty(uploadExtensions)) {
            return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), uploadExtensions);
        }
        return false;
    }

    @Override
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile, boolean async) {
        if (multipartFile == null) {
            return null;
        }
        String uploadPath;
        if (fileType == FileInfo.FileType.flash) {
            uploadPath = ConfigUtils.config.getProperty("flashUploadPath");
        } else if (fileType == FileInfo.FileType.media) {
            uploadPath = ConfigUtils.config.getProperty("mediaUploadPath");
        } else if (fileType == FileInfo.FileType.file) {
            uploadPath = ConfigUtils.config.getProperty("fileUploadPath");
        } else {
            uploadPath = ConfigUtils.config.getProperty("imageUploadPath");
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            String path = FreemarkerUtils.process(uploadPath, model);
            String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
                File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                multipartFile.transferTo(tempFile);
                if (async) {
                    addTask(storagePlugin, destPath, tempFile, multipartFile.getContentType());
                } else {
                    try {
                        storagePlugin.upload(destPath, tempFile, multipartFile.getContentType());
                    } finally {
                        FileUtils.deleteQuietly(tempFile);
                    }
                }
                return storagePlugin.getUrl(destPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile) {
        return upload(fileType, multipartFile, false);
    }

    @Override
    public String uploadLocal(FileInfo.FileType fileType, MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        String uploadPath;
        if (fileType == FileInfo.FileType.flash) {
            uploadPath = ConfigUtils.config.getProperty("flashUploadPath");
        } else if (fileType == FileInfo.FileType.media) {
            uploadPath = ConfigUtils.config.getProperty("mediaUploadPath");
        } else if (fileType == FileInfo.FileType.file) {
            uploadPath = ConfigUtils.config.getProperty("fileUploadPath");
        } else {
            uploadPath = ConfigUtils.config.getProperty("imageUploadPath");
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            String path = FreemarkerUtils.process(uploadPath, model);
            String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            File destFile = new File(servletContext.getRealPath(destPath));
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            multipartFile.transferTo(destFile);
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
