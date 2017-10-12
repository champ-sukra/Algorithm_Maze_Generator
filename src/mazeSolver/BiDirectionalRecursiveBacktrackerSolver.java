package mazeSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {

	private boolean[][] markedEn;
	private boolean[][] markedEx;
	
	@Override
	public void solveMaze(Maze maze) {

		this.initMarked(maze.sizeR, maze.sizeC);
		
		Cell entrance = maze.entrance;		
		this.markedEn[entrance.r][entrance.r] = true;
		this.performDfsForEntrance(entrance);
		
		Cell exit = maze.exit;
		this.markedEn[entrance.r][entrance.r] = true;
//		this.performDfsForExit(exit);
		
	} // end of solveMaze()

	private void performDfsForEntrance(Cell aEntrance) {
		//Find direction
		
		HashMap<Integer, Wall> walls = new HashMap<Integer, Wall>();		
		for (int i = 0; i < walls.size(); i++) {
			Wall wall = aEntrance.wall[i];
			if (wall != null && wall.present == false) {
				walls.put(new Integer(i), wall);
			}
		}
		List<Integer> keys = new ArrayList<Integer>(walls.keySet());
		Collections.shuffle(keys);
		
		int direction = keys.get(0);
		Wall selectedWall = walls.get(direction);
		Cell goTo = aEntrance.neigh[direction];
	}
	
	private void performDfsForExit() {
		
	}

	private void initMarked(int aSizeR, int aSizeC) {
    	this.markedEn = new boolean[aSizeR][aSizeC];
		for (int i = 0; i < aSizeR; i++) {
			for (int j = 0; j < aSizeC; j++) {
				markedEn[i][j] = false;
			}
		}	
		
		this.markedEx = new boolean[aSizeR][aSizeC];
		for (int i = 0; i < aSizeR; i++) {
			for (int j = 0; j < aSizeC; j++) {
				markedEx[i][j] = false;
			}
		}	
    }
	
	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return 0;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
