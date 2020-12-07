package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import general.Helper;

public class Day07 {

	public static final String SHINY_GOLD = "shiny gold";


	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day07p1.txt");

		Map<String, List<BagPair>> graph = new HashMap<>();
		Map<String, List<BagPair>> reverseGraph = new HashMap<>();

		for (String line : lines) {
			String[] containPart = line.split("bags contain");
			String key = containPart[0].trim();
			graph.putIfAbsent(key, new ArrayList<>());

			for (String part : containPart[1].split(",")) {
				String[] contained = part.trim().split(" ");
				if (contained[0].trim().equals("no")) break;
				int number = Integer.parseInt(contained[0]);
				String bagName = contained[1] + " " + contained[2];
				bagName = bagName.trim();
				reverseGraph.putIfAbsent(bagName, new ArrayList<>());
				reverseGraph.get(bagName).add(new BagPair(number, key));

				graph.get(key).add(new BagPair(number, bagName));
			}
		}

		solvePart1(reverseGraph);

		solvePart2(graph);

	}


	private static void solvePart2(Map<String, List<BagPair>> graph) {

		Queue<String> q = new LinkedList<>();
		q.add(SHINY_GOLD);
		Map<String, Integer> seen = new HashMap<>();

		int goldNeeds = runDfs(graph, seen, SHINY_GOLD);

		Helper.printResultPart2(String.valueOf(goldNeeds));
	}


	private static int runDfs(Map<String, List<BagPair>> graph, Map<String, Integer> seen, String currBag) {

		if (seen.containsKey(currBag)) return seen.get(currBag);
		if (graph.get(currBag).size() == 0) return 0;

		int bagCount = 0;
		for (BagPair neigh : graph.get(currBag)) {
			bagCount += neigh.numberOf + neigh.numberOf * runDfs(graph, seen, neigh.bagName);
		}
		seen.put(currBag, bagCount);
		return bagCount;
	}


	private static void solvePart1(Map<String, List<BagPair>> reverseGraph) {

		Queue<String> q = new LinkedList<>();
		q.add(SHINY_GOLD);
		Set<String> seen = new HashSet<>();

		int count = 0;
		while (!q.isEmpty()) {
			String next = q.poll();
			if (seen.contains(next)) continue;
			seen.add(next);
			count++;
			if (!reverseGraph.containsKey(next)) continue;

			for (BagPair rev : reverseGraph.get(next)) {
				if (seen.contains(rev.bagName)) continue;
				q.add(rev.bagName);
			}
		}

		Helper.printResultPart1(String.valueOf(count - 1));
	}


	private static class BagPair {
		int numberOf = 0;
		String bagName;

		public BagPair(int numberOf, String bagName) {

			this.numberOf = numberOf;
			this.bagName = bagName;
		}


		@Override
		public String toString() {

			return "BagPair{" +
					"numberOf=" + numberOf +
					", bagName='" + bagName + '\'' +
					'}';
		}
	}

}
