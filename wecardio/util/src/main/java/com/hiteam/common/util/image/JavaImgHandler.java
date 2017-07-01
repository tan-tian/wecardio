package com.hiteam.common.util.image;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Java实现的图像处理类
 * @author wengsiwei
 */
public class JavaImgHandler implements ImgHandler {

    public int[] shrink(File src, File dest, int size) throws ImgException {
        try {
            BufferedImage original = ImageIO.read(src);
            int width = original.getWidth();
            int height = original.getHeight();

            if (width < size && height < size) {
                FileUtils.copyFile(src, dest);
                return new int[] {width, height};
            } else {
                double ratio = (width > height ? (double) size / width : (double) size / height);

                int w = (int) (width * ratio);
                int h = (int) (height * ratio);
                return new int[] {w, h};
            }

        } catch (IOException e) {
            throw new ImgException("Exception occur when shrink image.", e);
        }
    }

    public void scale(File src, File dest, int w, int h) throws ImgException {
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        ImageOutputStream imageOutputStream = null;
        ImageWriter imageWriter = null;
        try {
            if (w <= 0 && h <= 0) {
                FileUtils.copyFile(src, dest);
                return;
            } else {
                String ext = FilenameUtils.getExtension(dest.getName().toLowerCase());

                if ("gif".equalsIgnoreCase(ext)) {
//					GifImage gifImage = GifDecoder.decode(src);
//					GifImage newGif = GifTransformer.resize(gifImage, w, h, false);
//					GifEncoder.encode(newGif, dest);
                } else {
                    BufferedImage original = ImageIO.read(src);
                    int srcWidth = original.getWidth();
                    int srcHeight = original.getHeight();

                    /**
                     * 先进行图片裁剪，再进行缩放
                     */
                    int cropWidth = srcWidth;
                    int cropHeight = srcHeight;
                    if (srcWidth > w && srcHeight > h) {
                        if (srcWidth / w > srcHeight / h) {
                            cropWidth = (int) Math.round(((srcHeight / (h * 1.0)) * w));
                        } else {
                            cropHeight = (int) Math.round(((srcWidth / (w * 1.0)) * h));
                        }
                    } else {
                        if (w / srcWidth > h /srcHeight) {
                            cropHeight = (int) Math.round(((srcWidth / (w * 1.0)) * h));
                        } else {
                            cropWidth = (int) Math.round(((srcHeight / (h * 1.0)) * w));
                        }
                    }
                    this.crop(src, dest, 0, 0, cropWidth, cropHeight, srcWidth, srcHeight);


                    BufferedImage destImage = ImageIO.read(dest);
                    MyScaleFilter scale = new MyScaleFilter(w, h, w, h);
                    BufferedImage biScale = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                    scale.filter(destImage, biScale);

//                    ImageIO.write(biScale, ext.equals("png") ? "png" : "jpeg", dest);

                    imageOutputStream = ImageIO.createImageOutputStream(dest);
                    imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(dest.getName())).next();
                    imageWriter.setOutput(imageOutputStream);
                    ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
                    imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    imageWriteParam.setCompressionQuality((float) (100 / 100.0));
                    imageWriter.write(null, new IIOImage(biScale, null, null), imageWriteParam);
                    imageOutputStream.flush();
                }
            }
        } catch (Exception e) {
            throw new ImgException("Exception occur when scale image.", e);
        } finally {
            if (imageWriter != null) {
                imageWriter.dispose();
            }
            if (imageOutputStream != null) {
                try {
                    imageOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	/*
	public void scale(File src, File dest, int size) throws ImgException {
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		try {
			String ext = FilenameUtils.getExtension(dest.getName().toLowerCase());

			if ("gif".equalsIgnoreCase(ext)) {
//				GifImage gifImage = GifDecoder.decode(src);
//				GifImage newGif = GifTransformer.resize(gifImage, size, size, false);
//				GifEncoder.encode(newGif, dest);
			} else {
				BufferedImage original = ImageIO.read(src);
				int width = original.getWidth();
				int height = original.getHeight();
				int maxSize = Math.min(width, height);

				BufferedImage biCrop = newImg(original, maxSize, maxSize);
				CropFilter crop = new CropFilter((width - maxSize) / 2, (height - maxSize) / 2, maxSize, maxSize);

				crop.filter(original, biCrop);

				MyScaleFilter scale = new MyScaleFilter(size, size);
				BufferedImage biScale = newImg(biCrop, size, size);

				scale.filter(biCrop, biScale);

				ImageIO.write(biScale, ext.equals("png") ? "png" : "jpeg", dest);
			}
		} catch (Exception e) {
			throw new ImgException("Exception occur when scale image.", e);
		}
	}
	*/

    public void crop(File src, File dest, int left, int top, int width, int height, int w, int h) throws ImgException {
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            String ext = FilenameUtils.getExtension(dest.getName().toLowerCase());
            BufferedImage original = ImageIO.read(src);

            height = Math.min(height, original.getHeight());
            width = Math.min(width, original.getWidth());

            if (height < 0) {
                height = original.getHeight();
            }

            if (width < 0) {
                width = original.getWidth();
            }

            top = Math.min(Math.max(0, top), original.getHeight() - height);
            left = Math.min(Math.max(0, left), original.getWidth() - width);

            if (top == 0 && left == 0 && width == original.getWidth() && height == original.getHeight()) {
                BufferedImage biScale = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Image scaleImage = original.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                Graphics2D g = biScale.createGraphics();
                g.drawImage(scaleImage, 0, 0, w, h, null);
                g.dispose();

                ImageIO.write(biScale, ext.equals("png") ? "png" : "jpeg", dest);
            } else {
                /**
                 * 先进行图片裁剪，再进行缩放
                 */
                BufferedImage biCrop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = biCrop.createGraphics();
                g.drawRenderedImage(original, AffineTransform.getTranslateInstance(-left, -top));
                g.dispose();

                BufferedImage biScale = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Image scaleImage = biCrop.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                g = biScale.createGraphics();
                g.drawImage(scaleImage, 0, 0, w, h, null);
                g.dispose();

                ImageIO.write(biCrop, ext.equals("png") ? "png" : "jpeg", dest);
            }

        } catch (Exception e) {
            throw new ImgException("Exception occur when crop image.", e);
        }
    }

	/*
	private BufferedImage newImg(BufferedImage src, int width, int height) {
		ColorModel dstCM = src.getColorModel();
		return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height), dstCM.isAlphaPremultiplied(), null);
	}
	*/

    @Override
    public void mergeImgVertical(String firstImagePath, String secondImagePath, String formate, String outPath) throws ImgException {
        //读取第一张图片
        File fileOne = new File(firstImagePath);
        BufferedImage imageOne = null;
        try {
            imageOne = ImageIO.read(fileOne);
        } catch (IOException e) {
            throw new ImgException("找不到要合并的第一张图片", e);
        }
        int widthOne = imageOne.getWidth();    // 第一张图片宽度
        int heightOne = imageOne.getHeight();  // 第一张图片高度

        // 对第二张图片做相同的处理
        File fileTwo = new File(secondImagePath);
        BufferedImage imageTwo = null;
        try {
            imageTwo = ImageIO.read(fileTwo);
        } catch (IOException e) {
            throw new ImgException("找不到要合并的第二张图片", e);
        }
        int widthTwo = imageTwo.getWidth();     // 第二张图片宽度
        int heightTwo = imageTwo.getHeight();   // 第二张图片高度

        int width = widthOne > widthTwo ? widthOne : widthTwo;
        int height = heightOne + heightTwo;

        // 从图片中读取RGB
        int[] imageArrayOne = new int[widthOne * heightOne];
        imageArrayOne = imageOne.getRGB(0, 0, widthOne, heightOne, imageArrayOne, 0, widthOne);

        int[] ImageArrayTwo = new int[widthTwo * heightTwo];
        ImageArrayTwo = imageTwo.getRGB(0, 0, widthTwo, heightTwo, ImageArrayTwo, 0, widthTwo);

        // 生成新图片
        BufferedImage imageNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageNew.setRGB(0, 0, widthOne, heightOne, imageArrayOne, 0, widthOne);         // 设置上半部分的RGB
        imageNew.setRGB(0, heightOne, widthTwo, heightTwo, ImageArrayTwo, 0, widthTwo); // 设置下半部分的RGB

        int x = widthOne > widthTwo ? widthTwo : widthOne;    // 需要填充白色背景的起点横坐标，取宽度较小的图片宽
        int y = widthOne > widthTwo ? heightOne : 0;          // 需要填充白色背景的起点纵坐标
        for (int i = 0, offsetX = width - x; i < offsetX; i++) {
            for (int j = 0, offsetY = (widthOne > widthTwo ? height : heightOne) - y; j < offsetY; j++) {
                imageNew.setRGB(x + i, y + j, 0xffffff);    // 每个像素点设置为白色
            }
        }

        File outFile = new File(outPath);
        try {
            ImageIO.write(imageNew, formate, outFile);  // 写图片
        } catch (IOException e) {
            throw new ImgException("纵向合并图片失败", e);
        }
    }
}
