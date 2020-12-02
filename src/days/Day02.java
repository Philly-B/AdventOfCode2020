package days;

import java.util.Arrays;
import java.util.List;

public class Day02 {

	public static void main(String[] args) {

		List<String> lines = general.Helper.readFile("./data/days/day02p1.txt");
		solvePart1(lines);
		solvePart2(lines);
	}


	private static void solvePart1(java.util.List<String> lines) {
		int valid = 0;
		int[] counts = new int[26];
		for (String line : lines) {
			Arrays.fill(counts, 0);

			String[] parts = line.split(":");
			String[] validationParts = parts[0].split(" ");
			String[] numerParts = validationParts[0].split("-");

			String password = parts[1].trim();
			char charToLookAt = validationParts[1].trim().toCharArray()[0];
			int minAppearance = Integer.parseInt(numerParts[0]);
			int maxAppearance = Integer.parseInt(numerParts[1]);

			for (char c : password.toCharArray()) {
				counts[c - 'a']++;
			}
			if (counts[charToLookAt - 'a'] >= minAppearance && counts[charToLookAt - 'a'] <= maxAppearance) {
				valid++;
			}
		}

		general.Helper.printResultPart1(String.valueOf(valid));
	}

	private static void solvePart2(java.util.List<String> lines) {
		int valid = 0;
		for (String line : lines) {
			String[] parts = line.split(":");
			String[] validationParts = parts[0].split(" ");
			String[] numerParts = validationParts[0].split("-");

			char[] password = parts[1].trim().toCharArray();
			char charToLookAt = validationParts[1].trim().toCharArray()[0];
			int firstAppearance = Integer.parseInt(numerParts[0]);
			int secondAppearance = Integer.parseInt(numerParts[1]);

			if (password[firstAppearance-1] == charToLookAt ^
					password[secondAppearance - 1] == charToLookAt) {
				valid++;
			}
		}

		general.Helper.printResultPart1(String.valueOf(valid));
	}



}
