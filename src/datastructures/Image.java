package datastructures;

import java.util.HashSet;
import java.util.Set;

import utils.MatrixUtils;

public class Image {

	public int imageId;
	public boolean[][] binImg;
	public String[] borders;
	public Set<String> possibleBorders = new HashSet<>();

	private boolean[][] binImgOrig;


	public Image(int imageId) {

		this.imageId = imageId;
	}


	@Override
	public String toString() {

		return String.valueOf(imageId);
	}


	public void flipLeftRight() {

		binImg = MatrixUtils.flipMatrixLeftRight(binImg);
		updateBorders();
	}


	public void rotate90ToRight() {

		binImg = MatrixUtils.rotateMatrixToRight(binImg);
		updateBorders();
	}


	public void removeRow(int row) {

		boolean[][] newImg = new boolean[binImg.length - 1][binImg[0].length];
		int delta = 0;
		for (int r = 0; r < binImg.length; r++) {
			for (int c = 0; c < binImg.length; c++) {
				if (r == row) {
					delta = 1;
					continue;
				}
				newImg[r - delta][c] = binImg[r][c];
			}
		}
		binImg = newImg;
	}


	public void removeCol(int col) {

		boolean[][] newImg = new boolean[binImg.length][binImg[0].length - 1];
		int delta = 0;
		for (int r = 0; r < binImg.length; r++) {
			delta = 0;
			for (int c = 0; c < binImg[0].length; c++) {
				if (c == col) {
					delta = 1;
					continue;
				}
				newImg[r][c - delta] = binImg[r][c];
			}
		}
		binImg = newImg;
	}


	private void updateBorders() {

		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		StringBuilder top = new StringBuilder();
		StringBuilder bottom = new StringBuilder();
		String[] currentSetting;
		for (int r = 0; r < binImg.length; r++) {
			for (int c = 0; c < binImg[0].length; c++) {
				if (r == 0) {
					top.append(binImg[r][c] ? '#' : '.');
				}
				if (r == binImg.length - 1) {
					bottom.append(binImg[r][c] ? '#' : '.');
				}
				if (c == 0) {
					left.append(binImg[r][c] ? '#' : '.');
				}
				if (c == binImg[0].length - 1) {
					right.append(binImg[r][c] ? '#' : '.');
				}
			}
		}
		borders = new String[] { top.toString(), right.toString(), bottom.toString(), left.toString() };
	}


	public void reset() {

		for (int r = 0; r < binImg.length; r++) {
			for (int c = 0; c < binImg[0].length; c++) {
				binImg[r][c] = binImgOrig[r][c];
			}
		}
	}


	public void setBinImg(boolean[][] binImg) {

		this.binImg = binImg;
		binImgOrig = new boolean[binImg.length][binImg[0].length];
		for (int r = 0; r < binImg.length; r++) {
			for (int c = 0; c < binImg[0].length; c++) {
				binImgOrig[r][c] = binImg[r][c];
			}
		}
		updateBorders();
		setAllPossibleBorders();
	}


	private void setAllPossibleBorders() {
		for (String border : borders) {
			possibleBorders.add(border);
			possibleBorders.add(new StringBuilder(border).reverse().toString());
		}
	}
}
