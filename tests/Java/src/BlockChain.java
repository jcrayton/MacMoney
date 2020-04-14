import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BlockChain {
    List<Block> chain;

    public void createGenisisBlock() throws NoSuchAlgorithmException {

        this.chain = new ArrayList<>();

        Block origin = new Block();
        origin.index = 0;
        origin.timestamp = "01/01/2000";
        origin.data = "origin";
        origin.previousHash = "null";
        origin.hash = Block.calculateHash((origin));
        this.chain.add(origin);
    }
    public Block getLatestBlock() {
        return this.chain.get(this.chain.size()-1);
    }
    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {
        newBlock.previousHash = this.getLatestBlock().hash;
        newBlock.hash = Block.calculateHash(newBlock);
        this.chain.add(newBlock);
    }
    public Boolean isChainValid() throws NoSuchAlgorithmException {
        for (int i = 1; i < this.chain.size(); i++) {
            Block currentBlock = this.chain.get(i);
            Block previousBlock = this.chain.get(i-1);

            if (!currentBlock.hash.equals(Block.calculateHash(currentBlock))) {System.out.println("1"); return false;}
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {System.out.println("2"); return false;}
            if (currentBlock.index <= previousBlock.index) {System.out.println("3"); return false;}
        }
        return true;
    }
    public void printable() {
        for(Block temp: this.chain) {
            System.out.println(temp.index + "\t" +  temp.timestamp + "\t"
                    + temp.data + "\t" + temp.previousHash + "\t" + temp.hash);
        }
    }
}
