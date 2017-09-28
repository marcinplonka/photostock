package pl.com.bottega.photostock.sales.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class Reservation {

    private Client owner;
    private Collection<Product> items = new LinkedList<>();
    private String number;

    public Reservation(Client owner) {
        this.number = UUID.randomUUID().toString();
        this.owner = owner;
    }

    public Reservation(String number, Client owner, Collection<Product> items) {
        this.number = number;
        this.owner = owner;
        this.items = items;
    }

    public void add(Product product) {
        product.ensureAvailable(owner);

        items.add(product);
        product.reservedPer(owner);
    }

    public void remove(Product product) {
        if (items.remove(product))
            product.unreservedPer(owner);
        else
            throw new IllegalArgumentException("Product is not part of this reservation");
    }

    public Offer generateOffer() {
        return new Offer(owner, items);
    }

    public int getItemsCount() {
        return items.size();
    }

    public String getNumber() {
        return number;
    }

    public Client getOwner() {
        return owner;
    }

    public Collection<Product> getItems() {
        return items;
    }
}
