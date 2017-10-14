package mazeSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	private boolean[][] marked;
	private int directions[];
	private boolean solved = false;
	
	private Stack<HashMap<String, Object>> stackIntersection = new Stack<HashMap<String, Object>>();
	
	int nVisited = 0;
	int currentDirection = 100; //Start
	
	@Override
	public void solveMaze(Maze maze) {
		
		this.initMarked(maze.sizeR, maze.sizeC);
		
		//Ensure that the walker keeps moving to right hand side
		this.directions = new int[]{Maze.EAST, Maze.SOUTH, Maze.WEST, Maze.NORTH};
		
		Cell entrance = maze.entrance;
		Cell exit = maze.exit;
		
		this.nVisited = 1;
		this.marked[entrance.r][entrance.c] = true;			
		this.performWallFollower(entrance, exit, maze, false);
	} // end of solveMaze()
    
    private void initMarked(int aSizeR, int aSizeC) {
    	this.marked = new boolean[aSizeR][aSizeC];
		for (int i = 0; i < aSizeR; i++) {
			for (int j = 0; j < aSizeC; j++) {
				marked[i][j] = false;
			}
		}		
    }
    
    private void performWallFollower(Cell aEntrance, Cell aExit, Maze aMaze, boolean aTurn90Degree) {
    	while (this.solved != true) {   
        	boolean canWalk = false;
        	
			if (aTurn90Degree) {
				int direction = this.currentDirection;
				if (direction == Maze.EAST) {
					direction = Maze.WEST;
				}
				else if (direction == Maze.WEST) {
					direction = Maze.EAST;
				}
				else if (direction == Maze.NORTH) {
					direction = Maze.NORTH;
				}
				else if (direction == Maze.NORTH) {
					direction = Maze.NORTH;
				}
				this.markDirectionFrom(direction);
				this.performWallFollower(aEntrance, aExit, aMaze, false);
				break;
				
			}
			else {	//keep walking to directions
				
				ArrayList<Wall> walls = new ArrayList<Wall>();		
	    		for (Wall wall : aEntrance.wall) { //0, 2
	    			if (wall != null && wall.present == false) {
	    				walls.add(wall);
	    			}    			
	    		}
	    		
				for (int i = 0; i < this.directions.length; i++) {				
					int direction = this.directions[i];
					if (aEntrance.wall[direction].present == false && (!this.marked[aEntrance.neigh[direction].r][aEntrance.neigh[direction].c] || aEntrance == aMaze.entrance)) {
						
						if (walls.size() > 2) {
							walls.remove(aEntrance.wall[direction]);
							
							//Marking intersection
							HashMap<String, Object> markingIntersection = new HashMap<String, Object>();			    			
							markingIntersection.put("cell", aEntrance);
							markingIntersection.put("walls", walls);
							this.stackIntersection.push(markingIntersection);
							
						}
						Cell goToCell = aEntrance.neigh[direction];
						this.markDirectionFrom(direction);
						this.currentDirection = direction;
						this.marked[goToCell.r][goToCell.c] = true;
						this.nVisited++;
						aEntrance = goToCell;
						canWalk = true;
						aMaze.drawFtPrt(goToCell);
											
						if (goToCell.equals(aExit)) {
							this.solved = true;
						}					
						break;
					}
				}					
			}			
			
			if (!canWalk) {
				System.out.println(this.stackIntersection.size());
				HashMap<String, Object> markingIntersection = (HashMap<String, Object>) this.stackIntersection.pop();
				System.out.println(this.stackIntersection.size());
				aEntrance = (Cell) markingIntersection.get("cell");
				this.performWallFollower(aEntrance, aExit, aMaze, true);
				break;
			}
		}
    }
    
    private void markDirectionFrom(int aDirection) {
		if (aDirection == Maze.EAST) {
			this.directions = new int[]{Maze.SOUTH, Maze.WEST, Maze.NORTH, Maze.EAST};
		}
		else if (aDirection == Maze.SOUTH) {
			this.directions = new int[]{Maze.WEST, Maze.NORTH, Maze.EAST, Maze.SOUTH};
		}
		else if (aDirection == Maze.WEST) {
			this.directions = new int[]{Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST};
		}
		else if (aDirection == Maze.NORTH) {
			this.directions = new int[]{Maze.EAST, Maze.SOUTH, Maze.WEST, Maze.NORTH};
		}
	}
	
	@Override
	public boolean isSolved() {
		return this.solved; 
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		return this.nVisited;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
