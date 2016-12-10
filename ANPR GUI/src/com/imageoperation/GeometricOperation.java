package com.imageoperation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GeometricOperation {

    public static BufferedImage resize(BufferedImage inputImage, int height, int width){
    	
	    int inputWidth = inputImage.getWidth();
	    int inputHeight = inputImage.getHeight();
	    
	    BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
	    Graphics2D g2d = inputImage.createGraphics();
	    
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.drawImage(inputImage, 0, 0, width, height, 0, 0, inputWidth, inputHeight, null);
	    g2d.dispose();
	    
	    return outputImage;      
   }
}
