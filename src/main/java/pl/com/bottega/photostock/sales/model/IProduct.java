package pl.com.bottega.photostock.sales.model;

public interface IProduct {
    Money calculatePrice(Client client);

    boolean isAvailable(Client client);

    void reservedPer(Client client);

    void unreservedPer(Client client);

    void soldPer(Client client);

    Long getNumber();

    default void ensureAvailable(Client client) {
        if (!isAvailable(client))
            throw new ProductNotAvailableException(this);
    }
}
