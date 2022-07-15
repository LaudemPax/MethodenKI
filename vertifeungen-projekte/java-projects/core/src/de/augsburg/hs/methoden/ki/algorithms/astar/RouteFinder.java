package de.augsburg.hs.methoden.ki.algorithms.astar;

import java.util.*;

public class RouteFinder<T extends GraphNode>  {
    private final Graph<T> graph;
    private final Scorer<T> nextNodeScorer;
    private final Scorer<T> targetScorer;

    public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }

    public List<T> findRoute(T from, T to) {

        // priority queue uses comparator to get the best entry
        Queue<RouteNode> openSet = new PriorityQueue<>();


        Map<T, RouteNode<T>> visitedNodes = new HashMap<>();

        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
        openSet.add(start);
        visitedNodes.put(from, start);

        while (!openSet.isEmpty()) {
            RouteNode<T> next = openSet.poll();
            if (next.getCurrent().equals(to)) {
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = visitedNodes.get(current.getPrevious());
                } while (current != null);
                return route;
            }
            graph.getConnections(next.getCurrent()).forEach(connectedNode -> {

                // create a new RouteNode Object for each connected Node
                RouteNode<T> nextNode = visitedNodes.getOrDefault(connectedNode, new RouteNode<>(connectedNode));

                // add it to the nodes we've visited
                visitedNodes.put(connectedNode, nextNode);

                // calculate the new score of the route
                double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connectedNode);
                if (newScore < nextNode.getRouteScore()) {
                    // if the route score is cheaper than the current route
                    // (without looking at the heuristic!) update the route
                    // and add it to the open set
                    nextNode.setPrevious(next.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connectedNode, to));
                    openSet.add(nextNode);
                }
            });
        }

        // no route found!
        return null;
    }
}
