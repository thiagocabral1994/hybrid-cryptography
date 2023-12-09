class EncryptThread extends BaseCryptThread {
    public EncryptThread(String text, String key, RSA rsa) {
        super(text, key, rsa);
    }

    public void run() {
        String vigenereText = Vigenere.encrypt(this.text, this.key);
        this.result = rsa.encrypt(vigenereText);
    }
}
