package searchStrategies;

import coreComponents.*;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;

public abstract class GeneralSearchAlgorithm{
    protected final Coord start;
    protected final Coord goal;
    protected final int[][] map;
    protected LinkedList<Node> frontier = new LinkedList<Node>();
    protected HashSet<String> explored = new HashSet<String>();
    protected int nodesVisited = 0;

    public GeneralSearchAlgorithm(Coord start, Coord goal, int[][] map) {
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

    protected void printFrontier(){
        System.out.print("[");

        for (int i = 0; i < frontier.size(); i++) {
            System.out.print(frontier.get(i).getState().toString());
            if(i == (frontier.size() - 1)) System.out.print("]\n");
            else System.out.print(",");
        }
    }

    protected Node makeNode(Node parent, Coord state, String direction){
        Node newNode = new Node(state, parent, direction);
        return newNode;
    }

    protected void insert(Node node){
        frontier.add(node);
    }

    protected abstract  Node remove();

    protected boolean goalTest(Coord state, Coord goal){
        if(state.equals(goal)) return true;
        return false;
    }

    protected void printSuccessToOutput(Node finalNode){
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

    protected abstract LinkedHashMap<String, Coord>  successorFn(Coord state);

    protected boolean validNeighbour(Coord state, int newY, int newX){
        int newR = state.getR() + newY;
        int newC = state.getC() + newX;

        if(newR < 0 || newR >= map.length) return false;
        if(newC < 0 || newC >= map[0].length) return false;
        if(map[newR][newC] == 1) return false;
        
        return true;
    }

    protected boolean inFrontier(Coord state){
        for (Node node: frontier) {
            if(node.getState().equals(state)) return true;
        }

        return false;
    }

    protected void printFailureToOutput(){
        System.out.println("fail");
        System.out.println(nodesVisited);
    }

}