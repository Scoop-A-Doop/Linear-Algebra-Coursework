import java.io.File;  
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner; 
public class suleymanShouib_PA3_p1 {
	
	//https://mkyong.com/java/how-to-round-double-float-value-to-2-decimal-points-in-java/
	private static final DecimalFormat df = new DecimalFormat("0.0000");
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		double [][] MatrixA = new double[2][2];
		double [][] MatrixB = new double [2][1];
		
		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		
		readFile(fileName, MatrixA, MatrixB);
		calculate(MatrixA, MatrixB);
		
		myScanner.close();
	}
	
	// This method reads through any given file and takes out only the needed numbers and places it into the appropriate matrix
	static void readFile(String fileName, double[][] MatrixA, double[][] MatrixB) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			
			while (myReader.hasNext()) {
				MatrixA[0][0] = myReader.nextInt();
				MatrixA[0][1] = myReader.nextInt();
				MatrixB[0][0] = myReader.nextInt();
				MatrixA[1][0] = myReader.nextInt();
				MatrixA[1][1] = myReader.nextInt();
				MatrixB[1][0] = myReader.nextInt();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// This method does the entire calculation process to calculate Ax=B solving for x
	static void calculate(double[][] MatrixA, double[][] MatrixB) {
		
		//If the determinant is not 0, there will be a unique solution. Else, the system may be underdetermined OR inconsistent
		if((MatrixA[0][0]*MatrixA[1][1] - MatrixA[0][1]*MatrixA[1][0] != 0)) {
			double determinant = Double.valueOf(df.format(1/(MatrixA[0][0]*MatrixA[1][1] - MatrixA[0][1]*MatrixA[1][0])));
			
			//Flip all of the values diagonally (Ex: 0,0 <--> 1,1), and for 0,1 <--> 1,0 flip the signs as well. Save everything to MatrixAFlipped
			double [][] MatrixAFlipped = new double[MatrixA.length][MatrixA[0].length];
			MatrixAFlipped[0][0] = Double.valueOf(df.format(MatrixA[1][1]));
			MatrixAFlipped[0][1] = Double.valueOf(df.format(MatrixA[0][1]*-1));
			MatrixAFlipped[1][0] = Double.valueOf(df.format(MatrixA[1][0]*-1));
			MatrixAFlipped[1][1] = Double.valueOf(df.format(MatrixA[0][0]));

			double[][] inverseMatrixA = new double[2][2];
			//Calculate the inverseMatrix by multiplying the flipped MatrixA above by the determinant. Save it to inverseMatrixA
			for(int i = 0; i < MatrixAFlipped.length; i++) {
				for(int j = 0; j < MatrixAFlipped[0].length; j++) {
					inverseMatrixA[i][j] = Double.valueOf(df.format(MatrixAFlipped[i][j]*determinant));
				}
			}
			
			double[][] Answer = new double[MatrixB.length][MatrixB[0].length];
			//Finally, calculate the answer by multiplying the inverse of MatrixA by MatrixB. Save it to Answer then print Answer
			Answer[0][0] = Double.valueOf(df.format((inverseMatrixA[0][0]*MatrixB[0][0])+(inverseMatrixA[0][1]*MatrixB[1][0])));
			Answer[1][0] = Double.valueOf(df.format((inverseMatrixA[1][0]*MatrixB[0][0])+(inverseMatrixA[1][1]*MatrixB[1][0])));
		
			print(Answer);
		}
		else{
			//This process is checking to see if the system is underdetermined
			double shearMatrix[][] = new double [2][2];
			//Check to see which shear to use, e1 or e2 axis. If neither are valid then the system is inconsistent
			if(MatrixA[0][0] != 0) {
				shearMatrix[0][0] = 1;
				shearMatrix[0][1] = 0;
				shearMatrix[1][0] = (MatrixA[1][0]*-1)/MatrixA[0][0]; //(-v2/v1)
				shearMatrix[1][1] = 1;
			}
			else if(MatrixA[0][1] != 0){
				shearMatrix[0][0] = 1;
				shearMatrix[0][1] = (MatrixA[0][0]*-1)/MatrixA[0][1];	//(-v1/v2)
				shearMatrix[1][0] = 0;
				shearMatrix[1][1] = 1;
			}
			else {
				System.out.println("System inconsistent!");
				return;
			}
			
			/* Since to figure out if the system is underdetermined, we only need to look at the second row for the results for Shear*MatrixA and Shear*MatrixB,
			 * We will only calculate those bottom row values since any other information is irrelevant. C and D are the bottom values for the 2x2 and B is the bottom value for the 2x1
			 */
			double c = (shearMatrix[1][0]*MatrixA[0][0]) + (shearMatrix[1][1]*MatrixA[1][0]); 
			double d = (shearMatrix[1][0]*MatrixA[0][1]) + (shearMatrix[1][1]*MatrixA[1][1]); 
			double b = (shearMatrix[1][0]*MatrixB[0][0]) + (shearMatrix[1][1]*MatrixB[1][0]); 
			
			//If all of the bottom row values are 0, the system is underdetermined. Else, the only possible solution is the system being inconsistent
			if(c == 0 && d == 0 && b == 0) {
				System.out.println("System underdetermined!");
			}
			else {
				System.out.println("System inconsistent!");
			}	
		}
		
	}
	
	//Prints out the matrix
	static void print(double[][] Matrix) {
		for(int i = 0; i < Matrix.length; i++) {
			for(int j = 0; j < Matrix[0].length; j++) {
				System.out.print(Matrix[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}