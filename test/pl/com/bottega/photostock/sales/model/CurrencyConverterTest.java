package pl.com.bottega.photostock.sales.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CurrencyConverterTest {

    private final Money sevenPln = Money.valueOf(7, "PLN");
    private final Money twoBucks = Money.valueOf(2, "USD");
    private final Money twoBitcoins = Money.valueOf(2, "BTC");
    private Map<String, Double> exRates = new HashMap<>();

    private CurrencyConverter sut;

    @Before
    public void setUp() {
        exRates.put("USD", 3.5);
        exRates.put("EUR", 4.2101);
        sut = new CurrencyConverter("PLN", exRates);
    }

    @Test
    public void convertsToBaseCurrency() {
        assertEquals(sevenPln, sut.convert(twoBucks));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsErrorWhenNoCurrency() {
        sut.convert(twoBitcoins);
    }

    @Test
    public void convertsFromBaseCurrency() {
        assertEquals(twoBucks, sut.convert(sevenPln, "USD"));
    }

    @Test
    public void convertsToBaseCurrencyAnotherWay() {
        assertEquals(sevenPln, sut.convert(twoBucks, "PLN"));
    }

    @Test
    public void convertsBetweenCurrencies() {
        assertEquals(Money.valueOf(1.66, "EUR"), sut.convert(twoBucks, "EUR"));
    }

    @Test
    public void convertsInEdgeCases() {
        assertEquals(twoBucks, sut.convert(twoBucks, "USD"));
        assertEquals(sevenPln, sut.convert(sevenPln, "PLN"));
    }

}
