package de.augsburg.hs.methoden.ki.screens.nqueens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.nqueens.QueenActor;
import de.augsburg.hs.methoden.ki.algorithms.constraints.ConstraintSolverQueensProblem;
import de.augsburg.hs.methoden.ki.engine.Screen;

public class NQueensScreen extends Screen {

    private final int VIEWPORT_WIDTH;

    private final int VIEWPORT_HEIGHT;

    private final int MINIMUM_N = 4;

    // hard to see which queen is where after n = 18
    private final int MAXIMUM_N = 27;

    private int nQueens = MINIMUM_N;

    private int cellWidth;

    private int cellHeight;

    private ShapeRenderer shapeRenderer;

    private Vector2[][] gridCoordinates;

    private ConstraintSolverQueensProblem solver;

    BitmapFont font;

    public NQueensScreen(MainGame game) {
        super(game);

        VIEWPORT_WIDTH = (int) camera.viewportWidth;
        VIEWPORT_HEIGHT = (int) camera.viewportHeight;

        gridCoordinates = new Vector2[MAXIMUM_N][MAXIMUM_N];

        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(new Color(0,0,0,1));
        setClearColor(new Color(1,1,1,1));

        solver = new ConstraintSolverQueensProblem();

        calculateNQueens();
    }

    /**
     * The main function where everything happens
     */
    private void calculateNQueens() {
        calculateGridCoordinates();
        int[] solution = solver.solve(nQueens);

        // empty all actors
        actors.removeAll(actors);

        for(int i = 0; i < solution.length; i++) {
            int column = solution[i] - 1;
            Vector2 coordinates = gridCoordinates[i][column];
            addActor(new QueenActor(coordinates));
        }
    }

    @Override
    protected void preDraw(SpriteBatch batch) {
        super.preDraw(batch);

        batch.end();
        drawGrid();
        batch.begin();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        font.draw(batch, String.format("N-Queens: %d", nQueens), 10, VIEWPORT_HEIGHT - 10);
    }

    private void drawGrid() {

        int cellWidth = VIEWPORT_WIDTH/nQueens;
        int cellHeight = VIEWPORT_HEIGHT/nQueens;

        int horizontalLines = VIEWPORT_WIDTH/cellWidth;
        int verticalLines = VIEWPORT_HEIGHT/cellHeight;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0,0,0,1));

        for(int i = 0 ; i < verticalLines; i++) {
            Vector2 lineStart = new Vector2(i * cellWidth, 0);
            Vector2 lineEnd = new Vector2(i* cellWidth, VIEWPORT_HEIGHT);

            shapeRenderer.line(lineStart, lineEnd);
        }

        for(int i = 0 ; i < horizontalLines; i++) {
            Vector2 lineStart = new Vector2(0, i * cellHeight);
            Vector2 lineEnd = new Vector2(VIEWPORT_WIDTH, i * cellHeight);

            shapeRenderer.line(lineStart, lineEnd);
        }

        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    private void calculateGridCoordinates() {

        int cellWidth = VIEWPORT_WIDTH/nQueens;
        int cellHeight = VIEWPORT_HEIGHT/nQueens;

        for (int i = 0; i < nQueens; i++) {
            for (int j = 0; j < nQueens; j++) {
                float coordX = j * cellWidth + cellWidth / 2f;
                float coordY = i * cellHeight + cellHeight / 2f;

                if(gridCoordinates[i][j] == null) {
                    // lazy initialize
                    gridCoordinates[i][j] = new Vector2(coordX, coordY);
                } else {
                    gridCoordinates[i][j].x = coordX;
                    gridCoordinates[i][j].y = coordY;
                }

            }
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

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                if(keycode == Input.Keys.UP) {

                    if((nQueens + 1) <= MAXIMUM_N) {
                        nQueens++;
                        calculateNQueens();
                    }

                } else if(keycode == Input.Keys.DOWN) {

                    if((nQueens - 1) >= MINIMUM_N) {
                        nQueens--;
                        calculateNQueens();
                    }

                } else if(keycode == Input.Keys.ESCAPE) {
                    game.goToStartScreen();
                }

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
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }
}