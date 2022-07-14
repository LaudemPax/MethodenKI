package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
        levelObjects.add(new StartScreenLevelObject("Kapitel 3: A* Pfadsuche", new AStarPathfindingScreen(game)));
        levelObjects.add(new StartScreenLevelObject("Kapitel 4: MinMax Algo", null));
        levelObjects.add(new StartScreenLevelObject("Kapitel 5: Constraints", null));

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
                Vector3 mousePosition = camera.unproject(new Vector3(screenX, screenY, 0));

                for(StartScreenLevelObject obj : levelObjects) {
                    if(isMouseWithinObjectBounds(mousePosition, obj)){
                        game.setScreen(obj.getTargetScreen());
                    }
                }

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
                    obj.setOnHover(isMouseWithinObjectBounds(mousePosition, obj));
                }

                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }

    private boolean isMouseWithinObjectBounds(Vector3 mousePos, StartScreenLevelObject object) {
        Vector2 position = object.getPosition();
        boolean mouseInXBounds = (mousePos.x > position.x  && (mousePos.x < position.x + StartScreenLevelObject.WIDTH));
        boolean mouseInYBounds = (mousePos.y > position.y && (mousePos.y < position.y + StartScreenLevelObject.HEIGHT));
        return mouseInXBounds && mouseInYBounds;
    }
}
