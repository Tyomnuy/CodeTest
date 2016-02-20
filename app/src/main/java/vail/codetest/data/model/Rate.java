package vail.codetest.data.model;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class Rate {

    private String from;
    private String to;
    private float rate;
    private float rateToGBP;

    public Rate() {

    }

    public Rate(String from, String to, float rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRateToGBP() {
        return rateToGBP;
    }

    public void setRateToGBP(float rateToGBP) {
        this.rateToGBP = rateToGBP;
    }
}
