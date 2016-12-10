package com.theapp;

import java.io.IOException;

import com.filehandler.DataReader;
import com.neuralnetworks.NeuralNetworks;

public class AlphaNumericNeuralNetworks {
	
	/*
	 * Configuration
	 */
	/*
	 * Character width & height
	 * Height is 12px
	 * Width is 9px
	 */
	private final int characterHeight = 12;
	private final int characterWidth = 9;
	
	/*
	 * Layer configuration
	 * First layer has 108 neurons
	 * Hidden layer has 168 neurons
	 * Output layer has 36 neurons
	 * Bias is not included in the specified count as it will be added automatically by the Layer object
	 */
	private int firstLayerNeuronCount = characterHeight * characterWidth;
	private int hiddenLayerNeuronCount = 188;
	private int outputLayerNeuronCount = 36;
	private int numberOfLayer = 3;
	private int[] numberOfNeuron = {firstLayerNeuronCount, hiddenLayerNeuronCount, outputLayerNeuronCount};
	private final String[] layerNames = {"inputLayer", "hiddenLayer", "outputLayer"};
	
	/*
	 * The NeuralNetworks object
	 * With public modifier
	 */
	public NeuralNetworks neuralNetworks;
	
	/*
	 * Default WeightData
	 */
	private String inputHiddenData;
	private String hiddenOutputData;
	

	/**
	 * Go
	 * =============================================================================================
	 */
	
	/*
	 * @inputHidden is the default firstHidden weight data
	 * @hiddenOutput is the default hiddenOutput weight data
	 */
	public AlphaNumericNeuralNetworks(String inputHidden, String hiddenOutput){
		
		this.inputHiddenData = inputHidden;
		this.hiddenOutputData = hiddenOutput;
		
		this.init();
	}
	
	/*
	 * Load Default WeightData
	 */
	private void init(){
		
		/*
		 * Read the Neuron count from the file
		 */
		try {
			numberOfNeuron = DataReader.readFile(inputHiddenData);
			numberOfLayer = numberOfNeuron.length;
			
			this.hiddenLayerNeuronCount = numberOfNeuron[1];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * Initiate the Neural Networks
		 */
		neuralNetworks = new NeuralNetworks("AlphaNumericNeuralNetworks", numberOfLayer, numberOfNeuron, layerNames, 0, 0);
		
		/*
		 * Set default weight into neural networks
		 */
		try {
			/*
			 * +1 for bias neuron
			 */
			neuralNetworks.setWeightValues(0, DataReader.readFile(inputHiddenData, firstLayerNeuronCount+1, hiddenLayerNeuronCount, layerNames[0], layerNames[1]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			/*
			 * +1 for bias neuron
			 */
			neuralNetworks.setWeightValues(1, DataReader.readFile(hiddenOutputData, hiddenLayerNeuronCount+1, outputLayerNeuronCount, layerNames[1], layerNames[2]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Do Forward Propagate
	 */
	public void forwardPropagate(double[] inputValues){
		
		neuralNetworks.forwardPropagate(inputValues);
	}
	
	/*
	 * Change the first -> hidden weights data
	 */
	public void changeInputHiddenData(String path){
		
		this.inputHiddenData = path;
		
		this.init();
	}
	
	/*
	 * Change the hidden -> output weights data
	 */
	public void changeHiddenOutputData(String path){
		
		this.hiddenOutputData = path;
		
		this.init();
	}
	
	/*
	 * get processing time
	 */
	public long getProcessingTime(){
		
		return neuralNetworks.getProcessingTime();
	}
}
