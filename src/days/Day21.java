package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import general.Helper;

public class Day21 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day21p1.txt");

		Map<String, Set<String>> allergensToPossibleIngredients = new HashMap<>();
		Map<String, AtomicInteger> ingredientCount = new HashMap<>();

		readInput(input, allergensToPossibleIngredients, ingredientCount);
		solvePart1(allergensToPossibleIngredients, ingredientCount);
		solvePart2(allergensToPossibleIngredients);

	}


	private static void solvePart2(Map<String, Set<String>> allergensToPossibleIngredients) {

		List<String> alreadyMappedIngredients = new ArrayList<>();

		List<String> dfsOrder = allergensToPossibleIngredients
				.entrySet()
				.stream()
				.sorted((e1, e2) -> e1.getValue().size() - e2.getValue().size())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		Map<String, String> result = dfsMapping(allergensToPossibleIngredients, alreadyMappedIngredients, dfsOrder, 0);

		String dangerousIngredientList = result
				.entrySet()
				.stream()
				.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.joining(","));

		Helper.printResultPart2(dangerousIngredientList);
	}


	private static Map<String, String> dfsMapping(
			Map<String, Set<String>> allergensToPossibleIngredients,
			List<String> alreadyMappedIngredients,
			List<String> allergenOrder, int currItem) {

		if (currItem == allergenOrder.size()) {
			return new HashMap<>();
		}
		String currentAllergen = allergenOrder.get(currItem);
		for (String possibleIngredient : allergensToPossibleIngredients.get(currentAllergen)) {
			if (alreadyMappedIngredients.contains(possibleIngredient)) continue;
			alreadyMappedIngredients.add(possibleIngredient);
			Map<String, String> result = dfsMapping(allergensToPossibleIngredients, alreadyMappedIngredients, allergenOrder, currItem + 1);
			if (result == null) {
				alreadyMappedIngredients.remove(possibleIngredient);
				continue;
			} else {
				result.put(possibleIngredient, currentAllergen);
				return result;
			}
		}
		return null;
	}


	private static void readInput(List<String> input, Map<String, Set<String>> allergensToPossibleIngredients, Map<String, AtomicInteger> ingredientCount) {

		for (String line : input) {
			String[] parts = line.split("\\(contains");
			String[] allergens = parts[1].substring(0, parts[1].length() - 1).split(",");
			List<String> ingredients = Arrays.asList(parts[0].split(" "));

			for (String ingredient : ingredients) {
				ingredientCount.putIfAbsent(ingredient, new AtomicInteger(0));
				ingredientCount.get(ingredient).incrementAndGet();
			}

			for (String allergen : allergens) {
				if (allergensToPossibleIngredients.containsKey(allergen)) {
					allergensToPossibleIngredients.get(allergen).retainAll(ingredients);
				} else {
					allergensToPossibleIngredients.put(allergen, new HashSet<>(ingredients));
				}
			}
		}
	}


	private static void solvePart1(Map<String, Set<String>> allergensToPossibleIngredients, Map<String, AtomicInteger> ingredientCount) {

		Set<String> ingredientsWhichContainStuff = allergensToPossibleIngredients
				.values()
				.stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());

		if (ingredientsWhichContainStuff.size() != allergensToPossibleIngredients.size()) {
			throw new IllegalArgumentException("there are not enough ingredients");
		}

		int occurenceOfSaveIngredients = 0;
		for (Map.Entry<String, AtomicInteger> entry : ingredientCount.entrySet()) {
			if (ingredientsWhichContainStuff.contains(entry.getKey())) {
				continue;
			}
			occurenceOfSaveIngredients += entry.getValue().get();
		}
		Helper.printResultPart1(String.valueOf(occurenceOfSaveIngredients));
	}

}
