/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.charset;

import java.util.Arrays;

public class Alphanumeric {
	
	/*
	 * Public modifier
	 */
	public CharacterRepresentation[] character = new CharacterRepresentation[36];
	
	private double[] code;
	private double threshold;
	
	/*
	 * Build
	 * a-z & 0-9
	 */
	public Alphanumeric(){
		
		//a
		this.code = new double[] {0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[0] = new CharacterRepresentation('a', code);
		
		//b
		this.code = new double[] {0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[1] = new CharacterRepresentation('b', code);
		
		//c
		this.code = new double[] {0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[2] = new CharacterRepresentation('c', code);
		
		//d
		this.code = new double[] {0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[3] = new CharacterRepresentation('d', code);
		
		//e
		this.code = new double[] {0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[4] = new CharacterRepresentation('e', code);
		
		//f
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[5] = new CharacterRepresentation('f', code);
		
		//g
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[6] = new CharacterRepresentation('g', code);
		
		//h
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[7] = new CharacterRepresentation('h', code);
		
		//i
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[8] = new CharacterRepresentation('i', code);
		
		//j
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[9] = new CharacterRepresentation('j', code);
		
		//k
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[10] = new CharacterRepresentation('k', code);
		
		//l
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[11] = new CharacterRepresentation('l', code);
		
		//m
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[12] = new CharacterRepresentation('m', code);
		
		//n
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[13] = new CharacterRepresentation('n', code);
		
		//o
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[14] = new CharacterRepresentation('o', code);
		
		//p
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[15] = new CharacterRepresentation('p', code);
		
		//q
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[16] = new CharacterRepresentation('q', code);
		
		//r
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[17] = new CharacterRepresentation('r', code);
		
		//s
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[18] = new CharacterRepresentation('s', code);
		
		//t
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[19] = new CharacterRepresentation('t', code);
		
		//u
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[20] = new CharacterRepresentation('u', code);
		
		//v
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[21] = new CharacterRepresentation('v', code);
		
		//w
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[22] = new CharacterRepresentation('w', code);
		
		//x
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[23] = new CharacterRepresentation('x', code);
		
		//y
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[24] = new CharacterRepresentation('y', code);
		
		//z
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[25] = new CharacterRepresentation('z', code);
		
		//0
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[26] = new CharacterRepresentation('0', code);
		
		//1
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[27] = new CharacterRepresentation('1', code);
		
		//2
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		character[28] = new CharacterRepresentation('2', code);
		
		//3
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1,0.1};
		character[29] = new CharacterRepresentation('3', code);
		
		//4
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1,0.1};
		character[30] = new CharacterRepresentation('4', code);
		
		//5
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1,0.1};
		character[31] = new CharacterRepresentation('5', code);
		
		//6
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1,0.1};
		character[32] = new CharacterRepresentation('6', code);
		
		//7
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1,0.1};
		character[33] = new CharacterRepresentation('7', code);
		
		//8
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9,0.1};
		character[34] = new CharacterRepresentation('8', code);
		
		//9
		this.code = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.9};
		character[35] = new CharacterRepresentation('9', code);
	}
	
	public String getCharacter(double[] inputValues){
		
		String output = "?";
		
		/*
		 * Normalise
		 */
		inputValues = this.normalizeValues(inputValues);
		
		
		if(Arrays.equals(inputValues, this.character[0].getCode())){
			
			output = "A";
		}
		else if(Arrays.equals(inputValues, this.character[1].getCode())){
			
			output = "B";
		}
		else if(Arrays.equals(inputValues, this.character[2].getCode())){
			
			output = "C";
		}
		else if(Arrays.equals(inputValues, this.character[3].getCode())){
			
			output = "D";
		}
		else if(Arrays.equals(inputValues, this.character[4].getCode())){
			
			output = "E";
		}
		else if(Arrays.equals(inputValues, this.character[5].getCode())){
			
			output = "F";
		}
		else if(Arrays.equals(inputValues, this.character[6].getCode())){
			
			output = "G";
		}
		else if(Arrays.equals(inputValues, this.character[7].getCode())){
			
			output = "H";
		}
		else if(Arrays.equals(inputValues, this.character[8].getCode())){
			
			output = "I";
		}
		else if(Arrays.equals(inputValues, this.character[9].getCode())){
			
			output = "J";
		}
		else if(Arrays.equals(inputValues, this.character[10].getCode())){
			
			output = "K";
		}
		else if(Arrays.equals(inputValues, this.character[11].getCode())){
			
			output = "L";
		}
		else if(Arrays.equals(inputValues, this.character[12].getCode())){
			
			output = "M";
		}
		else if(Arrays.equals(inputValues, this.character[13].getCode())){
			
			output = "N";
		}
		else if(Arrays.equals(inputValues, this.character[14].getCode())){
			
			output = "O";
		}
		else if(Arrays.equals(inputValues, this.character[15].getCode())){
			
			output = "P";
		}
		else if(Arrays.equals(inputValues, this.character[16].getCode())){
			
			output = "Q";
		}
		else if(Arrays.equals(inputValues, this.character[17].getCode())){
			
			output = "R";
		}
		else if(Arrays.equals(inputValues, this.character[18].getCode())){
			
			output = "S";
		}
		else if(Arrays.equals(inputValues, this.character[19].getCode())){
			
			output = "T";
		}
		else if(Arrays.equals(inputValues, this.character[20].getCode())){
			
			output = "U";
		}
		else if(Arrays.equals(inputValues, this.character[21].getCode())){
			
			output = "V";
		}
		else if(Arrays.equals(inputValues, this.character[22].getCode())){
			
			output = "W";
		}
		else if(Arrays.equals(inputValues, this.character[23].getCode())){
			
			output = "X";
		}
		else if(Arrays.equals(inputValues, this.character[24].getCode())){
			
			output = "Y";
		}
		else if(Arrays.equals(inputValues, this.character[25].getCode())){
			
			output = "Z";
		}
		else if(Arrays.equals(inputValues, this.character[26].getCode())){
			
			output = "0";
		}
		else if(Arrays.equals(inputValues, this.character[27].getCode())){
			
			output = "1";
		}
		else if(Arrays.equals(inputValues, this.character[28].getCode())){
			
			output = "2";
		}
		else if(Arrays.equals(inputValues, this.character[29].getCode())){
			
			output = "3";
		}
		else if(Arrays.equals(inputValues, this.character[30].getCode())){
			
			output = "4";
		}
		else if(Arrays.equals(inputValues, this.character[31].getCode())){
			
			output = "5";
		}
		else if(Arrays.equals(inputValues, this.character[32].getCode())){
			
			output = "6";
		}
		else if(Arrays.equals(inputValues, this.character[33].getCode())){
			
			output = "7";
		}
		else if(Arrays.equals(inputValues, this.character[34].getCode())){
			
			output = "8";
		}
		else if(Arrays.equals(inputValues, this.character[35].getCode())){
			
			output = "9";
		}
		
		return output;
	}
	
	/*
	 * Normalise the given input values
	 * Highest value will be maximised to 0.9
	 */
	private double[] normalizeValues(double[] inputValues){
		
		double[] outputValues = new double[inputValues.length];
		threshold = 0;
		
		/*
		 * Find the highest value
		 */
		for(int i=0; i<outputValues.length; i++){
			
			if(inputValues[i] > threshold){
				
				threshold = inputValues[i];
			}
		}
		
		/*
		 * Do the job
		 */
		for(int i=0; i<outputValues.length; i++){
			
			if(inputValues[i] >= threshold){
				
				outputValues[i] = 0.9;
			}
			else{
				
				outputValues[i] = 0.1;
			}
		}
		
		return outputValues;
	}
}
