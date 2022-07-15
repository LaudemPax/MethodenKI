package de.augsburg.hs.methoden.ki.screens.astar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.astar.AStarAgent;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;

import java.util.ArrayList;

public class AStarPathfindingScreen extends AbstractScreen {

    private final float bg_R = 150f / 255f;
    private final float bg_G = 190f / 255f;
    private final float bg_B = 37f / 255f;

    private final int CELL_SIZE = 20;

    private int CELL_ROWS;
    private int CELL_COLUMNS;
    private ShapeRenderer shapeRenderer;
    private ArrayList<CellNode> nodes;
    private BitmapFont font;

    public AStarPathfindingScreen(MainGame game) {
        super(game);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        font = new BitmapFont();
        font.getData().setScale(0.5f);
    }

    @Override
    public void create() {
        CELL_ROWS = Gdx.graphics.getHeight() / CELL_SIZE;
        CELL_COLUMNS = Gdx.graphics.getWidth() / CELL_SIZE;

        // create random terrain
        generateTerrain();

        // add a pathfinder agent
        Vector2 coordinates = nodes.get(MathUtils.random(nodes.size())).getCoordinates();
        addActor(new AStarAgent(coordinates));
    }

    private void generateTerrain() {
        nodes = new ArrayList<>();

        for(int y  = 0; y < CELL_ROWS; y++) {
            for(int x = 0; x < CELL_COLUMNS; x++) {
                String nodeId = String.format("%d:%d", x, y);

                // translate coordinates to the center of each cell
                float xCoord = x * CELL_SIZE + (CELL_SIZE/2f);
                float yCoord = y * CELL_SIZE + (CELL_SIZE/2f);

                Vector2 coordinates = new Vector2(xCoord, yCoord);
                float nodeCost = MathUtils.random(0f, 1f);

                nodes.add(new CellNode(nodeId, coordinates, nodeCost));
            }
        }
    }

    @Override
    protected void preDraw(SpriteBatch batch) {
        super.preDraw(batch);

        batch.end();
        drawGrid();
        drawTerrainCells();
        batch.begin();
    }

    /***
     * Draws the grid onto the background
     */
    private void drawGrid() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int verticalLines = CELL_COLUMNS;
        int horizontalLines = CELL_ROWS;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0,0,0,1));

        for(int i = 0 ; i < verticalLines; i++) {
            Vector2 lineStart = new Vector2(i * CELL_SIZE, 0);
            Vector2 lineEnd = new Vector2(i* CELL_SIZE, screenHeight);

            shapeRenderer.line(lineStart, lineEnd);
        }

        for(int i = 0 ; i < horizontalLines; i++) {
            Vector2 lineStart = new Vector2(0, i * CELL_SIZE);
            Vector2 lineEnd = new Vector2(screenWidth, i * CELL_SIZE);

            shapeRenderer.line(lineStart, lineEnd);
        }

        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    private void drawTerrainCells() {
        for(CellNode node : nodes) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // higher cost means darker green
            float greenIntensity = 1 - node.getCost();
            shapeRenderer.setColor(new Color(0,greenIntensity,0,1));

            Vector2 coords = node.getCoordinates();
            float x = coords.x - CELL_SIZE/2f;
            float y = coords.y - CELL_SIZE/2f;

            shapeRenderer.rect(x, y, CELL_SIZE, CELL_SIZE);
            shapeRenderer.end();
        }
    }
}
