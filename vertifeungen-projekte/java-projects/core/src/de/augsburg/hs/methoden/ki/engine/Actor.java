package de.augsburg.hs.methoden.ki.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * An actor is an abstraction for game objects managed by the engine
 */
public class Actor {

    protected Sprite sprite;
    protected Vector2 position;
    protected boolean isDrawCentered = false;

    public Actor() {
        this(new Sprite(), new Vector2(0,0));
    }

    public Actor(Sprite sprite) {
        this(sprite, new Vector2(0,0));
    }

    public Actor(Vector2 position) {
        this(new Sprite(), position);
    }

    public Actor(Sprite sprite, Vector2 position) {
        this.sprite = sprite;
        this.position = position;
    }

    protected void update(float delta) {
        sprite.setPosition(position.x, position.y);
    }

    protected void draw(SpriteBatch batch){
        if(isDrawCentered){
            float adjustedX = sprite.getX() - (sprite.getWidth()/2f);
            float adjustedY = sprite.getY() - (sprite.getHeight()/2f);
            sprite.setPosition(adjustedX, adjustedY);
        }

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

    public void loadSpriteFromAssets(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        setSprite(new Sprite(texture));
    }

    public boolean isDrawCentered() {
        return isDrawCentered;
    }

    public void setDrawCentered(boolean drawCentered) {
        isDrawCentered = drawCentered;
    }
}
