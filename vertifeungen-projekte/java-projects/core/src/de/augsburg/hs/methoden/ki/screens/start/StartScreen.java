package de.augsburg.hs.methoden.ki.screens.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;
import de.augsburg.hs.methoden.ki.actors.startscreen.StartScreenButtonActor;
import de.augsburg.hs.methoden.ki.screens.astar.AStarPathfindingScreen;
import de.augsburg.hs.methoden.ki.screens.minmax.MinMaxScreen;

import java.util.ArrayList;

/***
 *
 * Start screen shows a few buttons which go to other screens when clicked
 *
 */
public class StartScreen extends AbstractScreen {

    private final int MARGIN = 20;

    private ArrayList<StartScreenButtonActor> levelActors;

    public StartScreen(MainGame game) {
        super(game);

        levelActors = new ArrayList<>();
        levelActors.add(new StartScreenButtonActor("Kapitel 3: A* Pfadsuche", new AStarPathfindingScreen(game)));
        levelActors.add(new StartScreenButtonActor("Kapitel 4: MinMax Algo", new MinMaxScreen(game)));
        levelActors.add(new StartScreenButtonActor("Kapitel 5: Constraints", null));

        int screenWidth = Gdx.graphics.getWidth();

        int widthOfObjects = 2 * MARGIN + StartScreenButtonActor.WIDTH;
        int objectsPerRow = screenWidth / widthOfObjects;
        if(objectsPerRow < 1) objectsPerRow = 1;

        // calculate layout of title screen objects
        int totalWidthOfRow = 0;
        int lastYPosition = 0;

        for(int i = 1; i <= levelActors.size(); i++) {
            StartScreenButtonActor obj = levelActors.get(i - 1);

            int xPosition = totalWidthOfRow + MARGIN;
            int yPosition = lastYPosition + MARGIN;

            // OpenGL has its cooridnates on the bottom left corner
            int invertedYPosition = Gdx.graphics.getHeight() - yPosition;
            invertedYPosition -= StartScreenButtonActor.HEIGHT;

            obj.setPosition(xPosition, invertedYPosition);

            // each row
            if((i % objectsPerRow) > 0 || i == 1){
                totalWidthOfRow += MARGIN + StartScreenButtonActor.WIDTH;
            } else {
                totalWidthOfRow = 0;
                lastYPosition += MARGIN + StartScreenButtonActor.HEIGHT;
            }
        }
    }

    @Override
    public void show() {
        super.show();

        initializeInputProcessor();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float delta) {
        for(StartScreenButtonActor actor : levelActors) {
            actor.update(delta);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
         for(StartScreenButtonActor actor : levelActors) {
            actor.draw(batch);
         }
    }

    private void initializeInputProcessor() {
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

                for(StartScreenButtonActor actor : levelActors) {
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

                for(StartScreenButtonActor actor : levelActors) {
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

    private boolean isMouseWithinLevelActorBounds(Vector3 mousePos, StartScreenButtonActor actor) {
        Vector2 position = actor.getPosition();
        boolean mouseInXBounds = (mousePos.x > position.x  && (mousePos.x < position.x + StartScreenButtonActor.WIDTH));
        boolean mouseInYBounds = (mousePos.y > position.y && (mousePos.y < position.y + StartScreenButtonActor.HEIGHT));
        return mouseInXBounds && mouseInYBounds;
    }
}
