package de.augsburg.hs.methoden.ki;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.augsburg.hs.methoden.ki.screens.StartScreen;

public class MainGame extends Game {
	private SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new StartScreen(this));
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
