import java.util.List;
import java.util.Arrays;

class HybridEncryptor {
    private static final int SPLIT_PARTS = 4;
    private static final String SEPARATOR = " ";

    private static final long P_1 = 104147;
    private static final long Q_1 = 104123;

    private static final long P_2 = 104119;
    private static final long Q_2 = 104113;

    private static final long P_3 = 104683;
    private static final long Q_3 = 102071;

    private static final long P_4 = 100537;
    private static final long Q_4 = 100549;

    private static final long[] P_ARRAY = {P_1, P_2, P_3, P_4};
    private static final long[] Q_ARRAY = {Q_1, Q_2, Q_3, Q_4};

    private RSA rsa;
    private String key;

    public HybridEncryptor(String symmetricKey, long publicKey) {
        this.rsa = new RSA(P_1, Q_1, publicKey);
        this.key = symmetricKey;
    }

    public HybridEncryptor(String symmetricKey, RSA rsa) {
        this.rsa = rsa;
        this.key = symmetricKey;
    }

    public static String encryptDistinct(String text, String[] symmetricKeys, long[] publicKeys) throws InterruptedException {
        String[] subtexts = splitText(text);
        String result = "";
        for (int i = 0; i < subtexts.length; i++) {
            String vigenereText = Vigenere.encrypt(subtexts[i], symmetricKeys[i]);
            RSA partitionRSA = new RSA(P_ARRAY[i], Q_ARRAY[i], publicKeys[i]);
            result += partitionRSA.encrypt(vigenereText);
        }
        return result;
    }

    public static String decryptDistinct(String cypherText, String[] symmetricKeys, long[] publicKeys) throws InterruptedException {
        String[] cypherSubtexts = splitRSAText(cypherText);
        String result = "";
        for (int i = 0; i < cypherSubtexts.length; i++) {
            RSA partitionRSA = new RSA(P_ARRAY[i], Q_ARRAY[i], publicKeys[i]);
            String partialResult = partitionRSA.decrypt(cypherSubtexts[i]);
            result += Vigenere.decrypt(partialResult, symmetricKeys[i]);
        }
        return result;
    }
    public String encrypt(String text) throws InterruptedException {
        String[] subtexts = splitText(text);
        String result = "";
        for (int i = 0; i < subtexts.length; i++) {
            String vigenereText = Vigenere.encrypt(subtexts[i], this.key);
            result += this.rsa.encrypt(vigenereText);
        }
        return result;
    }

    public String encryptAsync(String text) throws InterruptedException {
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

    public String decryptAsync(String encryptText) throws InterruptedException {
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

    public String decrypt(String encryptText) throws InterruptedException {
        String[] cypherSubtexts = splitRSAText(encryptText);
        String result = "";
        for (int i = 0; i < cypherSubtexts.length; i++) {
            String partialResult = this.rsa.decrypt(cypherSubtexts[i]);
            result += Vigenere.decrypt(partialResult, this.key);
        }
        return result;
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
