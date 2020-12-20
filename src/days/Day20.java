package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import datastructures.Image;
import general.Helper;
import utils.MatrixUtils;

public class Day20 {

	public static final int IMAGE_SIZE = 10;

	private static final String[] whale = new String[] {
			"                  # ",
			"#    ##    ##    ###",
			" #  #  #  #  #  #   "
	};


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day20p1.txt");
		input.add("");

		Map<Integer, Image> imageIDToImage = readInput(input);
		Map<String, List<Image>> borderToImages = createMapOfBorders(imageIDToImage);
		Map<Integer, Set<Integer>> imageIdToNeigh = createMapOfNeighbors(borderToImages);

		resolvePart1(imageIdToNeigh);
		resolvePart1(imageIDToImage, imageIdToNeigh);

	}


	private static void resolvePart1(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh) {

		Image[][] gridOfImages = buildActualGridOfImages(imageIDToImage, imageIdToNeigh);
		removeBorders(gridOfImages);
		boolean[][] bigImg = createBigImage(gridOfImages);
		boolean[][] whaleForm = createWhaleForm();

		int whaleCount = findImgOrientationWithWhalesAndGetTheirNumber(bigImg, whaleForm);
		int numberOfHashtags = countNumberOfHashtags(bigImg);
		int numberOfHashtagsInAWhale = countNumberOfHashtags(whaleForm);

		Helper.printResultPart2(String.valueOf(numberOfHashtags - numberOfHashtagsInAWhale * whaleCount));
	}


	private static boolean[][] createWhaleForm() {

		boolean[][] whaleForm = new boolean[whale.length][whale[0].length()];
		for (int i = 0; i < whale.length; i++) {
			for (int j = 0; j < whale[0].length(); j++) {
				whaleForm[i][j] = whale[i].charAt(j) == '#';
			}
		}
		return whaleForm;
	}


	private static int findImgOrientationWithWhalesAndGetTheirNumber(boolean[][] bigImg, boolean[][] whaleForm) {

		for (int i = 0; i < 11; i++) {
			int whaleCount = countWhalesInImg(bigImg, whaleForm);
			if (whaleCount != 0) {
				return whaleCount;
			}
			bigImg = MatrixUtils.rotateMatrixToRight(bigImg);
			if (i == 3) {
				bigImg = MatrixUtils.flipMatrixLeftRight(bigImg);
			} else if (i == 7) {
				bigImg = MatrixUtils.flipMatrixLeftRight(bigImg);
				bigImg = MatrixUtils.flipMatrixLeftRight(bigImg);
			}
		}

		throw new RuntimeException("no whale found");
	}


	private static int countWhalesInImg(boolean[][] bigImg, boolean[][] whaleForm) {
		int whaleCount = 0;
		for (int r = whaleForm.length; r < bigImg.length; r++) {
			for (int c = 0; c < bigImg.length - whaleForm[0].length; c++) {
				if (isWhaleInside(bigImg, whaleForm, r, c)) {
					whaleCount++;
				}
			}
		}
		return whaleCount;
	}


	private static boolean isWhaleInside(boolean[][] bigImg, boolean[][] whaleForm, int r, int c) {

		boolean isOk = true;
		for (int rw = whaleForm.length - 1; isOk && rw >= 0; rw--) {
			for (int cw = 0; isOk && cw < whaleForm[0].length; cw++) {
				if (!whaleForm[rw][cw]) {
					continue;
				}
				if (whaleForm[rw][cw] && !bigImg[r - ((whaleForm.length - 1) - rw)][c + cw]) {
					isOk = false;
					break;
				}
			}
		}
		return isOk;
	}


	private static int countNumberOfHashtags(boolean[][] matrix) {

		int numberOfHashtags = 0;
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[0].length; c++) {
				if (matrix[r][c]) {
					numberOfHashtags++;
				}
			}
		}
		return numberOfHashtags;
	}


	private static boolean[][] createBigImage(Image[][] gridOfImages) {

		int newN = gridOfImages[0][0].binImg.length * gridOfImages.length;
		boolean[][] bigImg = new boolean[newN][newN];

		int currRow = 0;
		int currCol = 0;
		int currImageRow = 0;
		int currImageCol = 0;
		for (int r = 0; r < newN; r++) {
			for (int c = 0; c < newN; c++) {
				bigImg[r][c] = gridOfImages[currImageRow][currImageCol].binImg[currRow][currCol];
				currCol++;
				if (currCol == gridOfImages[currImageRow][currImageCol].binImg[currRow].length) {
					currImageCol++;
					currCol = 0;
				}
			}
			currImageCol = 0;
			currCol = 0;
			currRow++;
			if (currRow == gridOfImages[currImageRow][currImageCol].binImg.length) {
				currImageRow++;
				currRow = 0;
			}
		}
		return bigImg;
	}


	private static void removeBorders(Image[][] gridOfImageIds) {

		for (int r = 0; r < gridOfImageIds.length; r++) {
			for (int c = 0; c < gridOfImageIds.length; c++) {
				gridOfImageIds[r][c].removeRow(0);
				gridOfImageIds[r][c].removeRow(gridOfImageIds[r][c].binImg.length - 1);
				gridOfImageIds[r][c].removeCol(0);
				gridOfImageIds[r][c].removeCol(gridOfImageIds[r][c].binImg[0].length - 1);
			}
		}
	}


	private static Image[][] buildActualGridOfImages(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh) {

		int numberOfImagesPerRow = (int) Math.sqrt(imageIDToImage.size());
		Image[][] gridOfImageIds = new Image[numberOfImagesPerRow][numberOfImagesPerRow];

		Set<Integer> corners = getAllImagesWithNumberOfNeighAsIntSet(imageIdToNeigh, 2);
		Set<Integer> borders = getAllImagesWithNumberOfNeighAsIntSet(imageIdToNeigh, 3);
		Set<Integer> midImage = getAllImagesWithNumberOfNeighAsIntSet(imageIdToNeigh, 4);

		int upperLeftCornerImageId = corners.iterator().next();

		Set<Integer> alreadyPlaced = new HashSet<>();
		setTopLeftCorner(imageIDToImage, imageIdToNeigh, gridOfImageIds, upperLeftCornerImageId, alreadyPlaced);
		setTopAndBottomBorder(imageIDToImage, imageIdToNeigh, gridOfImageIds, corners, borders, alreadyPlaced);
		setBottomAndRightBorder(imageIDToImage, imageIdToNeigh, gridOfImageIds, corners, borders, alreadyPlaced);
		setRestOfImage(imageIDToImage, imageIdToNeigh, gridOfImageIds, midImage, alreadyPlaced);

		return calcValidGridOfImageIds(numberOfImagesPerRow, gridOfImageIds);
	}


	private static Image[][] calcValidGridOfImageIds(int numberOfImagesPerRow, Image[][] gridOfImageIds) {

		boolean isOk = false;
		for (int i = 0; i < 11; i++) {
			gridOfImageIds = rotateImages(numberOfImagesPerRow, gridOfImageIds);
			if (i == 3) {
				flipImages(numberOfImagesPerRow, gridOfImageIds);
			}

			isOk = findValidImageIdOrdering(gridOfImageIds);
			if (isOk) {
				break;
			}
		}
		if (!isOk) {
			throw new RuntimeException();
		}
		return gridOfImageIds;
	}


	private static Image[][] rotateImages(int numberOfImagesPerRow, Image[][] gridOfImageIds) {

		Image[][] newThing = new Image[numberOfImagesPerRow][numberOfImagesPerRow];
		for (int r = 0; r < numberOfImagesPerRow; r++) {
			for (int c = 0; c < numberOfImagesPerRow; c++) {
				newThing[c][numberOfImagesPerRow - 1 - r] = gridOfImageIds[r][c];
			}
		}
		return newThing;
	}


	private static void flipImages(int numberOfImagesPerRow, Image[][] gridOfImageIds) {

		Image tmp;
		for (int r = 0; r < numberOfImagesPerRow * 0.5; r++) {
			for (int c = 0; c < numberOfImagesPerRow; c++) {
				tmp = gridOfImageIds[r][c];
				gridOfImageIds[r][c] = gridOfImageIds[numberOfImagesPerRow - 1 - r][c];
				gridOfImageIds[numberOfImagesPerRow - 1 - r][c] = tmp;
			}
		}
	}


	private static boolean findValidImageIdOrdering(Image[][] gridOfImageIds) {

		boolean isOK = false;
		int i = 0;
		while (i < 11) {
			isOK = fixOrientation(gridOfImageIds, 1, 0) && fixOrientation(gridOfImageIds, 0, 1);
			if (isOK) {
				break;
			}
			for (int r = 0; r < gridOfImageIds.length; r++) {
				for (int c = 0; c < gridOfImageIds.length; c++) {
					if (r == 0 && c == 0) {
						continue;
					}
					gridOfImageIds[r][c].reset();
				}
			}

			gridOfImageIds[0][0].rotate90ToRight();
			if (i == 3) {
				gridOfImageIds[0][0].flipLeftRight();
			} else if (i == 7) {
				gridOfImageIds[0][0].reset();
				gridOfImageIds[0][0].rotate90ToRight();
				gridOfImageIds[0][0].flipLeftRight();
			}
			i++;
		}
		return isOK;
	}


	private static boolean fixOrientation(Image[][] gridOfImageIds, int row, int col) {

		if (!isValid(row, col, gridOfImageIds.length)) {
			return true;
		}

		boolean isOk = false;
		for (int i = 0; i < 11; i++) {
			isOk = true;
			if (isOk && isValid(row - 1, col, gridOfImageIds.length)) {
				isOk = gridOfImageIds[row - 1][col].borders[2].equals(gridOfImageIds[row][col].borders[0]);
			}
			if (isOk && isValid(row, col - 1, gridOfImageIds.length)) {
				isOk = gridOfImageIds[row][col - 1].borders[1].equals(gridOfImageIds[row][col].borders[3]);
			}
			if (isOk) {
				if (row == 0) {
					isOk = fixOrientation(gridOfImageIds, row + 1, col) && fixOrientation(gridOfImageIds, row, col + 1);
				} else {
					isOk = fixOrientation(gridOfImageIds, row + 1, col);
				}
			}
			if (isOk) {
				return true;
			}

			gridOfImageIds[row][col].rotate90ToRight();
			if (i == 3) {
				gridOfImageIds[row][col].flipLeftRight();
			} else if (i == 7) {
				gridOfImageIds[row][col].reset();
				gridOfImageIds[row][col].rotate90ToRight();
				gridOfImageIds[row][col].flipLeftRight();
			}
		}
		return false;
	}


	private static boolean isValid(int row, int col, int range) {

		return row >= 0 && row < range && col >= 0 && col < range;
	}


	private static void setRestOfImage(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> midImage, Set<Integer> alreadyPlaced) {

		for (int r1 = 1; r1 < gridOfImageIds.length - 1; r1++) {
			for (int c1 = 1; c1 < gridOfImageIds.length - 1; c1++) {
				Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[r1 - 1][c1].imageId);
				posNeigh.retainAll(imageIdToNeigh.get(gridOfImageIds[r1][c1 - 1].imageId));
				posNeigh.retainAll(midImage);
				posNeigh.removeAll(alreadyPlaced);
				gridOfImageIds[r1][c1] = imageIDToImage.get(posNeigh.iterator().next());
				alreadyPlaced.add(gridOfImageIds[r1][c1].imageId);
			}
		}
	}


	private static void setTopLeftCorner(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh, Image[][] gridOfImageIds,
			int upperLeftCornerImageId,
			Set<Integer> alreadyPlaced) {

		gridOfImageIds[0][0] = imageIDToImage.get(upperLeftCornerImageId);
		Integer[] neigh = imageIdToNeigh.get(gridOfImageIds[0][0].imageId).toArray(new Integer[0]);
		gridOfImageIds[1][0] = imageIDToImage.get(neigh[0]);
		gridOfImageIds[0][1] = imageIDToImage.get(neigh[1]);
		alreadyPlaced.add(upperLeftCornerImageId);
		alreadyPlaced.add(neigh[0]);
		alreadyPlaced.add(neigh[1]);

	}


	private static void setBottomAndRightBorder(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> corners, Set<Integer> borders, Set<Integer> alreadyPlaced) {

		for (int i = 1; i < gridOfImageIds.length; i++) {
			Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[gridOfImageIds.length - 1][i - 1].imageId);
			posNeigh.removeAll(alreadyPlaced);
			Integer actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[gridOfImageIds.length - 1][i] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);

			if (i == gridOfImageIds.length - 1) {
				continue;
			}

			posNeigh = imageIdToNeigh.get(gridOfImageIds[i - 1][gridOfImageIds.length - 1].imageId);
			posNeigh.removeAll(alreadyPlaced);
			actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[i][gridOfImageIds.length - 1] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);
		}
	}


	private static void setTopAndBottomBorder(Map<Integer, Image> imageIDToImage, Map<Integer, Set<Integer>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> corners, Set<Integer> borders, Set<Integer> alreadyPlaced) {

		for (int i = 2; i < gridOfImageIds.length; i++) {
			Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[0][i - 1].imageId);
			posNeigh.removeAll(alreadyPlaced);
			Integer actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[0][i] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);

			posNeigh = imageIdToNeigh.get(gridOfImageIds[i - 1][0].imageId);
			posNeigh.removeAll(alreadyPlaced);
			actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[i][0] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);
		}
	}


	private static void resolvePart1(Map<Integer, Set<Integer>> imageIdToNeigh) {

		long result = getAllImagesWithNumberOfNeigh(imageIdToNeigh, 2)
				.reduce(1, (l1, l2) -> l1 * l2);

		Helper.printResultPart1(String.valueOf(result));
	}


	private static Set<Integer> getAllImagesWithNumberOfNeighAsIntSet(
			Map<Integer, Set<Integer>> imageIdToNeigh,
			int numberOfNeigh) {

		return getAllImagesWithNumberOfNeigh(imageIdToNeigh, numberOfNeigh)
				.boxed()
				.map(Long::intValue)
				.collect(Collectors.toSet());
	}


	private static LongStream getAllImagesWithNumberOfNeigh(
			Map<Integer, Set<Integer>> imageIdToNeigh,
			int numberOfNeigh) {

		return imageIdToNeigh
				.entrySet()
				.stream()
				.filter(entry -> entry.getValue().size() == numberOfNeigh)
				.mapToLong(Map.Entry::getKey);
	}


	private static Map<Integer, Set<Integer>> createMapOfNeighbors(Map<String, List<Image>> borderToImages) {

		Map<Integer, Set<Integer>> mapOfNeighbors = new HashMap<>();

		Iterator<Map.Entry<String, List<Image>>> iterator = borderToImages.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Image>> next = iterator.next();
			List<Image> neighbors = next.getValue();
			if (neighbors.size() == 1) {
				iterator.remove();
				continue;
			}

			if (neighbors.size() != 2) {
				throw new IllegalArgumentException();
			}

			Image first = neighbors.get(0);
			Image second = neighbors.get(1);

			mapOfNeighbors.putIfAbsent(first.imageId, new HashSet<>());
			mapOfNeighbors.get(first.imageId).add(second.imageId);

			mapOfNeighbors.putIfAbsent(second.imageId, new HashSet<>());
			mapOfNeighbors.get(second.imageId).add(first.imageId);
		}
		return mapOfNeighbors;
	}


	private static Map<String, List<Image>> createMapOfBorders(Map<Integer, Image> imageIDToImage) {

		Map<String, List<Image>> borderToImages = new HashMap<>();
		for (Image image : imageIDToImage.values()) {
			for (String posBorderPair : image.possibleBorders) {
				borderToImages.putIfAbsent(posBorderPair, new ArrayList<>());
				borderToImages.get(posBorderPair).add(image);
			}
		}
		return borderToImages;
	}


	private static Map<Integer, Image> readInput(List<String> input) {

		Map<Integer, Image> imageIDToImage = new HashMap<>();
		Image currentImg = null;
		boolean[][] binImg = new boolean[IMAGE_SIZE][IMAGE_SIZE];
		int currRow = 0;
		for (String line : input) {
			if (line.isEmpty() && currentImg != null) {
				currentImg.setBinImg(binImg);
				imageIDToImage.put(currentImg.imageId, currentImg);
			} else if (line.startsWith("Tile")) {
				currentImg = new Image(Integer.parseInt(line.substring(5, 9)));
				binImg = new boolean[IMAGE_SIZE][IMAGE_SIZE];
				currRow = 0;
			} else {
				char[] row = line.toCharArray();
				for (int i = 0; i < row.length; i++) {
					binImg[currRow][i] = row[i] == '#';
				}
				currRow++;
			}
		}

		return imageIDToImage;
	}

}
