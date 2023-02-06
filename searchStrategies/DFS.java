package searchStrategies;

import coreComponents.*;

import java.util.Stack;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class DFS{
    private final Coord start;
    private final Coord goal;
    private final int[][] map;
    private Stack<Node> frontier = new Stack<Node>();
    private HashSet<String> explored = new HashSet<String>();
    private int nodesVisited = 0;

    public DFS(Coord start, Coord goal, int[][] map) {
        this.start = start;
        this.goal = goal;
        this.map = map;
    }

    public void search(){
        Node startingNode = makeNode(null, start, null);
        insert(startingNode);

        while(!(frontier.isEmpty())){
            printFrontier();
            Node nextNode = remove();
            nodesVisited++;
            explored.add(nextNode.getState().toString());

            if(goalTest(nextNode.getState(), goal)){
                printSuccessToOutput(nextNode);
            }else{
                for (Map.Entry<String, Coord> entry : successorFn(nextNode.getState()).entrySet()) {
                    String direction = entry.getKey();
                    Coord state = entry.getValue();
                    
                    if(!(explored.contains(state.toString()))){
                        if(!(inFrontier(state))){
                            Node createdNode = makeNode(nextNode, state, direction);
                            insert(createdNode);
                        } 
                    }
                }      
            }
        }

        printFailureToOutput();
    }

    private void printFrontier(){
        System.out.print("[");
        Node[] frontierOutput = frontier.toArray(new Node[frontier.size()]);

        for (int i = 0; i < frontierOutput.length; i++) {
            System.out.print(frontierOutput[i].getState().toString());
            if(i == (frontierOutput.length - 1)) System.out.print("]\n");
            else System.out.print(",");
        }
    }

    private Node makeNode(Node parent, Coord state, String direction){
        Node newNode = new Node(state, parent, direction);
        return newNode;
    }

    private void insert(Node node){
        frontier.add(node);
    }

    private Node remove(){
        Node node = frontier.pop();

        return node;
    }

    private boolean goalTest(Coord state, Coord goal){
        if(state.equals(goal)) return true;
        return false;
    }

    private void printSuccessToOutput(Node finalNode){
        double cost = finalNode.getCost();
        String directionString = finalNode.getPathTo();
        StringBuilder path = new StringBuilder();

        while (finalNode.getParent() != null) {
            path.insert(0, finalNode.getState().toString());
            Node nextNode = finalNode.getParent();
            finalNode = nextNode;
        }
        path.insert(0, finalNode.getState().toString());
        
        System.out.println(path);
        System.out.println(directionString);
        System.out.println(cost);
        System.out.println(nodesVisited);

        System.exit(0);
    }

    private LinkedHashMap<String, Coord>  successorFn(Coord state){
        LinkedHashMap<String, Coord> validNeighbours = new LinkedHashMap<String, Coord>();
        Boolean pointsUp;

        if((state.getR() + state.getC()) % 2 == 0) pointsUp = true;
        else pointsUp = false;

        if(!(pointsUp) && validNeighbour(state, -1, 0)) validNeighbours.put("Up ", new Coord(state.getR() - 1, state.getC()));
        if(validNeighbour(state, 0, -1)) validNeighbours.put("Left ", new Coord(state.getR(), state.getC() - 1));
        if(pointsUp && validNeighbour(state, 1, 0)) validNeighbours.put("Down ", new Coord(state.getR() + 1, state.getC()));
        if(validNeighbour(state, 0, 1)) validNeighbours.put("Right ", new Coord(state.getR(), state.getC() + 1));

        return validNeighbours;
    }

    private boolean validNeighbour(Coord state, int newY, int newX){
        int newR = state.getR() + newY;
        int newC = state.getC() + newX;

        if(newR < 0 || newR >= map.length) return false;
        if(newC < 0 || newC >= map[0].length) return false;
        if(map[newR][newC] == 1) return false;
        
        return true;
    }

    private boolean inFrontier(Coord state){
        Node[] frontierArray = frontier.toArray(new Node[frontier.size()]);

        for (Node node: frontierArray) {
            if(node.getState().equals(state)) return true;
        }

        return false;
    }

    private void printFailureToOutput(){
        System.out.println("fail");
        System.out.println(nodesVisited);
    }

}