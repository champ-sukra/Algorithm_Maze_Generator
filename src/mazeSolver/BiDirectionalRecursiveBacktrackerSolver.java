package mazeSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import maze.Cell;
import maze.Maze;
import maze.StdDraw;
import maze.Wall;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {	
	
	private boolean isSolved = false;
	private int nVisited = 0;
	private Stack<Cell> stackIntersectionEn = new Stack<Cell>();
	private Stack<Cell> stackIntersectionEx = new Stack<Cell>();
	private Cell start, exit;
	
	@Override
	public void solveMaze(Maze maze) {		

		Integer sizeC = maze.type == Maze.NORMAL? maze.sizeC : maze.sizeC + ((maze.sizeR + 1) / 2);
		
		boolean[][] markedEn = null;
		boolean[][] markedEx = null;
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL) {
			boolean[][] marked = new boolean[maze.sizeR][sizeC];
			this.initMarked(marked, maze.sizeR, sizeC);
		} 
		else {
			markedEn = new boolean[maze.sizeR][sizeC];
			markedEx = new boolean[maze.sizeR][sizeC];
			
			this.initMarked(markedEn, maze.sizeR, sizeC);
			this.initMarked(markedEx, maze.sizeR, sizeC);			
		}	
				
		this.start = maze.entrance;
		this.exit = maze.exit;
		
		this.reset(maze, true);					
		this.reset(maze, false);
		
		this.performDfs(this.start, this.exit, maze, markedEn, markedEx, true);
	} // end of solveMaze()


	
	public void performDfs(Cell aEntrance1, Cell aEntrance2, Maze aMaze, boolean[][] aMarked1, boolean[][] aMarked2, boolean aIsNewMoves) {
		if (this.isSolved) {
			return;
		}
		for (int i = 0; i < aMaze.sizeR; i++) {
			for (int j = 0; j < aMaze.sizeC; j++) {
				if (aMarked1[i][j] == true &&
						aMarked2[i][j] == true) {
					this.isSolved = true;
					return;
				}
			}
		}	
				
		Cell aEntrance = (aIsNewMoves == true) ? aEntrance1 : aEntrance2; 
		boolean[][] aMarked = (aIsNewMoves == true) ? aMarked1 : aMarked2;		
			
		boolean moved = false;
		while (!moved) {
			
			Cell goTo = this.dfs(aEntrance, aMarked, aMaze, aIsNewMoves);
			if (goTo != null) {
				aIsNewMoves = !aIsNewMoves;	
				moved = true;
				this.performDfs((aIsNewMoves == false)? goTo : aEntrance1, (aIsNewMoves == false)? aEntrance2 : goTo, aMaze, aMarked1, aMarked2, aIsNewMoves);
				break;
			}
			
			if (!moved) {
				aMarked[aEntrance.r][aEntrance.c] = true;
				
				//BackTrack
				Cell lastInterection = null;				
				
				if (aIsNewMoves) {					
					while (!this.stackIntersectionEn.isEmpty()) {
						Cell last = this.stackIntersectionEn.lastElement();
						if (last.equals(this.start)) {
							lastInterection = last;
						}
						else {
							lastInterection = (Cell) this.stackIntersectionEn.pop();
						}						
						StdDraw.setPenColor(StdDraw.WHITE);
						StdDraw.filledCircle(lastInterection.c + 0.5, lastInterection.r + 0.5, 0.25);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
							this.stackIntersectionEn.push(lastInterection);
							break;
						}
					}					
					moved = true;
				}
				else {
					while (!this.stackIntersectionEx.empty()) {
						Cell last = this.stackIntersectionEx.lastElement();
						if (last.equals(this.exit)) {
							lastInterection = last;
						}
						else {
							lastInterection = (Cell) this.stackIntersectionEx.pop();
						}	
						StdDraw.setPenColor(StdDraw.WHITE);
						StdDraw.filledCircle(lastInterection.c + 0.5, lastInterection.r + 0.5, 0.25);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
							this.stackIntersectionEx.push(lastInterection);
							break;
						}
					}	
					moved = true;
				}	
				
				if (aIsNewMoves)
					System.out.println("1 -- moved");
				else
					System.out.println("2 -- moved");
				if (moved) {
					//Switch turn
					aIsNewMoves = !aIsNewMoves;							
				}
				this.performDfs((aIsNewMoves == false)? goTo : aEntrance1, (aIsNewMoves == false)? aEntrance2 : goTo, aMaze, aMarked1, aMarked2, aIsNewMoves);
			}
		}		
	}	
	
	private Cell dfs(Cell aEntrance, boolean[][] aMarked, Maze aMaze, boolean aIsNewMoves) {				
		if (aEntrance == null) {			
			if (aIsNewMoves) {
				aEntrance = this.start;
				this.reset(aMaze, aIsNewMoves);
			}
			else {
				aEntrance = this.exit;
				this.reset(aMaze, aIsNewMoves);
			}			
		}
		HashMap<Integer, Wall> walls = new HashMap<Integer, Wall>();		
		for (int i = 0; i < aEntrance.wall.length; i++) {
			Wall wall = aEntrance.wall[i];
			if (wall != null && wall.present == false) {
				walls.put(new Integer(i), wall);
			}
		}

		List<Integer> keys = new ArrayList<Integer>(walls.keySet());
		Collections.shuffle(keys);
		
		Cell goTo = null;
		for (int direction : keys) {
			goTo = aEntrance.neigh[direction];								
			if (!aMarked[goTo.r][goTo.c]) {					
				
				aMarked[aEntrance.r][aEntrance.c] = true;				
				aMaze.drawFtPrt(goTo);
				nVisited++;
				
				if (aIsNewMoves) {
					if (!this.stackIntersectionEn.contains(goTo))
						this.stackIntersectionEn.push(goTo);
				}
				else {
					if (!this.stackIntersectionEx.contains(goTo)) {
						this.stackIntersectionEx.push(goTo);

					}
				}
				
				return goTo;
			}	
		}
		return null;
	}
	
	private void reset(Maze aMaze, boolean aNewMove) {
		if (aNewMove) {
			this.stackIntersectionEn.push(this.start);	
			aMaze.drawFtPrt(start);
		}		
		else {
			this.stackIntersectionEx.push(this.exit);
			aMaze.drawFtPrt(exit);
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
		return this.isSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return this.nVisited;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
