package searchStrategies;

import coreComponents.*;

import java.util.LinkedList;

public class AStar extends IntermediateSearchAlgorithm{

    /**
    *  Constructor for the AStar search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public AStar(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    /**
    *  Method that creates a new node instance. The BestF search creates nodes with a heursitic value acquired from the manhattan distance between the state and the goal
    *  as well as the heursitic value of its parent node
    *  @parameter parent Parent node for the new node
    *  @parameter state Coordinates of the node's state
    *  @parameter direction Direction String for how to reach the node from its parent
    *  @parameter goal Coordinates of the goal
    *  @return Newly created node instance
    */
    @Override
    protected Node makeNode(Node parent, Coord state, String direction, Coord goal){
        // Existing path cost variable is acquired based on whether the node is a root node or not
        double existingPathCost = 0;
        if(parent != null) existingPathCost = parent.getCost() + 1.0;

        Node newNode = new Node(state, parent, direction, super.getManhattanDistance(state, goal) + existingPathCost);
        return newNode;
    }

    /**
    *  Method that adds a node to the frontier. This overwrite is required as the AStar search works differently from normal insertions as it replaces nodes in the frontier of the same
    *  state if the new node has a lower heuristic value
    *  @parameter frontier Frontier that the node will be inserted into
    *  @parameter node Node to be inserted intop the frontier
    */
    @Override
    protected void insert(LinkedList<Node> frontier, Node node){
        if(super.inFrontier(frontier, node.getState())){// Super class' inFrontier method is called to see if the state is within the frontier
            for (Node currentNode : frontier) {
                if (currentNode.getState().equals(node.getState())) {// Frontier is iterated through until the node of the same state is found
                  if (currentNode.getHeuristic() > node.getHeuristic()) {// Check to see if this same node has a higher heuristic value than the new node
                    // If so, old node is replaced by the new
                    frontier.remove(currentNode);
                    frontier.add(node);
                  }
                }
              }
        }else frontier.add(node);// If not already in the frontier, then the node is simply added
    }

    /**
    *  Method that checks to see if a state is already in the frontier in the form of a node's state. This overwrite is required so that the GeneralSearchAlgorithm's search function still works
    *  as intended with the new laws on how AStar inserts nodes into the frontier
    *  @parameter frontier The frontier to be checked
    *  @parameter state The state being checked for
    *  @return Always false
    */
    @Override
    protected boolean inFrontier(LinkedList<Node> frontier, Coord state){
        return false;
    }

}