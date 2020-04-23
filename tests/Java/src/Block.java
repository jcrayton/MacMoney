import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Block {
    String timestamp;
    List<Transaction> transactions;
    String previousHash;
    String hash;
    private int nonce;

    private Block() {
        // https://stackoverflow.com/questions/52708773/cannot-deserialize-from-object-value-no-delegate-or-property-based-creator-ev
    }

    Block(String timestamp, List<Transaction> transactions, String previousHash) {
        this(); // https://stackoverflow.com/questions/52708773/cannot-deserialize-from-object-value-no-delegate-or-property-based-creator-ev
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
//        this.nonce = 0;
        this.hash = "";
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getNonce() {
        return nonce;
    }

    public String getHash() {
        return hash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    String calculateHash() throws NoSuchAlgorithmException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String transactionString = mapper.writeValueAsString(this.transactions);
        String input =  this.timestamp + "\t" + transactionString + "\t" + this.previousHash + "\t" + String.valueOf(this.nonce);
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

    void mineBlock(int dif) throws NoSuchAlgorithmException, JsonProcessingException {
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
