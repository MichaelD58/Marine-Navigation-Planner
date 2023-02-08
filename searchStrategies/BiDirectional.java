package searchStrategies;

import coreComponents.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;

//File used to represent the bidirectional search algorithm
public class BiDirectional{
    private final Coord start;// Start coordinate for the search
    private final Coord goal;// Goal coordinate for the search
    private final int[][] map;// Map that the search is working on
    private LinkedList<Node> frontier = new LinkedList<Node>();// Frontier used to store the nodes that can be accessed by the front search
    private HashMap<String, Node> explored = new HashMap<String, Node>();// Frontier used to store the nodes that can be accessed by the end search
    private LinkedList<Node> frontierFromGoal = new LinkedList<Node>();// HashMap used to store the coordinates that have already been visited by the front search
    private HashMap<String, Node> exploredFromGoal = new HashMap<String, Node>();// HashMap used to store the coordinates that have already been visited by the end search
    private int nodesVisited = 0;

    /**
    *  Constructor for the bidirectional search algorithm
    *  @parameter start Start coordinate for the search
    *  @parameter goal Goal coordinate for the search
    *  @parameter map Map that the search is working on
    */
    public BiDirectional(Coord start, Coord goal, int[][] map) {
        this.start = start;
        this.goal = goal;
        this.map = map;
    }

    /**
    *  The search algorithm that gets called within the A1Main class
    */
    public void search(){
        Node startingNode = makeNode(null, start);// Node is created from the starting coordinate
        Node finishNode = makeNode(null, goal);// Node is created from the goal coordinate
        insert(frontier, startingNode);// Starting node is inserted into the frontier
        insert(frontierFromGoal, finishNode);// Closing node is inserted into the frontier

        while(!(frontier.isEmpty()) || !(frontierFromGoal.isEmpty())){// Whilst either frontier still contains nodes
            printFrontiers();// Print frontiers' contents for output
            if(!(frontier.isEmpty()))actOnFrontier(frontier, explored, exploredFromGoal);//If start frontier is not empty, then act on node within
            if(!(frontierFromGoal.isEmpty()))actOnFrontier(frontierFromGoal, exploredFromGoal, explored);//If end frontier is not empty, then act on node within
        }
        
        printFailureToOutput();// If this line is reached, then the frontier is empty and thus the fail message is displayed
    }

    /**
    *  Method that creates a new node instance
    *  @parameter parent Parent node for the new node
    *  @parameter state Coordinates of the node's state
    *  @return Newly created node instance
    */
    private Node makeNode(Node parent, Coord state){
        Node newNode = new Node(state, parent, "");
        return newNode;
    }

    /**
    *  Method that adds a node to the frontier
    *  @parameter frontier Frontier that the node will be inserted into
    *  @parameter node Node to be inserted intop the frontier
    */
    private void insert(LinkedList<Node> frontier, Node node){
        frontier.add(node);
    }

    /**
    *  The method that prints the contents of the frontiers
    */
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

    /**
    *  Method used pull nodes from a frontier and act upon them
    *  @parameter frontier The frontier having a node removed from it
    *  @parameter explored The state whose valid neighbours are being acquired
    *  @parameter otherExplored The state whose valid neighbours are being acquired
    */
    private void actOnFrontier(LinkedList<Node> frontier,  HashMap<String, Node> explored,  HashMap<String, Node> otherExplored){
        Node nextNode = remove(frontier);// New node is acquired from the frontier
        nodesVisited++;// Number of nodes visited is incremented
        explored.put(nextNode.getState().toString(), nextNode);//State of new node is added to this search's explored data structure

        for (Map.Entry<String, Coord> entry : successorFn(nextNode.getState()).entrySet()) {// For each valid neighbouring state of the node
            String direction = entry.getKey();// Entry key is the direction from the node to the new coordinate
            Coord state = entry.getValue();// Entry value is the neighbouring state
                    
            if(otherExplored.containsKey(state.toString())){// If the neighbouring state has been explored by the other search
                Node connectingNode = makeNode(nextNode, state);// Connecting node created from the state
                printSuccessToOutput(connectingNode, otherExplored);// Successful search output called with the connecting node
            }else if(!(explored.containsKey(state.toString()))){// If the neighbouring state has not been explored by this search
                Node createdNode = makeNode(nextNode, state);// New node is created
                insert(frontier, createdNode);// New node is inserted into the frontier
            }
        } 
    }

    /**
    *  Method that removes a node from the frontier to then be acted on
    *  @parameter frontier The frontier that is to have a node remoced from it
    *  @return The node that has been removed from the frontier.
    */
    private Node remove(LinkedList<Node> frontier){
        Node newNode = frontier.get(0);
		frontier.remove(0);

        return newNode;
    }

    /**
    *  Method used to acquire all valid neighbours for the node currently being looked at
    *  @parameter state The state whose valid neighbours are being acquired
    *  @return Ordered HashMap of the direction-state pairs of valid neighbours to the state
    */
    private LinkedHashMap<String, Coord>  successorFn(Coord state){
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
    *  Method used to print out the results of a successful bidirectional search
    *  @parameter connectingNode The node of the state that has been explored by both searches
    *  @parameter otherExplored The other search's explored data structure
    */
    private void printSuccessToOutput(Node connectingNode, HashMap<String,Node> otherExplored){
        Coord connectState = connectingNode.getState();// The connecting state is acquired from the connecting node
        Node connectingNodeSecond = otherExplored.get(connectState.toString());// The node of the other search for the same state is acquired from the other search's explored data structure
        
        // The final cost is created from combining the costs of both nodes of the same state, creating a total cost between start and goal
        double firstCost = connectingNode.getCost();
        double secondCost = connectingNodeSecond.getCost();
        double finalCost = firstCost + secondCost;

        boolean pathLeadsToGoal;// Boolean that checks to see if one of the nodes terminates at the start or goal coordinate is created
        Node tempNode = connectingNode;// Temp node that is used for traversal is created from this node that is to be checked

        //Whilst the node has a parent, the node's parent is accessed. This is used to access the root node of one of the searches
        while (tempNode.getParent() != null) {
            Node nextNode = tempNode.getParent();
            tempNode = nextNode;
        }

        //Check is carried out to see if the root node's state is that of the goal state
        if(tempNode.getState().toString().equals(goal.toString()))pathLeadsToGoal = true;
        else pathLeadsToGoal = false;

        StringBuilder path = new StringBuilder();// Stringbuilder used to generate the path string is created
        ArrayList<String> tempOutputForDirections = new ArrayList<String>();// ArrayList used for the flipping of one of the paths is created

        if(pathLeadsToGoal){// If first path terminates in the goal
            // Path coordinates are added to the end of the String until there are no more to be added
            while (connectingNode.getParent() != null) {
                path.insert(path.length(), connectingNode.getState().toString());
                Node nextNode = connectingNode.getParent();
                connectingNode = nextNode;
            }
            path.insert(path.length(), connectingNode.getState().toString());
            
            // Path coordinates of the other search are added to the temp ArrayList until there are no more to be added
            while (connectingNodeSecond.getParent() != null) {
                tempOutputForDirections.add(connectingNodeSecond.getState().toString());
                Node nextNode = connectingNodeSecond.getParent();
                connectingNodeSecond = nextNode;
            }
            tempOutputForDirections.add(connectingNodeSecond.getState().toString());

            // Path coordinates throughout the ArrayList are added to the start of the string
            for(int i = 1; i < tempOutputForDirections.size(); i++){// Starts at the index 1 instead of 0 to prevent the middle coordinate from being printed twice
                path.insert(0, tempOutputForDirections.get(i));
            }
        }else{
            // Path coordinates are added to the end of the String until there are no more to be added
            while (connectingNodeSecond.getParent() != null) {
                path.insert(path.length(), connectingNodeSecond.getState().toString());
                Node nextNode = connectingNodeSecond.getParent();
                connectingNodeSecond = nextNode;
            }
            path.insert(path.length(), connectingNodeSecond.getState().toString());
            
            //Path coordinates of the other search are added to the temp ArrayList until there are no more to be added
            while (connectingNode.getParent() != null) {
                tempOutputForDirections.add(connectingNode.getState().toString());
                Node nextNode = connectingNode.getParent();
                connectingNode = nextNode;
            }
            tempOutputForDirections.add(connectingNode.getState().toString());

            // Path coordinates throughout the ArrayList are added to the start of the string
            for(int i = 1; i < tempOutputForDirections.size(); i++){// Starts at the index 1 instead of 0 to prevent the middle coordinate from being printed twice
                path.insert(0, tempOutputForDirections.get(i));
            }
        }

        // Output to match the outputs of the other searches is produced
        System.out.println(path);
        System.out.println(getPathTo(path.toString()));// Direction String is created from the path String
        System.out.println(finalCost);
        System.out.println(nodesVisited);

        //Since a succssful output has been created, the execution of the search function can be stopped
        System.exit(0);
    }

    /**
    *  Method used to create the direction String from the path String
    *  @parameter path String of the coordinates taken in the search
    *  @return String representing the directions taken to get to the goal from the start coordinate
    */
    private String getPathTo(String path){
        String directions = "";// Direction string for return value is created

        String[] splitString = path.split(",");// Path String is split at the commas
        
        int currentY = Character.getNumericValue(splitString[0].charAt(splitString[0].length() - 1));// Start coordinate Y value is acquired
        int currentX = Character.getNumericValue(splitString[1].charAt(0));// Start coordinate X value is acquired
        for(int i = 1; i < splitString.length - 1; i++){
            int newY = Character.getNumericValue(splitString[i].charAt(splitString[i].length() - 1));//Next coordinate's Y value is acquired
            int newX = Character.getNumericValue(splitString[i + 1].charAt(0));//Next coordinate's X value is acquired
            
            //Checks carried out to find what direction is taken between the previous coordinate and the new coordinate, with the result being added to the direction String
            if(newY > currentY) directions += "Down ";
            else if (newY < currentY) directions += "Up ";
            else if (newX > currentX) directions += "Right ";
            else directions += "Left ";

            currentY = newY;// Current Y updated to new Y and loop repeats
            currentX = newX;// Current X updated to new Y and loop repeats
        }

        return directions;// Direction String is returned
    }

    /**
    *  Method that produces the failed search output
    */
    private void printFailureToOutput(){
        System.out.println("\nfail");
        System.out.println(nodesVisited);
    } 
}