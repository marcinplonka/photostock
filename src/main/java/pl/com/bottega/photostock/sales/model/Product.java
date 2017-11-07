package pl.com.bottega.photostock.sales.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public abstract class Product implements IProduct, Serializable {
    @Id
    @GeneratedValue
    @Column(name = "product_number")
    Long number;
    @Column(name = "price_amount")
    private Long priceAmount;

    private String priceCurrency;
    private Boolean active;
    @ManyToOne(targetEntity = Client.class)
    private Client reservedBy;
    @ManyToOne(targetEntity = Client.class)
    private Client ownerId;

    public Product(Money price, Boolean active) {
        this.priceAmount = price.value();
        this.priceCurrency = price.currency();
        this.active = active;
    }

    public Product(Money price, Client reservedBy, Client owner, boolean active) {
        this.priceAmount = price.value();
        this.priceCurrency = price.currency();
        this.reservedBy = reservedBy;
        this.ownerId = owner;
        this.active = active;
    }

    protected Product() {
    }

    @Override
    public Money calculatePrice(Client client) {
        return getPrice().percent(100 - client.discountPercent());
    }

    @Override
    public boolean isAvailable(Client client) {
        return active && (reservedBy == null || reservedBy.equals(client));
    }

    @Override
    public void reservedPer(Client client) {
        ensureAvailable(client);
        reservedBy = client;
    }

    @Override
    public void unreservedPer(Client client) {
        if(ownerId != null)
            throw new IllegalStateException("Product is already purchased");
        checkReservation(client);
        reservedBy = null;
    }

    private void checkReservation(Client client) {
        if (reservedBy == null || !reservedBy.equals(client))
            throw new IllegalStateException(String.format("Product is not reserved by %s", client));
    }

    @Override
    public void soldPer(Client client) {
        checkReservation(client);
        ownerId = client;
        active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        return number.equals(picture.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public Long getNumber() {
        return number;
    }

    public Money getPrice() {
        return Money.valueOf(priceAmount, priceCurrency);
    }

    public Boolean isActive() {
        return active;
    }

    public String getReservedByNumber() {
        if (reservedBy != null) {
            return reservedBy.getNumber();
        } else {
            return "null";
        }
    }

    public String getOwnerNumber() {
        if (ownerId != null) {
            return ownerId.getNumber();
        } else {
            return "null";
        }
    }
}
