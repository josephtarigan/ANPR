package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.writeJPEG;

public class TestClass8 {

	/**
	 * Adaptive mean thresholding testing
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String inputImagePath = "src/images/input2.jpg";
		String outputImagePath = "src/images/output/thresholded.jpg";
		
		File inputFile = new File(inputImagePath);
		
		BufferedImage inputObj = ImageIO.read(inputFile);
		
		ImagePixelArray inputImage = new ImagePixelArray(inputObj);
		ImagePixelArray thresholded;
		
		
		thresholded = BinaryProcessing.threshold(inputImage, 175);
		
		writeJPEG.write(thresholded, outputImagePath);
	}

}
