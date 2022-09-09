import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.io.FileWriter;
import java.io.IOException;
public class suleymanShouib_p3 {

	public static void main(String[] args) {
		//All matrix sizes are determined from part 1
		double [][] Mat1 = new double [8][6];
		double [][] Mat2 = new double [6][8];	
		double [][] Mat3 = new double [6][8];	
		double [][] Mat4 = new double [9][11];
		double [][] Mat5 = new double [9][11];
		File mat12MultFile = new File("sshouib_p3_out12.txt");
		File mat13MultFile = new File("sshouib_p3_out13.txt");
		File mat14MultFile = new File("sshouib_p3_out14.txt");
		File mat15MultFile = new File("sshouib_p3_out15.txt");
		File mat23MultFile = new File("sshouib_p3_out23.txt");	
		File mat24MultFile = new File("sshouib_p3_out24.txt");
		File mat25MultFile = new File("sshouib_p3_out25.txt");
		File mat34MultFile = new File("sshouib_p3_out34.txt");
		File mat35MultFile = new File("sshouib_p3_out35.txt");
		File mat45MultFile = new File("sshouib_p3_out45.txt");	
		
		//Read the given files and populate the files corresponding matrix with the numbers from the file
		readFile("sshouib_mat1.txt", Mat1);
		readFile("sshouib_mat2.txt", Mat2);
		readFile("sshouib_mat3.txt", Mat3);
		readFile("sshouib_mat4.txt", Mat4);
		readFile("sshouib_mat5.txt", Mat5);
		
		//Multiply every combination of matrices and write them to their respective file
		multiplyMatrices(Mat1, Mat2, mat12MultFile);
		multiplyMatrices(Mat1, Mat3, mat13MultFile);
		multiplyMatrices(Mat1, Mat4, mat14MultFile);
		multiplyMatrices(Mat1, Mat5, mat15MultFile);
		multiplyMatrices(Mat2, Mat3, mat23MultFile);
		multiplyMatrices(Mat2, Mat4, mat24MultFile);
		multiplyMatrices(Mat2, Mat5, mat25MultFile);
		multiplyMatrices(Mat3, Mat4, mat34MultFile);
		multiplyMatrices(Mat3, Mat5, mat35MultFile);
		multiplyMatrices(Mat4, Mat5, mat45MultFile);
	}
	
	/* This method reads through any given file,
	 * and takes out every number and places it into a given matrix
	 */
	static void readFile(String fileName, double[][] Matrix) {
		int colSize = Matrix[0].length;
		int rowPosition = 0;
		int colPosition = 0;
		
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			
			while (myReader.hasNext()) {
				String data = myReader.next();
				
				/* Checks to see if we have passed all of the columns in the matrix
				 * If we are, then move down one row, and reset the column position back to 0		
				 * This prevents an out of bounds error		
				 */
				if(colPosition >= colSize) {
					rowPosition++;
					colPosition = 0;
				}
				Matrix[rowPosition][colPosition] = Double.parseDouble(data);	//Write the current number in the file into the matrix
				colPosition++;	//Move over one column then loop back around
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	/* Look through both matrices and determine if they can be multiplied or not
	 * If they can be multiplied, add them and write the result to a file
	 * If they cannot be multiplied, write an error message to the file
	 */
	static void multiplyMatrices(double[][] Matrix1, double[][] Matrix2, File fileName) {
		//If the two matrices do not have the same dimensions, write an error message to the file
		if(Matrix1.length != Matrix2.length || Matrix1[0].length != Matrix2[0].length) {
			try {
				FileWriter myWriter = new FileWriter(fileName);
				myWriter.write("The dimensions of the two matrices are different, therefore they cannot be multiplied!");		
				myWriter.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Else (they have the same dimensions), multiply the matrices together and write them to the file
		else {
			try {
				FileWriter myWriter = new FileWriter(fileName);
				for(int row = 0; row < Matrix1.length; row++) {
					for(int col = 0; col < Matrix1[0].length; col++) {
						double sumOfMatrixElement = Matrix1[row][col] * Matrix2[row][col];	//Multiply the two matrices at their current 1:1 position
						
						/* Again, for some reason multiplying two numbers may give a weird number past the tenth's place
						 * Example: The output between matrices 2 and 3 values 6.0 * 0.4 was giving 2.40000000000...4 instead of just 2.4
						 * So I implemented the following 2 lines of code to fix that issue, just like in Part 1
						 */
						String sValue = (String) String.format("%.2f", sumOfMatrixElement);
						Double newValue = Double.parseDouble(sValue);
						myWriter.write(newValue+" ");	
					}
					myWriter.write("\n");		//Goes to a new line after the row has been fully written
				}
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	//End of multiplyMatrices

}
