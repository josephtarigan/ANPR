/*
 * By Joseph joseph dot tarigan at gmail dot com on WOLF
 */
package com.charset;

public class CharacterRepresentation {

	private char character;
	private double[] representationCode;
	
	/*
	 * Build
	 */
	public CharacterRepresentation(char character, double[] representationCode){
		
		this.character = character;
		this.representationCode = representationCode;
	}
	
	/*
	 * Return the character
	 */
	public char getCharacter(){
		
		return this.character;
	}
	
	/*
	 * Return the serialized code
	 */
	public double[] getCode(){
		
		return this.representationCode;
	}
}
