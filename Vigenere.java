class Vigenere {
    private static final int ASCII_RANGE = 255;

    public static String encrypt(String text, String keyword) {
        String key = Vigenere.generateKey(text, keyword);
        return Vigenere.cipherText(text, key);
    }

    public static String decrypt(String text, String keyword) {
        String key = Vigenere.generateKey(text, keyword);
        return Vigenere.decipherText(text, key);
    }

    private static String generateKey(String str, String keyword) {
        int x = str.length();
        String key = keyword;

        if (keyword.length() > str.length()) {
            return keyword.substring(0, str.length());
        }

        for (int i = 0;; i++) {
            if (x == i)
                i = 0;
            if (key.length() == str.length())
                break;
            key += (key.charAt(i));
        }
        return key;
    }

    private static String cipherText(String str, String key) {
        String cipherTest = "";
        for (int i = 0; i < str.length(); i++) {
            int x = (str.charAt(i) + key.charAt(i)) % Vigenere.ASCII_RANGE;
            cipherTest += (char) x;
        }
        return cipherTest;
    }

    private static String decipherText(String cipherText, String key) {
        String originalText = "";
        for (int i = 0; i < cipherText.length() && i < key.length(); i++) {
            int x = (cipherText.charAt(i) - key.charAt(i) + Vigenere.ASCII_RANGE) % Vigenere.ASCII_RANGE;
            originalText += (char) x;
        }
        return originalText;
    }
}

