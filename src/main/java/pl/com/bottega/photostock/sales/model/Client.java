package pl.com.bottega.photostock.sales.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
@Entity
public abstract class Client implements Serializable {
    @Id
    private String number;
    @Column(unique = true)
    private String name;
    @OneToOne
    private Address address;
    private ClientStatus status;
    @OneToMany
    private List<Transaction> transactions = new LinkedList<>();
    @OneToMany
    private List<LightBox> lightBoxes;

    public Client(String name, Address address, ClientStatus status, Money balance) {
        this.name = name;
        this.address = address;
        this.status = status;
        if(balance.gt(Money.ZERO))
            transactions.add(new Transaction(balance, "First charge"));
        this.number = UUID.randomUUID().toString();
    }

    public Client(String name, Address address) {
        this(name, address, ClientStatus.STANDARD, Money.ZERO);
    }

    public Client() {
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



    public List<LightBox> getLightBoxes() {
        return lightBoxes;
    }
}
