import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String miningID;
        if (args.length != 0) miningID = args[0];
        else miningID = "Charity";


        BlockChain sample;

        // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
        try {
            ObjectMapper mapper = new ObjectMapper();
            sample = mapper.readValue(new File("chain.json"), BlockChain.class);
            System.out.println(sample.difficulty);
        }
        catch (IOException e) {
            System.out.println("We've had a problem reading the chain.json file");
            System.out.println(e);
            sample = new BlockChain(2, 15);
        }
//        BlockChain sample = new BlockChain(1, 50);

        sample.addPendingTransaction(new Transaction("Bill", "Bob", 50));

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
