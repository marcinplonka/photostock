package pl.com.bottega.photostock.sales.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

@Entity
public class Purchase {
    @Id
    @GeneratedValue
    private String number;
    @OneToMany
    private Collection<Product> items;
    @OneToOne
    private Client buyer;
    private LocalDateTime purchaseDate = LocalDateTime.now();

    public Purchase() {
    }

    public Purchase(Client buyer, Collection<Product> items) {
        this.buyer = buyer;
        this.items = new LinkedList<>(items);
        for (Product product : items)
            product.soldPer(buyer);
    }

    public Purchase(String number, Collection<Product> items, Client buyer, LocalDateTime purchaseDate) {
        this.number = number;
        this.items = items;
        this.buyer = buyer;
        this.purchaseDate = purchaseDate;
    }

    public String getNumber() {
        return number;
    }

    public Collection<Product> getItems() {
        return items;
    }

    public Client getBuyer() {
        return buyer;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }
}
