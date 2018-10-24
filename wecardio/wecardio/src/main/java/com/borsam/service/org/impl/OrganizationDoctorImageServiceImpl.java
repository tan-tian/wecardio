package com.borsam.service.org.impl;

import com.borsam.plugin.StoragePlugin;
import com.borsam.repository.dao.org.OrganizationDoctorImageDao;
import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.borsam.service.org.OrganizationDoctorImageService;
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
 * Service - 机构医生证书
 * Created by tantian on 2015/7/1.
 */
@Service("organizationDoctorImageServiceImpl")
public class OrganizationDoctorImageServiceImpl extends BaseServiceImpl<OrganizationDoctorImage, Long> implements OrganizationDoctorImageService {

    /** 目标扩展名 */
    private static final String DEST_EXTENSION = "jpg";
    /** 目标文件类型 */
    private static final String DEST_CONTENT_TYPE = "image/jpeg";

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Resource
    private List<StoragePlugin> storagePlugins;

    @Resource(name = "organizationDoctorImageDaoImpl")
    private OrganizationDoctorImageDao organizationDoctorImageDao;

    @Resource(name = "organizationDoctorImageDaoImpl")
    public void setBaseDao(OrganizationDoctorImageDao organizationDoctorImageDao) {
        super.setBaseDao(organizationDoctorImageDao);
    }

    /**
     * 添加图片处理任务
     * @param sourcePath 原图片上传路径
     * @param largePath 图片文件(大)上传路径
     * @param mediumPath 图片文件(小)上传路径
     * @param thumbnailPath 图片文件(缩略)上传路径
     * @param tempFile 原临时文件
     * @param contentType 原文件类型
     */
    private void addTask(final String sourcePath, final String largePath, final String mediumPath, final String thumbnailPath, final File tempFile, final String contentType) {
        try {
//            taskExecutor.execute(new Runnable() {
//                public void run() {
                    for (StoragePlugin storagePlugin : storagePlugins) {
                        if (storagePlugin.getIsEnabled()) {
                            String tempPath = System.getProperty("java.io.tmpdir");
                            File largeTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                            File mediumTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                            File thumbnailTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                            try {
                                ImgHandler imgHandler = new JavaImgHandler();
                                imgHandler.scale(tempFile, largeTempFile, Integer.parseInt(ConfigUtils.config.getProperty("largeDoctorImageWidth")), Integer.parseInt(ConfigUtils.config.getProperty("largeDoctorImageHeight")));
                                imgHandler.scale(tempFile, mediumTempFile, Integer.parseInt(ConfigUtils.config.getProperty("mediumDoctorImageWidth")), Integer.parseInt(ConfigUtils.config.getProperty("mediumDoctorImageHeight")));
                                imgHandler.scale(tempFile, thumbnailTempFile, Integer.parseInt(ConfigUtils.config.getProperty("thumbnailDoctorImageWidth")), Integer.parseInt(ConfigUtils.config.getProperty("thumbnailDoctorImageHeight")));
                                storagePlugin.upload(sourcePath, tempFile, contentType);
                                storagePlugin.upload(largePath, largeTempFile, DEST_CONTENT_TYPE);
                                storagePlugin.upload(mediumPath, mediumTempFile, DEST_CONTENT_TYPE);
                                storagePlugin.upload(thumbnailPath, thumbnailTempFile, DEST_CONTENT_TYPE);
                            } finally {
                                FileUtils.deleteQuietly(tempFile);
                                FileUtils.deleteQuietly(largeTempFile);
                                FileUtils.deleteQuietly(mediumTempFile);
                                FileUtils.deleteQuietly(thumbnailTempFile);
                            }
                        }
                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void build(OrganizationDoctorImage doctorImage) {
        String  filePath = doctorImage.getFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("uuid", UUID.randomUUID().toString());
                String uploadPath = FreemarkerUtils.process(ConfigUtils.config.getProperty("imageUploadPath"), model);
                String uuid = UUID.randomUUID().toString();
                String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(filePath);
                String largePath = uploadPath + uuid + "-large." + DEST_EXTENSION;
                String mediumPath = uploadPath + uuid + "-medium." + DEST_EXTENSION;
                String thumbnailPath = uploadPath + uuid + "-thumbnail." + DEST_EXTENSION;

                for (StoragePlugin storagePlugin : storagePlugins) {
                    if (storagePlugin.getIsEnabled()) {
                        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
                        if (!tempFile.getParentFile().exists()) {
                            tempFile.getParentFile().mkdirs();
                        }
                        File source = new File(ConfigUtils.config.getProperty("uploadPath"), filePath);
                        FileUtils.copyFile(source, tempFile);
                        addTask(sourcePath, largePath, mediumPath, thumbnailPath, tempFile, Multimedia.MIME_TYPES.get(FilenameUtils.getExtension(source.getName())));
                        doctorImage.setSource(sourcePath);
                        doctorImage.setLarge(largePath);
                        doctorImage.setMedium(mediumPath);
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
    public void removeImages(Long oid) {
        organizationDoctorImageDao.removeImages(oid);
    }
}
