package days;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import general.Helper;

public class Day06 {

	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day06p1.txt");

		int countAllYes = 0;
		int countAnyYes = 0;
		HashSet<Character> answeredYes = new HashSet<>();
		int[] countYeses = new int[26];
		int numPeopleInGroup = 0;
		for (String line: lines) {
			if (line.isEmpty()) {
				countAnyYes += answeredYes.size();
				countAllYes += countEveryEntryWithXAmount(countYeses, numPeopleInGroup);
				answeredYes.clear();
				Arrays.fill(countYeses, 0);
				numPeopleInGroup = 0;
			} else {
				numPeopleInGroup++;
			}
			for (char c : line.toCharArray()) {
				answeredYes.add(c);
				countYeses[c - 'a']++;
			}
		}
		countAnyYes += answeredYes.size();
		countAllYes += countEveryEntryWithXAmount(countYeses, numPeopleInGroup);

		Helper.printResultPart1(String.valueOf(countAnyYes));
		Helper.printResultPart2(String.valueOf(countAllYes));
	}


	private static int countEveryEntryWithXAmount(int[] countYeses, int numPeopleInGroup) {
		int res = 0;
		for (int yesses : countYeses) {
			if (yesses == numPeopleInGroup) res++;
		}
		System.out.println(Arrays.toString(countYeses) + " " + numPeopleInGroup + " " + res);
		return res;
	}

}
