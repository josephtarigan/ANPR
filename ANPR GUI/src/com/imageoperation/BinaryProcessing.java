/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BinaryProcessing {
	
	/*
	 * traditional method thresholding
	 */
	public static ImagePixelArray threshold(BufferedImage inputImageObj, int threshold){
		
		int height = inputImageObj.getHeight();
		int width = inputImageObj.getWidth();
		ImagePixelArray outputImage = new ImagePixelArray(height, width);
		
		/*
		 * Iterate through all pixel
		 */
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				Color c = new Color(inputImageObj.getRGB(x, y));
				
				if(LevelGrabber.getGrayLevel(c.getRed(), c.getGreen(), c.getBlue()) < threshold ){
					
					outputImage.setPixelLevel(y, x, 0);
				}
				else{
					outputImage.setPixelLevel(y, x, 255);
				}
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray threshold(ImagePixelArray inputImageObj, int threshold){
		
		int height = inputImageObj.getHeight();
		int width = inputImageObj.getWidth();
		ImagePixelArray outputImage = new ImagePixelArray(height, width);
		
		/*
		 * Iterate through all pixel
		 */
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				if(inputImageObj.getPixelLevel(y, x) < threshold ){
					
					outputImage.setPixelLevel(y, x, 0);
				}
				else{
					outputImage.setPixelLevel(y, x, 255);
				}
			}
		}
		
		return outputImage;
	}
	
	/*
	 * Horizontal projection of input image
	 * Count for non 0 pixel value
	 */
	public static int[] horizontalProjection(ImagePixelArray inputImage){
		
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int[] outputProjection = new int[width];
		
		for(int x=0; x<width; x++){
			
			/*
			 * Set to 0
			 */
			outputProjection[x] = 0;
			
			/*
			 * Count
			 */
			for(int y=0; y<height; y++){
				
				if(inputImage.getPixelLevel(y, x) > 0){
					outputProjection[x]++;
				}
			}
		}
		
		return outputProjection;
	}
	
	/*
	 * Vertical projection of input image
	 * Count for non 0 pixel value
	 */
	public static int[] verticalProjection(ImagePixelArray inputImage){
		
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int[] outputProjection = new int[height];
		
		for(int y=0; y<height; y++){
			
			/*
			 * Set to 0
			 */
			outputProjection[y] = 0;
			
			/*
			 * Count
			 */
			for(int x=0; x<width; x++){
				
				if(inputImage.getPixelLevel(y, x) > 0){
					outputProjection[y]++;
				}
			}
		}
		
		return outputProjection;
	}
	
	/*
	 * Otsu thresholding
	 */
	
	public static ImagePixelArray otsuThreshold(ImagePixelArray inputImage){
		
		int[] histogram = inputImage.getHistogram();
		int total = inputImage.getHeight() * inputImage.getWidth();
	
		float sum = 0;
		for(int i=0; i<256; i++){
			sum += i * histogram[i];
		}
		
			float sumB = 0;
		    int wB = 0;
		    int wF = 0;
		 
		    float varMax = 0;
		    int threshold = 0;
		 
		    for(int i=0 ; i<256 ; i++) {
		        wB += histogram[i];
		        if(wB == 0) continue;
		        wF = total - wB;

		        if(wF == 0) break;
		 
		        sumB += (float) (i * histogram[i]);
		        float mB = sumB / wB;
		        float mF = (sum - sumB) / wF;
		 
		        float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
		 
		        if(varBetween > varMax) {
		            varMax = varBetween;
		            threshold = i;
		        }
		    }
		
		return threshold(inputImage, threshold);
	}
}
