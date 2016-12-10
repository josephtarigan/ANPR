package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.imageoperation.ImagePixelArray;

public class TestClass7 {

	/**
	 * Histogram testing
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String inputImagePath = "src/images/input2.jpg";
		String outputImagePath = "src/images/output/histogram.png";
		
		File inputFile = new File(inputImagePath);
		File outputChart = new File(outputImagePath);
		BufferedImage inputObj = ImageIO.read(inputFile);
		
		ImagePixelArray inputImage = new ImagePixelArray(inputObj);
		int[] histogram;
		
		XYSeries dataSeries = new XYSeries("Level");
		XYSeriesCollection dataCollection;
		JFreeChart chart;
		
		/*
		 * Start
		 */
		histogram = inputImage.getHistogram();
		for(int i=0; i<histogram.length; i++){
			
			dataSeries.add(i, histogram[i]);
			System.out.printf("Level [%d] = %d\n", i, histogram[i]);
		}
		
		dataCollection = new XYSeriesCollection(dataSeries);
		chart = ChartFactory.createXYAreaChart("Histogram", "Level", "Intensity", dataCollection, PlotOrientation.VERTICAL, false, false, false);
		
		ChartUtilities.saveChartAsPNG(outputChart, chart, 1024, 768);
		
		/*
		 * Mean Gray Value
		 */
		System.out.printf("\n\nMean : %d", inputImage.getMeanGrayValue());
	}
}
