import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Block {
    int index;
    String timestamp;
    String data;
    String previousHash;
    String hash;
    private int nonce;

    public String calculateHash() throws NoSuchAlgorithmException
    {
        String input = String.valueOf(this.index) + "\t" + this.timestamp + "\t" + this.data + "\t" + this.previousHash + "\t" + String.valueOf(this.nonce);
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

    public void mineBlock(int dif)throws NoSuchAlgorithmException {
        while (!this.hash.substring(0, dif).equals(getDif(dif))) {
            this.nonce++;
            this.hash = this.calculateHash();
        }
        System.out.println("Block Mined");
    }
    private String getDif(int length) {
            char[] array = new char[length];
            Arrays.fill(array, '0');
            return new String(array);
    }
}
