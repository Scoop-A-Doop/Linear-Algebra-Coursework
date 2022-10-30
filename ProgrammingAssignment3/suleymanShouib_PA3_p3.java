import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;
import java.text.DecimalFormat;
public class suleymanShouib_PA3_p3 {
	private static final DecimalFormat df = new DecimalFormat("0.0000");
	
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		String area;
		String distance;
		System.out.println("Please type the name of the file you will be using, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		
		boolean is2D = twoDimensionalOrThreeDimensional(fileName);
		if(is2D) {
			double[][] matrix3D2D = new double[2][3];
			readFile(fileName, matrix3D2D);
			area = Double.toString(calculateArea(matrix3D2D, is2D));
			distance = Double.toString(calculateDistance(matrix3D2D, is2D));
			write(area, distance, is2D);
		}
		else {
			double[][] matrix3D = new double[3][3];
			readFile(fileName, matrix3D);
			area = Double.toString((calculateArea(matrix3D, is2D)));
			distance = Double.toString(calculateDistance(matrix3D, is2D));
			write(area, distance, is2D);
		}
		
		myScanner.close();
	}
	
	public static double calculateArea(double[][] triangle, boolean is2D) {
		double Area = 0;
		if(is2D) {
			//Use the distance formula given three coordinates of the triangle
			Area = (Math.abs((triangle[0][0]*(triangle[1][1]-triangle[1][2]))
					+(triangle[0][1]*(triangle[1][2]-triangle[1][0]))
					+(triangle[0][2]*(triangle[1][0]-triangle[1][1]))))/2;
		}
		else {
			//Create vectors AB and AC by subtracting A-B and A-C
			double[][] AB = new double [3][1];
			double[][] AC = new double [3][1];
			AB[0][0] = triangle[0][1]-triangle[0][0];
			AB[1][0] = triangle[1][1]-triangle[1][0];
			AB[2][0] = triangle[2][1]-triangle[2][0];
			
			AC[0][0] = triangle[0][2]-triangle[0][0];
			AC[1][0] = triangle[1][2]-triangle[1][0];
			AC[2][0] = triangle[2][2]-triangle[2][0];
			
			//Calculate the cross product of ABxAC
			double[][] crossProduct = new double [1][3];
			crossProduct[0][0] = AB[1][0]*AC[2][0]-AB[2][0]*AC[1][0];
			crossProduct[0][1] = AB[2][0]*AC[0][0]-AB[0][0]*AC[2][0];
			crossProduct[0][2] = AB[0][0]*AC[1][0]-AB[1][0]*AC[0][0];
			
			//Use distance equation with the cross product
			Area = (Math.sqrt(Math.pow(crossProduct[0][0],2) + Math.pow(crossProduct[0][1],2) + Math.pow(crossProduct[0][2],2)))/2;
		}
		
		return Double.valueOf(df.format(Area));
	}
	
	public static double calculateDistance(double[][] triangle, boolean is2D) {
		if(is2D) {
			//Save all three coordinates, (X1,Y1) and (X2,Y2) are the coordinates for the line
			double pointX1 = triangle[0][0];
			double pointY1 = triangle[1][0];
			double pointX2 = triangle[0][1];
			double pointY2 = triangle[1][1];
			
			//3X and 3Y are the points we are trying to find the distance of from the line above
			double point3X = triangle[0][2];
			double point3Y = triangle[1][2];
			
			double slope = (pointY2-pointY1)/(pointX2-pointX1);	//Calculate the slope so we can get y=mx
			
			double newSlope = (1/slope)*-1;	//newSlope is -(1/m). We will use this when calculating point-slope form 
			
			double pointIntercept = (newSlope*(point3X*-1))+(point3Y); //This is calculating the point-slope form to find out the intercept
			
			double x = slope + (-newSlope);	//After "substituting" the original slope equation into y in point slope form, we are moving the X's (both intercepts) to one side
			
			//Intersection coordinates from (point3X,point3Y) to the line
			double finalX = pointIntercept/x;
			double finalY = finalX*slope;

			double distance = Math.sqrt(Math.pow(point3X-finalX, 2) + Math.pow(point3Y-finalY, 2));	//Finally use the distance formula
			
			return Double.valueOf(df.format(distance));
		}
		else {
			double pointX1 = triangle[0][0];
			double pointY1 = triangle[1][0];
			double pointZ1 = triangle[2][0];
			double pointX2 = triangle[0][1];
			double pointY2 = triangle[1][1];
			double pointZ2 = triangle[2][1];
			
			double point3X = triangle[0][2];
			double point3Y = triangle[1][2];
			double point3Z = triangle[2][2];
			
			//Calculate values for ax + by + cz = 0
			double a = (pointX2-pointX1);
			double b = (pointY2-pointY1);
			double c = (pointZ2-pointZ1);
			double d = -((a*pointX2)+(b*pointY2)+(b*pointZ2));
			
			//This is the distance equation |ax+by+cz| / (a^2+b^2+c^2)^(1/2)
			double distance = (Math.abs((a*point3X)+(b*point3Y)+(c*point3Z))+d) / (Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)+Math.pow(c, 2)));
			
			return Double.valueOf(df.format(distance));
		}
	}
	
	//Read the text file and populate the matrix with the data in it
	static void readFile(String fileName, double[][] Matrix) {
		int rowCounter = 0; 
		int colCounter = 0;
		int colSize = Matrix[0].length;
		
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			while(myReader.hasNext()) {
				Matrix[rowCounter][colCounter] = myReader.nextDouble();
				colCounter++;
				
				if(colCounter == colSize) {
					colCounter = 0;
					rowCounter++;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public static boolean twoDimensionalOrThreeDimensional(String fileName) {
		int lineCounter = 0;
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			while(myReader.hasNext()) {
				lineCounter++;	//Count how many lines there are in the text file
				myReader.nextLine();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		//If there are 3 rows, then we are in 3D. Else, we are in 2D
		if(lineCounter == 3) {
			return false;
		}
		return true;
	}
	
	//Write data into a file. File depends on whether its 2D or 3D
	static void write(String area, String distance, boolean is2D) {
		try {
			if(is2D) {
				FileWriter myWriter = new FileWriter("PA3_p3_2D_Output.txt");
				myWriter.write(area+"\n");
				myWriter.write(distance);
				myWriter.close();
			}
			else {
				FileWriter myWriter = new FileWriter("PA3_p3_3D_Output.txt");
				myWriter.write(area+"\n");
				myWriter.write(distance);
				myWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
