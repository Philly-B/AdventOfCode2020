package days;

import java.util.ArrayList;
import java.util.List;

import datastructures.IntPair;
import general.Helper;

public class Day13 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day13p1.txt");

		solvePart1(input);
		runPart2Examples();
		runPart2(input.get(1));

	}

	private static void runPart2(String input) {

		long resultPart2 = solvePart2(input);
		Helper.printResultPart2(String.valueOf(resultPart2));
	}



	private static void runPart2Examples() {

		String[] examples = new String[] {
				"17,x,13,19",
				"67,7,59,61",
				"67,x,7,59,61",
				"67,7,x,59,61",
				"1789,37,47,1889",
				"7,13,x,x,59,x,31,19"
		};
		int[] exampleResults = new int[] {
				3417,
				754018,
				779210,
				1261476,
				1202161486,
				1068781,
		};

		for (int i=0; i < examples.length; i++) {
			long result = solvePart2(examples[i]);
			if (result == exampleResults[i]) {
				System.out.println("Example " + i + " is correct");
			} else {
				System.out.println("Example " + examples[i] + " is wrong " + result + " should be " + exampleResults[i]);
				break;
			}
			assert result == exampleResults[i];
		}
	}

	private static long solvePart2(String input) {

		String[] busses = input.split(",");
		List<IntPair> busIdsAndDelay = new ArrayList<>();
		for (int i=0; i < busses.length; i++) {
			if (busses[i].equals("x")) continue;
			busIdsAndDelay.add(new IntPair(Integer.parseInt(busses[i]), i));
		}
		long result = 0;
		long currentMultiplier =busIdsAndDelay.get(0).first;
		for (int i=1; i < busIdsAndDelay.size(); i++) {
			IntPair currentBus = busIdsAndDelay.get(i);
			for(int testNumber = 0; testNumber < currentBus.first; testNumber++) {
				if ((result + currentMultiplier * testNumber + currentBus.second) % currentBus.first == 0) {
					result = result + currentMultiplier * testNumber;
					currentMultiplier *= currentBus.first;
					break;
				}
			}
		}

		return result;
	}

	private static void solvePart1(List<String> input) {

		int timeOfArrival = Integer.parseInt(input.get(0));

		List<Integer> busLinesDriving = new ArrayList<>();

		for (String busId : input.get(1).split(",")) {
			if (busId.equals("x")) {
				continue;
			}
			busLinesDriving.add(Integer.parseInt(busId));
		}

		int bestDiffToArrival = Integer.MAX_VALUE;
		int bestBusId = 0;
		for (int busId : busLinesDriving) {
			int currDiff = (int) (Math.ceil(timeOfArrival * 1.0 / busId) * busId - timeOfArrival);
			if (currDiff < bestDiffToArrival) {
				bestDiffToArrival = currDiff;
				bestBusId = busId;
			}
		}

		Helper.printResultPart1(String.valueOf(bestBusId * bestDiffToArrival));
	}
}
