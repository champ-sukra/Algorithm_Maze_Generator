package mazeGenerator;

import java.util.ArrayList;
import java.util.Collections;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {
		ArrayList<Cell> zs = new ArrayList<Cell>();
		ArrayList<Cell> frontiers = new ArrayList<Cell>();
		
	@Override
	public void generateMaze(Maze maze) {
		
		//Step 1
		Cell entrance = maze.entrance;
		this.addVisited(entrance);		
		this.addFrontiersFrom(entrance);		
		
		//Step 2
		this.performPrimsAlgorithm();	
		
	} // end of generateMaze()

	private void performPrimsAlgorithm () {
		
		while (this.frontiers.size() > 0) {
			Collections.shuffle(frontiers);
			
			Cell cellC = frontiers.get(0);
			frontiers.remove(0);
			
			Collections.shuffle(zs);
			
			boolean isFound = false;
			for (Cell cellB : zs) {
				for (int i = 0; i < cellB.neigh.length; i++) {
					Cell neighbor = cellB.neigh[i];
					if (cellC == neighbor) {
						isFound = true;
						this.markDirection(cellB, cellC);
						
						//Step3
						this.addVisited(cellC);
						this.addFrontiersFrom(cellC);
						break;
					}
				}
				if (isFound) {
					break;
				}
			}		
		}
	}
	
	private void addVisited(Cell aCell) {
		this.zs.add(aCell);
	}
	
	private void addFrontiersFrom(Cell aCell) {
		for (Cell cell : aCell.neigh) {
			if (cell != null && !this.frontiers.contains(cell) && !this.zs.contains(cell)) {				
				this.frontiers.add(cell);
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
} // end of class ModifiedPrimsGenerator
