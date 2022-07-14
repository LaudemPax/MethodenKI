package de.augsburg.hs.methoden.ki.engine.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.GameObject;

public class TitleScreenLevelObject extends GameObject {

    private String levelName;
    private Color color;
    public static final int WIDTH = 100;
    public static final int HEIGHT = 80;
    private ShapeRenderer shapeRenderer;
    private boolean setProjectionFlag = false;


    public TitleScreenLevelObject(String levelName){
        super(new Sprite());
        this.levelName = levelName;
    }

    @Override
    protected void create() {
        float red = MathUtils.random(1);
        float blue = MathUtils.random(1);
        float green = MathUtils.random(1);
        color = new Color(red, blue, green, 1f);

        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        if(!setProjectionFlag) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            setProjectionFlag = true;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
        shapeRenderer.end();
    }
}
