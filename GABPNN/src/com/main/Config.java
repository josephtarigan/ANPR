package com.main;

public class Config {

	public static final int POPULATION_SIZE = 50;
	public static final int MATING_POOL_SIZE = 10;
	public static final int INDIVIDUAL_GEN_LENGTH = 26;
	public static final int MAX_GENERATION = 150;
	public static final int MIN_HIDDEN_NEURON = 30;
	
	/*
	 * HN_GEN_LENGTH -> hidden neuron genotype length
	 * LR_GEN_LENGTH -> learning rate genotype length
	 * MR_GEN_LENGTH -> momentum rate genotype length
	 */
	public static final int HN_GEN_LENGTH = 8;
	public static final int LR_GEN_LENGTH = 9;
	public static final int MR_GEN_LENGTH = 9;
	
	/*
	 * mutation rate
	 */
	public static final double MUTATION_RATE = 0.1;
	
	/*
	 * max epoch per training
	 */
	public static final int MAX_EPOCH = 1600;
	
	/*
	 * target error
	 */
	public static double TARGET_ERROR = 0.000001;
}
