import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BlockChain sample = new BlockChain();
        sample.createGenisisBlock();
//        Block first = new Block(LocalDateTime.now().toString(), null, sample.getLatestBlock().hash);
        Transaction first = new Transaction("Bill", "Bob", 50);
//        first.timestamp = "10/10/2010";
//        first.transaction = "{ammount: 1}";
//        first.previousHash = sample.getLatestBlock().hash;
//        first.hash = first.calculateHash();
        sample.addPendingTransaction(first);
        for(int i = 0; i<= 25; i++) {
            sample.minePendingTransactions("James");
        }

        System.out.println(sample.isChainValid());
        sample.printable();
    }
}
