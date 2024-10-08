# Data Structures & Algorithms (COSC2658) Assignment
![banner](Banner.png)

***

# Problem Details

### Important: You are not allowed to use the classes/interfaces defined in the Java Collection Framework. You can use/extend the examples/solutions I shared on our course's GitHub page. You are not allowed to use code copied from the Internet.

### Background: You found a box containing THE book "How to earn HDs for every course at RMIT with only 5-minute self-learning per day". It's very very very precious, isn't it? However, to open the box you need to know a secret key that is used to unlock the box. By looking at the lock pattern, you know that the secret key contains 16 letters, each can be either "R", "M", "I" or "T". You can calculate how many combinations there are. If each try takes you one second, do you want to try? Fortunately, you have a hack that: given a guessed secret key, it returns the number of positions that are matched between the guessed key and the correct key. For example (I used only 4 letters here for demonstration purposes only), if the correct secret key is "RMIT" and you guess "MMIT", the last three positions are correct, so 3 is returned. If you guess "TRMI", zero is returned. If you guess "RRRR", 1 is returned. And if you guess "RMIT", 4 is returned. Of course, in your case, you want to have 16 returned.

### Technical Description
The correct secret key contains exactly 16 letters, each of which must be either "R", "M", "I", or "T". It is managed by a class SecretKey (which I will describe later).

You need to create a class SecretKeyGuesser. The class contains one required public method (you can add more private methods/attributes as needed):

void start()

In this start() method, your code must:

First, create a new SecretKey instance (I will provide a sample SecretKey implementation - you don't need to do anything)

Then, repeatedly call the guess() method of the SecretKey instance. For each call, you must provide a String argument containing exactly 16 letters. Each letter of the String argument can only be "R", "M", "I", or "T". If you provide an invalid argument, you will get -1 as the returned value. If your argument is valid, the guess() method will return a value from 0 to 16, denoting how many positions in your guessed value are matched with the correct secret key.

Your code must stop whenever it receives 16 from a call to the guess() method. Then, you must display the correct secret key.

The following steps will be used to test your program:

- A secret key is generated (your program will NOT know anything about this key)
- An instance of your SecretKeyGuesser class is created
- The start() method of the SecretKeyGuesser instance will be called
- Anytime your program calls the guess() method, a counter value is increased
- Your program must try to make this counter as small as possible when it finds out the correct secret key
- I will run your program with three test cases. Then, the three counter values are added together. This is the performance of your program (the smaller, the better)

***

# Technical Aspects

## Java Classes and Their Relationships

| Class             | Description                                                                        | Interactions                                                                                              | Data Structures/Algorithms Used                                                                                                                                 |
|-------------------|------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Main`           | Entry point of the program. Runs multiple test cases, creates `SecretKeyGuesser` instances, and collects performance statistics. | Creates instances of `SecretKeyGuesser` and calls their `start()` method.                               | N/A                                                                                                                                                             |
| `SecretKey`      | Encapsulates the secret key. Generates a random key and provides a `guess()` method for comparison.                | Created by `SecretKeyGuesser`. Provides the `guess()` method to `SecretKeyGuesser`.                       |  - Generates random secret key. <br> - `guess()` method compares guessed key with the secret key and returns the number of correct characters in the correct positions. |
| `SecretKeyGuesser` | Implements the algorithm to deduce the secret key. Interacts with `SecretKey` by making guesses and interpreting feedback. | Creates an instance of `SecretKey`. Calls the `guess()` method of `SecretKey`.                             | - `char[] currentGuess`: Represents the current guess. <br> - `boolean[] confirmed`: Tracks confirmed character positions. <br> - Uses `POSSIBLE_CHARS` and `KEY_LENGTH` constants. |

## Data Structures Used

| Constant/Structure | Type    | Description                                       |
|-------------------|---------|---------------------------------------------------|
| `POSSIBLE_CHARS` | `char[]` | Contains the possible characters: 'R', 'M', 'I', 'T' |
| `KEY_LENGTH`    | `int`    | Represents the length of the key (16)             |

## Detailed Working of the Algorithm

### 1. Initialization

```
char defaultChar = POSSIBLE_CHARS;
for (int i = 0; i < KEY_LENGTH; i++) {
    currentGuess[i] = defaultChar;
}
int totalCorrect = makeGuess();

- All positions are initially set to 'R' (or the first character in POSSIBLE_CHARS).
- An initial guess is made, and the number of correct positions is stored in totalCorrect.

### 2. Iterative Refinement Loop

```
while (totalCorrect < KEY_LENGTH) {
    boolean progress = false;
    for (int pos = 0; pos < KEY_LENGTH; pos++) {
        if (confirmed[pos]) {
            continue;
        }
        // Try substituting with other possible characters
    }
    if (!progress) {
        resolveRemainingPositions();
        break;
    }
}

- The loop continues until all positions are confirmed (totalCorrect == KEY_LENGTH).
- Within the loop, we attempt to make progress by refining the guess.

### 3. Trying Substitutions

```
char originalChar = currentGuess[pos];
boolean positionConfirmed = false;
for (char testChar : POSSIBLE_CHARS) {
    if (testChar == originalChar) {
        continue;
    }
    currentGuess[pos] = testChar;
    int newCorrect = makeGuess();
    // Interpret the feedback
}

- For each possible character (excluding the original character), we substitute it at the current position and make a new guess.
- The feedback (newCorrect) is compared to totalCorrect to determine the next steps.

### 4. Interpreting Change in totalCorrect

If newCorrect > totalCorrect:
- The substitution has increased the number of correct positions.
- Update totalCorrect, keep the new character, and note that progress was made.

If newCorrect < totalCorrect:
- The substitution has decreased the number of correct positions.
- The original character is confirmed to be correct.
- Revert the change, mark the position as confirmed, and note progress.

If newCorrect == totalCorrect:
No new information is gained; revert the change and try the next character.

### 5. Resolving Remaining Positions

```
private void resolveRemainingPositions() {
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

- For any positions not yet confirmed, try each possible character.
- Since no progress was made in previous iterations, we resort to brute-force checking.
- Once a character increases the newCorrect, we mark the position as confirmed.

### 6. Termination
- The loop terminates when totalCorrect == KEY_LENGTH, meaning all positions have been correctly identified.
- The secret key is then printed.

## Personal Contributions



***