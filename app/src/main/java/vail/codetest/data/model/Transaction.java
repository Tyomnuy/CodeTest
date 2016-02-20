package vail.codetest.data.model;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class Transaction {

    private String sku;
    private float amount;
    private String currency;

    private float gbpAmmount;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getAmmountGBP() {
        return gbpAmmount;
    }

    public void setGbpAmmount(float gbpAmmount) {
        this.gbpAmmount = gbpAmmount;
    }
}
