package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ConnectedComponent;
import com.imageoperation.ImagePixelArray;

public class CCTest {
	
	public static void main(String[] args) throws IOException{
		
		String inputImagePath = "src/images/output/candidatePlate.jpg";
		
		File inputFile = new File(inputImagePath);
		BufferedImage inputObj = ImageIO.read(inputFile);
		
		ImagePixelArray inputImage = BinaryProcessing.threshold(inputObj, 127);
		ConnectedComponent CC = new ConnectedComponent();
		
		
		CC.scan(inputImage);
	}
}
