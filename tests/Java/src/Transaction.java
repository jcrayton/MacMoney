public class Transaction implements Cloneable {
    String fromAddress;
    String toAddress;
    int amount;

    public Transaction(String fromAddress, String toAddress, int amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }
}
