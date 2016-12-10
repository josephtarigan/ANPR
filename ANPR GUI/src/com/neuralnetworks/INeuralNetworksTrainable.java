/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public interface INeuralNetworksTrainable {

	
	/*
	 * Statistics
	 */
	/*
	public double MSE = 0; // mean square error
	public double[] perSetMSE = null; //per set MSE
	*/
	
	/*
	 * Calculate the Per Set MSE
	 * Means to calculate MSE for one training iteration of character
	 * 
	 * @outputValues is the output values from forward propagation
	 * @desiredOutput is the desired output
	 * @setNumber is the number of the char set
	 */
	void calculatePerSetMSE(LayerInterface outputValues, double[] desiredOutput, int setNumber);
	/*
	private void calculatePerSetMSE(LayerInterface outputValues, double[] desiredOutput, int setNumber){
		
		this.perSetMSE[setNumber] = 0;
		
		for(int i=0; i<CharConfig.CHAR_COUNT; i++){
			
			this.perSetMSE[setNumber] += Math.pow(desiredOutput[i] - outputValues.getNode(i).getValue(), 2);
		}
	}
	*/
	
	/*
	 * Calculate the globalMSE
	 */
	void calculateMSE();
	/*
	private void calculateMSE(){
		
		this.MSE = 0;
		
		for(double value: perSetMSE){
			
			this.MSE += value;
		}
		
		this.MSE = this.MSE/2;
	}
	*/
	
	int getEpoch();
	
	double getMSE();
}
