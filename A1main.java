import searchStrategies.*;
import coreComponents.*;

public class A1main {

	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Incorrect formatting. Please use 'java A1main <DFS|BFS|AStar|BestF|> <ConfID> [<any other param>]'");
			System.exit(0);
		}

		String searchChosen = args[0];// Search Algorithm is taken from command line
		Conf conf = Conf.valueOf(args[1]);// Config is taken from command line

		runSearch(searchChosen, conf.getMap(), conf.getS(), conf.getG());
	}

	private static void runSearch(String algorithm, Map map, Coord start, Coord goal) {
		switch(algorithm) {
			case "BFS":
				BFS bfs = new BFS(start, goal, map.getMap());
				bfs.search();
				break;
			case "DFS":
				DFS dfs = new DFS(start, goal, map.getMap());
				dfs.search();
				break;
			case "BestF":
				BestF bestf = new BestF(start, goal, map.getMap());
				bestf.search();
				break;
			case "AStar":
				AStar astar = new AStar(start, goal, map.getMap());
				astar.search();
				break;
			case "BiDirectional":
				BiDirectional bidirectional = new BiDirectional(start, goal, map.getMap());
				bidirectional.search();
				break;
			default:
				System.out.println(algorithm + " is not a supported search strategy in this implementation. Please try one of <BFS/DFS/BestF/AStar>");
				System.exit(0);
		}


	}


	private static void printMap(Map m, Coord init, Coord goal) {

		int[][] map=m.getMap();

		System.out.println();
		int rows=map.length;
		int columns=map[0].length;

		//top row
		System.out.print("  ");
		for(int c=0;c<columns;c++) {
			System.out.print(" "+c);
		}
		System.out.println();
		System.out.print("  ");
		for(int c=0;c<columns;c++) {
			System.out.print(" -");
		}
		System.out.println();

		//print rows 
		for(int r=0;r<rows;r++) {
			boolean right;
			System.out.print(r+"|");
			if(r%2==0) { //even row, starts right [=starts left & flip right]
				right=false;
			}else { //odd row, starts left [=starts right & flip left]
				right=true;
			}
			for(int c=0;c<columns;c++) {
				System.out.print(flip(right));
				if(isCoord(init,r,c)) {
					System.out.print("S");
				}else {
					if(isCoord(goal,r,c)) {
						System.out.print("G");
					}else {
						if(map[r][c]==0){
							System.out.print(".");
						}else{
							System.out.print(map[r][c]);
						}
					}
				}
				right=!right;
			}
			System.out.println(flip(right));
		}
		System.out.println();


	}

	private static boolean isCoord(Coord coord, int r, int c) {
		//check if coordinates are the same as current (r,c)
		if(coord.getR()==r && coord.getC()==c) {
			return true;
		}
		return false;
	}

	public static String flip(boolean right) {
        //prints triangle edges
		if(right) {
			return "\\"; //right return left
		}else {
			return "/"; //left return right
		}

	}

}
