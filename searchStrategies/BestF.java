package searchStrategies;

import coreComponents.*;

public class BestF extends IntermediateSearchAlgorithm{

    /**
    *  Constructor for the best first  search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public BestF(Coord  start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    /**
    *  Method that creates a new node instance. The BestF search creates nodes with a heursitic value acquired from the manhattan distance between the state and the goal
    *  @parameter parent Parent node for the new node
    *  @parameter state Coordinates of the node's state
    *  @parameter direction Direction String for how to reach the node from its parent
    *  @parameter goal Coordinates of the goal
    *  @return Newly created node instance
    */
    @Override
    protected Node makeNode(Node parent, Coord state, String direction, Coord goal){
        Node newNode = new Node(state, parent, direction, getManhattanDistance(state, goal));
        return newNode;
    }

}