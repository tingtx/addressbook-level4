package seedu.address.logic.commands.digestUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashDigest {
    /**
     * Return the hash digest of {@code text}. Used for creating accounts and validating log-ins.
     */
    public byte[] getHashDigest(String text){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
