import java.security.NoSuchAlgorithmException;

public class main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BlockChain sample = new BlockChain();
        sample.createGenisisBlock();
        Block first = new Block();
        first.index = 1;
        first.timestamp = "10/10/2010";
        first.data = "{ammount: 1}";
        first.previousHash = sample.getLatestBlock().hash;
        first.hash = Block.calculateHash(first);
        sample.addBlock(first);

        System.out.println(sample.isChainValid());
        sample.printable();
    }
}
