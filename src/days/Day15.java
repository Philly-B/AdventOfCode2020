package days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import general.Helper;

public class Day15 {

	private static final String[] examples = new String[] {
			"0,3,6",
			"1,3,2",
			"2,1,3",
			"1,2,3",
			"2,3,1",
			"3,2,1",
			"3,1,2"
	};
	private static final int[] resultsPart1 = new int[] {
			436,
			1,
			10,
			27,
			78,
			438,
			1836
	};

	private static final int[] resultsPart2 = new int[] {
			175594,
			2578,
			3544142,
			261214,
			6895259,
			18,
			362
	};


	public static void main(String[] args) {

		testPart1();
		solvePart1();
		testPart2();
		solvePart2();
	}


	private static void testPart1() {

		for (int i = 0; i < examples.length; i++) {
			int result = solve(examples[i], 2020);
			if (result != resultsPart1[i]) {
				throw new IllegalArgumentException("result for example " + i + " was " + result + " but should be " + resultsPart1[i]);
			}
		}
	}


	private static void testPart2() {

		for (int i = 0; i < examples.length; i++) {
			int result = solve(examples[i], 30000000);
			if (result != resultsPart2[i]) {
				throw new IllegalArgumentException("result for example " + i + " was " + result + " but should be " + resultsPart1[i]);
			}
		}
	}


	private static void solvePart1() {

		int resultPart1 = solve("8,0,17,4,1,12", 2020);
		Helper.printResultPart1(String.valueOf(resultPart1));
	}


	private static void solvePart2() {

		int resultPart1 = solve("8,0,17,4,1,12", 30000000);
		Helper.printResultPart1(String.valueOf(resultPart1));
	}


	private static int solve(String s, int limit) {

		String[] nums = s.split(",");

		Map<Integer, Integer> lastSeenIndex = new HashMap<>();

		int lastNumber = -1;
		int nextNumber;
		for (int i = 0; i < nums.length; i++) {
			nextNumber = Integer.parseInt(nums[i]);
			if (i > 0) {
				lastSeenIndex.put(lastNumber, i - 1);
			}
			lastNumber = nextNumber;
		}

		for (int i = nums.length - 1; i < limit-1; i++) {
			if (lastSeenIndex.get(lastNumber) == null) {
				nextNumber = 0;
			} else {
				nextNumber = i - lastSeenIndex.get(lastNumber);
			}
			lastSeenIndex.put(lastNumber, i);
			lastNumber = nextNumber;
		}

		return lastNumber;
	}

}
