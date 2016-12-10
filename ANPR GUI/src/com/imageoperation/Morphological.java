/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.imageoperation;

public class Morphological {

	public static ImagePixelArray erotion(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		StructuringElement SE = new StructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMinimum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray dilation(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		StructuringElement SE = new StructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMaximum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray opening(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = dilation(erotion(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray closing(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = erotion(dilation(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray topHatFilter(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		ImagePixelArray kernelImage = opening(inputImage, structuringElement);
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int level;
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				level = inputImage.getPixelLevel(y, x) - kernelImage.getPixelLevel(y, x);
				
				if(level < 0){
					//level = kernelImage.getPixelLevel(y, x);
					level = 0;
				}
				
				outputImage.setPixelLevel(y, x, level);
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray enhanceContrast(ImagePixelArray inputImage, int power){
		
		ImagePixelArray kernel = erotion(inputImage, power);
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int level;
		ImagePixelArray outputImage = new ImagePixelArray(height, width);
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				level = kernel.getPixelLevel(y, x) + inputImage.getPixelLevel(y, x);
				
				if(level > 255){
					
					level = 255;
				}
				
				outputImage.setPixelLevel(y, x, level);
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray flatErotion(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		FlatStructuringElement SE = new FlatStructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMinimum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray flatDilation(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		FlatStructuringElement SE = new FlatStructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMaximum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray flatOpening(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = flatDilation(flatErotion(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray flatClosing(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = flatErotion(flatDilation(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray flatTopHatFilter(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		ImagePixelArray kernelImage = flatOpening(inputImage, structuringElement);
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int level;
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				level = inputImage.getPixelLevel(y, x) - kernelImage.getPixelLevel(y, x);
				
				if(level < 0){
					//level = kernelImage.getPixelLevel(y, x);
					level = 0;
				}
				
				outputImage.setPixelLevel(y, x, level);
			}
		}
		
		return outputImage;
	}
	
public static ImagePixelArray rectangularErotion(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		RectangularStructuringElement SE = new RectangularStructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMinimum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray rectangularDilation(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		/*
		 * Create the structuring element
		 */
		RectangularStructuringElement SE = new RectangularStructuringElement(inputImage, structuringElement);
		
		/*
		 * Loop through all pixel
		 */
		for(int y=0; y<inputImage.getHeight(); y++){
			for(int x=0; x<inputImage.getWidth(); x++){
				
				outputImage.setPixelLevel(y, x, SE.getMaximum(x, y));
			}
		}
		
		return outputImage;
	}
	
	public static ImagePixelArray rectangularOpening(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = rectangularDilation(rectangularErotion(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray rectangularClosing(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		
		/*
		 * Do the opening
		 */
		outputImage = rectangularErotion(rectangularDilation(inputImage, structuringElement), structuringElement);
		
		return outputImage;
	}
	
	public static ImagePixelArray rectangularTopHatFilter(ImagePixelArray inputImage, int structuringElement){
		
		/*
		 * Construct the ImagePixelArray
		 */
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		ImagePixelArray kernelImage = rectangularOpening(inputImage, structuringElement);
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();
		int level;
		
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				level = inputImage.getPixelLevel(y, x) - kernelImage.getPixelLevel(y, x);
				
				if(level < 0){
					//level = kernelImage.getPixelLevel(y, x);
					level = 0;
				}
				
				outputImage.setPixelLevel(y, x, level);
			}
		}
		
		return outputImage;
	}
	
	/*
	 * Structuring element must be an odd number
	 */
	public static ImagePixelArray optimizedRectangularTopHatFilter(ImagePixelArray inputImage, int structuringElement){
		
		ImagePixelArray outputImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		ImagePixelArray erotedImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		ImagePixelArray dilatedImage = new ImagePixelArray(inputImage.getHeight(), inputImage.getWidth());
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		/*
		 * (SE - 1)/2
		 */
		int padding = (structuringElement - 1)/2;
		
		int minimumColumn[] = new int[width];
		int maximumColumn[] = new int[width];
		int minimum = 0;
		int maximum = 0;
		
		/*
		 * Erotion
		 * =========================================================================
		 */
		/*
		 * For each pixel
		 */
		for(int y=0; y<height; y++){
			
			/*
			 * Calculate MIN in all column
			 * 
			 * cx = x in SE
			 * cy = y in SE
			 */
			for(int cx=0; cx<width; cx++){
				
				/*
				 * Set mininum to max
				 */
				minimum = 256;
				
				/*
				 * Go
				 */
				for(int cy=y-padding; cy<=y+padding; cy++){
					
					if(cy < 0 || cy >= height){
						
						//do nothing
						//minimum = minimum;
					}
					else if(inputImage.getPixelLevel(cy, cx) < minimum){
						
						minimum = inputImage.getPixelLevel(cy, cx);
					}
				}
				
				/*
				 * Store into variable
				 */
				minimumColumn[cx] = minimum;
			}
			
			for(int x=0; x<width; x++){
				
				/*
				 * Find the minimum in each SE area
				 */
				minimum = 256;
				
				for(int cx=x-padding; cx<=x+padding; cx++){
					
					if(cx < 0 || cx >= width){
						
						//do nothing
						//minimum = minimum;
					}
					else if(minimumColumn[cx] < minimum){
						
						minimum = minimumColumn[cx];
					}
				}
				
				/*
				 * Store as pixel
				 */
				erotedImage.setPixelLevel(y, x, minimum);
			}
		}
		
		/*
		 * Dilation
		 * ====================================================================
		 */
		/*
		 * For each pixel
		 */
		for(int y=0; y<height; y++){
			
			/*
			 * Calculate MIN in all column
			 * 
			 * cx = x in SE
			 * cy = y in SE
			 */
			for(int cx=0; cx<width; cx++){
				
				/*
				 * Set maximum to min
				 */
				maximum = 0;
				
				/*
				 * Go
				 */
				for(int cy=y-padding; cy<=y+padding; cy++){
					
					if(cy < 0 || cy >= height){
						
						//do nothing
						//maximum = maximum
					}
					else if(erotedImage.getPixelLevel(cy, cx) > maximum){
						
						maximum = erotedImage.getPixelLevel(cy, cx);
					}
				}
				
				/*
				 * Store into variable
				 */
				maximumColumn[cx] = maximum;
			}
			
			for(int x=0; x<width; x++){
				
				/*
				 * Find the maximum in each SE area
				 */
				maximum = 0;
				
				for(int cx=x-padding; cx<=x+padding; cx++){
					
					if(cx < 0 || cx >= width){
						
						//maximum = 0;
					}
					else if(maximumColumn[cx] > maximum){
						maximum = maximumColumn[cx];
					}
				}
				
				/*
				 * Store as pixel
				 */
				dilatedImage.setPixelLevel(y, x, maximum);
				
				/*
				 * Top hat
				 */
				if((inputImage.getPixelLevel(y, x) - maximum) < 0){
					
					outputImage.setPixelLevel(y, x, 0);
				}
				else{
					outputImage.setPixelLevel(y, x, inputImage.getPixelLevel(y, x) - maximum);
				}
			}
		}
		
		//return erotedImage;
		return outputImage;
	}
}
