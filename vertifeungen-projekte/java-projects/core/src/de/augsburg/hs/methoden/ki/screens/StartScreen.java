package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;
import de.augsburg.hs.methoden.ki.engine.objects.StartScreenLevelObject;

import java.util.ArrayList;

public class StartScreen extends AbstractScreen {

    private final int MARGIN = 20;

    private ArrayList<StartScreenLevelObject> levelObjects;

    public StartScreen(MainGame game) {
        super(game);
    }

    @Override
    public void create() {
        levelObjects = new ArrayList<>();
        levelObjects.add(new StartScreenLevelObject("test 1"));
        levelObjects.add(new StartScreenLevelObject("test 2"));
        levelObjects.add(new StartScreenLevelObject("test 3"));
        levelObjects.add(new StartScreenLevelObject("test 4"));
        levelObjects.add(new StartScreenLevelObject("test 5"));
        levelObjects.add(new StartScreenLevelObject("test 6"));
        levelObjects.add(new StartScreenLevelObject("test 7"));
        levelObjects.add(new StartScreenLevelObject("test 8"));
        levelObjects.add(new StartScreenLevelObject("test 9"));

        int screenWidth = Gdx.graphics.getWidth();

        int widthOfObjects = 2 * MARGIN + StartScreenLevelObject.WIDTH;
        int objectsPerRow = screenWidth / widthOfObjects;
        if(objectsPerRow < 1) objectsPerRow = 1;

        // calculate layout of title screen objects
        int totalWidthOfRow = 0;
        int lastYPosition = 0;

        for(int i = 1; i <= levelObjects.size(); i++) {
            StartScreenLevelObject obj = levelObjects.get(i - 1);

            int xPosition = totalWidthOfRow + MARGIN;
            int yPosition = lastYPosition + MARGIN;

            // OpenGL has its cooridnates on the bottom left corner
            int invertedYPosition = Gdx.graphics.getHeight() - yPosition;
            invertedYPosition -= StartScreenLevelObject.HEIGHT;

            obj.setPosition(xPosition, invertedYPosition);

            // each row
            if((i % objectsPerRow) > 0 || i == 1){
                totalWidthOfRow += MARGIN + StartScreenLevelObject.WIDTH;
            } else {
                totalWidthOfRow = 0;
                lastYPosition += MARGIN + StartScreenLevelObject.HEIGHT;
            }
        }

        setupInputProcessor();
    }

    @Override
    public void update(float delta) {
        for(StartScreenLevelObject object : levelObjects) {
            object.update(delta);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
         for(StartScreenLevelObject object : levelObjects) {
            object.render(batch);
         }
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {

                Vector3 mousePosition = camera.unproject(new Vector3(screenX, screenY, 0));

                for(StartScreenLevelObject obj : levelObjects) {
                    obj.updateMousePosition(mousePosition.x, mousePosition.y);
                }

                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }
}
