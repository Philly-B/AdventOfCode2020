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

		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		int minZ = 0, maxZ = 0;
		int minW = 0, maxW = 0;

		for (int x = 0; x < input.size(); x++) {
			char[] row = input.get(x).toCharArray();
			for (int y = 0; y < row.length; y++) {
				if (row[y] == '#') {
					Point p = new Point(x, y, 0, 0);
					points.add(p);
					minX = Math.min(p.x, minX);
					minY = Math.min(p.y, minY);
					maxX = Math.max(p.x, maxX);
					maxY = Math.max(p.y, maxY);
				}
			}
		}

		for (int cycle = 0; cycle < 6; cycle++) {
			Set<Point> thisCyclesPoints = new HashSet<>();

			minX--;
			minY--;
			minZ--;
			maxX++;
			maxY++;
			maxZ++;
			if (enable4thDim) {
				minW--;
				maxW++;
			}

			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						for (int w = minW; w <= maxW; w++) {
							Point currentP = new Point(x, y, z, w);
							int neigh = countNeighbors(points, currentP, enable4thDim);
							if (neigh == 3 ||
									points.contains(currentP) && neigh == 2) {
								thisCyclesPoints.add(currentP);
							}
						}
					}
				}
			}
			points = thisCyclesPoints;
		}
		return points.size();
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
