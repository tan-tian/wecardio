package com.borsam.service.pub;

import com.borsam.pojo.file.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service - 文件
 * Created by Sebarswee on 2015/6/30.
 */
public interface FileService {

    /**
     * 文件验证
     * @param fileType 文件类型
     * @param multipartFile 上传文件
     * @return 文件验证是否通过
     */
    public boolean isValid(FileInfo.FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传
     * @param fileType 文件类型
     * @param multipartFile 上传文件
     * @param async 是否异步
     * @return 访问URL
     */
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile, boolean async);

    /**
     * 文件上传(异步)
     * @param fileType 文件类型
     * @param multipartFile 上传文件
     * @return 访问URL
     */
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传至本地
     * @param fileType 文件类型
     * @param multipartFile 上传文件
     * @return 路径
     */
    public String uploadLocal(FileInfo.FileType fileType, MultipartFile multipartFile);
}
