package com.hiteam.common.util.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class MyScaleFilter {
	private int width;
	private int height;
    private int destWidth;
    private int destHeight;

	public MyScaleFilter() {
		this(32, 32, 32, 32);
	}

    public MyScaleFilter(int width, int height, int destWidth, int destHeight) {
        this.width = width;
        this.height = height;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
    }

	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		if (dest == null) {
			ColorModel dstCM = src.getColorModel();
			dest = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height), dstCM.isAlphaPremultiplied(), null);
		}

		Image scaleImage = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		Graphics2D g = dest.createGraphics();
//        g.setBackground(Color.white);
//        g.clearRect(0, 0, destWidth, destHeight);
		g.drawImage(scaleImage, 0, 0, width, height, null);
//		g.dispose();

		return dest;
	}

}
