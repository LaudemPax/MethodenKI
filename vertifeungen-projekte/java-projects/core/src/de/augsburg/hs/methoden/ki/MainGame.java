package de.augsburg.hs.methoden.ki;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.augsburg.hs.methoden.ki.screens.start.StartScreen;

public class MainGame extends Game {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private StartScreen startScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		startScreen = new StartScreen(this);
		goToStartScreen();
	}

	public void goToStartScreen() {
		setScreen(startScreen);
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
