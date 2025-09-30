// General hierarchy:
// create a maze game in cli to then be transferred to gui;.
// Basically need to create the game to the point it works in cli so can ensure maze algo works
// then add gui elements -- could have both work eg have gui be run in separate file and if not __main__ then run as cli

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class mazeGame {
	private static final Random RANDOM = new Random();
	private static final char WALL = '#';
	private static final char PATH = ' ';
	private static final char PLAYER = 'P';

	public static void main() {
		System.out.println("---- Welcome to Maze Game type shi ----");
		char[][] maze = createMaze(11, 11);
		printMaze(maze);
	}

	public static void printMaze(char[][] maze) {

		for (char[] row : maze) {
			System.out.println(row);
		}

	}

	static char[][] createMaze(int x, int y) {
		// defaults x or y to 8 if either havent been set/ are invalid
		if (x <= 0) {
			x = 7;
		}
		if (y <= 0) {
			y = 7;
		}

		System.out.println("Generating " + x + "x" + y + " Maze");
		char[][] maze = new char[y][x];

		// fill with # -- walls
		for (char[] row : maze) {
			Arrays.fill(row, WALL);
		}

		// pick a random point to then be Prims Mazed
		int[] startPoint = { RANDOM.nextInt(1, x), RANDOM.nextInt(1, y) };
		// System.out.println(startPoint[0]);
		// System.out.println(startPoint[1]);
		maze[startPoint[0]][startPoint[1]] = PLAYER;

		ArrayList<Wall> nextWalls = new ArrayList<Wall>();
		// from startpoint locate where next coord should be then add PATH if not WALL

		Wall currWall = new Wall(startPoint[0], startPoint[1]);
		nextWalls.add(currWall);

		// TODO: fix when hitting edge it finishes
		while (!nextWalls.isEmpty()) {
			// startPoint[0] < x - 1 & startPoint[1] < y - 1)
			nextWalls.remove(RANDOM.nextInt(nextWalls.size()));

			// check all four cardinal directions:
			if (maze[startPoint[0] + 1][startPoint[1]] == WALL & startPoint[0] < x - 1) { // if column+1 is
													// a wall
				maze[startPoint[0] + 1][startPoint[1]] = PATH;
				startPoint[0]++;
				Wall upWall = new Wall(startPoint[0] + 1, startPoint[1]);
				nextWalls.add(upWall);

			}
			if (maze[startPoint[0] - 1][startPoint[1]] == WALL & startPoint[0] > 0) { // if column-1 is a
													// wall
				maze[startPoint[0] - 1][startPoint[1]] = PATH;
				startPoint[0]--;
				Wall downWall = new Wall(startPoint[0] - 1, startPoint[1]);
				nextWalls.add(downWall);

			}
			if (maze[startPoint[0]][startPoint[1] + 1] == WALL & startPoint[1] < y - 1) { // if row+1 is a
													// wall
				maze[startPoint[0]][startPoint[1] + 1] = PATH;
				startPoint[1]++;
				Wall rightWall = new Wall(startPoint[0], startPoint[1] + 1);
				nextWalls.add(rightWall);

			}
			if (maze[startPoint[0]][startPoint[1] - 1] == WALL & startPoint[1] > 0) { // if row-1 is a wall
				maze[startPoint[0]][startPoint[1] - 1] = PATH;
				startPoint[1]--;
				Wall leftWall = new Wall(startPoint[0], startPoint[1] - 1);
				nextWalls.add(leftWall);

			}

		}

		return maze;
	}

	static class Wall {
		int row, col, nextRow, nextCol;

		Wall(int row, int col, int nextRow, int nextCol) {
			this.row = row;
			this.col = col;
			this.nextRow = nextRow;
			this.nextCol = nextCol;
		}

		Wall(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

}
