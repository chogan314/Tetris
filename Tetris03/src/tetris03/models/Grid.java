package tetris03.models;

import java.util.ArrayList;

import processing.core.PVector;

public class Grid {
	public static final int TETRIS_GRID_WIDTH = 10;
	public static final int TETRIS_GRID_HEIGHT = 22;
	
	public int width, height;
	
	public ArrayList<Cell> cells;
	public ArrayList<TetrisPart> parts;
	
	public Grid() {
		cells = new ArrayList<Cell>();
		parts = new ArrayList<TetrisPart>();
		width = TETRIS_GRID_WIDTH;
		height = TETRIS_GRID_HEIGHT;
		buildGrid(width, height);
	}
	
	public Grid(int width, int height) {
		cells = new ArrayList<Cell>();
		parts = new ArrayList<TetrisPart>();
		this.width = width;
		this.height = height;
		buildGrid(width, height);
	}
	
	private void buildGrid(int width, int height) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				cells.add(new Cell(x, y));
			}
		}
	}
	
	public void placePart(TetrisPart part) {
		findCellByLocation(part.location).setPart(part);
		parts.add(part);
	}
	
	public void removePart(TetrisPart part) {
		findCellByLocation(part.location).setPart(null);
		parts.remove(part);
	}
	
	//finds cells in grid
	public Cell findCellByLocation(PVector location) {
		int index = width * (int)location.y + (int)location.x;
		return cells.get(index);
	}
	public Cell findCellByLocation(float x, float y) {
		int index = width * (int)y + (int)x;
		return cells.get(index);
	}
	
	
	//checks to see if grid contains location
	public boolean contains(PVector location) {
		if(location.x < 0 || location.y < 0) {
			return false;
		}
		if(location.x >= width || location.y >= height) {
			return false;
		}
		
		return true;
	}	
	public boolean contains(float x, float y) {
		if(x < 0 || y < 0) {
			return false;
		}
		if(x >= width || y >= height) {
			return false;
		}
		
		return true;
	}
}
