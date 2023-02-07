package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;

public class BFS extends GeneralSearchAlgorithm{

    public BFS(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    @Override
    protected Node remove(){
        Node newNode = frontier.get(0);
		frontier.remove(0);

        return newNode;
    }    

}