public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private static final int MAX_GUESSES = 1000000; // Prevent infinite loops
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;

    public void start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        guessCount = 0;
        initializeGuess();
        findSecretKey();
    }

    private void initializeGuess() {
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = POSSIBLE_CHARS[0];
        }
    }

    private void findSecretKey() {
        int correctPositions;
        do {
            guessCount++;
            String guessString = new String(currentGuess);
            correctPositions = secretKey.guess(guessString);
            System.out.println("Guess " + guessCount + ": " + guessString + " (Correct: " + correctPositions + ")");
            
            if (correctPositions == KEY_LENGTH) {
                System.out.println("Secret key found: " + guessString);
                System.out.println("Total guesses: " + guessCount);
                return;
            }
            
            if (guessCount >= MAX_GUESSES) {
                System.out.println("Maximum guess limit reached. Stopping.");
                return;
            }
            
            updateGuess(correctPositions);
        } while (true);
    }

    private void updateGuess(int correctPositions) {
        for (int i = correctPositions; i < KEY_LENGTH; i++) {
            int currentCharIndex = getCharIndex(currentGuess[i]);
            currentGuess[i] = POSSIBLE_CHARS[(currentCharIndex + 1) % POSSIBLE_CHARS.length];
            if (currentCharIndex < POSSIBLE_CHARS.length - 1) {
                break;
            }
        }
    }

    private int getCharIndex(char c) {
        for (int i = 0; i < POSSIBLE_CHARS.length; i++) {
            if (POSSIBLE_CHARS[i] == c) {
                return i;
            }
        }
        return -1;
    }
}