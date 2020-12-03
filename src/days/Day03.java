package days;

import java.util.Arrays;
import java.util.List;

import general.Helper;

public class Day03 {

	public static void main(String[] args) {

		List<String> lines = general.Helper.readFile("./data/days/day03p1.txt");
		boolean[][] field = convertToMatrix(lines);
		int n = lines.size();
		int m = lines.get(0).length();

		solvePart1(field, n, m);
		solvePart2(field, n, m);
	}


	private static void solvePart2(boolean[][] field, int n, int m) {

		long treeCountSum = 	solve(field, n, m, 1, 1) *
						solve(field, n, m, 3, 1) *
						solve(field, n, m, 5, 1) *
						solve(field, n, m, 7, 1) *
						solve(field, n, m, 1, 2);

		Helper.printResultPart2(String.valueOf(treeCountSum));
	}


	private static void solvePart1(boolean[][] field, int n, int m) {

		long treeCount = solve(field, n, m, 3, 1);

		Helper.printResultPart1(String.valueOf(treeCount));
	}


	private static long solve(boolean[][] field, int n, int m, int increaseCol, int increaseRow) {

		int posRow = increaseRow;
		int posCol = increaseCol;

		long treeCount = 0;
		while (posRow < n) {
			if (field[posRow][posCol]) {
				treeCount++;
			}

			posRow += increaseRow;
			posCol = (posCol + increaseCol) % m;
		}

		return treeCount;
	}


	private static boolean[][] convertToMatrix(List<String> lines) {

		int rows = lines.size();
		int cols = lines.get(0).length();

		boolean[][] field = new boolean[rows][cols];

		for (int r = 0; r < rows; r++) {
			char[] row = lines.get(r).toCharArray();
			for (int c = 0; c < cols; c++) {
				field[r][c] = row[c] == '#';
			}
		}

		return field;
	}

}
