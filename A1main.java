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
}
