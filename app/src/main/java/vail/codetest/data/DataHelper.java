package vail.codetest.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vail.codetest.data.model.Product;
import vail.codetest.data.model.Rate;
import vail.codetest.data.model.Transaction;
import vail.codetest.utils.CurrencyRateCalculator;

/**
 * Using static storage helps avoid useless serialization of big data during transferring from Activities.
 * And increase rate calculation by caching data.
 *
 * @author Evgen Marinin <ievgen.marinin@alterplay.com>
 * @since 20.02.16.
 */
public class DataHelper {

    private static final DataHelper INSTANCE = new DataHelper();

    private List<Transaction> transactionList;
    private Map<String, Product> productMap;

    private CurrencyRateCalculator calculator;

    public static DataHelper getInstance() {
        return INSTANCE;
    }

    public void setRates(List<Rate> rateList) {
        if (rateList == null) {
            rateList = new ArrayList<>();
        }

        this.calculator = new CurrencyRateCalculator(rateList);
    }

    public void setTransactions(List<Transaction> transactionList) {
        if (transactionList == null) {
            transactionList = new ArrayList<>();
        }
        this.transactionList = transactionList;
    }

    public Collection<Product> loadProducts() {
        if (productMap == null) {
            productMap = new HashMap<>();
            Product product;
            for (Transaction transaction: transactionList) {
                String id = transaction.getSku();
                product = productMap.get(id);
                if (product == null) {
                    product = new Product(id);
                    productMap.put(id, product);
                }
                product.addTransaction(transaction);
            }
        }

        return productMap.values();
    }

    public Product loadProduct(String id) {
        if (productMap == null) return null;

        Product product = productMap.get(id);
        if (!product.isCalculated()) {
            float totalAmountGBP = 0;
            for (Transaction transaction: product.getTransactions()) {
                float amountGBP = calculator.getGRBAmount(transaction.getCurrency(), transaction.getAmount());
                transaction.setGbpAmmount(amountGBP);
                totalAmountGBP += amountGBP;
            }
            product.setTotalAmountGBP(totalAmountGBP);
        }

        return product;
    }
}
