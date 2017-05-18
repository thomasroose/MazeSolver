import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Operations {
	Scanner scanner = new Scanner(System.in);
	private char [][] maze;
	private static int row = 0;
	private static int col = 0;
	
	private int startRow = -1;
	private int startCol = -1;
	
	/**
	 * Add the maze to the maze.txt file
	 * @throws IOException
	 */
	public void addMazeToFile() throws IOException{
		System.out.println("Enter an integer to determine the size of the maze");
		
		String entered = scanner.nextLine(); 
		int size = Integer.parseInt(entered);
		scanner.close();
		
		System.out.println();
		System.out.println("Generating maze...");
		System.out.println();
		
		Maze maze = new Maze(size);
		maze.print();
		
		List<String> lines = maze.toListOfStrings();
		String filename = "maze.txt";
		System.out.println("Save to file: [" + filename + "]");
		
		Path file = Paths.get(filename);
		Files.write(file, lines, Charset.forName("UTF-8"));
		System.out.println(filename + " saved");
	}
	
	/**
	 * Read the maze from the maze.txt file
	 * @throws IOException
	 */
	public void readMazeFromFile() throws IOException{
		File mazeFile = new File("maze.txt");
		BufferedReader read = new BufferedReader(new FileReader(mazeFile));
		
		int counter = 0;
		String line = read.readLine();
        
        row = line.length();
        col = line.length();
        maze = new char[row][col];
        
        for(int i = 0; i < line.length(); i++){
 		   maze[counter][i] = line.charAt(i);
 	   	}
        counter++;
      
        String s = null;
        
        while((s = read.readLine()) != null){
        	for(int i = 0; i < s.length(); i++){
        		maze[counter][i] = s.charAt(i);
        	}
    	   counter++;
        }
        findStartAndEnd();
        replaceXs();
        
        System.out.println();
        System.out.println("Maze loaded");
        System.out.println("Dimensions = " + row + ":" + col);
	}
	
	/**
	 * Replace the start position with 'S' and the end position with 'F'
	 */
	public void findStartAndEnd(){
		for(int i = 0; i < col; i++){
			if(maze[i][0] != 'X'){
				startRow = i;
				startCol = 0;
				maze[i][0] = 'S';	
			}
			int lastCol = col-1;
			if(maze[i][lastCol] != 'X'){
				maze[i][lastCol] = 'F';
			}
		}
	}
	
	/**
	 * Replace the 'X' with '+' to make it more clear
	 */
	public void replaceXs(){
		for(int i = 0; i < maze.length; i ++){
			for(int j = 0; j < maze.length; j++){
				if(maze[i][j] == 'X'){
					maze[i][j] = '+';
				}
			}
		}
	}
	
	/**
	 * Print the maze
	 */
	public void printMaze(){
		for(int i = 0; i < maze.length; i ++){
			for(int j = 0; j < maze.length; j++){
				if(maze[i][j] == 'A'){
					maze[i][j] = ' ';
				}
				if(maze[i][0] == '#'){
					maze[i][0] = 'S';
				}
			}
		}
		for(int i = 0; i < maze.length; i++){
			System.out.println(maze[i]);
		}
	}

	/**
	 * Print the unsolved maze
	 * Solve the maze 
	 * Print the solved maze
	 */
	public void solver(){
		System.out.println();
		System.out.println("Unsolved maze: ");
		System.out.println();
		printMaze();
		System.out.println();
		System.out.println("Solved maze: ");
		System.out.println();
		if(solveMaze(startRow,startCol)){
			printMaze();
		}	
	}
	
	/**
	 * Recursive backtracking algorithm to solve the maze
	 * @param r		the rows
	 * @param c		the columns
	 * @return true if the maze is solved, else false
	 */
	public boolean solveMaze(int r, int c){
		
		/**
		 * Check for boundaries (Out of bound exceptions)
		 */
		if( r < 0 || c < 0 || r >= maze.length || c >= maze.length){
			return false;
		}
			
		/**
		 * Did i reach the finish?
		 */
		if(maze[r][c] == 'F'){
			return true;
		}
		
		/**
		 * Check if I am at a valid point (hit a wall)
		 */
		//if (maze[r][c] == '+'){
		if(maze[r][c] != ' ' && maze[r][c] != 'S'){
			return false;
		}
		
		/**
		 * Been there done that
		 */
		maze[r][c] = 'A';
		
		/**
		 * Check above
		 */
		if(solveMaze(r-1, c)){
			maze[r][c] = '#';
			return true;
		}
		
		/**
		 * Check below
		 */
		if(solveMaze(r+1, c)){
			maze[r][c] = '#';
			return true;
		}
		
		/**
		 * Check left
		 */
		if(solveMaze(r, c-1)){
			maze[r][c] = '#';
			return true;
		}
		
		/**
		 * Check right
		 */
		if(solveMaze(r, c+1)){
			maze[r][c] = '#';
			return true;
		}
			
		/**
		 * Infinite loop
		 */
		return false;
	}	
}

