package searchStrategies;

import coreComponents.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class BestF extends GeneralSearchAlgorithm{

    public BestF(Coord start, Coord goal, int[][] map) {
        super(start, goal, map);
    }

    @Override
    protected void printFrontier(){
        frontier = orderFrontier();
        System.out.print("[");

        for (int i = 0; i < frontier.size(); i++) {
            System.out.print(frontier.get(i).getState().toString() + ":" + frontier.get(i).getHeuristic());
            if(i == (frontier.size() - 1)) System.out.print("]\n");
            else System.out.print(",");
        }
    }

    protected Node makeNode(Node parent, Coord state, String direction){
        Node newNode = new Node(state, parent, direction, getManhattanDistance(state, goal));
        return newNode;
    }

    @Override
    protected Node remove(){
        Node newNode = frontier.get(0);
		frontier.remove(0);

        return newNode;
    }

    @Override
    protected LinkedHashMap<String, Coord>  successorFn(Coord state){
        LinkedHashMap<String, Coord> validNeighbours = new LinkedHashMap<String, Coord>();
        Boolean pointsUp;

        if((state.getR() + state.getC()) % 2 == 0) pointsUp = true;
        else pointsUp = false;

        if(super.validNeighbour(state, 0, 1)) validNeighbours.put("Right ", new Coord(state.getR(), state.getC() + 1));
        if(pointsUp && super.validNeighbour(state, 1, 0)) validNeighbours.put("Down ", new Coord(state.getR() + 1, state.getC()));
        if(super.validNeighbour(state, 0, -1)) validNeighbours.put("Left ", new Coord(state.getR(), state.getC() - 1));
        if(!(pointsUp) && super.validNeighbour(state, -1, 0)) validNeighbours.put("Up ", new Coord(state.getR() - 1, state.getC()));

        return validNeighbours;
    }

    private double getManhattanDistance(Coord state, Coord goal){
        int[] s = getTriangleValues(state);
        int[] g = getTriangleValues(goal);
        
        int a = Math.abs(s[0] - g[0]);
        int b = Math.abs(s[1] - g[1]);
        int c = Math.abs(s[2] - g[2]);

        return a + b + c;
    }

    private int[] getTriangleValues(Coord state){
        int[] triangleCoords = new int[3];
        int pointsUp;

        if((state.getR() + state.getC()) % 2 == 0) pointsUp = 0;
        else pointsUp = 1;

        triangleCoords[0] = -state.getR();
        triangleCoords[1] = (state.getR() + state.getC() - pointsUp) / 2;
        triangleCoords[2] = (state.getR() + state.getC() - pointsUp) / 2 - state.getR() + pointsUp;

        return triangleCoords;
    }


    private LinkedList<Node> orderFrontier(){
        LinkedList<Node> newFrontier = new LinkedList<Node>();
        
        while(!frontier.isEmpty()){
            Node nextNode = frontier.get(0);
            int index = 0;
            for(int j = 1; j < frontier.size(); j++){
                if(frontier.get(j).getHeuristic() < nextNode.getHeuristic()){
                    nextNode = frontier.get(j);
                    index = j;
                }else if(frontier.get(j).getHeuristic() == nextNode.getHeuristic()){
                    if(frontier.get(j).getDirection() < nextNode.getDirection()){
                        nextNode = frontier.get(j);
                        index = j;
                    }else if(frontier.get(j).getDirection() == nextNode.getDirection()){
                        if(frontier.get(j).getDepth() < nextNode.getDepth()){
                            nextNode = frontier.get(j);
                            index = j;
                        }
                    }
                }
            }
            frontier.remove(index);
            newFrontier.add(nextNode);
        }

        return newFrontier;
    }

}