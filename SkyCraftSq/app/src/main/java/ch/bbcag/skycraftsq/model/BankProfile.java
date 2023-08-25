package ch.bbcag.skycraftsq.model;


import java.util.ArrayList;
import java.util.List;

public class BankProfile {
    private int id;

    private List<TransactionItem> transactions = new ArrayList<>();

    public BankProfile () {

    }

    public BankProfile (int id, List<TransactionItem> transactions) {
        this.id = id;
        this.transactions = transactions;
    }

    public void addTransaction(TransactionItem transactionItem) {
        transactions.add(transactionItem);
    }
    public void removeTransaction (TransactionItem transactionItem) {
        transactions.remove(transactionItem);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<TransactionItem> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionItem> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "BankProfile " + id;
    }
}
