/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.filehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.neuralnetworks.WeightMatrix;;

public class DataWriter {
	
	public static void writeFile(int neuronCount[], WeightMatrix weightMatrix, String filePath) throws FileNotFoundException{
	
		PrintWriter fWriter;
		File outputFile;
		
		outputFile = new File(filePath);
		fWriter = new PrintWriter(outputFile);
		
		/*
		 * Layer Configuration
		 * 
		 * First Line
		 * Neuron Count in Each Layer
		 */
		for(int i=0; i<neuronCount.length; i++){
			
			if(i < neuronCount.length - 1){
				
				fWriter.printf("%d, ", neuronCount[i]);
			}
			else{
				/*
				 * No comma
				 */
				fWriter.printf("%d", neuronCount[i]);
			}
		}
		
		fWriter.println();
		
		/*
		 * Weight Data
		 */
		
		for(int i=0; i<weightMatrix.getOriginLayerCount(); i++){
			for(int j=0; j<weightMatrix.getDestinationLayerCount() - 1; j++){
				
				fWriter.printf("%.25f, ", weightMatrix.getConnectionNode(i, j).getValue());
			}
			/*
			 * Last line
			 * Add println
			 */
			fWriter.printf("%.25f", weightMatrix.getConnectionNode(i, weightMatrix.getDestinationLayerCount() - 1).getValue());
			fWriter.println();
		}
		
		fWriter.close();
	}
}
