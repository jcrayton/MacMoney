import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String miningID;
        if (args.length != 0) miningID = args[0];
        else miningID = "Charity";
//        miningID = "charity";
        BlockChain sample = new BlockChain(1, 50);

        Transaction first = new Transaction("Bill", "Bob", 50);
        sample.addPendingTransaction(first);

        for(int i = 0; i<= 25; i++) {
            sample.minePendingTransactions(miningID);
        }

        sample.printable();
        System.out.println(sample.isChainValid());
        for(String name: sample.addresses) {
            System.out.println(name + '\t' + sample.getBalanceOfAddress(name));
        }
    }
}
