/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class InputNeuron extends Neuron{

	private final String neuronType = "INPUT";
	private double netInput = 0, inputValue = 0, outputValue = 0;
	
	public InputNeuron(int neuronNumber){
	
		super.setNeuronNumber(neuronNumber);
		super.setNeuronType(neuronType);
	}
	
	@Override
	public void calculateSummation() {
		
		this.netInput = inputValue;
	}

	@Override
	public void calculateOutput() {

		this.outputValue = this.netInput;
	}
	
	/*
	 * Set the input value of the neuron
	 */
	public void setInputValue(double inputValue){

		this.inputValue = inputValue;
	}
	
	public double getOutputValue(){
		
		return this.outputValue;
	}
}
