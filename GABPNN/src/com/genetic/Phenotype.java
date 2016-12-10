package com.genetic;

public class Phenotype {

	/*
	 * number of hidden neuron
	 */
	int hiddenNeuronCount;
	
	/*
	 * learning rate
	 */
	double learningRate;
	
	/*
	 * momentum rate
	 */
	double momentumRate;
	
	public Phenotype(){
		/*
		 * empty
		 */
	}
	
	/*
	 * setter for hidden neuron count
	 */
	public void setHiddenNeuronCount(int hiddenNeuronCount){
		
		this.hiddenNeuronCount = hiddenNeuronCount;
	}
	
	/*
	 * setter for learning rate
	 */
	public void setLearningRate(double learningRate){
		
		this.learningRate = learningRate;
	}
	
	/*
	 * setter for momentum rate
	 */
	public void setMomentumRate(double momentumRate){
		
		this.momentumRate = momentumRate;
	}
	
	/*
	 * getter for hidden neuron count
	 */
	public int getHiddenNeuronCount(){
		
		return this.hiddenNeuronCount;
	}
	
	/*
	 * getter for learning rate
	 */
	public double getLearningRate(){
		
		return this.learningRate;
	}
	
	/*
	 * getter for momentum rate
	 */
	public double getMomentumRate(){
		
		return this.momentumRate;
	}
}
