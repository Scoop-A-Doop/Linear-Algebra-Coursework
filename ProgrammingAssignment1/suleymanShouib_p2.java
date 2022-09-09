import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.io.FileWriter;
import java.io.IOException;

/* Source used to help with Part 2: 
 * https://www.w3schools.com/java/java_files_read.asp 
 * https://www.javacodex.com/Files/Read-File-Word-By-Word
 */
public class suleymanShouib_p2 {

	public static void main(String[] args) {
		//All matrix sizes are determined from part 1
		double [][] Mat1 = new double [8][6];
		double [][] Mat2 = new double [6][8];	
		double [][] Mat3 = new double [6][8];	
		double [][] Mat4 = new double [9][11];
		double [][] Mat5 = new double [9][11];
		File mat12AddFile = new File("sshouib_p2_out12.txt");
		File mat13AddFile = new File("sshouib_p2_out13.txt");
		File mat14AddFile = new File("sshouib_p2_out14.txt");
		File mat15AddFile = new File("sshouib_p2_out15.txt");
		File mat23AddFile = new File("sshouib_p2_out23.txt");	//Check solution
		File mat24AddFile = new File("sshouib_p2_out24.txt");
		File mat25AddFile = new File("sshouib_p2_out25.txt");
		File mat34AddFile = new File("sshouib_p2_out34.txt");
		File mat35AddFile = new File("sshouib_p2_out35.txt");
		File mat45AddFile = new File("sshouib_p2_out45.txt");	//Check solution
		
		//Read the given files and populate the files corresponding matrix with the numbers from the file
		readFile("sshouib_mat1.txt", Mat1);
		readFile("sshouib_mat2.txt", Mat2);
		readFile("sshouib_mat3.txt", Mat3);
		readFile("sshouib_mat4.txt", Mat4);
		readFile("sshouib_mat5.txt", Mat5);
		
		//Add every combination of matrices and write them to their respective file
		addMatrices(Mat1, Mat2, mat12AddFile);
		addMatrices(Mat1, Mat3, mat13AddFile);
		addMatrices(Mat1, Mat4, mat14AddFile);
		addMatrices(Mat1, Mat5, mat15AddFile);
		addMatrices(Mat2, Mat3, mat23AddFile);
		addMatrices(Mat2, Mat4, mat24AddFile);
		addMatrices(Mat2, Mat5, mat25AddFile);
		addMatrices(Mat3, Mat4, mat34AddFile);
		addMatrices(Mat3, Mat5, mat35AddFile);
		addMatrices(Mat4, Mat5, mat45AddFile);
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
	
	/* Look through both matrices and determine if they can be added or not
	 * If they can be added, add them and write the result to a file
	 * If they cannot be added, write an error message to the file
	 */
	static void addMatrices(double[][] Matrix1, double[][] Matrix2, File fileName) {
		//If the two matrices do not have the same dimensions, write an error message to the file
		if(Matrix1.length != Matrix2.length || Matrix1[0].length != Matrix2[0].length) {
			try {
				FileWriter myWriter = new FileWriter(fileName);
				myWriter.write("The dimensions of the two matrices are different, therefore they cannot be added!");		
				myWriter.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Else (they have the same dimensions), add the matrices together and write them to the file
		else {
			try {
				FileWriter myWriter = new FileWriter(fileName);
				for(int row = 0; row < Matrix1.length; row++) {
					for(int col = 0; col < Matrix1[0].length; col++) {
						double sumOfMatrixElement = Matrix1[row][col] + Matrix2[row][col];	//Add the two matrices at their current 1:1 position
						myWriter.write(sumOfMatrixElement+" ");	
					}
					myWriter.write("\n");		//Goes to a new line after the row has been fully written
				}
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	//End of addMatrices

}
