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

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

public class Preprocess {

	/**
	 * TestClass3 combined with TestClass2
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final double candidateThreshold = 0.10;
		final int cropThreshold = 5;
		final int widthThreshold = 7;
		final int heightThreshold = 30;
		
		String inputImagePath = "src/images/input7.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray tophatImage, erotedImage, inputImage, candidatePlate, thresholdedImage;
		
		/*
		 * resize
		 */
		inputImageObj = Scalr.resize(inputImageObj, 768, Scalr.OP_DARKER);
		
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
		List<Integer> yStart, xStart;
		List<Integer> yEnd, xEnd;
		List<ImagePixelArray> candidateImage;
		List<ImagePixelArray> alphanumericCharacter;
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
		tophatImage = Morphological.optimizedRectangularTopHatFilter(inputImage, 33);
		thresholdedImage = BinaryProcessing.otsuThreshold(tophatImage);
		erotedImage = Morphological.erotion(thresholdedImage, 3);
		
		height = tophatImage.getHeight();
		width = tophatImage.getWidth();
		
		writeJPEG.write(inputImage, outputImagePath + "input.jpg");
		writeJPEG.write(erotedImage, outputImagePath + "output.jpg");
		writeJPEG.write(tophatImage, outputImagePath + "tophat.jpg");
		writeJPEG.write(thresholdedImage, outputImagePath + "threshold.jpg");
		
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
		
		/*
		 * Horizontal Projection & Start End Points
		 * =======================================================================================
		 */
		
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
		
		/*
		 * horizontal projection
		 */

		XYSeries dataSeries = new XYSeries("Pixel");
		XYSeriesCollection dataCollection;
		JFreeChart chart;
		File outputChart = new File(outputImagePath + "HorizontalProjection.png");
		
		for(int i=0; i<candidatePlate.getWidth(); i++){
			
			dataSeries.add(i, horizontalProjection[i]);
		}
		
		dataCollection = new XYSeriesCollection(dataSeries);
		chart = ChartFactory.createXYAreaChart("Horizontal Projection", "Pixel", "Value", dataCollection, PlotOrientation.VERTICAL, false, false, false);
		ChartUtilities.saveChartAsPNG(outputChart, chart, 1024, 1024);
		writeJPEG.write(candidatePlate, outputImagePath + "candidatePlate.jpg");
		
		
		/*
		 * Segmentation, Verification & Write
		 * ========================================================================================
		 */
		
		xStartIterator = xStart.iterator();
		xEndIterator = xEnd.iterator();
		alphanumericCharacter = new ArrayList<ImagePixelArray>();
		int h = 1;
		iterator = 0;
		data = new double[xStart.size()];

		while(xStartIterator.hasNext()){
			
			int start = xStartIterator.next();
			int end = xEndIterator.next();
			int vStart = 0, vEnd = 0;
			int candidateHeight, candidateWidth;
			int blackFlag = 0;
			final int blackFlagMax = 60;
			double candidateRatio;

			/*
			 * Vertical Projection in each candidate
			 * ==========================================================================================================
			 */
			
			ImagePixelArray temp = new ImagePixelArray(candidatePlate.getHeight(), end-start);
			
			for(int y=0; y<candidatePlate.getHeight(); y++){
				for(int x=start; x<end; x++){
					
					temp.setPixelLevel(y, x-start, candidatePlate.getPixelLevel(y, x));
				}
			}
			
			System.out.printf("Start: %d\n End: %d\n\n", start, end);
			
			verticalProjection = BinaryProcessing.verticalProjection(temp);
			
			flag = false;
			for(int y=0; y<verticalProjection.length; y++){
				
				if(verticalProjection[y] > cropThreshold - 0 && flag == false){
					
					flag = true;
					vStart = y;
				}
				else if(verticalProjection[y] < cropThreshold - 0 && flag == true){
						
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
			 * if 0 then don't get it in
			 * candidate verification
			 */
			if(vEnd - vStart == 0){
				continue;
			}
			else if(vEnd - vStart <= heightThreshold || end - start <= widthThreshold){
				continue;
			}
			else{
			
				ImagePixelArray tempAfter = new ImagePixelArray(vEnd - vStart, end-start);
				
				
				for(int y=0; y<vEnd - vStart; y++){
					for(int x=0; x<temp.getWidth(); x++){
						
						tempAfter.setPixelLevel(y, x, temp.getPixelLevel(y+vStart, x));
					}
				}
	
				candidateHeight = tempAfter.getHeight();
				candidateWidth = tempAfter.getWidth();
				candidateRatio = (double)candidateWidth / (double)candidateHeight;
				
				data[iterator] = tempAfter.getHeight();
				iterator++;
				
				System.out.printf("%d %d %f\n\n", candidateHeight, candidateWidth, candidateRatio);
				
				//if((candidateRatio <= 0.265 && candidateRatio >= 0.155) || (candidateRatio >= 0.369 && candidateRatio <= 0.59)){
					
				if((candidateRatio <= 0.29 && candidateRatio >= 0.155) || (candidateRatio >= 0.300 && candidateRatio <= 0.59)){
					alphanumericCharacter.add(tempAfter);
				}
			}
		}
		
		/*
		 * standard deviation
		 * ======================================================================================================
		 */
		
		/*
		 * mean
		 */
		int length = alphanumericCharacter.size();
		for(int i=0; i<length; i++){
			
			average += data[i];
		}
		
		average = average/length;
		
		/*
		 * varian
		 */
		varian = 0;
		alphanumericCharacterIterator = alphanumericCharacter.iterator();
		
		for(int i=0; i<length; i++){
			
			int diff = alphanumericCharacterIterator.next().getHeight() - (int)average;
			varian += Math.pow(diff, 2);
		}
		
		varian = varian/length;
		
		standardDeviation = Math.sqrt(varian);
		
		/*
		 * 
		 * Second verification
		 * ========================================================================================
		 */
		
		alphanumericCharacterIterator = alphanumericCharacter.iterator();
		verifiedAlphanumericCharacter = new ArrayList<ImagePixelArray>();
		
		
		while(alphanumericCharacterIterator.hasNext()){
			
			ImagePixelArray temp = alphanumericCharacterIterator.next();
			height = temp.getHeight();
			int th = (int)average/6;
			int lb = (int)average - th;
			int ub = (int)average + th;
			//int diff = Math.abs(height - (int)average);
			
			/*
			if((double)diff <= (2*standardDeviation)){
				verifiedAlphanumericCharacter.add(temp);
			}
			*/
			if(height >= lb && height <= ub){
				verifiedAlphanumericCharacter.add(temp);
			}
		}
		
		
		/*
		 * write
		 * ========================================================================================
		 */
		
		alphanumericCharacterIterator = verifiedAlphanumericCharacter.iterator();
		
		while(alphanumericCharacterIterator.hasNext()){
			
			ImagePixelArray image = alphanumericCharacterIterator.next();
			//ImagePixelArray resized = new ImagePixelArray(Scalr.resize(image.convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_GRAYSCALE));
			
			writeJPEG.write(image, outputImagePath + "/Chars/alphanumericCandidate" + h + ".jpg");
			//writeJPEG.write(resized, outputImagePath + "/Chars/alphanumericCandidateResized" + h + ".jpg");
			h++;
		}
		
		/*
		 * 
		 */
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		
		System.out.printf("Elapsed Time : %.2f Seconds", (float)elapsedTime/1000);
	}
}
