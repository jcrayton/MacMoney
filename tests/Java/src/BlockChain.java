import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.io.File;  // Import the File class
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlockChain {
    int difficulty;
    private int miningReward;
    private List<Block> chain;
    private List<Transaction> pendingTransactions;
    List<String> addresses;

    private BlockChain() {
        // https://stackoverflow.com/questions/52708773/cannot-deserialize-from-object-value-no-delegate-or-property-based-creator-ev
    }

    BlockChain(int difficulty, int miningReward, List<Block> chain, List<Transaction> pendingTransactions, List<String> addresses) throws NoSuchAlgorithmException, IOException {
        this();
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;
        this.addresses = new ArrayList<>();
        this.createGenisisBlock();
        this.miningReward = miningReward;

        updateChain(this);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMiningReward() {
        return miningReward;
    }

    public List<Block> getChain() {
        return chain;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    private void updateChain(BlockChain chain) throws IOException {

        // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("chain.json"), chain);
    }

    private void createGenisisBlock() throws NoSuchAlgorithmException, IOException {
        List<Transaction> temp = new ArrayList<>();
        temp.add(new Transaction("", "", 0));

        Block origin = new Block(LocalDateTime.now().toString(),temp, "nope");
        origin.hash = origin.calculateHash();

        this.chain.add(origin);
    }
    private Block getLatestBlock() {
        return this.chain.get(this.chain.size()-1);
    }

    void minePendingTransactions(String miningRewardAddress) throws NoSuchAlgorithmException, IOException {
        List<Transaction> temp = new ArrayList<>(this.pendingTransactions);
        Block newBlock = new Block(LocalDateTime.now().toString(), temp, this.getLatestBlock().hash);
        newBlock.hash = newBlock.calculateHash();
        newBlock.mineBlock(this.difficulty);

        System.out.println("Block Mined " + "\t" + newBlock.hash);
        this.chain.add(newBlock);
        this.pendingTransactions.clear();
        addPendingTransaction(new Transaction(null, miningRewardAddress, miningReward));
        updateChain(this);
    }
    void addPendingTransaction(Transaction exchange) {
        this.pendingTransactions.add(exchange);
        if(!this.addresses.contains(exchange.toAddress)){
            this.addresses.add(exchange.toAddress);
        }
        if(!this.addresses.contains(exchange.fromAddress)){
            this.addresses.add(exchange.fromAddress);
        }
    }

    int getBalanceOfAddress(String address) {
        var balance = 0;

        for(Block block: this.chain) {
            if (!block.previousHash.equals("nope")) {
                for (Transaction T : block.transactions) {
                    if(T.fromAddress != null) {if (T.fromAddress.equals(address)) balance = balance - T.amount;}
                    if(T.toAddress.equals(address)) balance = balance + T.amount;
                }
            }
        }
        return balance;
    }

    Boolean isChainValid() throws NoSuchAlgorithmException {
        for (int i = 1; i < this.chain.size(); i++) {
            Block currentBlock = this.chain.get(i);
            Block previousBlock = this.chain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {System.out.println("1"); return false;}
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {System.out.println("2"); return false;}
        }
        return true;
    }
    void printable() {
        for(Block temp: this.chain) {
            System.out.println(temp.timestamp + "\t"
                    + temp.transactions + "\t" + temp.previousHash + "\t" + temp.hash);
        }
    }
}
