package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import general.Helper;

public class Day05 {

	public static void main(String[] args) {

		List<String> lines = Helper.readFile("./data/days/day05p1.txt");

		boolean[] seats = new boolean[127*8+7 +1];

		int maxTicketId = 0;
		for (String ticket : lines) {

			int ticketId = convertTicketToTicketId(ticket);
			seats[ticketId] = true;
			maxTicketId = Math.max(maxTicketId, ticketId);
		}

		Helper.printResultPart1(String.valueOf(maxTicketId));

		int yourSeat = 0;
		for (int i=1; i < seats.length-1; i++) {
			if (!seats[i] && seats[i-1] && seats[i+1]) {
				double row = Math.floor(i * 1.0 / 8);
				double col = i - row * 8;
				if (col != 0 && col != 7) {
					yourSeat = i;
				}
			}
		}

		Helper.printResultPart2(String.valueOf(yourSeat));

	}


	private static int convertTicketToTicketId(String ticket) {

		String rowString = ticket.substring(0, 7);
		String colString = ticket.substring(7);

		int lowRow = 0;
		int maxRow = 127;
		for (char c : rowString.toCharArray()) {
			int mid = (int) Math.ceil((lowRow + maxRow) * 0.5);
			if (c == 'F') {
				maxRow = mid -1;
			} else {
				lowRow = mid;
			}
		}
		int lowSeat = 0;
		int maxSeat = 8;
		for (char c : colString.toCharArray()) {
			int mid = (int) Math.ceil((lowSeat + maxSeat) * 0.5);
			if (c == 'L') {
				maxSeat = mid -1;
			} else {
				lowSeat = mid;
			}
		}
		return maxRow * 8 + maxSeat;
	}

}
