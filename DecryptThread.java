class DecryptThread extends BaseCryptThread {
    public DecryptThread(String text, String key, RSA rsa) {
        super(text, key, rsa);
    }

    public void run() {
        String vigenereText = rsa.decrypt(this.text);
        this.result = Vigenere.decrypt(vigenereText, this.key);
    }
}
