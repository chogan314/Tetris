package tetris03;

import tetris03.core.Game;
import tetris03.models.TetrisPiece.Direction;
import tetris03.models.TetrisPiece.Rotation;
import tetris03.screens.GameOverScreen;
import tetris03.screens.GameScreen;
import tetris03.screens.TitleScreen;


public class Tetris03 extends Game {
	private static final long serialVersionUID = 7468161665513961017L;
	
	public static final String VERSION = "0.0.0.01 Alpha";
	public static final String LOG = "Tetris";
	
	public static boolean soundOn = true;
	
	@Override
	public void setup() {
		super.setup();
		size(800, 601);
		background(0);
		setScreen(new TitleScreen(this));
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	@Override
	public void present() {
		super.present();
	}
	
	@Override
	public void keyPressed() {
		if(getScreen() instanceof GameScreen) {
			GameScreen screen = (GameScreen) getScreen();
			
			switch(key) {
				case('a'):case('A'):
					screen.activePiece.movePiece(Direction.LEFT);
					break;
				case('s'):case('S'):
					screen.activePiece.movePiece(Direction.DOWN);
					break;
				case('d'):case('D'):
					screen.activePiece.movePiece(Direction.RIGHT);
					break;
				case('q'):case('Q'):
					screen.activePiece.rotatePiece(Rotation.CLOCKWISE);
					break;
				case('e'):case('E'):
					screen.activePiece.rotatePiece(Rotation.COUNTERCLOCKWISE);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public void mouseClicked() {
		if(getScreen() instanceof GameScreen) {
			GameScreen screen = (GameScreen) getScreen();
			
			if(mouseX > width - 84 && mouseY > height - 84) {
				soundOn = !soundOn;
				if(soundOn) {
					screen.music.unmute();
				} else {
					screen.music.mute();
				}
			}
		} else if(getScreen() instanceof TitleScreen) {			
			if(mouseX > width - 84 && mouseY > height - 84) {
				soundOn = !soundOn;
			} else if(mouseX > width / 2 - 128 && mouseX < width / 2 + 128 &&
					mouseY < height / 2 + 128 && mouseY > height / 2) {
				setScreen(new GameScreen(this));
			}
		} else if(getScreen() instanceof GameOverScreen) {
			if(mouseX > width / 2 - 128 && mouseX < width / 2 + 128 &&
					mouseY < height / 2 + 64 && mouseY > height / 2 - 64) {
				setScreen(new GameScreen(this));
			}
		}
	}
}
