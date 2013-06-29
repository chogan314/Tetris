package tetris03.screens;

import processing.core.PFont;
import processing.core.PImage;
import tetris03.Tetris03;
import tetris03.core.Game;
import tetris03.core.Screen;

public class GameOverScreen extends Screen {
	int score;
	
	PImage restart;
	PFont font;

	public GameOverScreen(Game game, int score) {
		super(game);
		this.score = score;
		
		restart = game.loadImage("restart.png");
		font = game.createFont("Arial", 64);
		game.textFont(font);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void present() {
		game.background(60);
		game.image(restart, game.width / 2 - restart.width / 2, game.height / 2 - restart.height / 2);
		game.textAlign(Tetris03.CENTER, Tetris03.CENTER);
		game.text("Score: " + score, game.width / 2, game.height / 4);
	}
}
