/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class NeuralNetworks {

	private String neuralNetworksName;
	
	/*
	 * Configuration
	 */
	private int numberOfLayer;
	private String[] layerName;
	private Layer[] neuronLayer;
	private WeightMatrix[] weightMatrix;
	private int[] numberOfNeuron; //in each layer
	private double learningRate;
	private double momentumRate;	
	
	public NeuralNetworks(String neuralNetworksName, int numberOfLayer, int[] numberOfNeuron, String[] layerName, double learningRate, double momentumRate){
		
		this.neuralNetworksName = neuralNetworksName;
		this.layerName = layerName;
		this.numberOfLayer = numberOfLayer;
		//this.MSE = 0;
		this.learningRate = learningRate;
		this.momentumRate = momentumRate;
		this.numberOfNeuron = numberOfNeuron;
		
		/*
		 * Initialisation
		 */
		this.init();
	}
	
	/*
	 * Initialisation
	 */
	private void init(){
		
		/*
		 * Creation of Layer
		 * Layer 0 is reserved for the Input Layer
		 * The last layer is reserved for Output Layer
		 */
		
		this.neuronLayer = new Layer[numberOfLayer];
		
		/*
		 * The input layer
		 */
		this.neuronLayer[0] = new Layer(layerName[0], "INPUT", numberOfNeuron[0]);
		
		/*
		 * Hidden layer(s)
		 */
		for(int i=1; i<numberOfLayer-1; i++){
			this.neuronLayer[i] = new Layer(layerName[i], "HIDDEN", numberOfNeuron[i]);
		}
		
		/*
		 * The output layer
		 */
		this.neuronLayer[numberOfLayer-1] = new Layer(layerName[numberOfLayer-1], "OUTPUT", numberOfNeuron[numberOfLayer-1]);
		
		/*
		 * Creation of Weight Matrix
		 * Total count of WeightMatrix is Total Number of Layer - 1
		 */
		this.weightMatrix = new WeightMatrix[numberOfLayer - 1];
		
		for(int j=0; j<numberOfLayer - 1; j++){
			
			this.weightMatrix[j] = new WeightMatrix(layerName[j], layerName[j+1], this.neuronLayer[j].getInterface().getNodesCount(), this.neuronLayer[j+1].getNeuronCount());
		}
	}
	
	/*
	 * Randomise all WeightMatrix
	 */
	public void randomizeWeight(){
		
		/*
		 * nguyen widrow
		 */
		weightMatrix[0].nguyenWidrowInitialisation();
		/*
		 * normal
		 */
		weightMatrix[1].randomizeWeight();
		
		/*
		for(WeightMatrix i:weightMatrix){
			
			i.randomizeWeight();
		}
		*/
	}
	
	/*
	 * Set weightNode with given values
	 * Bias neuron is included in the originLayerNeuronCount --> uses Layer.getOutputNodesCount()
	 * Bias neuron is not included in the destinationLayerNeuronCount --> uses Layer.getNeuronCount()
	 */
	public void setWeightValues(int weightMatrixNumber, double[][] weightValues){
		
		int o = weightValues.length;
		int d = weightValues[0].length;
		
		for(int i=0; i<o; i++){
			for(int j=0; j<d; j++){
				this.weightMatrix[weightMatrixNumber].updateWeight(i, j, weightValues[i][j]);
			}
		}
	}
	
	/*
	 * Set weightNode with given values
	 * Bias neuron is included in the originLayerNeuronCount --> uses Layer.getOutputNodesCount()
	 * Bias neuron is not included in the destinationLayerNeuronCount --> uses Layer.getNeuronCount()
	 */
	public void setWeightValues(int weightMatrixNumber, WeightMatrix weightValues){
		
		int o = weightValues.getOriginLayerCount();
		int d = weightValues.getDestinationLayerCount();
		
		for(int i=0; i<o; i++){
			for(int j=0; j<d; j++){
				this.weightMatrix[weightMatrixNumber].updateWeight(i, j, weightValues.getConnectionNode(i, j).getValue());
			}
		}
	}
	
	/*
	 * Train the neural network
	 * With integer data type
	 */
	public void trainNeuralNetwork(double[] inputValues, double[] desiredOutputValues){
		
		//do forward propagation
		this.forwardPropagate(inputValues);
		
		//do backward propagation
		this.backwardPropagate(desiredOutputValues);
	}
	
	/*
	 * Do forward propagation
	 */
	public void forwardPropagate(double[] inputValues){
		
		/*
		 * For input layer
		 */
		//set input values into the Input Layer
		this.neuronLayer[0].setNeuronInputNodes(inputValues);
		
		//do summation
		this.neuronLayer[0].doSummation();
		
		//do activation
		this.neuronLayer[0].doActivation();
		
		//fetch output values
		this.neuronLayer[0].fetchOutputValues();
		
		/*
		 * For hidden layer(s)
		 */
		for(int i=1; i<numberOfLayer-1; i++){
			
			//set input values into layer
			this.neuronLayer[i].setNeuronInputNodes(this.neuronLayer[i-1].getInterface(), weightMatrix[i-1]);
			
			//do summation
			this.neuronLayer[i].doSummation();
			
			//do activation
			this.neuronLayer[i].doActivation();
			
			//fetch output values
			this.neuronLayer[i].fetchOutputValues();
		}
		
		/*
		 * For output layer
		 */
		//set input values into the Output Layer
		this.neuronLayer[this.numberOfLayer-1].setNeuronInputNodes(this.neuronLayer[this.numberOfLayer - 2].getInterface(), weightMatrix[this.weightMatrix.length - 1]);
		
		//do summation
		this.neuronLayer[this.numberOfLayer-1].doSummation();
		
		//do activation
		this.neuronLayer[this.numberOfLayer-1].doActivation();
		
		//fetch output values
		this.neuronLayer[this.numberOfLayer-1].fetchOutputValues();
		
		//save weight shadow
		for(int i=0; i<this.weightMatrix.length; i++){
		
			this.weightMatrix[i].saveWeightShadow();
		}
	}
	
	/*
	 * Do backward propagate
	 * Make sure it has gone through a forward propagation operation before executing NeuralNetworks method 
	 */
	public void backwardPropagate(double[] desiredOutputValues){
		
		/*
		 * For output layer
		 */
		
		//calculate error on the output layer
		this.neuronLayer[this.numberOfLayer - 1].doCalculateError(desiredOutputValues);
		
		//calculate weight correction
		this.weightMatrix[this.weightMatrix.length - 1].calculateWeightCorrection(this.neuronLayer[this.numberOfLayer - 2].getInterface(), this.neuronLayer[this.numberOfLayer - 1].getErrorValues(), this.learningRate);
	
		//update the weight
		this.weightMatrix[this.weightMatrix.length - 1].updateAllWeights(this.momentumRate);

		//set the weight correction for momentum
		this.weightMatrix[this.weightMatrix.length - 1].setWeightCorrectionForMomentum();
		
		/*
		 * For hidden layer & input layer
		 */
		for(int i=this.weightMatrix.length - 2; i>=0; i--){
			
			//calculate error of the layer
			this.neuronLayer[i+1].doCalculateError(this.neuronLayer[i+2].getErrorValues(), weightMatrix[i + 1]);
			
			//calculate weight correction
			this.weightMatrix[i].calculateWeightCorrection(this.neuronLayer[i].getInterface(), this.neuronLayer[i+1].getErrorValues(), this.momentumRate);
			
			//update the weight
			this.weightMatrix[i].updateAllWeights(this.momentumRate);
		
			//set the weight correction for momentum
			this.weightMatrix[i].setWeightCorrectionForMomentum();
		}
	}
	
	/*
	 * Return all output values in the output layer
	 * Wrapped in LayerInterface object
	 */
	public LayerInterface getOutputValues(){
		
		return this.neuronLayer[this.numberOfLayer-1].getInterface();
	}
	
	
	/*
	 * Return the Name of the Neural Networks
	 */
	public String getNeuralNetworksName(){
		
		return this.neuralNetworksName;
	}
	
	/*
	 * Return Layer
	 * For development use
	 */
	public Layer getLayer(int layerNumber){
		
		return this.neuronLayer[layerNumber];
	}
	
	/*
	 * Set the momentum rate
	 * For GA use
	 */
	public void setMomentumRate(double momentumRate){
		
		this.momentumRate = momentumRate;
	}
	
	/*
	 * Set the learning rate
	 * For GA use
	 */
	public void setLearningRate(double learningRate){
		
		this.learningRate = learningRate;
	}
	
	/*
	 * Return the number of WeightMatrix
	 * For file writing use
	 */
	public int getWeightMatrixCount(){
		
		return this.weightMatrix.length;
	}
	
	/*
	 * Return WeightMatrix
	 * For file writing use
	 */
	public WeightMatrix getWeightMatrix(int i){
		
		return weightMatrix[i];
	}
}
