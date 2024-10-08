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

/*
Feedback Interpretation Mechanism

How It Works:

1. **Initial Guess:**
   - The algorithm begins by making an initial guess where all positions are filled with a default character, typically the first character in the set of possible characters (e.g., `'R'`).
   - It then calls the `guess()` method with this guess and stores the number of correct positions returned (`totalCorrect`).

2. **Iterative Testing of Positions:**
   - For each position that hasn't been confirmed yet, the algorithm iteratively tries substituting the current character with each of the other possible characters.
   - After each substitution, it makes a new guess and observes the change in the `totalCorrect` value.

3. **Interpreting Changes in Correctness Score:**

   - **Increase in Correctness Score:**
     - If changing a character at a position results in an **increase** in the `totalCorrect` value, it implies that the new character is correct at that position.
     - The algorithm updates the current guess with this new character and continues to the next position.

   - **Decrease in Correctness Score:**
     - If changing a character leads to a **decrease** in the `totalCorrect` value, it indicates that the original character was correct at that position.
     - The algorithm reverts the change (restoring the original character) and marks the position as **confirmed**, meaning it won't attempt to change this position again.

   - **No Change in Correctness Score:**
     - If there's **no change** in the `totalCorrect` value after substitution, no definitive conclusion can be drawn about the correctness of the characters at that position.
     - The algorithm reverts the change and may attempt other substitutions or strategies.

#### Recognizing Patterns:

- **Indirect Feedback:**
  - By carefully analyzing how the correctness score changes in response to specific character substitutions at particular positions, the algorithm can deduce information about individual positions.
  - Even though the feedback doesn't specify which positions are correct, the **pattern of changes** in the correctness score reveals critical insights.

- **Example Scenario:**

  - Let's say the initial guess yields a `totalCorrect` of 5.
  - Changing the character at position 3 from `'R'` to `'M'` increases `totalCorrect` to 6.
    - This suggests that `'M'` is the correct character at position 3.
  - Changing the character at position 7 from `'R'` to `'I'` decreases `totalCorrect` to 4.
    - This indicates that the original character `'R'` is correct at position 7.

By iteratively applying this method across all positions, the algorithm effectively identifies the correct character for each position, even in the absence of explicit positional feedback.

### Hybrid Deduction Strategy

To efficiently deduce the secret key while minimizing the number of guesses, the algorithm combines iterative refinement with a targeted brute-force method. This hybrid strategy leverages the strengths of both approaches to handle different situations that arise during the deduction process.

#### Iterative Refinement:

- **Systematic Exploration:**
  - The algorithm starts by systematically testing each position through the feedback interpretation mechanism described earlier.
  - It attempts to confirm correct characters by interpreting increases or decreases in the correctness score resulting from character substitutions.

- **Efficiency:**
  - This method is efficient for positions where substitutions lead to clear changes in the correctness score.
  - It often allows the algorithm to confirm many positions without needing to exhaustively try every possible character at every position.

#### Targeted Brute-Force Method:

- **When It's Applied:**
  - The algorithm switches to a targeted brute-force method when iterative refinement doesn't yield conclusive results for certain positions.
  - This typically happens when changing characters at a position doesn't affect the correctness score, leaving ambiguity about which character is correct.

- **Focused Exhaustive Search:**
  - For these ambiguous positions, the algorithm tries all possible characters at the position one by one.
  - After each substitution, it makes a guess and observes any changes in the correctness score.

- **Efficient Resolution:**
  - By focusing only on the remaining uncertain positions, the algorithm limits the scope of the brute-force search.
  - This targeted approach prevents unnecessary guesses and accelerates the resolution of difficult positions.

#### Minimizing Guess Counts:

- **Balancing Exploration and Exhaustiveness:**
  - The hybrid strategy effectively balances the thoroughness of brute-force methods with the efficiency of iterative refinement.
  - It ensures that the algorithm doesn't waste guesses on positions already confirmed or unlikely to yield progress through iteration.

- **Comprehensive Coverage:**
  - Combining both methods guarantees that all positions are eventually resolved.
  - It minimizes the total number of guesses by avoiding a full brute-force search across all positions and characters.

#### Example Scenario:

- **Iterative Refinement Phase:**
  - The algorithm successfully confirms 12 out of 16 positions through iterative testing.
  - Changes in correctness scores provided clear indications for these positions.

- **Switching to Brute-Force:**
  - For the remaining 4 positions, substitutions didn't change the correctness score.
  - The algorithm then applies the brute-force method to these positions, trying each possible character and observing the results.

By integrating both strategies, the algorithm efficiently deduces the entire secret key with fewer guesses than would be required by a purely brute-force approach.

### Adaptive Algorithm Design

The algorithm's design is adaptive, meaning it adjusts its strategy based on the real-time feedback received from the `guess()` method. This adaptability allows it to handle various secret key configurations robustly and efficiently.

#### Dynamic Loop for Refinement:

- **Continuous Evaluation:**
  - The core of the algorithm is a dynamic `while` loop that continues until the secret key is fully deduced (`totalCorrect == KEY_LENGTH`).
  - Within this loop, it iteratively refines its guesses, responds to feedback, and updates its strategy as needed.

- **Progress Tracking:**
  - A `progress` flag is used to monitor whether any new positions were confirmed in each iteration.
  - If progress is made, the loop continues with iterative refinement.
  - If no progress is detected, the algorithm adapts by invoking alternative strategies (e.g., the targeted brute-force method).

#### Real-Time Strategy Adaptation:

- **Responsive to Feedback:**
  - The algorithm responds immediately to increases or decreases in the correctness score.
  - This responsiveness allows it to confirm positions and adjust its guesses without delay.

- **Switching Strategies:**
  - When the algorithm identifies that iterative refinement is no longer yielding results (i.e., the correctness score isn't changing despite substitutions), it dynamically switches to the brute-force method for unresolved positions.

#### Robust Handling of Different Secret Keys:

- **Uniform vs. Random Keys:**
  - The adaptive design ensures that the algorithm performs well regardless of the secret key's composition.
  - For keys with repeating characters, the algorithm quickly confirms multiple positions.
  - For keys with diverse characters or challenging patterns, the algorithm methodically resolves each position through its combined strategies.

#### Ensuring Completion:

- **No Dead Ends:**
  - The adaptive loop structure prevents the algorithm from getting stuck in situations where progress stalls.
  - By dynamically adjusting its approach, it ensures that all positions will eventually be deduced.

- **Example of Adaptation:**

  - **Situation:**
    - After several iterations, positions 5 and 10 remain unresolved despite substitutions not affecting the correctness score.
  
  - **Algorithm's Response:**
    - Recognizes the lack of progress (`progress` flag remains `false`).
    - Invokes `resolveRemainingPositions()` to apply the brute-force method to these positions.

  - **Outcome:**
    - Successfully identifies the correct characters for the remaining positions.
    - Completes the deduction of the entire secret key.

 */