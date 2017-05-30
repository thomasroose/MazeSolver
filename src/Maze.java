

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Maze generator class
 *
 */
public class Maze {	
	private char grid [][];
	private int size;
	private int gridSize;
	private static final char wall = 'X';
	private static final char unvisited = 'U';
	private static final char corridor = ' ';
	private int outerWallPosition;
	private RandomList<Coordinate> listOfWalls = new RandomList<Coordinate>();
	
	/**
	 * Generates a maze given a size
	 * @param size determines the number of free cells in the starting grid precondition: > 0
	 */
	public Maze(int size){
		this.size = size;
		gridSize = size * 2 + 1;
		outerWallPosition = gridSize - 1;
		grid = new char[gridSize][gridSize];
		initialise();
		pickStartCell();
		constructMaze();
		makeEntryAndExit();
	}
	
	/**
	 * Makes an entry and exit next to a corridor in the maze
	 */
	private void makeEntryAndExit(){
		grid[validEntryPoint()][0] = corridor;
		grid[validExitPoint()][outerWallPosition] = corridor;
	}
	
	/**
	 * @return a random position in the left wall next to a corridor
	 */
	private int validEntryPoint(){
		int randomRow; 
		do randomRow = randomPosition();
		while(grid[randomRow][1] != corridor);
		return randomRow;
	}
	
	/**
	 * @return a random position in the right wall next to a corridor
	 */
	private int validExitPoint(){
		int randomRow; 
		do randomRow = randomPosition();
		while(grid[randomRow][outerWallPosition-1] != corridor);
		return randomRow;
	}
	
	/**
	 * Uses the Prim Jarnik algorithm to construct a minimum spanning tree given a grid of cells which are surrounded by walls
	 */
	private void constructMaze(){
		while(!listOfWalls.isEmpty()){
			Coordinate position = listOfWalls.removeRandom();
			boolean newCellFound = checkCell(position.row(), position.col());
			if(newCellFound)grid[position.row()][position.col()] = corridor;			
		}
	}
	
	/**
	 * Determines if the cell next to the wall is an unvisited cell, and visit it if it is unvisited 
	 * @param row of the wall
	 * @param col of the wall
	 * @return true if the wall should be collapsed, false if not
	 */
	private boolean checkCell(int row, int col){
		if(grid[row + 1][col] == unvisited){
			visitCell(row + 1, col);
			return true;
		}
		if(grid[row - 1][col] == unvisited){
			visitCell(row - 1, col);
			return true;
		}
		if(grid[row][col + 1] == unvisited){
			visitCell(row, col + 1);
			return true;
		}
		if(grid[row][col - 1] == unvisited){
			visitCell(row, col - 1);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return a random position in the grid, excluding outer walls
	 */
	private int randomPosition(){
		return ThreadLocalRandom.current().nextInt(0, size) * 2 + 1;
	}
	
	/**
	 * Visits a cell, adding its surrounding walls to the list of walls if applicable
	 * @param row of the cell
	 * @param col of the cell
	 */
	private void visitCell(int row, int col){
		grid[row][col] = corridor;
		addSurroundingWalls(row, col);
	}
	
	/**
	 * Picks the seed of the Prim Jarnik algorithm and visits it
	 */
	private void pickStartCell(){
		int row = randomPosition();
		int col = randomPosition();
		visitCell(row, col);		
	}
	
	/**
	 * Checks if the walls of an unvisited cell should be added to the list of walls
	 * @param row of the unvisited cell
	 * @param col of the unvisited cell
	 */
	private void addSurroundingWalls(int row, int col){
		if(row > 1 && grid[row - 2][col] == unvisited && grid[row-1][col] == wall) listOfWalls.add(new Coordinate(row-1, col));
		if(row < gridSize - 2 && grid[row + 2][col] == unvisited && grid[row+1][col] == wall) listOfWalls.add(new Coordinate(row+1, col));
		if(col > 1 && grid[row][col-2] == unvisited && grid[row][col-1] == wall) listOfWalls.add(new Coordinate(row, col-1));
		if(col < gridSize - 2 && grid[row][col+2] == unvisited && grid[row][col+1] == wall) listOfWalls.add(new Coordinate(row, col+1));
	}
	
	/**
	 * Initialises a grid of walled cells with solid outer walls
	 */
	private void initialise(){
		for(int row = 0; row < gridSize; row++){
			for(int col = 0; col < gridSize; col++){
				if(row == 0) grid[row][col] = wall;
				else if(col == 0) grid[row][col] = wall;
				else if(row == outerWallPosition) grid[row][col] = wall;
				else if(col == outerWallPosition) grid[row][col] = wall;
				else if(col % 2 != 0 && row % 2 != 0) grid[row][col] = unvisited;
				else grid[row][col] = wall;
			}
		}
	}
	
	/**
	 * Prints the maze 'X' represents a wall
	 */
	public void print(){
		for(int i = 0; i < gridSize; i++){
			System.out.println(grid[i]);
		}
		System.out.println("");
	}
	
	/**
	 * 
	 * @return the maze as a list of strings, each element representing a row of the maze
	 */
	public List<String> toListOfStrings(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < gridSize; i++){
			list.add(new String(grid[i]));
		}
		return list;
		
	}
	
	/**
	 * Data class to represent a coordinate in the maze
	 *
	 */
	private class Coordinate{
		private final int row, col;
		
		public Coordinate(int row, int col){
			this.row = row;
			this.col = col;
		}
		
		public int row(){
			return row;
		}
		
		public int col(){
			return col;
		}
	}
	
	public void makeMaze(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter an integer to determine the size of the maze");
		
		String entered = scanner.nextLine(); 
		int size = Integer.parseInt(entered);
		
		System.out.println("Generating maze...");
		Maze maze = new Maze(size);
		maze.print();
		
		List<String> lines = maze.toListOfStrings();
		String filename = "maze.txt";
		System.out.println("Save to file: enter filename or use default [" + filename + "]");
		
		entered = scanner.nextLine();
		scanner.close();
		
		if(!entered.equals("")) filename = entered;
		
		Path file = Paths.get(filename);
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
			System.out.println(filename + " saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
