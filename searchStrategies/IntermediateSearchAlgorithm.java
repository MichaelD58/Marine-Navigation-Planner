package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public abstract class IntermediateSearchAlgorithm extends GeneralSearchAlgorithm{

    /**
    *  Constructor for the generic intermediate search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public IntermediateSearchAlgorithm(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    /**
    *  The method that prints the contents of the frontier. This has to be overwritten for the intermediate searhces as the heuristic value must also be displayed
    */
    @Override
    protected void printFrontier(){
        frontier = orderFrontier(frontier);
        System.out.print("[");

        for (int i = 0; i < frontier.size(); i++) {
            System.out.print(frontier.get(i).getState().toString() + ":" + frontier.get(i).getHeuristic());
            if(i == (frontier.size() - 1)) System.out.print("]\n");
            else System.out.print(",");
        }
    }

    /**
    *  Method that creates a new node instance. This is abstract as both intermediate searches create nodes differently
    *  @parameter parent Parent node for the new node
    *  @parameter state Coordinates of the node's state
    *  @parameter direction Direction String for how to reach the node from its parent
    *  @parameter goal Coordinates of the goal
    */
    @Override
    protected abstract Node makeNode(Node parent, Coord state, String direction, Coord goal);

    /**
    *  Method used to acquire all valid neighbours for the node currently being looked at
    *  @parameter state The state whose valid neighbours are being acquired
    *  @return Ordered HashMap of the direction-state pairs of valid neighbours to the state
    */
    @Override
    protected LinkedHashMap<String, Coord>  successorFn(Coord state){
        LinkedHashMap<String, Coord> validNeighbours = new LinkedHashMap<String, Coord>();// Ordered HashMap of all valid neigbours is created
        //Boolean that checks to see which directon the coordinate is pointing is created and assigned a value based on the value of its coordinates
        Boolean pointsUp;
        if((state.getR() + state.getC()) % 2 == 0) pointsUp = true;
        else pointsUp = false;

        // Each of the coordinates neighbours are check to see if they are valid, before being added to the HashMap in the order of the tie-breaking sequence
        if(validNeighbour(state, 0, 1)) validNeighbours.put("Right ", new Coord(state.getR(), state.getC() + 1));
        if(pointsUp && validNeighbour(state, 1, 0)) validNeighbours.put("Down ", new Coord(state.getR() + 1, state.getC()));
        if(validNeighbour(state, 0, -1)) validNeighbours.put("Left ", new Coord(state.getR(), state.getC() - 1));
        if(!(pointsUp) && validNeighbour(state, -1, 0)) validNeighbours.put("Up ", new Coord(state.getR() - 1, state.getC()));

        return validNeighbours;// Valid list of neighbours is returned
    }

    /**
    *  Method that removes a node from the frontier to then be acted on
    */
    @Override
    protected Node remove(){
        //First node within the frontier is acquired
        Node newNode = frontier.get(0);
		frontier.remove(0);

        return newNode;
    }

    /**
    *  Method used to get the manhattan distance value between a coordinate and the goal coordinate
    *  @parameter state Current coordinate
    *  @parameter goal Goal coordinate
    *  @return Manhattan distance value
    */
    protected double getManhattanDistance(Coord state, Coord goal){
        int[] s = getTriangleValues(state);// Three values for the state's triangle coordinates are acquired through method call
        int[] g = getTriangleValues(goal);// Three values for the goal's triangle coordinates are acquired through method call
        
        //Absolute values are acquired for the distances between each of the three pairs of coordinate values
        int a = Math.abs(s[0] - g[0]);
        int b = Math.abs(s[1] - g[1]);
        int c = Math.abs(s[2] - g[2]);

        return a + b + c;// Manhattan distane is returned
    }

    /**
    *  Method that acquires the triangle coordinate values for a state
    *  @parameter state The state whose triangle coordinate values are being acquired
    *  @return integer array containing the three values
    */
    protected int[] getTriangleValues(Coord state){
        int[] triangleCoords = new int[3];
        //Boolean that checks to see which directon the coordinate is pointing is created and assigned a value based on the value of its coordinates
        int pointsUp;
        if((state.getR() + state.getC()) % 2 == 0) pointsUp = 0;
        else pointsUp = 1;

        //Triangle values for the three coordinates are acquired according to the logic of the lecture slides: https://studres.cs.st-andrews.ac.uk/CS5011/Lectures/L4-w2-Search-3.pdf
        triangleCoords[0] = -state.getR();
        triangleCoords[1] = (state.getR() + state.getC() - pointsUp) / 2;
        triangleCoords[2] = (state.getR() + state.getC() - pointsUp) / 2 - state.getR() + pointsUp;

        return triangleCoords;// Triangle coordinate values are returned
    }

    /**
    *  Method used to order the frontier in accordance with the informed laws
    *  @parameter frontier The intial frontier
    *  @return The newly ordered frontier
    */
    protected LinkedList<Node> orderFrontier(LinkedList<Node> frontier){
        LinkedList<Node> newFrontier = new LinkedList<Node>();
        
        while(!frontier.isEmpty()){// Whilst there are still nodes to move to the new ordered frontier
            Node nextNode = frontier.get(0);// First node is acquired
            int index = 0;// Index for the best node is created and set to the first node's index
            for(int j = 1; j < frontier.size(); j++){// For each node within the unordered frontier
                if(frontier.get(j).getHeuristic() < nextNode.getHeuristic()){ //Check 1: Does the new node have a lower heuristic value than the current best node?
                    // New best node is set
                    nextNode = frontier.get(j);
                    index = j;
                }else if(frontier.get(j).getHeuristic() == nextNode.getHeuristic()){// If nodes have the same heuristic value
                    if(frontier.get(j).getDirection() < nextNode.getDirection()){//Check 2: Does the new node have a lower direction value than the current best node?
                        // New best node is set
                        nextNode = frontier.get(j);
                        index = j;
                    }else if(frontier.get(j).getDirection() == nextNode.getDirection()){// If nodes have the same direction value
                        if(frontier.get(j).getDepth() < nextNode.getDepth()){//Check 1: Does the new node have a smaller depth value than the current best node?
                            // New best node is set
                            nextNode = frontier.get(j);
                            index = j;
                        }
                    }
                }
            }
            // Best node candidate is added to the new frontier and removed from the old one
            frontier.remove(index);
            newFrontier.add(nextNode);
        }

        return newFrontier;// Ordered frontier is returned
    }

}