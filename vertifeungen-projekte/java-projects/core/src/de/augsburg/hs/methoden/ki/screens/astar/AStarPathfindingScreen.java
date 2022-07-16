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
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.actors.astar.AStarAgent;
import de.augsburg.hs.methoden.ki.actors.astar.AStarTarget;
import de.augsburg.hs.methoden.ki.algorithms.astar.Graph;
import de.augsburg.hs.methoden.ki.algorithms.astar.GraphNode;
import de.augsburg.hs.methoden.ki.algorithms.astar.RouteFinder;
import de.augsburg.hs.methoden.ki.algorithms.astar.implementation.CellNode;
import de.augsburg.hs.methoden.ki.algorithms.astar.implementation.CellNodeScorer;
import de.augsburg.hs.methoden.ki.algorithms.astar.implementation.CellTargetScorer;
import de.augsburg.hs.methoden.ki.engine.Screen;

import java.util.*;

/**
 * Screen for the A* pathfinding task
 */
public class AStarPathfindingScreen extends Screen {

    /**
     * Values for RGB are meant to be 0 to 1
     */
    private final float bg_R = 150f / 255f;
    private final float bg_G = 190f / 255f;
    private final float bg_B = 37f / 255f;

    private final int CELL_SIZE = 20;

    private final int CELL_ROWS;
    private final int CELL_COLUMNS;

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private CellNode targetNode;

    private CellNode startNode;

    private HashSet<CellNode> nodeSet;

    private HashMap<String, Set<String>> connections;

    private Graph cellNodeGraph;

    private List<CellNode> solvedRoute;

    private boolean noPathFlag = false;

    public AStarPathfindingScreen(MainGame game) {
        super(game);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        font = new BitmapFont();
        font.setColor(new Color(1,0,0,1));
        font.getData().setScale(2f);

        solvedRoute = new ArrayList<>();

        CELL_ROWS = Gdx.graphics.getHeight() / CELL_SIZE;
        CELL_COLUMNS = Gdx.graphics.getWidth() / CELL_SIZE;

        generatePathfindingTask();
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

    /**
     * Generates a random pathfinding task
     */
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

    /**
     * Resets the pathfinding task
     */
    private void clearPathfindingTask() {
        actors.removeAll(actors);
        solvedRoute.removeAll(solvedRoute);
        noPathFlag = false;
    }

    /**
     * Generates the search graph
     */
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

        // draws the generated terrain
        batch.end();
        drawGrid();
        drawTerrainCells();
        batch.begin();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        // if a solved route exists, draw it
        if(solvedRoute.size() > 0) {
            drawSolvedRoute();
        }

        // display text if no path is found
        if(noPathFlag) {
            font.draw(batch, "No traversable route found!"
                    , 10, Gdx.graphics.getHeight() - 300);
        }
    }

    /***
     * Draws the grid lines onto the background
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

    /**
     * Helper function for drawing the solved route
     */
    private void drawSolvedRoute() {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1,0,1,1));

        for (int i = 1; i < solvedRoute.size(); i++) {
            Vector2 lineStart = solvedRoute.get(i).getCoordinates();
            Vector2 lineEnd = solvedRoute.get(i-1).getCoordinates();

            // adjust to show center of tile
            float adjustedStartX = lineStart.x;
            float adjustedStartY = lineStart.y;
            float adjustedEndX = lineEnd.x;
            float adjustedEndY = lineEnd.y;

            shapeRenderer.rectLine(new Vector2(adjustedStartX, adjustedStartY), new Vector2(adjustedEndX, adjustedEndY),2);
        }

        shapeRenderer.end();

        batch.begin();
    }

    /**
     *
     * Helper function for drwaing the terrain cells.
     * The darker the green square the higher the cost of traversing the cell.
     *
     */
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

    /**
     * Generates the connection Id string by looking at
     * the 8 surrounding nodes of the given node.
     * @param node
     * @return
     */
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

    /**
     * Carries out the A* pathfinding
     */
    private void solvePathfindingTask() {

        // debugging print statements
        System.out.println("=========================================================");
        System.out.println("Solving for:");
        System.out.println(String.format("Start coordinates: (%f,%f)"
                , startNode.getCoordinates().x, startNode.getCoordinates().y));
        System.out.println(String.format("End coordinates: (%f,%f)"
                , targetNode.getCoordinates().x, targetNode.getCoordinates().y));
        System.out.println("Nodes: " + nodeSet.size());
        System.out.println("Node connections: " + connections.size());
        System.out.println("=========================================================");

        // instantiate scorers used for the algorithm
        CellNodeScorer nodeScorer = new CellNodeScorer();
        CellTargetScorer targetScorer = new CellTargetScorer();
        RouteFinder<CellNode> finder = new RouteFinder<>(cellNodeGraph, nodeScorer, targetScorer);

        List<CellNode> route = finder.findRoute(startNode, targetNode);

        if(route != null) {
            solvedRoute.addAll(route);

            CellNode solvedStartNode = solvedRoute.get(0);
            CellNode solvedEndNode = solvedRoute.get(solvedRoute.size() - 1);

            System.out.println("=========================================================");
            System.out.println("Solved");
            System.out.println("Route size: " + solvedRoute.size());
            System.out.println(String.format("Solved start coordinates: (%f,%f)"
                    , solvedStartNode.getCoordinates().x, solvedStartNode.getCoordinates().y));
            System.out.println(String.format("Solved end coordinates: (%f,%f)"
                    , solvedEndNode.getCoordinates().x, solvedEndNode.getCoordinates().y));
            System.out.println("Nodes: " + nodeSet.size());
            System.out.println("=========================================================");
        } else {
            noPathFlag = true;
            System.out.println("No path found!");
        }
    }

    private void initializeInputListener () {

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                if(keycode == Input.Keys.ENTER) {
                    if(solvedRoute.size() < 1) {
                        solvePathfindingTask();
                    }
                } else if(keycode == Input.Keys.SPACE) {
                    clearPathfindingTask();
                    generatePathfindingTask();
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
