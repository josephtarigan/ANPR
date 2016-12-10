package com.theapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Reade {

	public static void main(String[] args) throws IOException{
		
		File inputFile;
		File outputFile;
		FileReader fReader;
		BufferedReader bfReader;
		String path = "src/weightdata/ga/";
		String filename = "Generation ";
		String ext = ".txt";
		String temp;
		
		PrintWriter pw;
		
		double[] fitness;
		
		for(int i=1; i<=150; i++){
			
			fitness = new double[50];
			inputFile = new File(path + filename + i + ext);
			fReader = new FileReader(inputFile);
			bfReader = new BufferedReader(fReader);
			int h = 0;
			int hhhhhh= 1;
			
			while((temp = bfReader.readLine()) != null ){
				
				if(temp.contains("Fitness")){
					
					String[] hhhh = new String[2];
					int ggg = 0;
					
					for(String jjj: temp.split(":")){
						
						hhhh[ggg] = jjj;
						ggg++;
					}
					
					double value = Double.parseDouble(hhhh[1]);
					fitness[h] = value;
					h++;
					
					hhhhhh++;
					if(hhhhhh > 50){
						break;
					}
				}
			}
			
			outputFile = new File(path + "fitness/" + filename + i + ext);
			pw = new PrintWriter(outputFile);
			
			for(int ll=0; ll<50; ll++){
				
				pw.printf("%.15f", fitness[ll]);
				pw.println();
			}
			
			pw.close();
		}
	}
}
