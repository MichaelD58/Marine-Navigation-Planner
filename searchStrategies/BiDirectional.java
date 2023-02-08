package searchStrategies;

import coreComponents.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;

public class BiDirectional{
    private final Coord start;
    private final Coord goal;
    private final int[][] map;
    private LinkedList<Node> frontier = new LinkedList<Node>();
    private HashMap<String, Node> explored = new HashMap<String, Node>();
    private LinkedList<Node> frontierFromGoal = new LinkedList<Node>();
    private HashMap<String, Node> exploredFromGoal = new HashMap<String, Node>();
    private int nodesVisited = 0;

    public BiDirectional(Coord start, Coord goal, int[][] map) {
        this.start = start;
        this.goal = goal;
        this.map = map;
    }

    public void search(){
        Node startingNode = makeNode(null, start);
        Node finishNode = makeNode(null, goal);
        insert(frontier, startingNode);
        insert(frontierFromGoal, finishNode);

        while(!(frontier.isEmpty()) || !(frontierFromGoal.isEmpty())){
            printFrontiers();
            if(!(frontier.isEmpty()))actOnFrontier(frontier, goal, explored, exploredFromGoal);
            if(!(frontierFromGoal.isEmpty()))actOnFrontier(frontierFromGoal, start, exploredFromGoal, explored);
        }
        
        printFailureToOutput();
    }

    private void printFrontiers(){
        System.out.print("Forwards:[");
        if(!(frontier.isEmpty())){
            for (int i = 0; i < frontier.size(); i++) {
                System.out.print(frontier.get(i).getState().toString());
                if(i != (frontier.size() - 1))System.out.print(",");
            }
        }
        System.out.print("]\n");
        
        System.out.print("Backwards:[");
        if(!(frontierFromGoal.isEmpty())){
            for (int i = 0; i < frontierFromGoal.size(); i++) {
                System.out.print(frontierFromGoal.get(i).getState().toString());
                if(i != (frontierFromGoal.size() - 1))System.out.print(",");
            }
        }
        System.out.print("]\n");
    }

    private Node makeNode(Node parent, Coord state){
        Node newNode = new Node(state, parent, "");
        return newNode;
    }

    private void insert(LinkedList<Node> frontier, Node node){
        frontier.add(node);
    }

    private void printSuccessToOutput(Node connectingNode, HashMap<String,Node> otherExplored){
        Coord connectState = connectingNode.getState();
        Node connectingNodeSecond = otherExplored.get(connectState.toString());
        
        double firstCost = connectingNode.getCost();
        double secondCost = connectingNodeSecond.getCost();
        double finalCost = firstCost + secondCost;

        boolean pathLeadsToGoal;
        Node tempNode = connectingNode;

        while (tempNode.getParent() != null) {
            Node nextNode = tempNode.getParent();
            tempNode = nextNode;
        }

        if(tempNode.getState().toString().equals(goal.toString()))pathLeadsToGoal = true;
        else pathLeadsToGoal = false;

        System.out.println(pathLeadsToGoal);

        StringBuilder path = new StringBuilder();
        ArrayList<String> tempOutputForDirections = new ArrayList<String>();

        if(pathLeadsToGoal){
            while (connectingNode.getParent() != null) {
                path.insert(path.length(), connectingNode.getState().toString());
                Node nextNode = connectingNode.getParent();
                connectingNode = nextNode;
            }
            path.insert(path.length(), connectingNode.getState().toString());
            
            while (connectingNodeSecond.getParent() != null) {
                tempOutputForDirections.add(connectingNodeSecond.getState().toString());
                Node nextNode = connectingNodeSecond.getParent();
                connectingNodeSecond = nextNode;
            }
            tempOutputForDirections.add(connectingNodeSecond.getState().toString());

            for(int i = 1; i < tempOutputForDirections.size(); i++){// Starts at the index 1 instead of 0 to prevent the middle coordinate from being printed twice
                path.insert(0, tempOutputForDirections.get(i));
            }
        }else{
            while (connectingNodeSecond.getParent() != null) {
                path.insert(path.length(), connectingNodeSecond.getState().toString());
                Node nextNode = connectingNodeSecond.getParent();
                connectingNodeSecond = nextNode;
            }
            path.insert(path.length(), connectingNodeSecond.getState().toString());
            
            while (connectingNode.getParent() != null) {
                tempOutputForDirections.add(connectingNode.getState().toString());
                Node nextNode = connectingNode.getParent();
                connectingNode = nextNode;
            }
            tempOutputForDirections.add(connectingNode.getState().toString());

            for(int i = 1; i < tempOutputForDirections.size(); i++){// Starts at the index 1 instead of 0 to prevent the middle coordinate from being printed twice
                path.insert(0, tempOutputForDirections.get(i));
            }
        }

        System.out.println(path);
        System.out.println(getPathTo(path.toString()));
        System.out.println(finalCost);
        System.out.println(nodesVisited--);
        System.exit(0);
    }

    private LinkedHashMap<String, Coord>  successorFn(Coord state){
        LinkedHashMap<String, Coord> validNeighbours = new LinkedHashMap<String, Coord>();
        Boolean pointsUp;

        if((state.getR() + state.getC()) % 2 == 0) pointsUp = true;
        else pointsUp = false;

        if(validNeighbour(state, 0, 1)) validNeighbours.put("Right ", new Coord(state.getR(), state.getC() + 1));
        if(pointsUp && validNeighbour(state, 1, 0)) validNeighbours.put("Down ", new Coord(state.getR() + 1, state.getC()));
        if(validNeighbour(state, 0, -1)) validNeighbours.put("Left ", new Coord(state.getR(), state.getC() - 1));
        if(!(pointsUp) && validNeighbour(state, -1, 0)) validNeighbours.put("Up ", new Coord(state.getR() - 1, state.getC()));

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

    private void printFailureToOutput(){
        System.out.println("\nfail");
        System.out.println(nodesVisited);
    } 

    private Node remove(LinkedList<Node> frontier){
        Node newNode = frontier.get(0);
		frontier.remove(0);

        return newNode;
    }

    private void actOnFrontier(LinkedList<Node> frontier, Coord goal,  HashMap<String, Node> explored,  HashMap<String, Node> otherExplored){
        Node nextNode = remove(frontier);
        nodesVisited++;
        explored.put(nextNode.getState().toString(), nextNode);

        for (Map.Entry<String, Coord> entry : successorFn(nextNode.getState()).entrySet()) {
            String direction = entry.getKey();
            Coord state = entry.getValue();
                    
            if(otherExplored.containsKey(state.toString())){
                Node connectingNode = makeNode(nextNode, state);
                printSuccessToOutput(connectingNode, otherExplored);
            }else if(!(explored.containsKey(state.toString()))){
                Node createdNode = makeNode(nextNode, state);
                insert(frontier, createdNode);
            }
        } 
    }

    private String getPathTo(String path){
        String directions = "";

        String[] splitString = path.split(",");
        
        int currentY = Character.getNumericValue(splitString[0].charAt(splitString[0].length() - 1));
        int currentX = Character.getNumericValue(splitString[1].charAt(0));
        for(int i = 1; i < splitString.length - 1; i++){
            int newY = Character.getNumericValue(splitString[i].charAt(splitString[i].length() - 1));
            int newX = Character.getNumericValue(splitString[i + 1].charAt(0));
            
            if(newY > currentY) directions += "Down ";
            else if (newY < currentY) directions += "Up ";
            else if (newX > currentX) directions += "Right ";
            else directions += "Left ";

            currentY = newY;
            currentX = newX;
        }

        return directions;
    }


}