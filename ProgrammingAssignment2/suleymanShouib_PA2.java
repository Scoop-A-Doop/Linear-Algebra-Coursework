import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
public class suleymanShouib_PA2 {
	static int turn = 1;	//This will be used to keep track of what turn it is
	public static void main(String[] args) {
		//To make the program more user friendly, I make it so that you need to type the name of the file that you are using to play the game
		//This makes it so the user does not need to edit the code to play a game with a different file
		Scanner myScanner = new Scanner(System.in);
		System.out.println("Please type the name of the file you will be using to play Linear Domination, including \".txt\" at the end.");
		String fileName = myScanner.nextLine();
		
		char [][] gameBoard = new char[createBoard(fileName)][createBoard(fileName)];	//This is the main gameboard
		String [] lastKTurns = new String[findK(fileName)];	//This will keep track of coordinates played in the last K turns. This will be used to determine whether a move is valid or not
		
		playGame(fileName, gameBoard, lastKTurns); //Start the game
		myScanner.close();
	}
	
	//Only reads the very first number in the txt file to set up the board
	public static int createBoard(String fileName) {
		int boardSize = 0;
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			String data = myReader.next();
			boardSize = Integer.parseInt(data);
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return boardSize;
	}
	
	//Only reads the second number in the txt file to set up K
	public static int findK(String fileName) {
		int valueK = 0;
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			myReader.next();
			String data = myReader.next();
			valueK = Integer.parseInt(data);
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return valueK;
	}
	
	//Reads a file and goes line by line executing every move, every line alternating between player X and player O
	public static void playGame(String fileName, char[][] board, String[] lastXTurns) {
		int whosTurn = 0;	//If the number is odd, then it is X's turn. If it is even, it is O's turn. This keeps track of who's turn it is
		int sRow, sCol;	//Coordinates for the first point
		int eRow, eCol;	//Coordinates for the final point
		int counter = lastXTurns.length;	//This is important for the first X turns. It lets the game know which index to place the current coord in since at this point the array is not completely filled
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			while (myReader.hasNext()) {
				whosTurn++;	//Moves to the next persons turn
				
				//Obtain coordinates from the file
				sRow = Integer.parseInt(myReader.next())-1;
				sCol = Integer.parseInt(myReader.next())-1;
				eRow = Integer.parseInt(myReader.next())-1;
				eCol = Integer.parseInt(myReader.next())-1;
				
				/* First check to see if the move is invalid or not
				 * Then depending on whether it is X's turn or O's turn,
				 * Check to see if lastXTurns has an open spot. If so, place the current move onto the next available space. Otherwise,
				 * Free the oldest move and place the newest move there. 
				 * Finally, place the persons start and end coordinates in the graph and call connectLine() to "connect the dots"
				 */
				if(lastXTurns.length == 0 && (board[sRow][sCol] == 'X' || board[sRow][sCol] == 'O' || board[eRow][eCol] == 'X' || board[eRow][eCol] == 'O') ||!checkValidMove(lastXTurns, sRow, sCol, eRow, eCol)) {
					System.out.println("Sorry, that's not a valid move!");
				}
				else if(whosTurn % 2 != 0) {
					if(counter != 0) {
						String sR = Integer.toString(sRow);
						String sC = Integer.toString(sCol);
						String eR = Integer.toString(eRow);
						String eC = Integer.toString(eCol);
						String savedCoords = sR+sC+eR+eC;
						lastXTurns[counter-1] = savedCoords;
						counter--;
					}
					else {
						if(lastXTurns.length != 0) {
							shiftArray(lastXTurns);
							String sR = Integer.toString(sRow);
							String sC = Integer.toString(sCol);
							String eR = Integer.toString(eRow);
							String eC = Integer.toString(eCol);
							String savedCoords = sR+sC+eR+eC;
							lastXTurns[0] = savedCoords;
						}
					}
					board[sRow][sCol] = 'X';
					board[eRow][eCol] = 'X';
					connectLine(board, sRow, sCol, eRow, eCol, 'X');
				}
				else {
					if(counter != 0) {
						String sR = Integer.toString(sRow);
						String sC = Integer.toString(sCol);
						String eR = Integer.toString(eRow);
						String eC = Integer.toString(eCol);
						String savedCoords = sR+sC+eR+eC;
						lastXTurns[counter-1] = savedCoords;
						counter--;
					}
					else {
						if(lastXTurns.length != 0) {
							shiftArray(lastXTurns);
							String sR = Integer.toString(sRow);
							String sC = Integer.toString(sCol);
							String eR = Integer.toString(eRow);
							String eC = Integer.toString(eCol);
							String savedCoords = sR+sC+eR+eC;
							lastXTurns[0] = savedCoords;
						}
					}
					board[sRow][sCol] = 'O';
					board[eRow][eCol] = 'O';
					connectLine(board, sRow, sCol, eRow, eCol, 'O');
				}
				
				/* Check to see if the game is over. If it is, then write the final score into a file and end the game
				 * Else, the game is still going so print out the current board and the current score
				 */
				if(checkGameOver(board)) {
					System.out.println("Game Over!");
					printBoard(board);
					System.out.println("\nFinal Score");
					countScore(board);
					System.out.println("Writing the final score and final board into a file...");
					writeFinalBoard(board);
					myReader.close();
					return;
				}
				else {
					printBoard(board);
					System.out.println("\nCurrent Score");
					countScore(board);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		/* If the program makes it to here, that means that the board has not been completely filled with the moves in the file
		 * This means that the game is over so write the final score and board to a file and then the program/game will end
		 */
		System.out.println("There are no more moves left in the file!");
		System.out.println("Writing the final score and final board into a file...");
		writeFinalBoard(board);
	}
	
	//This updates the array to see what moves were done in the last X amount of turns
	public static void shiftArray(String[] lastXTurns) {
		if(lastXTurns.length != 1) {
			for (int i = lastXTurns.length-2; i >= 0; i--) {                
			    lastXTurns[i+1] = lastXTurns[i];
			}
		}
	}
	
	//Checks to see if the current move has been done in the last X amount of turns
	public static boolean checkValidMove(String[] lastXTurns, int sRow, int sCol, int eRow, int eCol) {
		String sR = Integer.toString(sRow);
		String sC = Integer.toString(sCol);
		String eR = Integer.toString(eRow);
		String eC = Integer.toString(eCol);
		String savedStart = sR+sC;
		String savedEnd = eR+eC;
		for(int i = 0; i < lastXTurns.length; i++) {
			//Skips elements in lastXTurns that are null to avoid null error. Also checks all 4 combinations making sure none of the coordinates has been played before for start or end
			if(lastXTurns[i] != null && (savedStart.equals(lastXTurns[i].substring(0, 2)) || savedEnd.equals(lastXTurns[i].substring(2, 4)) || savedStart.equals(lastXTurns[i].substring(2, 4)) || savedEnd.equals(lastXTurns[i].substring(0, 2)))) {
				return false;
			}
		}
		return true;
	}
	
	//Connects the two points by filling in the values between the starting point and ending point
	public static void connectLine(char[][] board, int sRow, int sCol, int eRow, int eCol, char player) {
		if(sRow < eRow && sCol < eCol) {
			while(sRow < eRow && sCol < eCol) {
				board[sRow][sCol] = player;
				sRow++;
				sCol++;
			}
			while(sRow < eRow) {
				board[sRow][sCol] = player;
				sRow++;
			}
			while(sCol < eCol) {
				board[sRow][sCol] = player;
				sCol++;
			}
		}
		
		else if(sRow > eRow && sCol < eCol) {
			while(sRow > eRow && sCol < eCol) {
				board[sRow][sCol] = player;
				sRow--;
				sCol++;
			}
			while(sRow > eRow) {
				board[sRow][sCol] = player;
				sRow--;
			}
			while(sCol < eCol) {
				board[sRow][sCol] = player;
				sCol++;
			}
		}
		
		else if(sRow < eRow && sCol > eCol) {
			while(sRow < eRow && sCol > eCol) {
				board[sRow][sCol] = player;
				sRow++;
				sCol--;
			}
			while(sRow < eRow) {
				board[sRow][sCol] = player;
				sRow++;
			}
			while(sCol > eCol) {
				board[sRow][sCol] = player;
				sCol--;
			}
		}
		
		else if(sRow > eRow && sCol > eCol) {
			while(sRow > eRow && sCol > eCol) {
				board[sRow][sCol] = player;
				sRow--;
				sCol--;
			}
			while(sRow > eRow) {
				board[sRow][sCol] = player;
				sRow--;
			}
			while(sCol > eCol) {
				board[sRow][sCol] = player;
				sCol--;
			}
		}
		
	}
	
	//Goes through the 2D array and prints out the current board plus current turn. At the end it increments the turn number
	public static void printBoard(char[][] board) {
		System.out.println("Turn: "+turn);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j]+" ");
			}
			System.out.println();	//Moves to the next row
		}
		turn++;
	}
	
	//Goes through the 2D array and counts how many spaces player X and player O has filled
	public static String countScore(char[][] board) {
		int player1Score = 0;
		int player2Score = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == 'X') {
					player1Score++;
				}
				else if(board[i][j] == 'O') {
					player2Score++;
				}
			}
		}
		System.out.println("X: "+player1Score+"\nO: "+player2Score);
		System.out.println("----------------");
		
		return "\nX: "+player1Score+"\nO: "+player2Score+"\n----------------";
	}

	//Checks to see if all spaces in the 2D array has been filled. If it has been, then the game is over
	public static boolean checkGameOver(char[][] board) {
		int squaresFilled = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == 'X' || board[i][j] == 'O') {
				squaresFilled++;
				}
			}
		}
		if(squaresFilled != board.length*board[0].length) {
			return false;
		}
		return true;
	}

	//Writes the final score and final board into a txt file called "FinalScoreAndBoard.txt"
	public static void writeFinalBoard(char[][] board) {
		try {
			FileWriter myWriter = new FileWriter("FinalScoreAndBoard.txt");
			myWriter.write("Final Score: "+countScore(board)+"\n");
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					myWriter.write(board[i][j]+" ");	
				}
				myWriter.write("\n");		//Goes to a new line after the row has been fully written
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done! Thanks for playing!");
	}

}