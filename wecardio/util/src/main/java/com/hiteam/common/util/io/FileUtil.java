package com.hiteam.common.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-12 10:53
 * </pre>
 */
public class FileUtil extends FileUtils {
	/**
	 * 创建临时文件
	 *
	 * @param inputStream
	 * @param name
	 *            文件名
	 * @param ext
	 *            扩展名
	 * @return
	 * @throws java.io.IOException
	 */
	public static File createTmpFile(InputStream inputStream, String name,
			String ext) throws IOException {
		FileOutputStream fos = null;
		try {
			File tmpFile = File.createTempFile(name, '.' + ext);
			tmpFile.deleteOnExit();
			fos = new FileOutputStream(tmpFile);
			int read = 0;
			byte[] bytes = new byte[1024 * 100];
			while ((read = inputStream.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
			fos.flush();
			return tmpFile;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 获取类路径
	 * 
	 * @return
	 */
	public static String getClassPath() {
		return FileUtil.class.getClassLoader().getResource("").getPath();
	}

	/**
	 * 解析文件路径表达式为文件对象，支持classpath:表达式来获取类路径下的文件
	 * 
	 * @param filePath
	 *            文件路径表达式，例如："abc.txt"、"classpath:abc.txt"
	 * @return
	 */
	public static File parseFile(String filePath) {
		File file = null;

		if (filePath != null) {
			if (filePath.toLowerCase().startsWith("classpath:")) {// 类路径
				filePath = getClassPath() + filePath.substring(10);
			}

			file = new File(filePath);
		}

		return file;
	}
}
