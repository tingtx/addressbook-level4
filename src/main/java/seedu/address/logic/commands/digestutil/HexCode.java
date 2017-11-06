package seedu.address.logic.commands.digestutil;

import org.apache.commons.codec.binary.Hex;
import java.util.Base64;

public class HexCode {
    public String getHexFormat(byte[] userNameHash) {
        return new Base64.getEncoder();
    }

    public String hexStringToByteArray(String saltHex) {
        return Base64.base64Decode(saltHex);
    }
}
