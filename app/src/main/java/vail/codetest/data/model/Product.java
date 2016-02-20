package vail.codetest.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class Product {

    private String id;
    private List<Transaction> transactions = new ArrayList<>();
    private boolean calculated = false;
    private float totalAmountGBP;

    public Product(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public float getTotalAmountGBP() {
        return totalAmountGBP;
    }

    public void setTotalAmountGBP(float totalAmountGBP) {
        this.totalAmountGBP = totalAmountGBP;
    }
}
