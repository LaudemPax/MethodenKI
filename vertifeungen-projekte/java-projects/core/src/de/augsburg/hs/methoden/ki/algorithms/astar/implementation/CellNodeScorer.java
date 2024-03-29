package de.augsburg.hs.methoden.ki.algorithms.astar.implementation;

import de.augsburg.hs.methoden.ki.algorithms.astar.Scorer;

/***
 * For calculating the cost between two nodes.
 * Total cost is the sum of the cost of traversing both nodes.
 */
public class CellNodeScorer implements Scorer<CellNode> {

    double costWeightage = 100;

    @Override
    public double computeCost(CellNode from, CellNode to) {

        return (from.getCost() + to.getCost()) * costWeightage;
    }
}
