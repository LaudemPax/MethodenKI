package de.augsburg.hs.methoden.ki.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Actor {

    protected Sprite sprite;
    protected Vector2 position;

    public Actor(Sprite sprite) {
        this(sprite, new Vector2(0,0));
    }

    public Actor(Sprite sprite, Vector2 position) {
        this.sprite = sprite;
        this.position = position;

        create();
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

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

}
