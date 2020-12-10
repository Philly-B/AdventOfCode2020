package days;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import general.Helper;

public class Day10 {

	public static void main(String[] args) {

		List<Integer> input = Helper.readFile("./data/days/day10p1.txt")
				.stream()
				.map(Integer::parseInt)
				.sorted()
				.collect(Collectors.toList());
		input.add(input.get(input.size() - 1)+3);
		input.add(0);
		Collections.sort(input);

		solvePart1(input);
		solvePart2(input);
	}



	private static void solvePart1(List<Integer> input) {

		int[] increments = new int[4];
		for (int i = 1; i < input.size(); i++) {
			increments[input.get(i) - input.get(i-1)]++;
		}
		Helper.printResultPart1(String.valueOf(increments[1] * increments[3]));
	}

	private static void solvePart2(List<Integer> input) {

		long[] dp = new long[input.size()];
		dp[0] = 1;
		for (int i = 0; i < input.size(); i++) {
			int currentNumber = input.get(i);
			int pos = i-1;
			while (pos >= 0 &&  currentNumber - input.get(pos) <= 3) {
				dp[i] += dp[pos];
				pos--;
			}
		}

		Helper.printResultPart2(String.valueOf(dp[input.size()-1]));
	}

}
