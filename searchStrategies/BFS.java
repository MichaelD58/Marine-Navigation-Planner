package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;

public class BFS extends GeneralSearchAlgorithm{

    /**
    *  Constructor for the breadth first search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public BFS(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
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

}