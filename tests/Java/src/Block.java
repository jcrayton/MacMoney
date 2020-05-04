import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
        String input =  this.timestamp + "\t" + new ObjectMapper().writeValueAsString(this.transactions) + "\t" + this.previousHash + "\t" + this.nonce;
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

    void mineBlock(BlockChain chain) throws NoSuchAlgorithmException, IOException, InterruptedException {
        while (!this.hash.substring(0, chain.getDifficulty()).equals(getDif(chain.getDifficulty()))) {
            try{
                if (chain.checkForChanges()) {
                    System.out.println("Chain Slipped, resetting...");
                    this.nonce = -1;
                    this.previousHash = chain.getLatestBlock().hash;
                }
                this.nonce++;
                this.hash = this.calculateHash();
            }
            catch (Exception e) {
                // I'm not proud of this
                Thread.sleep(100);
            }
        }
    }
    private String getDif(int length) {
            char[] array = new char[length];
            Arrays.fill(array, '0');
            return new String(array);
    }
//    private Boolean checkForChanges(BlockChain chain) throws IOException, NoSuchAlgorithmException {
//        BlockChain verififyChain = new ObjectMapper().readValue(new File("chain.json"), BlockChain.class);
//        if (!verififyChain.isChainValid()) { //the other chain is wrong, continue as normal
//            chain.updateChain(); //fix it
//            return false;
//        }
//        if (verififyChain.getLatestBlock().calculateHash().equals(chain.getLatestBlock().calculateHash()) &&
//                verififyChain.getLatestBlock().previousHash.equals(chain.getLatestBlock().previousHash)) return false; //should be the same if true
//        for(Block block: verififyChain.getChain()) if (block.previousHash.equals(chain.getLatestBlock().previousHash) && block.calculateHash().equals(chain.getLatestBlock().calculateHash())) {
//            chain = verififyChain;
//            return true;
//        }
//        chain.updateChain(); //technically correct, but we are now forked. Commit to the path!!
//        return false;
//    }
}
