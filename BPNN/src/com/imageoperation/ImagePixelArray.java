/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImagePixelArray {

	private  int[][] pixelLevel;
	private int height;
	private int width;
	private String identifier;
	
	/*
	 * Constructor
	 * 
	 * @height is the height of the image
	 * @width is the width of the image
	 */
	public ImagePixelArray(int height, int width){
		
		this.height = height;
		this.width = width;
		
		/*
		 * Initialisation
		 */
		this.init();
	}
	
	/*
	 * Overloads
	 */
	public ImagePixelArray(int height, int width, int[][] pixelLevel){
		
		this(height, width);
		this.insertPixelLevels(pixelLevel);
	}
	
	/*
	 * Overloads
	 */
	public ImagePixelArray(BufferedImage inputImageobj){
		
		this.height = inputImageobj.getHeight();
		this.width = inputImageobj.getWidth();
		
		/*
		 * Init
		 */
		this.init();
		
		/*
		 * Iterate through all pixel
		 */
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				Color c = new Color(inputImageobj.getRGB(x, y));
				this.pixelLevel[y][x] = LevelGrabber.getGrayLevel(c.getRed(), c.getGreen(), c.getBlue());
			}
		}
	}
	
	/*
	 * Insert definite pixel level into array of pixels level
	 */
	public void insertPixelLevels(int[][] pixelLevel){
		
		this.pixelLevel = pixelLevel;
	}
	
	/*
	 * Initialisation
	 * Construct the array of the pixels
	 */
	private void init(){
		
		this.pixelLevel = new int[height][width];
	}
	
	/*
	 * Return the level of the pixel on the specified location
	 */
	public int getPixelLevel(int y, int x){
		
		return this.pixelLevel[y][x];
	}
	
	/*
	 * Get normalised pixel level
	 * For training use
	 */
	public double getNormalizedPixelLevel(int y, int x){
		
		return Math.abs((double)this.pixelLevel[y][x]/(double)255);
	}
	
	/*
	 * Return serialised pixel level
	 * Used for neural networks input nodes
	 */
	public int[] getSerializedPixelLevels(){
		
		int[] serializedValues = new int[this.height*this.width];
		int i = 0;
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				serializedValues[i] = this.pixelLevel[y][x];
				i++;
			}
		}
		
		return serializedValues;
	}
	
	/*
	 * Return serialised normalised pixel level
	 * Used for neural networks input nodes
	 */
	public double[] getSerializedNormalizedPixelLevels(){
		
		double[] serializedValues = new double[this.height*this.width];
		int i = 0;
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				serializedValues[i] = this.getNormalizedPixelLevel(y, x);
				i++;
			}
		}
		
		return serializedValues;
	}
	
	
	/*
	 * Return all pixel levels
	 */
	public int[][] getPixelLevels(){
		
		return this.pixelLevel;
	}
	
	/*
	 * Return height
	 */
	public int getHeight(){
		
		return this.height;
	}
	
	/*
	 * Return width
	 */
	public int getWidth(){
		
		return this.width;
	}
	
	/*
	 * Set individual pixel level
	 */
	public void setPixelLevel(int y, int x, int level){
		
		this.pixelLevel[y][x] = level;
	}
	
	/*
	 * Convert into BufferedImage
	 */
	public BufferedImage convertIntoBufferedImage(){
		
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		int r, g, b;
		
		for(int y=0; y<this.height; y++){
			for(int x=0; x<this.width; x++){
				
				r = g = b = (int) (this.getPixelLevel(y, x) * 0.299 + this.getPixelLevel(y, x) * 0.587 + this.getPixelLevel(y, x) * 0.114);
				Color c = new Color(r, g, b);
				outputImage.setRGB(x, y, c.getRGB());
			}
		}
		
		return outputImage;
	}
	
	/*
	 * Get histogram
	 */
	public int[] getHistogram(){
		
		/*
		 * 8bit
		 */
		int histogram[] = new int[256];
		
		for(int x=0; x<this.height; x++){
			for(int y=0; y<this.width; y++){
				
				histogram[this.pixelLevel[x][y]]++;
			}
		}
		
		return histogram;
	}
	
	/*
	 * Mean Gray Value
	 */
	public int getMeanGrayValue(){
		
		int mean, totalIntensity = 0;
		
		for(int x=0; x<this.height; x++){
			for(int y=0; y<this.width; y++){
				
				totalIntensity += this.pixelLevel[x][y];
			}
		}
		
		mean = totalIntensity/(this.height*this.width);
		
		return mean;
	}
	
	/*
	 * set identifier
	 */
	public void setIdentifier(String identifier){
		
		this.identifier = identifier;
	}
	
	/*
	 * get identifier
	 */
	public String getIdentifier(){
		
		return this.identifier;
	}
}
