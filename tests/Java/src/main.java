import java.security.NoSuchAlgorithmException;

public class main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String miningID;
        if (args[0] != null) miningID = args[0];
        else miningID = "charity";
        BlockChain sample = new BlockChain(5, 50);
        sample.createGenisisBlock();

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
