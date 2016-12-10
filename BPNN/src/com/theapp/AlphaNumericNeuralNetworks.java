/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.theapp;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.charset.Alphanumeric;
import com.charset.CharConfig;
import com.charset.CharProvider;
import com.filehandler.DataWriter;
import com.neuralnetworks.*;

public class AlphaNumericNeuralNetworks implements INeuralNetworksTrainable{

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
	private final int firstLayerNeuronCount = characterHeight * characterWidth;
	private final int hiddenLayerNeuronCount = 252;
	private final int outputLayerNeuronCount = 36;
	private final int numberOfLayer = 3;
	private final int[] numberOfNeuron = {firstLayerNeuronCount, hiddenLayerNeuronCount, outputLayerNeuronCount};
	private final String[] layerNames = {"inputLayer", "hiddenLayer", "outputLayer"};
	private double momentumRate = 0.996094;
	private double learningRate = 0.048828;
	/*
	 * Alphanumeric object
	 * Provides desired output for each char
	 */
	private Alphanumeric alphaNumeric = new Alphanumeric();
	/*
	 * The CharProvider
	 * Provides ImagePixelArray of training image
	 */
	public CharProvider charProvider;
	/*
	 * The NeuralNetworks object
	 * With public modifier
	 */
	public static NeuralNetworks neuralNetworks;
	/*
	 * Statistic
	 */
	private double MSE = 0; // mean square error
	private double[] perSetMSE; // per set
	private int epoch;
	
	/*
	 * Constructor
	 */
	public AlphaNumericNeuralNetworks(){
		
		/*
		 * Build
		 */
		try {
			this.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Creating the NeuralNetworks object
	 */
	private void init() throws IOException{
		
		neuralNetworks = new NeuralNetworks("AlphaNumericNeuralNetworks", this.numberOfLayer, this.numberOfNeuron, this.layerNames, this.learningRate, this.momentumRate);
		this.charProvider = new CharProvider();
		this.perSetMSE = new double[CharConfig.CHAR_COUNT];
		/*
		 * Randomise weights
		 */
		neuralNetworks.randomizeWeight();
		/*
		 * Set epoch to 0
		 */
		this.epoch = 0;
		/*
		 * perSetMSE
		 */
		this.perSetMSE = new double[CharConfig.CHAR_COUNT];
	}
	
	/*
	 * Set the momentum rate
	 * For GA use
	 */
	public void setMomentumRate(double momentumRate){
		
		this.momentumRate = momentumRate;
		neuralNetworks.setMomentumRate(momentumRate);
	}
	
	/*
	 * Set the learning rate
	 * For GA use
	 */
	public void setLearningRate(double learningRate){
		
		this.learningRate = learningRate;
		neuralNetworks.setLearningRate(learningRate);
	}
	
	/*
	 * Train the neural networks with the given input pattern & desired output pattern
	 */
	public void trainNeuralNetworks(int set){

		
		/*
		 * Add epoch
		 */
		this.epoch++;
		/*
		 * Iterate through character
		 */
		for(int i=0; i<CharConfig.CHAR_COUNT; i++){
				
			/*
			 * charProvider provides the input value set for each character
			 * alphaNumeric provides the desired output for each character
			 */
			neuralNetworks.trainNeuralNetwork(charProvider.getChar(set, i).getSerializedNormalizedPixelLevels(), alphaNumeric.character[i].getCode());

			/*
			 * Calculate perSetMSE
			 */
			this.calculatePerCharacterMSE(neuralNetworks.getOutputValues(), alphaNumeric.character[i].getCode(), i);
		}
		
		//calculate global MSE
		this.calculateMSE();
	}

	/*
	 * Calculate the Per Set MSE
	 * Means to calculate MSE for one training iteration of character
	 * 
	 * @outputValues is the output values from forward propagation
	 * @desiredOutput is the desired output
	 * @setNumber is the number of the char set
	 */
	@Override
	public void calculatePerCharacterMSE(LayerInterface outputValues, double[] desiredOutput, int setNumber) {
		
		this.perSetMSE[setNumber] = 0;
		
		for(int i=0; i<CharConfig.CHAR_COUNT; i++){
			
			this.perSetMSE[setNumber] += Math.pow(desiredOutput[i] - outputValues.getNode(i).getValue(), 2);
		}
		
	}

	/*
	 * Calculate the globalMSE
	 */
	@Override
	public void calculateMSE() {
		
		this.MSE = 0;
		
		for(double value: perSetMSE){
			
			this.MSE += value;
		}
		
		this.MSE = this.MSE/2;
	}
	
	/*
	 * Get epoch number
	 */
	@Override
	public int getEpoch(){
		
		return this.epoch;
	}
	
	/*
	 * Get MSE
	 */
	@Override
	public double getMSE(){
		
		return this.MSE;
	}
	
	/*
	 * Save the result weight from training
	 */
	public void saveWeightData(String path) throws FileNotFoundException{
		
		/*
		 * Iterate trough WeightWatrix
		 */
		for(int i=0; i<neuralNetworks.getWeightMatrixCount(); i++){
			
			/*
			 * Filename >> originLayerName-destinationLayerName
			 */
			String fileName = neuralNetworks.getWeightMatrix(i).getOriginLayerName() + "-" + neuralNetworks.getWeightMatrix(i).getDestinationLayerName() + ".DAT";
			DataWriter.writeFile(numberOfNeuron, neuralNetworks.getWeightMatrix(i), path + "\\" + fileName);
		}
	}
	
	/*
	 * Method name says it all
	 */
	public void setEpochToZero(){
		
		this.epoch = 0;
	}
}
