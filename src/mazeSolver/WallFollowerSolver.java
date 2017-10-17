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
	private Integer directions[];
	private boolean solved = false;
	
	private Stack<HashMap<String, Object>> stackIntegerersection = new Stack<HashMap<String, Object>>();
	
	Integer nVisited = 0;
	Integer currentDirection = 100; //Start
	
	@Override
	public void solveMaze(Maze maze) {
		
		Integer sizeC = maze.type == Maze.NORMAL? maze.sizeC : maze.sizeC + ((maze.sizeR + 1) / 2);
		
		boolean[][] marked = null;
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL) {
			marked = new boolean[maze.sizeR][maze.sizeC];
			this.initMarked(marked, maze.sizeR, maze.sizeC);
			this.directions = new Integer[] {Maze.SOUTH, Maze.EAST, Maze.WEST, Maze.NORTH};
		} 
		else {
			marked = new boolean[maze.sizeR][sizeC];
			this.initMarked(marked, maze.sizeR, sizeC);
			this.directions = new Integer[] {Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.EAST, Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST};
		}				
		
		Cell entrance = maze.entrance;
		Cell exit = maze.exit;
		
		this.nVisited = 1;
		marked[entrance.r][entrance.c] = true;		
		
		maze.drawFtPrt(entrance);
		this.performWallFollower(entrance, exit, maze, marked, false, true);
	} // end of solveMaze()    
    
    private void performWallFollower(Cell aEntrance, Cell aExit, Maze aMaze, boolean[][] aMarked, boolean aTurn90Degree, boolean aStart) {
    	while (this.solved != true) {   
        	boolean canWalk = false;
        	
			if (aTurn90Degree) {
				Integer direction = this.currentDirection;
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
				else if (direction == Maze.NORTHEAST) {
					direction = Maze.SOUTHWEST;
				}
				else if (direction == Maze.SOUTHWEST) {
					direction = Maze.NORTHEAST;
				}
				else if (direction == Maze.NORTHWEST) {
					direction = Maze.SOUTHEAST;
				}
				else if (direction == Maze.SOUTHEAST) {
					direction = Maze.NORTHWEST;
				}
				
				this.markDirectionFrom(aMaze, direction);
				this.performWallFollower(aEntrance, aExit, aMaze, aMarked, false, false);
				break;
				
			}
			else {	//keep walking to directions
				
				ArrayList<Wall> walls = new ArrayList<Wall>();		
	    		for (Wall wall : aEntrance.wall) { //0, 2
	    			if (wall != null && wall.present == false) {
	    				walls.add(wall);
	    			}    			
	    		}
	    		
				for (Integer i = 0; i < this.directions.length; i++) {				
					Integer direction = this.directions[i];
					if (aEntrance.wall[direction].present == false && (!aMarked[aEntrance.neigh[direction].r][aEntrance.neigh[direction].c] || aEntrance == aMaze.entrance)) {
						
						if (walls.size() > 2 || aStart) {
							walls.remove(aEntrance.wall[direction]);
							
							//Marking Integerersection
							HashMap<String, Object> markingIntegerersection = new HashMap<String, Object>();			    			
							markingIntegerersection.put("cell", aEntrance);
							markingIntegerersection.put("walls", walls);
							this.stackIntegerersection.push(markingIntegerersection);
							
						}
						Cell goToCell = aEntrance.neigh[direction];
						this.markDirectionFrom(aMaze, direction);
						this.currentDirection = direction;
						aMarked[goToCell.r][goToCell.c] = true;
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
				HashMap<String, Object> markingIntegerersection = (HashMap<String, Object>) this.stackIntegerersection.pop();
				aEntrance = (Cell) markingIntegerersection.get("cell");
				this.performWallFollower(aEntrance, aExit, aMaze, aMarked, true, false);
				break;
			}
		}
    }
    
    private void markDirectionFrom(Maze aMaze, Integer aDirection) {
    	if (aMaze.type == Maze.NORMAL) {
    		if (aDirection == Maze.EAST) { //
    			this.directions = new Integer[]{Maze.SOUTH, Maze.EAST, Maze.WEST, Maze.NORTH};
    		}
    		else if (aDirection == Maze.SOUTH) {
    			this.directions = new Integer[]{Maze.WEST, Maze.SOUTH, Maze.NORTH, Maze.EAST};
    		}
    		else if (aDirection == Maze.WEST) {
    			this.directions = new Integer[]{Maze.NORTH, Maze.WEST, Maze.EAST, Maze.SOUTH};
    		}
    		else if (aDirection == Maze.NORTH) {
    			this.directions = new Integer[]{Maze.EAST, Maze.NORTH, Maze.SOUTH, Maze.WEST};
    		}
    	}
    	else if (aMaze.type == Maze.HEX) {
    		if (aDirection == Maze.EAST) {
    			this.directions = new Integer[]{Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.EAST, Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST};
    		}
    		else if (aDirection == Maze.SOUTHEAST) {
    			this.directions = new Integer[]{Maze.SOUTHWEST, Maze.WEST, Maze.SOUTHEAST, Maze.NORTHWEST, Maze.NORTHEAST, Maze.EAST};
    		}
    		else if (aDirection == Maze.SOUTHWEST) {
    			this.directions = new Integer[]{Maze.WEST, Maze.NORTHWEST, Maze.SOUTHWEST, Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST};
    		}
    		else if (aDirection == Maze.WEST) {
    			this.directions = new Integer[]{Maze.NORTHWEST, Maze.NORTHEAST, Maze.WEST, Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST};
    		}
    		else if (aDirection == Maze.NORTHWEST) {
    			this.directions = new Integer[]{Maze.NORTHEAST, Maze.EAST, Maze.NORTHWEST, Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST};
    		}
    		else if (aDirection == Maze.NORTHEAST) {
    			this.directions = new Integer[]{Maze.EAST, Maze.SOUTHEAST, Maze.NORTHEAST, Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST};
    		}
    	}				
	}
	
    private void initMarked(boolean[][] aMarked, Integer aSizeR, Integer aSizeC) {
		for (Integer i = 0; i < aSizeR; i++) {
			for (Integer j = 0; j < aSizeC; j++) {
				aMarked[i][j] = false;
			}
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
