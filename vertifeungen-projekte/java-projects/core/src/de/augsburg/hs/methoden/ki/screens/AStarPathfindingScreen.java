package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.astar.AStarAgent;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;

public class AStarPathfindingScreen extends AbstractScreen {

    private final float bg_R = 150f / 255f;
    private final float bg_G = 190f / 255f;
    private final float bg_B = 37f / 255f;

    private final int CELL_SIZE = 20;

    private final int CELL_ROWS;
    private final int CELL_COLUMNS;

    private ShapeRenderer shapeRenderer;

    public AStarPathfindingScreen(MainGame game) {
        super(game);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        CELL_ROWS = Gdx.graphics.getHeight() / CELL_SIZE;
        CELL_COLUMNS = Gdx.graphics.getWidth() / CELL_SIZE;
    }

    @Override
    public void create() {
        // change background to green
        setClearColor(new Color(bg_R,bg_G,bg_B, 1));

        // add actors
        addActor(new AStarAgent(new Vector2(10,10)));
    }



    @Override
    protected void preDraw(SpriteBatch batch) {
        super.preDraw(batch);

        batch.end();
        drawGrid(CELL_SIZE);
        batch.begin();
    }

    /***
     * Draws the grid onto the background
     * @param cellSize
     */
    private void drawGrid(int cellSize) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int verticalLines = CELL_COLUMNS;
        int horizontalLines = CELL_ROWS;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0,0,0,1));

        for(int i = 0 ; i < verticalLines; i++) {
            Vector2 lineStart = new Vector2(i * cellSize, 0);
            Vector2 lineEnd = new Vector2(i* cellSize, screenHeight);

            shapeRenderer.line(lineStart, lineEnd);
        }

        for(int i = 0 ; i < horizontalLines; i++) {
            Vector2 lineStart = new Vector2(0, i * cellSize);
            Vector2 lineEnd = new Vector2(screenWidth, i * cellSize);

            shapeRenderer.line(lineStart, lineEnd);
        }

        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
}
