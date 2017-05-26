import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Game {

	public static void main(String[] args) throws IOException {
		/**
		 * Object of the Operations class
		 */
		Operations ops = new Operations();
		
		/**
		 * Method that adds a maze to the maze.txt file
		 */
		ops.addMazeToFile();
		
		/**
		 * Method that reads the file from a maze
		 */
		ops.readMazeFromFile();
		
		/**
		 * Method that solves the maze and prints it
		 */
		ops.solver();
	}
}
