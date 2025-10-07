// have a maze synth that creates a map object? using a passed-through 2d array.
public class mazeGame {
	private static final char WALL = '#';
	private static final char PATH = ' ';
	private static final char PLAYER = 'P';

	public static void main() {
		System.out.println("---- Welcome to Maze Game type shi ----");
		char[][] maze = createMaze(11, 11);
		printMaze(maze);
	}

	public static void printMaze(char[][] maze) {

		System.out.println("\n");
		for (char[] row : maze) {
			System.out.println(row);
		}
		System.out.println("\n");
	}

}

		return maze;
	}
