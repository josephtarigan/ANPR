/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.filehandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.neuralnetworks.WeightMatrix;;

public class DataReader {
	
	/*
	 * Read file
	 * Parse data into a WeightMatrix object
	 * Return a WeightMatrix object
	 * 
	 * @filePath is the to the file
	 * @x is the origin layer nodes count
	 * @y is the destination layer nodes count
	 * @originLayerName is the origin layer name
	 * @destinationLayerName is the destination layer name
	 */
	public static WeightMatrix readFile(String filePath, int x, int y, String originLayerName, String destinationLayerName) throws IOException, FileNotFoundException{
		
		File inputFile;
		FileReader fReader;
		BufferedReader bfReader;
		WeightMatrix weightMatrix;
		String temp;
		int originLayerNeuronCount = 0, destinationLayerNeuronCount = 0;
		
		inputFile = new File(filePath);
		fReader = new FileReader(inputFile);
		bfReader = new BufferedReader(fReader);
		weightMatrix = new WeightMatrix(originLayerName, destinationLayerName, x, y);
		
		/*
		 * Ignore the first line
		 */
		temp = bfReader.readLine();
				
		/*
		 * Go
		 */
		while((temp = bfReader.readLine()) != null ){
			
			/*
			 * For each iteration, set the destination layer neuron count to 0
			 */
			destinationLayerNeuronCount = 0;
			
			for(String val: temp.split(",")){
				
				weightMatrix.getConnectionNode(originLayerNeuronCount, destinationLayerNeuronCount).setValue(Double.parseDouble(val));
				destinationLayerNeuronCount++;
			}
			
			/*
			 * For each end of line, origin layer neuron count + 1
			 */
			originLayerNeuronCount++;
		}
		
		bfReader.close();
		
		return weightMatrix;
	}
	
	public static int[] readFile(String filePath) throws IOException{
		
		File inputFile;
		FileReader fReader;
		BufferedReader bfReader;
		int neuronCount[], count = 0;
		List<Integer> temporaryVar = new ArrayList<Integer>();
		Iterator<Integer> temporaryVarIterator;
		String temp;
		
		inputFile = new File(filePath);
		fReader = new FileReader(inputFile);
		bfReader = new BufferedReader(fReader);
		
		/*
		 * Read first line
		 */
		temp = bfReader.readLine();
		
		/*
		 * Split by comma
		 */
		for(String c: temp.split(",")){
			
			temporaryVar.add(Integer.parseInt(c));
		}
		
		/*
		 * Store into int[]
		 */
		temporaryVarIterator = temporaryVar.iterator();
		neuronCount = new int[temporaryVar.size()];
		
		while(temporaryVarIterator.hasNext()){
		
			neuronCount[count] = temporaryVarIterator.next();
			count++;
		}
		
		bfReader.close();
		
		return neuronCount;
	}
}
