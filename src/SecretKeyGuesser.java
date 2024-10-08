/* 

1st algorithm works by starting with all 'R's and systematically trying all combinations, updating positions from right to left when needed. 
This approach ensures to find the correct key while keeping the number of guesses relatively low.

2nd algorithm uses a frequency-based approach to make more informed guesses, potentially reducing the number of guesses needed.

*/

public class SecretKeyGuesser {
    private static final int KEY_LENGTH = 16;
    private static final char[] POSSIBLE_CHARS = {'R', 'M', 'I', 'T'};
    private static final int MAX_GUESSES = 1000000;
    private SecretKey secretKey;
    private char[] currentGuess;
    private int guessCount;
    private int[] charFrequency;

    public void start() {
        secretKey = new SecretKey();
        currentGuess = new char[KEY_LENGTH];
        charFrequency = new int[POSSIBLE_CHARS.length];
        guessCount = 0;
        findSecretKey();
    }

    private void findSecretKey() {
        int correctPositions;
        do {
            guessCount++;
            generateGuess();
            String guessString = new String(currentGuess);
            correctPositions = secretKey.guess(guessString);
            System.out.println("Guess " + guessCount + ": " + guessString + " (Correct: " + correctPositions + ")");
            
            if (correctPositions == KEY_LENGTH) {
                System.out.println("Secret key found: " + guessString);
                System.out.println("Total guesses: " + guessCount);
                return;
            }
            
            if (guessCount >= MAX_GUESSES) {
                System.out.println("Maximum guess limit reached. Stopping.");
                return;
            }
            
            updateCharFrequency(correctPositions);
        } while (true);
    }

    private void generateGuess() {
        for (int i = 0; i < KEY_LENGTH; i++) {
            currentGuess[i] = POSSIBLE_CHARS[getMostLikelyCharIndex()];
        }
    }

    private void updateCharFrequency(int correctPositions) {
        for (int i = 0; i < POSSIBLE_CHARS.length; i++) {
            if (charFrequency[i] == 0) continue;
            charFrequency[i] = (charFrequency[i] * correctPositions) / KEY_LENGTH;
        }
        int totalFreq = 0;
        for (int freq : charFrequency) totalFreq += freq;
        if (totalFreq == 0) {
            for (int i = 0; i < POSSIBLE_CHARS.length; i++) {
                charFrequency[i] = 1;
            }
        }
    }

    private int getMostLikelyCharIndex() {
        int maxFreq = -1;
        int maxIndex = 0;
        for (int i = 0; i < POSSIBLE_CHARS.length; i++) {
            if (charFrequency[i] > maxFreq) {
                maxFreq = charFrequency[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}