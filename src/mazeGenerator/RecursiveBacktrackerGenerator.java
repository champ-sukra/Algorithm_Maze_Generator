package mazeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	List<Integer> directions;
			
	@Override
	public void generateMaze(Maze maze) {
				
		boolean[][] marked = new boolean[maze.sizeR][maze.sizeC];
		this.initMarked(marked, maze.sizeR, maze.sizeC);
		
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL){
			directions = new ArrayList<Integer>(Arrays.asList(Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH));
		}
		else {
//			this.direction = new int[]{Maze.EAST, Maze.NORTHEAST, Maze.NORTHWEST, Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
		}
		
		//Getting a exit cell for stop looping
		Cell currentCell = null;
		while (currentCell == null) {
			int randomR = randomWithRange(0, maze.sizeR);
			int randomC = randomWithRange(0, maze.sizeC);
			currentCell = maze.map[randomR][randomC];
		}
		
		this.dfs(currentCell, marked);		
		
	} // end of generateMaze()

	private void dfs(Cell aNeighbor, boolean[][] aMarked) {
		System.out.println("c : " + aNeighbor.c);
		System.out.println("r : " + aNeighbor.r);			
		
		aMarked[aNeighbor.r][aNeighbor.c] = true;
		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();		
		for (Cell cell : aNeighbor.neigh) { //0, 2
			if (cell != null) {
				neighbors.add(cell);				
			}			
		}
		
		Collections.shuffle(neighbors);
		
		for (Cell neighbor : neighbors) {
			if (!aMarked[neighbor.r][neighbor.c]) {
				this.markDirection(aNeighbor, neighbor);
				this.dfs(neighbor, aMarked);
			}		
		}		
	}
	
	private void markDirection(Cell aEntrance, Cell aNeighbor) {
		//Comparing both cells
		int direction = 0;
		if (aNeighbor.c == aEntrance.c + 1) {
			System.out.println("Go East");
			direction = Maze.EAST;			
		}
		else if (aNeighbor.c == aEntrance.c - 1) {
			System.out.println("Go West");
			direction = Maze.WEST;
		}
		else if (aNeighbor.r == aEntrance.r - 1) {
			System.out.println("Go South");
			direction = Maze.SOUTH;
		}
		else if (aNeighbor.r == aEntrance.r + 1) {
			System.out.println("Go North");			
			direction = Maze.NORTH;
		}		
		aEntrance.wall[direction].present = false;
	}

	private void initMarked(boolean[][] aMarked, int aSizeR, int aSizeC) {
		for (int i = 0; i < aSizeR; i++) {
			for (int j = 0; j < aSizeC; j++) {
				aMarked[i][j] = false;
			}
		}	
    }
	 
	private int randomWithRange(int aMin, int aMax) {
		int range = (aMax - aMin);
		return (int)(Math.random() * range) + aMin;
	}
} // end of class RecursiveBacktrackerGenerator
