package de.augsburg.hs.methoden.ki.algorithms.astar;

import de.augsburg.hs.methoden.ki.algorithms.GraphNode;

public interface Scorer <T extends GraphNode>{
    double computeCost(T from, T to);
}
