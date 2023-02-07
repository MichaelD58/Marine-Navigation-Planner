package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class BestF extends IntermediateSearchAlgorithm{

    public BestF(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    @Override
    protected Node makeNode(Node parent, Coord state, String direction){
        Node newNode = new Node(state, parent, direction, super.getManhattanDistance(state, goal));
        return newNode;
    }

}