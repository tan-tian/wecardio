package com.hiteam.common.service;

import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;

/**
 * 验证码生成器
 */
public class CaptchaEngine extends ListImageCaptchaEngine {
	
	private static final int IMAGE_WIDTH = 80;												// 图片宽度
	private static final int IMAGE_HEIGHT = 28;												// 图片高度
	private static final int MIN_FONT_SIZE = 14;											// 字体最小值
	private static final int MAX_FONT_SIZE = 16;											// 字体最大值
	private static final int MIN_WORD_LENGTH = 4;											// 验证码最小长度
	private static final int MAX_WORD_LENGTH = 4;											// 验证码最大长度
	private static final String BACKGROUND_IMAGE_PATH = "captcha/";		// 背景图片位置
	private static final String CHAR_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";					// 验证码随机内容
	
	/**
	 * 随机字体
	 */
	private static final Font[] FONTS = {
		new Font("nyala", Font.BOLD, MAX_FONT_SIZE), 
		new Font("Arial", Font.BOLD, MAX_FONT_SIZE), 
		new Font("nyala", Font.BOLD, MAX_FONT_SIZE), 
		new Font("Bell", Font.BOLD, MAX_FONT_SIZE), 
		new Font("Bell MT", Font.BOLD, MAX_FONT_SIZE), 
		new Font("Credit", Font.BOLD, MAX_FONT_SIZE), 
		new Font("valley", Font.BOLD, MAX_FONT_SIZE), 
		new Font("Impact", Font.BOLD, MAX_FONT_SIZE) 
	};
	
	/**
	 * 随机颜色
	 */
	private static final Color[] COLORS = {
		new Color(255, 255, 255), 
		new Color(255, 220, 220), 
		new Color(220, 255, 255), 
		new Color(220, 220, 255), 
		new Color(255, 255, 220), 
		new Color(220, 255, 220)
	};

	/**
	 * 验证码生成
	 */
	@Override
	protected void buildInitialFactories() {
		RandomFontGenerator fontGenerator = new RandomFontGenerator(Integer.valueOf(MIN_FONT_SIZE), Integer.valueOf(MAX_FONT_SIZE), FONTS);
		FileReaderRandomBackgroundGenerator backgroundGenerator = new FileReaderRandomBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Thread.currentThread().getContextClassLoader().getResource(BACKGROUND_IMAGE_PATH).getPath());
		DecoratedRandomTextPaster textPaster = new DecoratedRandomTextPaster(Integer.valueOf(MIN_WORD_LENGTH), Integer.valueOf(MAX_WORD_LENGTH), new RandomListColorGenerator(COLORS), new TextDecorator[0]);
		addFactory(new GimpyFactory(new RandomWordGenerator(CHAR_STRING), new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster)));
	}

}
