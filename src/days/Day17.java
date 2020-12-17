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
		Helper.printResultPart1(String.valueOf(numberOfActivePointsPart2));
	}


	private static int solve(List<String> input, boolean enable4thDim) {

		Set<Point> points = new HashSet<>();

		Point minValues = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
		Point maxValues = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);

		for (int x = 0; x < input.size(); x++) {
			char[] row = input.get(x).toCharArray();
			for (int y = 0; y < row.length; y++) {
				if (row[y] == '#') {
					Point p = new Point(x, y, 0, 0);
					points.add(p);
					minValues.x = Math.min(p.x, minValues.x);
					minValues.y = Math.min(p.y, minValues.y);
					maxValues.x = Math.max(p.x, maxValues.x);
					maxValues.y = Math.max(p.y, maxValues.y);
				}
			}
		}

		for (int cycle = 0; cycle < 6; cycle++) {
			points = calculateNextCycle(enable4thDim, points, minValues, maxValues);
		}
		return points.size();
	}


	private static Set<Point> calculateNextCycle(boolean enable4thDim, Set<Point> points, Point minValues, Point maxValues) {

		Set<Point> nextCyclesPoints = new HashSet<>();

		minValues.addDelteToEveryPoint(-1, enable4thDim);
		maxValues.addDelteToEveryPoint(1, enable4thDim);

		for (int x = minValues.x; x <= maxValues.x; x++) {
			for (int y = minValues.y; y <= maxValues.y; y++) {
				for (int z = minValues.z; z <= maxValues.z; z++) {
					for (int w = minValues.w; w <= maxValues.w; w++) {
						Point currentP = new Point(x, y, z, w);
						int neigh = countNeighbors(points, currentP, enable4thDim);
						if (neigh == 3 || points.contains(currentP) && neigh == 2) {
							nextCyclesPoints.add(currentP);
						}
					}
				}
			}
		}
		return nextCyclesPoints;
	}


	private static int countNeighbors(Set<Point> points, Point currentP, boolean enable4thDim) {

		int neighbors = 0;
		int minWDelta = 0, maxWDelta = 0;
		if (enable4thDim) {
			minWDelta = -1;
			maxWDelta = 1;
		}
		Point tmp = new Point(currentP.x, currentP.y, currentP.z, currentP.w);
		for (int xd = -1; xd <= 1; xd++) {
			tmp.x = currentP.x + xd;
			for (int yd = -1; yd <= 1; yd++) {
				tmp.y = currentP.y + yd;
				for (int zd = -1; zd <= 1; zd++) {
					tmp.z = currentP.z + zd;
					for (int wd = minWDelta; wd <= maxWDelta; wd++) {
						tmp.w = currentP.w + wd;
						if (points.contains(tmp)) {
							neighbors++;
						}
					}
				}
			}
		}
		if (points.contains(currentP)) {
			neighbors--;
		}

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


		public void addDelteToEveryPoint(int delta, boolean wAsWell) {

			x += delta;
			y += delta;
			z += delta;
			if (wAsWell) {
				w += delta;
			}
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
