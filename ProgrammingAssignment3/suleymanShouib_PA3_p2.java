import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.text.DecimalFormat;

public class suleymanShouib_PA3_p2 {
	private static final DecimalFormat df = new DecimalFormat("0.0000");
	static double eigenValue1 = 0;
	static double eigenValue2 = 0;
	static boolean realEigenValues = true;
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		String [][] MatrixA = new String[2][2];
		
		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		readFile(fileName, MatrixA);
		
		//Part A
		calculateLambda(MatrixA);
		if(realEigenValues == false) {
			myScanner.close();
			return;
		}
		double [][]Lambda = new double[2][2];
		Lambda[0][0] = Double.valueOf(df.format(eigenValue1));
		Lambda[0][1] = 0;
		Lambda[1][0] = 0;
		Lambda[1][1] = Double.valueOf(df.format(eigenValue2));
		print(Lambda);
		
		
		//Part B
		double [][]Matrix = new double[2][2];
		Matrix[0][0] = Double.valueOf(MatrixA[0][0].substring(0, MatrixA[0][0].length()-2));
		Matrix[0][1] = Double.valueOf(MatrixA[0][1].substring(0, MatrixA[0][1].length()));
		Matrix[1][0] = Double.valueOf(MatrixA[1][0].substring(0, MatrixA[1][0].length()));
		Matrix[1][1] = Double.valueOf(MatrixA[1][1].substring(0, MatrixA[1][1].length()-2));
		double [][]EigenVectorA = new double[2][1];
		EigenVectorA = calculateEigenVector(Matrix, eigenValue1);
		
		//In other tests, without this somehow Matrix gets changed when going into the second EigenVector, so im resetting it here so that never happens
		Matrix[0][0] = Double.valueOf(MatrixA[0][0].substring(0, MatrixA[0][0].length()-2));
		Matrix[0][1] = Double.valueOf(MatrixA[0][1].substring(0, MatrixA[0][1].length()));
		Matrix[1][0] = Double.valueOf(MatrixA[1][0].substring(0, MatrixA[1][0].length()));
		Matrix[1][1] = Double.valueOf(MatrixA[1][1].substring(0, MatrixA[1][1].length()-2));
		double [][]EigenVectorB = new double[2][1];
		EigenVectorB = calculateEigenVector(Matrix, eigenValue2);
		
		double [][]R = new double[2][2];
		R[0][0] = EigenVectorA[0][0];
		R[1][0] = EigenVectorA[1][0];
		R[0][1] = EigenVectorB[0][0];
		R[1][1] = EigenVectorB[1][0];
		print(R);
		
		//Part C
		double[][] MatrixComposition = new double[2][2];
		MatrixComposition = matrixComposition(Lambda, R);
		print(MatrixComposition);
		
		//Part D
		//In other tests, without this somehow Matrix gets changed when going into the second EigenVector, so im resetting it here so that never happens
		Matrix[0][0] = Double.valueOf(MatrixA[0][0].substring(0, MatrixA[0][0].length()-2));
		Matrix[0][1] = Double.valueOf(MatrixA[0][1].substring(0, MatrixA[0][1].length()));
		Matrix[1][0] = Double.valueOf(MatrixA[1][0].substring(0, MatrixA[1][0].length()));
		Matrix[1][1] = Double.valueOf(MatrixA[1][1].substring(0, MatrixA[1][1].length()-2));
		System.out.println(checkIfEqual(Matrix, MatrixComposition));
		
		myScanner.close();
	}
	
	//Part A begins
	static void calculateLambda(String[][] MatrixA) {
		MatrixA[0][0] = MatrixA[0][0]+"-L";
		MatrixA[1][1] = MatrixA[1][1]+"-L";
		
		String first = "";
		String outter = "";
		String inner = "";
		String last = "";
		String otherDiagonal = "";
		
		otherDiagonal = Integer.toString(Integer.valueOf(MatrixA[0][1])*Integer.valueOf(MatrixA[1][0]));
		
		first = MatrixA[0][0].substring(0, 1)+"*"+MatrixA[1][1].substring(0, 1);
		outter = MatrixA[0][0].substring(0, 1)+"*"+MatrixA[1][1].substring(1, 3);
		inner = MatrixA[0][0].substring(1, 3)+"*"+MatrixA[1][1].substring(0, 1);
		last = MatrixA[0][0].substring(1, 3)+"*"+MatrixA[1][1].substring(1, 3);
		
		//Simplify
		first = Integer.toString((Integer.valueOf(first.substring(0, 1))*Integer.valueOf(first.substring(2,3))));
		outter = outter.substring(2, 3)+outter.substring(0,1)+outter.substring(3, 4);
		inner = inner.substring(0, 1)+inner.substring(3,4)+inner.substring(1, 2);
		last = "L2";
		
		//Condense
		String outterInner = Integer.toString(Integer.valueOf(outter.substring(0,2))+Integer.valueOf(inner.substring(0,2)))+"L";
		String firstDiagonal = Integer.toString(Integer.valueOf(first)-Integer.valueOf(otherDiagonal));
		
		String maxBoundary = Integer.toString(Integer.valueOf(outterInner.substring(0, outterInner.length()-1))*Integer.valueOf(firstDiagonal));
		String multiplyTo = firstDiagonal;
		String addTo = outterInner.substring(0, outterInner.length()-1);

		for(int i = Integer.valueOf(maxBoundary)*-1; i <= Integer.valueOf(maxBoundary); i++) {
			for(int j = Integer.valueOf(maxBoundary); j > Integer.valueOf(maxBoundary)*-1; j--) {
				
				//If i and j fit the criteria to be factored out, they could be possible eigenvalues 
				if(i*j == Integer.valueOf(multiplyTo) && i+j == Integer.valueOf(addTo)) {
					//Check to make sure that i and j are equal in the FOIL
					if(FOIL(i,j).equals(last+" "+outterInner+" "+firstDiagonal)) {
						//Multiply them 
						eigenValue1 = i*-1;
						eigenValue2 = j*-1;
						return;	
					}
				}
				
			}
		}

		//This is just the quadratic formula to find the eigen value
		double quadraticA = 1;
		double quadraticB = ((Integer.valueOf(outterInner.substring(0, outterInner.length()-1))));
		double quadraticC = Integer.valueOf(firstDiagonal);
		
		//If the denominator in the quadratic formula turns to be 0, there are no Eigenvalues. The rest of the program cannot run so quit here.
		if(2*quadraticA == 0) {
			realEigenValues = false;
			System.out.println("No Real Eigenvalues");
			return;
		}
		
		eigenValue1 = ((quadraticB*-1) + Math.sqrt(Math.pow(quadraticB, 2) - 4*quadraticA*quadraticC))/(2*quadraticA);
		eigenValue2 = ((quadraticB*-1) - Math.sqrt(Math.pow(quadraticB, 2) - 4*quadraticA*quadraticC))/(2*quadraticA);
	}
	
	static String FOIL(int i, int j) {
		String first = "";
		String outter = "";
		String inner = "";
		String last = "";
		
		first = "L*L";
		outter = "L*"+j;
		inner = i+"*L";
		last = i+"*"+j;
		
		//Simplify
		first = "L2";
		outter = outter.substring(outter.indexOf("*")+1, outter.length())+outter.substring(0,1);
		inner = inner.substring(0, inner.indexOf("*"))+inner.substring(inner.indexOf("*")+1,inner.length());
		last = Integer.toString(i*j);
		//Condense
		String outterInner = Integer.toString(Integer.valueOf(outter.substring(0,outter.length()-1))+Integer.valueOf(inner.substring(0,inner.length()-1)))+"L";
		
		return first+" "+outterInner+" "+last;
	}
	//Part A ends
	
	//Part B begins
	static double [][] calculateEigenVector(double[][] Matrix, double eigenValue) {
		//Plug in eigenvalues
		Matrix[0][0] = Matrix[0][0]-eigenValue;
		Matrix[1][1] = Matrix[1][1]-eigenValue;
		
		//Find shear values
		double[][] Shear = new double[2][2];
		Shear[0][0] = 1;
		Shear[0][1] = 0;
		Shear[1][0] = (Matrix[1][0]*-1)/Matrix[0][0]; //(-v2/v1)
		Shear[1][1] = 1;
		
		//Shear the matrix
		double[][]shearedMatrix = new double [2][2];
		shearedMatrix[0][0] = (Shear[0][0]*Matrix[0][0]) + (Shear[0][1]*Matrix[1][0]); 
		shearedMatrix[0][1] = (Shear[0][0]*Matrix[0][1]) + (Shear[0][1]*Matrix[1][1]); 
		shearedMatrix[1][0] = (Shear[1][0]*Matrix[0][0]) + (Shear[1][1]*Matrix[1][0]); 
		shearedMatrix[1][1] = (Shear[1][0]*Matrix[0][1]) + (Shear[1][1]*Matrix[1][1]); 
		
		//Gauss elimination
		double[][] eigenVector = new double[2][1];
		double x2 = 1;
		double x1 = ((shearedMatrix[0][1]*x2)*-1)/shearedMatrix[0][0];
		
		eigenVector[1][0] = Double.valueOf(df.format(x2));
		eigenVector[0][0] = Double.valueOf(df.format(x1));
		
		//Finally, normalize the eigenVector (this is what the professor did on 10/3/22 Lecture at 8:35)
		double eigenVectorLength = Math.sqrt(Math.pow(eigenVector[0][0], 2) + Math.pow(eigenVector[1][0], 2));
		double normalizeA = eigenVector[0][0]/eigenVectorLength;
		double normalizeB = eigenVector[1][0]/eigenVectorLength;
		double[][] normalizedEigenVector = new double[2][1];
		
		normalizedEigenVector[0][0] = normalizeA;
		normalizedEigenVector[1][0] = normalizeB;
		
		return normalizedEigenVector;
	}
	//Part B ends
	
	//Part C Begins
	static double[][] matrixComposition(double[][]Lambda, double[][]R) {
		//First find the tranpose of R
		double[][] RTranspose = new double[2][2];
		RTranspose[0][0] = R[0][0];
		RTranspose[0][1] = R[1][0];
		RTranspose[1][0] = R[0][1];
		RTranspose[1][1] = R[1][1];
		
		//Multiply R with Lambda
		double[][] RxLambda = new double[2][2];
		RxLambda[0][0] = (R[0][0]*Lambda[0][0]) + (R[0][1]*Lambda[1][0]); 
		RxLambda[0][1] = (R[0][0]*Lambda[0][1]) + (R[0][1]*Lambda[1][1]); 
		RxLambda[1][0] = (R[1][0]*Lambda[0][0]) + (R[1][1]*Lambda[1][0]); 
		RxLambda[1][1] = (R[1][0]*Lambda[0][1]) + (R[1][1]*Lambda[1][1]); 
		
		//Multiply R*Lambda with the transpose of R
		double[][] Answer = new double[2][2];
		Answer[0][0] = Double.valueOf(df.format((RxLambda[0][0]*RTranspose[0][0]) + (RxLambda[0][1]*RTranspose[1][0]))); 
		Answer[0][1] = Double.valueOf(df.format((RxLambda[0][0]*RTranspose[0][1]) + (RxLambda[0][1]*RTranspose[1][1]))); 
		Answer[1][0] = Double.valueOf(df.format((RxLambda[1][0]*RTranspose[0][0]) + (RxLambda[1][1]*RTranspose[1][0]))); 
		Answer[1][1] = Double.valueOf(df.format((RxLambda[1][0]*RTranspose[0][1]) + (RxLambda[1][1]*RTranspose[1][1]))); 
		
		return Answer;
	}
	//Part C Ends
	
	//Part D
	static int checkIfEqual(double[][]A, double[][]Composition) {
		//If all elements are equal, return 1 (Yes), else return 0 (No(
		if(A[0][0] == Composition[0][0] && A[0][1] == Composition[0][1] && A[1][0] == Composition[1][0] && A[1][1] == Composition[1][1]) {
			return 1;
		}
		
		return 0;
	}
	
	// This method reads through any given file and takes out only the needed numbers and places it into the appropriate matrix
	static void readFile(String fileName, String[][] MatrixA) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			
			MatrixA[0][0] = myReader.next();
			MatrixA[0][1] = myReader.next();
			myReader.nextLine();
			MatrixA[1][0] = myReader.next();
			MatrixA[1][1] = myReader.next();
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
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
