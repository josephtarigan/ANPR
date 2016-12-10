package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.filehandler.DataReader;
import com.neuralnetworks.WeightMatrix;
//import com.theapp.MainWindow.FirstHiddenDialog;

public class TestClass5 {
	
	/*
	 * Character width & height
	 * Height is 12px
	 * Width is 9px
	 */
	private static final int characterHeight = 12;
	private static final int characterWidth = 9;
	
	/*
	 * Layer configuration
	 * First layer has 108 neurons
	 * Hidden layer has 168 neurons
	 * Output layer has 36 neurons
	 * Bias is not included in the specified count as it will be added automatically by the Layer object
	 */
	private static final int firstLayerNeuronCount = characterHeight * characterWidth;
	private static final int hiddenLayerNeuronCount = 168;
	private static final int outputLayerNeuronCount = 36;
	private static final int numberOfLayer = 3;
	private static final int[] numberOfNeuron = {firstLayerNeuronCount, hiddenLayerNeuronCount, outputLayerNeuronCount};
	private static final String[] layerNames = {"inputLayer", "hiddenLayer", "outputLayer"};
	

	private static final String srcDir = "src/";
	private static final String defaultInputHiddenData = srcDir + "weightdata/inputLayer-hiddenLayer.DAT";
	private static WeightMatrix tempWeightMatrix;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		tempWeightMatrix = DataReader.readFile(defaultInputHiddenData, firstLayerNeuronCount, hiddenLayerNeuronCount, layerNames[0], layerNames[1]);
	}

}
