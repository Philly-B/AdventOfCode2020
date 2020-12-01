package day._1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import datastructures.IntPair;
import general.Helper;

public class Day1 {

	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/day_1/part1.txt");
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
		for (int num = start; num < numbers.length; num++) {
			if (seen.contains(goal - num)) {
				return new IntPair(num, goal - num);
			}
			seen.add(num);
		}
		return null;
	}

}
