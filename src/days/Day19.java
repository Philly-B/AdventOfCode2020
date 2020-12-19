package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import general.Helper;

public class Day19 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day19p1.txt");

		Map<Integer, String> rules = new HashMap<>();
		List<String> wordsToMatch = new ArrayList<>();

		parseInput(input, rules, wordsToMatch);

		int resultPart1 = solve(rules, wordsToMatch, 1);
		Helper.printResultPart1(String.valueOf(resultPart1));

		rules.put(8, "42 | 42 8");
		rules.put(11, "42 31 | 42 11 31");

		int resultPart2 = solve(rules, wordsToMatch, 20);
		Helper.printResultPart2(String.valueOf(resultPart2));
	}


	private static int solve(Map<Integer, String> rules, List<String> wordsToMatch, int maxTryAndError) {

		int matchesTheRule = 0;
		for (String wordToMatch : wordsToMatch) {
			outer: for (int eightLoopCount=0; eightLoopCount < maxTryAndError; eightLoopCount++) {
				for (int elevenLoopCount=0; elevenLoopCount < maxTryAndError; elevenLoopCount++) {
					if (doesItMatch(rules, wordToMatch, 0, 0, eightLoopCount, elevenLoopCount) == wordToMatch.length()) {
						matchesTheRule++;
						break outer;
					}
				}
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
			if (line.contains("\"")) {
				String ruleChar = idRulePair[1].replace("\"", "").trim();
				rules.put(ruleId, ruleChar);
			} else {
				rules.put(ruleId, idRulePair[1].trim());
			}
		}
		i++;
		for (; i < input.size(); i++)
			wordsToMatch.add(input.get(i));
	}


	private static int doesItMatch(
			Map<Integer, String> rules,
			String toCheck,
			int currPosInToCheck,
			int currRule,
			int eightLoopCount,
			int elevenLoopCount) {

		String currentRule = rules.get(currRule);
		if (currentRule.length() == 1) {
			if (currPosInToCheck < toCheck.length() && currentRule.equals(toCheck.charAt(currPosInToCheck) + "")) {
				return currPosInToCheck + 1;
			}
			return currPosInToCheck;
		}
		String[] ruleParts = currentRule.split("\\|");
		// Bruteforce for 8 and 11
		if (currRule == 8 ) {
			if (eightLoopCount > 0) {
				ruleParts = new String[] { ruleParts[1] };
				eightLoopCount--;
			} else {
				ruleParts = new String[] { ruleParts[0] };
			}
		}else if (currRule == 11) {
			if (elevenLoopCount > 0) {
				ruleParts = new String[] { ruleParts[1] };
				elevenLoopCount--;
			} else {
				ruleParts = new String[] { ruleParts[0] };
			}
		}

		int best = currPosInToCheck;
		for (String rulePart : ruleParts) {
			int tmpCurrPos = currPosInToCheck;
			for (String ruleId : rulePart.trim().split(" ")) {
				int newPos = doesItMatch(rules, toCheck, tmpCurrPos, Integer.parseInt(ruleId), eightLoopCount, elevenLoopCount);
				if (newPos == tmpCurrPos) {
					tmpCurrPos = currPosInToCheck;
					break;
				}
				tmpCurrPos = newPos;
			}
			best = Math.max(best, tmpCurrPos);
		}
		return best;
	}

}
