package pl.com.bottega.photostock.sales.model;

public abstract class AbstractProduct implements Product {
    protected Long number;
    protected Money price;
    protected Boolean active;
    private Client reservedBy;
    private Client owner;

    public AbstractProduct(Money price, Boolean active, Long number) {
        this.price = price;
        this.active = active;
        this.number = number;
    }

    public AbstractProduct(Long number, Money price, Client reservedBy, Client owner, boolean active) {
        this.number = number;
        this.price = price;
        this.reservedBy = reservedBy;
        this.owner = owner;
        this.active = active;
    }

    @Override
    public Money calculatePrice(Client client) {
        return price.percent(100 - client.discountPercent());
    }

    @Override
    public boolean isAvailable() {
        return active && reservedBy == null;
    }

    @Override
    public void reservedPer(Client client) {
        ensureAvailable();
        reservedBy = client;
    }

    @Override
    public void unreservedPer(Client client) {
        if(owner != null)
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
        owner = client;
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
}
