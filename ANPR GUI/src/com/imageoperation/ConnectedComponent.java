/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.imageoperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConnectedComponent {
	
	private int height;
	private int width;
	private ImagePixelArray inputImage;
	
	/*
	 * resulting variable
	 */
	List<ImagePixelArray> outputImages;
	
	/*
	 * the labels
	 */
	int[][] labels;
	
	/*
	 * equivalence table
	 */
	List<EquivalenceLabel> equivalenceTable = new ArrayList<EquivalenceLabel>();
	
	/*
	 * label
	 */
	int label;
	
	public ConnectedComponent(){
		
		label = 1;
	}

	public List<ImagePixelArray> scan(ImagePixelArray inputImage){
		
		this.height = inputImage.getHeight();
		this.width = inputImage.getWidth();
		this.inputImage = inputImage;
		
		/*
		 * resulting variable
		 */
		this.outputImages = new ArrayList<ImagePixelArray>();
		
		/*
		 * mirror image
		 */
		this.labels = new int[height][width];
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				labels[y][x] = 0;
			}
		}
		
		/*
		 * equivalence table
		 */
		this.equivalenceTable = new ArrayList<EquivalenceLabel>();
		
		/*
		 * label
		 */
		this.label = 0;
		
		/*
		 * 1st pass
		 */
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				/*
				 * if the pixel is white
				 */
				if(this.inputImage.getPixelLevel(y, x) > 0){
					
					/*
					 * check the west and north neighbourhood
					 * if the pointer is at the upper or at the left border, set to zero
					 */
					int west, north;
					
					if(x == 0 && y == 0){
						
						west = 0;
						north = 0;
					}
					else if(y == 0){
						
						north = 0;
						west = labels[y][x-1];
					}
					else if(x == 0){
						
						west = 0;
						north = labels[y-1][x];
					}
					else{
						west = labels[y][x-1];
						north = labels[y-1][x];
					}
					
					/*
					 * dari Jain R., Kasturi R., Schunk B. G. (1995: 46-47)
					 * if only one of its upper and left neighbours has a label, then copy the label
					 */
					if(west == 0 && north > 0){
						
						labels[y][x] = north;
					}
					else if(north == 0 && west > 0){
						
						labels[y][x] = west;
					}
					else if(west > 0 && north > 0){
						
						/*
						 * if both have the same label, then copy the label
						 */
						if(north == west){
							
							labels[y][x] = west;
						}
						/*
						 * if both have different labels, then copy the upper's label and enter the labels in the equivalence table as equivalence
						 */
						else{
							
							labels[y][x] = north;
							
							equivalenceTable.get(north-1).getEquivalentLabels().add(west);
							equivalenceTable.get(west-1).getEquivalentLabels().add(north);
							
							/*
							 * all
							 */
							for(Integer k: equivalenceTable.get(north-1).getEquivalentLabels()){
								
								for(Integer l: equivalenceTable.get(north-1).getEquivalentLabels()){
									
									equivalenceTable.get(k-1).getEquivalentLabels().add(l);
								}
							}
							
							for(Integer k: equivalenceTable.get(west-1).getEquivalentLabels()){
								
								for(Integer l: equivalenceTable.get(west-1).getEquivalentLabels()){
									
									equivalenceTable.get(k-1).getEquivalentLabels().add(l);
								}
							}
						}
					}
					
					/*
					 * otherwise, assign a new label to this pixel and enter this label in the equivalence table
					 */
					else{
						
						label++;
						equivalenceTable.add(new EquivalenceLabel(label));
						equivalenceTable.get(label-1).getEquivalentLabels().add(label);
						labels[y][x] = label;
					}
				}
				else{
					
					/*
					 * the pixel is black, assign 0
					 */
					labels[y][x] = 0;
				}
				
				/*
				 * debug only
				 */
				//System.out.printf("Position: %d, %d\nLabel: %d\n\n", y, x, labels[y][x]);
			}
		}
		
		
		/*
		 * 2nd pass
		 */
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				/*
				 * check for equivalency
				 */
				if(this.inputImage.getPixelLevel(y, x) > 0){
					
					ArrayList<Integer> sorted = new ArrayList<>(equivalenceTable.get((labels[y][x])-1).getEquivalentLabels());
					Collections.sort(sorted);
					//Iterator<Integer> it = equivalenceTable.get((labels[y][x])-1).getEquivalentLabels().iterator();
					Iterator<Integer> it = sorted.iterator();
					labels[y][x] = it.next();
				}
			}
		}
		
		/*
		 * 3rd scan
		 * scan vertically, up to bottom
		 * to avoid the non-horizontally-aligned objects, keeping the order of the objects
		 */
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				
				if(labels[y][x] > 0){
					
					//check for existence
					int index = checkExistence(labels[y][x]);
					
					if(index < 0){
						
						ImagePixelArray image = new ImagePixelArray(height, width, String.valueOf(labels[y][x]));
						image.setPixelLevel(y, x, 255);
						outputImages.add(image);
					}
					else{
						
						outputImages.get(index).setPixelLevel(y, x, 255);
					}
				}
			}
		}
		
		/*
		 * debug
		 */
		/*
		for(ImagePixelArray a: outputImages){
			
			writeJPEG.write(a, "src/images/" + a.getIdentifier() + ".jpg");
		}
		*/
		
		return outputImages;
	}
	
	/*
	 * check for existence
	 */
	private int checkExistence(int label){
		
		int index = -1;
		int i = 0;
		
		for(ImagePixelArray image : outputImages){
			
			if(image.getIdentifier().equals(String.valueOf(label))){
				
				index = i;
				break;
			}
			else{
				
				i++;
			}
		}
		
		return index;
	}
	
	/*
	 * equivalence label class
	 * has ID and labels that are associated with the ID
	 */
	class EquivalenceLabel{
		
		private int ID;
		private Set<Integer> equivalentLabels; // start from 1
		
		/*
		 * build
		 */
		public EquivalenceLabel(int ID){
			
			this.ID = ID;
			this.equivalentLabels = new HashSet<Integer>();
		}
		
		/*
		 * get ID
		 */
		public int getID(){
			
			return this.ID;
		}
		
		/*
		 * get equivalent labels, List<Integer>, editable
		 */
		public Set<Integer> getEquivalentLabels(){
			
			return this.equivalentLabels;
		}
	}
}
