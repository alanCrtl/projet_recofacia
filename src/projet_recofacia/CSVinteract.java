package projet_recofacia;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVinteract {
	public static void writeStrVecToFile(String[] vec, String appelation) {
		int row = vec.length;
		
		try {
	      FileWriter writer = new FileWriter(appelation);          
	         for(int i = 0; i < row; i++){
	        	 writer.append(vec[i]);
	        	 writer.append(',');
	        	 writer.append('\n');
	        	 writer.flush();
	         }
	         writer.close();
		}catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
	public static void writeMatToFile(double[][] mat,String appelation) {
		int row = mat.length;
		int column = mat[0].length;
		
		try {
	      FileWriter writer = new FileWriter(appelation);          
	         for(int i = 0; i < row; i++){
	           for (int j=0; j<column; j++){
	        	   writer.append(String.valueOf(mat[i][j]));
	        	   writer.append(',');
	        	   }
	           writer.append('\n');
	           writer.flush();
	         }
	         writer.close();
		}catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
	public static void writeMatToFile(double[] mat,String appelation) {
		int row = mat.length;
		
		try {
	      FileWriter writer = new FileWriter(appelation);          
	         for(int i = 0; i < row; i++){
	        	 writer.append(String.valueOf(mat[i]));
	        	 writer.append(',');
	        	 writer.append('\n');
	        	 writer.flush();
	         }
	         writer.close();
		}catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
	
	public static double[][] fromFileToMat(String path) {
		List<String[]> rowList = new ArrayList<String[]>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        rowList.add(values);
		    }
		    br.close();
		}catch(Exception e){
		    // Handle any I/O problems
		}
		String[][] matrix = new String[rowList.size()][];
		for (int i = 0; i < rowList.size(); i++) {
		    String[] row = rowList.get(i);
		    matrix[i] = row;
		}
		double[][] mat = new double[matrix.length][matrix[0].length];
		for (int i=0;i<matrix.length;i++) {
			for (int j=0;j<matrix[0].length;j++) {
				mat[i][j]=Double.parseDouble(matrix[i][j]);
			}
		}
		return mat;
	}
	
	
	public static double[] fromFileToVec(String path){
		List<String[]> rowList = new ArrayList<String[]>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        rowList.add(values);
		    }
		    br.close();
		}catch(Exception e){
		    // Handle any I/O problems
		}
		String[][] matrix = new String[rowList.size()][];
		for (int i = 0; i < rowList.size(); i++) {
		    String[] row = rowList.get(i);
		    matrix[i] = row;
		}
		double[] vect = new double[matrix.length];
		for (int i=0;i<matrix.length;i++) {
			for (int j=0;j<matrix[0].length;j++) {
				vect[i]=Double.parseDouble(matrix[i][j]);
			}
		}
		return vect;
	}
	
	
	public static String[] fromFileToStrVec(String path) {
		List<String[]> rowList = new ArrayList<String[]>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        rowList.add(values);
		    }
		    br.close();
		}catch(Exception e){
		    // Handle any I/O problems
		}
		String[][] matrix = new String[rowList.size()][];
		for (int i = 0; i < rowList.size(); i++) {
		    String[] row = rowList.get(i);
		    matrix[i] = row;
		}
		String[] vect = new String[matrix.length];
		for (int i=0;i<matrix.length;i++) {
			for (int j=0;j<matrix[0].length;j++) {
				vect[i]=matrix[i][j];
			}
		}
		return vect;
	}

}
