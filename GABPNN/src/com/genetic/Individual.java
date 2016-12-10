/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.genetic;

import java.util.Random;

import com.main.Config;

public class Individual {

	private double fitnessValue;
	private Genotype genotype;
	private int individualNumber;
	
	public Individual(int individualNumber){
		
		this.individualNumber = individualNumber;
		this.fitnessValue = 0;
		this.genotype = new Genotype(Config.INDIVIDUAL_GEN_LENGTH);
	}
	
	public Individual(){}
	
	/*
	 * return individual number
	 */
	public int getIndividualNumber(){
		
		return this.individualNumber;
	}
	
	/*
	 * set individual number
	 */
	public void setIndividualNumber(int number){
		
		this.individualNumber = number;
	}
	
	/*
	 * randomise value in each gene, 0 or 1
	 */
	public void randomiseAllele(){
		
		for(int i=0; i<Config.INDIVIDUAL_GEN_LENGTH; i++){
			
			Random rand = new Random();
			
			this.genotype.setGene(i, rand.nextInt(2));
		}
	}
	
	public void randomiseHiddenNeuronAllele(){
		
		for(int i=0; i<8; i++){
			
			Random rand = new Random();
			
			this.genotype.setGene(i, rand.nextInt(2));
		}
	}
	
	/*
	 * mutation operation
	 */
	public void mutate(){
		
		double geneProbability[] = new double[Config.INDIVIDUAL_GEN_LENGTH];
		Random rand = new Random();
		
		/*
		 * randomise [0 - 1]
		 * then check probability, if < 0.1 then mutate
		 */
		for(int i=0; i<Config.INDIVIDUAL_GEN_LENGTH; i++){
			
			geneProbability[i] = rand.nextDouble();
			
			if(geneProbability[i] <= Config.MUTATION_RATE){
				
				if(this.getGenotype().getGene(i) == 1){
					
					this.getGenotype().setGene(i, 0);
				}
				else{
					
					this.getGenotype().setGene(i, 1);
				}
			}
		}
	}
	
	/*
	 * get genotype
	 */
	public Genotype getGenotype(){
		
		return this.genotype;
	}
	
	/*
	 * get fitness value
	 */
	public double getFitnessValue(){
		
		return this.fitnessValue;
	}
	
	/*
	 * set fitness value
	 */
	public void setFitnessValue(double fitnessValue){
		
		this.fitnessValue = fitnessValue;
	}
	
	/*
	 * check genotype
	 * if hidden neuron count = 0, randomise
	 */
	public void validate(){
		
		if(this.genotype.decode().hiddenNeuronCount < Config.MIN_HIDDEN_NEURON){
			
			this.randomiseHiddenNeuronAllele();
		}
	}
}
