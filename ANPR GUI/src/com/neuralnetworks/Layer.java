/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class Layer {

	private String layerType, layerName;
	private Neuron layerNeurons[];
	private int neuronCount;
	private LayerInterface layerInterface;
	private double[] errorValues;
	
	public Layer(String layerName, String layerType, int neuronCount){
		
		this.layerName = layerName;
		this.layerType = layerType;
		
		/*
		 * If it isn't an output layer, get a bias neuron in this layer
		 */
		if(this.layerType.equals("OUTPUT")){
			this.neuronCount = neuronCount;
		}
		else{
			this.neuronCount = neuronCount + 1;
		}
		
		/*
		 * Initialization
		 */
		
		this.init();
	}
	
	/*
	 * Initialization method
	 */
	private void init(){
		
		/*
		 * Output layer doesn't have a bias neuron
		 */
		if(layerType.equals("OUTPUT")){
			
			this.layerNeurons = new Neuron[neuronCount];
			this.layerInterface = new LayerInterface(layerName, neuronCount);
			
			for(int i=0; i<neuronCount; i++){
				
				this.layerNeurons[i] = new OutputNeuron(i);
			}
		}
		else if(layerType.equals("HIDDEN")){
			
			this.layerNeurons = new Neuron[neuronCount];
			this.layerInterface = new LayerInterface(layerName, neuronCount);
			
			for(int i=0; i<neuronCount - 1; i++){
			
				this.layerNeurons[i] = new HiddenNeuron(i);
			}
			
			/*
			 * The bias
			 */
			this.layerNeurons[neuronCount - 1] = new BiasNeuron(neuronCount - 1);
		}
		else if(layerType.equals("INPUT")){
			
			this.layerNeurons = new Neuron[neuronCount];
			this.layerInterface = new LayerInterface(layerName, neuronCount);
			
			for(int i=0; i<neuronCount - 1; i++){
				
				this.layerNeurons[i] = new InputNeuron(i);
			}
			
			/*
			 * The bias
			 */
			this.layerNeurons[neuronCount - 1] = new BiasNeuron(neuronCount - 1);
		}
	}
	
	/*
	 * Overloaded method
	 * Set the input value into neurons all at once
	 * Only for INPUT type
	 */
	public void setNeuronInputNodes(double[] inputValue){
		
		for(int i=0; i<neuronCount - 1; i++){

			((InputNeuron) this.layerNeurons[i]).setInputValue(inputValue[i]);
		}
	}
	
	/*
	 * Overloaded method
	 * Set the input value into neurons all at once
	 */
	public void setNeuronInputNodes(LayerInterface inputNode, WeightMatrix weightMatrix){
		
		if(layerType.equals("OUTPUT")){
			for(int i=0; i<neuronCount; i++){
	
				((OutputNeuron) this.layerNeurons[i]).setInputNodes(inputNode, weightMatrix);
			}
		}
		else if(layerType.equals("HIDDEN")){
			for(int i=0; i<neuronCount - 1; i++){
				
				((HiddenNeuron) this.layerNeurons[i]).setInputNodes(inputNode, weightMatrix);
			}
		}
	}
	
	/*
	 * Do summation on all neurons
	 */
	public void doSummation(){
		
		if(layerType.equals("OUTPUT")){
			for(int i=0; i<neuronCount; i++){
				
				this.layerNeurons[i].calculateSummation();
			}
		}
		else{
			for(int i=0; i<neuronCount-1; i++){
				
				this.layerNeurons[i].calculateSummation();
			}
		}
	}
	
	/*
	 * Do activation on all neurons
	 * Store all output values into the interface
	 */
	public void doActivation(){
		
		if(layerType.equals("OUTPUT")){
			for(int i=0; i<neuronCount; i++){
				
				this.layerNeurons[i].calculateOutput();
			}
		}
		else{
			for(int i=0; i<neuronCount - 1; i++){
				
				this.layerNeurons[i].calculateOutput();
			}
		}
	}
	
	/*
	 * Store all output values into the interface
	 */
	public void fetchOutputValues(){
		
		if(layerType.equals("OUTPUT")){
			
			for(int i=0; i<neuronCount; i++){
				
				layerInterface.getNode(i).setValue(((OutputNeuron) this.layerNeurons[i]).getOutputValue());
			}
		}
		else if(layerType.equals("HIDDEN")){
			
			for(int i=0; i<neuronCount - 1; i++){
				
				layerInterface.getNode(i).setValue(((HiddenNeuron) this.layerNeurons[i]).getOutputValue());
			}
			
			/*
			 * The bias
			 */
			layerInterface.getNode(neuronCount - 1).setValue(((BiasNeuron) this.layerNeurons[neuronCount - 1]).getOutputValue());
		}
		else if(layerType.equals("INPUT")){
					
			for(int i=0; i<neuronCount - 1; i++){
				
				layerInterface.getNode(i).setValue(((InputNeuron) this.layerNeurons[i]).getOutputValue());
			}
			
			/*
			 * The bias
			 */
			layerInterface.getNode(neuronCount - 1).setValue(((BiasNeuron) this.layerNeurons[neuronCount - 1]).getOutputValue());			
		}
	}
	
	/*
	 * Calculate error for back propagation operation
	 * This method is supposed to be used only in output layer
	 */
	public void doCalculateError(double[] desiredOutput){
		

		this.errorValues = new double[neuronCount];
		
		for(int i=0; i<neuronCount; i++){
			
			((OutputNeuron) this.layerNeurons[i]).calculateError(desiredOutput[i]);
			this.errorValues[i] = ((OutputNeuron) this.layerNeurons[i]).getErrorValue();
		}
	}
	
	/*
	 * Calculate error for backpropagation operation
	 * This method is supposed to be used only in hidden layer
	 * 
	 * @calculatedError is the error collection from the above layer
	 * @weightMatrix is the WeightMatrix that connects i layer to i+1 layer
	 */
	public void doCalculateError(double[] calculatedError, WeightMatrix weightMatrix){
		
		this.errorValues = new double[neuronCount - 1];
		
		for(int i=0; i<neuronCount - 1; i++){
			
			((HiddenNeuron) this.layerNeurons[i]).calculateError(calculatedError, weightMatrix);
			this.errorValues[i] = ((HiddenNeuron) this.layerNeurons[i]).getErrorValue();
		}
	}
	
	/*
	 * Return the collection of neurons output --> layerInterface
	 */
	public LayerInterface getInterface(){
		
		return this.layerInterface;
	}
	
	/*
	 * Get individual neuron
	 */
	public Neuron getNeuron(int neuronNumber){
		
		return this.layerNeurons[neuronNumber];
	}
	
	/*
	 * Return the type
	 */
	public String getType(){
		
		return this.layerType;
	}
	
	/*
	 * Return the name
	 */
	public String getName(){
		
		return this.layerName;
	}
	
	/*
	 * Return the error values
	 */
	public double[] getErrorValues(){
		
		return this.errorValues;
	}
	
	/*
	 * Return the neuron count
	 * Return the exact number of neuron if this layer is an output layer
	 * Return the number minus 1 if else due to a bias neuron
	 */
	public int getNeuronCount(){
		
		if(layerType.equals("OUTPUT")){
			
			return this.neuronCount;
		}
		else{
			
			return this.neuronCount - 1;
		}
	}
	
	/*
	 * Return number of neuron that has an output node
	 * This means return count off all neuron regardless its type
	 */
	public int getOutputNodesCount(){
		
		return this.layerInterface.getNodesCount();
	}
}
