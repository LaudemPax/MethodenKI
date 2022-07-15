package de.augsburg.hs.methoden.ki.screens.astar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.astar.AStarAgent;
import de.augsburg.hs.methoden.ki.algorithms.GraphNode;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;

import java.util.ArrayList;

public class AStarPathfindingScreen extends AbstractScreen {

    private final float bg_R = 150f / 255f;
    private final float bg_G = 190f / 255f;
    private final float bg_B = 37f / 255f;

    private final int CELL_SIZE = 20;

    private final int CELL_ROWS;
    private final int CELL_COLUMNS;
    private ShapeRenderer shapeRenderer;
    private ArrayList<CellNode> nodes;
    private BitmapFont font;

    public AStarPathfindingScreen(MainGame game) {
        super(game);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        font = new BitmapFont();

        CELL_ROWS = Gdx.graphics.getHeight() / CELL_SIZE;
        CELL_COLUMNS = Gdx.graphics.getWidth() / CELL_SIZE;
    }

    @Override
    public void create() {
        // change background to green
        setClearColor(new Color(bg_R,bg_G,bg_B, 1));

        // add actors
        addActor(new AStarAgent(new Vector2(10,10)));

        // randomizes terrain
        generateTerrain();
    }

    private void generateTerrain() {
        nodes = new ArrayList<>();

        for(int y  = 0; y < CELL_ROWS; y++) {
            for(int x = 0; x < CELL_COLUMNS; x++) {
                String nodeId = String.format("%d:%d", x, y);

                // center of each cell
                float xCoord = x * CELL_SIZE/2.0f;
                float yCoord = y * CELL_SIZE/2.0f;

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
        drawGrid(CELL_SIZE);
        batch.begin();

        // draw terrain information
        for(CellNode node : nodes) {
            Vector2 nodeCoord = node.getCoordinates();
            font.draw(batch, node.getId(), nodeCoord.x, nodeCoord.y);
        }
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
