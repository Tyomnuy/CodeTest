package vail.codetest.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vail.codetest.data.model.Rate;

/**
 * Utility that helps to find currency rate using breadth-first search with caching already processed path of tree.
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class CurrencyRateCalculator {

    public static final String GBP = "GBP";

    private List<Rate> rateList;

    private Rate[][] ratesTable;
    private int tableSize;
    private List<String> currencyList = new ArrayList<>();

    public CurrencyRateCalculator(List<Rate> rateList) {
        this.rateList = rateList;

        initRateTable();
    }

    public float getGRBAmount(String currency, float amount) {
        if (TextUtils.isEmpty(currency)) return 0;

        float rate = getRateExchange(currency);

        return amount * rate;
    }

    private void initRateTable() {
        Iterator<Rate> iterator = rateList.listIterator();
        while(iterator.hasNext()) {
            Rate rate = iterator.next();
            if (isValid(rate)) {
                addCurrency(rate.getFrom().toUpperCase());
                addCurrency(rate.getTo().toUpperCase());
            } else {
                iterator.remove();
            }
        }
        tableSize = currencyList.size();
        ratesTable = new Rate[tableSize][tableSize];

        for (Rate rate: rateList) {
            int x = currencyList.indexOf(rate.getFrom().toUpperCase());
            int y = currencyList.indexOf(rate.getTo().toUpperCase());
            ratesTable[x][y] = rate;
        }
    }

    private boolean isValid(Rate rate) {
        return !TextUtils.isEmpty(rate.getFrom()) && !TextUtils.isEmpty(rate.getTo()) && rate.getRate() > 0;
    }

    private void addCurrency(String currency) {
        if (!currencyList.contains(currency)) {
            currencyList.add(currency);
        }
    }

    private float getRateExchange(String currency) {
        if (GBP.equals(currency)) return 1;

        int currencyIndex = currencyList.indexOf(currency);
        if (currencyIndex == -1) return 0;

        List<Rate> processedRates = new ArrayList<>();
        List<RateWrapper> itemToProceed = new ArrayList<>();


        itemToProceed.add(new RateWrapper(null,null,currencyIndex));
        do {
            RateWrapper parent = itemToProceed.remove(0);
            Rate[] row = ratesTable[parent.rateIndex];
            for (int i = 0; i < tableSize; i++) {
                Rate rate = row[i];
                if (rate == null || processedRates.indexOf(rate) >= 0) {
                    continue;
                }
                if (rate.getRateToGBP() > 0 || rate.getTo().equals(GBP)) {
                    return calculateRate(parent, rate);
                }
                processedRates.add(rate);
                itemToProceed.add(new RateWrapper(rate, parent, i));
            }
        } while (itemToProceed.size() > 0);

        return 0;
    }

    private float calculateRate(RateWrapper rateWrapper, Rate rate) {
        float rateExchange = rate.getRateToGBP();
        if (rateExchange == 0) {
            rateExchange = rate.getRate();
        }

        while (rateWrapper.parent != null) {
            rateExchange *= rateWrapper.current.getRate();
            rateWrapper.current.setRateToGBP(rateExchange);
            rateWrapper = rateWrapper.parent;
        }

        return rateExchange;
    }

    /**
     * Helper class to restore path
     */
    private static class RateWrapper {
        Rate current;
        RateWrapper parent;
        int rateIndex;

        public RateWrapper(Rate current, RateWrapper parent, int rateIndex) {
            this.current = current;
            this.parent = parent;
            this.rateIndex = rateIndex;
        }
    }
}
