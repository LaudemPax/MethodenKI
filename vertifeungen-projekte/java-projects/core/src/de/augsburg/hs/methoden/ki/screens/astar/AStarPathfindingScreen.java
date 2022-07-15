package de.augsburg.hs.methoden.ki.screens.astar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.astar.AStarAgent;
import de.augsburg.hs.methoden.ki.actors.astar.AStarTarget;
import de.augsburg.hs.methoden.ki.algorithms.Graph;
import de.augsburg.hs.methoden.ki.algorithms.GraphNode;
import de.augsburg.hs.methoden.ki.algorithms.astar.RouteFinder;
import de.augsburg.hs.methoden.ki.engine.AbstractScreen;

import javax.xml.soap.Node;
import java.util.*;

public class AStarPathfindingScreen extends AbstractScreen {

    private final float bg_R = 150f / 255f;
    private final float bg_G = 190f / 255f;
    private final float bg_B = 37f / 255f;

    private final int CELL_SIZE = 20;

    private int CELL_ROWS;
    private int CELL_COLUMNS;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private CellNode targetNode;

    private CellNode startNode;

    private HashSet<CellNode> nodeSet;

    private HashMap<String, Set<String>> connections;

    private Graph cellNodeGraph;

    public AStarPathfindingScreen(MainGame game) {
        super(game);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        font = new BitmapFont();
        font.getData().setScale(0.5f);
    }

    @Override
    public void show() {
        super.show();

        initializeInputListener();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void create() {
        CELL_ROWS = Gdx.graphics.getHeight() / CELL_SIZE;
        CELL_COLUMNS = Gdx.graphics.getWidth() / CELL_SIZE;

        generatePathfindingTask();
    }

    private void generatePathfindingTask() {
        // create random graph of nodes
        generateGraph();

        // get random start and target nodes.
        // also ensure start node is not target node.
        startNode = (CellNode) getRandomNodeFromSet(nodeSet);
        do {
            targetNode = (CellNode) getRandomNodeFromSet(nodeSet);
        }while(startNode.getId().equals(targetNode.getId()));


        // add a pathfinder agent
        addActor(new AStarAgent(startNode.getCoordinates()));

        // add a target
        addActor(new AStarTarget(targetNode.getCoordinates()));
    }

    private void clearPathfindingTask() {
        actors.removeAll(actors);
    }

    private void generateGraph() {
        nodeSet = new HashSet<>();
        connections = new HashMap<>();

        // Note: (0,0) is bottom left of the screen
        for(int y = 0; y < CELL_ROWS; y++) {
            for(int x = 0; x < CELL_COLUMNS; x++) {
                // translate coordinates to the center of each cell
                float xCoord = x * CELL_SIZE + (CELL_SIZE/2f);
                float yCoord = y * CELL_SIZE + (CELL_SIZE/2f);
                Vector2 coordinates = new Vector2(xCoord, yCoord);

                // generate a random cost for the node
                float nodeCost = MathUtils.random(0f, 1f);
                CellNode node = new CellNode(coordinates, x, y, nodeCost);

                // add node to set
                nodeSet.add(node);

                // create connections for node
                connections.put(node.getId(), generateConnectionIdsForNode(node));
            }
        }

        cellNodeGraph = new Graph<>(nodeSet, connections);
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
        for(CellNode node : nodeSet) {
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

    private <T extends GraphNode> GraphNode getRandomNodeFromSet(Set<T> set) {
        int randomIndex = MathUtils.random(set.size());

        Iterator<T> iter = set.iterator();

        T node = null;

        for(int i = 0; i < randomIndex; i++) {
            node = iter.next();
        }

        return node;
    }

    private Set<String> generateConnectionIdsForNode(CellNode node) {

        int nodeX = node.getNodeX();
        int nodeY = node.getNodeY();

        NodeCoordinates nodeNorth = new NodeCoordinates(nodeX, nodeY+1);
        NodeCoordinates nodeNorthEast = new NodeCoordinates(nodeX +1 , nodeY+1);
        NodeCoordinates nodeEast = new NodeCoordinates(nodeX + 1 , nodeY);
        NodeCoordinates nodeSouthEast = new NodeCoordinates(nodeX + 1, nodeY - 1);
        NodeCoordinates nodeSouth = new NodeCoordinates(nodeX, nodeY - 1);
        NodeCoordinates nodeSouthWest = new NodeCoordinates(nodeX - 1, nodeY - 1);
        NodeCoordinates nodeWest = new NodeCoordinates(nodeX - 1, nodeY);
        NodeCoordinates nodeNorthWest = new NodeCoordinates(nodeX - 1, nodeY + 1);

        ArrayList<NodeCoordinates> nodeCoordinates = new ArrayList<>();
        nodeCoordinates.add(nodeNorth);
        nodeCoordinates.add(nodeNorthEast);
        nodeCoordinates.add(nodeEast);
        nodeCoordinates.add(nodeSouthEast);
        nodeCoordinates.add(nodeSouth);
        nodeCoordinates.add(nodeSouthWest);
        nodeCoordinates.add(nodeWest);
        nodeCoordinates.add(nodeNorthWest);

        Set<String> connectionIds = new HashSet<>();

        for(NodeCoordinates nCoords : nodeCoordinates) {
            // create id if within range
            if(nCoords.x > 0 && nCoords.x < CELL_COLUMNS &&
                nCoords.y > 0 && nCoords.y < CELL_ROWS) {

                connectionIds.add(CellNode.formatNodeCoordinatesString(nCoords.x, nCoords.y));
            }
        }

        return connectionIds;
    }

    private void solvePathfindingTask() {

        CellNodeScorer nodeScorer = new CellNodeScorer();
        CellTargetScorer targetScorer = new CellTargetScorer();

        RouteFinder<CellNode> finder = new RouteFinder<>(cellNodeGraph, nodeScorer, targetScorer);
    }

    private void initializeInputListener () {

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                if(keycode == Input.Keys.ENTER) {
                    System.out.println("Find path!");
                } else if(keycode == Input.Keys.SPACE) {
                    System.out.println("Randomize!");
                    clearPathfindingTask();
                    generatePathfindingTask();
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

    /**
     * Helper data class for NodeCoordinates.
     * Node coordinates are the coordinates of a node
     * in the graph (not in the game world!)
     */
    private class NodeCoordinates {
        public int x;
        public int y;

        public NodeCoordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
