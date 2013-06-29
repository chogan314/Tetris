package tetris03.core;

import processing.core.PApplet;

public abstract class Game extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private Screen screen;

	private long startTime;
	
	@Override
	public void setup() {
		startTime = System.nanoTime();
	}
	
	@Override
	public void draw() {
		float delta = (System.nanoTime() - startTime) / 1000000000.0f;
		startTime = System.nanoTime();
				
		update(delta);
		present();
	}
	
	public void update(float delta) {
		if(screen != null) {
			screen.update(delta);
		}
	}
	
	public void present() {
		if(screen != null) {
			screen.present();
		}
	}
	
	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
	public Screen getScreen() {
		return screen;
	}
}
