package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.GameScreen;
import de.augsburg.hs.methoden.ki.engine.objects.TitleScreenLevelObject;

import java.util.ArrayList;

public class StartScreen extends GameScreen {

    private final int MARGIN = 20;

    private ArrayList<TitleScreenLevelObject> levelObjects;

    public StartScreen(MainGame game) {
        super(game);
    }

    @Override
    public void create() {
        levelObjects = new ArrayList<>();
        levelObjects.add(new TitleScreenLevelObject("test"));

        int screenWidth = Gdx.graphics.getWidth();

        int widthOfObjects = 2 * MARGIN + TitleScreenLevelObject.WIDTH;
        int objectsPerRow = screenWidth / widthOfObjects;
        if(objectsPerRow < 1) objectsPerRow = 1;

        // calculate layout of title screen objects
        for(TitleScreenLevelObject object : levelObjects) {
            int totalWidthOfRow = 0;
            int lastYPosition = 0;

            // for each row
            for(int i = 0; i < objectsPerRow; i++){
                int xPosition = totalWidthOfRow + MARGIN;
                int yPosition = lastYPosition + MARGIN;

                object.setPosition(xPosition, yPosition);

                totalWidthOfRow += MARGIN + TitleScreenLevelObject.WIDTH;
            }

            totalWidthOfRow = 0;
            lastYPosition += MARGIN;
        }
    }

    @Override
    public void update(float delta) {

    }

    @Override
    protected void draw(SpriteBatch batch) {
        for(TitleScreenLevelObject object : levelObjects) {
            object.render(batch);
        }
    }
}
