package days;


import general.Helper;

public class Day25 {

    private static final long MOD = 20_201_227;

    public static void main(String[] args) {

        long cardPublicKey = 11239946;
        long doorPublicKey = 10464955;

        runTest();
        solvePart1(cardPublicKey, doorPublicKey);

    }

    private static void solvePart1(long cardPublicKey, long doorPublicKey) {
        int cardLoopSize = findOutLoopSize(cardPublicKey);
        int doorLoopSize = findOutLoopSize(doorPublicKey);

        long encryKey1 = executeEncryption(doorPublicKey, cardLoopSize);
        long encryKey2 = executeEncryption(cardPublicKey, doorLoopSize);

        assert encryKey1 == encryKey2;
        Helper.printResultPart1(String.valueOf(encryKey1));
    }

    private static void runTest() {

        int firstPublicKey = 5764801;
        int secondPublicKey = 17807724;

        int firstLoopSize = findOutLoopSize(firstPublicKey);
        if (firstLoopSize != 8) {
            throw new IllegalArgumentException();
        }
        int secondLoopSize = findOutLoopSize(secondPublicKey);
        if (secondLoopSize != 11) {
            throw new IllegalArgumentException();
        }

        long firstEncryptionKey = executeEncryption(secondPublicKey, firstLoopSize);
        long secondEncryptionKey = executeEncryption(firstPublicKey, secondLoopSize);

        if (firstEncryptionKey != secondEncryptionKey || firstEncryptionKey != 14897079) {
            throw new IllegalArgumentException();
        }
    }

    private static int findOutLoopSize(long expectedResult) {

        int loopSize = 0;
        long result = 1;

        while (result != expectedResult) {
            result = (result * 7) % MOD;
            loopSize++;
        }
        return loopSize;
    }

    private static long executeEncryption(long publicKey, int loopSize) {

        long result = 1;
        for (int i = 0; i < loopSize; i++) {
            result = (result * publicKey) % MOD;
        }
        return result;
    }


}
