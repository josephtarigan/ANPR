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
import com.imageoperation.ImagePixelArray;
import com.imageoperation.Morphological;
import com.imageoperation.writeJPEG;

/*
 * Processes given input image
 * Outputs are detected candidate plate and alphanumeric characters to be used with neural networks
 * rata rata process 200ms
 */

public class PlateExtractor {

	private final double candidateThreshold = 0.10;
	private final int cropThreshold = 3;
	private final int heightThreshold = 30;
	private final int widthThreshold = 7;
	
	private BufferedImage inputImageObj, detectedPlate;
	private ImagePixelArray tophatImage, erotedImage, inputImage, candidatePlate, thresholdedImage;

	private int height, width;
	private int peak = 0;
	private int padding;
	private int averageHeight = 0;
	private int upperBound;
	private int lowerBound;
	private int heightAbsCompensation;

	private int[] verticalProjection;
	private int[] horizontalProjection;
	
	private boolean flag;
	
	/*
	 * processing time
	 */
	private long preprocessTime;
	
	/*
	 * Collection for candidate
	 */
	private List<Integer> yStart, xStart;
	private List<Integer> yEnd, xEnd;
	private List<ImagePixelArray> candidateImage;
	private List<ImagePixelArray> alphanumericCharacter;
	private List<ImagePixelArray> verifiedAlphanumericCharacter;
	@SuppressWarnings("unused")
	private Iterator<ImagePixelArray> alphanumericCharacterIterator;
	private Iterator<Integer> yStartIterator, yEndIterator, xStartIterator, xEndIterator;
	private Iterator<ImagePixelArray> candidateIterator;
	
	/*
	 * Go
	 * ============================================================================================
	 */
	public PlateExtractor(){
		
		this.preprocessTime = 0;
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
			
			this.convertDetectedIntoBufferedImage();
			
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
			 * Segmentation, Verification & Write
			 * ========================================================================================
			 */
			
			xStartIterator = xStart.iterator();
			xEndIterator = xEnd.iterator();
			alphanumericCharacter = new ArrayList<ImagePixelArray>();

			while(xStartIterator.hasNext()){
				
				int start = xStartIterator.next();
				int end = xEndIterator.next();
				int vStart = 0, vEnd = 0;
				int candidateHeight, candidateWidth;
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
				
				verticalProjection = BinaryProcessing.verticalProjection(temp);
				
				flag = false;
				for(int y=0; y<verticalProjection.length; y++){
					
					if(verticalProjection[y] > cropThreshold-0 && flag == false){
						
						flag = true;
						vStart = y;
					}
					else if(verticalProjection[y] < cropThreshold-0 && flag == true){
						
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
				
				//System.out.printf("Start : %d\n End : %d\n\n", vStart, vEnd);
				
				/*
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
					
					/*
					 * count average height
					 */
					averageHeight += candidateHeight;
					
					//System.out.printf("%d\n%d\n%.5f\n\n", candidateHeight, candidateWidth, candidateRatio);
					
					/*
					 * cek rasio
					 */
					if((candidateRatio <= 0.34 && candidateRatio >= 0.15) || (candidateRatio >= 0.369 && candidateRatio <= 0.613)){
						
						alphanumericCharacter.add(tempAfter);
						System.out.printf("%d\n%d\n%.5f\n\n", candidateHeight, candidateWidth, candidateRatio);
						
					}
				}
			}
			
			/*
			 * average height
			 */
			averageHeight = averageHeight/alphanumericCharacter.size();
			
			/*
			 * second verification, using lower bound * upper bound
			 * ======================================================================================
			 */
			heightAbsCompensation = averageHeight/4;
			upperBound = averageHeight + heightAbsCompensation;
			lowerBound = averageHeight - heightAbsCompensation;
			
			alphanumericCharacterIterator = alphanumericCharacter.iterator();
			verifiedAlphanumericCharacter = new ArrayList<ImagePixelArray>();
			
			while(alphanumericCharacterIterator.hasNext()){

				ImagePixelArray temp = alphanumericCharacterIterator.next();
				
				if(temp.getHeight() >= lowerBound && temp.getHeight() <= upperBound){
					
					verifiedAlphanumericCharacter.add(temp);
				}
			}
			
			/*
			int h = 1;
			alphanumericCharacterIterator = verifiedAlphanumericCharacter.iterator();
			
			while(alphanumericCharacterIterator.hasNext()){
				ImagePixelArray tempArray = alphanumericCharacterIterator.next();
				//ImagePixelArray temp = new ImagePixelArray(Scalr.resize(tempArray.convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_GRAYSCALE));
				writeJPEG.write(tempArray, "src/images/output/chars/" + "alphanumericCandidate" + h + ".jpg");
				h++;
			}
			*/
			
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
