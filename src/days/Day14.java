package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datastructures.IntPair;
import general.Helper;

public class Day14 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day14p1.txt");

		testPart1();
		solvePart1(input);
		testPart2();
		solvePart2(input);
	}


	private static void solvePart1(List<String> input) {

		Map<Long, Long> memoryToValue = new HashMap<>();

		long currentMaskZeros = 0;
		long currentMaskOnes = 0;

		for (String line : input) {
			String[] commandValueSplit = line.split("=");
			if (line.startsWith("mask")) {
				String mask = commandValueSplit[1].trim();
				// set every bit to 1
				currentMaskZeros = Long.MAX_VALUE;
				// set every bit to 0
				currentMaskOnes = 0l;

				for (int i = mask.length() - 1; i >= 0; i--) {
					int actualBitPosition = mask.length() - 1 - i;
					if (mask.charAt(i) == '0') {
						currentMaskZeros &= ~(1l << actualBitPosition);
					} else if (mask.charAt(i) == '1') {
						currentMaskOnes |= 1l << actualBitPosition;
					}
				}
				if (currentMaskOnes < 0 || currentMaskZeros < 0) {
					throw new RuntimeException();
				}
			} else {
				long value = Long.parseLong(commandValueSplit[1].trim());
				value &= currentMaskZeros;
				value |= currentMaskOnes;

				String memoryField = commandValueSplit[0].substring(4);
				memoryField = memoryField.substring(0, memoryField.length() - 2);
				memoryToValue.put(Long.valueOf(memoryField), value);
			}
		}
		long sum = 0;
		for (Long value : memoryToValue.values()) {
			sum += value;
		}
		Helper.printResultPart1(String.valueOf(sum));
	}

	private static void solvePart2(List<String> input) {

		Map<Long, Long> memoryToValue = new HashMap<>();

		String mask = null;

		for (String line : input) {
			String[] commandValueSplit = line.split("=");
			if (line.startsWith("mask")) {
				mask = commandValueSplit[1].trim();
			} else {
				String memoryField = commandValueSplit[0].substring(4);
				memoryField = memoryField.substring(0, memoryField.length()-2);
				long value = Long.parseLong(commandValueSplit[1].trim());

				for (Long memoryAddress : calcAllMemAddresses(mask, Long.valueOf(memoryField))) {
					memoryToValue.put(memoryAddress, value);
				}
			}
		}
		long sum = 0;
		for (Long value : memoryToValue.values()) {
			sum += value;
		}
		Helper.printResultPart1(String.valueOf(sum));
	}


	private static Set<Long> calcAllMemAddresses(String mask, long memoryField) {

		Set<Long> allMemoryAddresse  = new HashSet<>();
		allMemoryAddresse.add(0L);

		long memoryAddress = Long.valueOf(memoryField);
		long currentMask = 0l;
		for (int i=mask.length()-1; i >= 0; i--) {
			Set<Long> newAddresses = new HashSet<>();
			int actualBitPosition = mask.length() - 1 - i;
			currentMask = 1l << actualBitPosition;
			if (mask.charAt(i) == '1') {
				for (Long address : allMemoryAddresse) {
					newAddresses.add(address | 1l << actualBitPosition);
				}
			} else if (mask.charAt(i) == 'X') {
				for (Long address : allMemoryAddresse) {
					newAddresses.add(address);
					newAddresses.add(address | 1l << actualBitPosition);
				}
			} else if (mask.charAt(i) == '0' && (memoryField & currentMask) == currentMask){
				for (Long address : allMemoryAddresse) {
					newAddresses.add(address | 1l << actualBitPosition);
				}
			} else {
				continue;
			}
			allMemoryAddresse = newAddresses;
		}

		return allMemoryAddresse;
	}


	private static void testPart1() {

		List<String> testInput = new ArrayList<>();
		testInput.add("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");
		testInput.add("mem[8] = 11");
		testInput.add("mem[7] = 101");
		testInput.add("mem[8] = 0");

		solvePart1(testInput);
	}

	private static void testPart2() {

		List<String> testInput = new ArrayList<>();
		testInput.add("mask = 000000000000000000000000000000X1001X");
		testInput.add("mem[42] = 100");
		testInput.add("mask = 00000000000000000000000000000000X0XX");
		testInput.add("mem[26] = 1");

		solvePart2(testInput);
	}
}
