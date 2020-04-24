import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        BlockChain theChain;
        String miningID;
//            Use if you need a new blockchain started
        theChain = new BlockChain(5, 100, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        if (args.length != 0) {
            String chainLocal = args[0];
            if (args.length >= 2) {miningID = args[1]; System.out.println("Sending Rewards to miner: " + miningID);}
            else {miningID = "Charity"; System.out.println("No mining id provided. Sending rewards to charity.");}
            System.out.println();
            // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
            try {
                ObjectMapper mapper = new ObjectMapper();
                theChain = mapper.readValue(new File(chainLocal), BlockChain.class);

                try {
                    theChain.setPendingTransactions(mapper.readValue(new File("PendingTransactions.json"), new TypeReference<List<Transaction>>(){}));
                }
                catch (Exception e) {
                    System.out.println("No prior pending transactions found. Creating a new list.");
                    // https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
                    mapper.writeValue(new File("PendingTransactions.json"), "");
                }

                theChain.addPendingTransaction(new Transaction("Bill", "Bob", 50));

                int i = 0;
                while(i <= 10) {
                    theChain.minePendingTransactions(miningID);
                    System.out.print("\t" + "#" + i);
                    i++;
                }

//                theChain.printable();
                System.out.println("\n" + theChain.isChainValid());
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
