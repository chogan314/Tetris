package tetris03.screens;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import tetris03.Tetris03;
import tetris03.core.Game;
import tetris03.core.Screen;
import tetris03.models.Cell;
import tetris03.models.Grid;
import tetris03.models.Particle;
import tetris03.models.TetrisPart;
import tetris03.models.TetrisPiece;
import tetris03.models.TetrisPiece.Direction;
import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class GameScreen extends Screen {
	public static final int PIXELS_PER_METER = 30;
	public static final PVector GRID_SHIFT = new PVector();
	public static final int STARTING_SCORE = 6000;
	
	Grid grid, previewGrid;
	public TetrisPiece activePiece, nextPiece;
	float fallSpeed = 1.0f;
	
	float accum = 0;
	boolean rowsFalling = false;
	boolean[] rowsFull;
	
	int score = 0;
	int toNextLevel = STARTING_SCORE;
	int currentLevelExp = 0;
	int level = 0;
	int linesCleared;
	
	ArrayList<Particle> particles;
	
	Minim minim = new Minim(game);
	public AudioPlayer music = minim.loadFile("tetris.mp3");
	AudioSample stickSound = minim.loadSample("stick.wav");
	AudioSample clearSound = minim.loadSample("clear.wav");
	
	PImage speakerOn;
	PImage speakerOff;
	
	PFont font;

	public GameScreen(Game game) {
		super(game);
		GRID_SHIFT.set(game.width / 2 - 5 * PIXELS_PER_METER,  -2 * PIXELS_PER_METER);
		
		grid = new Grid();
		previewGrid = new Grid(6, 4);
		
		particles = new ArrayList<Particle>();
		
		nextPiece = new TetrisPiece();
		nextPiece.place(previewGrid, 1, 1);
		makeNewPiece();
		
		music.setGain(-15);
		music.loop();
		if(!Tetris03.soundOn) {
			music.mute();
		}
		stickSound.setGain(-5);
		clearSound.setGain(-5);
		
		speakerOn = game.loadImage("speakerOn.png");
		speakerOff = game.loadImage("speakerOff.png");
		
		font = game.createFont("Arial", 24, true);
	}
	
	private void makeNewPiece() {
		activePiece = nextPiece;
		activePiece.remove();
		activePiece.place(grid, 3, (activePiece.pieceType == TetrisPiece.TYPE_I) ? 1 : 0);
		nextPiece = new TetrisPiece();
		nextPiece.place(previewGrid, 1, 1);
	}

	@Override
	public void update(float delta) {
		if(!rowsFalling) {
			if(!activePiece.stuck) {
				accum += delta;
				while(accum >= fallSpeed) {
					activePiece.movePiece(Direction.DOWN);
					accum -= fallSpeed;
				}
			} else {
				checkGameOver();
				accum = 0;
				if(checkForFullRows()) {
					if(Tetris03.soundOn) {
						clearSound.trigger();
					}
				} else {
					if(Tetris03.soundOn) {
						stickSound.trigger();
					}
				}
				makeNewPiece();
			}
		} else {
			explodeParts();
			fall(delta);
		}
		
		while(score >= toNextLevel) {
			level += 1;
			if(fallSpeed > 0.3) {
				fallSpeed -= 0.2;
			}
			currentLevelExp = toNextLevel;
			toNextLevel = STARTING_SCORE + STARTING_SCORE * (int)Math.pow(level, 2);
			System.out.println(toNextLevel);
		}
		
		for(Iterator<Particle> iter = particles.iterator(); iter.hasNext();) {
			Particle particle = iter.next();
			particle.update(delta);
			if(particle.timeAlive >= Particle.PARTICLE_LIFETIME || particle.position.x < 100) {
				score += 10 + 5 * (level);
				iter.remove();
			}
		}
	}

	@Override
	public void present() {
		game.background(60);
		drawProgressBar();
		drawGrid();
		drawPreviewGrid();
		drawParticles();
		drawIcons();
		drawText();
	}
	
	private void drawGrid() {
		game.stroke(80);
		game.strokeWeight(1);
		game.fill(255);
		
		game.pushMatrix();
		game.translate(GRID_SHIFT.x,  GRID_SHIFT.y);
		
		for(Cell cell : grid.cells) {
			game.rect(cell.location.x * PIXELS_PER_METER, cell.location.y * PIXELS_PER_METER, 
					PIXELS_PER_METER, PIXELS_PER_METER);
		}
		
		for(TetrisPart part : grid.parts) {
			setFillColor(part);
			game.rect(part.location.x * PIXELS_PER_METER, part.location.y * PIXELS_PER_METER, 
					PIXELS_PER_METER, PIXELS_PER_METER);
		}
		
		game.popMatrix();
	}
	
	private void drawPreviewGrid() {
		game.stroke(80);
		game.fill(255);
		game.pushMatrix();
		game.translate(20 * PIXELS_PER_METER, 8 * PIXELS_PER_METER);
		
		for(Cell cell : previewGrid.cells) {
			game.rect(cell.location.x * PIXELS_PER_METER, cell.location.y * PIXELS_PER_METER, 
					PIXELS_PER_METER, PIXELS_PER_METER);
		}
		
		for(TetrisPart part : previewGrid.parts) {
			setFillColor(part);
			game.rect(part.location.x * PIXELS_PER_METER, part.location.y * PIXELS_PER_METER, 
					PIXELS_PER_METER, PIXELS_PER_METER);
		}
		
		game.popMatrix();
	}
	
	private void drawParticles() {
		game.noStroke();
		
		for(Particle particle : particles) {
			setFillColor(particle);
			game.ellipse(particle.position.x, particle.position.y, particle.width, particle.height);
		}
	}
	
	private void drawIcons() {
		if(Tetris03.soundOn) {
			game.image(speakerOn, game.width - 20 - 64, game.height - 20 - 64);
		} else {
			game.image(speakerOff, game.width - 20 - 64, game.height - 20 - 64);
		}
	}
	
	private void drawProgressBar() {
		game.stroke(80);
		game.fill(255);
		game.rect(20, 20, 100, game.height - 40);
		game.fill(100, 100, 255);
		game.rect(20, game.height - 20, 100, -(game.height - 40) * 
				(score - currentLevelExp) / (toNextLevel - currentLevelExp));
	}
	
	private void drawText() {
		game.fill(255);
		game.textFont(font);
		game.textAlign(Tetris03.RIGHT, Tetris03.TOP);
		game.text("Score: " + score, game.width - 20, 20);
		game.text("Level: " + level, game.width - 20, 60);
		game.text("A/S/D - move piece", game.width - 20, game.height - 180);
		game.text("Q/E - rotate piece", game.width - 20, game.height - 140);
		game.text("Next Piece:", game.width - 40, 200);
	}
	
	private void setFillColor(TetrisPart part) {
		switch(part.pieceType) {
			case TetrisPiece.TYPE_I:
				game.fill(0, 255, 255);
				break;
			case TetrisPiece.TYPE_J:
				game.fill(138, 43, 226);
				break;
			case TetrisPiece.TYPE_L:
				game.fill(255, 165, 0);
				break;
			case TetrisPiece.TYPE_O:
				game.fill(255, 215, 0);
				break;
			case TetrisPiece.TYPE_S:
				game.fill(0, 255, 0);
				break;
			case TetrisPiece.TYPE_T:
				game.fill(128, 0, 128);
				break;
			case TetrisPiece.TYPE_Z:
				game.fill(255, 0, 0);
				break;
			default:
				break;
		}
	}
	
	private void setFillColor(Particle particle) {
		switch(particle.particleType) {
			case TetrisPiece.TYPE_I:
				game.fill(0, 255, 255, 180);
				break;
			case TetrisPiece.TYPE_J:
				game.fill(138, 43, 226, 180);
				break;
			case TetrisPiece.TYPE_L:
				game.fill(255, 165, 0, 180);
				break;
			case TetrisPiece.TYPE_O:
				game.fill(255, 215, 0, 180);
				break;
			case TetrisPiece.TYPE_S:
				game.fill(0, 255, 0, 180);
				break;
			case TetrisPiece.TYPE_T:
				game.fill(128, 0, 128, 180);
				break;
			case TetrisPiece.TYPE_Z:
				game.fill(255, 0, 0, 180);
				break;
			default:
				break;
		}
	}
	
	private boolean checkForFullRows() {
		rowsFull = new boolean[grid.height];
		for(int i = 0; i < rowsFull.length; i++) {
			rowsFull[i] = true;
		}
		boolean removed = false;
		
		for(int y = 0; y < grid.height; y++) {
			for(int x = 0; x < grid.width; x++) {
				if(!grid.findCellByLocation(x, y).isOccupied) {
					rowsFull[y] = false;
				}
			}
		}

		linesCleared = 0;
		for(int y = 0; y < grid.height; y++) {
			if(rowsFull[y]) {
				linesCleared++;
				removed = true;
			}
		}
		
		rowsFalling = removed;
		
		return removed;
	}
	
	private void fall(float delta) {
		for(int i = 0; i < rowsFull.length; i++) {
			if(rowsFull[i]) {
				for(int y = i - 1; y >= 0; y--) {
					for(int x = 0; x < grid.width; x++) {
						if(grid.findCellByLocation(x, y).isOccupied) {
							TetrisPart part = grid.findCellByLocation(x, y).part;
							part.lift();
							part.drop(x, y + 1);
						}
					}
				}
			}
		}
		rowsFalling = false;
	}
	
	private void explodeParts() {
		int particlesPerExplosion = 4 * linesCleared;
		for(int y = 0; y < rowsFull.length; y++) {
			if(rowsFull[y]) {
				for(int x = 0; x < grid.width; x++) {
					for(int i = 0; i < particlesPerExplosion; i++) {
						particles.add(new Particle(grid.findCellByLocation(x, y).part.pieceType, 
								x * PIXELS_PER_METER + GRID_SHIFT.x + PIXELS_PER_METER / 2, 
								y * PIXELS_PER_METER + GRID_SHIFT.y + PIXELS_PER_METER / 2, 
								0, game.height / 2));
					}
					grid.findCellByLocation(x, y).part.remove();
				}
			}
		}
	}
	
	private void checkGameOver() {
		boolean gameOver = false;
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 10; x++) {
				if(grid.findCellByLocation(x, y).isOccupied) {
					gameOver = true;
				}
			}
		}
		
		if(gameOver) {
			music.pause();
			game.setScreen(new GameOverScreen(game, score));
		}
	}
}
