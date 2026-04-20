package Interfaces;

/**
 * DecryptorMaster defines the interface for log decryption strategies.
 */
public interface DecryptorMaster {

    /**
     * Checks if the text can be decrypted by this implementation.
     * @param encryptedText The text to check.
     * @return True if decryption is possible.
     */
    boolean canDecrypt(String encryptedText);

    /**
     * Decrypts the provided text.
     * @param encryptedText The text to decrypt.
     * @return The decrypted string.
     */
    String decrypt(String encryptedText);
}
