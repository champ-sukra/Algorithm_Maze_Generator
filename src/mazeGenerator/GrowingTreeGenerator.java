package mazeGenerator;

import java.util.*;

import maze.Maze;
import maze.Cell;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	
	double threshold = 0.1;
	ArrayList<Cell> zs = new ArrayList<Cell>();
	HashMap<String, Cell> traversalOrder = new HashMap<String, Cell>();
	
	@Override
	public void generateMaze(Maze maze) {
		
		//Step 1
		Cell entrance = maze.entrance;
		this.addVisited(entrance);
		
		//Step 2				
		while (!this.zs.isEmpty()) {
			Collections.shuffle(this.zs);
			Cell cellB = this.zs.get(0);
			
			ArrayList<Cell> neighbors = new ArrayList<Cell>();
			for (Cell cell : cellB.neigh) {
				if (cell != null && !this.zs.contains(cell) && !traversalOrder.containsKey(String.valueOf(cell.c) + "-" + String.valueOf(cell.r))) {				
					neighbors.add(cell);
				}			
			}
			
			if (!neighbors.isEmpty()) {
				Collections.shuffle(neighbors);
				
				Cell neighbor = neighbors.get(0);
				traversalOrder.put(String.valueOf(neighbor.c) + "-" + String.valueOf(neighbor.r), neighbor);
				this.markDirection(cellB, neighbor);
				this.addVisited(neighbor);
				
				//Step 3 repeat step 2
			}		
			else {
				this.zs.remove(cellB);
			}	
		}	
	}

	private void addVisited(Cell aCell) {
		this.zs.add(aCell);
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
