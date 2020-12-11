package days;

import java.util.List;
import java.util.function.Function;

import general.Helper;

public class Day11 {

	private static int ROWS;
	private static int COLS;


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day11p1.txt");
		ROWS = input.size();
		COLS = input.get(0).length();

		// . floor = 0
		// L seat empty = 1
		// seat full = 2
		int[][] grid = initGrid(input);
		int currentOccupiedSeats1 = calculateOccupiedSeatsInStablePosition(grid, Day11::calcOccupiedPart1, 4);
		Helper.printResultPart1(String.valueOf(currentOccupiedSeats1));

		int currentOccupiedSeats2 = calculateOccupiedSeatsInStablePosition(grid, Day11::calcOccupiedPart2, 5);
		Helper.printResultPart2(String.valueOf(currentOccupiedSeats2));

	}


	private static int calculateOccupiedSeatsInStablePosition(int[][] grid, Function<GraphCellWrapper, Integer> calcOccupiedNeighbors, int numOfOccupiedOk) {

		int currentOccupiedSeats = 0;
		boolean somethingChanges = true;
		GraphCellWrapper graphCellWrapper = new GraphCellWrapper();
		while (somethingChanges) {
			somethingChanges = false;
			currentOccupiedSeats = 0;

			graphCellWrapper.graph = grid;

			int[][] nextGrid = new int[ROWS][COLS];
			for (int r = 0; r < ROWS; r++) {
				graphCellWrapper.row = r;
				for (int c = 0; c < COLS; c++) {
					if (grid[r][c] == 2) {
						currentOccupiedSeats++;
					}
					graphCellWrapper.col = c;
					int numberOfAdjacentOccupied = calcOccupiedNeighbors.apply(graphCellWrapper);
					if (grid[r][c] == 1 && numberOfAdjacentOccupied == 0) {
						nextGrid[r][c] = 2;
						somethingChanges = true;
					} else if (grid[r][c] == 2 && numberOfAdjacentOccupied >= numOfOccupiedOk) {
						nextGrid[r][c] = 1;
						somethingChanges = true;
					} else {
						nextGrid[r][c] = grid[r][c];
					}
				}
			}
			grid = nextGrid;

		}
		return currentOccupiedSeats;
	}


	private static int calcOccupiedPart1(GraphCellWrapper graphCellWrapper) {

		int[][] grid = graphCellWrapper.graph;
		int row = graphCellWrapper.row;
		int col = graphCellWrapper.col;
		int occupied = 0;
		for (int rd = -1; rd <= 1; rd++) {
			for (int cd = -1; cd <= 1; cd++) {
				if (rd == 0 && cd == 0) {
					continue;
				}
				int currentRow = row + rd;
				int currentCol = col + cd;
				if (isValidCell(currentRow, currentCol)
						&& grid[currentRow][currentCol] == 2) {
					occupied++;
				}
			}
		}
		return occupied;
	}


	private static int calcOccupiedPart2(GraphCellWrapper graphCellWrapper) {

		int[][] grid = graphCellWrapper.graph;
		int row = graphCellWrapper.row;
		int col = graphCellWrapper.col;
		int occupied = 0;
		for (int rd = -1; rd <= 1; rd++) {
			for (int cd = -1; cd <= 1; cd++) {
				if (rd == 0 && cd == 0) {
					continue;
				}
				int currentRow = row + rd;
				int currentCol = col + cd;
				while (isValidCell(currentRow, currentCol)
						&& grid[currentRow][currentCol] == 0) {
					currentRow += rd;
					currentCol += cd;
				}
				if (isValidCell(currentRow, currentCol) && grid[currentRow][currentCol] == 2) {
					occupied++;
				}
			}
		}
		return occupied;
	}


	private static boolean isValidCell(int currentRow, int currentCol) {

		return currentRow >= 0 && currentRow < ROWS
				&& currentCol >= 0 && currentCol < COLS;
	}


	private static int[][] initGrid(List<String> input) {

		int[][] grid = new int[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
			char[] charsOfRow = input.get(r).toCharArray();
			for (int c = 0; c < COLS; c++) {
				if (charsOfRow[c] == '.') {
					grid[r][c] = 0;
				} else {
					grid[r][c] = 1;
				}
			}
		}
		return grid;
	}


	private static class GraphCellWrapper {

		int[][] graph;
		int row;
		int col;
	}

}
