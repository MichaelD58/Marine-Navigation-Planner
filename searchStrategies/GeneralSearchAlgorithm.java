package searchStrategies;

import coreComponents.*;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;

//File used to represent the generic search algorithm structure
public abstract class GeneralSearchAlgorithm{
    protected final Coord start;// Start coordinate for the search
    protected final Coord goal;// Goal coordinate for the search
    protected final int[][] map;// Map that the search is working on
    protected LinkedList<Node> frontier = new LinkedList<Node>();// Frontier used to store the nodes that can be accessed by the search
    protected HashSet<String> explored = new HashSet<String>();// HashSet used to store the coordinates that have already been visited by the search
    protected int nodesVisited = 0;// Counter used to track the number of nodes visited by the search

    /**
    *  Constructor for the generic search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public GeneralSearchAlgorithm(Coord start, Coord goal, int[][] map) {
        this.start = start;
        this.goal = goal;
        this.map = map;
    }

    /**
    *  The search algorithm that gets called within the A1Main class
    */
    public void search(){
        Node startingNode = makeNode(null, start, null, goal);// Node is created from the starting coordinate
        insert(frontier, startingNode);// Starting node is inserted into the frontier

        while(!(frontier.isEmpty())){// Whilst the frontier still contains nodes
            printFrontier();// Print frontier contents for output
            Node nextNode = remove();// Next node is acquired from the frontier using the remove method
            nodesVisited++;// Number of nodes visited is incremented
            explored.add(nextNode.getState().toString());// New node's coordinates is added to the explored data structure

            if(goalTest(nextNode.getState(), goal)) printSuccessToOutput(nextNode);// If new node's coordinates is that of the goal coordinates the print succesful output
            else{
                for (Map.Entry<String, Coord> entry : successorFn(nextNode.getState()).entrySet()){// Series of neighbouring coordinates is acquired from the successorFN method
                    String direction = entry.getKey();// Key of entry is the direction to new coordinate
                    Coord state = entry.getValue();// Value of entry is the state of the new coordinate
                    
                    if(!(explored.contains(state.toString()))){// If coordinate has not been explored already
                        if(!(inFrontier(frontier, state))){// If coordinate is not currently within the frontier as node
                            Node createdNode = makeNode(nextNode, state, direction, goal);// New node created for the neighbour coordinate 
                            insert(frontier, createdNode);// New node inserted into the frontier
                        } 
                    }
                }      
            }
        }

        printFailureToOutput();// If this line is reached, then the frontier is empty and thus the fail message is displayed
    }

    /**
    *  The method that prints the contents of the frontier
    */
    protected void printFrontier(){
        System.out.print("[");

        for (int i = 0; i < frontier.size(); i++) {
            System.out.print(frontier.get(i).getState().toString());//State of the node is sent to the display
            // If this is the last node in the frontier, then the closing bracket is printed, else a comma is printed for the next coordinate to follow
            if(i == (frontier.size() - 1)) System.out.print("]\n");
            else System.out.print(",");
        }
    }

    /**
    *  Method that creates a new node instance
    *  @parameter parent Parent node for the new node
    *  @parameter state Coordinates of the node's state
    *  @parameter direction Direction String for how to reach the node from its parent
    *  @parameter goal Coordinates of the goal
    *  @return Newly created node instance
    */
    protected Node makeNode(Node parent, Coord state, String direction, Coord goal){
        Node newNode = new Node(state, parent, direction);
        return newNode;
    }

    /**
    *  Method that adds a node to the frontier
    *  @parameter frontier Frontier that the node will be inserted into
    *  @parameter node Node to be inserted intop the frontier
    */
    protected void insert(LinkedList<Node> frontier, Node node){
        frontier.add(node);
    }

    /**
    *  Method that removes a node from the frontier to then be acted on
    */
    protected abstract  Node remove();

    /**
    *  Method that checks if current state is the goal state
    *  @parameter state The current state being looked at
    *  @parameter goal The goal state for the search
    *  @return Whether the current state is the same as the goal state
    */
    protected boolean goalTest(Coord state, Coord goal){
        if(state.equals(goal)) return true;
        return false;
    }

    /**
    *  Method that produces the successful search output
    *  @parameter finalNode The final node of the succesful search
    */
    protected void printSuccessToOutput(Node finalNode){
        double cost = finalNode.getCost();// The final cost output for the search
        String directionString = finalNode.getPathTo();// The direction output for the search
        StringBuilder path = new StringBuilder();// Stringbuilder used to output the path for the search

        //Every node in the chain of nodes from finalNode to the start node has their coordinates added to the start of the stringbuilder
        while (finalNode.getParent() != null) {
            path.insert(0, finalNode.getState().toString());
            Node nextNode = finalNode.getParent();
            finalNode = nextNode;
        }
        path.insert(0, finalNode.getState().toString());
        
        // Outputs required for this practical are printed
        System.out.println(path);
        System.out.println(directionString);
        System.out.println(cost);
        System.out.println(nodesVisited);

        System.exit(0);// Successful output so execution is stopped
    }

    /**
    *  Method that acquires the valid neighbour coordinates of a state
    *  @parameter state The state whose neighbours are to be acquired
    */
    protected abstract LinkedHashMap<String, Coord>  successorFn(Coord state);

    /**
    *  Method that determines if a newly acquired coordinate is valid
    *  @parameter state Coordinates of the state whose neighbour is being checked
    *  @parameter newY The difference to the Y coordinate of the current state to get ther neighbour
    *  @parameter newX The difference to the X coordinate of the current state to get ther neighbour
    *  @return Whethwe coordinate is valid
    */
    protected boolean validNeighbour(Coord state, int newY, int newX){
        int newR = state.getR() + newY;// Neighbour's Y coordinate is acquired
        int newC = state.getC() + newX;// Neighbour's X coordinate is acquired

        if(newR < 0 || newR >= map.length) return false;// If neighbour's Y coordinate is outwith the limits return false
        if(newC < 0 || newC >= map[0].length) return false;// If neighbour's X coordinate is outwith the limits return false
        if(map[newR][newC] == 1) return false;// If neighbour's is an island (unaccessible) return false
        
        return true;
    }

    /**
    *  Method that checks to see if a state is already in the frontier in the form of a node's state
    *  @parameter frontier The frontier to be checked
    *  @parameter state The state being checked for
    *  @return Whether node is within the frontier
    */
    protected boolean inFrontier(LinkedList<Node> frontier, Coord state){
        // For each node within the frontier, if its state equals that of the one being searched for, return true
        for (Node node : frontier) {
            if(node.getState().equals(state)) return true;
        }

        return false;
    }
    
    /**
    *  Method that produces the failed search output
    */
    protected void printFailureToOutput(){
        System.out.println("\nfail");
        System.out.println(nodesVisited);
    }

}