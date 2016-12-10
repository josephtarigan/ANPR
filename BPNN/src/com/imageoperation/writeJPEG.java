/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class writeJPEG {

	/*
	 * Write levels data into a gray scale image
	 * 
	 * @inputData is the input levels of the image
	 * @outputPath is the output path 
	 */
	public static boolean write(ImagePixelArray inputData, String outputPath){
		
		BufferedImage outputObj =  new BufferedImage(inputData.getWidth(), inputData.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		File outputFile = new File(outputPath);
		int r, g, b;
		
		for(int y=0; y<inputData.getHeight(); y++){
			for(int x=0; x<inputData.getWidth(); x++){
				
				r = g = b = (int) (inputData.getPixelLevel(y, x) * 0.299 + inputData.getPixelLevel(y, x) * 0.587 + inputData.getPixelLevel(y, x) * 0.114);
				Color c = new Color(r, g, b);
				outputObj.setRGB(x, y, c.getRGB());
			}
		}
		
		try{
			ImageIO.write(outputObj, "jpg", outputFile);
			return true;
		}catch(IOException e){
			return false;
		}
	}
}
