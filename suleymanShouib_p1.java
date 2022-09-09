import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class suleymanShouib_p1 {
	
	public static void main(String[] args) {
		double [][] Mat1 = new double [8][6];	// First name: Suleyman (8 letters), Last name: Shouib (6 letters)
		double [][] Mat2 = new double [6][8];	// First name: Last name: Shouib (6 letters), Suleyman (8 letters)
		double [][] Mat3 = new double [6][8];	// First name: Last name: Shouib (6 letters), Suleyman (8 letters)
		double [][] Mat4 = new double [9][11];
		double [][] Mat5 = new double [9][11];
		File mat1TextFile = new File("sshouib_mat1.txt");
		File mat2TextFile = new File("sshouib_mat2.txt");
		File mat3TextFile = new File("sshouib_mat3.txt");
		File mat4TextFile = new File("sshouib_mat4.txt");
		File mat5TextFile = new File("sshouib_mat5.txt");
		
		//Populate matrices. (Value you want [0][0] to start with, increment value, Matrix you are populating)
		populateMatrix(1, 1, Mat1);
		populateMatrix(4, 2, Mat2);
		populateMatrix(0.3, 0.1, Mat3);
		populateMatrix(3, 3, Mat4);
		populateMatrix(-5, 1.5, Mat5);
		
		//Write matrices to a file. (Matrix you want to write, the file of that matrix to write to)
		writeFiles(Mat1, mat1TextFile);
		writeFiles(Mat2, mat2TextFile);
		writeFiles(Mat3, mat3TextFile);
		writeFiles(Mat4, mat4TextFile);
		writeFiles(Mat5, mat5TextFile);
	}
	
	/* populateMatrix takes in a startingPosition (for [0][0]), and increments 
	 * the starting position by the value the user wants, which is what goes into Matrix
	 */
	static double[][] populateMatrix(double startingPosition, double increment, double[][] Matrix) {
		for(int row = 0; row < Matrix.length; row++) {
			for(int col = 0; col < Matrix[0].length; col++) {
				/* For some reason for Matrix 3, Java can't properly add 0.7 with 0.1. 
				 * Instead it returns 0.7999999... and does so for all future numbers. 
				 * The following 2 lines of code fixes that.
				 * Source for solution: https://stackoverflow.com/questions/7747469/how-can-i-truncate-a-double-to-only-two-decimal-places-in-java 
				 */
				String sValue = (String) String.format("%.2f", startingPosition);
				Double newValue = Double.parseDouble(sValue);
				
				Matrix[row][col] = newValue;				//Inserts value into the matrix
				startingPosition += increment;				//Increments the value
			}
		}
		return Matrix;
	}
	
	/* writeFiles takes in a matrix and the corresponding text file for that matrix.
	 * It iterates through the entire matrix and write it to the file
	 * Source: https://www.w3schools.com/java/java_files_create.asp 
	 */
	static void writeFiles(double[][] Matrix, File file) {
		try {
			FileWriter myWriter = new FileWriter(file);
			for(int row = 0; row < Matrix.length; row++) {
				for(int col = 0; col < Matrix[0].length; col++) {
					myWriter.write(Matrix[row][col]+" ");			//Write to the text file the current Matrix element
				}
				myWriter.write("\n");		//Goes to a new line after the row has been fully written
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
