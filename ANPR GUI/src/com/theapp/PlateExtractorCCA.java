/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.theapp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.imgscalr.Scalr;

import com.imageoperation.BinaryProcessing;
import com.imageoperation.ConnectedComponent;
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

/*
 * Processes given input image
 * Outputs are detected candidate plate and alphanumeric characters to be used with neural networks
 * rata-rata 400ms, tetapi pada image image sederhana hanya puluhan ms
 */

public class PlateExtractorCCA {

	private final double candidateThreshold = 0.087;
	private final int heightThreshold = 35;
	private final int widthThreshold = 7;
	
	private BufferedImage inputImageObj, detectedPlate;
	private ImagePixelArray tophatImage, erotedImage, inputImage, candidatePlate, thresholdedImage;

	private int height, width;
	private int peak = 0;
	private int padding;

	private int[] verticalProjection;
	private int[] horizontalProjection;
	
	private boolean flag;
	
	/*
	 * CCA
	 */
	ConnectedComponent ct;
	
	/*
	 * processing time
	 */
	private long preprocessTime;
	
	/*
	 * Collection for candidate
	 */
	private List<Integer> yStart;
	private List<Integer> yEnd;
	private int hStart, hEnd, vStart, vEnd;
	private List<ImagePixelArray> candidateImage;
	private List<ImagePixelArray> extractedObjects;
	private List<ImagePixelArray> verifiedAlphanumericCharacter;
	@SuppressWarnings("unused")
	private Iterator<ImagePixelArray> alphanumericCharacterIterator;
	private Iterator<Integer> yStartIterator, yEndIterator;
	private Iterator<ImagePixelArray> candidateIterator;
	
	private String outputPath = "src/images/output/";
	
	/*
	 * Go
	 * ============================================================================================
	 */
	public PlateExtractorCCA(){
		
		this.preprocessTime = 0;
		ct = new ConnectedComponent();
	}

	/*
	 * Set the input image
	 */
	public void setInputImage(BufferedImage inputImageObj){

		this.inputImageObj = Scalr.resize(inputImageObj, 768, Scalr.OP_DARKER);
	}

	/*
	 * Do the magic
	 */
	public void extract(){
		
		if(this.inputImageObj != null){
			
			/*
			 * Initialisation
			 * ======================================================================================
			 */
			
			inputImage = new ImagePixelArray(inputImageObj);
			tophatImage = Morphological.optimizedRectangularTopHatFilter(inputImage, 33);
			thresholdedImage = BinaryProcessing.otsuThreshold(tophatImage);
			erotedImage = Morphological.erotion(thresholdedImage, 3);
			
			//writeJPEG.write(erotedImage, outputPath + "eroted.jpg");
			
			height = tophatImage.getHeight();
			width = tophatImage.getWidth();
			
			preprocessTime = System.currentTimeMillis();

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
				else if(verticalProjection[y] == 0 && flag == true){
					
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
			
			writeJPEG.write(candidatePlate, "src/images/candidate.jpg");
			this.convertDetectedIntoBufferedImage();
			
			/*
			 * Connected Component Analysis on the candidate plate
			 * =======================================================================================
			 */
			
			extractedObjects = ct.scan(new ImagePixelArray(Scalr.resize(candidatePlate.convertIntoBufferedImage(), 450, Scalr.OP_GRAYSCALE)));
			verifiedAlphanumericCharacter = new ArrayList<ImagePixelArray>();
			Iterator<ImagePixelArray> characterIterator = extractedObjects.iterator();
			
			/*
			 * trim each character
			 */
			int hh=1;
			while(characterIterator.hasNext()){
				
				ImagePixelArray character = characterIterator.next();
				hStart = hEnd = vStart = vEnd = 0;
				int candidateHeight, candidateWidth;
				double candidateRatio;
				
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
					else if(verticalProjection[y] <= 1 && flag == true){
							
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
				candidateHeight = vEnd - vStart;
				candidateWidth = hEnd - hStart;
				candidateRatio = (double)candidateWidth / (double)candidateHeight;
				
				//System.out.printf("%d\n%d\n%.5f\n\n", candidateHeight, candidateWidth, candidateRatio);
				//writeJPEG.write(character, "src/images/" + hh + ".jpg");
				//hh++;

				if(candidateHeight > heightThreshold && candidateWidth > widthThreshold){
					if((candidateRatio <= 0.345 && candidateRatio >= 0.15) || (candidateRatio >= 0.369 && candidateRatio <= 0.613)){
						
						ImagePixelArray candidate = new ImagePixelArray(candidateHeight, candidateWidth);
						
						for(int y=0; y<vEnd-vStart; y++){
							for(int x=0; x<hEnd-hStart; x++){
								
								candidate.setPixelLevel(y, x, character.getPixelLevel(y+vStart, x+hStart));
							}
						}
						
						verifiedAlphanumericCharacter.add(candidate);
						writeJPEG.write(new ImagePixelArray(Scalr.resize(candidate.convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_BRIGHTER)), "src/images/" + hh + ".jpg");
						hh++;
						//System.out.printf("%d\n%d\n%.5f\n\n", candidateHeight, candidateWidth, candidateRatio);
					}
				}
			}

			/*
			 * time
			 */
			preprocessTime = System.currentTimeMillis() - preprocessTime;
		}
	}

	/*
	 * Get detected plate
	 */
	public BufferedImage getDetectedPlate(){
		
		return this.detectedPlate;
	}
	
	/*
	 * Internal, for converting ImagePixelArray into BufferedImage
	 */
	private void convertDetectedIntoBufferedImage(){

		this.detectedPlate = this.candidatePlate.convertIntoBufferedImage();
	}

	/*
	 * Get detected alphanumeric character
	 */
	public List<ImagePixelArray> getDetectedCharacters(){
		
		return this.verifiedAlphanumericCharacter;
	}
	
	/*
	 * get processing time
	 */
	public long getProcessingTime(){
		
		return this.preprocessTime;
	}
}
