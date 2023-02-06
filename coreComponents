package coreComponents;

public class Node{
    private Coord state;
    private Node parent;
    private double cost;
    private int depth;
    private String pathTo;


    public Node(Coord state, Node parent, String directionTo) {
        this.state = state;
        this.parent = parent;
        if(parent != null){
            this.cost = parent.getCost() + 1;
            this.depth = parent.getDepth() + 1;
            this.pathTo = parent.getPathTo() + " " + directionTo;
        }else{
            this.cost = 0;
            this.depth = 0;
            this.pathTo = "";
        }
        
    }

    public Coord getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

    public double getCost() {
		return cost;
	}

	public int getDepth() {
		return depth;
	}

    public String getPathTo() {
		return pathTo;
	}
}
