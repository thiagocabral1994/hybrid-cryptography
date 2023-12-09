abstract class BaseCryptThread extends Thread {
    String text;
    String key;
    RSA rsa;
    String result;

    public BaseCryptThread(String text, String key, RSA rsa) {
        this.text = text;
        this.key = key;
        this.rsa = rsa;
    }

    public String getMessage() {
        return this.result;
    }
    public abstract void run();
}
