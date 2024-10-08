/* 

1st algorithm works by starting with all 'R's and systematically trying all combinations, updating positions from right to left when needed. 
This approach ensures to find the correct key while keeping the number of guesses relatively low.

*/

public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private static final int MAX_GUESSES = 1000000;
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;

    public int start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        guessCount = 0;
        initializeGuess();
        return findSecretKey();
    }

    private void initializeGuess() {
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = POSSIBLE_CHARS[0];
        }
    }

    private int findSecretKey() {
        int correctPositions;
        do {
            guessCount++;
            String guessString = new String(currentGuess);
            correctPositions = secretKey.guess(guessString);
            System.out.println("Guess " + guessCount + ": " + guessString + " (Correct: " + correctPositions + ")");
            
            if (correctPositions == KEY_LENGTH) {
                System.out.println("Secret key found: " + guessString);
                System.out.println("Total guesses: " + guessCount);
                return guessCount;
            }
            
            if (guessCount >= MAX_GUESSES) {
                System.out.println("Maximum guess limit reached. Stopping.");
                return guessCount;
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