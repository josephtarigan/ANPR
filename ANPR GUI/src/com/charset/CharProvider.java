/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.charset;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.imageoperation.*;

public class CharProvider {

	private Alphanumeric alphaNumeric;
	private ImagePixelArray[][] charPixelsArray;
	/*
	 * Debug
	 */
	String a;
	
	public CharProvider() throws IOException{
		
		/*
		 * Do all things
		 */
		this.alphaNumeric = new Alphanumeric();
		this.init();
	}
	
	private void init() throws IOException{
		
		this.charPixelsArray = new ImagePixelArray[CharConfig.CHARSET_COUNT][CharConfig.CHAR_COUNT];
		
		/*
		 * Iterate through sets
		 */
		for(int j=0; j<CharConfig.CHARSET_COUNT; j++){
			
			/*
			 * Iterate through all char
			 */
			for(int i=0; i<CharConfig.CHAR_COUNT; i++){
			
				/*
				 * Setting up
				 */
				int set = j+1;
				charPixelsArray[j][i] = new ImagePixelArray(CharConfig.CHAR_HEIGHT, CharConfig.CHAR_WIDTH);
				File fileObj = new File(CharConfig.CHARS_PATH + set + "/" + alphaNumeric.character[i].getCharacter() + CharConfig.CHARS_FILETYPE);
				BufferedImage imageObj = ImageIO.read(fileObj);
				
				/*
				 * Read image pixel by pixel
				 * Get the level and store it into ImagePixelArray object
				 */
				for(int y=0; y<CharConfig.CHAR_HEIGHT; y++){
					for(int x=0; x<CharConfig.CHAR_WIDTH; x++){
						
						Color c = new Color(imageObj.getRGB(x, y));
						charPixelsArray[j][i].setPixelLevel(y, x, LevelGrabber.getGrayLevel(c.getRed(), c.getGreen(), c.getBlue()));
					}
				}
			}
		}
	}
	
	/*
	 * Return individual ImagepixelArray
	 * 
	 * @j is the set number
	 * @i is the char number
	 */
	public ImagePixelArray getChar(int j, int i){
		
		return this.charPixelsArray[j][i];
	}
	
	/*
	 * Return all ImagePixelArray
	 */
	public ImagePixelArray[][] getChars(){
		
		return this.charPixelsArray;
	}
}
