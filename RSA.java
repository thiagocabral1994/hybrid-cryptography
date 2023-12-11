import java.math.BigInteger;

public class RSA {
    private static final String SEPARATOR = " ";

    private long p;
    private long q;
    private long n;
    private long e;
    private long d;

    public RSA(long p, long q, long e) {
        this.p = p;
        this.q = q;
        this.n = p * q;
        this.e = e;
        this.d = this.calculatePrivateKey();
    }

    private long calculatePrivateKey() {
        long phi = (p - 1) * (q - 1);
        BigInteger eBigInteger = BigInteger.valueOf(e);
        BigInteger phiBigInteger = BigInteger.valueOf(phi);
        BigInteger dBigInteger = eBigInteger.modInverse(phiBigInteger);
        return dBigInteger.longValue();
    }

    public String encrypt(String message) {
        StringBuilder ciphertextBuilder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            BigInteger m = BigInteger.valueOf((int) c);
            BigInteger encrypted = m.modPow(BigInteger.valueOf(e), BigInteger.valueOf(n));
            ciphertextBuilder.append(encrypted).append(SEPARATOR);
        }
        return ciphertextBuilder.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintextBuilder = new StringBuilder();
        String[] encryptedChars = ciphertext.split(SEPARATOR);
        for (String encryptedChar : encryptedChars) {
            BigInteger encrypted = new BigInteger(encryptedChar);
            BigInteger decrypted = encrypted.modPow(BigInteger.valueOf(d), BigInteger.valueOf(n));
            char plaintextChar = (char) decrypted.intValue();
            plaintextBuilder.append(plaintextChar);
        }
        return plaintextBuilder.toString();
    }

    public void setP(int p) {
        this.p = p;
    }

    public void setQ(int q) {
        this.q = q;
    }
}
