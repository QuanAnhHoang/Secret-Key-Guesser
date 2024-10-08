/* 

The algorithm works by starting with all 'R's and systematically trying all combinations, updating positions from right to left when needed. 
This approach ensures to find the correct key while keeping the number of guesses relatively low.

*/

import java.util.Random;

public class SecretKey {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private final String key;

    public SecretKey() {
        Random random = new Random();
        StringBuilder keyBuilder = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            keyBuilder.append(POSSIBLE_CHARS[random.nextInt(POSSIBLE_CHARS.length)]);
        }
        this.key = keyBuilder.toString();
    }

    public int guess(String guessedKey) {
        if (guessedKey.length() != KEY_LENGTH) {
            return -1;
        }
        for (char c : guessedKey.toCharArray()) {
            if (c != 'R' && c != 'M' && c != 'I' && c != 'T') {
                return -1;
            }
        }
        int correctPositions = 0;
        for (int i = 0; i < KEY_LENGTH; i++) {
            if (key.charAt(i) == guessedKey.charAt(i)) {
                correctPositions++;
            }
        }
        return correctPositions;
    }

    // Method for testing purposes
    public String getKey() {
        return key;
    }
}