package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import general.Helper;

public class Day16 {

	private static int NUMBERS_ON_TICKET = 20;

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day16p1.txt");

		List<Condition> conditions = new ArrayList<>();
		List<Ticket> tickets = new ArrayList<>();

		Ticket yourTicket = readInput(input, conditions, tickets);
		solvePart1(conditions, tickets);

		solvePart2(conditions, tickets, yourTicket);
	}


	private static void solvePart2(List<Condition> conditions, List<Ticket> tickets, Ticket yourTicket) {

		addAllPossibleMappingsToConditions(conditions, tickets);
		Map<Integer, Mapping> mappings = createMappings(conditions, tickets);
		boolean ok = dfsFindMapping(mappings, 0, new HashSet<>());
		if (!ok) throw new IllegalArgumentException();
		long result = calculateSolutionForTicket(conditions, yourTicket);
		Helper.printResultPart2(String.valueOf(result));
	}


	private static long calculateSolutionForTicket(List<Condition> conditions, Ticket yourTicket) {

		long result = 1;
		for (Condition condition: conditions) {
			if (condition.name.startsWith("departure")) {
				result *= yourTicket.numbers.get(condition.actualPossition);
			}
		}
		return result;
	}


	private static Map<Integer, Mapping> createMappings(List<Condition> conditions, List<Ticket> tickets) {

		Map<Integer, Mapping> mappings = new HashMap<>();
		for (Condition condition: conditions) {
			for (int i=0; i < NUMBERS_ON_TICKET; i++) {
				if (condition.possiblePositions[i] == tickets.size()) {
					condition.actualPossition = i;
					mappings.putIfAbsent(i, new Mapping(i, new HashSet<>()));
					mappings.get(i).possibleMappings.add(condition);
					continue;
				}
			}
		}
		return mappings;
	}


	private static void addAllPossibleMappingsToConditions(List<Condition> conditions, List<Ticket> tickets) {

		for (Ticket ticket : tickets) {
			for (int i=0; i < ticket.numbers.size(); i++) {
				for (Condition condition: conditions) {
					if (condition.fitsCondition(ticket.numbers.get(i))) {
						condition.possiblePositions[i]++;
					}
				}
			}
		}
	}


	private static boolean dfsFindMapping(Map<Integer, Mapping> possibleMappings, int currentNum, Set<Condition> conditionsAlreadyMapped) {

		if (currentNum >= NUMBERS_ON_TICKET) {
			return true;
		}

		for (Condition condition : possibleMappings.get(currentNum).possibleMappings) {
			if (conditionsAlreadyMapped.contains(condition)) continue;

			conditionsAlreadyMapped.add(condition);
			if (dfsFindMapping(possibleMappings, currentNum+1, conditionsAlreadyMapped)) {
				condition.actualPossition = currentNum;
				return true;
			}
			conditionsAlreadyMapped.remove(condition);
		}
		return false;
	}


	private static void assertConditionsValid(List<Condition> conditions) {

		Set<Integer> allFieldIds = new HashSet<>();
		for (Condition condition : conditions) {
			allFieldIds.add(condition.actualPossition);
		}
		if (allFieldIds.size() != NUMBERS_ON_TICKET) {
			throw new IllegalArgumentException();
		}
	}


	private static Ticket readInput(List<String> input, List<Condition> conditions, List<Ticket> tickets) {

		int i = 0;

		while (!input.get(i).isEmpty()) {
			conditions.add(new Condition(input.get(i++)));
		}
		i += 2;
		Ticket yourTicket = new Ticket(input.get(i++));
		i += 2;

		while (i < input.size()) {
			tickets.add(new Ticket(input.get(i++)));
		}
		return yourTicket;
	}


	private static void solvePart1(List<Condition> conditions, List<Ticket> tickets) {

		Iterator<Ticket> ticketIterator = tickets.iterator();
		int sumOfMissfits = 0;
		while (ticketIterator.hasNext()) {
			Ticket ticket = ticketIterator.next();
			for (int num : ticket.numbers) {
				boolean fits = false;
				for (Condition condition : conditions) {
					if (condition.fitsCondition(num)) {
						fits = true;
						break;
					}
				}
				if (!fits) {
					sumOfMissfits += num;
					ticketIterator.remove();
					break;
				}
			}
		}
		Helper.printResultPart1(String.valueOf(sumOfMissfits));
	}

	private static class Mapping implements  Comparable<Mapping> {

		int fieldId;
		Set<Condition> possibleMappings;


		public Mapping(int fieldId, Set<Condition> possibleMappings) {

			this.fieldId = fieldId;
			this.possibleMappings = possibleMappings;
		}


		@Override
		public int compareTo(Mapping o) {

			return possibleMappings.size() - o.possibleMappings.size();
		}


		@Override
		public String toString() {

			return "Mapping{" +
					"fieldId=" + fieldId +
					", possibleMappings=" + possibleMappings +
					'}';
		}
	}

	private static class Ticket {

		List<Integer> numbers;

		public Ticket(String ticketNumbers) {

			numbers = new ArrayList<>();
			for (String num : ticketNumbers.split(",")) {
				numbers.add(Integer.parseInt(num));
			}
		}


		@Override
		public String toString() {

			return "Ticket{" +
					"numbers=" + numbers +
					'}';
		}
	}

	private static class Condition {

		String name;
		int firstRangeLower;
		int firstRangeUpper;
		int secondRangeLower;
		int secondRangeUpper;

		int[] possiblePositions;
		int actualPossition;

		public Condition(String conditionLine) {
			possiblePositions = new int[NUMBERS_ON_TICKET];
			actualPossition=-1;

			// arrival station: 46-753 or 775-953
			String[] nameConditions = conditionLine.split(":");
			name = nameConditions[0];
			String[] conditions = nameConditions[1].split(" or ");
			String[] firstCondition = conditions[0].trim().split("-");
			String[] secondCondition = conditions[1].trim().split("-");
			firstRangeLower = Integer.parseInt(firstCondition[0]);
			firstRangeUpper = Integer.parseInt(firstCondition[1]);
			secondRangeLower = Integer.parseInt(secondCondition[0]);
			secondRangeUpper = Integer.parseInt(secondCondition[1]);
		}

		public boolean fitsCondition(int number) {

			return ((number >= firstRangeLower && number <= firstRangeUpper) ||
					(number >= secondRangeLower && number <= secondRangeUpper));
		}


		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Condition condition = (Condition) o;
			return firstRangeLower == condition.firstRangeLower &&
					firstRangeUpper == condition.firstRangeUpper &&
					secondRangeLower == condition.secondRangeLower &&
					secondRangeUpper == condition.secondRangeUpper &&
					actualPossition == condition.actualPossition &&
					Objects.equals(name, condition.name) &&
					Arrays.equals(possiblePositions, condition.possiblePositions);
		}


		@Override
		public int hashCode() {

			int result = Objects.hash(name, firstRangeLower, firstRangeUpper, secondRangeLower, secondRangeUpper, actualPossition);
			result = 31 * result + Arrays.hashCode(possiblePositions);
			return result;
		}


		@Override
		public String toString() {

			return "Condition{" +
					"name='" + name + '\'' +
					", firstRangeLower=" + firstRangeLower +
					", firstRangeUpper=" + firstRangeUpper +
					", secondRangeLower=" + secondRangeLower +
					", secondRangeUpper=" + secondRangeUpper +
					", possiblePositions=" + Arrays.toString(possiblePositions) +
					", actualPossition=" + actualPossition +
					'}';
		}
	}

}
