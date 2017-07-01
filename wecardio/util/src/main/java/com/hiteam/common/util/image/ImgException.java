package com.hiteam.common.util.image;

/**
 * 图像处理异常
 */
public class ImgException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4697523731943726032L;

	public ImgException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ImgException(String msg) {
		super(msg);
	}
}
