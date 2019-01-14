package com.weeds.pand.utils.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weeds.pand.utils.PandStringUtils;

public class ImageTools {
	
	private static Logger logger = LoggerFactory.getLogger(ImageTools.class);
	
	public static void main(String[] args) {
		makeCircularImg("d://opt/12.png","30x30;60x60;120x120");
	}
	
	/***
    *
    * @param srcFilePath 源图片文件路径
    * @param circularImgSavePath 新生成的图片的保存路径，需要带有保存的文件名和后缀
    * @param targetSize 文件的边长，单位：像素，最终得到的是一张正方形的图，所以要求targetSize<=源文件的最小边长
    * @param cornerRadius 圆角半径，单位：像素。如果=targetSize那么得到的是圆形图
    * @return  文件的保存路径
    * @throws IOException
    */
   public static String makeCircularImg(String srcFilePath,String sizeArray){
	   String imgUrl = null;
		try {
			File srcFile = new File(srcFilePath);
			if(!srcFile.exists()){
				return imgUrl;
			}
			ImageIcon imageIcon = new ImageIcon(srcFilePath);
			int targetSize = imageIcon.getIconWidth();
			int cornerRadius = imageIcon.getIconHeight();
			if(targetSize>cornerRadius){//处理超宽图片
				cornerRadius = targetSize;
			}
			String circularImgName = PandStringUtils.getUUID()+".png";
			BufferedImage bufferedImage = ImageIO.read(new File(srcFilePath));
			BufferedImage circularBufferImage = roundImage(bufferedImage,targetSize,cornerRadius);
			ImageIO.write(circularBufferImage, "png", new File(srcFile.getParent()+"/"+circularImgName));
			imgUrl = circularImgName;
			
			//再生产一套指定尺寸图片   Android 60X60
			String[] sizeSplit = sizeArray.split(";");
			for (int i = 0; i < sizeSplit.length; i++) {
				String[] sa = sizeSplit[i].split("x");
				int wv = Integer.valueOf(sa[0]);
				int hv = Integer.valueOf(sa[1]);
				saveSizeImg(srcFile.getParent()+"/"+circularImgName, srcFile.getParent(), circularImgName, wv, hv);
			}
			
		} catch (Exception e) {
			logger.error("图片处理异常"+e.getMessage(),e);
		}
		return imgUrl;
   }

   private static BufferedImage roundImage(BufferedImage image, int targetSize, int cornerRadius) {
       BufferedImage outputImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
       Graphics2D g2 = outputImage.createGraphics();
       g2.setComposite(AlphaComposite.Src);
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       g2.setColor(Color.WHITE);
       g2.fill(new RoundRectangle2D.Float(0, 0, targetSize, targetSize, cornerRadius, cornerRadius));
       g2.setComposite(AlphaComposite.SrcAtop);
       g2.drawImage(image, 0, 0, null);
       g2.dispose();
       return outputImage;
   }
   
   private static void saveSizeImg(String sourceImgPath,String porder,String imgName,int width,int heigth){
	   	try {
	   		BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceImgPath));
			Image bi =ImageIO.read(in);
			BufferedImage tag = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
			//绘制改变尺寸后的图
			tag.getGraphics().drawImage(bi, 0, 0,width, heigth, null);  
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(porder+"/"+imgName+"_"+width+"x"+heigth));
			ImageIO.write(tag, "PNG",out);
			in.close();
			out.close();
		} catch (Exception e) {
			logger.error("生成指定图片异常"+e.getMessage(),e);
		}
   }

}
