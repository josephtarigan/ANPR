package com.imageoperation;


//==============================
// Joseph Tarigan @ Wolf
//==============================
//
// This class will do SCW on the array of gray value
//

public class SlidingConcentricWindow {
	
	/*
	 * @InputImage is ImagePixelArray object
	 * @x is the padding of x axis
	 * @y is the padding of y axis
	 * 
	 */
	public static ImagePixelArray process(ImagePixelArray inputImage, int x, int y, double threshold){
		
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		
		int x1 = x;
		int x2 = 2*x;
		int y1 = y;
		int y2 = 2*y;
		
		double meanBox1 = 0;
		double meanBox2 = 0;
		
		ImagePixelArray outputImage = new ImagePixelArray(height, width);
		
		/*
		 * Do the statistical measurement
		 */
		for(int k=0; k<height; k++){
			for(int l=0; l<width; l++){
				
				meanBox1 = 0;
				meanBox2 = 0;
				
				/*
				 * OuterBox
				 */
				for(int m=k-y2; m<=k+y2; m++){
					for(int n=l-x2; n<=l+x2; n++){
						
						/*
						 * Inner Box
						 */
						if((m <= k+y1 && m >= k-y1) || (n <= l+x1 && n >= l-x1)){
							
							if((m < 0 || n < 0)){
								
								meanBox1 += 0;
							}
							else if(m >= height || n >= width){
								
								meanBox1 += 0;
							}
							else{
								
								meanBox1 += Math.pow(inputImage.getPixelLevel(m, n), 2);
							}
						}
						else{
							
							/*
							 * Outer Box 
							 */
							if((m < 0 || n < 0)){
								
								meanBox2 += 0;
							}
							else if(m >= height || n >= width){
								
								meanBox2 += 0;
							}
							else{
								
								meanBox2 += Math.pow(inputImage.getPixelLevel(m, n), 2);
							}
						}
						
					}
				}
				
				meanBox2 = Math.sqrt(meanBox2 / ((x2*y2) - (x1*y1)));
				meanBox1 = Math.sqrt(meanBox1 / (x1*y1));
				
				if((meanBox2 / meanBox1) > threshold){
					
					outputImage.setPixelLevel(k, l, 255);
				}
				else{
					
					outputImage.setPixelLevel(k, l, 0);
				}
			}
		}
		
		return outputImage;
	}
}
