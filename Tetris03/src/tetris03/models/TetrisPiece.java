package tetris03.models;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PVector;

public class TetrisPiece {
	public static final int TYPE_I = 0;
	public static final int TYPE_J = 1;
	public static final int TYPE_L = 2;
	public static final int TYPE_O = 3;
	public static final int TYPE_S = 4;
	public static final int TYPE_T = 5;
	public static final int TYPE_Z = 6;
	
	public int pieceType;
	public ArrayList<TetrisPart> parts;
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	public enum Rotation {
		CLOCKWISE, COUNTERCLOCKWISE
	}
	
	public TetrisPart origin;
	public Grid grid = null;
	public boolean active = true;
	public boolean stuck = false;
	ArrayList<PVector> tempLocations = new ArrayList<PVector>();
	
	float accum = 0;
	
	public TetrisPiece() {
		parts = new ArrayList<TetrisPart>();
		Random random = new Random();
		pieceType = random.nextInt(7);
		buildPiece();
	}
	
	public TetrisPiece(int pieceType) {
		parts = new ArrayList<TetrisPart>();
		this.pieceType = pieceType;
		buildPiece();
	}
	
	private void buildPiece() {
		switch(pieceType) {
			case TYPE_I:
				buildI();
				break;
			case TYPE_J:
				buildJ();
				break;
			case TYPE_L:
				buildL();
				break;
			case TYPE_O:
				buildO();
				break;
			case TYPE_S:
				buildS();
				break;
			case TYPE_T:
				buildT();
				break;
			case TYPE_Z:
				buildZ();
				break;
		}
	}
	
	private void buildI() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 2, 0));
		parts.add(new TetrisPart(this, 3, 0));
		
		origin = parts.get(1);
	}
	
	private void buildJ() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 2, 0));
		parts.add(new TetrisPart(this, 2, 1));
		
		origin = parts.get(1);
	}
	
	private void buildL() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 2, 0));
		parts.add(new TetrisPart(this, 0, 1));
		
		origin = parts.get(1);
	}
	
	private void buildO() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 0, 1));
		parts.add(new TetrisPart(this, 1, 1));
	}
	
	private void buildS() {
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 2, 0));
		parts.add(new TetrisPart(this, 0, 1));
		parts.add(new TetrisPart(this, 1, 1));
		
		origin = parts.get(3);
	}
	
	private void buildT() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 2, 0));
		parts.add(new TetrisPart(this, 1, 1));
		
		origin = parts.get(1);
	}
	
	private void buildZ() {
		parts.add(new TetrisPart(this, 0, 0));
		parts.add(new TetrisPart(this, 1, 0));
		parts.add(new TetrisPart(this, 1, 1));
		parts.add(new TetrisPart(this, 2, 1));
		
		origin = parts.get(2);
	}
	
	//used to place piece on grid
	public void place(Grid grid, float x, float y) {
		if(this.grid == null) {
			this.grid = grid;
			if(canPlaceAt(grid, x, y)) {
				for(TetrisPart part : parts) {
					part.place(grid, x + part.locationInPiece.x, y + part.locationInPiece.y);
				}
			} else {
				System.out.println("CANNOT PLACE PIECE -- INVALID LOCATION");
			}
		} else {
			System.out.println("CANNOT PLACE PIECE -- PIECE ALREADY ON A GRID");
		}
	}
	
	public void place(Grid grid, PVector location) {
		if(this.grid == null) {
			this.grid = grid;
			if(canPlaceAt(grid, location)) {
				for(TetrisPart part : parts) {
					part.place(grid, PVector.add(location, part.locationInPiece));
				}
			} else {
				System.out.println("CANNOT PLACE PIECE -- INVALID LOCATION");
			}
		} else {
			System.out.println("CANNOT PLACE PIECE -- PIECE ALREADY ON A GRID");
		}
	}
	
	//used to remove piece from grid
	public void remove() {
		if(grid != null) {
			grid = null;
			for(TetrisPart part : parts) {
				part.remove();
			}
		} else {
			System.out.println("CANNOT REMOVE PIECE -- PIECE NOT ON A GRID");
		}
	}
	
	//used to lift piece off grid while retaining reference
	public void lift() {
		if(grid != null) {
			for(TetrisPart part : parts) {
				part.lift();
			}
		} else {
			System.out.println("CANNOT LIFT PIECE -- PIECE NOT ON A GRID");
		}
	}
	
	//used to drop piece on grid
	public void drop(PVector location) {
		if(grid != null) {
			for(TetrisPart part : parts) {
				part.drop(location);
			}
		} else {
			System.out.println("CANNOT DROP PIECE -- PIECE NOT ON A GRID");
		}
	}
	
	public void drop(float x, float y) {
		if(grid != null) {
			for(TetrisPart part : parts) {
				part.drop(x, y);
			}
		} else {
			System.out.println("CANNOT DROP PIECE -- PIECE NOT ON A GRID");
		}
	}
	
	public void moveTo(PVector location) {
		if(canMoveTo(location)) {
			lift();
			drop(location.x, location.y);
		}
	}
	
	public void moveTo(float x, float y) {
		if(canMoveTo(x, y)) {
			lift();
			drop(x, y);
		}
	}
	
	public void moveBy(PVector location) {
		if(canMoveBy(location)) {
			lift();
			for(TetrisPart part : parts) {
				part.drop(part.location.x + location.x, part.location.y + location.y);
			}
		}
	}
	
	public void moveBy(float x, float y) {
		if(canMoveBy(x, y)) {
			lift();
			for(TetrisPart part : parts) {
				part.drop(part.location.x + x, part.location.y + y);
			}
		}
	}
	
	public void movePiece(Direction direction) {
		switch(direction) {
			case UP:
				if(canMoveBy(0, -1)) {
					moveBy(0, -1);
				}
				break;
			case DOWN:
				if(canMoveBy(0, 1)) {
					moveBy(0, 1);
				} else {
					tryStickPiece();
				}
				break;
			case LEFT:
				if(canMoveBy(-1, 0)) {
					moveBy(-1, 0);
				}
				break;
			case RIGHT:
				if(canMoveBy(1, 0)) {
					moveBy(1, 0);
				}
				break;
			default:
				break;
		}
	}
	
	public void tryStickPiece() {
		if(!canMoveBy(0, 1)) {
			stuck = true;
			for(TetrisPart part : parts) {
				part.stuck = true;
			}
			lockPiece();
		}
	}
	
	public void unlockPiece() {
		active = true;
		for(TetrisPart part : parts) {
			part.active = true;
		}
	}
	
	public void lockPiece() {
		active = false;
		for(TetrisPart part : parts) {
			part.active = false;
		}
	}
	
	public void rotatePiece(Rotation rotation) {
		if(pieceType == TetrisPiece.TYPE_O) {
			return;
		}
		
		float x, y, temp;
		boolean canMove = true;
		tempLocations.clear();
		
		switch(rotation) {
			case COUNTERCLOCKWISE:
				for(TetrisPart part : parts) {
					x = part.location.x;
					y = part.location.y;
					
					x -= origin.location.x;
					y -= origin.location.y;
					
					y = -y;
					
					temp = x;
					x = -y;
					y = temp;
					
					y = -y;
					
					x += origin.location.x;
					y += origin.location.y;
					
					tempLocations.add(new PVector(x, y));
					
					if(!part.canMoveTo(x, y)) {
						canMove = false;
					}
				}
				break;
			case CLOCKWISE:
				for(TetrisPart part : parts) {
					x = part.location.x;
					y = part.location.y;
					
					x -= origin.location.x;
					y -= origin.location.y;
					
					y = -y;
					
					temp = -x;
					x = y;
					y = temp;
					
					y = -y;
					
					x += origin.location.x;
					y += origin.location.y;
					
					tempLocations.add(new PVector(x, y));
					
					if(!part.canMoveTo(x, y)) {
						canMove = false;
					}
				}
				break;
			default:
				break;			
		}
		
		if(canMove) {
			for(int i = 0; i < 4; i++) {
				parts.get(i).remove();
				parts.get(i).place(grid, tempLocations.get(i).x, tempLocations.get(i).y);
			}
		}
	}
	
	//********************************************
	//**checks made before placement at location**
	//********************************************
	public boolean canPlaceAt(Grid grid, PVector location) {
		for(TetrisPart part : parts) {
			if(!part.canPlaceAt(grid, location)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canPlaceAt(Grid grid, float x, float y) {
		for(TetrisPart part : parts) {
			if(!part.canPlaceAt(grid, x, y)) {
				return false;
			}
		}
		return true;
	}
	
	//********************************************
	//***checks made before moving to location****
	//********************************************
	public boolean canMoveTo(PVector location) {
		for(TetrisPart part : parts) {
			if(!part.canMoveTo(location)) {
				return false;
			}
		}
		return true;
	}	
	public boolean canMoveTo(float x, float y) {
		for(TetrisPart part : parts) {
			if(!part.canMoveTo(x, y)) {
				return false;
			}
		}
		return true;
	}	
	
	//********************************************
	//****checks made before moving by vector*****
	//********************************************
	public boolean canMoveBy(PVector location) {
		if(grid == null) {
			return false;
		}
		
		for(TetrisPart part : parts) {
			if(!part.canMoveBy(location)) {
				return false;
			}
		}
		
		return true;
	}	
	public boolean canMoveBy(float x, float y) {
		if(grid == null) {
			return false;
		}
		
		for(TetrisPart part : parts) {
			if(!part.canMoveBy(x, y)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void update(float delta, float fallSpeed) {
		accum += delta;
		while(accum > fallSpeed) {
			movePiece(Direction.DOWN);
			accum -= fallSpeed;
		}
	}
}











