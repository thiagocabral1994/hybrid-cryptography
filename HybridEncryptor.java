import java.util.List;
import java.util.Arrays;

class HybridEncryptor {
    private static final int SPLIT_PARTS = 4;
    private static final String SEPARATOR = " ";

    private RSA rsa;
    private String key;

    public HybridEncryptor(String symmetricKey, int firstPrime, int secondPrime, int publicKey) {
        this.rsa = new RSA(firstPrime, secondPrime, publicKey);
        this.key = symmetricKey;
    }

    public HybridEncryptor(String symmetricKey, RSA rsa) {
        this.rsa = rsa;
        this.key = symmetricKey;
    }

    public String encrypt(String text) throws InterruptedException {
        String[] subtexts = splitText(text);
        EncryptThread[] encryptThreads = new EncryptThread[subtexts.length];

        for (int i = 0; i < encryptThreads.length; i++) {
            EncryptThread thread = new EncryptThread(subtexts[i], this.key, this.rsa);
            encryptThreads[i] = thread;
            thread.start();
        }

        String encryptText = "";
        for (int i = 0; i < encryptThreads.length; i++) {
            EncryptThread thread = encryptThreads[i];
            thread.join();
            encryptText += thread.getMessage();
        }

        return encryptText;
    }

    public String decrypt(String encryptText) throws InterruptedException {
        String[] encryptSubtexts = splitRSAText(encryptText);
        DecryptThread[] decryptThreads = new DecryptThread[encryptSubtexts.length];
        for (int i = 0; i < decryptThreads.length; i++) {
            DecryptThread thread = new DecryptThread(encryptSubtexts[i], this.key, this.rsa);
            decryptThreads[i] = thread;
            thread.start();
        }

        String originalText = "";
        for (int i = 0; i < decryptThreads.length; i++) {
            DecryptThread thread = decryptThreads[i];
            thread.join();
            originalText += thread.getMessage();
        }
        return originalText;
    }

    private static String[] splitText(String text) {
        int partLength = (int) Math.ceil((double) text.length() / SPLIT_PARTS);
        String[] parts = new String[SPLIT_PARTS];
        for (int i = 0; i < SPLIT_PARTS; i++) {
            int limit = Math.min(text.length(), (i + 1) * partLength);
            parts[i] = text.substring(i * partLength, limit);
        }
        return parts;
    }

    private static String[] splitRSAText(String rsaText) {
        String[] rsaParts = rsaText.split(SEPARATOR);
        int partLength = (int) Math.ceil((double) rsaParts.length / SPLIT_PARTS);
        String[] parts = new String[SPLIT_PARTS];
        for (int i = 0; i < SPLIT_PARTS; i++) {
            int limit = Math.min(rsaParts.length, (i + 1) * partLength);
            parts[i] = String.join(" ", Arrays.copyOfRange(rsaParts, i * partLength, limit));
        }
        return parts;
    }
}
