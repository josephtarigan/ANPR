/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Desaturater {

	/*
	 * Convert image into gray scale image
	 * Return as array of pixel level
	 */
	public static double[][] convertIntoGrayscale(BufferedImage input){
		
		double[][] outputImageArray = new double[input.getHeight()][input.getWidth()];
		
		for(int i=0; i<input.getHeight(); i++){
			for(int j=0; j<input.getWidth(); j++){
				
				Color c = new Color(input.getRGB(j, i));
				outputImageArray[i][j] = LevelGrabber.getGrayLevel(c.getRed(), c.getGreen(), c.getBlue());
			}
		}
		
		return outputImageArray;
	}
	
	/*
	 * Convert image into gray scale image
	 * Return as a gray scale BufferedImage
	 */	
	public static BufferedImage convertIntoGrayscale(BufferedImage input, boolean t){
		
		BufferedImage output = new BufferedImage(input.getHeight(), input.getWidth(), BufferedImage.TYPE_BYTE_GRAY);
		int r, g, b;
		
		for(int i=0; i<input.getHeight(); i++){
			for(int j=0; j<input.getWidth(); j++){
				
				//get RGB from input image
				Color c = new Color(input.getRGB(j, i));
				
				//get gray level
				r = g = b = LevelGrabber.getGrayLevel(c.getRed(), c.getGreen(), c.getBlue());
				
				//put into Color object
				c = new Color(r, g, b);
				
				//finally put into output object
				output.setRGB(j, i, c.getRGB());
			}
		}
		
		return output;
	}
}
