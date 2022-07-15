package de.augsburg.hs.methoden.ki.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.MainGame;

import java.util.ArrayList;

public abstract class AbstractScreen extends ScreenAdapter {

    protected MainGame game;
    protected SpriteBatch batch;

    private Color clearColor;

    protected OrthographicCamera camera;

    protected ArrayList<AbstractActor> actors;

    public AbstractScreen(MainGame game) {
        this.game = game;
        this.batch = game.getSpriteBatch();
        this.camera = game.getCamera();
        actors = new ArrayList<>();
        clearColor = new Color(0,0,0,1);

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

    public void update(float delta) {
        for(AbstractActor actor : actors) {
            actor.update(delta);
        }
    }

    protected void preDraw(SpriteBatch batch) {
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    protected void draw(SpriteBatch batch) {
        for(AbstractActor actor : actors) {
            actor.draw(batch);
        }
    }

    protected void postDraw(SpriteBatch batch) {
        batch.end();
    }

    public void setClearColor(Color color) {
        clearColor = color;
    }

    public void addActor(AbstractActor actor) {
        actors.add(actor);
    }

    public void removeActor(int i) {
        actors.remove(i);
    }

    public void removeActor(AbstractActor actor) {
        actors.remove(actor);
    }
}
