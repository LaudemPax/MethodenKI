package de.augsburg.hs.methoden.ki.screens.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.algorithms.GraphNode;
import de.augsburg.hs.methoden.ki.algorithms.astar.Scorer;

/***
 * Calculates the cost of a given node to the target node.
 * Euclidean distance heuristic is used.
 */
public class CellTargetScorer implements Scorer {

    @Override
    public double computeCost(GraphNode from, GraphNode to) {

        Vector2 coordinatesFrom = ((CellNode) from).getCoordinates();
        Vector2 coordinatesTo = ((CellNode) to).getCoordinates();

        double deltaX = coordinatesFrom.x - coordinatesTo.x;
        double deltaY = coordinatesFrom.y - coordinatesTo.y;

        double euclideanDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        return euclideanDistance;
    }
}
