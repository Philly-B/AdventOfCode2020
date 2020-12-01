package days;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import datastructures.IntPair;
import general.Helper;

public class Day01 {

	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day01p1.txt");
		int[] numbers = convertLines(lines);
		solvePart1(numbers);
		solvePart2(numbers);
	}


	private static int[] convertLines(List<String> lines) {

		return lines.stream()
				.mapToInt(Integer::parseInt)
				.sorted()
				.toArray();
	}


	private static void solvePart2(int[] numbers) {

		for (int i = 0; i < numbers.length - 2; i++) {
			int curr = numbers[i];
			IntPair result = solve(numbers, i + 1, 2020 - curr);
			if (result != null) {
				Helper.printResultPart2(String.valueOf(result.first * result.second * curr));
				return;
			}
		}
	}


	private static void solvePart1(int[] numbers) {

		IntPair result = solve(numbers, 0, 2020);
		if (result == null) {
			return;
		}
		Helper.printResultPart1(String.valueOf(result.first * result.second));
	}


	private static IntPair solve(int[] numbers, int start, int goal) {

		Set<Integer> seen = new HashSet<>(numbers.length);
		int num;
		for (int i = start; i < numbers.length; i++) {
			num = numbers[i];
			if (seen.contains(goal - num)) {
				return new IntPair(num, goal - num);
			}
			seen.add(num);
		}
		return null;
	}

}
