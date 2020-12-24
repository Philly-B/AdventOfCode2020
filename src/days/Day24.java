package days;

import datastructures.IntPair;
import general.Helper;

import java.util.List;

public class Day24 {

    private static final Integer MAX_N = 1000;

    public static void main(String[] args) {

        List<String> input = Helper.readFile("./data/days/day24p1.txt");

        boolean[][] grid = initGridWithInput(input);

        solvePart1(grid);

        solvePart2(grid);

    }

    private static void solvePart1(boolean[][] grid) {
        int countBlackTiles = countNumberOfBlackTiles(grid);

        Helper.printResultPart1(String.valueOf(countBlackTiles));
    }

    private static void solvePart2(boolean[][] grid) {
        int countBlackTiles;
        int blackTilesOnLastDay = 0;
        for (int day = 1; day <= 100; day++) {

            grid = perfromDayAction(grid);
            countBlackTiles = countNumberOfBlackTiles(grid);

            blackTilesOnLastDay = countBlackTiles;
        }

        Helper.printResultPart2(String.valueOf(blackTilesOnLastDay));
    }

    private static boolean[][] perfromDayAction(boolean[][] grid) {

        boolean[][] nextDaysGrid = new boolean[MAX_N][MAX_N];

        for (int r = 0; r < MAX_N; r++) {
            int cStart = r % 2 == 1 ? 1 : 0;
            for (int c = cStart; c < MAX_N; c += 2) {
                int blackNeighbors = countBlackNeighborCells(grid, r, c);
                if (grid[r][c] && (blackNeighbors == 0 || blackNeighbors > 2)
                        || !grid[r][c] && blackNeighbors == 2) {
                    nextDaysGrid[r][c] = !grid[r][c];
                } else {
                    nextDaysGrid[r][c] = grid[r][c];
                }
            }
        }
        return nextDaysGrid;
    }

    private static int countNumberOfBlackTiles(boolean[][] grid) {
        int countBlackTiles = 0;
        for (
                int row = 0;
                row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                countBlackTiles += grid[row][col] ? 1 : 0;
            }
        }
        return countBlackTiles;
    }

    private static boolean[][] initGridWithInput(List<String> input) {
        boolean[][] grid = new boolean[MAX_N][MAX_N];
        IntPair refCell = new IntPair(MAX_N / 2, MAX_N / 2);
        for (String tileRef : input) {

            // first is row, second is column
            IntPair currCell = new IntPair(refCell);
            for (int i = 0; i < tileRef.length(); i++) {

                char currentChar = tileRef.charAt(i);
                char nextChar = i + 1 < tileRef.length() ? tileRef.charAt(i + 1) : '-';

                String command = "" + currentChar;
                if (currentChar == 'n' || currentChar == 's') {
                    command += nextChar;
                    i++;
                }
                processWayDirection(currCell, command);
            }
            grid[currCell.first][currCell.second] = !grid[currCell.first][currCell.second];
        }
        return grid;
    }

    private static void processWayDirection(IntPair currCell, String command) {
        switch (command) {
            case "ne":
                currCell.addDelta(1, 1);
                break;
            case "se":
                currCell.addDelta(1, -1);
                break;
            case "e":
                currCell.addDelta(2, 0);
                break;
            case "w":
                currCell.addDelta(-2, 0);
                break;
            case "nw":
                currCell.addDelta(-1, 1);
                break;
            case "sw":
                currCell.addDelta(-1, -1);
                break;
        }
    }


    private static int countBlackNeighborCells(boolean[][] grid, int row, int col) {

        int blackCells = 0;
        for (int rowDelta = -2; rowDelta <= 2; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                if (Math.abs(rowDelta) + Math.abs(colDelta) == 2
                        && isValidCell(row + rowDelta, col + colDelta)) {
                    blackCells += grid[row + rowDelta][col + colDelta] ? 1 : 0;
                }
            }
        }
        return blackCells;
    }

    private static boolean isValidCell(int row, int col) {
        return row >= 0 && row < MAX_N && col >= 0 && col < MAX_N;
    }


}
