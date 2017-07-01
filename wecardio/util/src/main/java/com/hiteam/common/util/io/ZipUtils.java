package com.hiteam.common.util.io;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Zip压缩工具
 * Created by Sebarswee on 2014/12/3.
 */
public class ZipUtils {

    /**
     * 不可实例化
     */
    private ZipUtils() {}

    /**
     * 压缩打包
     * @param files   需要打包文件
     * @param zipFile 目标文件
     */
    public static void packEntries(File[] files, File zipFile) {
        OutputStream out = null;
        ArchiveOutputStream os = null;
        try {
            out = new FileOutputStream(zipFile);
            os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);

            for (File file : files) {
                os.putArchiveEntry(new ZipArchiveEntry(file.getName()));
                IOUtils.copy(new FileInputStream(file), os);
                os.closeArchiveEntry();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(out);
        }
    }
}
