package pl.com.bottega.photostock.sales.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class Purchase {

    private Collection<Product> items;
    private Client buyer;
    private LocalDateTime purchaseDate = LocalDateTime.now();
    private String number;

    public Purchase(Client buyer, Collection<Product> items) {
        this.number = UUID.randomUUID().toString();
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
