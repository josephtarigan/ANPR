package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.imageoperation.BinaryMorphological;
import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

public class TestClass2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		/*
		 * Initial operation
		 * ==============================================================================================================================
		 */
		String inputImagePath = "src/images/input2.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		ImagePixelArray imagePixelArray = new ImagePixelArray(inputImageObj);
		ImagePixelArray erotedImage, dilatedImage, opening, closing, tophat, threshold;
		
		final double candidateThreshold = 0.18;
		double globalMean;
		
		/*
		 * Threshold for vertical & horizontal segmentation
		 */
		final int cropThreshold = 10;
		
		/*
		 * Collection for candidate
		 */
		List<Integer> yStart, xStart;
		List<Integer> yEnd, xEnd;
		List<ImagePixelArray> candidateImage;
		List<Double> verticalRatio;
		List<Double> horizontalRatio;
		List<ImagePixelArray> alphanumericCharacter;
		List<Double> ratios;
		Iterator<Double> ratiosIterator;
		Iterator<ImagePixelArray> alphanumericCharacterIterator;
		Iterator<Double> verticalIterator, horizontalIterator;
		Iterator<Integer> yStartIterator, yEndIterator, xStartIterator, xEndIterator;
		Iterator<ImagePixelArray> candidateIterator;
		int[] candidateContainer;
		int padding;
		int peak = 0;
		ImagePixelArray licensePlate;
		
		//erotedImage = Morphological.erotion(imagePixelArray, 5);
		//dilatedImage = Morphological.dilation(imagePixelArray, 5);
		//opening = Morphological.opening(imagePixelArray, 5);
		//closing = Morphological.closing(imagePixelArray, 7);
		tophat = Morphological.topHatFilter(imagePixelArray, 33); //33
		threshold = BinaryProcessing.threshold(tophat, 100);
		opening = Morphological.opening(threshold, 5);
		
		//writeJPEG.write(erotedImage, outputImagePath + "grayeroted.jpg");
		//writeJPEG.write(dilatedImage, outputImagePath + "graydilated.jpg");
		//writeJPEG.write(opening, outputImagePath + "grayopened.jpg");
		//writeJPEG.write(closing, outputImagePath + "grayclosed.jpg");
		//writeJPEG.write(tophat, outputImagePath + "graytophat.jpg");
		//writeJPEG.write(threshold, outputImagePath + "graytophatthreshold.jpg");
		writeJPEG.write(opening, outputImagePath + "graytophatthresholdopening.jpg");
		
		/*
		 * Vertical Projection
		 */
		int[] verticalProjection = BinaryProcessing.verticalProjection(opening);
		int[] horizontalProjection;
		XYSeries dataSeries = new XYSeries("Pixel");
		XYSeriesCollection dataCollection;
		JFreeChart chart;
		File outputChart = new File(outputImagePath + "VerticalProjection.png");
		
		for(int p=0; p<opening.getHeight(); p++){
			
			dataSeries.add(p, verticalProjection[p]);
			if(verticalProjection[p] > peak){
				peak = verticalProjection[p];
			}
		}
		
		dataCollection = new XYSeriesCollection(dataSeries);
		
		chart = ChartFactory.createXYAreaChart("Vertical Projection", "Pixel", "Value", dataCollection, PlotOrientation.HORIZONTAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart, chart, 1024, 1024);
		
		/*
		 * Thresholding for vertical segmentation
		 * ========================================================================================================
		 */
		
		candidateContainer = new int[imagePixelArray.getHeight()];
		padding = (int)Math.floor(candidateThreshold * peak);
		
		XYSeries dataSeries2 = new XYSeries("Pixel");
		XYSeriesCollection dataCollection2;
		JFreeChart chart2;
		File outputChart2 = new File(outputImagePath + "candidate.png");
		
		for(int y=0; y<imagePixelArray.getHeight(); y++){
			if(verticalProjection[y] > padding){
				candidateContainer[y] = verticalProjection[y];
			}
			else{
				candidateContainer[y] = 0;
			}
			
			dataSeries2.add(y, candidateContainer[y]);
		}
		
		dataCollection2 = new XYSeriesCollection(dataSeries2);
		
		chart2 = ChartFactory.createXYAreaChart("Candidate Plate", "Pixel", "Value", dataCollection2, PlotOrientation.HORIZONTAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart2, chart2, 1024, 1024);
				
		/*
		 * Find the vertical segmentation candidate
		 * ===========================================================================================================
		 */
		
		/*
		 * Check for start & end
		 */
		
		//flag
		boolean flag = false;
		
		yStart = new ArrayList<Integer>();
		yEnd = new ArrayList<Integer>();
		
		for(int x=0; x<candidateContainer.length; x++){
			
			if(candidateContainer[x] > 0 && flag == false){
				
				flag = true;
				yStart.add(x);
				//System.out.printf("%d\n", x);
			}
			else if(candidateContainer[x] < cropThreshold && flag == true){
				
				flag = false;
				yEnd.add(x);
				//System.out.printf("%d\n", x);
			}
		}
		
		//System.out.printf("Candidate count: %d", yStart.size());
		
		/*
		 * Vertical segmentation
		 * ============================================================================================================
		 */
		
		candidateImage = new ArrayList<ImagePixelArray>();
		yStartIterator = yStart.iterator();
		yEndIterator = yEnd.iterator();
		
		while(yStartIterator.hasNext()){
			
			int start = yStartIterator.next();
			int end = yEndIterator.next();
			
			ImagePixelArray temp = new ImagePixelArray(end - start, imagePixelArray.getWidth());
			
			for(int o=start; o<end; o++){
				for(int w=0; w<imagePixelArray.getWidth(); w++){
					
					/*
					 * Do draw each candidate and calculate sigma for ratio
					 */
					temp.setPixelLevel(o-start, w, opening.getPixelLevel(o, w));
				}
			}
			
			candidateImage.add(temp);
			
		}
		
		candidateIterator = candidateImage.iterator();
		
		int h=1;
		
		while(candidateIterator.hasNext()){
			
			writeJPEG.write(candidateIterator.next(), outputImagePath + "candidate" + h + ".jpg");
			
			h++;
		}
		
		/*
		 * Verification of candidate - useless
		 * ================================================================================================================
		 */
		
		/*

		verticalRatio = new ArrayList<Double>();
		yStartIterator = yStart.iterator();
		yEndIterator = yEnd.iterator();
		int num = 1;
		*/
		/*
		while(yStartIterator.hasNext()){
			
			int start = yStartIterator.next();
			int end = yEndIterator.next();
			int sigma = 0;
			double ratiotemp;
			
			for(int i=start; i<=end; i++){
				sigma += verticalProjection[i];
			}
			
			ratiotemp = sigma/(end-start); 
			
			verticalRatio.add(ratiotemp);
			*/
			/*if(ratiotemp < 350){
				System.out.println(ratiotemp + " " + num);
			}
			else{
				System.out.println(ratiotemp);
			}
			*/
			/*
			System.out.printf("%d / %d = %f\n", sigma, end-start, ratiotemp);
			
			num++;
		}
		*/
		
		/*
		 * Vertical verification
		 * ======================================================================================================
		 */
		
		candidateIterator = candidateImage.iterator();
		licensePlate = null;
		int max = 0;
		
		while(candidateIterator.hasNext()){
			
			ImagePixelArray temp = candidateIterator.next();
			//System.out.printf("\n%d\n", temp.getHeight());
			
			if(temp.getHeight() > max){
				
				max = temp.getHeight();
				licensePlate = temp;
			}
		}
		
		writeJPEG.write(licensePlate, outputImagePath + "LicensePlate.jpg");
		
		/*
		 * Horizontal segmentation
		 * ==============================================================================================================
		 */
		
		XYSeries dataSeries3 = new XYSeries("Pixel");
		XYSeriesCollection dataCollection3;
		JFreeChart chart3;
		File outputChart3 = new File(outputImagePath + "HorizontalProjection.png");
		
		horizontalProjection = BinaryProcessing.horizontalProjection(licensePlate);
		
		peak = 0;
		
		for(int i=0; i<horizontalProjection.length; i++){
			
			dataSeries3.add(i, horizontalProjection[i]);
			if(horizontalProjection[i] > peak){
				
				peak = horizontalProjection[i];
			}
		}
		
		dataCollection3 = new XYSeriesCollection(dataSeries3);
		chart3 = ChartFactory.createXYAreaChart("Horizontal Projection", "Pixel", "Value", dataCollection3, PlotOrientation.VERTICAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart3, chart3, 1024, 1024);
		
		/*
		 * Find the start & end position
		 * =================================================================================================================
		 */
		
		flag = false;
		alphanumericCharacter = new ArrayList<ImagePixelArray>();
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
		 * Segmentation
		 * =====================================================================================================================
		 */
		
		xStartIterator = xStart.iterator();
		xEndIterator = xEnd.iterator();
		alphanumericCharacter = new ArrayList<ImagePixelArray>();
		h=1;
		
		while(xStartIterator.hasNext()){
			
			int start = xStartIterator.next();
			int end = xEndIterator.next();
			
			ImagePixelArray temp = new ImagePixelArray(licensePlate.getHeight(), end-start);
			
			for(int y=0; y<licensePlate.getHeight(); y++){
				for(int x=start; x<end; x++){
					
					temp.setPixelLevel(y, x-start, licensePlate.getPixelLevel(y, x));
				}
			}
			
			alphanumericCharacter.add(temp);
		}
		
		alphanumericCharacterIterator = alphanumericCharacter.iterator();
		
		while(alphanumericCharacterIterator.hasNext()){
			
			writeJPEG.write(alphanumericCharacterIterator.next(), outputImagePath + "alphanumericCandidate" + h + ".jpg");
			h++;
		}
		
		/*
		 * Ratio counting, for development use
		 * ==========================================================================================================================
		 */
		
		alphanumericCharacterIterator = alphanumericCharacter.iterator();
		ratios = new ArrayList<Double>();
		h=1;
		
		while(alphanumericCharacterIterator.hasNext()){
			
			ImagePixelArray temp = alphanumericCharacterIterator.next();
			double ratio;
			
			ratio = (double)temp.getWidth()/(double)temp.getHeight();
			
			ratios.add(ratio);
			System.out.printf("%d - %f\n\n", h, ratio);
			h++;
		}
	}
}
