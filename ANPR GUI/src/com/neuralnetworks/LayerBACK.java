package com.neuralnetworks;

public abstract class LayerBACK {

	protected String layerType, layerName;
	protected Neuron layerNeurons[];
	protected int neuronCount;
	protected LayerInterface layerInterface;
	protected double[] errorValues;
	
	public LayerBACK(String layerName, String layerType, int neuronCount){
		
		this.layerName = layerName;
		this.layerType = layerType;
		
	}
}
