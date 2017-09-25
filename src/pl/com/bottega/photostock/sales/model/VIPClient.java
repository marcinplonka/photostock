package pl.com.bottega.photostock.sales.model;

public class VIPClient extends Client {

    private Money creditLimit;

    public VIPClient(String name, Address address, ClientStatus status, Money balance, Money creditLimit) {
        super(name, address, status, balance);
        this.creditLimit = creditLimit;
    }

    public VIPClient(String name, Address address) {
        this(name, address, ClientStatus.VIP, Money.ZERO, Money.ZERO);
    }

    public boolean canAfford(Money amount) {
        return amount.lte(balance().add(creditLimit));
    }

}
