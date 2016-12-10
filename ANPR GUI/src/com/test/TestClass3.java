package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.imageoperation.BinaryMorphological;
import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

public class TestClass3 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String inputImagePath = "src/images/input2.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray erotedDilatedImage, tophatImage, openingImage, erotedImage, inputImage, dilatedImage, constrastImage, threholdedImage1, threholdedImage2;
		
		/*
		 * ================================================================================
		 */
		
		inputImage = new ImagePixelArray(inputImageObj);
		
		/*
		 * Do erotion
		 */
		//erotedDilatedImage = BinaryMorphological.dilation(BinaryMorphological.erotion(BinaryProcessing.threshold(inputImageObj, 150), 5), 5);
		//constrastImage = Morphological.enhanceContrast(inputImage, 5);
		//erotedImage = Morphological.erotion(BinaryProcessing.threshold(inputImage, 175), 3);
		//erotedImage = BinaryMorphological.erotion(erotedImage, 7);
		
		/*
		 * Do threshold
		 */
		//threholdedImage1 = BinaryProcessing.threshold(inputImage, 175);
		threholdedImage2 = BinaryProcessing.otsuThreshold(inputImage);
		erotedImage = BinaryMorphological.erotion(threholdedImage2, 3);
		tophatImage = Morphological.rectangularTopHatFilter(erotedImage, 33);
		
		//writeJPEG.write(threholdedImage1, outputImagePath + "threshold.jpg");
		//writeJPEG.write(threholdedImage2, outputImagePath + "otsu.jpg");
		
		writeJPEG.write(tophatImage, outputImagePath + "tophat.jpg");
		writeJPEG.write(erotedImage, outputImagePath + "tanpatophat.jpg");
		
		/*
		 * Do tophat
		 */
		//tophatImage = Morphological.rectangularTopHatFilter(erotedImage, 33);
		//tophatImage = BinaryMorphological.dilation(tophatImage, 11);
		
		/*
		 * Do opening
		 */
		//openingImage = Morphological.opening(erotedImage, 15);
		
		/*
		 * Write output for erotion
		 */
		//writeJPEG.write(erotedDilatedImage, outputImagePath + "eroted.jpg");
		
		/*
		 * Write tophat
		 */
		//writeJPEG.write(tophatImage, outputImagePath + "tophat.jpg");
		
		/*
		 * Write opening
		 */
		//writeJPEG.write(openingImage, outputImagePath + "opening.jpg");
		
		//writeJPEG.write(constrastImage, outputImagePath + "enhanced.jpg");
	}

}
