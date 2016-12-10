/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.main;

import java.io.FileNotFoundException;
import java.util.Random;

import com.charset.CharConfig;
import com.filehandler.DataWriter;
import com.genetic.GeneticAlgorithm;
import com.genetic.IGABPNN;
import com.genetic.Phenotype;
import com.main.uiobserver.UIObservable;
import com.main.uiobserver.UIObserver;
import com.neuralnetworks.AlphaNumericNeuralNetworks;

public class GABPNN implements IGABPNN, UIObservable{

	/*
	 * enumerator for status
	 */
	private static enum trainerStatuses {
		NOTREADY("Not Ready"), READY("Ready"), TRAINING("Training"), WRITING("Writing"), PAUSED("Paused"), FINISHED("Finished"), PARENTSELECTION("Selecting Parents"), SURVIVORSELECTION("Selecting Survivors"), CROSSOVER("Cross Overing"), MUTATION("Mutating"), GENERATING("Generating Individuals"), EVALUATE("Evaluate Recognition Rate"), NORMALISING("Normalising Fitness Values");
		private String value;
		
		private trainerStatuses(String s){
			this.value = s;
		}
		
		public String toString(){
			return this.value;
		}
	}
	
	public AlphaNumericNeuralNetworks alphaNumericNeuralNetworks;
	public GeneticAlgorithm geneticAlgorithm;
	
	/*
	 * for observers
	 */
	private String status;
	private int individualIEvaluate;
	private UIObserver gabpnnUIObserver;
	/*
	 * max epoch
	 */
	private int maxEpoch;
	/*
	 * statistical use
	 */
	private double cumulativeMSE;
	/*
	 * training flag use
	 */
	private final int POPULATION = 1;
	private final int OFFSPRING = 2;
	
	public GABPNN(){
	
		geneticAlgorithm = new GeneticAlgorithm();
		this.maxEpoch = Config.MAX_EPOCH;
	}
	
	/*
	 * experimental
	 */
	public double getFitnessValue(){
		
		double fitness = 0;
		Random rand = new Random();
		
		fitness = rand.nextDouble() / alphaNumericNeuralNetworks.getHiddenNeuronCount();
		
		return fitness;
	}
	
	private void trainNeuralNetworks(Phenotype phenotype, int individual, int population){
		
		//init
		alphaNumericNeuralNetworks = new AlphaNumericNeuralNetworks();
		//set hidden neuron
		alphaNumericNeuralNetworks.setHiddenNeuronCount(phenotype.getHiddenNeuronCount());
		//set learning rate
		alphaNumericNeuralNetworks.setLearningRate(phenotype.getLearningRate());
		//set momentum rate
		alphaNumericNeuralNetworks.setMomentumRate(phenotype.getMomentumRate());
		//re-initiate neural networks object
		alphaNumericNeuralNetworks.init();
		//cumulative MSE
		this.cumulativeMSE = 0;
		//local fitness variable
		double fitness = 0;
		
		/*
		 * train
		 */
		//first, set epoch to zero
		alphaNumericNeuralNetworks.setEpochToZero();
		//main loop, train till epoch == 312
		while(alphaNumericNeuralNetworks.getEpoch() <= this.maxEpoch){

			/*
			 * For each char set
			 */
			for(int i=0; i<CharConfig.CHARSET_COUNT; i++){
				/*
				 * Training
				 */
				alphaNumericNeuralNetworks.trainNeuralNetworks(i);
				/*
				 * get MSE
				 */
				cumulativeMSE += alphaNumericNeuralNetworks.getMSE();
			}
		}
		
		/*
		 * calculate fitness
		 */
		fitness = cumulativeMSE/(double)Config.MAX_EPOCH;
		
		/*
		 * put fitness into individual
		 * 1 = population, 2 = offspring
		 */
		if(population == OFFSPRING){
			geneticAlgorithm.getOffspringPopulation().getIndividual(individual).setFitnessValue(fitness);
		}else if(population == POPULATION){
			geneticAlgorithm.getIndividual(individual).setFitnessValue(fitness);
		}
	}
	
	/*
	 * train neural networks
	 * until MSE < 0.000001
	 */
	private boolean trainNeuralNetworks(Phenotype phenotype, String saveFilePath) throws FileNotFoundException{
		
		//init
		alphaNumericNeuralNetworks = new AlphaNumericNeuralNetworks();
		//set hidden neuron
		alphaNumericNeuralNetworks.setHiddenNeuronCount(phenotype.getHiddenNeuronCount());
		//set learning rate
		alphaNumericNeuralNetworks.setLearningRate(phenotype.getLearningRate());
		//set momentum rate
		alphaNumericNeuralNetworks.setMomentumRate(phenotype.getMomentumRate());
		//re-initiate neural networks object
		alphaNumericNeuralNetworks.init();
		//MSE
		double MSE = 256;
		
		/*
		 * train
		 */
		//first, set epoch to zero
		alphaNumericNeuralNetworks.setEpochToZero();
		//main loop, train till MSE < TARGET_ERROR
		while(MSE > Config.TARGET_ERROR){

			/*
			 * For each char set
			 */
			for(int i=0; i<CharConfig.CHARSET_COUNT; i++){
				
				/*
				 * Training
				 */
				alphaNumericNeuralNetworks.trainNeuralNetworks(i);
				MSE = alphaNumericNeuralNetworks.getMSE();
			}
		}
		
		/*
		 * if MSE < targetError, then write weights data & chart
		 */
		if(MSE < Config.TARGET_ERROR){
			
			alphaNumericNeuralNetworks.saveWeightData(saveFilePath);
		}
		
		return true;
	}
	
	/*
	 * GA optimising process
	 * @filePath path to save individual data
	 */
	public void evolute(String filePath) throws FileNotFoundException{
		
		/*
		 * for inverting use
		 */
		double localMax;
		
		/*
		 * the first 50 individuals
		 * =========================================================================================================================
		 */
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			
			//train
			this.status = trainerStatuses.TRAINING.toString();
			this.individualIEvaluate = i+1;
			this.notifyObserver();
			this.trainNeuralNetworks(geneticAlgorithm.getIndividual(i).getGenotype().decode(), i, POPULATION);
			
			//notify fitness observer
			geneticAlgorithm.notifyObserver();
		}
		/*
		 * =========================================================================================================================
		 */
		
		/*
		 * store the max fitness, before inverting
		 * sort, descending
		 * then get the first individual's fitness
		 * =========================================================================================================================
		 */
		geneticAlgorithm.getPopulation().sortDescending();
		localMax = geneticAlgorithm.getIndividual(0).getFitnessValue();
		/*
		 * =========================================================================================================================
		 */
		
		/*
		 * invert fitness value
		 * =========================================================================================================================
		 */
		geneticAlgorithm.getPopulation().invertFitnessValue(localMax);
		/*
		 * =========================================================================================================================
		 */
		
		/*
		 * sort, descending
		 * =========================================================================================================================
		 */
		geneticAlgorithm.getPopulation().sortDescending();
		/*
		 * =========================================================================================================================
		 */

		//notify fitness observer
		this.notifyObserver();
		geneticAlgorithm.notifyObserver();
		
		/*
		 * looping, until generation = 50
		 * =========================================================================================================================
		 */
		do{

			/*
			 * parent selection
			 * =========================================================================================================================
			 */
			this.status = trainerStatuses.PARENTSELECTION.toString();
			this.notifyObserver();
			geneticAlgorithm.selectParents();
			/*
			 * =========================================================================================================================
			 */
			
			/*
			 * do crossover
			 * =========================================================================================================================
			 */
			this.status = trainerStatuses.CROSSOVER.toString();
			this.notifyObserver();
			geneticAlgorithm.crossOver();
			/*
			 * =========================================================================================================================
			 */
			
			/*
			 * do mutation
			 * =========================================================================================================================
			 */
			this.status = trainerStatuses.MUTATION.toString();
			this.notifyObserver();
			geneticAlgorithm.mutate();
			/*
			 * =========================================================================================================================
			 */
			
			//notify fitness observer
			geneticAlgorithm.notifyObserver();
			
			/*
			 * train the offsprings
			 * =========================================================================================================================
			 */
			for(int i=0; i<Config.MATING_POOL_SIZE; i++){
				
				//train
				this.status = trainerStatuses.TRAINING.toString();
				this.individualIEvaluate = i+1;
				this.notifyObserver();
				this.trainNeuralNetworks(geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode(), i, OFFSPRING);
				
				//notify fitness label
				geneticAlgorithm.notifyObserver();
			}
			/*
			 * =========================================================================================================================
			 */
			
			/*
			 * check the highest fitness before inverting
			 * if the fitness is higher then localMax, revert the fitness of population, localMax = fitness, invert population and offsprings with the new localMax
			 * else, invert offspring with localMax
			 * 
			 * menghindari fitness yang tidak proporsional pada populasi dan offspring
			 * =========================================================================================================================
			 */
			geneticAlgorithm.getOffspringPopulation().sortDescending();
			if(localMax < geneticAlgorithm.getOffspringPopulation().getIndividual(0).getFitnessValue()){
				
				//revert
				geneticAlgorithm.getPopulation().revertFitnessValue(localMax);
				//localMax = fitness
				localMax = geneticAlgorithm.getOffspringPopulation().getIndividual(0).getFitnessValue();
				//invert
				geneticAlgorithm.getPopulation().invertFitnessValue(localMax);
				geneticAlgorithm.getOffspringPopulation().invertFitnessValue(localMax);
			}else{
				
				geneticAlgorithm.getOffspringPopulation().invertFitnessValue(localMax);
			}
			/*
			 * =========================================================================================================================
			 */
			
			/*
			 * survivor selection
			 * =========================================================================================================================
			 */
			this.status = trainerStatuses.SURVIVORSELECTION.toString();
			this.notifyObserver();
			geneticAlgorithm.selectSurvivors();
			/*
			 * =========================================================================================================================
			 */
			
			/*
			 * sort, descending
			 * =========================================================================================================================
			 */
			geneticAlgorithm.getPopulation().sortDescending();
			/*
			 * =========================================================================================================================
			 */
			
			//notify individual info label
			geneticAlgorithm.notifyObserver();
			
			/*
			 * write data
			 * =========================================================================================================================
			 */
			this.status = trainerStatuses.WRITING.toString();
			this.notifyObserver();
			DataWriter.writeData(geneticAlgorithm, filePath);
			/*
			 * =========================================================================================================================
			 */
			
			//notify fitness observer
			this.notifyObserver();
			geneticAlgorithm.notifyObserver();
			
			/*
			 * increase generation
			 * notify generation label
			 * =========================================================================================================================
			 */
			geneticAlgorithm.increaseGeneration();
			/*
			 * =========================================================================================================================
			 */
			
		}while(geneticAlgorithm.getGeneration() <= Config.MAX_GENERATION);
		/*
		 * =========================================================================================================================
		 */
		
		/*
		 * generation == 50
		 * get the best individual, by sorting the population descending, get the first individual
		 * train BPNN with phenotype from the individual in the 50th generation
		 * write weight data and topology
		 * =========================================================================================================================
		 */
		//train
		this.status = trainerStatuses.TRAINING.toString();
		this.notifyObserver();
		//geneticAlgorithm.getPopulation().sortDescending();
		
		if(this.trainNeuralNetworks(geneticAlgorithm.getIndividual(0).getGenotype().decode(), filePath)){
			/*
			 * done
			 */
			this.status = trainerStatuses.FINISHED.toString();
			this.notifyObserver();
		}
		/*
		 * =========================================================================================================================
		 */
	}

	@Override
	public void notifyObserver() {
		
		this.gabpnnUIObserver.update();
	}

	@Override
	public void addObserver(UIObserver observer) {
		
		this.gabpnnUIObserver = observer;
	}
	
	/*
	 * for UI
	 */
	public String getStatus(){
		
		return this.status;
	}
	
	/*
	 * for UI
	 */
	public int getIndividualInEvalute(){
		
		return this.individualIEvaluate;
	}
	
}
