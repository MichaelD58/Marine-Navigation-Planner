package searchStrategies;

import coreComponents.*;

import java.util.LinkedList;

public class AStar extends IntermediateSearchAlgorithm{

    public AStar(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    @Override
    protected Node makeNode(Node parent, Coord state, String direction, Coord goal){
        double existingPathCost = 0;
        if(parent != null) existingPathCost = parent.getCost() + 1.0;

        Node newNode = new Node(state, parent, direction, super.getManhattanDistance(state, goal) + existingPathCost);
        return newNode;
    }

    @Override
    protected void insert(LinkedList<Node> frontier, Node node){
        if(super.inFrontier(frontier, node.getState())){
            for (Node currentNode : frontier) {
                if (currentNode.getState().equals(node.getState())) {
                  if (currentNode.getHeuristic() > node.getHeuristic()) {
                    frontier.remove(currentNode);
                    frontier.add(node);
                  }
                }
              }
        }else frontier.add(node);
    }

    @Override
    protected boolean inFrontier(LinkedList<Node> frontier, Coord state){
        return false;
    }

}