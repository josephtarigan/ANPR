package com.genetic;

import java.util.Arrays;

public class Genotype {

	private int gene[];
	
	/*
	 * constructor
	 * @genotypeLength -> the genotype length
	 */
	public Genotype(int genotypeLength){
		
		this.gene = new int[genotypeLength];
	}
	
	/*
	 * get all gene
	 */
	public int[] getGenes(){
		
		return this.gene;
	}
	
	/*
	 * get single gene at a position
	 */
	public int getGene(int position){
		
		return this.gene[position];
	}
	
	/*
	 * get genes at a range
	 */
	public int[] getGenes(int start, int end){
		
		return Arrays.copyOfRange(this.gene, start, end);
	}
	
	/*
	 * set single gene at position
	 */
	public void setGene(int position, int value){
		
		this.gene[position] = value;
	}
	
	/*
	 * decode the genotype
	 * output is hidden neuron number, learning rate & mutation rate
	 */
	public Phenotype decode(){
		
		/*
		 * 0-7 
		 */
		String hiddenNeuronGene = "";
		/*
		 * 8-16
		 */
		String learningRateGene = "";
		/*
		 * 17-25
		 */
		String momentumRateGene = "";
		/*
		 * the phenotype
		 */
		Phenotype phenotype = new Phenotype();
		
		/*
		 * hidden neuron
		 */
		for(int i=0; i<=7; i++){
			hiddenNeuronGene += this.gene[i];
		}
		phenotype.setHiddenNeuronCount(Integer.parseInt(hiddenNeuronGene, 2));
		
		/*
		 * learning rate
		 */
		for(int i=8; i<=16; i++){
			learningRateGene += this.gene[i];
		}
		phenotype.setLearningRate(((double)(Integer.parseInt(learningRateGene, 2))+1)/512);
		
		/*
		 * momentum rate
		 */
		for(int i=17; i<=25; i++){
			momentumRateGene += this.gene[i];
		}
		phenotype.setMomentumRate(((double)(Integer.parseInt(momentumRateGene, 2))+1)/512);
		
		/*
		 * finally
		 */
		return phenotype;
	}
}
