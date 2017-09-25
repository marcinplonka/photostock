package pl.com.bottega.photostock.sales.model;

public interface PurchaseRepository {

    void save(Purchase purchase);

    Purchase get(String number);

}
