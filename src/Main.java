public class Main {
    private static final int TEST_RUNS = 3;

    public static void main(String[] args) {
        runPerformanceTests();
    }

    /**
     * Runs multiple test cases to evaluate the performance of the SecretKeyGuesser.
     */
    private static void runPerformanceTests() {
        int totalGuesses = 0;
        int minGuesses = Integer.MAX_VALUE;
        int maxGuesses = 0;

        for (int i = 0; i < TEST_RUNS; i++) {
            SecretKeyGuesser guesser = new SecretKeyGuesser();
            guesser.start();
            int guesses = guesser.getGuessCount();
            totalGuesses += guesses;
            minGuesses = Math.min(minGuesses, guesses);
            maxGuesses = Math.max(maxGuesses, guesses);
            System.out.println("Run " + (i + 1) + ": " + guesses + " guesses");
        }

        double avgGuesses = (double) totalGuesses / TEST_RUNS;

        System.out.println("\nPerformance over " + TEST_RUNS + " runs:");
        System.out.println("Average guesses: " + avgGuesses);
        System.out.println("Minimum guesses: " + minGuesses);
        System.out.println("Maximum guesses: " + maxGuesses);
        System.out.println("Total guesses: " + totalGuesses);
    }
}