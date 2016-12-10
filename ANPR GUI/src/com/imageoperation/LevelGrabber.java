/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

public class LevelGrabber {

	/*
	 * Return 0-255 scale of gray level
	 * 
	 * @r is red value
	 * @g is green value
	 * @b is blue value
	 */
	public static int getGrayLevel(int r, int g, int b){
		
		return (int)(Math.abs(((double)(r * 0.30 + g * 0.59 + b * 0.11))));
	}
	
	/*
	 * Return 0.1 0.9 scale of gray level
	 * 
	 * @r is red value
	 * @g is green value
	 * @b is blue value
	 */
	public static double getNormalizedGrayLevel(int r, int g, int b){

		double temp = getGrayLevel(r, g, b)/255;
		
		if(temp < 0.1){
			return 0.1;
		}
		else if(temp > 0.9){
			return 0.9;
		}
		else{
			return temp * 0.9;
		}
	}
	
	
}
