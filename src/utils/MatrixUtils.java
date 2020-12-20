package utils;

public class MatrixUtils {

	public static boolean[][] rotateMatrixToRight(boolean[][] matrix) {

		boolean[][] newThing = new boolean[matrix[0].length][matrix.length];
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[0].length; c++) {
				newThing[c][matrix.length - 1 - r] = matrix[r][c];
			}
		}
		return newThing;
	}


	public static boolean[][] flipMatrixLeftRight(boolean[][] matrix) {

		boolean[][] newThing = new boolean[matrix.length][matrix[0].length];
		for (int r = 0; r < matrix.length * 0.5; r++) {
			for (int c = 0; c < matrix[0].length; c++) {
				newThing[r][c] = matrix[matrix.length - 1 - r][c];
				newThing[matrix.length - 1 - r][c] = matrix[r][c];
			}
		}
		return newThing;
	}

}
