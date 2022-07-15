package de.augsburg.hs.methoden.ki.screens.minmax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;

public class MinMaxScreen extends AbstractScreen {

    ShapeRenderer shapeRenderer;

    private final int VIEWPORT_WIDTH;

    private final int VIEWPORT_HEIGHT;

    private TicTacToe[][] gridArray;

    public MinMaxScreen(MainGame game) {
        super(game);

        VIEWPORT_WIDTH = (int) camera.viewportWidth;
        VIEWPORT_HEIGHT = (int) camera.viewportHeight;

        setClearColor(new Color(1,1,1,1));
        shapeRenderer = new ShapeRenderer();

        // array to store which shape is where
        gridArray = new TicTacToe[3][3];
    }

    @Override
    public void show() {
        super.show();

        setupInputProcessor();
    }

    @Override
    public void hide() {
        super.hide();

        Gdx.input.setInputProcessor(null);
    }

    @Override
    protected void preDraw(SpriteBatch batch) {
        super.preDraw(batch);

        batch.end();
        drawTicTacToeGrid();
        batch.begin();
    }

    private void drawTicTacToeGrid() {

        int cellWidth = VIEWPORT_WIDTH / 3;
        int cellHeight = VIEWPORT_HEIGHT / 3;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,1));

        for(int i = 1; i < 3; i++) {
            Vector2 lineStart = new Vector2(i * cellWidth, 0);
            Vector2 lineEnd = new Vector2(i* cellWidth, VIEWPORT_HEIGHT);

            shapeRenderer.rectLine(lineStart, lineEnd, 2);
        }

        for(int i = 1; i < 3; i++) {
            Vector2 lineStart = new Vector2(0, i * cellHeight);
            Vector2 lineEnd = new Vector2(VIEWPORT_WIDTH, i * cellHeight);

            shapeRenderer.rectLine(lineStart, lineEnd, 2);
        }

        shapeRenderer.end();
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

                Vector3 mousePos = camera.unproject(new Vector3(screenX, screenY, 0));

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
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }

    private enum TicTacToe {
        CIRCLE,
        CROSS
    }
}
