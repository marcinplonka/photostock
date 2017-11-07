package pl.com.bottega.photostock.sales.model;

import javax.persistence.Entity;

@Entity
public class VIPClient extends Client {

    private Long creditLimitCents;
    private String creditLimitCurrency;

    public VIPClient(String name, Address address, ClientStatus status, Money balance, Money creditLimit) {
        super(name, address, status, balance, login);
        this.creditLimitCents = creditLimit.value();
        this.creditLimitCurrency = creditLimit.currency();
    }

    public VIPClient(String name, Address address) {
        this(name, address, ClientStatus.VIP, Money.ZERO, Money.ZERO);
    }

    public VIPClient(String name, String number, Address address, ClientStatus status, Money balance, Money creditLimit) {
        super(name, number, address, status, balance, login);
        this.creditLimitCents = creditLimit.value();
        this.creditLimitCurrency = creditLimit.currency();
    }

    public VIPClient() {
    }


    public boolean canAfford(Money amount) {
        return amount.lte(balance().add(getCreditLimit()));
    }

    public Money getCreditLimit() {
        return Money.valueOf(creditLimitCents, creditLimitCurrency);
    }
}
