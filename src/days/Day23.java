package days;

import general.Helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day23 {

    public static final String TEST_INPUT = "389125467";

    public static void main(String[] args) {

        String actualInput = "712643589";
        solveForPart1(actualInput);
        solveForPart2(actualInput);
    }

    private static void solveForPart1(String input) {
        String expectedResult = "67384529";
        Map<Integer, ListItem> listItems = playGame(TEST_INPUT, 9, 100);
        String testResult = calcResultStartingFrom(listItems.get(1));
        if (!expectedResult.equals(testResult)) {
            throw new IllegalArgumentException("Test fails");
        }
        listItems = playGame(input, 9, 100);
        String actualResult = calcResultStartingFrom(listItems.get(1));
        Helper.printResultPart1(actualResult);
    }

    private static void solveForPart2(String input) {
        long expectedResult = 149245887792l;
        Map<Integer, ListItem> listItems = playGame(TEST_INPUT, 1_000_000, 10_000_000);
        long testResult = ((long) listItems.get(1).next.value) * listItems.get(1).next.next.value;
        if (expectedResult != testResult) {
            throw new IllegalArgumentException("Test fails " + testResult + " should be " + expectedResult);
        }
        listItems = playGame(input, 1_000_000, 10_000_000);
        long actualResult = ((long) listItems.get(1).next.value) * listItems.get(1).next.next.value;
        Helper.printResultPart2(String.valueOf(actualResult));
    }

    private static Map<Integer, ListItem> playGame(String input, int fillUpUntil, int moves) {
        Map<Integer, ListItem> numToListItem = new HashMap<>();
        ListItem first = readInput(input, numToListItem);
        fillUpTheCups(fillUpUntil, numToListItem, first);
        playGame(numToListItem, first, moves);
        return numToListItem;
    }

    private static void fillUpTheCups(int fillUpUntil, Map<Integer, ListItem> numToListItem, ListItem first) {
        ListItem lastItem = first.next;
        while (lastItem.next != first) {
            lastItem = lastItem.next;
        }

        for (int i = numToListItem.size() + 1; i <= fillUpUntil; i++) {
            ListItem nextItem = new ListItem(i);
            numToListItem.put(i, nextItem);
            lastItem.next = nextItem;
            lastItem = nextItem;
        }
        lastItem.next = first;
    }


    private static void playGame(Map<Integer, ListItem> numToListItem, ListItem first, int moves) {
        ListItem current;
        Set<Integer> pickUpVals = new HashSet<>();
        current = first;
        for (int i = 0; i < moves; i++) {
            ListItem nextAfterPickUp = current;
            for (int j = 0; j < 3; j++) {
                nextAfterPickUp = nextAfterPickUp.next;
                pickUpVals.add(nextAfterPickUp.value);
            }

            ListItem dest = findValidDestination(current, pickUpVals, numToListItem);
            ListItem destNext = dest.next;
            ListItem currNext = current.next;

            current.next = nextAfterPickUp.next; // remove pickups
            dest.next = currNext; // add pickups to destination
            nextAfterPickUp.next = destNext; // fix end of pickups

            current = current.next;
            pickUpVals.clear();
        }
    }

    private static ListItem readInput(String input, Map<Integer, ListItem> numToListItem) {
        ListItem first = null;
        ListItem current = null;
        for (char c : input.toCharArray()) {
            int num = c - '0';
            ListItem next = new ListItem(num);
            numToListItem.put(num, next);
            if (current != null)
                current.next = next;
            current = next;
            if (first == null)
                first = current;
        }
        current.next = first;
        return first;
    }

    private static String calcResultStartingFrom(ListItem startItem) {

        StringBuilder result = new StringBuilder(10);
        ListItem curr = startItem.next;
        while (curr != startItem) {
            result.append(curr.value);
            curr = curr.next;
        }
        return result.toString();
    }

    private static ListItem findValidDestination(ListItem current, Set<Integer> pickUpVals, Map<Integer, ListItem> numToListItem) {
        int destination = nextDestination(current.value, numToListItem);
        while (pickUpVals.contains(destination)) destination = nextDestination(destination, numToListItem);
        return numToListItem.get(destination);
    }

    private static int nextDestination(int destination, Map<Integer, ListItem> numToListItem) {
        int nextDest = destination - 1;
        if (nextDest == 0) nextDest = numToListItem.size();
        return nextDest;
    }

    private static class ListItem {

        int value;
        ListItem next;

        public ListItem(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + value + '}';
        }
    }

}
