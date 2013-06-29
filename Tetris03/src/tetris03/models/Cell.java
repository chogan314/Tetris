package tetris03.models;

import processing.core.PVector;

public class Cell {
	public PVector location;
	public boolean isOccupied = false;
	public TetrisPart part = null;
	
	public Cell(float x, float y) {
		this.location = new PVector(x, y);
	}
	
	public void setPart(TetrisPart part) {
		this.part = part;
		
		if(part == null) {
			isOccupied = false;
		} else {
			isOccupied = true;
		}
	}
}
