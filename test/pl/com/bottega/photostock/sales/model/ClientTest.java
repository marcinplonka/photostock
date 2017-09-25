package pl.com.bottega.photostock.sales.model;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private Address address = new Address("ul. Północna 11", "Poland", "Lublin", "20-001");
    private Client clientWithCredit = new VIPClient("test",
            address,
            ClientStatus.VIP,
            Money.valueOf(100),
            Money.valueOf(100));

    private Client clientWithNoMoney = new VIPClient("test", address);

    @Test
    public void shouldCheckIfClientCanAfford() {
        // when
        clientWithNoMoney.recharge(Money.valueOf(100));

        // then
        assertTrue(clientWithNoMoney.canAfford(Money.valueOf(100)));
        assertTrue(clientWithNoMoney.canAfford(Money.valueOf(50)));
        assertFalse(clientWithNoMoney.canAfford(Money.valueOf(101)));
    }

    @Test
    public void shouldCheckIfClientCanAffordWithCredit() {
        //then
        assertTrue(clientWithCredit.canAfford(Money.valueOf(200)));
        assertFalse(clientWithCredit.canAfford(Money.valueOf(201)));
    }

    @Test
    public void shouldChargeAndRechargeClient() {
        // when
        clientWithCredit.charge(Money.valueOf(200), "Testowy zakup");
        clientWithCredit.recharge(Money.valueOf(100));

        //then
        assertTrue(clientWithCredit.canAfford(Money.valueOf(100)));
        assertFalse(clientWithCredit.canAfford(Money.valueOf(101)));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAllowToChargeMoreThanCanAfford() {
        clientWithCredit.charge(Money.valueOf(50), "Testowy zakup");
        clientWithCredit.charge(Money.valueOf(100), "Testowy zakup");
        clientWithCredit.charge(Money.valueOf(100), "Testowy zakup");
    }

}
