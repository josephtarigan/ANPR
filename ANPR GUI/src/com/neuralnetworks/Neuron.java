/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public abstract class Neuron {

	private String neuronType;
	private int neuronNumber;
	
	
	/*
	 * 	Return the neuron type, either input, hidden, output or bias
	 */
	public String getNeuronType(){
		
		return this.neuronType;
	}
	
	/*
	 *  Set the neuron type, this method is binded with the constructor of the neuron object
	 */
	protected void setNeuronType(String type){
		
		this.neuronType = type;
	}
	
	/*
	 *  Calculate summation of input node weights
	 */
	public abstract void calculateSummation();
	
	/*
	 *  Calculate the output value of the neuron
	 *  Formula used depends on neuron type
	 *  Input & bias neurons don't do activation, they only pass its input to the output
	 */
	public abstract void calculateOutput();
	
	
	/*
	 * This method will return the neuron number
	 */
	public int getNeuronNumber(){
		
		return this.neuronNumber;
	}
	
	public void setNeuronNumber(int neuronNumber){
		
		this.neuronNumber = neuronNumber;
	}
}
