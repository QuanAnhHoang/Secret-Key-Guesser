/* 

1st algorithm works by starting with all 'R's and systematically trying all combinations, updating positions from right to left when needed. 
This approach ensures to find the correct key while keeping the number of guesses relatively low.

2nd approach should significantly reduce the number of guesses compared to the previous implementations. It makes intelligent decisions based on the feedback from each guess, effectively narrowing down the possible combinations much faster.This approach should significantly reduce the number of guesses compared to the previous implementations. 
It makes intelligent decisions based on the feedback from each guess, effectively narrowing down the possible combinations much faster.

*/

public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;

    public int start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        guessCount = 0;
        // Initialize currentGuess with valid characters
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = 'R';
        }
        solveKey(0, KEY_LENGTH - 1);
        return guessCount;
    }

    private void solveKey(int start, int end) {
        if (start > end) return;
        
        int mid = (start + end) / 2;
        char bestChar = findBestChar(start, mid);
        
        for (int i = start; i <= mid; i++) {
            currentGuess[i] = bestChar;
        }
        
        if (start == end) return;
        
        int correctBefore = makeGuess();
        solveKey(start, mid);
        int correctAfter = makeGuess();
        
        if (correctAfter > correctBefore) {
            solveKey(mid + 1, end);
        } else {
            for (int i = mid + 1; i <= end; i++) {
                currentGuess[i] = bestChar;
            }
        }
    }

    private char findBestChar(int start, int end) {
        int[] counts = new int[POSSIBLE_CHARS.length];
        char[] tempGuess = currentGuess.clone(); // Create a temporary array for testing
        
        for (int i = 0; i < POSSIBLE_CHARS.length; i++) {
            for (int j = start; j <= end; j++) {
                tempGuess[j] = POSSIBLE_CHARS[i];
            }
            counts[i] = secretKey.guess(new String(tempGuess));
            guessCount++;
        }
        
        int maxIndex = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[maxIndex]) {
                maxIndex = i;
            }
        }
        return POSSIBLE_CHARS[maxIndex];
    }

    private int makeGuess() {
        guessCount++;
        return secretKey.guess(new String(currentGuess));
    }
}