import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

public class BlockChain {
    List<Block> chain;
    int difficulty;
    List<Transaction> pendingTransactions;
    int miningReward = 100;
    List<String> addresses;

    public BlockChain(int difficulty, int miningReward) {
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;
        this.addresses = new ArrayList<>();
    }

    public void createGenisisBlock() throws NoSuchAlgorithmException {
        Block origin = new Block(LocalDateTime.now().toString(),null, "nope");
        origin.hash = origin.calculateHash();
        this.chain.add(origin);
    }
    public Block getLatestBlock() {
        return this.chain.get(this.chain.size()-1);
    }

    public void minePendingTransactions(String miningRewardAddress) throws NoSuchAlgorithmException {
        List<Transaction> temp = new ArrayList<>(this.pendingTransactions);
        Block newBlock = new Block(LocalDateTime.now().toString(), temp, this.getLatestBlock().hash);
        newBlock.hash = newBlock.calculateHash();
        newBlock.mineBlock(this.difficulty);

        System.out.println("Block Mined " + "\t" + newBlock.hash);
        this.chain.add(newBlock);
        this.pendingTransactions.clear();
        addPendingTransaction(new Transaction(null, miningRewardAddress, miningReward));
    }
    public void addPendingTransaction(Transaction exchange) {
        this.pendingTransactions.add(exchange);
        if(!this.addresses.contains(exchange.toAddress)){
            this.addresses.add(exchange.toAddress);
        }
        if(!this.addresses.contains(exchange.fromAddress)){
            this.addresses.add(exchange.fromAddress);
        }
    }

    public int getBalanceOfAddress(String address) {
        var balance = 0;

        for(Block block: this.chain) {
            if (block.previousHash != "nope") {
                for (Transaction T : block.transaction) {
                    if(T.fromAddress != null) {if (T.fromAddress.equals(address)) balance = balance - T.amount;}
                    if(T.toAddress.equals(address)) balance = balance + T.amount;
                }
            }
        }
        return balance;
    }

    public Boolean isChainValid() throws NoSuchAlgorithmException {
        for (int i = 1; i < this.chain.size(); i++) {
            Block currentBlock = this.chain.get(i);
            Block previousBlock = this.chain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {System.out.println("1"); return false;}
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {System.out.println("2"); return false;}
        }
        return true;
    }
    public void printable() {
        for(Block temp: this.chain) {
            System.out.println(temp.timestamp + "\t"
                    + temp.transaction + "\t" + temp.previousHash + "\t" + temp.hash);
        }
    }
}
