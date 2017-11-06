package pl.com.bottega.photostock.sales.model;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private final String mainCurrency;
    private final Map<String, Double> exRates;

    public CurrencyConverter(String mainCurrency, Map<String, Double> exRates) {
        this.mainCurrency = mainCurrency;
        this.exRates = new HashMap<>(exRates);
    }

    public Money convert(Money amount) {
        if(mainCurrency.equals(amount.currency()))
            return amount;
        return amount.convert(mainCurrency, exRate(amount.currency()));
    }

    private Double exRate(String currency) {
        if(!exRates.containsKey(currency))
            throw new IllegalArgumentException("No ex rate for " + currency);
        return exRates.get(currency);
    }

    public Money convert(Money amount, String targetCurrency) {
        if(targetCurrency.equals(mainCurrency))
            return convert(amount);
        if(amount.currency().equals(mainCurrency))
            return amount.convert(targetCurrency, 1/exRate(targetCurrency));
        return convert(convert(amount), targetCurrency);
    }
}
