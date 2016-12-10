/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.imageoperation;

public class BinaryMorphological {

	/*
	 * @inputImage is the inputImage object
	 * @structuringElement is the equilateral value of the structuring element
	 */
	public static ImagePixelArray erotion(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		StructuringElement SE = new StructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				if(SE.getStatistic(x, y)[0] == 0){
					outputImage.setPixelLevel(y, x, 0);
				}
				else if(SE.getStatistic(x, y)[0] > (structuringElement*structuringElement) - 1){ //minus the center pixel
					outputImage.setPixelLevel(y, x, 255);
				}
				else{
					outputImage.setPixelLevel(y, x, 0);
				}
			}
		}
		
		return outputImage;
	}
	
	/*
	 * Do the dilation
	 */
	public static ImagePixelArray dilation(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		StructuringElement SE = new StructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				if(SE.getStatistic(x, y)[0] == 0){
					outputImage.setPixelLevel(y, x, 0);
				}
				else{
					outputImage.setPixelLevel(y, x, 255);
				}
			}
		}
		
		return outputImage;
	}
}
