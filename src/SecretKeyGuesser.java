/*

1. Per-Position Tracking (knownCorrect Array): The knownCorrect array keeps track of which characters have been correctly identified for each position. 
A null character ('\0') indicates that the correct character for that position is still unknown.

2. Initialization: The currentGuess is initialized with all 'R' characters, serving as the baseline for initial guesses. 
The knownCorrect array is initialized with null characters to indicate that no positions have been confirmed yet.

3. Guess Evaluation: The initial guess is made with all 'R' characters. The number of correct positions (correctPositions) is recorded based on the response from secretKey.guess().

4. Iterative Refinement: For each position, if not all positions are correct, the algorithm attempts to find the correct character by iterating through the possible characters ('R', 'M', 'I', 'T'), excluding the current character.
After changing a character at position i, it makes a new guess and compares the new number of correct positions.

5. If an improvement is detected (newCorrectPositions > correctPositions):
The new character is confirmed as correct for that position.
The knownCorrect array is updated, and the algorithm proceeds to the next position.

6. If no improvement:
The change is reverted, and the algorithm tries the next character.

7.If no change improves the guess:
The original character is considered correct for that position.
Termination and Display:

8. If the number of correct positions reaches the key length (16), the algorithm stops and displays the correct secret key.
The makeGuess() method handles the display upon successfully finding the key.

9. Guess Count Tracking: The guessCount variable accurately tracks the number of guesses made during the process.
An optional getGuessCount() method is included to retrieve the number of guesses if needed externally.
 
 */


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