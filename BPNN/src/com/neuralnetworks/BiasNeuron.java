/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class BiasNeuron extends Neuron{

	private final String neuronType = "BIAS";
	private double outputValue = 0.9; //normalized
	
	public BiasNeuron(int neuronNumber){
		
		super.setNeuronNumber(neuronNumber);
		super.setNeuronType(neuronType);
	}
	
	@Override
	public void calculateSummation() {
		
		/*
		 * Bias neuron always have its output value 1
		 */
	}

	@Override
	public void calculateOutput() {
		
		/*
		 * Bias neuron always have its output value 1
		 */
	}
	
	public double getOutputValue(){
		
		return this.outputValue;
	}

}
