/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.neuralnetworks;

public class Node {
	
	private double nodeValue;
	
	/*
	 * Default empty constructor
	 */
	public Node(){
	}
	
	/*
	 * Parameterized constructor
	 */
	public Node(double nodeValue){
		
		this.nodeValue = nodeValue;
	}
	
	public double getValue(){
		
		return this.nodeValue;
	}
	
	public void setValue(double nodeValue){
		
		this.nodeValue = nodeValue;
	}
}
