package de.augsburg.hs.methoden.ki.screens.minmax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.algorithms.minimax.CellOptions;
import de.augsburg.hs.methoden.ki.algorithms.minimax.TicTacToeMiniMax;
import de.augsburg.hs.methoden.ki.engine.Screen;

public class MinMaxScreen extends Screen {

    ShapeRenderer shapeRenderer;

    private final int VIEWPORT_WIDTH;

    private final int VIEWPORT_HEIGHT;

    private final int CELL_WIDTH;

    private final int CELL_HEIGHT;

    private CellOptions[][] grid;

    private final Vector2[][] gridCoordinates;

    private TicTacToeMiniMax miniMax;

    public MinMaxScreen(MainGame game) {
        super(game);

        VIEWPORT_WIDTH = (int) camera.viewportWidth;
        VIEWPORT_HEIGHT = (int) camera.viewportHeight;

        CELL_WIDTH = VIEWPORT_WIDTH / 3;
        CELL_HEIGHT = VIEWPORT_HEIGHT / 3;

        setClearColor(new Color(1,1,1,1));
        shapeRenderer = new ShapeRenderer();

        // algorithm implementation
        miniMax = new TicTacToeMiniMax();

        // array to store which shape is where
        grid = miniMax.generateEmptyGrid();

        // array to translate grid cell to coordinates
        gridCoordinates = new Vector2[3][3];
        generateGridCoordinates();
    }

    private void generateGridCoordinates() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float coordX = i * CELL_WIDTH + CELL_WIDTH / 2f;
                float coordY = i * CELL_HEIGHT - CELL_HEIGHT / 2f;

                gridCoordinates[i][j] = new Vector2(coordX, coordY);
            }
        }
    }

    /**
     * returns the coordinates of the center of the grid
     * based on the given coordinates.
     *
     * Example: if the given coordinates are in the top left grid
     * the coordinates of the center of the top left grid are returned
     *
     * @param coordinates
     * @return
     */
    private Vector2 coordinatesToGridCoodinates(Vector2 coordinates) {
        float x = coordinates.x;
        float y = coordinates.y;

        if(x < CELL_HEIGHT && y < CELL_WIDTH) {
            return gridCoordinates[0][0];
        } else  if(x < CELL_HEIGHT && y > CELL_WIDTH * 2) {
            return gridCoordinates[0][1];
        }
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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,1));

        for(int i = 1; i < 3; i++) {
            Vector2 lineStart = new Vector2(i * CELL_WIDTH, 0);
            Vector2 lineEnd = new Vector2(i* CELL_WIDTH, VIEWPORT_HEIGHT);

            shapeRenderer.rectLine(lineStart, lineEnd, 2);
        }

        for(int i = 1; i < 3; i++) {
            Vector2 lineStart = new Vector2(0, i * CELL_HEIGHT);
            Vector2 lineEnd = new Vector2(VIEWPORT_WIDTH, i * CELL_HEIGHT);

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
                System.out.printf("Clicked at (%f, %f)%n", mousePos.x, mousePos.y);
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
}
