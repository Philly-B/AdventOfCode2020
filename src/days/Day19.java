package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import general.Helper;

public class Day19 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day19pe1.txt");

		Map<Integer, String> rules = new HashMap<>();
		List<String> wordsToMatch = new ArrayList<>();

		parseInput(input, rules, wordsToMatch);

		int resultPart1 = solvePart1(rules, wordsToMatch);
		Helper.printResultPart1(String.valueOf(resultPart1));

		rules.put(8, "42 | 42 8");
		rules.put(11, "42 31 | 42 11 31");

		int resultPart2 = solvePart1(rules, wordsToMatch);
		Helper.printResultPart2(String.valueOf(resultPart2));
	}


	private static int solvePart1(Map<Integer, String> rules, List<String> wordsToMatch) {

		int matchesTheRule = 0;
		for (String wordToMatch: wordsToMatch) {
			if (doesItMatch(rules, wordToMatch, 0, 0) == wordToMatch.length()) {
				matchesTheRule++;
			}
		}
		return matchesTheRule;
	}


	private static void parseInput(List<String> input, Map<Integer, String> rules, List<String> wordsToMatch) {

		int i = 0;
		for (; i < input.size(); i++) {
			String line = input.get(i);
			if (line.isEmpty()) {
				break;
			}
			String[] idRulePair = line.split(":");
			int ruleId = Integer.parseInt(idRulePair[0]);
			if (line.contains("\"")){
				String ruleChar = idRulePair[1].replace("\"", "").trim();
				rules.put(ruleId, ruleChar);
			} else {
				rules.put(ruleId, idRulePair[1].trim());
			}
		}
		i++;
		for (; i < input.size(); i++)  wordsToMatch.add(input.get(i));
	}


	private static int doesItMatch(Map<Integer, String> rules, String toCheck, int currPosInToCheck, int currRule) {

		String currentRule = rules.get(currRule);
		if (currentRule.length() == 1) {
			if(currPosInToCheck < toCheck.length() && currentRule.equals(toCheck.charAt(currPosInToCheck)+"")) {
				return currPosInToCheck+1;
			}
			return currPosInToCheck;
		}
		String[] ruleParts = currentRule.split("\\|");
		int best = currPosInToCheck;
		for (String rulePart : ruleParts) {
			int tmpCurrPos = currPosInToCheck;
			for (String ruleId : rulePart.trim().split(" ")) {
				int newPos = doesItMatch(rules, toCheck, tmpCurrPos, Integer.parseInt(ruleId));
				if (newPos == tmpCurrPos) break;
				tmpCurrPos = newPos;
			}
			best = Math.max(best, tmpCurrPos);
		}
		return best;
	}

}
