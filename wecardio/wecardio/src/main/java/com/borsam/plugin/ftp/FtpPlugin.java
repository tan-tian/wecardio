package com.borsam.plugin.ftp;

import com.borsam.plugin.StoragePlugin;
import com.borsam.pojo.file.FileInfo;
import com.hiteam.common.util.ConfigUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin - FTP存储
 * Created by Sebarswee on 2015/6/30.
 */
public class FtpPlugin extends StoragePlugin {

    @Override
    public boolean getIsEnabled() {
        return Boolean.valueOf(ConfigUtils.config.getProperty("isFtpPluginEnable"));
    }

    @Override
    public void upload(String path, File file, String contentType) {
        String host = ConfigUtils.config.getProperty("ftpHost");
        Integer port = Integer.valueOf(ConfigUtils.config.getProperty("ftpPort"));
        String username = ConfigUtils.config.getProperty("ftpUsername");
        String password = ConfigUtils.config.getProperty("ftpPassword");
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                String directory = StringUtils.substringBeforeLast(path, "/");
                String filename = StringUtils.substringAfterLast(path, "/");
                if (!ftpClient.changeWorkingDirectory(directory)) {
                    String[] paths = StringUtils.split(directory, "/");
                    String p = "/";
                    ftpClient.changeWorkingDirectory(p);
                    for (String s : paths) {
                        p += s + "/";
                        if (!ftpClient.changeWorkingDirectory(p)) {
                            ftpClient.makeDirectory(s);
                            ftpClient.changeWorkingDirectory(p);
                        }
                    }
                }
                ftpClient.storeFile(filename, inputStream);
                ftpClient.logout();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getUrl(String path) {
        String urlPrefix = ConfigUtils.config.getProperty("ftpUrlPrefix");
        return urlPrefix + path;
    }

    @Override
    public List<FileInfo> browser(String path) {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        String host = ConfigUtils.config.getProperty("ftpHost");
        Integer port = Integer.valueOf(ConfigUtils.config.getProperty("ftpPort"));
        String username = ConfigUtils.config.getProperty("ftpUsername");;
        String password = ConfigUtils.config.getProperty("ftpPassword");
        String urlPrefix = ConfigUtils.config.getProperty("ftpUrlPrefix");
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()) && ftpClient.changeWorkingDirectory(path)) {
                for (FTPFile ftpFile : ftpClient.listFiles()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setName(ftpFile.getName());
                    fileInfo.setUrl(urlPrefix + path + ftpFile.getName());
                    fileInfo.setIsDirectory(ftpFile.isDirectory());
                    fileInfo.setSize(ftpFile.getSize());
                    fileInfo.setLastModified(ftpFile.getTimestamp().getTime());
                    fileInfos.add(fileInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileInfos;
    }
}
