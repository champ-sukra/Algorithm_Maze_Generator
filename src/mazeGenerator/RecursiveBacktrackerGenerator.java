package mazeGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
				
		//Getting a exit cell for stop looping
		Cell entrance = maze.entrance;		
		this.traverse(entrance, maze);	
		
	} // end of generateMaze()
	
	private void traverse(Cell aEntrance, Maze aMaze) {		
		HashMap<String, Cell> traversalOrder = new HashMap<String, Cell>();
		traversalOrder.put(String.valueOf(aEntrance.c) + "-" + String.valueOf(aEntrance.r), aEntrance);		
		
		System.out.println("c : " + aEntrance.c);
		System.out.println("r : " + aEntrance.r);
		
		//Getting neighbor from random		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();		
		for (Cell cell : aEntrance.neigh) { //0, 2
			if (cell != null) {
				if (!traversalOrder.containsKey(String.valueOf(cell.c) + "-" + String.valueOf(cell.r))) {
					neighbors.add(cell);
				}
			}			
		}
		
		Collections.shuffle(neighbors);
		this.markDirection(aEntrance, neighbors.get(0));
		this.dfs(neighbors.get(0), traversalOrder);
	}
	
	private void dfs(Cell aNeighbor, HashMap<String, Cell> aOrders) {
		System.out.println("c : " + aNeighbor.c);
		System.out.println("r : " + aNeighbor.r);
		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();		
		for (Cell cell : aNeighbor.neigh) { //0, 2
			if (cell != null) {
				neighbors.add(cell);				
			}			
		}
		
		Collections.shuffle(neighbors);
		aOrders.put(String.valueOf(aNeighbor.c) + "-" + String.valueOf(aNeighbor.r), aNeighbor);
		
		for (Cell cell : neighbors) {
			if (!aOrders.containsKey(String.valueOf(cell.c) + "-" + String.valueOf(cell.r))) {
				this.markDirection(aNeighbor, cell);
				this.dfs(cell, aOrders);
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

} // end of class RecursiveBacktrackerGenerator
