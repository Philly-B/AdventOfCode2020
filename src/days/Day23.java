package days;

import general.Helper;

import java.util.Arrays;

public class Day23 {


    public static void main(String[] args) {

        runTestCase();

        String actualInput = "712643589";
        solveForPart1(actualInput);

    }

    private static void runTestCase() {
        String testInput = "389125467";
        String expectedResult = "67384529";
        String testResult = executeMoves(testInput);
        if (!expectedResult.equals(testResult)) {
            throw new IllegalArgumentException("Test fails");
        }
    }

    private static void solveForPart1(String input) {
        String result = executeMoves(input);
        Helper.printResultPart1(result);
    }

    private static String executeMoves(String input) {
        ListItem[] numToListItem = new ListItem[10];
        ListItem first = readInput(input, numToListItem);
        executeMoves(numToListItem, first);
        return calcResultStartingFrom(numToListItem[1]);
    }

    private static void executeMoves(ListItem[] numToListItem, ListItem first) {
        ListItem current;
        boolean[] pickUpVals = new boolean[10];
        current = first;
        for (int i = 0; i < 100; i++) {
            Arrays.fill(pickUpVals, false);
            ListItem nextAfterPickUp = current;
            for (int j = 0; j < 3; j++) {
                nextAfterPickUp = nextAfterPickUp.next;
                pickUpVals[nextAfterPickUp.value] = true;
            }

            ListItem dest = findValidDestination(current, pickUpVals, numToListItem);
            ListItem destNext = dest.next;
            ListItem currNext = current.next;

            current.next = nextAfterPickUp.next; // remove pickups
            dest.next = currNext; // add pickups to destination
            nextAfterPickUp.next = destNext; // fix end of pickups

            current = current.next;
        }
    }

    private static ListItem readInput(String input, ListItem[] numToListItem) {
        ListItem first = null;
        ListItem current = null;
        for (char c : input.toCharArray()) {
            int num = c - '0';
            ListItem next = new ListItem(num);
            numToListItem[num] = next;
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

    private static ListItem findValidDestination(ListItem current, boolean[] pickUpVals, ListItem[] numToListItem) {
        int destination = nextDestination(current.value);
        boolean valid = false;
        while (!valid) {
            valid = true;
            for (int d = 1; d < 10; d++) {
                if (pickUpVals[d] && destination == d) {
                    destination = nextDestination(destination);
                    valid = false;
                    break;
                }
            }
        }
        return numToListItem[destination];
    }

    private static int nextDestination(int destination) {
        int nextDest = destination - 1;
        if (nextDest == 0) nextDest = 9;
        return nextDest;
    }


    private static void printList(ListItem first) {

        boolean[] seen = new boolean[10];
        ListItem curr = first;
        do {
            seen[curr.value] = true;
            System.out.print(curr.value + " ");
            curr = curr.next;
        } while (!seen[curr.value]);

        System.out.println();

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
