package de.augsburg.hs.methoden.ki.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.augsburg.hs.methoden.ki.MainGame;

public abstract class GameScreen extends ScreenAdapter {

    protected MainGame game;
    private SpriteBatch batch;

    public GameScreen(MainGame game) {
        this.game = game;
        this.batch = game.getSpriteBatch();

        this.create();
    }

    @Override
    public void render(float delta) {
        this.update(delta);
        this.preDraw(batch);
        this.draw(batch);
        this.postDraw(batch);
    }

    public abstract void create();

    public abstract void update(float delta);

    protected void preDraw(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        batch.begin();
    }

    protected abstract void draw(SpriteBatch batch);

    protected void postDraw(SpriteBatch batch) {
        batch.end();
    }

}
