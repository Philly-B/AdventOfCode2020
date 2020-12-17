package days;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import general.Helper;

public class Day17 {

	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day17p1.txt");

		int numberOfActivePointsPart1 = solve(input, false);
		Helper.printResultPart1(String.valueOf(numberOfActivePointsPart1));
		int numberOfActivePointsPart2 = solve(input, true);
		Helper.printResultPart2(String.valueOf(numberOfActivePointsPart2));
	}


	private static int solve(List<String> input, boolean enable4thDim) {

		Set<Point> points = new HashSet<>();
		Set<Point> pointsToConsiderInNextRound = initPoints(input, points, enable4thDim);

		for (int cycle = 0; cycle < 6; cycle++) {

			Set<Point> nextCyclesPoints = new HashSet<>();
			Set<Point> nextCyclesPointsToConsiderInNextRound = new HashSet<>();

			for (Point p : pointsToConsiderInNextRound) {
				handlePointForNextCycle(enable4thDim, points, nextCyclesPoints, nextCyclesPointsToConsiderInNextRound, p);
			}

			points = nextCyclesPoints;
			pointsToConsiderInNextRound = nextCyclesPointsToConsiderInNextRound;
		}
		return points.size();
	}


	private static void handlePointForNextCycle(boolean enable4thDim,
			Set<Point> points,
			Set<Point> nextCyclesPoints,
			Set<Point> nextCyclesPointsToConsiderInNextRound,
			Point p) {

		long activeNeighbors = calculateNeighbors(p, enable4thDim)
				.stream()
				.filter(points::contains)
				.count();

		if (activeNeighbors == 3 || points.contains(p) && activeNeighbors == 2) {
			nextCyclesPoints.add(p);
			nextCyclesPointsToConsiderInNextRound.addAll(calculateNeighbors(p, enable4thDim));
		}
	}


	private static Set<Point> initPoints(List<String> input, Set<Point> points, boolean enable4thDim) {

		Set<Point> pointsToConsiderInNextRound = new HashSet<>();
		for (int x = 0; x < input.size(); x++) {
			char[] row = input.get(x).toCharArray();
			for (int y = 0; y < row.length; y++) {
				if (row[y] == '#') {
					Point p = new Point(x, y, 0, 0);
					points.add(p);
					pointsToConsiderInNextRound.addAll(calculateNeighbors(p, enable4thDim));
				}
			}
		}
		return pointsToConsiderInNextRound;
	}


	private static Set<Point> calculateNeighbors(Point pointToCalcNeighbors, boolean enable4thDim) {

		Set<Point> neighbors = new HashSet<>();

		int minWDelta = -1;
		int maxWDelta = 1;
		if (!enable4thDim) {
			minWDelta = 0;
			maxWDelta = 0;
		}

		for (int xd = -1; xd <= 1; xd++) {
			for (int yd = -1; yd <= 1; yd++) {
				for (int zd = -1; zd <= 1; zd++) {
					for (int wd = minWDelta; wd <= maxWDelta; wd++) {
						neighbors.add(new Point(pointToCalcNeighbors.x + xd,
												pointToCalcNeighbors.y + yd,
												pointToCalcNeighbors.z + zd,
												pointToCalcNeighbors.w + wd));
					}
				}
			}
		}
		neighbors.remove(pointToCalcNeighbors);
		return neighbors;
	}


	private static class Point {

		int x;
		int y;
		int z;
		int w;


		public Point(int x, int y, int z, int w) {

			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}


		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Point point = (Point) o;
			return x == point.x &&
					y == point.y &&
					z == point.z &&
					w == point.w;
		}


		@Override
		public int hashCode() {

			return Objects.hash(x, y, z, w);
		}
	}

}
