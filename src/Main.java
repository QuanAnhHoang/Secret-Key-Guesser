public class Main {
    public static void main(String[] args) {
        testSecretKeyGuesser();
    }

    private static void testSecretKeyGuesser() {
        SecretKeyGuesser guesser = new SecretKeyGuesser();
        guesser.start();
    }
}