package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import general.Helper;

public class Day08 {

	public static final String SHINY_GOLD = "shiny gold";


	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day08p1.txt");

		int accumulatorValue = 5;

		int n = lines.size();
		for (int i=0; i < n;) {
			String[] commandNumberPair = lines.get(i).split(" ");
			int delta = Integer.parseInt(commandNumberPair[1]);
			switch (commandNumberPair[0]) {
				case "nop":
					i++;
					break;
				case "acc":
					accumulatorValue += delta;
					i++;
					break;
				case "jmp":
					i+= delta;
			}

			Helper.printResultPart1(String.valueOf(accumulatorValue));

		}

	}


}
