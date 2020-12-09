package days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import general.Helper;

public class Day09 {

	public static final int PREAMBLE = 25;


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day09p1.txt");

		long[] nums = convertToLongArray(input);
		List<Long> preambleVals = calculatePreamble(nums, PREAMBLE);
		long invalidNumber = findFirstWhichIsNotSumOfTwoPreambleVals(nums, preambleVals);
		Helper.printResultPart1(String.valueOf(invalidNumber));
		long encryptionWeakness = findContiguosSubarrayOfNum(nums, invalidNumber);
		Helper.printResultPart2(String.valueOf(encryptionWeakness));

	}


	private static long findContiguosSubarrayOfNum(long[] nums, long targetSum) {

		for (int i = 0; i < nums.length; i++) {
			long sum = nums[i];
			for (int j = i + 1; j < nums.length; j++) {
				sum += nums[j];
				if (sum > targetSum) {
					break;
				} else if (sum == targetSum) {
					return calculateSumOfMinAndMaxInRange(nums, i, j);
				}
			}
		}
		throw new RuntimeException("Didnt find the contiguosArray");
	}


	private static long calculateSumOfMinAndMaxInRange(long[] nums, int i, int j) {

		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for (int x = i; x<= j; x++) {
			min = Math.min(min, nums[x]);
			max = Math.max(max, nums[x]);
		}
		return min+max;
	}


	private static long findFirstWhichIsNotSumOfTwoPreambleVals(long[] nums, List<Long> lastOfPreamble) {

		for (int i = PREAMBLE; i < nums.length; i++) {

			if (!isNumberSumOf(lastOfPreamble, nums[i])) {
				return nums[i];
			}
			lastOfPreamble.remove(nums[i - PREAMBLE]);
			lastOfPreamble.add(nums[i]);
			Collections.sort(lastOfPreamble);
		}
		throw new RuntimeException("Unable to find an invalid number");
	}


	private static long[] convertToLongArray(List<String> input) {

		long[] nums = new long[input.size()];
		for (int i = 0; i < input.size(); i++) {
			nums[i] = Long.parseLong(input.get(i));
		}
		return nums;
	}


	private static List<Long> calculatePreamble(long[] nums, int preamble) {

		List<Long> lastOfPreamble = new ArrayList<>();
		for (int i = 0; i < preamble; i++) {
			lastOfPreamble.add(nums[i]);
		}
		Collections.sort(lastOfPreamble);
		return lastOfPreamble;
	}


	private static boolean isNumberSumOf(List<Long> nums, long targetSum) {

		int left = 0;
		int right = nums.size() - 1;

		while (left < right) {
			long leftNum = nums.get(left).longValue();
			long rightNum = nums.get(right).longValue();
			long currentSum = leftNum + rightNum;
			if (currentSum == targetSum) {
				return true;
			} else if (currentSum > targetSum) {
				right--;
			} else if (currentSum < targetSum) {
				left++;
			}
		}
		return false;
	}

}
