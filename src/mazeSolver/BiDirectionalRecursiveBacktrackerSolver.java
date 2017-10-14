package mazeSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import maze.Cell;
import maze.Maze;
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

		boolean[][] markedEn = new boolean[maze.sizeR][maze.sizeC];
		boolean[][] markedEx = new boolean[maze.sizeR][maze.sizeC];
		
		this.initMarked(markedEn, maze.sizeR, maze.sizeC);
		this.initMarked(markedEx, maze.sizeR, maze.sizeC);
		
//		this.start.add(maze.entrance);
//		this.exit.add(maze.exit);
		
		this.start = maze.entrance;
		this.exit = maze.exit;
		
		this.stackIntersectionEn.push(this.start);	
		maze.drawFtPrt(start);
		this.stackIntersectionEx.push(this.exit);
		maze.drawFtPrt(exit);
		
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
				//BackTrack
				Cell lastInterection = null;				
				
				if (aIsNewMoves) {					
					while (!this.stackIntersectionEn.isEmpty()) {
						lastInterection = (Cell) this.stackIntersectionEn.pop();
						System.out.println("back to : " + lastInterection.r + "," + lastInterection.c);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
							break;
						}
					}					
					moved = true;
				}
				else {
					while (!this.stackIntersectionEx.empty()) {
						lastInterection = (Cell) this.stackIntersectionEx.pop();
						System.out.println("back to : " + lastInterection.r + "," + lastInterection.c);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
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
			}
			else {
				aEntrance = this.exit;
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


/*package mazeSolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 *
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {	
	
	private boolean isSolved = false;
	private int nVisited = 0;
//	private Stack<Queue<Cell>> stackIntersectionEn = new Stack<Queue<Cell>>();
//	private Stack<Queue<Cell>> stackIntersectionEx = new Stack<Queue<Cell>>();
	private Queue<Cell> start, exit;
	
	@Override
	public void solveMaze(Maze maze) {		

		boolean[][] markedEn = new boolean[maze.sizeR][maze.sizeC];
		boolean[][] markedEx = new boolean[maze.sizeR][maze.sizeC];
		
		this.initMarked(markedEn, maze.sizeR, maze.sizeC);
		this.initMarked(markedEx, maze.sizeR, maze.sizeC);
		
		start = new ArrayDeque<Cell>();
		exit = new ArrayDeque<Cell>();
		
		this.start.add(maze.entrance);
		this.exit.add(maze.exit);
		
//		this.stackIntersectionEn.push(this.start);		
//		this.stackIntersectionEx.push(this.exit);
		
		this.performDfs(this.start, this.exit, maze, markedEn, markedEx, true);
	} // end of solveMaze()


	
	public void performDfs(Queue<Cell> aEntrance1, Queue<Cell> aEntrance2, Maze aMaze, boolean[][] aMarked1, boolean[][] aMarked2, boolean aIsNewMoves) {
		
//		if (aEntrance1.poll().c == aEntrance2.poll().c && aEntrance1.poll().r == aEntrance2.poll().r) {
//			this.isSolved = true;
//			return;
//		}
						
		Cell aEntrance = (aIsNewMoves == true) ? aEntrance1.peek() : aEntrance2.peek(); 
		boolean[][] aMarked = (aIsNewMoves == true) ? aMarked1 : aMarked2;		
			
		boolean moved = false;
		while (!moved) {
			
			Queue<Cell> goTo = this.dfs(aEntrance, aMarked, aMaze, aIsNewMoves);
			if (goTo != null) {
				aIsNewMoves = !aIsNewMoves;	
				moved = true;
				this.performDfs((aIsNewMoves == false)? goTo : aEntrance1, (aIsNewMoves == false)? aEntrance2 : goTo, aMaze, aMarked1, aMarked2, aIsNewMoves);
				break;
			}
			
			if (!moved) {
				//BackTrack
				Cell lastInterection = null;				
				
				if (aIsNewMoves) {					
					for (int i = 0; i < this.start.size(); i++) {
						lastInterection = (Cell) this.start.remove();
						System.out.println("back to : " + lastInterection.r + "," + lastInterection.c);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
							break;
						}
					}					
					moved = true;
				}
				else {
					for (int i = 0; i < this.exit.size(); i++) {
						lastInterection = (Cell) this.exit.remove();
						System.out.println("back to : " + lastInterection.r + "," + lastInterection.c);
						goTo = this.dfs(lastInterection, aMarked, aMaze, aIsNewMoves);
						if (goTo != null) {
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
	
	private Queue<Cell> dfs(Cell aEntrance, boolean[][] aMarked, Maze aMaze, boolean aIsNewMoves) {
		aMaze.drawFtPrt(aEntrance);
		
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
				nVisited++;
				
				if (aIsNewMoves) {
					this.start.add(goTo);	
					return this.start;
				}
				else {
					this.exit.add(goTo);
					return this.exit;
				}
//				
//				aEntrance.add(goTo);
//				return aEntrance;
			}	
		}
		return null;
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
*/