package tetris03.screens;

import processing.core.PFont;
import processing.core.PImage;
import tetris03.Tetris03;
import tetris03.core.Game;
import tetris03.core.Screen;

public class TitleScreen extends Screen {
	
	PImage speakerOn, speakerOff;
	PImage logo;
	PImage button;
	PFont font;

	public TitleScreen(Game game) {
		super(game);
		
		speakerOn = game.loadImage("speakerOn.png");
		speakerOff = game.loadImage("speakerOff.png");
		logo = game.loadImage("tetris.png");
		button = game.loadImage("play.png");
		
		font = game.createFont("Arial", 22);
		game.textFont(font);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void present() {
		game.background(100, 100, 255);
		drawIcons();
	}
	
	private void drawIcons() {
		if(Tetris03.soundOn) {
			game.image(speakerOn, game.width - 20 - 64, game.height - 20 - 64);
		} else {
			game.image(speakerOff, game.width - 20 - 64, game.height - 20 - 64);
		}
		
		game.image(logo, game.width / 2 - logo.width / 2, 40);
		
		game.image(button, game.width / 2 - button.width / 2, game.height /2);
	}
}
