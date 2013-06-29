package tetris03.models;

import processing.core.PVector;

public class TetrisPart {
	public PVector locationInPiece;
	public int pieceType;
	
	public Grid grid = null;
	public PVector location;
	
	public boolean active = true;
	public boolean stuck = false;
	
	public TetrisPart(TetrisPiece piece, float x, float y) {
		this.pieceType = piece.pieceType;
		locationInPiece = new PVector(x, y);
		location = new PVector();
	}
	
	//used to place part on grid
	public void place(Grid grid, float x, float y) {
		if(this.grid == null) {
			this.grid = grid;
			location.set(x, y);
			grid.placePart(this);
		} else {
			System.out.println("CANNOT PLACE PART -- PART ALREADY ON A GRID");
		}
	}
	
	public void place(Grid grid, PVector location) {
		if(this.grid == null) {
			this.grid = grid;
			this.location.set(location);
			grid.placePart(this);
		} else {
			System.out.println("CANNOT PLACE PART -- PART ALREADY ON A GRID");
		}
	}
	
	//used to remove part from grid
	public void remove() {
		if(grid != null) {
			grid.removePart(this);
			grid = null;
		} else {
			System.out.println("CANNOT REMOVE PART -- PART NOT ON A GRID");
		}
	}
	
	//used to lift part off grid (part retains reference to grid)
	//useful for moving a collection of parts
	public void lift() {
		if(grid != null) {
			grid.removePart(this);
		} else {
			System.out.println("CANNOT LIFT PART -- PART NOT ON A GRID");
		}
	}
	
	//used to drop part at location after part has been lifted
	public void drop(PVector location) {
		if(grid != null) {
			this.location.set(location);
			grid.placePart(this);
		} else {
			System.out.println("CANNOT DROP PART -- PART NOT ON A GRID");
		}
	}
	
	public void drop(float x, float y) {
		if(grid != null) {
			location.set(x, y);
			grid.placePart(this);
		} else {
			System.out.println("CANNOT DROP PART -- PART NOT ON A GRID");
		}
	}
	
	//********************************************
	//**checks made before placement at location**
	//********************************************
	public boolean canPlaceAt(Grid grid, PVector location) {		
		if(!grid.contains(location)) {
			return false;
		}
		
		if(grid.findCellByLocation(location).isOccupied) {
			if(!grid.findCellByLocation(location).part.active) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean canPlaceAt(Grid grid, float x, float y) {		
		if(!grid.contains(x, y)) {
			return false;
		}
		
		if(grid.findCellByLocation(x, y).isOccupied) {
			if(!grid.findCellByLocation(x, y).part.active) {
				return false;
			}
		}
		
		return true;
	}
	
	//********************************************
	//***checks made before moving to location****
	//********************************************
	public boolean canMoveTo(PVector location) {
		if(grid == null) {
			return false;
		}
		
		if(!grid.contains(location)) {
			return false;
		}
		
		if(grid.findCellByLocation(location).isOccupied) {
			if(!grid.findCellByLocation(location).part.active) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean canMoveTo(float x, float y) {
		if(grid == null) {
			return false;
		}
		
		if(!grid.contains(x, y)) {
			return false;
		}
		
		if(grid.findCellByLocation(x, y).isOccupied) {
			if(!grid.findCellByLocation(x, y).part.active) {
				return false;
			}
		}
		
		return true;
	}
	
	//********************************************
	//****checks made before moving by vector*****
	//********************************************
	public boolean canMoveBy(PVector vector) {
		if(grid == null) {
			return false;
		}
		
		if(!grid.contains(this.location.x + vector.x, this.location.y + vector.y)) {
			return false;
		}
		
		if(grid.findCellByLocation(this.location.x + vector.x, 
				this.location.y + vector.y).isOccupied) {
			if(!grid.findCellByLocation(this.location.x + vector.x, 
					this.location.y + vector.y).part.active) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean canMoveBy(float x, float y) {
		if(grid == null) {
			return false;
		}
		
		if(!grid.contains(location.x + x, location.y + y)) {
			return false;
		}
		
		if(grid.findCellByLocation(location.x + x, location.y + y).isOccupied) {
			if(!grid.findCellByLocation(location.x + x, location.y + y).part.active) {
				return false;
			}
		}
		
		return true;
	}
}
