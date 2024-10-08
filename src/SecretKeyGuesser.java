/* 

1. Handling Decrease in totalCorrect:
- If changing a character at a position results in a decrease in totalCorrect, we conclude that the original character was correct, mark the position as confirmed, and revert the change.

2. Loop Adjustments:
- Introduced a while loop that continues until all positions are confirmed (i.e., totalCorrect == KEY_LENGTH).
- The progress variable tracks whether we made any progress in an iteration. If no progress is made, we attempt to resolve remaining positions with the resolveRemainingPositions() method.

3. Resolving Remaining Positions:
- The resolveRemainingPositions() method attempts to assign possible characters to unconfirmed positions by trying all options. This is a brute-force step to handle any remaining uncertainties.

*/



public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;
    private boolean[] confirmed;    // Tracks confirmed positions
    
    /**
     * Starts the guessing process to determine the secret key.
     */
    public void start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        confirmed = new boolean[KEY_LENGTH];
        guessCount = 0;
    
        // Initialize currentGuess with the first possible character
        char defaultChar = POSSIBLE_CHARS[0];
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = defaultChar;
        }
    
        // Make an initial guess
        int totalCorrect = makeGuess();
    
        // Iteratively refine the guess
        while (totalCorrect < KEY_LENGTH) {
            boolean progress = false;
            for (int pos = 0; pos < KEY_LENGTH; pos++) {
                if (confirmed[pos]) {
                    continue;
                }
    
                char originalChar = currentGuess[pos];
                boolean positionConfirmed = false;
                for (char testChar : POSSIBLE_CHARS) {
                    if (testChar == originalChar) {
                        continue;
                    }
                    currentGuess[pos] = testChar;
                    int newCorrect = makeGuess();
                    if (newCorrect > totalCorrect) {
                        // Found a correct character at this position
                        totalCorrect = newCorrect;
                        originalChar = testChar;
                        progress = true;
                        break;
                    } else if (newCorrect < totalCorrect) {
                        // Original character is correct at this position
                        currentGuess[pos] = originalChar; // Revert change
                        confirmed[pos] = true;
                        progress = true;
                        positionConfirmed = true;
                        break;
                    } else {
                        // No change in totalCorrect, revert and try next character
                        currentGuess[pos] = originalChar;
                    }
                }
                if (!positionConfirmed && !progress) {
                    // Cannot conclude for this position yet
                    continue;
                }
                if (totalCorrect == KEY_LENGTH) {
                    break;
                }
            }
            if (!progress) {
                // No progress made in this iteration, need to resolve remaining positions
                resolveRemainingPositions();
                break;
            }
        }
    
        if (totalCorrect == KEY_LENGTH) {
            System.out.println("Secret Key Found: " + new String(currentGuess));
        } else {
            // All positions should be confirmed at this point
            System.out.println("Secret Key Found (after resolving): " + new String(currentGuess));
        }
    }
    
    /**
     * Makes a guess by submitting the current guess to the SecretKey instance.
     * Increments the guess count and processes the result.
     *
     * @return The number of correct positions returned by the SecretKey.
     */
    private int makeGuess() {
        guessCount++;
        String guessString = new String(currentGuess);
        int result;
    
        try {
            result = secretKey.guess(guessString);
        } catch (IllegalArgumentException e) {
            // Handle invalid guesses gracefully
            System.out.println("Invalid guess: " + e.getMessage());
            result = -1;
        }
    
        return result;
    }
    
    /**
     * Retrieves the number of guesses made.
     *
     * @return The guess count.
     */
    public int getGuessCount() {
        return guessCount;
    }
    
    /**
     * Resolves any remaining unconfirmed positions.
     * This method is called when no progress can be made by direct substitution.
     */
    private void resolveRemainingPositions() {
        // Collect possible characters for each unconfirmed position
        for (int pos = 0; pos < KEY_LENGTH; pos++) {
            if (confirmed[pos]) {
                continue;
            }
            for (char testChar : POSSIBLE_CHARS) {
                currentGuess[pos] = testChar;
                int newCorrect = makeGuess();
                if (newCorrect > guessCount) {
                    confirmed[pos] = true;
                    break;
                }
            }
        }
    }
}