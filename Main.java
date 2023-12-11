import java.util.List;
import java.util.Arrays;

class Main {
    public static void main(String args[]) throws InterruptedException {
        String text = """
                    Lorem ipsum dolor sit amét, aliquyam consentáneum convenire data defensá, dicitis eripuit indignius modo né posset putem quietem.
                    Accessio doloribus inciderit litterae, placet quantaque tenent?
                    Aéqui compárándáe eruditus inflammati.
                    Accuratius aéquum amet cupidatat discenda, disputatum gratia imperió numen quam quoquo repellere telos?
                    Bella fugiendis loco ludicra, posidonium praeclare próbantur.
                    Chrysippe dicénda iucunde patientia vitam?
                    Calere fugiendus instituéndarum institutionem, interdictum interdum laboris videbitur!
                    Aequitatem aristippi coniunctione imagines, maéstitiam maior parum possent, securi simul.
                    Divitiarum erimus familias géométria locatus movét praéclarorum putamus témpérantia. árs asperner deserere effecerit éxaudita, facete inportuno scilicet!
                    Affirmatis amentur ángere impétu, impetum intéllégat medeam propositum, quaerimus sentió solám utens!
                """;
        String key = "Lorem ipsum amét 123!#?";
        HybridEncryptor hybridEncryptor = new HybridEncryptor(key, 4421);
        String[] keywords = { "Lorem ipsum amét 123!#?", "nmaskdjh1230954", "245!@#test-123", "Coding is my passion" };
        long[] publicKeys = { 4421, 27947, 19819, 18911 };
        // RSA rsa = new rsa(2749, 2621, 4421);
        // HybridEncryptor hybridEncryptor = new HybridEncryptor(key, rsa);

        String encryptText = hybridEncryptor.encrypt(text);

        String encryptTextDistinct = HybridEncryptor.encryptDistinct(text, keywords, publicKeys);
        System.out.println("Crypt================================");
        System.out.println(encryptText);

        System.out.println("\n\n");

        String originalText = hybridEncryptor.decrypt(encryptText);
        String originalTextDistinct = HybridEncryptor.decryptDistinct(encryptTextDistinct, keywords, publicKeys);
        System.out.println("Decrypt================================");
        System.out.println(originalText);

        System.out.println("\n\n");

        System.out.println("EncryptedText equals EncryptTextDistinct " + encryptText.equals(encryptTextDistinct));
        System.out.println("OriginalText equals OriginalTextDistinct " + originalText.equals(originalTextDistinct));
    }
}
