/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.filehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.neuralnetworks.WeightMatrix;;

public class DataWriter {
	
	public static void writeFile(WeightMatrix weightMatrix, String filePath) throws FileNotFoundException{
	
		PrintWriter fWriter;
		File outputFile;
		
		outputFile = new File(filePath);
		fWriter = new PrintWriter(outputFile);
		
		for(int i=0; i<weightMatrix.getOriginLayerCount(); i++){
			for(int j=0; j<weightMatrix.getDestinationLayerCount() - 1; j++){
				
				fWriter.printf("%.10f, ", weightMatrix.getConnectionNode(i, j).getValue());
			}
			/*
			 * Last line
			 * Add println
			 */
			fWriter.printf("%.10f", weightMatrix.getConnectionNode(i, weightMatrix.getDestinationLayerCount() - 1).getValue());
			fWriter.println();
		}
		
		fWriter.close();
	}
}
