package de.augsburg.hs.methoden.ki.algorithms.astar.implementation;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.algorithms.astar.GraphNode;

public class CellNode implements GraphNode {
    private final Vector2 coordinates;

    private final int nodeX;

    private final int nodeY;

    private final float cost;

    public CellNode(Vector2 coordinates, int nodeX, int nodeY, float cost) {
        this.nodeX = nodeX;
        this.nodeY = nodeY;
        this.coordinates = coordinates;
        this.cost = cost;
    }

    @Override
    public String getId() {
        return CellNode.formatNodeCoordinatesString(nodeX, nodeY);
    }

    public Vector2 getCoordinates() {
        return coordinates;
    }

    public int getNodeX() {
        return nodeX;
    }

    public int getNodeY() {
        return nodeY;
    }

    public float getCost() {
        return cost;
    }

    public static String formatNodeCoordinatesString(int nodeX, int nodeY) {
        return String.format("%d:%d", nodeX, nodeY);
    }
}
