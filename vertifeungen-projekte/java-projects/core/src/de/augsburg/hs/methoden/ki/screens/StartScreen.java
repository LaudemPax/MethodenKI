package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;
import de.augsburg.hs.methoden.ki.actors.StartScreenLevelActor;
import de.augsburg.hs.methoden.ki.screens.astar.AStarPathfindingScreen;

import java.util.ArrayList;

public class StartScreen extends AbstractScreen {

    private final int MARGIN = 20;

    private ArrayList<StartScreenLevelActor> levelActors;

    public StartScreen(MainGame game) {
        super(game);
    }

    @Override
    public void create() {
        levelActors = new ArrayList<>();
        levelActors.add(new StartScreenLevelActor("Kapitel 3: A* Pfadsuche", new AStarPathfindingScreen(game)));
        levelActors.add(new StartScreenLevelActor("Kapitel 4: MinMax Algo", null));
        levelActors.add(new StartScreenLevelActor("Kapitel 5: Constraints", null));

        int screenWidth = Gdx.graphics.getWidth();

        int widthOfObjects = 2 * MARGIN + StartScreenLevelActor.WIDTH;
        int objectsPerRow = screenWidth / widthOfObjects;
        if(objectsPerRow < 1) objectsPerRow = 1;

        // calculate layout of title screen objects
        int totalWidthOfRow = 0;
        int lastYPosition = 0;

        for(int i = 1; i <= levelActors.size(); i++) {
            StartScreenLevelActor obj = levelActors.get(i - 1);

            int xPosition = totalWidthOfRow + MARGIN;
            int yPosition = lastYPosition + MARGIN;

            // OpenGL has its cooridnates on the bottom left corner
            int invertedYPosition = Gdx.graphics.getHeight() - yPosition;
            invertedYPosition -= StartScreenLevelActor.HEIGHT;

            obj.setPosition(xPosition, invertedYPosition);

            // each row
            if((i % objectsPerRow) > 0 || i == 1){
                totalWidthOfRow += MARGIN + StartScreenLevelActor.WIDTH;
            } else {
                totalWidthOfRow = 0;
                lastYPosition += MARGIN + StartScreenLevelActor.HEIGHT;
            }
        }

        setupInputProcessor();
    }

    @Override
    public void update(float delta) {
        for(StartScreenLevelActor actor : levelActors) {
            actor.update(delta);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
         for(StartScreenLevelActor actor : levelActors) {
            actor.draw(batch);
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

                for(StartScreenLevelActor actor : levelActors) {
                    if(isMouseWithinLevelActorBounds(mousePosition, actor)){
                        game.setScreen(actor.getTargetScreen());
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

                for(StartScreenLevelActor actor : levelActors) {
                    actor.setOnHover(isMouseWithinLevelActorBounds(mousePosition, actor));
                }

                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }

    private boolean isMouseWithinLevelActorBounds(Vector3 mousePos, StartScreenLevelActor actor) {
        Vector2 position = actor.getPosition();
        boolean mouseInXBounds = (mousePos.x > position.x  && (mousePos.x < position.x + StartScreenLevelActor.WIDTH));
        boolean mouseInYBounds = (mousePos.y > position.y && (mousePos.y < position.y + StartScreenLevelActor.HEIGHT));
        return mouseInXBounds && mouseInYBounds;
    }
}
