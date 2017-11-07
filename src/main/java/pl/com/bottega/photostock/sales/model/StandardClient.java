package pl.com.bottega.photostock.sales.model;

import javax.persistence.Entity;

@Entity
public class StandardClient extends Client {

    public StandardClient(String name, Address address, ClientStatus status, Money balance) {
        super(name, address, status, balance, login);
    }

    public StandardClient(String name, Address address) {
        super(name, address, login);
    }

    public StandardClient(String number, String name, Address address, ClientStatus status, Money balance) {
        super(number, name, address, status, balance, login);
    }

    public StandardClient() {
    }

    public boolean canAfford(Money amount) {
        return amount.lte(balance());
    }

}
