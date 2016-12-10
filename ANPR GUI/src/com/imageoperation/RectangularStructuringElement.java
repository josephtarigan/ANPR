package com.imageoperation;

/*
 * Rectangular Structuring Element
 * It has an equilateral width and height
 * height is ((width+1)/2) - 1
 * Equilateral value is always an odd number
 * Functions are,
 * 
 * Constructor, scanImage, getStatistic & getPadding
 */

public class RectangularStructuringElement {
	
	private int equilateralValue;
	private int[][] kernel;
	private int kernelHeight;
	private int kernelHeightPadding;
	private int padding;
	private ImagePixelArray inputImage;
	private int height;
	private int width;
	
	public RectangularStructuringElement(ImagePixelArray inputImage, int equilateralValue){
		
		this.equilateralValue = equilateralValue;
		this.kernelHeight = 21; //31
		this.kernel = new int[this.kernelHeight][this.equilateralValue];
		this.inputImage = inputImage;
		this.padding = (this.equilateralValue - 1)/2;
		this.kernelHeightPadding = (this.kernelHeight - 1)/2; //15
		this.height = inputImage.getHeight();
		this.width = inputImage.getWidth();
	}
	
	/*
	 * Scan the image at given coordinate
	 * 
	 * Padding from the centre pixel = ((equiliteral value-1)/2)
	 * 
	 * Scanning starts from x-padding, y-padding
	 * Scanning ends at x+padding, y+padding
	 */
	private void scanImage(int x, int y){
		
		int k = 0, l = 0;
		
		for(int i=y-kernelHeightPadding; i<=y+kernelHeightPadding; i++){
			
			l = 0;
			
			for(int j=x-padding; j<=x+padding; j++){
				
				/*
				 * Exception for out of bound pixels
				 */
				if(i<0 || j<0){
					this.kernel[k][l] = 0;
				}
				else{
					if(i>height-1 || j>width-1){
						this.kernel[k][l] = 0;
					}
					else{
						this.kernel[k][l] = inputImage.getPixelLevel(i, j);
					}
				}
				l++;
			}
			
			k++;
		}
	}
	
	/*
	 * Check if the kernel has blank section
	 * 
	 * pixels[0] for white pixel
	 * pixels[1] for black pixel
	 */
	public int[] getStatistic(int x, int y){
		
		int pixels[] = new int[2];
		pixels[0] = 0;
		pixels[1] = 0;
		this.scanImage(x, y);
		
		for(int i=0; i<kernelHeight; i++){
			for(int j=0; j<equilateralValue; j++){
				
				if(this.kernel[i][j] > 0){
					
					pixels[0]++;
				}
				else{
					
					pixels[1]++;
				}
			}
		}
		
		return pixels;
	}
	
	/*
	 * Get the minimum pixel level in the kernel
	 * For gray image morphological operation
	 */
	public int getMinimum(int x, int y){
		
		int minimumValue;
		this.scanImage(x, y);
		
		/*
		 * set to max
		 */
		minimumValue = 255;
		
		for(int i=0; i<kernelHeight; i++){
			for(int j=0; j<equilateralValue; j++){
				
				if(this.kernel[i][j] < minimumValue){
					minimumValue = this.kernel[i][j];
				}
			}
		}
		
		return minimumValue;
	}
	
	/*
	 * Get the maximum pixel level in the kernel
	 * For gray image morphological operation
	 */
	public int getMaximum(int x, int y){
		
		int maximumValue;
		this.scanImage(x, y);
		
		/*
		 * set to 0
		 */
		maximumValue = 0;
		
		for(int i=0; i<kernelHeight; i++){
			for(int j=0; j<equilateralValue; j++){
				
				if(this.kernel[i][j] > maximumValue){
					maximumValue = this.kernel[i][j];
				}
			}
		}
		
		return maximumValue;
	}
	
	public int getPadding(){
		
		return this.padding;
	}
}
