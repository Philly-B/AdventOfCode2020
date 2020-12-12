package days;

import java.util.List;

import general.Helper;

public class Day12 {

	public static final int[][] movement = new int[][] {
			{ 1, 0 }, // N
			{ 0, 1 }, // E
			{ -1, 0 }, // S
			{ 0, -1 },// W
			{0,0} // no move
			 };


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day12p1.txt");

		int resultPart1 = solve(input, true, 0, 1);
		Helper.printResultPart1(String.valueOf(resultPart1));
		int resultPart2 = solve(input, false, 1, 10);
		Helper.printResultPart2(String.valueOf(resultPart2));
	}


	private static int solve(List<String> input, boolean moveShip, int initialWayPointNS, int initialWayPointEW) {

		int[] shipPos = new int[2]; // first is NS second is EW
		int[] waypointPos = new int[] {initialWayPointNS, initialWayPointEW}; // first is NS second is EW

		for (String op : input) {
			char direction = op.charAt(0);
			int dist = Integer.parseInt(op.substring(1));

			int[] moveToDo = movement[4];

			switch (direction) {
				case 'N':
					moveToDo = movement[0];
					break;
				case 'E':
					moveToDo = movement[1];
					break;
				case 'S':
					moveToDo = movement[2];
					break;
				case 'W':
					moveToDo = movement[3];
					break;
				case 'F':
					movePointToWaypoint(shipPos, dist, waypointPos);
					break;
				case 'L':
					dist = 360 - dist;
				case 'R':
					rotateWayPoint(waypointPos, dist);
					break;
			}
			if (moveShip) {
				movePointToWaypoint(shipPos, dist, moveToDo);
			} else {
				movePointToWaypoint(waypointPos, dist, moveToDo);
			}
		}
		return Math.abs(shipPos[0]) + Math.abs(shipPos[1]);
	}

	private static void movePointToWaypoint(int[] position, int moveDist, int[] moveBy) {
		position[0] += moveDist * moveBy[0];
		position[1] += moveDist * moveBy[1];
	}

	private static void rotateWayPoint(int[] waypointPos, int dist) {

		while (dist > 0) {
			int tmp = waypointPos[0];
			waypointPos[0] = -1 * waypointPos[1];
			waypointPos[1] = tmp;
			dist -= 90;
		}
	}

}
