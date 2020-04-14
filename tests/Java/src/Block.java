import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    int index;
    String timestamp;
    String data;
    String previousHash;
    String hash;

    public static String calculateHash(Block info) throws NoSuchAlgorithmException
    {
        String input = String.valueOf(info.index) + "\t" + info.timestamp + "\t" + info.data + "\t" + info.previousHash + "\t";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
