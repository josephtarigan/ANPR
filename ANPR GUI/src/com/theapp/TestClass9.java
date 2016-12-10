package com.theapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

public class TestClass9 {

	/**
	 * tophat threshold opening
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * Initial operation
		 * ==============================================================================================================================
		 */

		String inputImagePath = "src/images/input3.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray imagePixelArray = new ImagePixelArray(inputImageObj);
		ImagePixelArray erotedImage, dilatedImage, opening, closing, tophat, threshold, optimized;
		
		final double candidateThreshold = 0.18;
		
		long elapsedTime;
		
		/*
		 * Operation
		 * ==============================================================================================================================
		 */
		
		/*
		 * Go
		 */
		elapsedTime = System.currentTimeMillis();
		
		/*
		tophat = Morphological.topHatFilter(imagePixelArray, 33); //33
		threshold = BinaryProcessing.threshold(tophat, 100);
		opening = Morphological.opening(threshold, 5);
		*/
		
		threshold = BinaryProcessing.otsuThreshold(imagePixelArray);
		optimized = Morphological.optimizedRectangularTopHatFilter(threshold, 33);
		erotedImage = Morphological.erotion(optimized, 3);
		//tophat = Morphological.rectangularTopHatFilter(erotedImage, 33);
		
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		
		//writeJPEG.write(tophat, outputImagePath + "otsuerosiontophat.jpg");
		//writeJPEG.write(erotedImage, outputImagePath + "otsuerosion.jpg");
		writeJPEG.write(erotedImage, outputImagePath + "optimized.jpg");
		
		
		System.out.printf("Elapsed Time : %.2f Seconds", (float)elapsedTime/1000);
	}

}
