package pl.com.bottega.photostock.sales.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoneyTest {

    private Money fiftyCredit = Money.valueOf(50);
    private Money seventyCredit = Money.valueOf(70);
    private Money fiftyEur = Money.valueOf(50, "EUR");

    @Test
    public void shouldAddMoney() {
        // when
        Money sum = fiftyCredit.add(seventyCredit);

        // then
        Money expected = Money.valueOf(120);
        assertEquals(expected, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAddMoneyInDifferentCurrencies() {
        // when
        fiftyEur.add(seventyCredit);
    }

    @Test
    public void shouldSubtractMoney() {
        // when
        Money dif = fiftyCredit.sub(seventyCredit);

        // then
        Money expected = Money.valueOf(-20);
        assertEquals(expected, dif);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSubtractMoneyInDifferentCurrencies() {
        // when
        fiftyEur.sub(seventyCredit);
    }

    @Test
    public void shouldCompareMoney() {
        assertTrue(fiftyCredit.compareTo(seventyCredit) < 0);
        assertTrue(seventyCredit.compareTo(fiftyCredit) > 0);
        assertTrue(fiftyCredit.compareTo(fiftyCredit) == 0);
    }

    @Test
    public void shouldCompareMoneyUsingBooleanMethods() {
        assertTrue(fiftyCredit.lt(seventyCredit));
        assertTrue(fiftyCredit.lte(seventyCredit));
        assertTrue(seventyCredit.gt(fiftyCredit));
        assertTrue(seventyCredit.gte(fiftyCredit));
        assertFalse(fiftyCredit.gt(seventyCredit));
        assertFalse(fiftyCredit.gte(seventyCredit));
        assertFalse(seventyCredit.lt(fiftyCredit));
        assertFalse(seventyCredit.lte(fiftyCredit));
        assertTrue(fiftyCredit.gte(fiftyCredit));
        assertTrue(fiftyCredit.lte(fiftyCredit));
        assertFalse(fiftyCredit.lt(fiftyCredit));
        assertFalse(fiftyCredit.gt(fiftyCredit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCompareDifferentCurrencies() {
        fiftyCredit.compareTo(fiftyEur);
    }

    @Test
    public void shouldCalculatePercent() {
        assertEquals(Money.valueOf(5), fiftyCredit.percent(10));
        assertEquals(Money.valueOf(5.50), fiftyCredit.percent(11));
        assertEquals(Money.valueOf(75), fiftyCredit.percent(150));
        assertEquals(Money.valueOf(0.01), Money.valueOf(0.11).percent(10));
        assertEquals(Money.valueOf(0.01), Money.valueOf(0.19).percent(10));
    }

    @Test
    public void shouldConvertCurrencies() {
        assertEquals(Money.valueOf(3.5, "PLN"), Money.valueOf(1, "USD").convert("PLN", 3.5));
    }

}
