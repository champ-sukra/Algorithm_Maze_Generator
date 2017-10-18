package mazeGenerator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	int[] directions;
	int tunnel = 99;
	
	@Override
	public void generateMaze(Maze maze) {								
		
		int sizeC = maze.type == Maze.HEX? maze.sizeC + ((maze.sizeR + 1) / 2) :  maze.sizeC;

		boolean[][] marked = null;
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL) {
			marked = new boolean[maze.sizeR][sizeC];
			this.initMarked(marked, maze.sizeR, sizeC);
			this.directions = new int[] {Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH};
		} 
		else {
			marked = new boolean[maze.sizeR][sizeC];
			this.initMarked(marked, maze.sizeR, sizeC);
			this.directions = new int[] {Maze.EAST, Maze.NORTHEAST, Maze.NORTHWEST, Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
		}
		
		//Getting a exit cell for stop looping
		Cell currentCell = null;
		while (currentCell == null) {
			int randomR = randomWithRange(0, maze.sizeR);
			int randomC = randomWithRange(0, sizeC);
			currentCell = maze.map[randomR][randomC];
		}
		
		this.dfs(maze, currentCell, marked);		
		
	} // end of generateMaze()

	private void dfs(Maze aMaze, Cell aNeighbor, boolean[][] aMarked) {
		System.out.println("c : " + aNeighbor.c);
		System.out.println("r : " + aNeighbor.r);			
				
		List<Integer> randomDirection = new LinkedList<Integer>();
		for (int direction : this.directions){
			randomDirection.add(direction);
		}
		
		Collections.shuffle(randomDirection);
		
		aMarked[aNeighbor.r][aNeighbor.c] = true;

		Cell neighbor = null;
		if (aNeighbor.tunnelTo != null) {
			neighbor = aNeighbor.tunnelTo;
			if (!aMarked[neighbor.r][neighbor.c]) {
				this.dfs(aMaze, neighbor, aMarked);
			}
		}
		for (Integer direction : randomDirection) {
			neighbor = aNeighbor.neigh[direction];
			if (neighbor != null && !aMarked[neighbor.r][neighbor.c]) {
				if (direction != this.tunnel) {
					aNeighbor.wall[direction].present = false;
				}
				this.dfs(aMaze, neighbor, aMarked);
			}						
		}	
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
