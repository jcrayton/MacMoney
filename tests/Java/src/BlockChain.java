import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.io.File;  // Import the File class
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlockChain {
    private int difficulty;
    private int miningReward;
    private List<Block> chain;
    List<Transaction> pendingTransactions;
    List<String> chainList;
    List<String> addresses;

    private BlockChain() {
        // https://stackoverflow.com/questions/52708773/cannot-deserialize-from-object-value-no-delegate-or-property-based-creator-ev
    }

    BlockChain(int difficulty, int miningReward, List<Block> chain, List<Transaction> pendingTransactions, List<String> addresses, List<String> chainList) throws IOException {
        this();
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;
        this.addresses = new ArrayList<>();
        this.createGenesisBlock();
        this.miningReward = miningReward;
        this.chainList = new ArrayList<>();

        updateChain();
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

    public List<String> getChainList() { return chainList; }

    public void setPendingTransactions(List<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public void setChainList(List<String> chainList) {
        this.chainList = chainList;
    }

    void updateChain() throws IOException {

        // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("chain.json"), this);
    }

    private void createGenesisBlock() {
        List<Transaction> temp = new ArrayList<>();
        temp.add(new Transaction("", "", 0));

        Block origin = new Block(LocalDateTime.now().toString(),temp, "nope");
        origin.hash = "";

        this.chain.add(origin);
    }
    Block getLatestBlock() {
        return this.chain.get(this.chain.size()-1);
    }

    void minePendingTransactions(String miningRewardAddress) throws NoSuchAlgorithmException, IOException, InterruptedException {
        List<Transaction> temp = new ArrayList<>(this.pendingTransactions);
        Block newBlock = new Block(LocalDateTime.now().toString(), temp, this.getLatestBlock().hash);
        newBlock.hash = newBlock.calculateHash();

        this.pendingTransactions.clear();
//         https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("PendingTransactions.json"), pendingTransactions);

        newBlock.mineBlock(this);
        System.out.print("\r" + "Block Mined " + "\t" + newBlock.hash);
        this.chain.add(newBlock);

        addPendingTransaction(new Transaction(null, miningRewardAddress, miningReward));
        updateChain();
    }
    void addPendingTransaction(Transaction exchange) throws IOException {
        this.pendingTransactions.add(exchange);

        // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("PendingTransactions.json"), pendingTransactions);

//        if(!this.addresses.contains(exchange.toAddress)){
//            this.addresses.add(exchange.toAddress);
//        }
//        if(!this.addresses.contains(exchange.fromAddress) && exchange.fromAddress != null){
//            this.addresses.add(exchange.fromAddress);
//        }
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

    Boolean isChainValid() throws NoSuchAlgorithmException, JsonProcessingException {
        for (int i = 1; i < this.chain.size(); i++) {
            Block currentBlock = this.chain.get(i);
            Block previousBlock = this.chain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {System.out.println("1"); return false;}
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {System.out.println("2"); return false;}
        }
        return true;
    }
    void printable() {
//        for(Block temp: this.chain) {
//            System.out.println(temp.timestamp + "\t"
//                    + temp.transactions + "\t" + temp.previousHash + "\t" + temp.hash + "\t" + temp.getNonce());
//        }
        for (Block b : this.chain) for (Transaction t : b.transactions) {

            if(!this.addresses.contains(t.toAddress)){
                this.addresses.add(t.toAddress);
            }
            if(!this.addresses.contains(t.fromAddress) && t.fromAddress != null){
                this.addresses.add(t.fromAddress);
            }

        }
    }
    Boolean checkForChanges() throws IOException, NoSuchAlgorithmException {
        for (String location : this.chainList) {
            BlockChain verififyChain = new ObjectMapper().readValue(new File(location), BlockChain.class);
            if (verififyChain.isChainValid()) { //the other chain is wrong, continue as normal
                if (!verififyChain.getLatestBlock().calculateHash().equals(this.getLatestBlock().calculateHash())) {
                    for (Block block : verififyChain.chain) {
                        if (block.previousHash.equals(this.getLatestBlock().previousHash) && block.calculateHash().equals(this.getLatestBlock().calculateHash())) {
                            this.chain = verififyChain.chain;
                            updateChain(); //fix it
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
