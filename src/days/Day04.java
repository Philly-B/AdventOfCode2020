package days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import general.Helper;

public class Day04 {

	static final String[] requiredFields = new String[] { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };
	static final Pattern hclPattern = Pattern.compile("#[0-9a-f]{6}");
	static final Pattern eclPattern = Pattern.compile("amb|blu|brn|gry|grn|hzl|oth");
	static final Pattern pidPattern = Pattern.compile("[0-9]{9}");

	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day04p1.txt");
		lines.add("");

		solve(lines, Day04::isValidPassportPart1);
		solve(lines, Day04::isValidPassportPart2);
	}


	private static void solve(List<String> lines, Function<Map<String, String>, Boolean> isValidPassport) {

		Map<String, String> passport = new HashMap<>();
		int validPassports = 0;
		for (String line : lines) {

			if (line.isEmpty()) {
				validPassports += isValidPassport.apply(passport) ? 1 : 0;
				passport.clear();
			} else {
				for (String pairs : line.split(" ")) {
					String[] keyVal = pairs.split(":");
					passport.put(keyVal[0], keyVal[1]);
				}
			}
		}
		Helper.printResultPart1(String.valueOf(validPassports));
	}


	private static boolean isValidPassportPart1(Map<String, String> passport) {

		for (String requiredField : requiredFields) {
			if (!passport.containsKey(requiredField)) {
				return false;
			}
		}
		return true;
	}


	private static boolean isValidPassportPart2(Map<String, String> passport) {

		for (String requiredField : requiredFields) {
			if (!passport.containsKey(requiredField)) {
				return false;
			}
			String toCheck = passport.get(requiredField);
			boolean validField = true;
			switch (requiredField) {
				case "byr":
					validField &= parseIntAndCheckInRange(toCheck, 1920, 2002);
					break;
				case "iyr":
					validField &= parseIntAndCheckInRange(toCheck, 2010, 2020);
					break;
				case "eyr":
					validField &= parseIntAndCheckInRange(toCheck, 2020, 2030);
					break;
				case "hgt":
					if (toCheck.endsWith("in")) {
						String actualHgtValue = toCheck.substring(0, toCheck.length() - 2);
						validField &= parseIntAndCheckInRange(actualHgtValue, 59, 76);
					} else if (toCheck.endsWith("cm")) {
						String actualHgtValue = toCheck.substring(0, toCheck.length() - 2);
						validField &= parseIntAndCheckInRange(actualHgtValue, 150, 193);
					} else {
						validField = false;
					}
					break;
				case "hcl":
					validField &= hclPattern.matcher(toCheck).matches();
					break;
				case "ecl":
					validField &= eclPattern.matcher(toCheck).matches();
					break;
				case "pid":
					validField &= pidPattern.matcher(toCheck).matches();
					break;
			}
			if (!validField) return false;
		}
		return true;
	}


	private static boolean parseIntAndCheckInRange(String toCheck, int minVal, int maxVal) {

		int numberToCheck = Integer.parseInt(toCheck);
		return numberToCheck >= minVal && numberToCheck <= maxVal;
	}

}
