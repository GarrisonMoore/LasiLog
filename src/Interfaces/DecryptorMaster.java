package Interfaces;

public interface DecryptorMaster {

    boolean canDecrypt(String encryptedText);

    String decrypt(String encryptedText);
}
