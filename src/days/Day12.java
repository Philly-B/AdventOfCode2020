package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import datastructures.IntPair;
import general.Helper;

public class Day12 {

	public static final char[] POS_MAP = new char[] { 'N', 'E', 'S', 'W' };


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day12p1.txt");

		solvePart1(input);
		solvePart2(input);
	}


	private static void solvePart1(List<String> input) {

		char currentOrientation = 'E';
		int numOfEast = 0;
		int numOfNorth = 0;

		for (String op : input) {
			char direction = op.charAt(0);
			int dist = Integer.parseInt(op.substring(1));

			if (direction == 'F') {
				direction = currentOrientation;
			}

			switch (direction) {
				case 'E':
					numOfEast += dist;
					break;
				case 'W':
					numOfEast -= dist;
					break;
				case 'N':
					numOfNorth += dist;
					break;
				case 'S':
					numOfNorth -= dist;
					break;
				case 'L':
					dist = 360 - dist;
				case 'R':
					currentOrientation = changeOrientationByDegreeToRight(currentOrientation, dist);
					break;
			}
		}
		Helper.printResultPart1(String.valueOf(Math.abs(numOfEast) + Math.abs(numOfNorth)));
	}


	private static char changeOrientationByDegreeToRight(char currentOrientation, int dist) {

		int movePositions = dist / 90;

		for (int i = 0; i < POS_MAP.length; i++) {
			if (POS_MAP[i] == currentOrientation) {
				return POS_MAP[(i + movePositions) % POS_MAP.length];
			}
		}
		throw new IllegalArgumentException("cant handle char " + currentOrientation);
	}

	private static void solvePart2(List<String> input) {

		int shipNSPos = 0;
		int shipEWPos = 0;

		int waypointNSPos = 1;
		int waypointEWPos = 10;

		for (String op : input) {
			char direction = op.charAt(0);
			int dist = Integer.parseInt(op.substring(1));
			switch (direction) {
				case 'E':
					waypointEWPos += dist;
					break;
				case 'W':
					waypointEWPos -= dist;
					break;
				case 'N':
					waypointNSPos += dist;
					break;
				case 'S':
					waypointNSPos -= dist;
					break;
				case 'F':
					shipNSPos += dist * waypointNSPos;
					shipEWPos += dist * waypointEWPos;
					break;
				case 'L':
					dist = 360 - dist;
				case 'R':
					while (dist > 0) {
							int tmp = waypointNSPos;
							waypointNSPos = -1 * waypointEWPos;
							waypointEWPos = tmp;
						dist -= 90;
					}
					break;
			}
			System.out.println("command " + op);
			System.out.println("ship " + shipNSPos + " " + shipEWPos);
			System.out.println("waypoint " + waypointNSPos + " " + waypointEWPos);
		}
		Helper.printResultPart2(String.valueOf( Math.abs(shipNSPos) + Math.abs(shipEWPos)));
	}

}
