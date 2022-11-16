import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
public class suleymanShouib_PA4_p2 {
	
	static String subPartOneContent = "";
	static String subPartTwoContent = "";
	private static final DecimalFormat df = new DecimalFormat("0.0000");
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		
		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		
		subPartOneReadFile(fileName);
		subPartTwoReadFile(fileName);
		
		writeToFile("sshouib_part2_output_A.txt", subPartOneContent);
		writeToFile("sshouib_part2_output_B.txt", subPartTwoContent);
		
		myScanner.close();
	}
	
	/* This method looks saves all numbers from the file, calculates the equation for the plane,
	 * then calls distanceCalculator to calculate that lines distance from point to plane
	 */
	static void subPartOneReadFile(String fileName) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);

			double[][] plane = new double[1][4];
			double[][] point = new double [1][3];
			while(myReader.hasNext()) {
				//Save and Calculate information to get the equation of a plane
				double x = myReader.nextDouble();
				double y = myReader.nextDouble();
				double z = myReader.nextDouble();
				plane[0][0] = myReader.nextDouble();	//a
				plane[0][1] = myReader.nextDouble();	//b
				plane[0][2] = myReader.nextDouble();	//c
				plane[0][3] = -((plane[0][0]*x) + (plane[0][1]*y) + (plane[0][2]*z));	//d -(ax+by+cz)
				
				//This is the point we will be finding the distance to
				point[0][0] = myReader.nextDouble();
				point[0][1] = myReader.nextDouble();
				point[0][2] = myReader.nextDouble();
				
				distanceCalculator(plane, point);
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// This method uses the distance formula to calculate the distance from a point to a plane
	static void distanceCalculator(double[][] plane, double[][] point) {
		double distance;
		
		//Distance equation, |a*x + b*y + c*z + d| / sqrt(a^2 + b^2 + c^2) where a b c d are from plane and x y z are from point
		distance = Math.abs(plane[0][0]*point[0][0]+plane[0][1]*point[0][1]+plane[0][2]*point[0][2]+plane[0][3]) /
				Math.sqrt(Math.pow(plane[0][0],2) + Math.pow(plane[0][1], 2) + Math.pow(plane[0][2], 2));
		
		subPartOneContent += df.format(distance)+"\n";
	}
	
	/* This method looks saves all numbers from the file, calculates the equation for the plane,
	 * then calls intersectionCalculator to calculate that point of intersection from line to plane
	 */
	static void subPartTwoReadFile(String fileName) {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			
			//Saves the information of the two points that will end up being our line
			double[][] pointOne = new double [1][3];
			pointOne[0][0] = myReader.nextDouble();
			pointOne[0][1] = myReader.nextDouble();
			pointOne[0][2] = myReader.nextDouble();
			
			double[][] pointTwo = new double[1][3];
			pointTwo[0][0] = myReader.nextDouble();
			pointTwo[0][1] = myReader.nextDouble();
			pointTwo[0][2] = myReader.nextDouble();
			
			myReader.nextLine();
			while(myReader.hasNext()) {
				//Save the three points that make up the plane
				double[][] A = new double[1][3];
				double[][] B = new double[1][3];
				double[][] C = new double[1][3];
				A[0][0] = myReader.nextDouble();
				A[0][1] = myReader.nextDouble();
				A[0][2] = myReader.nextDouble();
				
				B[0][0] = myReader.nextDouble();
				B[0][1] = myReader.nextDouble();
				B[0][2] = myReader.nextDouble();
				
				C[0][0] = myReader.nextDouble();
				C[0][1] = myReader.nextDouble();
				C[0][2] = myReader.nextDouble();
				
				//Find the differences of the two points
				double[][]AB = new double[1][3];
				AB[0][0] = B[0][0]-A[0][0];
				AB[0][1] = B[0][1]-A[0][1];
				AB[0][2] = B[0][2]-A[0][2];
				
				double[][]AC = new double[1][3];
				AC[0][0] = C[0][0]-A[0][0];
				AC[0][1] = C[0][1]-A[0][1];
				AC[0][2] = C[0][2]-A[0][2];
				
				//Calculate the cross product (and d) to find the equation of plane
				double[][] plane = new double[1][4];
				plane[0][0] = (AB[0][1]*AC[0][2]) - (AB[0][2]*AC[0][1]);
				plane[0][1] = (AB[0][2]*AC[0][0]) - (AB[0][0]*AC[0][2]);
				plane[0][2] = (AB[0][0]*AC[0][1]) - (AB[0][1]*AC[0][0]);
				plane[0][3] = -((A[0][0]*plane[0][0]) + (A[0][1]*plane[0][1]) + (A[0][2]*plane[0][2]));
				
				intersectionCalculator(pointOne, pointTwo, plane);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// This method calculates the intersection from a line to a plane 
	static void intersectionCalculator(double[][] pointOne, double[][] pointTwo, double[][] plane) {
		double[][] directionVector = new double [1][3];
		double[][] parametricX = new double [1][2];
		double[][] parametricY = new double [1][2];
		double[][] parametricZ = new double [1][2];
		
		//Calculate the direction vector v
		directionVector[0][0] = pointTwo[0][0]-pointOne[0][0];
		directionVector[0][1] = pointTwo[0][1]-pointOne[0][1];
		directionVector[0][2] = pointTwo[0][2]-pointOne[0][2];
		
		//Calculate the parametric equation for x, y and z. [0][1] acts as "t"
		parametricX[0][0] = pointOne[0][0];
		parametricX[0][1] = directionVector[0][0];
		
		parametricY[0][0] = pointOne[0][1];
		parametricY[0][1] = directionVector[0][1];
		
		parametricZ[0][0] = pointOne[0][2];
		parametricZ[0][1] = directionVector[0][2];
		
		//Substitute the parametric equations into the plane equation
		double[][] substitute = new double[1][7];
		substitute[0][0] = plane[0][0]*parametricX[0][0];
		substitute[0][1] = plane[0][0]*parametricX[0][1];
		
		substitute[0][2] = plane[0][1]*parametricY[0][0];
		substitute[0][3] = plane[0][1]*parametricY[0][1];
		
		substitute[0][4] = plane[0][2]*parametricZ[0][0];
		substitute[0][5] = plane[0][2]*parametricZ[0][1];
		
		substitute[0][6] = plane[0][3];
		
		//Simplify the substituted plane equation
		double[][] simplified = new double[1][2];
		simplified[0][0] = -(substitute[0][0]+substitute[0][2]+substitute[0][4]+substitute[0][6]);	//negative to simulate crossing sides of equation
		simplified[0][1] = substitute[0][1]+substitute[0][3]+substitute[0][5];
		
		//simplified[0][1] is "t". If t is zero, then we can't solve for t, meaning that there is no intersection
		if(simplified[0][1] == 0 ) {
			subPartTwoContent += "Does not intersect.\n";
		}
		else {	//else calculate the value of "t" and substitute that back into the parametric equations
			double t = simplified[0][0]/simplified[0][1];
			parametricX[0][1] = parametricX[0][1]*t;
			parametricY[0][1] = parametricY[0][1]*t;
			parametricZ[0][1] = parametricZ[0][1]*t;
			
			//Solve the parametric equations and save their values into answer. This is the intersection point
			double[][] answer = new double[1][3];
			answer[0][0] = parametricX[0][0]+parametricX[0][1];
			answer[0][1] = parametricY[0][0]+parametricY[0][1];
			answer[0][2] = parametricZ[0][0]+parametricZ[0][1];
			
			subPartTwoContent += "["+df.format(answer[0][0])+" "+df.format(answer[0][1])+" "+df.format(answer[0][2])+"]\n";
		}
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
