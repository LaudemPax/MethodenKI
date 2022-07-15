package de.augsburg.hs.methoden.ki.algorithms.astar;

public interface Scorer <T extends GraphNode>{
    double computeCost(T from, T to);
}
