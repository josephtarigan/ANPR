package com.genetic;

import java.util.Random;
import com.main.Config;
import com.main.uiobserver.UIObservable;
import com.main.uiobserver.UIObserver;

public class GeneticAlgorithm implements UIObservable{

	private Population population;
	private Population matingPool;
	private Population offspringPopulation;
	private Population sortedPool;
	private int generation;
	
	private UIObserver gaUIObserver;

	public GeneticAlgorithm(){
		
		population = new Population(Config.POPULATION_SIZE);
		matingPool = new Population(Config.MATING_POOL_SIZE);
		offspringPopulation = new Population(Config.MATING_POOL_SIZE);

		/*
		 * init
		 */
		this.init();
	}
	
	/*
	 * initial
	 * randomise genes in each individual
	 * generation = 1
	 */
	private void init(){
		
		this.gaUIObserver = null;
		
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			
			population.getIndividual(i).randomiseAllele();
		}
		
		population.validate();
		generation = 1;
	}
	
	/*
	 * parent selection
	 */
	public void selectParents(){
		
		Random rand = new Random();
		int position = 0;
		double randomNumber = 0;
		
		matingPool = new Population(Config.MATING_POOL_SIZE);
		offspringPopulation =  new Population(Config.MATING_POOL_SIZE);
		sortedPool = new Population(Config.POPULATION_SIZE);
		
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			sortedPool.setIndividual(i, population.getIndividual(i));
		}
		
		/*
		 * sort by fitness
		 */
		sortedPool.sortAscending();
		
		/*
		 * calculate fitness proportional
		 */
		sortedPool.calculateFitnessProportional();
		
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			position = 0;
			randomNumber = rand.nextFloat();
			
			while(sortedPool.getFitnessProportionalValue(position) < randomNumber && position < Config.POPULATION_SIZE-1){
				
				position++;
			}
			
			matingPool.setIndividual(i, sortedPool.getIndividual(position));
		}
		
		matingPool.validate();
	}
	
	/*
	 * crossover operation
	 * select 2 individuals randomly from mating pool
	 * do crossover
	 * put offsprings in offspringPopulation
	 */
	public void crossOver(){
		
		Random rand = new Random();
		int parent1Pos, parent2Pos;
		double geneProbability[] = new double[Config.INDIVIDUAL_GEN_LENGTH];
		Individual offspring1, offspring2;
		
		/*
		 * do 5 times, 2*5 = 10 offspring
		 */
		for(int i=0; i<5; i++){
			
			if(rand.nextDouble() > 0.5){
				
				/*
				 * do crossover
				 * randomise, to get 2 parents
				 * do while loop, if parent1 == parent2
				 */
				parent1Pos = rand.nextInt(Config.MATING_POOL_SIZE);
				do{
					parent2Pos = rand.nextInt(Config.MATING_POOL_SIZE);
				}while(parent1Pos == parent2Pos);
				
				/*
				 * built individual
				 */
				offspring1 = new Individual(i*2);
				offspring2 = new Individual(i*2+1);
				
				/*
				 * do crossover
				 */
				for(int j=0; j<Config.INDIVIDUAL_GEN_LENGTH; j++){
					
					geneProbability[j] = rand.nextDouble();
					
					if(geneProbability[j] > 0.5){
						
						//crossover
						offspring1.getGenotype().setGene(j, matingPool.getIndividual(parent2Pos).getGenotype().getGene(j));
						offspring2.getGenotype().setGene(j, matingPool.getIndividual(parent1Pos).getGenotype().getGene(j));
					}
					else{
						
						//no crossover
						offspring1.getGenotype().setGene(j, matingPool.getIndividual(parent1Pos).getGenotype().getGene(j));
						offspring2.getGenotype().setGene(j, matingPool.getIndividual(parent2Pos).getGenotype().getGene(j));
					}
				}
				
				/*
				 * put into offspring population
				 */
				offspringPopulation.setIndividual(i*2, offspring1);
				offspringPopulation.setIndividual((i*2)+1, offspring2);
			}
			else{
				
				/*
				 * no crossover
				 */
				offspringPopulation.setIndividual(i*2, population.getIndividual(i*2));
				offspringPopulation.setIndividual((i*2)+1, population.getIndividual((i*2)+1));
			}
			
			/*
			 * reset fitness value
			 */
			//this.offspringPopulation.getIndividual(i*2).setFitnessValue(0);
			//this.offspringPopulation.getIndividual((i*2)+1).setFitnessValue(0);
		}
		
		offspringPopulation.validate();
	}
	
	/*
	 * mutation operator
	 * on each individual
	 */
	public void mutate(){
		
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			offspringPopulation.getIndividual(i).mutate();
		}
		
		offspringPopulation.validate();
	}
	
	/*
	 * survivor selection
	 * 
	 */
	public void selectSurvivors(){
		
		Population combinedPopulation = new Population(Config.POPULATION_SIZE + Config.MATING_POOL_SIZE);
		
		/*
		 * put original individuals into queue
		 */
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			
			combinedPopulation.setIndividual(i, population.getIndividual(i));
		}
		
		/*
		 * put offsprings into population
		 */
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			combinedPopulation.setIndividual(i + Config.POPULATION_SIZE, offspringPopulation.getIndividual(i));
		}
		
		/*
		 * Sort individuals by its fitness
		 */
		combinedPopulation.sortDescending();
		
		/*
		 * select the 50 best fitness
		 */
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			
			population.setIndividual(i, combinedPopulation.getIndividual(i));
		}
		
		population.validate();
	}
	
	/*
	 * get individual
	 */
	public Individual getIndividual(int number){
		
		return population.getIndividual(number);
	}
	
	/*
	 * get generation
	 */
	public int getGeneration(){
		
		return this.generation;
	}
	
	/*
	 * increase generation
	 */
	public void increaseGeneration(){
		
		this.generation++;
		this.notifyObserver();
	}
	
	/*
	 * get offspring population
	 */
	public Population getOffspringPopulation(){
		
		return offspringPopulation;
	}
	
	/*
	 * get mating pool population
	 */
	public Population getMatingPoolPopulation(){
		
		return matingPool;
	}
	
	/*
	 * get population
	 */
	public Population getPopulation(){
		
		return population;
	}

	@Override
	public void notifyObserver() {
		
		this.gaUIObserver.update();
	}

	@Override
	public void addObserver(UIObserver observer) {
		
		this.gaUIObserver = observer;
	}
}
