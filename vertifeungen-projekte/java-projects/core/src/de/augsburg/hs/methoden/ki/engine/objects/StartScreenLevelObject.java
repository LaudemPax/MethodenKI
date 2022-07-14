package de.augsburg.hs.methoden.ki.engine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.engine.GameObject;

public class StartScreenLevelObject extends GameObject {

    private String levelName;

    // fill color
    private Color color;
    private final float color_R = 248.0f / 255.0f;
    private final float color_G = 132.0f / 255.0f;
    private final float color_B = 4.0f / 255.0f;

    public static final int WIDTH = 150;
    public static final int HEIGHT = 80;
    private ShapeRenderer shapeRenderer;
    private boolean setProjectionFlag = false;
    private BitmapFont font;

    private Vector2 mousePos;


    public StartScreenLevelObject(String levelName){
        super(new Sprite());
        this.levelName = levelName;
    }

    @Override
    protected void create() {
        this.color = new Color(color_R, color_G, color_B, 1);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.mousePos = new Vector2(0,0);
    }

    @Override
    public void update(float delta) {

        // mouse hover effect
        boolean mouseInXBounds = (mousePos.x > position.x  && (mousePos.x < position.x + WIDTH));
        boolean mouseInYBounds = (mousePos.y > position.y && (mousePos.y < position.y + HEIGHT));

        if(mouseInXBounds && mouseInYBounds) {
            color.r = 1;
            color.g = 0;
            color.b = 0;
        } else {
            color.r = color_R;
            color.g = color_G;
            color.b = color_B;
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.end();

        if(!setProjectionFlag) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            setProjectionFlag = true;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
        shapeRenderer.end();

        batch.begin();

        font.draw(batch, levelName, position.x + 10, position.y + HEIGHT/2 + 10);
    }

    public void updateMousePosition(float x, float y) {
        mousePos.x = x;
        mousePos.y = y;
    }

    public void onMouseClick() {

    }
}
