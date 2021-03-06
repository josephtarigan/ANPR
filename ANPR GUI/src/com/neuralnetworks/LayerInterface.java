/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class LayerInterface {

	/*
	 * Hold all neurons output value from the layer attached to this interface
	 */
	
	public Node outputNodes[];
	private String layerName;
	private int nodesCount;
	
	/*
	 * Standard constructor, the layerName
	 */
	public LayerInterface(String layerName, int nodesCount){
		
		this.layerName = layerName;
		this.outputNodes = new Node[nodesCount];
		this.nodesCount = nodesCount;
		
		/*
		 * Build Nodes
		 */
		for(int i=0; i<this.nodesCount; i++){
			
			this.outputNodes[i] = new Node();
		}
	}
	
	/*
	 * Return the layer name
	 */
	public String getLayerName(){
		
		return this.layerName;
	}
	
	public Node getNode(int i){
		
		return this.outputNodes[i];
	}
	
	public int getNodesCount(){
		
		return this.nodesCount;
	}
	
	public double[] getSerializedOutput(){
		
		double[] output = new double[nodesCount];
		
		for(int i=0; i<nodesCount; i++){
			
			output[i] = this.getNode(i).getValue();
		}
		
		return output;
	}
}
