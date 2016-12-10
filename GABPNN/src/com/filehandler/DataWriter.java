/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.filehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.genetic.GeneticAlgorithm;
import com.main.Config;
import com.neuralnetworks.WeightMatrix;;

public class DataWriter {
	
	/*
	 * for BPNN use
	 * write weights data
	 */
	public static void writeFile(int neuronCount[], WeightMatrix weightMatrix, String filePath) throws FileNotFoundException{
	
		PrintWriter fWriter;
		File outputFile;
		
		outputFile = new File(filePath);
		fWriter = new PrintWriter(outputFile);
		
		/*
		 * Layer Configuration
		 * 
		 * First Line
		 * Neuron Count in Each Layer
		 */
		for(int i=0; i<neuronCount.length; i++){
			
			if(i < neuronCount.length - 1){
				
				fWriter.printf("%d, ", neuronCount[i]);
			}
			else{
				/*
				 * No comma
				 */
				fWriter.printf("%d", neuronCount[i]);
			}
		}
		
		fWriter.println();
		
		/*
		 * Weight Data
		 */
		
		for(int i=0; i<weightMatrix.getOriginLayerCount(); i++){
			for(int j=0; j<weightMatrix.getDestinationLayerCount() - 1; j++){
				
				fWriter.printf("%.25f, ", weightMatrix.getConnectionNode(i, j).getValue());
			}
			/*
			 * Last line
			 * Add println
			 */
			fWriter.printf("%.25f", weightMatrix.getConnectionNode(i, weightMatrix.getDestinationLayerCount() - 1).getValue());
			fWriter.println();
		}
		
		fWriter.close();
	}
	
	/*
	 * for GABPNN use
	 * write generation data
	 */
	public static void writeData(GeneticAlgorithm geneticAlgorithm, String filePath) throws FileNotFoundException{
		
		PrintWriter fWriter;
		File outputFile;
		
		/*
		 * path for individual generation
		 */
		String realPath = filePath + "\\generation " + geneticAlgorithm.getGeneration() + ".txt";
		
		outputFile = new File(realPath);
		fWriter = new PrintWriter(outputFile);
		int number;
		
		/*
		 * write generation number
		 */
		fWriter.printf("GENERATION : %d", geneticAlgorithm.getGeneration());
		fWriter.println();
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
		
		/*
		 * write population data
		 */
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.printf("POPULATION DATA");
		fWriter.println();
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
		for(int i=0; i<geneticAlgorithm.getPopulation().getPopulationSize(); i++){
			
			number = i+1;
			fWriter.printf("%sIndividual #%d%s", "----", number, "-----------------------------------");
			fWriter.println();
			fWriter.println();
			fWriter.printf("Genotype Data");
			fWriter.println();
			
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			for(int j=0; j<8; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			for(int j=8; j<17; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//momentum rate
			fWriter.printf("\n%-20s : ", "Momentum Rate");
			for(int j=17; j<26; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Phenotype Data");
			fWriter.println();
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			fWriter.printf("%d", geneticAlgorithm.getIndividual(i).getGenotype().decode().getHiddenNeuronCount());
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			fWriter.printf("%f", geneticAlgorithm.getIndividual(i).getGenotype().decode().getLearningRate());
			fWriter.println();
			
			//momentum rate
			fWriter.printf("%-20s : ", "Momentum Rate");
			fWriter.printf("%f", geneticAlgorithm.getIndividual(i).getGenotype().decode().getMomentumRate());
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Fitness : %f", geneticAlgorithm.getIndividual(i).getFitnessValue());
			fWriter.println();
			fWriter.println();
			fWriter.printf("%s", "-----------------------------------------------------");
			fWriter.println();
			fWriter.println();
		}
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
			
		/*
		 * write mating pool data
		 */
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.printf("MATING POOL DATA");
		fWriter.println();
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			number = i+1;
			fWriter.printf("%sIndividual #%d%s", "----", number, "-----------------------------------");
			fWriter.println();
			fWriter.println();
			fWriter.printf("Genotype Data");
			fWriter.println();
			
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			for(int j=0; j<8; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			for(int j=8; j<17; j++){
			
				fWriter.printf("%d", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//momentum rate
			fWriter.printf("\n%-20s : ", "Momentum Rate");
			for(int j=17; j<26; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Phenotype Data");
			fWriter.println();
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			fWriter.printf("%d", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getHiddenNeuronCount());
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			fWriter.printf("%f", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getLearningRate());
			fWriter.println();
			
			//momentum rate
			fWriter.printf("%-20s : ", "Momentum Rate");
			fWriter.printf("%f", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getMomentumRate());
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Fitness : %f", geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getFitnessValue());
			
			fWriter.printf("%s", "-----------------------------------------------------");
			fWriter.println();
			fWriter.println();
		}
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
		
		/*
		 * write offspring data
		 */
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.printf("OFFSPRING DATA");
		fWriter.println();
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			number = i+1;
			fWriter.printf("%sIndividual #%d%s", "----", number, "-----------------------------------");
			fWriter.println();
			fWriter.println();
			fWriter.printf("Genotype Data");
			fWriter.println();
			
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			for(int j=0; j<8; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			for(int j=8; j<17; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			//momentum rate
			fWriter.printf("\n%-20s : ", "Momentum Rate");
			for(int j=17; j<26; j++){
				
				fWriter.printf("%d", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGene(j));
			}
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Phenotype Data");
			fWriter.println();
			//hidden neuron
			fWriter.printf("%-20s : ", "Hidden Neuron");
			fWriter.printf("%d", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getHiddenNeuronCount());
			fWriter.println();
			
			//learning rate
			fWriter.printf("%-20s : ", "Learning Rate");
			fWriter.printf("%f", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getLearningRate());
			fWriter.println();
			
			//momentum rate
			fWriter.printf("%-20s : ", "Momentum Rate");
			fWriter.printf("%f", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getMomentumRate());
			fWriter.println();
			
			fWriter.println();
			fWriter.printf("Fitness : %f", geneticAlgorithm.getOffspringPopulation().getIndividual(i).getFitnessValue());
			
			fWriter.printf("%s", "-----------------------------------------------------");
			fWriter.println();
			fWriter.println();
		}
		fWriter.printf("=========================================================");
		fWriter.println();
		fWriter.println();
	
		fWriter.close();
	}
}
