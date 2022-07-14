package de.augsburg.hs.methoden.ki;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainGame extends Game {
	private SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
