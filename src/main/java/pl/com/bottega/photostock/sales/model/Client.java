package pl.com.bottega.photostock.sales.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Client {

    private String number;
    private String name;
    private Address address;
    private ClientStatus status;
    private List<Transaction> transactions = new LinkedList<>();

    public Client(String name, Address address, ClientStatus status, Money balance) {
        this.name = name;
        this.address = address;
        this.status = status;
        if(balance.gt(Money.ZERO))
            transactions.add(new Transaction(balance, "First charge"));
        this.number = UUID.randomUUID().toString();
    }

    public Client(String name, String number, Address address, ClientStatus status, Money balance) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.status = status;
        if(balance.gt(Money.ZERO))
            transactions.add(new Transaction(balance, "First charge"));
    }

    public Client(String name, Address address) {
        this(name, address, ClientStatus.STANDARD, Money.ZERO);
    }

    public abstract boolean canAfford(Money amount);

    public void charge(Money amount, String reason) {
        if(!canAfford(amount))
            throw new IllegalStateException("Not enough balance");
        transactions.add(new Transaction(amount.neg(), reason));
    }

    public void recharge(Money amount) {
        transactions.add(new Transaction(amount, "Recharge account"));
    }

    public ClientStatus getStatus() {
        return status;
    }

    public int discountPercent() {
        return status.discountPercent();
    }

    public Money balance() {
        Money sum = Money.ZERO;
        for(Transaction tx : transactions)
            sum = sum.add(tx.getAmount());
        return sum;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean hasLogin(String login) {
        return name.equals(login);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        return getNumber().equals(client.getNumber());
    }

    @Override
    public int hashCode() {
        return getNumber().hashCode();
    }
}
