package pl.com.bottega.photostock.sales.model;

public class StandardClient extends Client {

    public StandardClient(String name, Address address, ClientStatus status, Money balance) {
        super(name, address, status, balance);
    }

    public StandardClient(String name, Address address) {
        super(name, address);
    }

    public boolean canAfford(Money amount) {
        return amount.lte(balance());
    }

}
