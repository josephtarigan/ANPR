/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

import java.util.Random;

public class WeightMatrix {

	private Node weightNodes[][];
	private double[][] weightCorrection, weightCorrectionForMomentum;
	private String originLayer, destinationLayer;
	private int originLayerNeuronCount, destinationLayerNeuronCount;
	private Random random = new Random();
	
	/*
	 * Standard constructor
	 */	
	public WeightMatrix(String originLayer, String destinationLayer, int originNeuronCount, int destinationNeuronCount){
		
		this.destinationLayer = destinationLayer;
		this.originLayer = originLayer;
		this.originLayerNeuronCount = originNeuronCount;
		this.destinationLayerNeuronCount = destinationNeuronCount;
	
		/*
		 * Initialization
		 */
		
		this.init();
	}
	
	/*
	 * Build the connections
	 */
	public void init(){
		
		this.weightNodes = new Node[originLayerNeuronCount ][destinationLayerNeuronCount];
		this.weightCorrection = new double[originLayerNeuronCount][destinationLayerNeuronCount];
		this.weightCorrectionForMomentum = new double[originLayerNeuronCount][destinationLayerNeuronCount];
		
		/*
		 * Set all values in weightCorrectionForMomentum to 0
		 * Construct all nodes
		 */
		for(int i=0; i<this.originLayerNeuronCount; i++){
			for(int j=0; j<this.destinationLayerNeuronCount; j++){
				
				/*
				 * Momentum
				 */
				this.weightCorrectionForMomentum[i][j] = 0;
				/*
				 * Node
				 */
				this.weightNodes[i][j] = new Node();
			}
		}
	}
	
	/*
	 * Randomize all connection's weight
	 */
	public void randomizeWeight(){
		
		for(int i=0; i<this.originLayerNeuronCount; i++){
			for(int j=0; j<this.destinationLayerNeuronCount; j++){
				this.weightNodes[i][j].setValue((random.nextDouble() * 1) - 0.5);
			}
		}
	}
	
	public String getOriginLayerName(){
		
		return this.originLayer;
	}
	
	public String getDestinationLayerName(){
		
		return this.destinationLayer;
	}
	
	/*
	 * Update the weight, replacing the old one
	 */
	public void updateWeight(int i, int j, double newValue){
		
		this.weightNodes[i][j].setValue(newValue);
	}
	
	/*
	 * Update all the weight simultaneously
	 * Bias neuron is included in the originLayerNeuronCount --> uses Layer.getOutputNodesCount()
	 * Bias neuron is not included in the destinationLayerNeuronCount --> uses Layer.getNeuronCount()
	 */
	public void updateAllWeights(double momentum){
		
		for(int x=0; x<this.originLayerNeuronCount; x++){
			for(int y=0; y<this.destinationLayerNeuronCount; y++){
				
				this.weightNodes[x][y].setValue(this.weightNodes[x][y].getValue() + this.weightCorrection[x][y] + (weightCorrectionForMomentum[x][y] * momentum));
			}
		}
	}
	
	
	/*
	 * Update all the weight simultaneously
	 */
	/*
	public void updateWeight(int i, int j, double momentum){
		
		for(int x=0; x<i; x++){
			for(int y=0; y<j; y++){
				
				this.weightNodes[x][y].setValue(this.weightNodes[x][y].getValue() + this.weightCorrection[x][y] + (this.weightCorrectionForMomentum[x][y] * momentum));
			}
		}
	}
	*/
	
	/*
	 * Return individual node
	 */
	public Node getConnectionNode(int from, int to){
		
		return this.weightNodes[from][to];
	}
	
	/*
	 * Calculate weight correction
	 * Bias neuron is included in the originLayerNeuronCount --> uses Layer.getOutputNodesCount()
	 * Bias neuron is not included in the destinationLayerNeuronCount --> uses Layer.getNeuronCount()
	 */
	public void calculateWeightCorrection(LayerInterface outputValue, double[] errorValue, double trainingRate){
		
		for(int i=0; i<originLayerNeuronCount; i++){
			for(int j=0; j<destinationLayerNeuronCount; j++){
				this.weightCorrection[i][j] = (trainingRate * errorValue[j] * outputValue.getNode(i).getValue());
			}
		}
	}
	
	/*
	 * Set values for n-1 iteraion's weightCorrection
	 * This method should be called at the very last step of back propagation method
	 * To set the n-1 values for the next iteration
	 */
	public void setWeightCorrectionForMomentum(){
		
		this.weightCorrectionForMomentum = this.weightCorrection;
	}
	
	/*
	 * Return original layer nodes count
	 */
	public int getOriginLayerCount(){
		
		return this.originLayerNeuronCount;
	}
	
	/*
	 * Return destination layer nodes count
	 */
	public int getDestinationLayerCount(){
		
		return this.destinationLayerNeuronCount;
	}
}
