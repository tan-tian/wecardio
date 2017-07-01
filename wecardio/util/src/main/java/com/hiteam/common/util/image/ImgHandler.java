package com.hiteam.common.util.image;

import java.io.File;

public interface ImgHandler {
    /**
     * 根据比例缩小图像到指定的方框中
     * @param src	源文件
     * @param dest	目标文件
     * @param size	方框大小
     * @return 宽、高
     * @throws ImgException
     */
    public int[] shrink(File src, File dest, int size) throws ImgException;

    /**
     * 图像缩放
     * @param src 	源文件
     * @param dest 	目标文件
     * @param w 	缩放宽度
     * @param h 	缩放高度
     * @throws ImgException
     */
    public void scale(File src, File dest, int w, int h) throws ImgException;

    /**
     * 将图像缩放到某个正方形框内
     * @param src 	源文件
     * @param dest	目标文件
     * @param size	正方形大小
     * @exception
     */
//	public void scale(File src, File dest, int size) throws ImgException;

    /**
     * 进行图像剪裁
     * @param src		源文件
     * @param dest		目标文件
     * @param left		剪裁部分的左上角x轴
     * @param top		剪裁部分的左上角y轴
     * @param width		剪裁部分的宽度
     * @param height	剪裁部分的高度
     * @param w			目标大小宽度
     * @param h			目标大小高度
     * @throws ImgException
     */
    public void crop(File src, File dest, int left, int top, int width, int height, int w, int h) throws ImgException;

    /**
     * 图像旋转
     *
     * @param src		源文件
     * @param dest		目标文件
     * @param degrees	旋转度数
     * @throws ImgException
     */
//	public abstract void rotate(File src, File dest, double degrees) throws ImgException;

    /**
     * 纵向合并两张图片
     * @param firstImagePath    第一张图片路径
     * @param secondImagePath   第二张图片路径
     * @param formate           输出格式
     * @param outPath           输出路径
     * @throws ImgException
     */
    public void mergeImgVertical(String firstImagePath, String secondImagePath, String formate, String outPath) throws ImgException;
}

