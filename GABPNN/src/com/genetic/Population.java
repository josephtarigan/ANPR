/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.genetic;

import com.main.Config;

public class Population {

	private int populationSize;
	private Individual individual[];
	private double[] fitnessProportional;
	
	public Population(int populationSize){
		
		this.populationSize = populationSize;
		this.individual = new Individual[populationSize];
		
		/*
		 * init
		 */
		this.init();
	}
	
	private void init(){
		
		/*
		 * build the population
		 */
		for(int i=0; i<this.populationSize; i++){
			
			this.individual[i] = new Individual(i);
		}
	}
	
	/*
	 * get single individual at position
	 */
	public Individual getIndividual(int number){
		
		return this.individual[number];
	}
	
	/*
	 * get all individual
	 */
	public Individual[] getAllIndividual(){
		
		return this.individual;
	}
	
	/*
	 * set individual into population
	 */
	public void setIndividual(int position, Individual individual){
		
		/*
		 * this, will pass by reference
		 * NOT GOOD
		 */
		//this.individual[position] = individual;
		
		/*
		 * use the primitives data
		 */
		/*
		 * copy the genes
		 */
		for(int i=0; i<Config.INDIVIDUAL_GEN_LENGTH; i++){
			
			this.individual[position].getGenotype().setGene(i, individual.getGenotype().getGene(i));
		}
		
		/*
		 * copy fitness 
		 */
		this.individual[position].setFitnessValue(individual.getFitnessValue());
		
		/*
		 * copy individual number
		 */
		this.individual[position].setIndividualNumber(individual.getIndividualNumber());
	}
	
	public void calculateFitnessProportional(){
		
		this.fitnessProportional = new double[this.populationSize];
		double max = 0;
		
		//find the max
		for(int i=0; i<this.populationSize; i++){
			
			if(individual[i].getFitnessValue() > max){
				
				max = individual[i].getFitnessValue();
			}
		}
		
		//go
		for(int i=0; i<this.populationSize; i++){
			
			double fitness = this.individual[i].getFitnessValue()/(max);
			fitnessProportional[i] = fitness;
		}
	}
	
	/*
	 * get fitness proportional value of a position
	 * make sure to run calculateFitnessProportional before using this function
	 */
	public double getFitnessProportionalValue(int position){
		
		return this.fitnessProportional[position];
	}
	
	/*
	 * sort individual by fitness
	 * descending, highest first
	 * bubble sort
	 */
	public void sortDescending(){
		
		Individual tempIndividual;
		
		for(int i=0; i<this.populationSize-1; i++){
			for(int j=i+1; j<this.populationSize; j++){
				
				//if i<i+1, change position
				if(this.individual[i].getFitnessValue() < this.individual[j].getFitnessValue()){
					
					tempIndividual = this.individual[j];
					this.individual[j] = this.individual[i];
					this.individual[i] = tempIndividual;
				}
			}
		}
	}
	
	/*
	 * sort individual by fitness
	 * ascending, lowest first
	 * bubble sort
	 */
	public void sortAscending(){
		
		Individual tempIndividual;
		
		for(int i=0; i<this.populationSize-1; i++){
			for(int j=i+1; j<this.populationSize; j++){
				
				//if i>i+1, change position
				if(this.individual[i].getFitnessValue() > this.individual[j].getFitnessValue()){
					
					tempIndividual = this.individual[j];
					this.individual[j] = this.individual[i];
					this.individual[i] = tempIndividual;
				}
			}
		}
	}
	
	/*
	 * get population size
	 */
	public int getPopulationSize(){
		
		return this.populationSize;
	}
	
	/*
	 * validate individual
	 * if individual has less than the minimum hidden neuron then randomise allele
	 */
	public void validate(){
		
		for(int i=0; i<this.populationSize; i++){
			
			this.individual[i].validate();
		}
	}
	
	/*
	 * inverse fitness value
	 * to min-max from max-min
	 * pastikan semua individu sudah punya fitness terlebih dahulu
	 */
	public void invertFitnessValue(){
		
		double max = 0;
		
		//find the max
		for(int i=0; i<this.populationSize; i++){
			
			if(individual[i].getFitnessValue() > max){
				
				max = individual[i].getFitnessValue();
			}
		}
		
		//fix the fitness
		for(int i=0; i<this.populationSize; i++){
			
			double fitness = max - this.individual[i].getFitnessValue();
			this.individual[i].setFitnessValue(fitness);
		}
	}
	
	/*
	 * inverse fitness value
	 * to min-max from max-min
	 * using a predefined max
	 * pastikan semua individu sudah punya fitness terlebih dahulu
	 */
	public void invertFitnessValue(double max){
		
		//fix the fitness
		for(int i=0; i<this.populationSize; i++){
			
			double fitness = max - this.individual[i].getFitnessValue();
			this.individual[i].setFitnessValue(fitness);
		}
	}
	
	/*
	 * reverse fitness value
	 * to max-min from min-max
	 */
	public void revertFitnessValue(){
		
		double max = 0;
		
		//find the max
		for(int i=0; i<this.populationSize; i++){
			
			if(individual[i].getFitnessValue() > max){
				
				max = individual[i].getFitnessValue();
			}
		}
		
		//fix the fitness
		for(int i=0; i<this.populationSize; i++){
			
			double fitness = max - this.individual[i].getFitnessValue();
			this.individual[i].setFitnessValue(fitness);
		}
	}
	
	/*
	 * reverse fitness value
	 * using predefined max
	 * to max-min from min-max
	 */
	public void revertFitnessValue(double max){
		
		//fix the fitness
		for(int i=0; i<this.populationSize; i++){
			
			double fitness = max - this.individual[i].getFitnessValue();
			this.individual[i].setFitnessValue(fitness);
		}
	}
}
