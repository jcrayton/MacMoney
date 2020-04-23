import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Block {
    String timestamp;
    List<Transaction> transaction;
    String previousHash;
    String hash;
    int nonce;

    Block(String timestamp, List<Transaction> transaction, String previousHash) {
        this.timestamp = timestamp;
        this.transaction = transaction;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = "";
    }

    public int getNonce() {
        return nonce;
    }

    public String getHash() {
        return hash;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String calculateHash() throws NoSuchAlgorithmException
    {
        String input =  this.timestamp + "\t" + this.transaction + "\t" + this.previousHash + "\t" + String.valueOf(this.nonce);
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
    }
    private String getDif(int length) {
            char[] array = new char[length];
            Arrays.fill(array, '0');
            return new String(array);
    }
}
