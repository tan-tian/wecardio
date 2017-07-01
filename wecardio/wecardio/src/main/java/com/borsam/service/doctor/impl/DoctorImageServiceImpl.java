package com.borsam.service.doctor.impl;

import com.borsam.plugin.StoragePlugin;
import com.borsam.repository.dao.doctor.DoctorImageDao;
import com.borsam.repository.entity.doctor.DoctorImage;
import com.borsam.service.doctor.DoctorImageService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.image.ImgHandler;
import com.hiteam.common.util.image.JavaImgHandler;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service - 医生资质证书
 * Created by Sebarswee on 2015/7/14.
 */
@Service("doctorImageServiceImpl")
public class DoctorImageServiceImpl extends BaseServiceImpl<DoctorImage, Long> implements DoctorImageService {

    /** 目标扩展名 */
    private static final String DEST_EXTENSION = "jpg";
    /** 目标文件类型 */
    private static final String DEST_CONTENT_TYPE = "image/jpeg";

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Resource
    private List<StoragePlugin> storagePlugins;

    @Resource(name = "doctorImageDaoImpl")
    DoctorImageDao doctorImageDao;

    @Resource(name = "doctorImageDaoImpl")
    public void setBaseDao(DoctorImageDao doctorImageDao) {
        super.setBaseDao(doctorImageDao);
    }

    /**
     * 添加图片处理任务
     * @param sourcePath 原图片上传路径
     * @param thumbnailPath 图片文件(缩略)上传路径
     * @param tempFile 原临时文件
     * @param contentType 原文件类型
     */
    private void addTask(final String sourcePath, final String thumbnailPath, final File tempFile, final String contentType) {
        try {
            taskExecutor.execute(new Runnable() {
                public void run() {
                    for (StoragePlugin storagePlugin : storagePlugins) {
                        if (storagePlugin.getIsEnabled()) {
                            String tempPath = System.getProperty("java.io.tmpdir");
                            File thumbnailTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                            try {
                                ImgHandler imgHandler = new JavaImgHandler();
                                imgHandler.scale(tempFile, thumbnailTempFile, Integer.parseInt(ConfigUtils.config.getProperty("thumbnailCertificateImageWidth")), Integer.parseInt(ConfigUtils.config.getProperty("thumbnailCertificateImageHeight")));
                                storagePlugin.upload(sourcePath, tempFile, contentType);
                                storagePlugin.upload(thumbnailPath, thumbnailTempFile, DEST_CONTENT_TYPE);
                            } finally {
                                FileUtils.deleteQuietly(tempFile);
                                FileUtils.deleteQuietly(thumbnailTempFile);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void build(DoctorImage doctorImage) {
        String  filePath = doctorImage.getFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("uuid", UUID.randomUUID().toString());
                String uploadPath = FreemarkerUtils.process(ConfigUtils.config.getProperty("imageUploadPath"), model);
                String uuid = UUID.randomUUID().toString();
                String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(filePath);
                String thumbnailPath = uploadPath + uuid + "-thumbnail." + DEST_EXTENSION;

                for (StoragePlugin storagePlugin : storagePlugins) {
                    if (storagePlugin.getIsEnabled()) {
                        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
                        if (!tempFile.getParentFile().exists()) {
                            tempFile.getParentFile().mkdirs();
                        }
                        File source = new File(ConfigUtils.config.getProperty("uploadPath"), filePath);
                        FileUtils.copyFile(source, tempFile);
                        addTask(sourcePath, thumbnailPath, tempFile, Multimedia.MIME_TYPES.get(FilenameUtils.getExtension(source.getName())));
                        doctorImage.setSource(sourcePath);
                        doctorImage.setThumbnail(thumbnailPath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void removeImages(Long did) {
        doctorImageDao.removeImages(did);
    }
}
