package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import general.Helper;

public class Day08 {

	public static final String SHINY_GOLD = "shiny gold";


	public static void main(String[] args) {

		List<Instruction> instructions = Helper.readFile("./data/days/day08p1.txt")
				.stream()
				.map(Instruction::new)
				.collect(Collectors.toList());

		runPart1(instructions);
		runPart2(instructions);
	}


	private static void runPart1(List<Instruction> instructions) {

		Accumulator accumulator = new Accumulator(instructions);
		accumulator.runAccumulator();
		Helper.printResultPart1(String.valueOf(accumulator.getCurrentResult().lastValueOfAccumulator));
	}


	private static void runPart2(List<Instruction> instructions) {

		for (Instruction instruction : instructions) {
			if (instruction.command.equals(Instruction.COMMAND.ACC)) {
				continue;
			}
			Instruction.COMMAND toResetTO = instruction.command;
			if (instruction.command.equals(Instruction.COMMAND.JMP)) {
				instruction.command = Instruction.COMMAND.NOP;
			} else {
				instruction.command = Instruction.COMMAND.JMP;
			}

			Accumulator accumulator = new Accumulator(instructions);
			accumulator.runAccumulator();
			AccumulatorResult accumulatorResult = accumulator.getCurrentResult();

			if (accumulatorResult.finished) {
				Helper.printResultPart2(String.valueOf(accumulatorResult.lastValueOfAccumulator));
				break;
			}

			instruction.command = toResetTO;
		}
	}



	private static class AccumulatorResult {

		int lastValueOfAccumulator;
		boolean finished;


		public AccumulatorResult(int lastValueOfAccumulator, boolean finished) {

			this.lastValueOfAccumulator = lastValueOfAccumulator;
			this.finished = finished;
		}
	}

	private static class Accumulator {

		private int accumulatorValue = 0;
		private boolean finished = false;
		private int instructionPos = 0;
		private List<Instruction> instructions;

		public Accumulator(List<Instruction> instructions) {

			this.instructions = instructions;
		}

		public void runAccumulator() {

			Set<Integer> instructionsDone = new HashSet<>();
			for (; instructionPos < instructions.size(); ) {
				if (instructionsDone.contains(instructionPos)) {
					finished = false;
					return;
				}
				instructionsDone.add(instructionPos);
				Instruction current = instructions.get(instructionPos);
				switch (current.command) {
					case NOP:
						instructionPos++;
						break;
					case ACC:
						accumulatorValue += current.value;
						instructionPos++;
						break;
					case JMP:
						instructionPos += current.value;
						break;
				}
			}
			finished = true;
		}

		public AccumulatorResult getCurrentResult() {
			return new AccumulatorResult(accumulatorValue, finished);
		}

	}

	private static class Instruction {

		enum COMMAND {
			ACC,
			JMP,
			NOP
		}

		COMMAND command;
		int value;


		public Instruction(String line) {

			String[] commandNumberPair = line.split(" ");
			command = parseCommand(commandNumberPair[0]);
			value = Integer.parseInt(commandNumberPair[1]);
		}


		private COMMAND parseCommand(String command) {

			switch (command) {
				case "nop":
					return COMMAND.NOP;
				case "acc":
					return COMMAND.ACC;
				case "jmp":
					return COMMAND.JMP;
				default:
					throw new RuntimeException("Unknown command " + command);
			}
		}


		@Override
		public String toString() {

			return "Instruction{" +
					"command=" + command +
					", value=" + value +
					'}';
		}
	}

}
