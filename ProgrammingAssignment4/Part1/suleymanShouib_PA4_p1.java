import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
public class suleymanShouib_PA4_p1 {
	//These two variables contain information that will be written to the file by the end of the program
	static String parallelProjectionContent = "";
	static String perspectiveProjectionContent = "";
	
	private static final DecimalFormat df = new DecimalFormat("0.0000");
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		double [][] directionOfProjection = new double[1][3];
		double [][] pointOnPlane = new double[1][3];
		double [][] normalToPlane = new double[1][3];

		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();

		populateMatrixes(fileName, pointOnPlane, normalToPlane, directionOfProjection);

		readFile(fileName, true, directionOfProjection, pointOnPlane, normalToPlane);	//(true) This will end up calculating parallel projection
		readFile(fileName, false, directionOfProjection, pointOnPlane, normalToPlane);	//(false) This will end up calculating perspective projection
		
		
		writeToFile("sshouib_part1_output_A.txt", parallelProjectionContent);
		writeToFile("sshouib_part1_output_B.txt", perspectiveProjectionContent);
		
		myScanner.close();
	}
	
	// This method reads the first line of the file, populating 3 matrices given information from the first line
	static void populateMatrixes(String fileName, double[][] POP, double[][] NTP, double[][] DOP) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			
			POP[0][0] = myReader.nextDouble();
			POP[0][1] = myReader.nextDouble();
			POP[0][2] = myReader.nextDouble();
			
			NTP[0][0] = myReader.nextDouble();
			NTP[0][1] = myReader.nextDouble();
			NTP[0][2] = myReader.nextDouble();
			
			DOP[0][0] = myReader.nextDouble();
			DOP[0][1] = myReader.nextDouble();
			DOP[0][2] = myReader.nextDouble();
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// This method reads the remainder of the file, skipping the first line since that was read in populateMatrixes
	static void readFile(String fileName, boolean isParallel, double[][]DOP, double[][] POP, double[][]NTP) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();	
			
			while(myReader.hasNext()) {
				organizePoints(myReader.nextDouble(), myReader.nextDouble(), myReader.nextDouble(),
						myReader.nextDouble(), myReader.nextDouble(), myReader.nextDouble(),
						myReader.nextDouble(), myReader.nextDouble(), myReader.nextDouble(), isParallel, DOP, POP, NTP);	
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// This method organizes all 9 values from any given line from the file, and organizes the 9 values into 3 1x3 matrices
	static void organizePoints(double a, double b, double c, double d, double e, double f, double g, double h, double i, boolean isParallel, double[][] DOP, double[][] POP, double[][] NTP ) {
		double[][] pointOne = new double [1][3];
		double[][] pointTwo = new double[1][3];
		double[][] pointThree = new double[1][3];
		
		pointOne[0][0] = a;
		pointOne[0][1] = b;
		pointOne[0][2] = c;
		
		pointTwo[0][0] = d;
		pointTwo[0][1] = e;
		pointTwo[0][2] = f;
		
		pointThree[0][0] = g;
		pointThree[0][1] = h;
		pointThree[0][2] = i;
		
		if(isParallel) {
			parallelProjection(pointOne, pointTwo, pointThree, DOP, POP, NTP);
		}
		else {
			perspectiveProjection(pointOne, pointTwo, pointThree, POP, NTP);
		}
	}
	
	/* This method does the parallel projection calculation. Class lecture notations: DOP = v, POP = q, NTP = n
	 * This entire method simulates the Professors process in lecture video 10/19/2022 at 8:40
	 */
	static void parallelProjection(double[][] one, double[][] two, double[][] three, double[][] DOP, double[][] POP, double[][] NTP) {
		//DOP*NTP^T
		double[][] vnT = new double[3][3];
		vnT[0][0] = DOP[0][0]*NTP[0][0];
		vnT[0][1] = DOP[0][0]*NTP[0][1];
		vnT[0][2] = DOP[0][0]*NTP[0][2];
		vnT[1][0] = DOP[0][1]*NTP[0][0];
		vnT[1][1] = DOP[0][1]*NTP[0][1];
		vnT[1][2] = DOP[0][1]*NTP[0][2];
		vnT[2][0] = DOP[0][2]*NTP[0][0];
		vnT[2][1] = DOP[0][2]*NTP[0][1];
		vnT[2][2] = DOP[0][2]*NTP[0][2];
		
		//DOP dot NTP
		double vDotN = (DOP[0][0]*NTP[0][0]) + (DOP[0][1]*NTP[0][1]) + (DOP[0][2]*NTP[0][2]);
		
		//vnT / vDotN
		double[][] vnTDivideVDotN = new double[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				vnTDivideVDotN[i][j] = vnT[i][j]/vDotN;
			}
		}
		
		//[I - vnTDivideVDotN]
		double[][] leftHalf = new double[3][3];
		double[][] I = new double[3][3];
		I[0][0] = 1; I[0][1] = 0; I[0][2] = 0;
		I[1][0] = 0; I[1][1] = 1; I[1][2] = 0;
		I[2][0] = 0; I[2][1] = 0; I[2][2] = 1;
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				leftHalf[i][j] = I[i][j] - vnTDivideVDotN[i][j]; 
			}
		}
		
		//POP dot NTP
		double qDotN = (POP[0][0]*NTP[0][0]) + (POP[0][1]*NTP[0][1]) + (POP[0][2]*NTP[0][2]);
		
		//qDotN / vDotN
		double dotsDivide = qDotN/vDotN;
		
		//dotsDivide * v
		double[][] rightHalf = new double[1][3];
		rightHalf[0][0] = dotsDivide * DOP[0][0];
		rightHalf[0][1] = dotsDivide * DOP[0][1];
		rightHalf[0][2] = dotsDivide * DOP[0][2];
		
		//leftHalf * one
		double[][] answerOne = new double[1][3];
		answerOne[0][0] = leftHalf[0][0]*one[0][0] + leftHalf[0][1]*one[0][1] + leftHalf[0][2]*one[0][2];
		answerOne[0][1] = leftHalf[1][0]*one[0][0] + leftHalf[1][1]*one[0][1] + leftHalf[1][2]*one[0][2];
		answerOne[0][2] = leftHalf[2][0]*one[0][0] + leftHalf[2][1]*one[0][1] + leftHalf[2][2]*one[0][2];
		answerOne[0][0] = answerOne[0][0]+rightHalf[0][0];
		answerOne[0][1] = answerOne[0][1]+rightHalf[0][1];
		answerOne[0][2] = answerOne[0][2]+rightHalf[0][2];
		
		double[][] answerTwo = new double[1][3];
		answerTwo[0][0] = leftHalf[0][0]*two[0][0] + leftHalf[0][1]*two[0][1] + leftHalf[0][2]*two[0][2];
		answerTwo[0][1] = leftHalf[1][0]*two[0][0] + leftHalf[1][1]*two[0][1] + leftHalf[1][2]*two[0][2];
		answerTwo[0][2] = leftHalf[2][0]*two[0][0] + leftHalf[2][1]*two[0][1] + leftHalf[2][2]*two[0][2];
		answerTwo[0][0] = answerTwo[0][0]+rightHalf[0][0];
		answerTwo[0][1] = answerTwo[0][1]+rightHalf[0][1];
		answerTwo[0][2] = answerTwo[0][2]+rightHalf[0][2];
		
		double[][] answerThree = new double[1][3];
		answerThree[0][0] = leftHalf[0][0]*three[0][0] + leftHalf[0][1]*three[0][1] + leftHalf[0][2]*three[0][2];
		answerThree[0][1] = leftHalf[1][0]*three[0][0] + leftHalf[1][1]*three[0][1] + leftHalf[1][2]*three[0][2];
		answerThree[0][2] = leftHalf[2][0]*three[0][0] + leftHalf[2][1]*three[0][1] + leftHalf[2][2]*three[0][2];
		answerThree[0][0] = answerThree[0][0]+rightHalf[0][0];
		answerThree[0][1] = answerThree[0][1]+rightHalf[0][1];
		answerThree[0][2] = answerThree[0][2]+rightHalf[0][2];
		
		parallelProjectionContent += "["+df.format(answerOne[0][0])+" "+df.format(answerOne[0][1])+" "+df.format(answerOne[0][2])+"] ";
		parallelProjectionContent += "["+df.format(answerTwo[0][0])+" "+df.format(answerTwo[0][1])+" "+df.format(answerTwo[0][2])+"] ";
		parallelProjectionContent += "["+df.format(answerThree[0][0])+" "+df.format(answerThree[0][1])+" "+df.format(answerThree[0][2])+"]\n";
	}
	
	/* This method does the perspective projection calculation. Class lecture notations: DOP = v, POP = q, NTP = n
	 * This entire method simulates the Professors process in lecture video 10/19/2022 at 42:54
	 */
	static void perspectiveProjection(double[][] one, double[][] two, double[][] three, double[][] POP, double[][] NTP) {
		//POP dot NTP
		double qDotN = (POP[0][0]*NTP[0][0]) + (POP[0][1]*NTP[0][1]) + (POP[0][2]*NTP[0][2]);
		
		//Calculates the dot products from all three points with N
		double oneDotN = (one[0][0]*NTP[0][0]) + (one[0][1]*NTP[0][1]) + (one[0][2]*NTP[0][2]);
		double twoDotN = (two[0][0]*NTP[0][0]) + (two[0][1]*NTP[0][1]) + (two[0][2]*NTP[0][2]);
		double threeDotN = (three[0][0]*NTP[0][0]) + (three[0][1]*NTP[0][1]) + (three[0][2]*NTP[0][2]);
		
		//Divides qDotN with all three dot products
		double dotDivisionOne = qDotN/oneDotN;
		double dotDivisionTwo = qDotN/twoDotN;
		double dotDivisionThree = qDotN/threeDotN;
		
		//dotDivision * point (one, two and three)
		double[][] answerOne = new double[1][3];
		answerOne[0][0] = dotDivisionOne * one[0][0];
		answerOne[0][1] = dotDivisionOne * one[0][1];
		answerOne[0][2] = dotDivisionOne * one[0][2];
		
		double[][] answerTwo = new double[1][3];
		answerTwo[0][0] = dotDivisionTwo * two[0][0];
		answerTwo[0][1] = dotDivisionTwo * two[0][1];
		answerTwo[0][2] = dotDivisionTwo * two[0][2];
		
		double[][] answerThree = new double[1][3];
		answerThree[0][0] = dotDivisionThree * three[0][0];
		answerThree[0][1] = dotDivisionThree * three[0][1];
		answerThree[0][2] = dotDivisionThree * three[0][2];
		
		perspectiveProjectionContent += "["+df.format(answerOne[0][0])+" "+df.format(answerOne[0][1])+" "+df.format(answerOne[0][2])+"] ";
		perspectiveProjectionContent += "["+df.format(answerTwo[0][0])+" "+df.format(answerTwo[0][1])+" "+df.format(answerTwo[0][2])+"] ";
		perspectiveProjectionContent += "["+df.format(answerThree[0][0])+" "+df.format(answerThree[0][1])+" "+df.format(answerThree[0][2])+"]\n";
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