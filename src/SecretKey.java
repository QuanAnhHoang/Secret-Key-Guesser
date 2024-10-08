import java.util.Random;

public class SecretKey {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private final String key;

    /**
     * Generates a random secret key composed of 'R', 'M', 'I', and 'T'.
     */
    public SecretKey() {
        Random random = new Random();
        StringBuilder keyBuilder = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            keyBuilder.append(POSSIBLE_CHARS[random.nextInt(POSSIBLE_CHARS.length)]);
        }
        this.key = keyBuilder.toString();
    }

    /**
     * Compares the guessed key with the secret key and returns the number of correct positions.
     *
     * @param guessedKey The guessed key string.
     * @return The number of characters correctly positioned.
     * @throws IllegalArgumentException If the guessed key is invalid.
     */
    public int guess(String guessedKey) {
        if (guessedKey == null) {
            throw new IllegalArgumentException("Guessed key cannot be null");
        }
        if (guessedKey.length() != KEY_LENGTH) {
            throw new IllegalArgumentException("Guessed key must be exactly " + KEY_LENGTH + " characters long");
        }
        for (char c : guessedKey.toCharArray()) {
            if (c != 'R' && c != 'M' && c != 'I' && c != 'T') {
                throw new IllegalArgumentException("Guessed key can only contain R, M, I, or T");
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

    /**
     * Retrieves the secret key.
     * Note: This method is for testing purposes.
     *
     * @return The secret key string.
     */
    public String getKey() {
        return key;
    }
}