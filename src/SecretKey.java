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
}