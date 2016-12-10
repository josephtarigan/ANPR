package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.SlidingConcentricWindow;
import com.imageoperation.writeJPEG;

public class TestClass6 {

	/**
	 * TestClass3 combined with TestClass2
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final double candidateThreshold = 0.16;
		final int cropThreshold = 5;
		
		String inputImagePath = "src/images/input3.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray tophatImage, erotedImage, inputImage, candidatePlate, thresholdedImage, slidingConcentricWindowImage;
		
		int height, width;
		int peak = 0;
		int padding;
		
		int[] verticalProjection;
		int[] horizontalProjection;
		
		boolean flag;
		
		long elapsedTime;
		
		/*
		 * Collection for candidate
		 */
		List<Integer> yStart, xStart;
		List<Integer> yEnd, xEnd;
		List<ImagePixelArray> candidateImage;
		List<ImagePixelArray> alphanumericCharacter;
		Iterator<ImagePixelArray> alphanumericCharacterIterator;
		Iterator<Integer> yStartIterator, yEndIterator, xStartIterator, xEndIterator;
		Iterator<ImagePixelArray> candidateIterator;
		
		/*
		 * Initialisation
		 * ======================================================================================
		 */
		
		elapsedTime = System.currentTimeMillis();
		
		inputImage = new ImagePixelArray(inputImageObj);
		thresholdedImage = BinaryProcessing.otsuThreshold(inputImage);
		erotedImage = Morphological.erotion(thresholdedImage, 3);
		//slidingConcentricWindowImage = SlidingConcentricWindow.process(erotedImage, 4, 2, 0.5);
		tophatImage = Morphological.rectangularTopHatFilter(erotedImage, 33);
		
		//writeJPEG.write(thresholdedImage, outputImagePath + "threshold.jpg");
		//writeJPEG.write(erotedImage, outputImagePath + "eroted.jpg");
		writeJPEG.write(tophatImage, outputImagePath + "tophat.jpg");
		
		/*
		height = tophatImage.getHeight();
		width = tophatImage.getWidth();
		*/
		/*
		 * Vertical Projection
		 * Calculate threshold padding
		 * ======================================================================================
		 */
		
		/*
		verticalProjection = BinaryProcessing.verticalProjection(tophatImage);
		
		peak = 0;
		for(int y=0; y<height; y++){
			
			if(verticalProjection[y] > peak){
				peak = verticalProjection[y];
			}
		}
		padding = (int)Math.floor(candidateThreshold * peak);
		*?
		
		/*
		 * Cropping Vertical Projection
		 * Using padding as threshold
		 * =======================================================================================
		 */
		
		/*
		for(int y=0; y<height; y++){
			if(verticalProjection[y] > padding){
				verticalProjection[y] = verticalProjection[y];
			}
			else{
				verticalProjection[y] = 0;
			}
		}
		*/
		
		/*
		 * Find Start & End Points
		 * From thresholded vertical projection
		 * =======================================================================================
		 */
		
		/*
		yStart = new ArrayList<Integer>();
		yEnd = new ArrayList<Integer>();
		
		flag = false;
		
		for(int y=0; y<height; y++){
			
			if(verticalProjection[y] > 0 && flag == false){
				
				flag = true;
				yStart.add(y);
			}
			else if(verticalProjection[y] < cropThreshold && flag == true){
				
				flag = false;
				yEnd.add(y);
			}
		}
		/*
		
		/*
		 * Check Height
		 * The tallest is considered as a candidate plate
		 * =======================================================================================
		 */
		
		/*
		candidateImage = new ArrayList<ImagePixelArray>();
		yStartIterator = yStart.iterator();
		yEndIterator = yEnd.iterator();
		
		while(yStartIterator.hasNext()){
			
			int start = yStartIterator.next();
			int end = yEndIterator.next();
			
			ImagePixelArray temp = new ImagePixelArray(end - start, width);
			
			for(int y=start; y<end; y++){
				for(int x=0; x<width; x++){
					
					temp.setPixelLevel(y-start, x, tophatImage.getPixelLevel(y, x));
				}
			}
			
			candidateImage.add(temp);
		}
		
		candidateIterator = candidateImage.iterator();
		candidatePlate = null;
		int max = 0;
		
		while(candidateIterator.hasNext()){
			
			ImagePixelArray temp = candidateIterator.next();
			
			if(temp.getHeight() > max){
				
				max = temp.getHeight();
				candidatePlate = temp;
			}
		}
		*/
		
		/*
		 * Horizontal Projection & Start End Points
		 * =======================================================================================
		 */
		
		/*
		horizontalProjection = BinaryProcessing.horizontalProjection(candidatePlate);
		
		flag = false;
		xStart = new ArrayList<Integer>();
		xEnd = new ArrayList<Integer>();
		
		for(int i=0; i<horizontalProjection.length; i++){
			
			if(horizontalProjection[i] > cropThreshold && flag == false){
				
				flag = true;
				xStart.add(i);
			}
			else if(horizontalProjection[i] < cropThreshold && flag == true){
				
				flag = false;
				xEnd.add(i);
			}
			else if(i == horizontalProjection.length-1 && flag == true){
				
				flag = false;
				xEnd.add(i);
			}
		}
		*/
		
		/*
		 * Segmentation, Verification & Write
		 * ========================================================================================
		 */
		
		/*
		xStartIterator = xStart.iterator();
		xEndIterator = xEnd.iterator();
		alphanumericCharacter = new ArrayList<ImagePixelArray>();
		int h = 1;

		while(xStartIterator.hasNext()){
			
			int start = xStartIterator.next();
			int end = xEndIterator.next();
			int vStart = 0, vEnd = 0;
			int candidateHeight, candidateWidth;
			double candidateRatio;
			*/
			
			/*
			 * Vertical Projection in each candidate
			 * ==========================================================================================================
			 */
			/*
			ImagePixelArray temp = new ImagePixelArray(candidatePlate.getHeight(), end-start);
			
			for(int y=0; y<candidatePlate.getHeight(); y++){
				for(int x=start; x<end; x++){
					
					temp.setPixelLevel(y, x-start, candidatePlate.getPixelLevel(y, x));
				}
			}
			
			verticalProjection = BinaryProcessing.verticalProjection(temp);
			
			flag = false;
			for(int y=0; y<verticalProjection.length; y++){
				
				if(verticalProjection[y] > cropThreshold - 3 && flag == false){
					
					flag = true;
					vStart = y;
				}
				else if(verticalProjection[y] < cropThreshold - 3 && flag == true){
					
					flag = false;
					vEnd = y;
					break;
				}
				else if(y == verticalProjection.length-1 && flag == true){
					
					flag = false;
					vEnd = y;
					break;
				}
			}
			
			ImagePixelArray tempAfter = new ImagePixelArray(vEnd - vStart, end-start);
			
			for(int y=0; y<vEnd - vStart; y++){
				for(int x=0; x<temp.getWidth(); x++){
					
					tempAfter.setPixelLevel(y, x, temp.getPixelLevel(y+vStart, x));
				}
			}

			candidateHeight = tempAfter.getHeight();
			candidateWidth = tempAfter.getWidth();
			candidateRatio = (double)candidateWidth / (double)candidateHeight;
			
			System.out.printf("%f\n", candidateRatio);
			
			if((candidateRatio <= 0.246 && candidateRatio >= 0.17) || (candidateRatio >= 0.369 && candidateRatio <= 5.0)){
				alphanumericCharacter.add(tempAfter);	
			}
		}
		
		alphanumericCharacterIterator = alphanumericCharacter.iterator();
		
		while(alphanumericCharacterIterator.hasNext()){
			
			writeJPEG.write(alphanumericCharacterIterator.next(), outputImagePath + "alphanumericCandidate" + h + ".jpg");
			h++;
		}
		*/
		
		/*
		 * 
		 */
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		
		System.out.printf("Elapsed Time : %.2f Seconds", (float)elapsedTime/1000);
	}
}
