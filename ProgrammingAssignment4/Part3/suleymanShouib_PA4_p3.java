import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class suleymanShouib_PA4_p3 {
	
	static String fileWriteContent = "[";
	static int googleMatrixSize = 0;
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		double[][] googleMatrix = new double[obtainMatrixBoundaries(fileName)][obtainMatrixBoundaries(fileName)];
		double[][] eigenVectors = new double[1][obtainMatrixBoundaries(fileName)];
		
		readFile(fileName, googleMatrix);
		
		//If the matrix is invalid, write "invalid input" to the file. Else, 
		if(!isValidMatrix(googleMatrix)) {
			fileWriteContent += "Invalid input";
			writeToFile("sshouib_part3_output.txt", fileWriteContent);
		}
		else {
			eigenVectors = calculateEigenVectors(googleMatrix);
			sortWebPageRank(eigenVectors);
			writeToFile("sshouib_part3_output.txt", fileWriteContent);
		}
		
		myScanner.close();
	}
	
	/* This method goes through the entire process of calculating eigenvectors using the power method.
	 * This entire method simulates the Professors process in lecture video 11/14/2022 at 28:00.
	 */
	static double[][] calculateEigenVectors(double[][] m) {
		//Initialize starting vector r to just be ones
		double[][] r = new double[1][m.length];
		for(int i = 0; i < m.length; i++) {
			r[0][i] = 1;
		}

		double[][] p = new double[1][m.length];	//This will act as the most recent calculated vector
		
		int maxIterations = 4;
		for(int k = 1; k < maxIterations; k++) {
			/* If this is NOT the first iteration, Update xi to be the most recent calculation, that was p can be rewritten for the next calculation
			 * This cannot run in the first iteration due to the fact that this will overwrite the starting vector value
			 */
			if(k!=1) {
				for(int i = 0; i < r[0].length; i++) {
					r[0][i] = p[0][i];
				}
			}
			
			//This is the main calculation method where p = m*r (class notation ri+1 = A*ri). It multiplies matrix m by the current vector xi
			for(int i = 0; i < r[0].length; i++) {
				for(int j = 0; j < m[0].length; j++) {
					/* Note: xi is the most recent calculated vector due to the for loop above. 
					 * However, after the calculations, p becomes the most recently calculated vector, confirming comment on line 42
					 * Vector v becomes the previous vector, which is what is used after the iteration k for loop
					 */
					p[0][i] += m[i][j] * r[0][i];	
				}
			}
		}
		
		double[][]answer = new double [1][m.length];
		for(int i = 0; i < r[0].length; i++) {
			answer[0][i] = p[0][i]/r[0][i];
		}
		
		for(int i = 0; i < answer[0].length; i++) {
			fileWriteContent += Double.toString(answer[0][i])+" ";
		}
		fileWriteContent += "]\n";
		return answer;
	}
	
	//This method sorts eigenvector matrix from highest to lowest and displays the sorted index of the web pages 
	static void sortWebPageRank(double[][] m) {
		int [] webPageRank = new int[m[0].length];
		for(int i = 0; i < webPageRank.length; i++) {
			webPageRank[i] = i+1;
		}
		
		for (int i = 0; i < m[0].length; i++) {
            for (int j = i + 1; j < m[0].length; j++) {
                double tempM = m[0][0];
                int tempW = webPageRank[0];
                if (m[0][j] > m[0][i]) {
                    tempM = m[0][i];
                    m[0][i] = m[0][j];
                    m[0][j] = tempM;
                    
                    tempW = webPageRank[i];
                    webPageRank[i] = webPageRank[j];
                    webPageRank[j] = tempW;
                }
            }
        }

		for(int i = 0; i < webPageRank.length; i++) {
			fileWriteContent += Integer.toString(webPageRank[i])+" ";
		}
        
	}
	
	/* Obtains the number of rows. Because the instructions say the file is an N x N matrix, 
	 * I am assuming it will ALWAYS be N x N, so I only need to know the number of rows to know the number of columns
	 */
	static int obtainMatrixBoundaries(String fileName) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			//Counts how many rows there are
			int rowCounter = 0;
			while(myReader.hasNext()) {
				myReader.nextLine();
				rowCounter++;
			}
			myReader.close();
			return rowCounter;
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	// Goes through the entire file and populates the google matrix. 
	static void readFile(String fileName, double[][] googleMatrix) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			int colCounter = 0;
			int rowCounter = 0;
			
			while(myReader.hasNext()) {
				googleMatrix[rowCounter][colCounter] = myReader.nextDouble();
				colCounter++;
				if(colCounter == googleMatrix[0].length) {
					colCounter = 0;
					rowCounter++;
				}
				googleMatrixSize++;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	//Checks to see if the matrix is stochastic and does not contain any negative numbers
	static boolean isValidMatrix(double[][] googleMatrix) {
		//Check to make sure the matrix is a square
		if(googleMatrixSize != (Math.pow(googleMatrix.length, 2))) {
			return false;
		}
		
		//Check for negative numbers
		for(int i = 0; i < googleMatrix.length; i++) {
			for(int j = 0; j < googleMatrix.length; j++) {
				if(googleMatrix[i][j] < 0) {
					return false;
				}
			}
		}
		
		//Check if its not stochastic
		double columnSum = 0;
		for(int i = 0; i < googleMatrix.length; i++) {
			for(int j = 0; j < googleMatrix.length; j++) {
				columnSum += googleMatrix[i][j];	
			}
			if(columnSum > 0.98 && columnSum < 1.1) {
				columnSum = 0;
			}
			else {	//Column does not equal 1 (and isnt close to it)
				return false;
			}
		}
		
		//The matrix is valid
		return true;
	}
	
	// This method writes all of the calculations into a file
	static void writeToFile(String fileName, String content) {
		try {
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write(content);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}