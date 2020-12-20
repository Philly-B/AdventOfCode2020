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

import general.Helper;

public class Day20 {

	public static final int IMAGE_SIZE = 10;


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day20p1.txt");
		input.add("");

		Map<Integer, Image> imageIDToImage = readInput(input);
		Map<String, List<ImageBorderPosTriple>> borderToImages = createMapOfBorders(imageIDToImage);
		Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh = createMapOfNeighbors(borderToImages);

		resolvePart1(imageIdToNeigh);

		Image[][] gridOfImageIds = buildActualGridOfImages(imageIDToImage, imageIdToNeigh);

		// todo find rotation that fits
		// todo remove all borders
		// create one big image
		// count monsters contained -> result = number of hashtags - (number of monsters * hashtags in monsters)
	}


	private static Image[][] buildActualGridOfImages(Map<Integer, Image> imageIDToImage, Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh) {

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

		return gridOfImageIds;
	}


	private static void setRestOfImage(Map<Integer, Image> imageIDToImage, Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> midImage, Set<Integer> alreadyPlaced) {

		for (int r1 = 1; r1 < gridOfImageIds.length - 1; r1++) {
			for (int c1 = 1; c1 < gridOfImageIds.length - 1; c1++) {
				Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[r1 - 1][c1].imageId).keySet();
				posNeigh.retainAll(imageIdToNeigh.get(gridOfImageIds[r1][c1 - 1].imageId).keySet());
				posNeigh.retainAll(midImage);
				posNeigh.removeAll(alreadyPlaced);
				gridOfImageIds[r1][c1] = imageIDToImage.get(posNeigh.iterator().next());
				alreadyPlaced.add(gridOfImageIds[r1][c1].imageId);
			}
		}
	}


	private static void setTopLeftCorner(Map<Integer, Image> imageIDToImage, Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh, Image[][] gridOfImageIds, int upperLeftCornerImageId,
			Set<Integer> alreadyPlaced) {

		gridOfImageIds[0][0] = imageIDToImage.get(upperLeftCornerImageId);
		Integer[] neigh = imageIdToNeigh.get(gridOfImageIds[0][0].imageId).keySet().toArray(new Integer[0]);
		gridOfImageIds[1][0] = imageIDToImage.get(neigh[0]);
		gridOfImageIds[0][1] = imageIDToImage.get(neigh[1]);
		alreadyPlaced.add(upperLeftCornerImageId);
		alreadyPlaced.add(neigh[0]);
		alreadyPlaced.add(neigh[1]);

		correctImageOrientation(imageIdToNeigh, gridOfImageIds, 0, 0);

	}


	private static void correctImageOrientation(Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh, Image[][] gridOfImageIds, int currRow, int curCol) {

		Map<Integer, ImageMatch> neighborsOfcurrent = imageIdToNeigh.get(gridOfImageIds[currRow][curCol].imageId);
		ImageMatch imageMatch = neighborsOfcurrent.get(gridOfImageIds[currRow+1][curCol].imageId);
		int posOneWithoutRev = imageMatch.posOne % 10;
		int posTwoWithoutRev = imageMatch.posTwo % 10;

		while (posTwoWithoutRev != posOneWithoutRev) {
			gridOfImageIds[currRow][curCol].rotate90ToRight();
			posOneWithoutRev = (posOneWithoutRev + 1) % 4;
		}
		if (posOneWithoutRev >= 10) {
			gridOfImageIds[currRow][curCol].flipLeftRight();
		}
		imageMatch = neighborsOfcurrent.get(gridOfImageIds[curCol][currRow + 1].imageId);
		posOneWithoutRev = imageMatch.posOne % 10;

		if (posOneWithoutRev >= 10) {
			gridOfImageIds[currRow][curCol].flipTopBottom();
		}
	}


	private static void setBottomAndRightBorder(Map<Integer, Image> imageIDToImage, Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> corners, Set<Integer> borders, Set<Integer> alreadyPlaced) {

		for (int i = 1; i < gridOfImageIds.length; i++) {
			Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[gridOfImageIds.length - 1][i - 1].imageId).keySet();
			posNeigh.removeAll(alreadyPlaced);
			Integer actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[gridOfImageIds.length - 1][i] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);

			if (i == gridOfImageIds.length - 1) {
				continue;
			}

			posNeigh = imageIdToNeigh.get(gridOfImageIds[i - 1][gridOfImageIds.length - 1].imageId).keySet();
			posNeigh.removeAll(alreadyPlaced);
			actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[i][gridOfImageIds.length - 1] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);
		}
	}


	private static void setTopAndBottomBorder(Map<Integer, Image> imageIDToImage, Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh, Image[][] gridOfImageIds,
			Set<Integer> corners, Set<Integer> borders, Set<Integer> alreadyPlaced) {

		for (int i = 2; i < gridOfImageIds.length; i++) {
			Set<Integer> posNeigh = imageIdToNeigh.get(gridOfImageIds[0][i - 1].imageId).keySet();
			posNeigh.removeAll(alreadyPlaced);
			Integer actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[0][i] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);

			posNeigh = imageIdToNeigh.get(gridOfImageIds[i - 1][0].imageId).keySet();
			posNeigh.removeAll(alreadyPlaced);
			actualNeigh = posNeigh.stream().filter(id -> borders.contains(id) || corners.contains(id)).findAny().get();
			gridOfImageIds[i][0] = imageIDToImage.get(actualNeigh);
			alreadyPlaced.add(actualNeigh);
		}
	}


	private static void resolvePart1(Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh) {

		long result = getAllImagesWithNumberOfNeigh(imageIdToNeigh, 2)
				.reduce(1, (l1, l2) -> l1 * l2);

		Helper.printResultPart1(String.valueOf(result));
	}


	private static Set<Integer> getAllImagesWithNumberOfNeighAsIntSet(
			Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh,
			int numberOfNeigh) {

		return getAllImagesWithNumberOfNeigh(imageIdToNeigh, numberOfNeigh)
				.boxed()
				.map(Long::intValue)
				.collect(Collectors.toSet());
	}


	private static LongStream getAllImagesWithNumberOfNeigh(
			Map<Integer, Map<Integer, ImageMatch>> imageIdToNeigh,
			int numberOfNeigh) {

		return imageIdToNeigh
				.entrySet()
				.stream()
				.filter(entry -> entry.getValue().size() == numberOfNeigh)
				.mapToLong(Map.Entry::getKey);
	}


	private static Map<Integer, Map<Integer, ImageMatch>> createMapOfNeighbors(Map<String, List<ImageBorderPosTriple>> borderToImages) {

		Map<Integer, Map<Integer, ImageMatch>> matchOfTwoImages = new HashMap<>();

		Iterator<Map.Entry<String, List<ImageBorderPosTriple>>> iterator = borderToImages.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<ImageBorderPosTriple>> next = iterator.next();
			List<ImageBorderPosTriple> neighbors = next.getValue();
			if (neighbors.size() == 1) {
				iterator.remove();
				continue;
			}

			if (neighbors.size() != 2) {
				throw new IllegalArgumentException();
			}

			ImageBorderPosTriple first = neighbors.get(0);
			ImageBorderPosTriple second = neighbors.get(1);

			matchOfTwoImages.putIfAbsent(first.image.imageId, new HashMap<>());
			matchOfTwoImages.get(first.image.imageId).put(second.image.imageId, new ImageMatch(first.image, second.image, first.pos, second.pos));

			matchOfTwoImages.putIfAbsent(second.image.imageId, new HashMap<>());
			matchOfTwoImages.get(second.image.imageId).put(first.image.imageId, new ImageMatch(second.image, first.image, second.pos, first.pos));
		}
		return matchOfTwoImages;
	}


	private static Map<String, List<ImageBorderPosTriple>> createMapOfBorders(Map<Integer, Image> imageIDToImage) {

		Map<String, List<ImageBorderPosTriple>> borderToImages = new HashMap<>();
		for (Image image : imageIDToImage.values()) {
			for (Map.Entry<Integer, String> posBorderPair : image.posToBorders.entrySet()) {
				borderToImages.putIfAbsent(posBorderPair.getValue(), new ArrayList<>());
				borderToImages.get(posBorderPair.getValue()).add(new ImageBorderPosTriple(image, posBorderPair.getKey(), posBorderPair.getValue()));
			}
		}
		return borderToImages;
	}

	private static Map<Integer, Image> readInput(List<String> input) {

		Map<Integer, Image> imageIDToImage = new HashMap<>();
		Image currentImg = null;
		boolean[][] binImg = new boolean[IMAGE_SIZE][IMAGE_SIZE];
		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		int currRow = 0;
		for (String line : input) {
			if (line.isEmpty() && currentImg != null) {
				currentImg.posToBorders.put(3, left.toString());
				currentImg.posToBorders.put(13, left.reverse().toString());
				currentImg.posToBorders.put(1, right.toString());
				currentImg.posToBorders.put(11, right.reverse().toString());
				right = new StringBuilder();
				left = new StringBuilder();
				imageIDToImage.put(currentImg.imageId, currentImg);
				currentImg = null;
				currRow = 0;
			} else if (line.startsWith("Tile")) {
				currentImg = new Image(Integer.parseInt(line.substring(5, 9)), binImg);
			} else {
				if (currRow == 0) {
					currentImg.posToBorders.put(0, line);
					currentImg.posToBorders.put(10, new StringBuilder(line).reverse().toString());

				} else if (currRow == IMAGE_SIZE - 1) {
					currentImg.posToBorders.put(2, line);
					currentImg.posToBorders.put(12, new StringBuilder(line).reverse().toString());
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
		return imageIDToImage;
	}

	private static class ImageBorderPosTriple {
		Image image;
		int pos;
		String border;

		public ImageBorderPosTriple(Image image, int pos, String border) {

			this.image = image;
			this.pos = pos;
			this.border = border;
		}
	}

	private static class ImageMatch {
		Image one;
		Image two;
		int posOne;
		int posTwo;

		public ImageMatch(Image one, Image two, int posOne, int posTwo) {

			this.one = one;
			this.two = two;
			this.posOne = posOne;
			this.posTwo = posTwo;
		}
	}

	private static class Image {

		int imageId;
		private boolean[][] binImg;

		// pos = top right bottom left => 0 1 2 3
		// rev pos = top right bottom left => 10 11 12 13
		Map<Integer, String> posToBorders = new HashMap<>();


		public Image(int imageId, boolean[][] binImg) {

			this.imageId = imageId;
			this.binImg = binImg;
		}


		@Override
		public String toString() {

			return String.valueOf(imageId);
		}


		public void flipLeftRight() {

			boolean tmp;
			for (int r = 0; r < IMAGE_SIZE; r++) {
				for (int c = 0; c < IMAGE_SIZE; c++) {
					tmp = binImg[r][c];
					binImg[r][c] = binImg[r][IMAGE_SIZE - 1 - c];
					binImg[r][IMAGE_SIZE - 1 - c] = tmp;
				}
			}
		}


		public void flipTopBottom() {

			boolean tmp;
			for (int r = 0; r < IMAGE_SIZE; r++) {
				for (int c = 0; c < IMAGE_SIZE; c++) {
					tmp = binImg[r][c];
					binImg[r][c] = binImg[IMAGE_SIZE - 1 - r][c];
					binImg[IMAGE_SIZE - 1 - r][c] = tmp;
				}
			}
		}


		public void rotate90ToRight() {

			boolean[][] newThing = new boolean[IMAGE_SIZE][IMAGE_SIZE];
			for (int r = 0; r < IMAGE_SIZE; r++) {
				for (int c = 0; c < IMAGE_SIZE; c++) {
					newThing[c][IMAGE_SIZE - 1 - r] = binImg[r][c];
				}
			}
			binImg = newThing;
		}


		public void removeBorders() {

			boolean[][] newImg = new boolean[IMAGE_SIZE - 2][IMAGE_SIZE - 2];
			for (int r = 1; r < IMAGE_SIZE - 1; r++) {
				for (int c = 1; c < IMAGE_SIZE - 1; c++) {
					newImg[r - 1][c - 1] = binImg[r][c];
				}
			}
			binImg = newImg;
		}

	}
}
