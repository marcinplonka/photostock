package pl.com.bottega.photostock.sales.model;

import javax.persistence.Entity;

@Entity
public class StandardClient extends Client {

    public StandardClient(String name, Address address, ClientStatus status, Money balance, String login) {
        super(name, address, status, balance);
    }

    public StandardClient(String name, String number, Address address, ClientStatus status, Money balance) {
        super(name, address, status, balance);
    }

    public StandardClient() {
    }

    public boolean canAfford(Money amount) {
        return amount.lte(balance());
    }

}
