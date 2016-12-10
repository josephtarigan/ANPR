/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class HiddenNeuron extends Neuron{

	private final String neuronType = "HIDDEN";
	private double netInput = 0, outputValue = 0, errorValue = 0;
	private LayerInterface inputValues;
	private int inputNodesCount;
	private WeightMatrix weightMatrix;
	
	public HiddenNeuron(int neuronNumber){
		
		super.setNeuronType(neuronType);
		super.setNeuronNumber(neuronNumber);
	}
	
	/*
	 * Set in the layer interface & weight matrix
	 */
	public void setInputNodes(LayerInterface inputValues, WeightMatrix weightMatrix){
		
		this.inputValues = inputValues;
		this.inputNodesCount = this.inputValues.outputNodes.length;
		this.weightMatrix = weightMatrix;
	}
	
	@Override
	public void calculateSummation() {
		
		/*
		 * Calculate each input connection that comes to this neuron
		 * Input value * input weight
		 */
		this.netInput = 0;
		
		for(int i=0; i<inputNodesCount; i++){
			
			this.netInput += inputValues.getNode(i).getValue() * weightMatrix.getConnectionNode(i, super.getNeuronNumber()).getValue();
		}
	}

	@Override
	public void calculateOutput() {
		
		/*
		 * Sigmoid function
		 * 1 / (1 + e^-netInput)
		 */	
		this.outputValue = (1/(1+Math.exp(-1*this.netInput)));
		
		/*
		 * We need to normalize the output
		 */		
		if(this.outputValue > 0.9){
			this.outputValue = 0.9;
		}
		else if(this.outputValue < 0.1){
			this.outputValue = 0.1;
		}
	}

	public void calculateError(double[] calculatedError, WeightMatrix calculatedWeightMatrix) {
		
		double summation;
		
		summation = 0;
		for(int j=0; j<calculatedError.length; j++){
	
			summation += calculatedError[j] * calculatedWeightMatrix.getConnectionNode(super.getNeuronNumber(), j).getValue();
		}
		
		this.errorValue = summation * (this.outputValue * (1 - this.outputValue));
	}
	
	/*
	 * Return the error value
	 */
	public double getErrorValue(){
		
		return this.errorValue;
	}
	
	/*
	 * Return the output value
	 */
	public double getOutputValue(){
		
		return this.outputValue;
	}	
}
