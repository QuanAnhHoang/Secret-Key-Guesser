public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;
    private char[] knownCorrect; // Tracks known correct characters per position

    public void start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        knownCorrect = new char[KEY_LENGTH];
        // Initialize currentGuess and knownCorrect with null characters
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = 'R'; // Initial guess for all positions
            knownCorrect[i] = '\0'; // '\0' denotes unknown
        }
        guessCount = 0;
        solveKey();
    }

    private void solveKey() {
        // Initial guess with all 'R's
        int correctPositions = makeGuess();
        
        // Iterate through each position to determine the correct character
        for (int i = 0; i < KEY_LENGTH; i++) {
            if (correctPositions == KEY_LENGTH) {
                break; // All positions are correct
            }

            char originalChar = currentGuess[i];
            boolean found = false;

            for (char c : POSSIBLE_CHARS) {
                if (c == originalChar) {
                    continue; // Skip the character already in this position
                }

                // Set the current position to a new character
                currentGuess[i] = c;
                int newCorrectPositions = makeGuess();

                if (newCorrectPositions > correctPositions) {
                    // Correct character found for this position
                    knownCorrect[i] = c;
                    correctPositions = newCorrectPositions;
                    found = true;
                    break; // Move to the next position
                } else {
                    // Revert the change as it didn't improve the guess
                    currentGuess[i] = originalChar;
                }
            }

            if (!found) {
                // If no improvement, the original character was correct
                knownCorrect[i] = originalChar;
                // No need to change currentGuess[i] as it's already the originalChar
            }
        }
    }

    private int makeGuess() {
        guessCount++;
        String guessString = new String(currentGuess);
        int result = secretKey.guess(guessString);
        if (result == KEY_LENGTH) {
            // Found the correct key
            System.out.println("Secret Key Found: " + guessString);
        }
        return result;
    }

    // Optional: Method to retrieve the number of guesses made
    public int getGuessCount() {
        return guessCount;
    }
}