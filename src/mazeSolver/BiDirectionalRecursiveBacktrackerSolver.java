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
	
	private boolean nextStep = false;
	private int step = 0;
	
	private interface ICallbacks { 	    
	    public void onWalked(Cell aEntrance, boolean[][] aMarked, ICallbacks aBlock);
	}
	
	@Override
	public void solveMaze(Maze maze) {

		boolean[][] markedEn = new boolean[maze.sizeR][maze.sizeC];
		boolean[][] markedEx = new boolean[maze.sizeR][maze.sizeC];
		
		this.initMarked(markedEn, maze.sizeR, maze.sizeC);
		this.initMarked(markedEx, maze.sizeR, maze.sizeC);
		
		Cell entrance = maze.entrance;
		Cell exit = maze.exit;		
		
		this.performDfs(entrance, exit, maze, markedEn, markedEx, true);
	} // end of solveMaze()


	
	public void performDfs(Cell aEntrance1, Cell aEntrance2, Maze aMaze, boolean[][] aMarked1, boolean[][] aMarked2, boolean aIsNewMoves) {
		
		Cell aEntrance = (aIsNewMoves == true) ? aEntrance1 : aEntrance2; 
		boolean[][] aMarked = (aIsNewMoves == true) ? aMarked1 : aMarked2;		
		
		//Getting not present wall
		HashMap<Integer, Wall> walls = new HashMap<Integer, Wall>();		
		for (int i = 0; i < aEntrance.wall.length; i++) {
			Wall wall = aEntrance.wall[i];
			if (wall != null && wall.present == false) {
				walls.put(new Integer(i), wall);
			}
		}
		//Shuffling
		List<Integer> keys = new ArrayList<Integer>(walls.keySet());
		Collections.shuffle(keys);
					
		for (int direction : keys) {
			Cell goTo = aEntrance.neigh[direction];								
			if (!aMarked[goTo.r][goTo.c]) {					
				
				aMarked1[aEntrance.r][aEntrance.c] = true;
				aMaze.drawFtPrt(aEntrance);
				
				aIsNewMoves = !aIsNewMoves;
				this.performDfs((aIsNewMoves == false)? goTo : aEntrance1, (aIsNewMoves == false)? aEntrance2 : goTo, aMaze, aMarked1, aMarked2, aIsNewMoves);
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
