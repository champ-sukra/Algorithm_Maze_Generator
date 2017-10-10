package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;

import maze.Cell;
import maze.Maze;
import maze.Wall;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
				
		//Random cell from the neighbors of initial cell
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (Cell cell : maze.entrance.neigh) {
			if (cell != null) {
				System.out.println("c : " + cell.c);
				System.out.println("r : " + cell.r);
				System.out.println("r : " + cell.wall);				
				cells.add(cell);
			}			
		}
		
		Cell exit = maze.exit; 
		
		Cell setCell = null;
		while (setCell == null || setCell != exit) {
			setCell = this.settingUpWall(cells);
			
			cells.clear();
			for (Cell cell : setCell.neigh) {
				if (cell != null) {								
					cells.add(cell);
				}			
			}
		}				
		
	} // end of generateMaze()
	
	private Cell settingUpWall(ArrayList<Cell> aCells) {
		Random rand = new Random();
		int  n = rand.nextInt(aCells.size());
		Cell cell = aCells.get(n);
		ArrayList<Wall> walls = new ArrayList<Wall>();
		for (Wall wall : cell.wall) {
			walls.add(wall);	
		}		
		Wall wall = walls.get(0);
		wall.present = false;
		return cell;
	}

} // end of class RecursiveBacktrackerGenerator
