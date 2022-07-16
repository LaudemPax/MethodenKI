package de.augsburg.hs.methoden.ki.screens.minmax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.minimax.CircleActor;
import de.augsburg.hs.methoden.ki.actors.minimax.CrossActor;
import de.augsburg.hs.methoden.ki.algorithms.minimax.TicTacToe;
import de.augsburg.hs.methoden.ki.algorithms.minimax.TicTacToeMiniMax;
import de.augsburg.hs.methoden.ki.algorithms.minimax.TicTacToeMove;
import de.augsburg.hs.methoden.ki.engine.Screen;

public class MinMaxScreen extends Screen {

    ShapeRenderer shapeRenderer;

    private final int VIEWPORT_WIDTH;

    private final int VIEWPORT_HEIGHT;

    private final int CELL_WIDTH;

    private final int CELL_HEIGHT;

    private TicTacToe[][] grid;

    private final Vector2[][] gridCoordinates;

    private TicTacToeMiniMax miniMax;

    private final TicTacToe PLAYER_MOVE_TYPE = TicTacToe.MIN;

    private final TicTacToe AI_MOVE_TYPE = TicTacToe.MAX;

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
                float coordX = j * CELL_WIDTH + CELL_WIDTH / 2f;
                float coordY = i * CELL_HEIGHT + CELL_HEIGHT / 2f;

                gridCoordinates[i][j] = new Vector2(coordX, coordY);
            }
        }
    }

    /**
     * returns the grird position based on the given coordinates.
     *
     * Example: if the given coordinates are within the top left grid
     * then row = 2, column = 0 is returned
     *
     * This is used to figure out which square was clicked by the user
     *
     * @param x, y
     * @return
     */
    private TicTacToeMove coordinatesToTicTacToeMove(float x, float y) {
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 4; j++) {
                if(x < (CELL_WIDTH * j) && (y < CELL_HEIGHT * i)) {
                    return new TicTacToeMove(i - 1, j - 1);
                }
            }
        }

        // not found
        return null;
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

    /**
     * Carries out a round of TicTacToe
     */
    private void executeGameRound(TicTacToeMove playerMove) {
        doPlayerMove(playerMove);

        TicTacToeMove aiMove = miniMax.findBestMove(grid);
        doAiMove(aiMove);
    }

    private void doPlayerMove(TicTacToeMove move) {
        if(grid [move.row][move.column] == TicTacToe.EMPTY) {
            grid[move.row][move.column] = PLAYER_MOVE_TYPE;
            Vector2 cellCoordinates = gridCoordinates[move.row][move.column];
            addActor(new CrossActor(cellCoordinates));
        }
    }

    private void doAiMove(TicTacToeMove move) {
        if(grid [move.row][move.column] == TicTacToe.EMPTY) {
            grid[move.row][move.column] = AI_MOVE_TYPE;
            Vector2 cellCoordinates = gridCoordinates[move.row][move.column];
            addActor(new CircleActor(cellCoordinates));
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

                Vector3 mousePos = camera.unproject(new Vector3(screenX, screenY, 0));
                System.out.printf("Clicked at (%f, %f)%n", mousePos.x, mousePos.y);
                TicTacToeMove move = coordinatesToTicTacToeMove(mousePos.x, mousePos.y);
                System.out.printf("Grid pos (row,col): (%d,%d)", move.row, move.column);
                executeGameRound(move);

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
