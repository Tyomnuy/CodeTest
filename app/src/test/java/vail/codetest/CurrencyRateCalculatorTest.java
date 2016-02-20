package vail.codetest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import vail.codetest.data.model.Rate;
import vail.codetest.utils.CurrencyRateCalculator;

import static org.junit.Assert.*;

/**
 * Robolectric used because of dependencies to android sdk like TextUtils
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CurrencyRateCalculatorTest {

    private static final double DIFF = 0.00000001;

    private static final String USD = "USD";
    private static final String EUR = "EUR";
    private static final String UAH = "UAH";

    @Test
    public void testNoPath() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, EUR, 1.5f);
        Rate rateUAH = new Rate(UAH, USD, 2.5f); // ^^
        rateList.add(rateUSD);
        rateList.add(rateUAH);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(0f, currencyRateCalculator.getGRBAmount(USD, 0), DIFF);
    }

    @Test
    public void testDirect() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, EUR, 1.5f);
        Rate rateUAH = new Rate(UAH, CurrencyRateCalculator.GBP, 2.5f); // ^^
        rateList.add(rateUSD);
        rateList.add(rateUAH);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(rateUAH.getRate(), currencyRateCalculator.getGRBAmount(UAH, 1), DIFF);
    }

    @Test
    public void testOneLevel() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, UAH, 1.5f);
        Rate rateUAH = new Rate(UAH, CurrencyRateCalculator.GBP, 2.5f); // ^^
        rateList.add(rateUSD);
        rateList.add(rateUAH);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        float expected = rateUSD.getRate() * rateUAH.getRate();
        assertEquals(expected, currencyRateCalculator.getGRBAmount(USD, 1f), DIFF);
    }

    @Test
    public void testTwoLevels() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, EUR, 1.5f);
        Rate rateEUR = new Rate(EUR, UAH, 2.5f);
        Rate rateUAH = new Rate(UAH, CurrencyRateCalculator.GBP, 2.5f); // ^^
        rateList.add(rateUSD);
        rateList.add(rateEUR);
        rateList.add(rateUAH);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        float expected = rateUSD.getRate() * rateEUR.getRate() * rateUAH.getRate();
        assertEquals(expected, currencyRateCalculator.getGRBAmount(USD, 1), DIFF);
    }

    @Test
    public void testCaching() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, EUR, 1.5f);
        Rate rateEUR = new Rate(EUR, UAH, 0.5f);
        Rate rateUAH = new Rate(UAH, CurrencyRateCalculator.GBP, 2.5f); // ^^
        rateList.add(rateUSD);
        rateList.add(rateEUR);
        rateList.add(rateUAH);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);
        currencyRateCalculator.getGRBAmount(USD, 1);

        float expected = rateEUR.getRate() * rateUAH.getRate();
        assertEquals(expected, rateEUR.getRateToGBP(), DIFF);
    }

    @Test
    public void testAmount() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, CurrencyRateCalculator.GBP, 1.5f);
        rateList.add(rateUSD);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        float expected = 23.5f * rateUSD.getRate();
        assertEquals(expected, currencyRateCalculator.getGRBAmount(USD, 23.5f), DIFF);
    }

    @Test
    public void testZeroAmount() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, CurrencyRateCalculator.GBP, 1.5f);
        rateList.add(rateUSD);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(0f, currencyRateCalculator.getGRBAmount(USD, 0f), DIFF);
    }

    @Test
    public void testGBPtoGBP() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, CurrencyRateCalculator.GBP, 1.5f);
        Rate rateUSD2 = new Rate(USD, EUR, 0.1f);
        Rate rateUSD3 = new Rate(USD, CurrencyRateCalculator.GBP, 4.5f);
        rateList.add(rateUSD);
        rateList.add(rateUSD2);
        rateList.add(rateUSD3);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(1f, currencyRateCalculator.getGRBAmount(CurrencyRateCalculator.GBP, 1f), DIFF);
    }

    @Test
    public void testEmptyFromTo() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, "", 1.5f);
        Rate rate2 = new Rate("", CurrencyRateCalculator.GBP, 0.1f);
        rateList.add(rateUSD);
        rateList.add(rate2);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(0f, currencyRateCalculator.getGRBAmount(USD, 1f), DIFF);
    }

    @Test
    public void testNotValidRate() throws Exception {
        List<Rate> rateList = new ArrayList<>();
        Rate rateUSD = new Rate(USD, CurrencyRateCalculator.GBP, -20f);
        Rate rateUSD2 = new Rate(USD, CurrencyRateCalculator.GBP, -1f);
        rateList.add(rateUSD);
        rateList.add(rateUSD2);

        CurrencyRateCalculator currencyRateCalculator = new CurrencyRateCalculator(rateList);

        assertEquals(0f, currencyRateCalculator.getGRBAmount(USD, 1f), DIFF);
    }
}