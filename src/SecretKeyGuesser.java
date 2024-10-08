/* 

The algorithm works by starting with all 'R's and systematically trying all combinations, updating positions from right to left when needed. 
This approach ensures to find the correct key while keeping the number of guesses relatively low.

*/



public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private SecretKey secretKey;
    private char[] currentGuess;

    public void start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
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
            correctPositions = secretKey.guess(new String(currentGuess));
            if (correctPositions == KEY_LENGTH) {
                System.out.println("Secret key found: " + new String(currentGuess));
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