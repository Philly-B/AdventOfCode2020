package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import datastructures.IntPair;
import general.Helper;

public class Day20 {

	public static final int IMAGE_SIZE = 10;


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day20p1.txt");
		input.add("");

		Map<Integer, Image> imageIDToImage = new HashMap<>();
		Image currentImg = null;
		boolean[][] binImg = new boolean[IMAGE_SIZE][IMAGE_SIZE];
		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		int currRow = 0;
		for (String line : input) {
			if (line.isEmpty() && currentImg != null) {
				currentImg.allBorders.add(left.toString());
				currentImg.allBorders.add(left.reverse().toString());
				currentImg.allBorders.add(right.toString());
				currentImg.allBorders.add(right.reverse().toString());
				right = new StringBuilder();
				left = new StringBuilder();
				imageIDToImage.put(currentImg.imageId, currentImg);
				currentImg = null;
				currRow = 0;
			} else if (line.startsWith("Tile")) {
				currentImg = new Image(Integer.parseInt(line.substring(5, 9)), binImg);
			} else {
				if (currRow == 0 || currRow == IMAGE_SIZE - 1) {
					currentImg.allBorders.add(line);
					currentImg.allBorders.add(new StringBuilder(line).reverse().toString());
				}
				char[] row = line.toCharArray();
				for (int i = 0; i < row.length; i++) {
					binImg[currRow][i] = row[i] == '#';
					if (i == 0) {
						left.append(row[i]);
					}
					if (i == row.length - 1) {
						right.append(row[row.length - 1]);
					}
				}
				currRow++;
			}
		}

		Map<String, List<Image>> borderToImages = new HashMap<>();
		for (Image image : imageIDToImage.values()) {
			for (String border : image.allBorders) {
				borderToImages.putIfAbsent(border, new ArrayList<>());
				borderToImages.get(border).add(image);
			}
		}


		Map<Integer, Set<Integer>> imageIdToNeigh = new HashMap<>();

		Iterator<Map.Entry<String, List<Image>>> iterator = borderToImages.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Image>> next = iterator.next();
			List<Image> neightbors = next.getValue();
			if (neightbors.size() == 1) {
				iterator.remove();
				continue;
			}

			if (neightbors.size() != 2) {
				throw new IllegalArgumentException();
			}

			imageIdToNeigh.putIfAbsent(neightbors.get(0).imageId, new HashSet<>());
			imageIdToNeigh.get(neightbors.get(0).imageId).add(neightbors.get(1).imageId);
			imageIdToNeigh.putIfAbsent(neightbors.get(1).imageId, new HashSet<>());
			imageIdToNeigh.get(neightbors.get(1).imageId).add(neightbors.get(0).imageId);
		}

		long result = 1;
		Iterator<Map.Entry<Integer, Set<Integer>>> imgNeighIter = imageIdToNeigh.entrySet().iterator();
		while (imgNeighIter.hasNext()) {
			Map.Entry<Integer, Set<Integer>> next = imgNeighIter.next();
			if (next.getValue().size() == 2) result *= next.getKey();
		}
		Helper.printResultPart1(String.valueOf(result));

	}


	private static class Image {

		int imageId;
		private boolean[][] binImg;
		Set<String> allBorders = new HashSet<>();


		public Image(int imageId, boolean[][] binImg) {

			this.imageId = imageId;
			this.binImg = binImg;
		}

	}
}
