package days;

import general.Helper;

import java.util.*;

public class Day22 {

    public static void main(String[] args) {

        List<String> input = Helper.readFile("./data/days/day22p1.txt");

        Queue<Integer> firstPlayer = new LinkedList<>();
        Queue<Integer> secondPlayer = new LinkedList<>();

        readInput(input, firstPlayer, secondPlayer);
        solvePart1(firstPlayer, secondPlayer);

        readInput(input, firstPlayer, secondPlayer);
        solvePart2(firstPlayer, secondPlayer);

    }

    private static void solvePart2(Queue<Integer> firstPlayer, Queue<Integer> secondPlayer) {
        playRecursiveCombat(firstPlayer, secondPlayer);
        int winnersPointsRound2 = getWinnersPoints(firstPlayer, secondPlayer);
        Helper.printResultPart2(String.valueOf(winnersPointsRound2));
    }

    private static void solvePart1(Queue<Integer> firstPlayer, Queue<Integer> secondPlayer) {
        playNormalCombat(firstPlayer, secondPlayer);
        int winnersPointsRound1 = getWinnersPoints(firstPlayer, secondPlayer);
        Helper.printResultPart1(String.valueOf(winnersPointsRound1));
    }

    private static int getWinnersPoints(Queue<Integer> firstPlayer, Queue<Integer> secondPlayer) {
        return firstPlayer.isEmpty() ? countPoints(secondPlayer) : countPoints(firstPlayer);
    }

    private static void readInput(List<String> input, Queue<Integer> firstPlayer, Queue<Integer> secondPlayer) {
        firstPlayer.clear();
        secondPlayer.clear();

        int i = 1;
        while (i < input.size()) {
            if (input.get(i).isEmpty()) break;
            firstPlayer.add(Integer.parseInt(input.get(i)));
            i++;
        }
        i += 2;
        while (i < input.size()) {
            if (input.get(i).isEmpty()) break;
            secondPlayer.add(Integer.parseInt(input.get(i)));
            i++;
        }
    }

    private static int countPoints(Queue<Integer> winner) {

        int multiplier = winner.size();
        int result = 0;
        while (!winner.isEmpty()) {
            result += multiplier * winner.poll();
            multiplier--;
        }
        return result;
    }

    private static void playNormalCombat(Queue<Integer> firstPlayer, Queue<Integer> secondPlayer) {

        while (!firstPlayer.isEmpty() && !secondPlayer.isEmpty()) {
            Integer firstPlayersCard = firstPlayer.poll();
            Integer secondPlayersCard = secondPlayer.poll();

            if (firstPlayersCard > secondPlayersCard) {
                firstPlayer.add(firstPlayersCard);
                firstPlayer.add(secondPlayersCard);
            } else {
                secondPlayer.add(secondPlayersCard);
                secondPlayer.add(firstPlayersCard);
            }
        }
    }


    private static boolean playRecursiveCombat(Queue<Integer> firstPlayer,
                                               Queue<Integer> secondPlayer) {


        Set<Integer> deckHashesFirstPlayer = new HashSet<>();
        Set<Integer> deckHashesSecondPlayer = new HashSet<>();

        while (true) {

            int firstPlayersCardHash = firstPlayer.hashCode();
            int secondPlayersCardHash = secondPlayer.hashCode();
            if (deckHashesFirstPlayer.contains(firstPlayersCardHash) &&
                    deckHashesSecondPlayer.contains(secondPlayersCardHash)) {
                return true;
            }

            deckHashesFirstPlayer.add(firstPlayersCardHash);
            deckHashesSecondPlayer.add(secondPlayersCardHash);

            Integer firstPlayersCard = firstPlayer.poll();
            Integer secondPlayersCard = secondPlayer.poll();

            final boolean didPlayerOneWin;
            if (firstPlayer.size() >= firstPlayersCard &&
                    secondPlayer.size() >= secondPlayersCard) {

                Queue<Integer> firstPlayerSubGame = createListOfNextNCards(firstPlayer, firstPlayersCard);
                Queue<Integer> secondPlayerSubGame = createListOfNextNCards(secondPlayer, secondPlayersCard);

                didPlayerOneWin = playRecursiveCombat(firstPlayerSubGame, secondPlayerSubGame);

            } else {
                didPlayerOneWin = firstPlayersCard > secondPlayersCard;
            }

            if (didPlayerOneWin) {
                firstPlayer.add(firstPlayersCard);
                firstPlayer.add(secondPlayersCard);
            } else {
                secondPlayer.add(secondPlayersCard);
                secondPlayer.add(firstPlayersCard);
            }

            if (firstPlayer.isEmpty()) {
                return false;
            } else if (secondPlayer.isEmpty()) {
                return true;
            }
        }
    }

    private static Queue<Integer> createListOfNextNCards(Queue<Integer> cards, Integer n) {
        Queue<Integer> subGame = new LinkedList<>();
        Iterator<Integer> iterator = cards.iterator();
        for (int i = 0; i < n; i++) {
            subGame.add(iterator.next());
        }
        return subGame;
    }


}
