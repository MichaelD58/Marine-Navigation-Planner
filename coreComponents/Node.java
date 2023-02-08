package coreComponents;

//File used to represent the nodes used within the frontier(s) of the searches
public class Node{
    private Coord state;// Coordinate representation of the state
    private Node parent;// Parent node of this node
    private double cost;// Cost to reach this node
    private int depth;// Depth of this node within the search tree
    private String pathTo;// String for the directions on how to reach this node
    private double heuristic;// Heuristic value for this node used by the intermediate searches
    private int direction;// Integer representation for the type of direction to reach this node from its parent

    //Constructor used by the basic searches that does not concern itself with the heuristic value or integer representation of
    //the direction to as these are only required within the intermediate searches
    public Node(Coord state, Node parent, String directionTo) {
        this.state = state;
        this.parent = parent;
        //Check to see if node is root node within the saerch tree as cost, depth and pathTo are handled differently
        if(parent != null){
            this.cost = parent.getCost() + 1;
            this.depth = parent.getDepth() + 1;
            this.pathTo = parent.getPathTo() + directionTo;
        }else{
            this.cost = 0;
            this.depth = 0;
            this.pathTo = "";
        }
    }

    //Constructor used by the intermediate searches
    public Node(Coord state, Node parent, String directionTo, double heuristic) {
        this.state = state;
        this.parent = parent;
        //Check to see if node is root node within the saerch tree as cost, depth and pathTo are handled differentl
        if(parent != null){
            this.cost = parent.getCost() + 1;
            this.depth = parent.getDepth() + 1;
            this.pathTo = parent.getPathTo() + directionTo;
        }else{
            this.cost = 0;
            this.depth = 0;
            this.pathTo = "";
        }
        //Direction to the node from parent node String is converted into its integer representation here
        if(directionTo != null){
            if(directionTo.equals("Right ")) this.direction = 0;
            else if(directionTo.equals("Down ")) this.direction = 1;
            else if(directionTo.equals("Left ")) this.direction = 2;
            else this.direction = 3;
        }
        this.heuristic = heuristic;
    }

    /**
    *  Method used to return the node's state
    *  @return Coord object representing the node's state
    */ 
    public Coord getState() {
		return state;
	}

    /**
    *  Method used to return the node's parent node
    *  @return Node object representing the node's parent node
    */ 
	public Node getParent() {
		return parent;
	}

    /**
    *  Method used to return the node's depth within the search tree
    *  @return Double value for the cost required to get to this node from the start point.
    */ 
    public double getCost() {
		return cost;
	}

    /**
    *  Method used to return the node's depth within the search tree
    *  @return Integer value for the node's depth value.
    */ 
	public int getDepth() {
		return depth;
	}

    /**
    *  Method used to return the directions required to reach this node from the start point
    *  @return String representing the path variable that is used for the output at the end of a successful search
    */ 
    public String getPathTo() {
		return pathTo;
	}

    /**
    *  Method used to return the node's heuristic value. Used only by the intermediate searches
    *  @return Double value for the node's heuristic value.
    */ 
    public double getHeuristic() {
		return heuristic;
	}

    /**
    *  Method used to return the node's direction integer
    *  @return Integer used to represent the direction required to get to node from parent node
    */ 
    public int getDirection() {
		return direction;
	}
}