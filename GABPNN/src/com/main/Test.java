package com.main;

import java.util.Random;

import com.genetic.Phenotype;
import com.genetic.Population;

public class Test {
	
	public static void main(String[] args){
		
		Population population = new Population(Config.POPULATION_SIZE);
		Phenotype phenotype = new Phenotype();
		Random rand = new Random();
		
		for(int i=0; i<Config.INDIVIDUAL_GEN_LENGTH; i++){
			
			System.out.printf("%d", population.getIndividual(0).getGenotype().getGene(i));
		}
		
		phenotype = population.getIndividual(0).getGenotype().decode();
		
		System.out.printf("\n\nHidden Neuron: %d\nLearning Rate: %f\nMomentum Rate: %f", phenotype.getHiddenNeuronCount(), phenotype.getLearningRate(), phenotype.getMomentumRate());
		
		System.out.printf("\n\n%f", rand.nextDouble());
	}
}
