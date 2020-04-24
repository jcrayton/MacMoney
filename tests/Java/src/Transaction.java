
public class Transaction {
    private final String type = "transaction";
    String fromAddress;
    String toAddress;
    int amount;

    private Transaction() {
        // https://stackoverflow.com/questions/52708773/cannot-deserialize-from-object-value-no-delegate-or-property-based-creator-ev
    }

    Transaction(String fromAddress, String toAddress, int amount) {
        this();
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

}
