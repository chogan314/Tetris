package tetris03.core;

public abstract class Screen {
	protected Game game;
	
	public Screen(Game game) {
		this.game = game;
	}
	
	public abstract void update(float delta);
	public abstract void present();
}
