package de.augsburg.hs.methoden.ki.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    private Sprite sprite;
    private Vector2 position;

    public GameObject(Sprite sprite) {
        this(sprite, new Vector2(0,0));
    }

    public GameObject(Sprite sprite, Vector2 position) {
        this.sprite = sprite;
        this.position = position;
    }

    protected abstract void create();

    public void step(float delta, SpriteBatch batch) {
        this.update(delta);
        this.render(batch);
    }

    protected abstract void update(float delta);

    protected void render(SpriteBatch batch){
        sprite.draw(batch);
    }

}
