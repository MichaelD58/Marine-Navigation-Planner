package coreComponents;

public class Coord {
	private int r;//row
	private int c;//column

	public Coord(int row,int column) {
		r=row;
		c=column;
	}

	public String toString() {
		return "(" + r + "," + c + ")";
	}

	public int getR() {
		return r;
	}
	public int getC() {
		return c;
	}

	@Override
	public boolean equals(Object o) {

		Coord coord=(Coord) o;
		if(coord.r == r && coord.c == c) return true;
		
		return false; 
	}
}
