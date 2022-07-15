package de.augsburg.hs.methoden.ki.screens.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.algorithms.GraphNode;

public class CellNode implements GraphNode {
    private final String id;
    private final Vector2 coordinates;
    private final float cost;

    public CellNode(String id, Vector2 coordinates, float cost) {
        this.id = id;
        this.coordinates = coordinates;
        this.cost = cost;
    }

    @Override
    public String getId() {
        return id;
    }

    public Vector2 getCoordinates() {
        return coordinates;
    }

    public float getCost() {
        return cost;
    }
}
