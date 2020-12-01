package general;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Helper {

	private Helper() {

	}

	public static void printResultPart1(String result) {
		printResultPart(1, result);
	}

	public static void printResultPart2(String result) {
		printResultPart(2, result);
	}

	private static void printResultPart(int part, String result) {
		System.out.println("Result part " + part + ":" + result);
	}

	public static List<String> readFile(String path) {

		try {
			return Files.readAllLines(new File(path).toPath());
		} catch (IOException e) {
			throw new IllegalArgumentException("No file found", e);
		}
	}

}
