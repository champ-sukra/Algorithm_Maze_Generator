package mazeGenerator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	int[] directions;
			
	@Override
	public void generateMaze(Maze maze) {								
		
		boolean[][] marked = null;
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL) {
			marked = new boolean[maze.sizeR][maze.sizeC];
			this.initMarked(marked, maze.sizeR, maze.sizeC);
			this.directions = new int[] {Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH};
		} 
		else {
			marked = new boolean[maze.sizeR][maze.sizeC + ((maze.sizeR + 1) / 2)];
			this.initMarked(marked, maze.sizeR, (maze.sizeR + 1) / 2);
			this.directions = new int[] {Maze.EAST, Maze.NORTHEAST, Maze.NORTHWEST, Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
		}
		
		//Getting a exit cell for stop looping
		int sizeC = maze.type == Maze.NORMAL? maze.sizeC : maze.sizeC + ((maze.sizeR + 1) / 2);
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
		
		for (Integer direction : randomDirection) {
			Cell neighbor = aNeighbor.neigh[direction];
			if (neighbor != null && !aMarked[neighbor.r][neighbor.c]) {
				aNeighbor.wall[direction].present = false;
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
