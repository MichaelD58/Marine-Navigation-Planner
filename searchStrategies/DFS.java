package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;

public class DFS extends GeneralSearchAlgorithm{

    public DFS(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }
 
    @Override
    protected Node remove(){
        Node newNode = frontier.get(frontier.size() - 1);
		frontier.remove(frontier.size() - 1);

        return newNode;
    }

    @Override
    protected LinkedHashMap<String, Coord>  successorFn(Coord state){
        LinkedHashMap<String, Coord> validNeighbours = new LinkedHashMap<String, Coord>();
        Boolean pointsUp;

        if((state.getR() + state.getC()) % 2 == 0) pointsUp = true;
        else pointsUp = false;

        if(!(pointsUp) && super.validNeighbour(state, -1, 0)) validNeighbours.put("Up ", new Coord(state.getR() - 1, state.getC()));
        if(super.validNeighbour(state, 0, -1)) validNeighbours.put("Left ", new Coord(state.getR(), state.getC() - 1));
        if(pointsUp && super.validNeighbour(state, 1, 0)) validNeighbours.put("Down ", new Coord(state.getR() + 1, state.getC()));
        if(super.validNeighbour(state, 0, 1)) validNeighbours.put("Right ", new Coord(state.getR(), state.getC() + 1));

        return validNeighbours;
    }

}