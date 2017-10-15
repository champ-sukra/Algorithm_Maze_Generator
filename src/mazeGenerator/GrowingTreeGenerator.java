package mazeGenerator;

import java.util.*;

import maze.Maze;
import maze.Cell;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	
	double threshold = 0.1;
	ArrayList<Cell> zs = new ArrayList<Cell>();
	int[] directions;
	
	@Override
	public void generateMaze(Maze maze) {
		
		int sizeC = maze.type == Maze.NORMAL? maze.sizeC : maze.sizeC + ((maze.sizeR + 1) / 2);
		
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
		
		//Step 1
		Cell currentCell = null;
		while (currentCell == null) {
			int randomR = randomWithRange(0, maze.sizeR);
			int randomC = randomWithRange(0, sizeC);
			currentCell = maze.map[randomR][randomC];
		}
		this.addQueue(currentCell);
		
		this.performGrowingTreeAlgorithm(marked);		
	}

	private void performGrowingTreeAlgorithm(boolean[][] aMarked) {
		//Step 2	
		
		List<Integer> randomDirection = new LinkedList<Integer>();
		for (int direction : this.directions){
			randomDirection.add(direction);
		}
		Collections.shuffle(randomDirection);
		
		while (!this.zs.isEmpty()) {
			Collections.shuffle(this.zs);
			Cell cellB = this.zs.get(0);
			
			/*
			ArrayList<Cell> neighbors = new ArrayList<Cell>();
			for (Cell cell : cellB.neigh) {
				if (cell != null && !aMarked[cell.r][cell.c]) {				
					neighbors.add(cell);
				}			
			}
			
			if (!neighbors.isEmpty()) {
				Collections.shuffle(neighbors);
				
				Cell neighbor = neighbors.get(0);		
				aMarked[neighbor.r][neighbor.c] = true;
				this.markDirection(cellB, neighbor);
				this.addQueue(neighbor);
				
				//Step 3 repeat step 2
			}		
			else {
				this.removeQueue(cellB);
			}
			
			/*/		
			
			boolean visitorFound = false;
			for (Integer direction : randomDirection) {
				Cell neighbor = cellB.neigh[direction];
				if (neighbor != null && !aMarked[neighbor.r][neighbor.c]) {
					cellB.wall[direction].present = false;
					aMarked[neighbor.r][neighbor.c] = true;
					visitorFound = true;
					this.addQueue(neighbor);
					break;
				}
			}	
			if (!visitorFound) {
				this.removeQueue(cellB);
			}
			
			//*/			
			
		}	
	}
	
	private void addQueue(Cell aCell) {
		this.zs.add(aCell);
	}
	
	private void removeQueue(Cell aCell) {
		this.zs.remove(aCell);
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
}
