package com.theapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.imageoperation.BinaryMorphological;
import com.imageoperation.BinaryProcessing;
import com.imageoperation.ConnectedComponent;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

public class PreprocessWithCCA {

	/**
	 * TestClass3 combined with TestClass2
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final double candidateThreshold = 0.10;
		final int cropThreshold = 5;
		final int widthThreshold = 7;
		final int heightThreshold = 30;
		
		String inputImagePath = "src/images/input9.jpg";
		String outputImagePath = "src/images/output/cca/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray tophatImage, erotedImage, inputImage, candidatePlate, thresholdedImage;
		
		ConnectedComponent ct = new ConnectedComponent();
		
		/*
		 * resize
		 */
		inputImageObj = Scalr.resize(inputImageObj, 500, Scalr.OP_DARKER);
		
		int height, width;
		int peak = 0;
		int padding;
		
		int[] verticalProjection;
		int[] horizontalProjection;
		
		/*
		 * SD
		 */
		double[] data;
		double average = 0;
		double varian;
		double standardDeviation;
		int iterator;
		
		boolean flag;
		
		long elapsedTime;
		
		/*
		 * Collection for candidate
		 */
		List<Integer> yStart;
		List<Integer> yEnd;
		int hStart, hEnd, vStart, vEnd;
		List<ImagePixelArray> candidateImage;
		List<ImagePixelArray> extractedObjects, alphanumericCharacters;
		List<ImagePixelArray> verifiedAlphanumericCharacter;
		Iterator<ImagePixelArray> alphanumericCharacterIterator;
		Iterator<Integer> yStartIterator, yEndIterator, xStartIterator, xEndIterator;
		Iterator<ImagePixelArray> candidateIterator;
		
		/*
		 * Initialisation
		 * ======================================================================================
		 */
		
		elapsedTime = System.currentTimeMillis();
		
		inputImage = new ImagePixelArray(inputImageObj);
		tophatImage = Morphological.optimizedRectangularTopHatFilter(inputImage, 29);
		thresholdedImage = BinaryProcessing.otsuThreshold(tophatImage);
		erotedImage = Morphological.erotion(thresholdedImage, 3);
		
		//writeJPEG.write(erotedImage, outputImagePath + "eroted.jpg");
		
		height = tophatImage.getHeight();
		width = tophatImage.getWidth();
		
		/*
		 * Vertical Projection
		 * Calculate threshold padding
		 * ======================================================================================
		 */
		
		verticalProjection = BinaryProcessing.verticalProjection(erotedImage);
		
		peak = 0;
		for(int y=0; y<height; y++){
			
			if(verticalProjection[y] > peak){
				peak = verticalProjection[y];
			}
		}
		padding = (int)Math.floor(candidateThreshold * peak);
		
		/*
		 * Cropping Vertical Projection
		 * Using padding as threshold
		 * =======================================================================================
		 */
		
		for(int y=0; y<height; y++){
			if(verticalProjection[y] > padding){
				verticalProjection[y] = verticalProjection[y];
			}
			else{
				verticalProjection[y] = 0;
			}
		}
		
		/*
		 * Find Start & End Points
		 * From thresholded vertical projection
		 * =======================================================================================
		 */
		
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
			else if(y == height-1 && flag == true){
				
				flag = false;
				yEnd.add(y);
			}
		}
		
		/*
		 * Check Height
		 * The tallest is considered as a candidate plate
		 * =======================================================================================
		 */
		
		candidateImage = new ArrayList<ImagePixelArray>();
		yStartIterator = yStart.iterator();
		yEndIterator = yEnd.iterator();
		
		while(yStartIterator.hasNext()){
			
			int start = yStartIterator.next();
			int end = yEndIterator.next();
			
			ImagePixelArray temp = new ImagePixelArray(end - start, width);
			
			for(int y=start; y<end; y++){
				for(int x=0; x<width; x++){
					
					temp.setPixelLevel(y-start, x, erotedImage.getPixelLevel(y, x));
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
		
		//writeJPEG.write(new ImagePixelArray(Scalr.resize(candidatePlate.convertIntoBufferedImage(), 300, Scalr.OP_BRIGHTER)), outputImagePath + "candidate.jpg");
		
		/*
		 * Connected Component Analysis on the candidate plate
		 * =======================================================================================
		 */
		
		extractedObjects = ct.scan(new ImagePixelArray(Scalr.resize(candidatePlate.convertIntoBufferedImage(), 300, Scalr.OP_BRIGHTER)));
		verifiedAlphanumericCharacter = new ArrayList<ImagePixelArray>();
		Iterator<ImagePixelArray> characterIterator = extractedObjects.iterator();
		
		/*
		 * trim each character
		 */
		while(characterIterator.hasNext()){
			
			ImagePixelArray character = characterIterator.next();
			hStart = hEnd = vStart = vEnd = 0;
			
			/*
			 * horizontal trim
			 */
			horizontalProjection = BinaryProcessing.horizontalProjection(character);
			
			flag = false;
			
			for(int i=0; i<horizontalProjection.length; i++){
				
				if(horizontalProjection[i] > 0 && flag == false){
					
					flag = true;
					hStart = i;
				}
				else if(horizontalProjection[i] == 0 && flag == true){
					
					flag = false;
					hEnd = i;
				}
				else if(i == horizontalProjection.length-1 && flag == true){
					
					flag = false;
					hEnd = i;
				}
			}
			
			/*
			 * vertical trim
			 */
			verticalProjection = BinaryProcessing.verticalProjection(character);
			
			flag = false;
			
			for(int y=0; y<verticalProjection.length; y++){
				
				if(verticalProjection[y] > 0 && flag == false){
					
					flag = true;
					vStart = y;
				}
				else if(verticalProjection[y] == 0 && flag == true){
						
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
			
			/*
			 * start and end point have been found, now verificate and trim
			 */
			int candidateHeight = vEnd - vStart;
			int candidateWidth = hEnd - hStart;
			double candidateRatio = (double)candidateWidth / (double)candidateHeight;
			
			if(candidateHeight > heightThreshold && candidateWidth > widthThreshold){
				if((candidateRatio <= 0.34 && candidateRatio >= 0.15) || (candidateRatio >= 0.369 && candidateRatio <= 0.613)){
					
					ImagePixelArray candidate = new ImagePixelArray(candidateHeight, candidateWidth);
					
					for(int y=0; y<vEnd-vStart; y++){
						for(int x=0; x<hEnd-hStart; x++){
							
							candidate.setPixelLevel(y, x, character.getPixelLevel(y+vStart, x+hStart));
						}
					}
					
					verifiedAlphanumericCharacter.add(candidate);
					System.out.printf("%d\n%d\n%.5f\n\n", candidateHeight, candidateWidth, candidateRatio);
				}
			}
		}
		
		/*
		 * test
		 */
		
		/*
		characterIterator = verifiedAlphanumericCharacter.iterator();
		int hh = 1;
		
		while(characterIterator.hasNext()){
			ImagePixelArray resized;
			resized = new ImagePixelArray(Scalr.resize(characterIterator.next().convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_GRAYSCALE));
			//resized = BinaryMorphological.dilation(characterIterator.next(), 1);
			//resized = new ImagePixelArray(Scalr.resize(resized.convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_GRAYSCALE));
			writeJPEG.write(resized, outputImagePath + hh + ".jpg");
			hh++;
		}
		*/
		
		/*
		 * 
		 */
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		
		System.out.printf("Elapsed Time : %.2f Seconds", (float)elapsedTime/1000);
	}
}
