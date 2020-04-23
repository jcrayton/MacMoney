import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        BlockChain theChain;
        String miningID;
//            Use if you need a new blockchain started
        theChain = new BlockChain(2, 100, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        if (args.length != 0) {
            String chainLocal = args[0];
            if (args.length >= 2) {miningID = args[1]; System.out.println("Sending Rewards to miner: " + miningID);}
            else {miningID = "Charity"; System.out.println("No mining id provided. Sending rewards to charity.");}

            // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
            try {
                ObjectMapper mapper = new ObjectMapper();
                theChain = mapper.readValue(new File(chainLocal), BlockChain.class);

                theChain.addPendingTransaction(new Transaction("Bill", "Bob", 50));

                for(int i = 0; i<= 25; i++) {
                    theChain.minePendingTransactions(miningID);
                }

//                theChain.printable();
                System.out.println(theChain.isChainValid());
                for(String name: theChain.addresses) {
                    System.out.println(name + '\t' + theChain.getBalanceOfAddress(name));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Please provide a path to the local copy of the blockchain.");

    }
}
