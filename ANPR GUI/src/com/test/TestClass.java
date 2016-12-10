package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.imageoperation.BinaryMorphological;
import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.StructuringElement;
import com.imageoperation.writeJPEG;

public class TestClass {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String inputImagePath = "src/images/input17.jpg";
		String outputImagePath = "src/images/output/";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputImageObj = ImageIO.read(inputFile);
		
		//ImagePixelArray imagePixelArray = new ImagePixelArray(inputImageObj);
		ImagePixelArray erotedImage, dilatedImage, tophat, thresholdedImage;
		ImagePixelArray inputImage;
		
		final double candidateThreshold = 0.14;
		double threshold;
		
		int max = 0;
		
		/*
		 * Resize
		 */
		inputImage = new ImagePixelArray(Scalr.resize(inputImageObj, 768, Scalr.OP_GRAYSCALE));
		
		/*
		 * Do dilation
		 */
		//dilatedImage = BinaryMorphological.dilation(BinaryProcessing.threshold(inputImageObj, 150), 5);
		
		/*
		 * Top hat
		 */
		tophat = Morphological.optimizedRectangularTopHatFilter(inputImage, 33);
		//writeJPEG.write(tophat, outputImagePath + "tophat.jpg");
		/*
		 * Threshold
		 */
		thresholdedImage = BinaryProcessing.otsuThreshold(tophat);
		//writeJPEG.write(thresholdedImage, outputImagePath + "threshold.jpg");
		/*
		 * Do erotion
		 */
		erotedImage = BinaryMorphological.erotion(thresholdedImage, 3);
		
		
		/*
		 * Vertical Projection
		 * =======================================================================================
		 */
		int[] verticalProjection = BinaryProcessing.verticalProjection(erotedImage);
		XYSeries dataSeries = new XYSeries("Pixel");
		XYSeriesCollection dataCollection;
		JFreeChart chart;
		File outputChart = new File(outputImagePath + "VerticalProjection.png");
		
		for(int p=0; p<tophat.getHeight(); p++){
			
			dataSeries.add(p, verticalProjection[p]);
			//dataSet.addValue(verticalProjection[p], "Value", String.valueOf(p));
		}
		
		dataCollection = new XYSeriesCollection(dataSeries);
		
		chart = ChartFactory.createXYAreaChart("Vertical Projection", "Pixel", "Value", dataCollection, PlotOrientation.HORIZONTAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart, chart, 1024, 1024);
		
		/*
		 * Vertical Projection thresholded
		 * =======================================================================================================
		 */
		dataSeries = new XYSeries("Pixel");
		outputChart = new File(outputImagePath + "VerticalProjectionThresholded.png");
		
		for(int i=0; i<tophat.getHeight(); i++){
			
			if(verticalProjection[i] > max){
				max = verticalProjection[i];
			}
		}
		
		threshold = candidateThreshold * (double)max;
		
		for(int p=0; p<tophat.getHeight(); p++){
			
			if(verticalProjection[p] < (int)threshold){
				
				verticalProjection[p] = 0;
			}
			
			dataSeries.add(p, verticalProjection[p]);
			//dataSet.addValue(verticalProjection[p], "Value", String.valueOf(p));
		}
		
		dataCollection = new XYSeriesCollection(dataSeries);
		
		chart = ChartFactory.createXYAreaChart("Vertical Projection", "Pixel", "Value", dataCollection, PlotOrientation.HORIZONTAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart, chart, 1024, 1024);
		
		
		/*
		 * horizontal projection
		 * ===========================================================================================================
		 */
	}
}
