package de.augsburg.hs.methoden.ki.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;
import de.augsburg.hs.methoden.ki.engine.AbstractActor;

public class StartScreenLevelActor extends AbstractActor {

    private String levelName;

    // fill color
    private Color color;
    private final float color_R = 248.0f / 255.0f;
    private final float color_G = 132.0f / 255.0f;
    private final float color_B = 4.0f / 255.0f;

    public static final int WIDTH = 172;
    public static final int HEIGHT = 80;
    private ShapeRenderer shapeRenderer;
    private boolean setProjectionFlag = false;
    private BitmapFont font;

    private AbstractScreen targetScreen;

    private boolean onHoverFlag = false;

    public StartScreenLevelActor(String levelName, AbstractScreen targetScreen){
        this.levelName = levelName;
        this.targetScreen = targetScreen;
    }

    @Override
    protected void create() {
        this.color = new Color(color_R, color_G, color_B, 1);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
    }

    @Override
    public void update(float delta) {

        // mouse hover effect
        if(onHoverFlag) {
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
    public void draw(SpriteBatch batch) {

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

    public void setOnHover(boolean hover) {
        this.onHoverFlag = hover;
    }

    public AbstractScreen getTargetScreen() {
        return targetScreen;
    }
}
