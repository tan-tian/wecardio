package com.hiteam.common.util;

import com.hiteam.common.util.image.ImgHandler;
import com.hiteam.common.util.image.JavaImgHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * 多媒体相关操作类
 */
public abstract class Multimedia {

	public final static ImgHandler imgHandler = new JavaImgHandler();
	
	/**
	 * 所有支持的图片MIME TYPES,其他格式的图片不做支持
	 */
	public final static String[] IMG_EXTS = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};
	
	/**
	 * 所有支持的音频MIME TYPES
	 */
	public final static String[] AUDIO_EXTS = new String[] {"wma", "mp3", "arm", "mid", "aac", "imy"};
	
	/**
	 * 所有支持的视频MIME TYPES
	 */
	public final static String[] VIDEO_EXTS = new String[] {"avi", "rm", "3gp", "wmv", "mpg", "asf","mp4"};
	
	/**
	 * FLASH
	 */
	public final static String[] FLASH_EXTS = new String[] {"swf"};

	/**
	 * 文档类型
	 */
	public final static String[] DOCS_EXTS = new String[] {"DCM","EVT","INF",
														"txt", "htm", "html", "pdf", "doc", "rtf", "xls", "ppt"};
	
	public final static HashMap<String, String> MIME_TYPES = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6697381094303577006L;

		{
			put("jar", "application/java-archive");
			put("jad", "text/vnd.sun.j2me.app-descriptor");
			put("sis", "application/vnd.symbian.install");
			put("sisx", "x-epoc/x-sisx-app");
			put("thm", "application/vnd.eri.thm");
			put("nth", "application/vnd.nok-s40theme");
			put("zip", "application/zip");
			put("rar", "application/octet-stream");
			put("cab", "application/octet-stream");

			put("gif", "image/gif");
			put("jpg", "image/jpeg");
			put("jpeg", "image/jpeg");
			put("png", "image/png");
			put("bmp", "image/bmp");

			put("avi", "video/x-msvideo");
			put("rm", "application/vnd.rn-realmedia");
			put("3gp", "video/3gpp");
			put("wmv", "video/x-ms-wmv");
			put("mpg", "video/mpg");
			put("asf", "video/x-ms-asf");
			put("flv", "video/x-flv");
			put("mp4", "video/mp4");

			put("wma", "audio/x-ms-wma");
			put("mp3", "audio/mp3");
			put("arm", "audio/amr");
			put("mid", "audio/x-midi");
			put("aac", "audio/aac");
			put("imy", "audio/imelody");

			put("swf", "application/x-shockwave-flash");

			put("txt", "text/plain");
			put("htm", "text/html");
			put("html", "text/html");
			put("pdf", "application/pdf");
			put("doc", "application/msword");
			put("rtf", "application/msword");
			put("docx", "application/msword");
			put("xls", "application/vnd.ms-excel");
			put("ppt", "application/vnd.ms-powerpoint");
			put("xlsx", "application/vnd.ms-excel");
			put("pptx", "application/vnd.ms-powerpoint");
			put("chm", "application/octet-stream");
			//holter设备上传的文件格式
			put("DCM", "");
			put("EVT", "");
			put("INF", "application/application/inf");
		}
	};

	
	/**
	 * 返回图片大小
	 * @param img
	 * @return [宽, 高]
	 * @throws IOException
	 */
	public static int[] getImageSize(File img) throws IOException {
		BufferedImage bi = ImageIO.read(img);
		return new int[] {bi.getWidth(), bi.getHeight()};
	}
	
	/**
	 * 保存图片
	 * @param img
	 * @param destPath
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void saveImage(File img, String destPath, int width, int height) throws IOException {
		imgHandler.scale(img, new File(destPath), width, height);
	}
	
	/**
	 * 保存图片
	 * @param img
	 * @param dest
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 * @param objWidth
	 * @param objHeight
	 * @throws IOException
	 */
	public static void saveImage(File img, String dest, int left, int top,
			int width, int height, int objWidth, int objHeight) throws IOException {
		imgHandler.crop(img, new File(dest), left, top, width, height, objWidth, objHeight);
	}
	
	/**
	 * 保存原图
	 * @param img
	 * @param destPath
	 * @return
	 * @throws IOException
	 */
	public static int[] saveImage(File img, String destPath) throws IOException {
		File dest = new File(destPath);
		
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		
		String ext = FilenameUtils.getExtension(dest.getName()).toLowerCase();
		BufferedImage bi = ImageIO.read(img);
		
		if (ImageIO.write(bi, ext.equals("png") ? "png" : "jpeg", dest)) {
			return new int[] {bi.getWidth(), bi.getHeight()};
		}
		
		return null;
	}
	
	public static class Size {
		public int width;
		public int height;
		
		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
	
	/**
	 * 是否图片文件
	 * @param path
	 * @return
	 */
	public static boolean isImageFile(String path) {
		String ext = FilenameUtils.getExtension(path);
		
		if (StringUtils.isBlank(ext)) {
			return false;
		} else {
			return ArrayUtils.contains(IMG_EXTS, ext.toLowerCase());
		}
	}
	
	/**
	 * 是否音频文件
	 * @param path
	 * @return
	 */
	public static boolean isAudioFile(String path) {
		String ext = FilenameUtils.getExtension(path);
		
		if (StringUtils.isBlank(ext)) {
			return false;
		} else {
			return ArrayUtils.contains(AUDIO_EXTS, ext.toLowerCase());
		}
	}
	
	/**
	 * 是否视频文件
	 * @param path
	 * @return
	 */
	public static boolean isVideoFile(String path) {
		String ext = FilenameUtils.getExtension(path);
		
		if (StringUtils.isBlank(ext)) {
			return false;
		} else {
			return ArrayUtils.contains(VIDEO_EXTS, ext.toLowerCase());
		}
	}
	
	/**
	 * 是否FLASH文件
	 * @param path
	 * @return
	 */
	public static boolean isFlashFile(String path) {
		String ext = FilenameUtils.getExtension(path);
		
		if (StringUtils.isBlank(ext)) {
			return false;
		} else {
			return ArrayUtils.contains(FLASH_EXTS, ext.toLowerCase());
		}
	}
	
	/**
	 * 是否文档
	 * @param path
	 * @return
	 */
	public static boolean isDocumentFile(String path) {
		String ext = FilenameUtils.getExtension(path);
		
		if (StringUtils.isBlank(ext)) {
			return false;
		} else {
			return ArrayUtils.contains(DOCS_EXTS, ext.toLowerCase());
		}
	}
}
